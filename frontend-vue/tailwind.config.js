/** @type {import('tailwindcss').Config} */
export default {
  darkMode: 'class', // 使用 class 模式支援手動切換
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        coffee: {
          light: '#ffc87fff',   // 奶茶色
          DEFAULT: '#6f4e37', // 咖啡棕
          dark: '#3e2723',    // 深焙棕
        },
        cream: '#ffd7afe6',     // 奶泡白
        bean: '#4b2e1e',      // 咖啡豆深棕
        latte: '#b08968',     // 淺拿鐵色
        darkbg: '#1f1f1f',     // 暗色背景
      },
      fontFamily: {
        sans: ['"Noto Sans TC"', 'ui-sans-serif', 'system-ui'],
        serif: ['"Noto Serif TC"', 'Georgia', 'serif'],
      },
    },
  },
  plugins: [],
}
