# TrainScheduler 火車時刻表排程系統

一個使用 Java 實作的火車時刻表與排程管理系統，支援查詢列車班次、時刻表排序等功能。此專案旨在展示 Java 在物件導向、檔案處理與 GUI 方面的實力，並可作為期末專案、學術作業或個人練習使用。

---

## 🧩 功能特色

### ✅ 基本功能
- 新增火車班次（班次編號、車種、停靠站與時刻）
- 查詢起訖站之間的可搭乘班次
- 根據發車時間排序列車

### 🖥️ GUI（進階）
- 使用 JavaFX 建立圖形介面（可選擇 Console 版）
- 顯示時刻表、搜尋欄、輸入欄位等元件

---

## 🧱 技術架構

| 技術/工具 | 說明 |
|------------|----------------|
| Java | 核心程式語言 |
| JavaFX | GUI（可選） |
| OOP | 類別：Train, Stop, Schedule, TicketSystem |
| Java I/O | 讀寫 CSV / Text 資料 |
| MySQL | 資料庫 |
| Collection | 使用 List / Map 管理列車資料 |

---

## 📂 專案結構

```
TrainScheduler/
├── src/
│   ├── Main.java                   ← 程式主入口
│
│   ├── controller/                 ← 控制邏輯層（處理使用者輸入）
│   │   └── TrainController.java
│
│   ├── model/                      ← 資料與邏輯模型（物件＋演算法）
│   │   ├── Train.java
│   │   ├── Stop.java
│   │   ├── Schedule.java
│   │   └── Ticket.java
│
│   ├── dao/                        ← 資料存取層（連接 DB 或檔案）
│   │   ├── TrainDAO.java
│   │   └── DatabaseConnector.java
│
│   ├── view/                       ← 輸出畫面層（可選 GUI 或 Console）
│   │   ├── ConsoleUI.java
│   │   └── GUI.java (optional)
│
│   └── utils/                      ← 工具類別
│       └── TimeParser.java
│
├── data/
│   └── trains.csv
├── db/
│   └── schema.sql
└── README.md
```

---

|  層次   | 負責內容  |
|  ----  | ----  |
| Model  | 定義資料結構與業務邏輯，處理計算與驗證 |
| View  | 顯示資料，接收使用者輸入，向 Controller 傳遞請求 |
| Controller  | 接收使用者請求，調用 Model 層處理邏輯，將結果傳給 View 層 |
| DAO  | 負責資料存取，將資料與外部資料源進行交互（例如資料庫） |

## 🚀 執行方式

### ✅ Console 版本
```bash
javac -d bin src/**/*.java
java -cp bin Main
```

### ✅ JavaFX GUI 版本（若有）
請先確認已安裝 JavaFX 並設定 VM Options：
```
--module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

---

## 📌 未來擴充功能（建議）

- 模擬月台排程衝突檢查
- 路線轉乘建議與最短時間演算法（如 Dijkstra）
- 圖形化時刻表顯示（時間軸 + 車站軸）
- 匯出 PDF 或 HTML 報表
- 查詢座位可用狀況
- 模擬乘客訂票與取消票
- 支援多執行緒同時訂票（展示 Java Thread 與同步機制）
---

## 👨‍💻 作者資訊

- 專案作者：謝珷兆、方穆涵、陳澤諒、周羿辰
- 使用技術：Java, JavaFX, OOP, Thread, File I/O
- 學術用途：期末專題 / 課堂展示 / 個人練習

---

📫 有任何建議或想要參與開發，歡迎聯絡或發 PR 🙌
