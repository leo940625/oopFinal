# TrainScheduler 火車時刻表排程系統

一個使用 Java 實作的火車時刻表與排程管理系統，支援查詢列車班次、時刻表排序等功能。此專案旨在展示 Java 在物件導向、檔案處理與 GUI 方面的實力，作為期末專案使用。

---

## 🧩 功能特色

### ✅ 基本功能
- 新增火車班次（班次編號、車種、停靠站與時刻）
- 查詢起訖站之間的可搭乘班次
- 根據發車時間排序列車

### 🖥️ GUI（進階）
- 使用 JavaSwing 建立圖形介面
- 顯示時刻表、搜尋欄、輸入欄位等元件

---

## 🧱 技術架構

| 技術/工具 | 說明 |
|------------|----------------|
| Java | 核心程式語言 |
| JavaSwing | GUI|
| OOP | 類別：Train, StopTime, Station, Blocksection |
| MySQL | 資料庫 |

---

## 📂 專案結構

```
├── src/
│   ├── Main.java                         ← 程式主入口
│
│   ├── ui/                               ← 使用者介面主目錄（模組化 GUI）
│   │   ├── addtrain/                     ← 新增車次模組
│   │   │   ├── AddTrainFrame.java
│   │   │   └── login/                    ← 登入流程模組（僅供新增車次使用）
│   │   │       ├── LoginChoiceFrame.java
│   │   │       └── EmployeeLoginFrame.java
│   │   │
│   │   ├── ticketdownload/              ← 票務下載模組
│   │   │   ├── TicketPreviewFrame.java
│   │   │   ├── TicketPanel.java
│   │   │   └── TicketInputFrame.java
│   │   │
│   │   ├── searchtrain/                 ← 列車查詢模組（尚未實作）
│   │   │
│   │   ├── contact/                     ← 聯絡資訊模組
│   │   │   └── BannerPanel.java
│   │   │
│   │   ├── HomeFrame.java               ← 主畫面
│   │   ├── BackgroundPanel.java         ← 共用背景面板
│   │   └── GradientPanel.java           ← 共用漸層面板
│
│   ├── controller/                      ← 控制邏輯層（MVC 中 C）
│   │   └── TrainController.java
│
│   ├── model/                           ← 資料與邏輯模型（MVC 中 M）
│   │   ├── Train.java                   ← 車次資訊
│   │   ├── Stop.java                    ← 停靠站資訊
│   │   ├── Schedule.java                ← 排程邏輯與時間運算
│   │   └── Ticket.java                  ← 車票邏輯（如有訂票功能）
│
│   ├── dao/                             ← 資料存取層（資料庫／檔案操作）
│   │   ├── TrainDAO.java
│   │   ├── TrainDAOImpl.java
│   │   ├── StationDAO.java
│   │   └── StationDAOImpl.java
│
│   ├── view/                            ← UI 呈現層（文字或圖形介面）
│   │   ├── ConsoleUI.java               ← 命令列工具（可選）
│   │   └── GUI.java                     ← Swing GUI 簡化入口（可選）
│
│   └── utils/                           ← 工具與公用元件
│       └── DBConnection.java
│
├── data/                                ← 外部靜態資料（如匯入匯出用）
│   └── trains.csv                       ← 列車初始資料（可選）
│
├── db/                                  ← SQL 建表與初始化資料
│   └── schema.sql                       ← 建立資料表與預設資料
│
├── resources/                           ← UI 所需圖片資源（logo、背景圖等）
│
└── README.md                            ← 專案說明文件
```

---

|  層次   | 負責內容  |
|  ----  | ----  |
| Model  | 定義資料結構與業務邏輯，處理計算與驗證 |
| View  | 顯示資料，接收使用者輸入，向 Controller 傳遞請求 |
| Controller  | 接收使用者請求，調用 Model 層處理邏輯，將結果傳給 View 層 |
| dao  | 負責資料存取，將資料與外部資料源進行交互（例如資料庫） |

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
- 使用技術：Java, JavaSwing, OOP, Netbeam, MySQL
- 學術用途：期末專題

---

