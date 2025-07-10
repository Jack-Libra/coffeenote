# ☕ Coffee Journal - 咖啡筆記應用

一個現代化的咖啡筆記管理系統，採用微服務架構設計，讓咖啡愛好者能夠記錄和管理他們的咖啡品嚐體驗。

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

### 🔐 使用者認證
- JWT Token 認證機制
- 安全的密碼雜湊
- Token 刷新和驗證

### 📝 咖啡筆記管理
- **創建筆記**: 記錄咖啡豆資訊、烘焙程度、風味描述等
- **編輯筆記**: 修改現有筆記內容
- **刪除筆記**: 移除不需要的筆記
- **搜尋功能**: 根據關鍵字搜尋筆記
- **分頁瀏覽**: 支援大量筆記的分頁顯示

### 📊 統計分析
- 筆記總數統計
- 平均評分計算
- 評分分佈分析
- 最近筆記查看

## 🛠️ 快速開始

### 前置需求
- Docker 和 Docker Compose（生產部署）
- Java 17+ (本地開發)
- Node.js 18+ (本地開發)
- PHP 8.2+ (本地開發)

### 🚀 一鍵部署（推薦）

```bash
# 克隆專案
git clone <repository-url>
cd coffee-journal

# 使用 Docker 部署
./deploy.sh
```

### 🛠️ 開發環境

```bash
# 啟動開發環境（自動啟動所有服務）
./dev-start.sh
```

### 手動啟動

#### 使用 Docker
```bash
cd docker
docker-compose up -d
```

#### 本地開發
```bash
# 啟動 Java 後端
cd backend-java
./gradlew bootRun

# 啟動 Laravel 前端（新終端）
cd backend-laravel
composer install
npm install
npm run dev
php artisan serve
```

### 訪問應用
- 前端應用: http://localhost:8000
- Java API: http://localhost:8080
- API 健康檢查: http://localhost:8080/api/health
- H2 控制台: http://localhost:8080/h2-console

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

### 認證端點
- `POST /api/auth/login` - 使用者登入
- `POST /api/auth/refresh` - 刷新 Token
- `GET /api/auth/validate` - 驗證 Token
- `POST /api/auth/logout` - 使用者登出

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

## 🙏 致謝

感謝所有為這個專案做出貢獻的開發者和咖啡愛好者！
