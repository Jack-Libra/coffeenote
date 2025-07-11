# 🎨 Laravel 前端服務

Coffee Journal 的前端服務，負責使用者介面、認證管理和 JWT Token 簽發。

## 🏗️ 技術架構

### 核心技術棧
- **Laravel 11**: PHP 後端框架
- **Vue.js 3**: 前端 JavaScript 框架
- **Inertia.js**: SPA 體驗的橋樑
- **Tailwind CSS**: 實用優先的 CSS 框架
- **Vite**: 現代化前端建構工具
- **Laravel Breeze**: 認證腳手架

### 主要職責
- 🔐 **使用者認證**: 註冊、登入、密碼重設
- 🎫 **JWT 簽發**: 為 Java 後端提供認證 Token
- 🖥️ **前端介面**: Vue.js 單頁應用
- 📱 **響應式設計**: 支援各種設備尺寸

## 📁 目錄結構

```
backend-laravel/
├── app/
│   ├── Http/Controllers/
│   │   ├── Auth/           # Breeze 認證控制器
│   │   ├── JwtController.php # JWT Token 管理
│   │   └── ProfileController.php
│   └── Models/
│       └── User.php        # 使用者模型
├── resources/
│   ├── js/
│   │   ├── Components/     # Vue 組件
│   │   ├── Layouts/        # 頁面佈局
│   │   ├── Pages/          # 頁面組件
│   │   │   ├── Auth/       # 認證相關頁面
│   │   │   ├── Dashboard.vue
│   │   │   ├── Notes.vue   # 咖啡筆記頁面
│   │   │   └── Welcome.vue
│   │   └── app.js          # 主要 JS 入口
│   ├── css/
│   │   └── app.css         # 主要樣式文件
│   └── views/
│       └── app.blade.php   # 主要 Blade 模板
├── routes/
│   ├── web.php             # Web 路由
│   ├── api.php             # API 路由（JWT 相關）
│   └── auth.php            # 認證路由
├── database/
│   └── migrations/         # 資料庫遷移
├── config/
│   ├── cors.php            # CORS 配置
│   └── sanctum.php         # Sanctum 配置
└── docs/                   # 文檔目錄
    └── architecture.md     # 架構文檔
```

## 🚀 開發環境設置

### 前置需求
- PHP 8.2+
- Composer
- Node.js 18+
- npm 或 yarn

### 安裝步驟

1. **安裝 PHP 依賴**
   ```bash
   composer install
   ```

2. **安裝前端依賴**
   ```bash
   npm install
   ```

3. **環境配置**
   ```bash
   cp .env.example .env
   php artisan key:generate
   ```

4. **資料庫設置**
   ```bash
   php artisan migrate
   ```

5. **啟動開發服務器**
   ```bash
   # 後端服務器
   php artisan serve

   # 前端開發服務器（新終端）
   npm run dev
   ```

## 🔧 重要配置

### JWT 配置 (.env)
```env
# JWT 密鑰（必須與 Java 後端一致）
JWT_SECRET=mySecretKey12345678901234567890123456789012345678901234567890

# Java 後端 API 地址
JAVA_API_URL=http://localhost:8080

# Sanctum 配置
SANCTUM_STATEFUL_DOMAINS=localhost,127.0.0.1,127.0.0.1:8000
```

### CORS 配置
```php
// config/cors.php
'allowed_origins' => ['http://localhost:8080'],
'allowed_methods' => ['*'],
'allowed_headers' => ['*'],
```

## 🔐 認證流程

### 1. 使用者註冊/登入
- 使用 Laravel Breeze 提供的認證功能
- 支援 email 驗證和密碼重設

### 2. JWT Token 生成
```javascript
// 前端自動獲取 JWT Token
const token = await fetch('/api/jwt/token', {
  method: 'POST',
  headers: {
    'X-CSRF-TOKEN': csrfToken,
    'Content-Type': 'application/json'
  }
})
```

### 3. 調用 Java API
```javascript
// 使用 JWT Token 調用 Java 後端
const response = await fetch('http://localhost:8080/api/notes', {
  headers: {
    'Authorization': `Bearer ${jwtToken}`,
    'Content-Type': 'application/json'
  }
})
```

## 📡 API 端點

### JWT 管理 API
- `POST /api/jwt/token` - 生成 JWT Token
- `POST /api/jwt/verify` - 驗證 JWT Token
- `POST /api/jwt/refresh` - 刷新 JWT Token
- `GET /api/user` - 獲取當前使用者資訊

### 認證路由（Breeze）
- `GET /login` - 登入頁面
- `POST /login` - 處理登入
- `GET /register` - 註冊頁面
- `POST /register` - 處理註冊
- `POST /logout` - 登出

## 🧪 測試

### 執行測試
```bash
# 執行所有測試
php artisan test

# 執行特定測試
php artisan test --filter=JwtControllerTest
```

### 前端測試
```bash
# 執行 JavaScript 測試
npm run test

# 執行 E2E 測試
npm run test:e2e
```

## 🔧 開發指令

```bash
# 清除快取
php artisan cache:clear
php artisan config:clear
php artisan route:clear

# 查看路由
php artisan route:list

# 資料庫操作
php artisan migrate
php artisan migrate:rollback
php artisan db:seed

# 前端建構
npm run build        # 生產建構
npm run dev          # 開發模式
npm run watch        # 監聽模式
```

## 🐛 常見問題

### CSRF Token 錯誤
確保在 AJAX 請求中包含 CSRF token：
```javascript
headers: {
  'X-CSRF-TOKEN': document.querySelector('meta[name="csrf-token"]').content
}
```

### CORS 錯誤
檢查 `config/cors.php` 配置，確保允許 Java 後端的域名。

### JWT Token 無效
確認 `.env` 中的 `JWT_SECRET` 與 Java 後端一致。

## 📚 相關文檔

- [Vue.js 頁面說明](resources/js/Pages/README.md)
- [架構設計文檔](docs/architecture.md)
- [Laravel 官方文檔](https://laravel.com/docs)
- [Inertia.js 文檔](https://inertiajs.com/)
- [Vue.js 文檔](https://vuejs.org/)
