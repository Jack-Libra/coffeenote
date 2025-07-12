import { ref, computed } from 'vue'
import { defineStore } from 'pinia'

export const useThemeStore = defineStore('theme', () => {
  // 狀態
  const isDark = ref(false)

  // 計算屬性
  const currentTheme = computed(() => isDark.value ? 'dark' : 'light')
  const themeIcon = computed(() => isDark.value ? '☀️' : '🌙')
  const themeText = computed(() => isDark.value ? '明亮模式' : '暗色模式')

  // 初始化主題
  const initTheme = () => {
    // 檢查 localStorage 中的主題設定
    const savedTheme = localStorage.getItem('coffee-journal-theme')
    
    if (savedTheme) {
      isDark.value = savedTheme === 'dark'
    } else {
      // 如果沒有保存的設定，檢查系統偏好
      isDark.value = window.matchMedia('(prefers-color-scheme: dark)').matches
    }
    
    // 應用主題
    applyTheme()
  }

  // 應用主題到 DOM
  const applyTheme = () => {
    const htmlElement = document.documentElement
    
    if (isDark.value) {
      htmlElement.classList.add('dark')
    } else {
      htmlElement.classList.remove('dark')
    }
  }

  // 切換主題
  const toggleTheme = () => {
    isDark.value = !isDark.value
    
    // 保存到 localStorage
    localStorage.setItem('coffee-journal-theme', currentTheme.value)
    
    // 應用主題
    applyTheme()
    
    // 觸發自定義事件，通知其他組件
    window.dispatchEvent(new CustomEvent('theme-changed', {
      detail: { theme: currentTheme.value }
    }))
  }

  // 設定特定主題
  const setTheme = (theme: 'light' | 'dark') => {
    isDark.value = theme === 'dark'
    localStorage.setItem('coffee-journal-theme', theme)
    applyTheme()
  }

  // 監聽系統主題變化
  const watchSystemTheme = () => {
    const mediaQuery = window.matchMedia('(prefers-color-scheme: dark)')
    
    mediaQuery.addEventListener('change', (e) => {
      // 只有在沒有手動設定主題時才跟隨系統
      const savedTheme = localStorage.getItem('coffee-journal-theme')
      if (!savedTheme) {
        isDark.value = e.matches
        applyTheme()
      }
    })
  }

  return {
    // 狀態
    isDark,
    
    // 計算屬性
    currentTheme,
    themeIcon,
    themeText,
    
    // 方法
    initTheme,
    toggleTheme,
    setTheme,
    watchSystemTheme
  }
})
