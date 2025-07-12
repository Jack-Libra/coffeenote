# 🔧 JWT Token 419 錯誤解決方案

## 🔍 問題分析

**錯誤**: `/api/jwt/token` 返回 419 狀態碼 (CSRF Token Mismatch)

**根本原因**: Laravel Sanctum 的 stateful 認證配置問題

## ✅ 解決方案

### 方案 1: 簡化 JWT API 路由 (推薦)

將 JWT API 移出 Sanctum 中間件，使用簡單的 web 認證：

```php
// routes/web.php
Route::middleware('auth')->group(function () {
    Route::post('/jwt/token', [JwtController::class, 'generateToken']);
    Route::post('/jwt/verify', [JwtController::class, 'verifyToken']);
    Route::post('/jwt/refresh', [JwtController::class, 'refreshToken']);
});
```

### 方案 2: 修復 Sanctum 配置

1. **確保 CSRF Cookie 端點可用**:
   ```bash
   curl -X GET http://localhost:8000/sanctum/csrf-cookie
   ```

2. **前端先獲取 CSRF Cookie**:
   ```javascript
   // 在每次 API 調用前
   await fetch('/sanctum/csrf-cookie', {
     credentials: 'same-origin'
   })
   ```

3. **確保正確的 Headers**:
   ```javascript
   headers: {
     'Content-Type': 'application/json',
     'X-CSRF-TOKEN': getCsrfToken(),
     'X-Requested-With': 'XMLHttpRequest'
   },
   credentials: 'same-origin'
   ```

## 🚀 快速修復

### 步驟 1: 修改路由配置

```php
// routes/web.php - 添加以下路由
Route::middleware('auth')->group(function () {
    Route::post('/jwt/token', [JwtController::class, 'generateToken']);
    Route::post('/jwt/verify', [JwtController::class, 'verifyToken']);
    Route::post('/jwt/refresh', [JwtController::class, 'refreshToken']);
});
```

### 步驟 2: 更新前端 API 調用

```javascript
// 移除 /api 前綴，直接調用
const tokenRes = await fetch('/jwt/token', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json',
    'X-CSRF-TOKEN': getCsrfToken(),
    'X-Requested-With': 'XMLHttpRequest'
  },
  credentials: 'same-origin'
})
```

### 步驟 3: 測試流程

1. **登入系統**: 訪問 http://localhost:8000 並登入
2. **檢查 JWT API**: 在瀏覽器開發者工具中查看網路請求
3. **驗證 Token**: 確保 JWT token 成功生成

## 🔍 調試工具

### 檢查認證狀態
```bash
curl -X GET http://localhost:8000/api/user \
  -H "Accept: application/json" \
  -H "X-Requested-With: XMLHttpRequest" \
  -b cookies.txt
```

### 檢查 CSRF Token
```javascript
// 在瀏覽器控制台中執行
console.log(document.querySelector('meta[name="csrf-token"]')?.getAttribute('content'))
```

### 檢查 Session
```bash
# 檢查 cookies.txt 文件
cat cookies.txt
```

## 📝 當前狀態

- ✅ Laravel 服務器運行正常 (http://localhost:8000)
- ✅ Java 後端運行正常 (http://localhost:8080)
- ✅ 用戶存在 (test@example.com)
- ✅ CSRF Token 配置正確
- ❌ Sanctum 認證流程有問題

## 🎯 建議的最終解決方案

使用簡化的 web 路由而不是 API 路由，避免 Sanctum 的複雜性：

1. **移除 API 路由中的 JWT 端點**
2. **在 web 路由中添加 JWT 端點**
3. **使用標準的 Laravel web 認證**
4. **前端調用時不使用 `/api` 前綴**

這樣可以避免 CSRF 和 Sanctum 的複雜配置問題，同時保持功能完整性。
