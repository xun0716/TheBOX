# 圖書館管理系統
這個系統是一個基於 Java 和 MySQL 的圖書館管理系統，設計來簡化圖書館的日常運營，系統的主要特色和功能介紹：

![image](https://github.com/user-attachments/assets/f8e4e81e-83f3-474a-955a-d63933d31fa1)

 
## 1.	使用者註冊與登入功能
   ![image](https://github.com/user-attachments/assets/b43e60b0-3cba-4bc1-ae37-79784444bee4)

 
- 系統支援使用者註冊和登入功能，並且在登入後會顯示使用者名稱，提供更個性化的使用體驗。
- 使用者可以通過註冊功能創建帳戶，登入後可以進行借書和還書等操作。
- 
## 2. 書籍管理功能

![image](https://github.com/user-attachments/assets/b938b696-b97e-411b-b9c9-a47b0da9af73)
![image](https://github.com/user-attachments/assets/89ca536b-ee81-42d4-9f5c-8ec61d74661a)
![image](https://github.com/user-attachments/assets/3b7504de-8115-4c1b-b666-b6f62dff6eb4)

- **添加書籍**：管理員可以通過 GUI 介面或者程式碼添加書籍，書籍資料會同時存儲在本地記憶體和 MySQL 資料庫中。
- **刪除書籍**：系統允許管理員根據書籍的 ISBN 號刪除書籍，從本地列表和資料庫中移除。
- **查看書籍列表**：使用者或管理員可以通過系統查看所有可用書籍，這些書籍資料是從資料庫中動態加載的。

## 3.	借閱與還書功能
 
![image](https://github.com/user-attachments/assets/77a6739e-d232-4b97-818a-bb1c348180df)

- 使用者可以根據書籍的 ISBN 號進行借書操作，系統會檢查書籍的可用性並在成功借閱後標記該書不可用。
- 還書功能可以讓使用者歸還書籍，系統會更新書籍的狀態，使其重新可用。
  
## 4. 資料庫整合
- 系統與 MySQL 資料庫整合，所有書籍資料都存儲在 MySQL 中，確保數據的持久性。
- 資料庫操作如添加、刪除和查詢都通過 `DatabaseConnection` 類來處理，確保與資料庫的連接穩定。

## 5. 簡易的圖形用戶介面 (GUI)
- 系統提供了一個簡單且易用的 GUI，使用者可以通過圖形介面進行書籍的添加、借閱、還書等操作，提供了友好的用戶體驗。
- 透過彈出對話框（如添加書籍的對話框），用戶可以直觀地操作系統。

## 6. 自動錯誤處理與日誌記錄
- 系統包含基本的錯誤處理機制，例如在資料庫連接失敗時會提示錯誤訊息，確保使用者瞭解操作狀態。

## 7. 跨平臺支援
- 基於 Java 開發，該系統能夠跨平臺運行，無論是在 Windows、Linux 還是 macOS 上都能順利運行。









