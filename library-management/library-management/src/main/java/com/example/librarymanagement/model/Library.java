import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Library {
    private static final Logger LOGGER = Logger.getLogger(Library.class.getName());

    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, isbn, available) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            conn.setAutoCommit(false);
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, book.getTitle());
                statement.setString(2, book.getAuthor());
                statement.setString(3, book.getIsbn());
                statement.setBoolean(4, book.isAvailable());
                statement.executeUpdate();
                conn.commit();
            } catch (SQLException e) {
                conn.rollback();
                LOGGER.log(Level.SEVERE, "Error adding book: ", e);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection error: ", e);
        }
    }

    public void removeBook(String isbn) {
        String sql = "DELETE FROM books WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, isbn);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error removing book: ", e);
        }
    }

    public boolean borrowBook(String isbn, User user) {
        String checkSql = "SELECT available FROM books WHERE isbn = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, isbn);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getBoolean("available")) {
                String updateSql = "UPDATE books SET available = false WHERE isbn = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, isbn);
                    updateStmt.executeUpdate();
                }

                String insertSql = "INSERT INTO borrowed_books (user_id, isbn) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setInt(1, user.getUserId());
                    insertStmt.setString(2, isbn);
                    insertStmt.executeUpdate();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error borrowing book: ", e);
            return false;
        }
    }

    public boolean returnBook(String isbn, User user) {
        String checkSql = "SELECT * FROM borrowed_books WHERE user_id = ? AND isbn = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, user.getUserId());
            checkStmt.setString(2, isbn);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String deleteSql = "DELETE FROM borrowed_books WHERE user_id = ? AND isbn = ?";
                try (PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {
                    deleteStmt.setInt(1, user.getUserId());
                    deleteStmt.setString(2, isbn);
                    deleteStmt.executeUpdate();
                }

                String updateSql = "UPDATE books SET available = true WHERE isbn = ?";
                try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                    updateStmt.setString(1, isbn);
                    updateStmt.executeUpdate();
                }
                return true;
            }
            return false;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error returning book: ", e);
            return false;
        }
    }

    public List<Book> getBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("title");
                String author = rs.getString("author");
                String isbn = rs.getString("isbn");
                boolean available = rs.getBoolean("available");
                books.add(new Book(title, author, isbn, available));
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving books: ", e);
        }
        return books;
    }

    public boolean loginUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error logging in user: ", e);
            return false;
        }
    }

    public boolean registerUser(String username, String password) {
        String checkSql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setString(1, username);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false;
            } else {
                String insertSql = "INSERT INTO users (username, password) VALUES (?, ?)";
                try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                    insertStmt.setString(1, username);
                    insertStmt.setString(2, password);
                    insertStmt.executeUpdate();
                    return true;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error registering user: ", e);
            return false;
        }
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection()) {
            assert conn != null;
            try (PreparedStatement statement = conn.prepareStatement(sql)) {
                statement.setString(1, username);
                ResultSet rs = statement.executeQuery();

                if (rs.next()) {
                    int userId = rs.getInt("id");
                    return new User(userId, username);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error retrieving user by username: ", e);
        }
        return null;
    }
}
