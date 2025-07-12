# ☕ Coffee Journal - 咖啡筆記應用

一個現代化的咖啡筆記管理系統，採用微服務架構設計，讓咖啡愛好者能夠記錄和管理他們的咖啡品嚐體驗。

## 🎯 專案概覽

Coffee Journal 是一個分離式架構的 Web 應用程式，將前端、認證與後端資料處理完全分離：

- **前端應用**: Vue.js 3 + TypeScript + Tailwind CSS
- **認證服務**: Laravel + JWT 簽發
- **後端服務**: Spring Boot + PostgreSQL
- **認證機制**: JWT Token 跨服務認證

## 🏗️ 專案架構

### 微服務架構設計
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Vue.js 前端   │    │   Laravel 認證   │    │   Java 後端     │
│   (獨立專案)    │────│   (JWT 簽發)    │────│   (CRUD API)    │
│   TypeScript    │    │   使用者管理     │    │   Spring Boot   │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   PostgreSQL    │
                    │   資料庫        │
                    └─────────────────┘
```

### 專案結構
```
~/coffee-journal/
├── backend-laravel/           # Laravel 認證服務
│   ├── app/Http/Controllers/  # 認證控制器
│   ├── app/Models/           # 用戶模型
│   ├── config/               # JWT 配置
│   └── routes/api.php        # 認證 API 路由
├── backend-java/             # Spring Boot API 服務
│   ├── src/main/java/        # Java 源碼
│   │   └── com/coffeenote/api/
│   │       ├── model/        # 資料模型
│   │       ├── repository/   # 資料存取層
│   │       ├── service/      # 業務邏輯層
│   │       ├── controller/   # REST API 控制器
│   │       ├── filter/       # JWT 認證過濾器
│   │       └── util/         # JWT 工具類
│   └── build.gradle          # Gradle 建構配置
├── frontend-vue/             # Vue.js 前端應用
│   ├── src/views/           # 頁面組件
│   ├── src/stores/          # Pinia 狀態管理
│   ├── src/services/        # API 服務
│   └── src/router/          # 路由配置
├── Makefile                 # 開發工具指令
└── README.md                # 專案說明文檔
```

## 🚀 技術棧

### 前端應用 (Vue.js)
- **Vue.js 3**: 現代化前端框架，提供響應式使用者介面
- **TypeScript**: 類型安全的 JavaScript 超集
- **Tailwind CSS**: 實用優先的 CSS 框架
- **Pinia**: Vue.js 狀態管理庫
- **Vue Router**: 前端路由管理
- **Axios**: HTTP 客戶端庫
- **Vite**: 快速的前端建構工具

### 認證服務 (Laravel)
- **Laravel 11**: PHP 後端框架，專門處理使用者認證
- **php-open-source-saver/jwt-auth**: JWT Token 簽發和管理

### 後端 API 服務 (Spring Boot)
- **Spring Boot 3**: Java 企業級應用框架
- **Spring Security**: 安全框架，提供 JWT 認證
- **Spring Data JPA**: 資料持久化框架
- **PostgreSQL**: 生產環境資料庫
- **H2 Database**: 開發測試用記憶體資料庫

### JWT 認證流程
1. **Laravel**: 負責使用者註冊、登入，簽發 JWT Token
2. **Vue.js**: 儲存 Token，在 API 請求中攜帶
3. **Spring Boot**: 驗證 JWT Token，提供受保護的 API

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
- Java 17+
- PHP 8.2+
- Node.js 18+
- Composer
- Make（可選，用於簡化指令）

### 本地開發環境
```bash
# 克隆專案
git clone <repository-url>
cd coffee-journal

# 安裝依賴
make install

# 啟動開發環境
make dev

# 或分別啟動服務
make dev-vue      # Vue.js 前端 (端口 5173)
make dev-laravel  # Laravel 認證 (端口 8000)
make dev-java     # Java 後端 (端口 8080)
```

### 常用指令
```bash
# 查看所有可用指令
make help

# 執行測試
make test

# 僅測試 Java 後端
make test-java

# 更新依賴
make update
```

### 訪問應用
- **Vue.js 前端**: http://localhost:5173
- **Laravel 認證**: http://localhost:8000
- **Java API**: http://localhost:8080
- **健康檢查**: http://localhost:8080/api/health
- **H2 控制台**: http://localhost:8080/h2-console

### 🔐 認證流程
1. 在 Vue.js 前端註冊/登入
2. Laravel 簽發 JWT Token
3. Vue.js 使用 Token 調用 Java API
4. Java 驗證 Token 並提供數據

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

## 📚 詳細文檔

- **[Vue 前端文檔](frontend-vue/README.md)** - Vue.js 前端架構和開發指南
- **[Laravel 認證文檔](backend-laravel/README.md)** - Laravel 認證服務說明
- **[Java 後端文檔](backend-java/README.md)** - Spring Boot API 服務說明
- **[Vue 風格指南](frontend-vue/STYLEGUIDE.md)** - 前端設計系統和樣式規範

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
