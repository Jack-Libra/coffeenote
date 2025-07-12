# ☕ Coffee Journal - Vue.js 前端

Coffee Journal 的現代化 Vue.js 前端應用，提供直觀的咖啡筆記管理介面。

## 🎯 專案概覽

這是一個基於 Vue.js 3 + TypeScript + Tailwind CSS 的單頁應用程式，負責：

- 🔐 使用者認證介面（與 Laravel 後端整合）
- 📝 咖啡筆記 CRUD 操作（與 Java 後端整合）
- 🎨 響應式設計和暗色模式支援
- 📱 移動端友好的使用者體驗

## 🏗️ 技術架構

### 核心技術棧
- **Vue.js 3** - 組合式 API 和 `<script setup>` 語法
- **TypeScript** - 類型安全的開發體驗
- **Tailwind CSS** - 實用優先的 CSS 框架
- **Pinia** - 現代化的狀態管理
- **Vue Router** - 客戶端路由
- **Axios** - HTTP 客戶端
- **Vite** - 快速的建構工具

### 專案結構
```
src/
├── assets/           # 靜態資源
├── components/       # 可重用組件
├── router/          # 路由配置
├── services/        # API 服務層
├── stores/          # Pinia 狀態管理
├── views/           # 頁面組件
├── App.vue          # 根組件
└── main.ts          # 應用入口
```

## 🚀 快速開始

### 環境需求
- Node.js 18+
- npm 或 yarn

### 安裝依賴
```bash
npm install
```

### 開發環境
```bash
npm run dev
```
應用將在 http://localhost:5173 啟動

### 建構生產版本
```bash
npm run build
```

### 執行測試
```bash
npm run test:unit
```

### 程式碼檢查
```bash
npm run lint
```

## 🎨 設計系統

詳細的設計系統和樣式指南請參考 [STYLEGUIDE.md](./STYLEGUIDE.md)

### 主題色彩
- **咖啡色系**: `coffee-light`, `coffee`, `coffee-dark`
- **奶泡白**: `cream`
- **咖啡豆深棕**: `bean`
- **淺拿鐵色**: `latte`

### 暗色模式
支援系統偏好設定和手動切換的暗色模式。

## 📱 頁面結構

### 認證頁面
- **登入頁面** (`/login`) - 使用者登入
- **註冊頁面** (`/register`) - 新使用者註冊

### 主要功能頁面
- **儀表板** (`/dashboard`) - 系統概覽和快速操作
- **筆記管理** (`/notes`) - 咖啡筆記的完整 CRUD 操作

### 路由守衛
- 未認證使用者自動重定向到登入頁面
- 已認證使用者可存取所有功能頁面

## 🔌 API 整合

### Laravel 認證服務 (port 8000)
```typescript
// 登入
POST /api/auth/login
// 註冊
POST /api/auth/register
// 登出
POST /api/auth/logout
```

### Java 後端服務 (port 8080)
```typescript
// 筆記 CRUD
GET    /api/notes
POST   /api/notes
PUT    /api/notes/{id}
DELETE /api/notes/{id}
// 搜尋和統計
GET    /api/notes/search
GET    /api/notes/stats
```

## 🛠️ 開發指南

### 推薦 IDE 設定
- [VSCode](https://code.visualstudio.com/)
- [Volar](https://marketplace.visualstudio.com/items?itemName=Vue.volar) 擴充功能
- 停用 Vetur 擴充功能

### TypeScript 支援
專案使用 `vue-tsc` 進行類型檢查，確保 `.vue` 檔案的類型安全。

### 程式碼風格
- 使用 ESLint 進行程式碼檢查
- 遵循 Vue.js 官方風格指南
- 使用 Prettier 進行程式碼格式化

## 📦 建構和部署

### 環境變數
```env
VITE_LARAVEL_API_URL=http://localhost:8000
VITE_JAVA_API_URL=http://localhost:8080
```

### 生產建構
```bash
npm run build
```

建構產物將輸出到 `dist/` 目錄。

## 🧪 測試

### 單元測試
使用 Vitest 進行單元測試：
```bash
npm run test:unit
```

### 測試覆蓋率
```bash
npm run test:coverage
```

## 📚 相關文檔

- [Vue.js 官方文檔](https://vuejs.org/)
- [Tailwind CSS 文檔](https://tailwindcss.com/)
- [Pinia 狀態管理](https://pinia.vuejs.org/)
- [Vite 建構工具](https://vitejs.dev/)

## 🤝 貢獻指南

1. 遵循現有的程式碼風格
2. 為新功能添加適當的測試
3. 更新相關文檔
4. 確保所有測試通過

## 📄 授權

本專案採用 MIT 授權
