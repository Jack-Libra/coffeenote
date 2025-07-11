# 📱 Vue.js 頁面組件說明

Coffee Journal 前端頁面組件的詳細說明文檔。

## 📁 頁面結構

```
Pages/
├── Auth/                   # 認證相關頁面
│   ├── ConfirmPassword.vue # 密碼確認頁面
│   ├── ForgotPassword.vue  # 忘記密碼頁面
│   ├── Login.vue          # 登入頁面
│   ├── Register.vue       # 註冊頁面
│   ├── ResetPassword.vue  # 重設密碼頁面
│   └── VerifyEmail.vue    # Email 驗證頁面
├── Profile/               # 個人資料管理
│   ├── Edit.vue          # 編輯個人資料
│   └── Partials/         # 個人資料子組件
├── Dashboard.vue         # 儀表板頁面
├── Notes.vue            # 咖啡筆記主頁面 ⭐
├── Welcome.vue          # 歡迎頁面
└── README.md           # 本文檔
```

## 🏠 主要頁面

### Welcome.vue - 歡迎頁面
**路由**: `/`  
**用途**: 應用程式首頁，未登入使用者的入口

**功能**:
- 顯示應用程式介紹
- 提供登入/註冊連結
- 展示 Laravel 和 PHP 版本資訊

**組件特色**:
- 響應式設計
- 漸變背景效果
- 動態版本資訊顯示

```vue
<template>
  <div class="min-h-screen bg-gradient-to-br from-blue-50 to-indigo-100">
    <!-- 歡迎內容 -->
  </div>
</template>
```

### Dashboard.vue - 儀表板
**路由**: `/dashboard`  
**權限**: 需要認證  
**用途**: 登入後的主控台頁面

**功能**:
- 顯示使用者歡迎訊息
- 提供快速導航連結
- 展示系統狀態概覽

**組件特色**:
- 卡片式佈局
- 快速操作按鈕
- 統計資訊展示

### Notes.vue - 咖啡筆記主頁面 ⭐
**路由**: `/notes`  
**權限**: 需要認證  
**用途**: 咖啡筆記的核心功能頁面

**主要功能**:
- 📝 **筆記管理**: 新增、編輯、刪除咖啡筆記
- 🔍 **搜尋功能**: 根據關鍵字搜尋筆記
- 📊 **資料展示**: 以卡片形式展示筆記列表
- 🔧 **JWT 測試**: 提供 JWT 認證流程測試功能

**技術特點**:
- 使用 Composition API
- 自動 JWT Token 管理
- 與 Java 後端 API 整合
- 響應式表單設計

**資料流程**:
```
Vue 組件 → Laravel JWT API → Java 後端 API → PostgreSQL
```

**核心方法**:
```javascript
// JWT Token 管理
const getJwtToken = async () => { /* 自動獲取/刷新 Token */ }

// 筆記 CRUD 操作
const fetchNotes = async () => { /* 獲取筆記列表 */ }
const submitNote = async () => { /* 新增/更新筆記 */ }
const deleteNote = async (id) => { /* 刪除筆記 */ }

// 測試功能
const testJwtFlow = async () => { /* 測試 JWT 認證流程 */ }
```

**表單欄位**:
- `bean_name`: 咖啡豆名稱
- `origin`: 產地
- `roast_level`: 烘焙程度
- `flavor_notes`: 風味描述
- `rating`: 評分 (1-5)
- `brewing_method`: 沖煮方法

## 🔐 認證頁面 (Auth/)

### Login.vue - 登入頁面
**路由**: `/login`  
**用途**: 使用者登入

**功能**:
- Email/密碼登入
- 記住我選項
- 忘記密碼連結
- 註冊頁面連結

**表單驗證**:
- Email 格式驗證
- 密碼必填驗證
- 即時錯誤提示

### Register.vue - 註冊頁面
**路由**: `/register`  
**用途**: 新使用者註冊

**功能**:
- 使用者資料收集
- 密碼確認驗證
- 服務條款同意
- 自動登入選項

**表單欄位**:
- 姓名
- Email
- 密碼
- 密碼確認

### ForgotPassword.vue - 忘記密碼
**路由**: `/forgot-password`  
**用途**: 密碼重設請求

**功能**:
- Email 輸入
- 重設連結發送
- 狀態回饋訊息

### ResetPassword.vue - 重設密碼
**路由**: `/reset-password/{token}`  
**用途**: 密碼重設執行

**功能**:
- Token 驗證
- 新密碼設定
- 密碼確認
- 自動重導向

### VerifyEmail.vue - Email 驗證
**路由**: `/verify-email`  
**用途**: Email 地址驗證

**功能**:
- 驗證狀態顯示
- 重新發送驗證信
- 驗證完成處理

### ConfirmPassword.vue - 密碼確認
**路由**: `/confirm-password`  
**用途**: 敏感操作前的密碼確認

**功能**:
- 當前密碼驗證
- 安全操作確認
- 會話延長

## 👤 個人資料頁面 (Profile/)

### Edit.vue - 編輯個人資料
**路由**: `/profile`  
**權限**: 需要認證  
**用途**: 個人資料管理

**功能**:
- 基本資料編輯
- 密碼變更
- 帳號刪除
- 資料驗證

**子組件**:
- `UpdateProfileInformationForm`
- `UpdatePasswordForm`
- `DeleteUserForm`

## 🎨 設計規範

### 樣式系統
- **CSS 框架**: Tailwind CSS
- **色彩主題**: 藍色系為主，綠色為輔助
- **字體**: 系統預設字體堆疊
- **間距**: Tailwind 標準間距系統

### 響應式設計
- **斷點**: 遵循 Tailwind 預設斷點
- **佈局**: Mobile-first 設計原則
- **組件**: 自適應寬度和高度

### 互動設計
- **按鈕**: Hover 效果和點擊回饋
- **表單**: 即時驗證和錯誤提示
- **載入**: Loading 狀態指示器
- **通知**: Toast 訊息系統

## 🔧 開發指南

### 組件開發規範
```vue
<script setup>
// 1. 導入依賴
import { ref, onMounted } from 'vue'
import { Head, Link } from '@inertiajs/vue3'

// 2. 定義 props
const props = defineProps({
  // props 定義
})

// 3. 響應式資料
const data = ref({})

// 4. 方法定義
const method = () => {}

// 5. 生命週期
onMounted(() => {})
</script>

<template>
  <!-- 模板內容 -->
</template>
```

### 狀態管理
- 使用 Vue 3 Composition API
- 響應式資料使用 `ref` 和 `reactive`
- 複雜狀態考慮使用 Pinia

### API 調用規範
```javascript
// 統一的錯誤處理
try {
  const response = await fetch(url, options)
  if (!response.ok) {
    throw new Error(`HTTP ${response.status}`)
  }
  const data = await response.json()
  return data
} catch (error) {
  console.error('API 調用失敗:', error)
  // 錯誤處理邏輯
}
```

## 🧪 測試建議

### 單元測試
- 組件渲染測試
- 方法邏輯測試
- Props 驗證測試

### 整合測試
- 頁面導航測試
- 表單提交測試
- API 整合測試

### E2E 測試
- 使用者流程測試
- 跨頁面互動測試
- 認證流程測試

## 📚 相關資源

- [Vue.js 3 官方文檔](https://vuejs.org/)
- [Inertia.js 文檔](https://inertiajs.com/)
- [Tailwind CSS 文檔](https://tailwindcss.com/)
- [Laravel Breeze 文檔](https://laravel.com/docs/starter-kits#laravel-breeze)
