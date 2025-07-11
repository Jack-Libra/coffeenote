# ☕ Coffee Journal - 咖啡筆記應用

一個現代化的咖啡筆記管理系統，採用微服務架構設計，讓咖啡愛好者能夠記錄和管理他們的咖啡品嚐體驗。

## 🎯 專案概覽

Coffee Journal 是一個分離式架構的 Web 應用程式，將前端認證與後端資料處理完全分離：

- **前端服務**: Laravel + Vue.js + Inertia.js
- **後端服務**: Spring Boot + PostgreSQL
- **認證機制**: JWT Token 跨服務認證
- **部署方式**: Docker 容器化部署

## 🏗️ 專案架構

```
coffee-journal/
├── backend-laravel/           # Laravel + Vue.js 前端服務
│   ├── app/                   # Laravel 應用程式邏輯
│   ├── resources/js/          # Vue.js 前端代碼
│   ├── routes/                # 路由定義
│   └── Dockerfile             # Laravel 容器化配置
├── backend-java/              # Spring Boot 後端 API 服務
│   ├── src/main/java/         # Java 源碼
│   │   └── com/coffeenote/api/
│   │       ├── model/         # 資料模型
│   │       ├── repository/    # 資料存取層
│   │       ├── service/       # 業務邏輯層
│   │       ├── controller/    # REST API 控制器
│   │       ├── security/      # 安全配置
│   │       └── util/          # 工具類
│   ├── build.gradle           # Gradle 建構配置
│   └── Dockerfile             # Java 容器化配置
├── docker/                    # Docker 編排配置
│   └── docker-compose.yml     # 多服務容器編排
├── deploy.sh                  # 一鍵部署腳本
├── dev-start.sh               # 開發環境啟動腳本
└── README.md                  # 專案說明文檔
```

## 🚀 技術棧

### 前端服務 (Laravel + Vue.js)
- **Laravel 11**: PHP 後端框架，處理使用者認證和前端路由
- **Vue.js 3**: 現代化前端框架，提供響應式使用者介面
- **Inertia.js**: 連接 Laravel 和 Vue.js 的橋樑
- **Tailwind CSS**: 實用優先的 CSS 框架
- **Vite**: 快速的前端建構工具

### 後端 API 服務 (Spring Boot)
- **Spring Boot 3**: Java 企業級應用框架
- **Spring Security**: 安全框架，提供 JWT 認證
- **Spring Data JPA**: 資料持久化框架
- **PostgreSQL**: 關聯式資料庫
- **JWT**: 無狀態認證機制

### 基礎設施
- **Docker**: 容器化部署
- **Docker Compose**: 多服務編排
- **Nginx**: 反向代理和負載均衡

## 📋 功能特色

### 🔐 認證架構
- **Laravel**: 負責使用者註冊、登入、會話管理
- **JWT Token**: Laravel 簽發 JWT，Java 後端驗證
- **無狀態認證**: 前端使用 JWT 調用 Java API
- **Token 管理**: 自動刷新和驗證機制

### 📝 咖啡筆記管理（Java 後端）
- **創建筆記**: 記錄咖啡豆資訊、烘焙程度、風味描述等
- **編輯筆記**: 修改現有筆記內容
- **刪除筆記**: 移除不需要的筆記
- **搜尋功能**: 根據關鍵字搜尋筆記
- **分頁瀏覽**: 支援大量筆記的分頁顯示
- **使用者隔離**: 每個使用者只能存取自己的筆記

### 📊 統計分析
- 筆記總數統計
- 平均評分計算
- 評分分佈分析
- 最近筆記查看

## 🚀 快速開始

### 前置需求
- Docker 和 Docker Compose（推薦）
- Make（可選，用於簡化指令）

### 一鍵部署
```bash
# 克隆專案
git clone <repository-url>
cd coffee-journal

# 使用 Make 指令（推薦）
make up

# 或使用傳統方式
./deploy.sh
```

### 常用指令
```bash
# 查看所有可用指令
make help

# 啟動服務
make up

# 查看服務狀態
make status

# 查看日誌
make logs

# 停止服務
make down

# 執行測試
make test
```

### 訪問應用
- **前端應用**: http://localhost:8000
- **Java API**: http://localhost:8080
- **API 文檔**: http://localhost:8080/swagger-ui.html
- **健康檢查**: http://localhost:8080/api/health

## 🔧 配置說明

### 環境變數

#### Java 後端 (application.properties)
```properties
# 資料庫配置
spring.datasource.url=jdbc:postgresql://localhost:5432/coffeenote
spring.datasource.username=postgres
spring.datasource.password=your_password

# JWT 配置
jwt.secret=your_jwt_secret_key
jwt.expiration=86400000
```

#### Laravel 前端 (.env)
```env
# 應用配置
APP_URL=http://localhost:8000
APP_ENV=local

# 資料庫配置
DB_CONNECTION=pgsql
DB_HOST=localhost
DB_PORT=5432
DB_DATABASE=coffeenote

# Java API 配置
JAVA_API_URL=http://localhost:8080
```

## 📡 API 文檔

### Laravel 認證端點
- `POST /login` - 使用者登入（Breeze）
- `POST /register` - 使用者註冊（Breeze）
- `POST /logout` - 使用者登出（Breeze）
- `GET /api/user` - 獲取當前使用者資訊
- `POST /api/jwt/token` - 生成 JWT Token
- `POST /api/jwt/verify` - 驗證 JWT Token
- `POST /api/jwt/refresh` - 刷新 JWT Token

### Java 後端認證端點
- `POST /api/auth/login` - 使用者登入（測試用）
- `GET /api/auth/validate` - 驗證 Token

### 筆記管理端點
- `GET /api/notes` - 獲取筆記列表（支援分頁）
- `POST /api/notes` - 創建新筆記
- `GET /api/notes/{id}` - 獲取特定筆記
- `PUT /api/notes/{id}` - 更新筆記
- `DELETE /api/notes/{id}` - 刪除筆記
- `GET /api/notes/search` - 搜尋筆記
- `GET /api/notes/stats` - 獲取統計資訊

### 健康檢查端點
- `GET /api/health` - 應用健康狀態
- `GET /api/ping` - 簡單存活檢查

## 🗄️ 資料庫結構

### coffee_notes 表
| 欄位 | 類型 | 說明 |
|------|------|------|
| id | BIGINT | 主鍵，自動遞增 |
| bean_name | VARCHAR(255) | 咖啡豆名稱 |
| origin | VARCHAR(255) | 產地 |
| roast_level | VARCHAR(255) | 烘焙程度 |
| flavor_notes | TEXT | 風味描述 |
| rating | INTEGER | 評分 (1-5) |
| brewing_method | VARCHAR(255) | 沖煮方法 |
| user_id | BIGINT | 使用者 ID |
| created_at | TIMESTAMP | 建立時間 |
| updated_at | TIMESTAMP | 更新時間 |

## 🧪 測試

### 執行 Java 後端測試
```bash
cd backend-java
./gradlew test
```

### 執行 Laravel 前端測試
```bash
cd backend-laravel
php artisan test
```

## 📦 部署

### 生產環境部署
1. 更新 `docker-compose.yml` 中的環境變數
2. 設定 SSL 憑證
3. 配置域名和反向代理
4. 執行部署命令：
   ```bash
   docker-compose -f docker-compose.prod.yml up -d
   ```

## 🤝 貢獻指南

1. Fork 專案
2. 創建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交變更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 開啟 Pull Request

## 📄 授權

本專案採用 MIT 授權 - 詳見 [LICENSE](LICENSE) 文件

## 👥 開發團隊

- **Coffee Journal Team** - 初始開發

## 📚 詳細文檔

- **[Laravel 前端文檔](backend-laravel/README.md)** - 前端架構和開發指南
- **[Java 後端文檔](backend-java/README.md)** - 後端 API 和服務說明
- **[架構設計文檔](backend-laravel/docs/architecture.md)** - 系統架構詳細說明
- **[JWT 認證流程](backend-java/docs/jwt-flow.md)** - 認證機制技術細節
- **[Vue 頁面說明](backend-laravel/resources/js/Pages/README.md)** - 前端頁面組件文檔

## 🤝 貢獻指南

1. Fork 專案
2. 創建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交變更 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 開啟 Pull Request

## 📄 授權

本專案採用 MIT 授權 - 詳見 [LICENSE](LICENSE) 文件

## 👥 開發團隊

- **Coffee Journal Team** - 初始開發

## 🙏 致謝

感謝所有為這個專案做出貢獻的開發者和咖啡愛好者！
