<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterView } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { useThemeStore } from '@/stores/theme'
import { storeToRefs } from 'pinia'

const authStore = useAuthStore()
const themeStore = useThemeStore()
const { isDark } = storeToRefs(themeStore)

onMounted(async () => {
  // 初始化認證狀態
  await authStore.initializeAuth()
})
</script>

<template>
  <div
    id="app"
    class="min-h-screen theme-transition"
    :class="{ 'dark-theme': isDark }"
  >
    <div class="app-background"></div>
    <div class="app-content">
      <RouterView />
    </div>
  </div>
</template>

<style scoped>
#app {
  position: relative;
  overflow-x: hidden;
}

.app-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: var(--gradient-light);
  transition: var(--transition-theme);
  z-index: -1;
}

.dark-theme .app-background {
  background: var(--gradient-dark);
}

.app-content {
  position: relative;
  z-index: 1;
  min-height: 100vh;
}

/* 主題切換時的特殊效果 */
.dark-theme {
  color-scheme: dark;
}

.dark-theme::before {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: radial-gradient(circle at 50% 50%, rgba(255, 215, 175, 0.1) 0%, transparent 70%);
  pointer-events: none;
  z-index: 0;
  transition: var(--transition-theme);
}

/* 星空效果 (暗色模式) */
.dark-theme::after {
  content: '';
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-image:
    radial-gradient(2px 2px at 20px 30px, #ffd7afe6, transparent),
    radial-gradient(2px 2px at 40px 70px, #ffc87fff, transparent),
    radial-gradient(1px 1px at 90px 40px, #ffd7afe6, transparent),
    radial-gradient(1px 1px at 130px 80px, #ffc87fff, transparent),
    radial-gradient(2px 2px at 160px 30px, #ffd7afe6, transparent);
  background-repeat: repeat;
  background-size: 200px 100px;
  animation: sparkle 20s linear infinite;
  pointer-events: none;
  z-index: 0;
  opacity: 0.3;
}

@keyframes sparkle {
  from { transform: translateX(0); }
  to { transform: translateX(200px); }
}

/* 明亮模式的陽光效果 */
#app:not(.dark-theme)::before {
  content: '';
  position: fixed;
  top: -50%;
  right: -50%;
  width: 200%;
  height: 200%;
  background: radial-gradient(circle, rgba(255, 200, 127, 0.1) 0%, transparent 70%);
  animation: sunshine 30s ease-in-out infinite;
  pointer-events: none;
  z-index: 0;
}

@keyframes sunshine {
  0%, 100% { transform: rotate(0deg) scale(1); opacity: 0.1; }
  50% { transform: rotate(180deg) scale(1.1); opacity: 0.2; }
}
</style>
