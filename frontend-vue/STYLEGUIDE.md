# 🎨 Coffee Journal - Vue.js 風格指南

Coffee Journal 前端設計系統和樣式規範，確保整個應用程式的視覺一致性和使用者體驗。

## 🎯 設計理念

### 核心概念
- **咖啡主題**: 以咖啡文化為靈感的溫暖色調
- **簡潔優雅**: 清晰的層次結構和直觀的互動
- **響應式設計**: 適配各種螢幕尺寸的流暢體驗
- **無障礙友好**: 符合 WCAG 2.1 標準的可訪問性

## 🎨 色彩系統

### 主要色彩
```css
/* 咖啡色系 - 主要品牌色 */
coffee-light: #d7ccc8   /* 奶茶色 - 淺色背景、卡片 */
coffee:       #6f4e37   /* 咖啡棕 - 主要按鈕、連結 */
coffee-dark:  #3e2723   /* 深焙棕 - 標題、重要文字 */

/* 輔助色彩 */
cream:        #fefaf6   /* 奶泡白 - 主要背景色 */
bean:         #4b2e1e   /* 咖啡豆深棕 - 強調色 */
latte:        #b08968   /* 淺拿鐵色 - 次要文字 */

/* 暗色模式 */
darkbg:       #1f1f1f   /* 暗色背景 */
```

### 語義化色彩
```css
/* 狀態色彩 */
success:      #10b981   /* 成功狀態 */
warning:      #f59e0b   /* 警告狀態 */
error:        #ef4444   /* 錯誤狀態 */
info:         #3b82f6   /* 資訊狀態 */

/* 中性色彩 */
gray-50:      #f9fafb
gray-100:     #f3f4f6
gray-200:     #e5e7eb
gray-300:     #d1d5db
gray-400:     #9ca3af
gray-500:     #6b7280
gray-600:     #4b5563
gray-700:     #374151
gray-800:     #1f2937
gray-900:     #111827
```

## 🌓 暗色模式支援

### 實現方式
使用 Tailwind CSS 的 `dark:` 前綴實現暗色模式：

```vue
<template>
  <div class="bg-cream dark:bg-darkbg text-coffee-dark dark:text-gray-100">
    <!-- 內容 -->
  </div>
</template>
```

### 切換機制
```typescript
// stores/theme.ts
export const useThemeStore = defineStore('theme', () => {
  const isDark = ref(false)
  
  const toggleTheme = () => {
    isDark.value = !isDark.value
    document.documentElement.classList.toggle('dark', isDark.value)
  }
  
  return { isDark, toggleTheme }
})
```

### 暗色模式色彩對應
| 明亮模式 | 暗色模式 | 用途 |
|---------|---------|------|
| `bg-cream` | `bg-darkbg` | 主要背景 |
| `bg-white` | `bg-gray-800` | 卡片背景 |
| `text-coffee-dark` | `text-gray-100` | 主要文字 |
| `text-latte` | `text-gray-300` | 次要文字 |
| `border-gray-200` | `border-gray-700` | 邊框 |

## 📝 字體系統

### 字體族
```css
/* 主要字體 - 無襯線 */
font-sans: "Noto Sans TC", ui-sans-serif, system-ui

/* 標題字體 - 襯線 */
font-serif: "Noto Serif TC", Georgia, serif
```

### 字體大小
```css
text-xs:    12px    /* 小標籤、輔助文字 */
text-sm:    14px    /* 次要文字 */
text-base:  16px    /* 主要文字 */
text-lg:    18px    /* 大文字 */
text-xl:    20px    /* 小標題 */
text-2xl:   24px    /* 中標題 */
text-3xl:   30px    /* 大標題 */
text-4xl:   36px    /* 主標題 */
```

### 字重
```css
font-light:     300   /* 輕量文字 */
font-normal:    400   /* 一般文字 */
font-medium:    500   /* 中等文字 */
font-semibold:  600   /* 半粗體 */
font-bold:      700   /* 粗體 */
```

## 🧩 公用樣式類別

### 按鈕樣式
```css
/* 主要按鈕 */
.btn-primary {
  @apply bg-coffee hover:bg-coffee-dark text-white px-4 py-2 rounded-lg 
         transition-colors duration-200 font-medium;
}

/* 次要按鈕 */
.btn-secondary {
  @apply bg-coffee-light hover:bg-coffee text-coffee-dark hover:text-white 
         px-4 py-2 rounded-lg transition-colors duration-200 font-medium;
}

/* 危險按鈕 */
.btn-danger {
  @apply bg-red-500 hover:bg-red-600 text-white px-4 py-2 rounded-lg 
         transition-colors duration-200 font-medium;
}

/* 輪廓按鈕 */
.btn-outline {
  @apply border-2 border-coffee text-coffee hover:bg-coffee hover:text-white 
         px-4 py-2 rounded-lg transition-colors duration-200 font-medium;
}
```

### 卡片樣式
```css
/* 基本卡片 */
.card {
  @apply bg-white dark:bg-gray-800 rounded-lg shadow-md p-6 
         border border-gray-200 dark:border-gray-700;
}

/* 懸停卡片 */
.card-hover {
  @apply card hover:shadow-lg transition-shadow duration-200;
}

/* 咖啡主題卡片 */
.card-coffee {
  @apply bg-cream dark:bg-gray-800 border-coffee-light dark:border-gray-600 
         rounded-lg p-6 shadow-sm;
}
```

### 表單樣式
```css
/* 輸入框 */
.input {
  @apply w-full px-3 py-2 border border-gray-300 dark:border-gray-600 
         rounded-lg focus:ring-2 focus:ring-coffee focus:border-coffee 
         bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100;
}

/* 標籤 */
.label {
  @apply block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2;
}

/* 錯誤訊息 */
.error-message {
  @apply text-red-500 text-sm mt-1;
}
```

## 📐 間距系統

### 標準間距
```css
/* Tailwind 預設間距 */
space-1:  0.25rem   /* 4px */
space-2:  0.5rem    /* 8px */
space-3:  0.75rem   /* 12px */
space-4:  1rem      /* 16px */
space-5:  1.25rem   /* 20px */
space-6:  1.5rem    /* 24px */
space-8:  2rem      /* 32px */
space-10: 2.5rem    /* 40px */
space-12: 3rem      /* 48px */
space-16: 4rem      /* 64px */
```

### 佈局間距
```css
/* 容器內邊距 */
.container-padding {
  @apply px-4 sm:px-6 lg:px-8;
}

/* 區塊間距 */
.section-spacing {
  @apply py-8 sm:py-12 lg:py-16;
}

/* 元素間距 */
.element-spacing {
  @apply space-y-4 sm:space-y-6;
}
```

## 🎭 動畫效果

### 過渡動畫
```css
/* 標準過渡 */
.transition-standard {
  @apply transition-all duration-200 ease-in-out;
}

/* 快速過渡 */
.transition-fast {
  @apply transition-all duration-150 ease-in-out;
}

/* 慢速過渡 */
.transition-slow {
  @apply transition-all duration-300 ease-in-out;
}
```

### 懸停效果
```css
/* 懸停提升 */
.hover-lift {
  @apply transform hover:-translate-y-1 transition-transform duration-200;
}

/* 懸停縮放 */
.hover-scale {
  @apply transform hover:scale-105 transition-transform duration-200;
}
```

## 📱 響應式設計

### 斷點
```css
sm:   640px   /* 小型平板 */
md:   768px   /* 平板 */
lg:   1024px  /* 小型桌面 */
xl:   1280px  /* 桌面 */
2xl:  1536px  /* 大型桌面 */
```

### 響應式容器
```css
.responsive-container {
  @apply max-w-7xl mx-auto px-4 sm:px-6 lg:px-8;
}

.responsive-grid {
  @apply grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6;
}
```

## 📁 CSS 檔案結構

```
src/assets/
├── base.css          # 基礎樣式重置
├── main.css          # 主要樣式入口
└── components/       # 組件專用樣式
    ├── buttons.css
    ├── forms.css
    └── cards.css
```

### 樣式組織原則
1. **基礎樣式**: 全域重置和基本設定
2. **組件樣式**: 可重用的組件樣式
3. **工具類**: 單一用途的工具類別
4. **主題變數**: 色彩和間距變數

## ⚠️ 注意事項

### 開發規範
1. **優先使用 Tailwind 類別**: 避免自定義 CSS
2. **保持一致性**: 使用預定義的色彩和間距
3. **響應式優先**: 所有組件都應支援響應式
4. **暗色模式**: 新組件必須支援暗色模式
5. **無障礙性**: 確保適當的對比度和鍵盤導航

### 效能考量
1. **避免過度嵌套**: 保持 CSS 選擇器簡潔
2. **使用 Tailwind 的 JIT**: 只生成使用的樣式
3. **優化圖片**: 使用適當的格式和尺寸
4. **懶載入**: 對非關鍵資源使用懶載入

### 瀏覽器支援
- Chrome 90+
- Firefox 88+
- Safari 14+
- Edge 90+

## 🔧 工具和配置

### Tailwind 配置
參考 `tailwind.config.js` 中的自定義配置。

### PostCSS 插件
- `tailwindcss`
- `autoprefixer`

### 開發工具
- **Tailwind CSS IntelliSense**: VSCode 擴充功能
- **Headwind**: 自動排序 Tailwind 類別
- **Prettier**: 程式碼格式化

## 📚 參考資源

- [Tailwind CSS 官方文檔](https://tailwindcss.com/)
- [Vue.js 樣式指南](https://vuejs.org/style-guide/)
- [Material Design 色彩系統](https://material.io/design/color/)
- [WCAG 2.1 無障礙指南](https://www.w3.org/WAI/WCAG21/quickref/)
