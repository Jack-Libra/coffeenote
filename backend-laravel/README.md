# ☕ Coffee Journal - Laravel 認證服務

Coffee Journal 的 Laravel 後端服務，專門負責使用者認證和 JWT Token 管理。

## 🎯 服務職責

Laravel 應用專注於：

- 🔐 **使用者認證** - 註冊、登入、登出
- 🎫 **JWT Token 管理** - 簽發和驗證 JWT tokens  
- 👤 **使用者管理** - 基本的使用者資料管理
- 🔒 **安全性** - 密碼加密、CORS 設定

## 🏗️ 精簡架構

### 核心依賴 (已優化)
- **Laravel 11** - PHP Web 框架
- **JWT Authentication** - `php-open-source-saver/jwt-auth`
- **SQLite** - 輕量級資料庫


### 結構
```
app/
├── Http/Controllers/
│   ├── AuthController.php           # JWT 認證控制器
│   └── Auth/                        # 基礎認證控制器
├── Models/
│   └── User.php                     # 使用者模型
└── Providers/
    └── AppServiceProvider.php       # 服務提供者

config/
├── auth.php                         # 認證配置
├── cors.php                         # CORS 配置  
├── database.php                     # 資料庫配置
└── jwt.php                          # JWT 配置

routes/
├── api.php                          # API 路由
├── auth.php                         # 認證路由 (精簡版)
└── web.php                          # 基本健康檢查
```

## 🚀 快速開始

### 環境需求
- PHP 8.2+
- Composer

### 安裝步驟

1. **安裝依賴**
   ```bash
   composer install --no-dev
   ```

2. **環境配置**
   ```bash
   cp .env.example .env
   php artisan key:generate
   php artisan jwt:secret
   ```

3. **資料庫設定**
   ```bash
   touch database/database.sqlite
   php artisan migrate
   ```

4. **啟動服務**
   ```bash
   php artisan serve --host=0.0.0.0 --port=8000
   ```

## 📡 API 端點

### 認證相關
```http
POST /api/auth/register    # 使用者註冊
POST /api/auth/login       # 使用者登入
POST /api/auth/logout      # 使用者登出
POST /api/auth/refresh     # 刷新 JWT token
GET  /api/auth/me          # 獲取當前使用者資訊
```

### 健康檢查
```http
GET  /                     # 服務狀態檢查
```

## 🔧 精簡配置

### JWT 配置
- **Token 有效期**: 60 分鐘
- **刷新期限**: 20160 分鐘 (14 天)
- **演算法**: HS256

### CORS 設定
- 允許來源: Vue 前端 (localhost:5173, localhost:5174)
- 允許方法: GET, POST, PUT, DELETE, OPTIONS
- 允許標頭: Authorization, Content-Type, Accept

## 🔄 系統整合

### 與 Vue 前端整合 (port 5174)
```javascript
// 登入獲取 JWT token
const response = await axios.post('http://localhost:8000/api/auth/login', {
  email: 'user@example.com',
  password: 'password'
})
const token = response.data.access_token
```

### 與 Java 後端整合 (port 8080)
- Laravel 簽發 JWT tokens
- Java 後端驗證 Laravel 的 JWT tokens
- 實現跨服務的使用者認證

## 🧪 快速測試

### 健康檢查
```bash
curl http://localhost:8000/
```

### 認證測試
```bash
# 註冊
curl -X POST http://localhost:8000/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"name":"Test","email":"test@example.com","password":"password","password_confirmation":"password"}'

# 登入
curl -X POST http://localhost:8000/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"test@example.com","password":"password"}'
```

## 📦 依賴管理

### 生產依賴
```json
{
  "php": "^8.2",
  "laravel/framework": "^11.31",
  "laravel/tinker": "^2.9",
  "php-open-source-saver/jwt-auth": "^2.8"
}
```

### 開發依賴 
```json
{
  "fakerphp/faker": "^1.23",
  "laravel/pint": "^1.13",
  "mockery/mockery": "^1.6",
  "nunomaduro/collision": "^8.1"
}
```

## 🚨 故障排除

### JWT Secret 未設定
```bash
php artisan jwt:secret
```

### 資料庫問題
```bash
touch database/database.sqlite
php artisan migrate
```

### 清理快取
```bash
php artisan cache:clear
php artisan config:clear
```


## 📄 授權

本專案採用 MIT 授權
