<!-- 主題切換按鈕組件 -->
<template>
  <button
    @click="handleToggle"
    :title="themeText"
    aria-label="切換主題"
    class="theme-toggle-btn"
    :class="{ 'dark-mode': isDark }"
  >
    <!-- 動畫圖標容器 -->
    <div class="icon-container">
      <!-- 太陽圖標 (明亮模式) -->
      <div
        class="theme-icon sun-icon"
        :class="{ 'active': !isDark, 'inactive': isDark }"
      >
        ☀️
      </div>
      <!-- 月亮圖標 (暗色模式) -->
      <div
        class="theme-icon moon-icon"
        :class="{ 'active': isDark, 'inactive': !isDark }"
      >
        🌙
      </div>
    </div>

    <!-- 切換滑塊 -->
    <div class="toggle-track">
      <div
        class="toggle-thumb"
        :class="{ 'moved': isDark }"
      ></div>
    </div>

    <span class="theme-text">{{ themeText }}</span>
  </button>
</template>

<script setup lang="ts">
import { useThemeStore } from '@/stores/theme'
import { storeToRefs } from 'pinia'

const themeStore = useThemeStore()
const { isDark, themeText } = storeToRefs(themeStore)
const { toggleTheme } = themeStore

const handleToggle = () => {
  toggleTheme()
}
</script>

<style scoped>
.theme-toggle-btn {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.5rem 1rem;
  border-radius: 2rem;
  background: linear-gradient(135deg, #ffc87fff 0%, #ffd7afe6 100%);
  border: 2px solid #6f4e37;
  color: #3e2723;
  font-weight: 500;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  position: relative;
  overflow: hidden;
  cursor: pointer;
  box-shadow: 0 4px 15px rgba(111, 78, 55, 0.2);
}

.theme-toggle-btn.dark-mode {
  background: linear-gradient(135deg, #374151 0%, #1f2937 100%);
  border-color: #ffd7afe6;
  color: #ffd7afe6;
  box-shadow: 0 4px 15px rgba(255, 215, 175, 0.2);
}

.theme-toggle-btn:hover {
  transform: translateY(-2px) scale(1.05);
  box-shadow: 0 8px 25px rgba(111, 78, 55, 0.3);
}

.theme-toggle-btn.dark-mode:hover {
  box-shadow: 0 8px 25px rgba(255, 215, 175, 0.3);
}

.icon-container {
  position: relative;
  width: 1.5rem;
  height: 1.5rem;
}

.theme-icon {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.25rem;
  transition: all 0.5s cubic-bezier(0.4, 0, 0.2, 1);
}

.theme-icon.active {
  opacity: 1;
  transform: rotate(0deg) scale(1);
}

.theme-icon.inactive {
  opacity: 0;
  transform: rotate(360deg) scale(0.3);
}

.toggle-track {
  width: 3rem;
  height: 1.5rem;
  background: rgba(255, 255, 255, 0.3);
  border-radius: 0.75rem;
  position: relative;
  border: 1px solid rgba(111, 78, 55, 0.3);
  backdrop-filter: blur(10px);
}

.dark-mode .toggle-track {
  background: rgba(0, 0, 0, 0.3);
  border-color: rgba(255, 215, 175, 0.3);
}

.toggle-thumb {
  width: 1.25rem;
  height: 1.25rem;
  background: #6f4e37;
  border-radius: 50%;
  position: absolute;
  top: 0.125rem;
  left: 0.125rem;
  transition: all 0.4s cubic-bezier(0.4, 0, 0.2, 1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
}

.dark-mode .toggle-thumb {
  background: #ffd7afe6;
}

.toggle-thumb.moved {
  transform: translateX(1.5rem);
}

.theme-text {
  font-size: 0.875rem;
  font-weight: 600;
  white-space: nowrap;
  letter-spacing: 0.025em;
}

/* 響應式設計 */
@media (max-width: 640px) {
  .theme-toggle-btn {
    padding: 0.5rem;
    gap: 0.5rem;
    min-width: 3rem;
  }

  .theme-text {
    display: none;
  }

  .toggle-track {
    width: 2.5rem;
    height: 1.25rem;
  }

  .toggle-thumb {
    width: 1rem;
    height: 1rem;
  }

  .toggle-thumb.moved {
    transform: translateX(1.25rem);
  }
}

/* 按鈕點擊動畫 */
.theme-toggle-btn:active {
  transform: translateY(-1px) scale(0.98);
}

/* 漣漪效果 */
.theme-toggle-btn::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 50%;
  width: 0;
  height: 0;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.4);
  transform: translate(-50%, -50%);
  transition: all 0.3s ease-out;
  pointer-events: none;
}

.theme-toggle-btn:active::before {
  width: 200%;
  height: 200%;
  opacity: 0;
}
</style>
