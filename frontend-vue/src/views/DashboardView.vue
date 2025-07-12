<template>
  <div class="min-h-screen bg-cream dark:bg-darkbg transition-colors duration-300">
    <!-- 導航欄 -->
    <nav class="bg-white dark:bg-latte shadow-lg border-b border-coffee-light dark:border-gray-700">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16 items-center">
          <h1 class="text-2xl font-bold text-coffee-dark dark:text-cream flex items-center space-x-2 font-serif">
            <span class="text-3xl">☕</span>
            <span>Coffee Journal</span>
          </h1>
          <div class="flex items-center space-x-4">
            <ThemeToggle />
            <span class="text-sm text-latte dark:text-gray-300">
              👋 歡迎, <strong class="text-coffee-dark dark:text-cream">{{ authStore.user?.name || 'Coffee Lover' }}</strong>
            </span>
            <button
              @click="handleLogout"
              class="px-4 py-2 bg-red-500 hover:bg-red-600 dark:bg-red-600 dark:hover:bg-red-700
                     text-white rounded-lg text-sm font-medium transition-all duration-200
                     transform hover:scale-105"
            >
              登出
            </button>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主內容 -->
    <main class="py-10">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="glass-effect card-shadow rounded-3xl p-8 border-gradient">
          <div class="text-center mb-12">
            <div class="mb-6 pulse-animation">
              <span class="text-8xl">☕</span>
            </div>
            <h2 class="text-5xl font-bold text-gradient mb-6 font-serif">
              歡迎來到您的咖啡日記
            </h2>
            <p class="text-latte dark:text-gray-300 text-xl max-w-3xl mx-auto leading-relaxed">
              記錄您的品嚐體驗，追蹤喜愛的咖啡豆與沖泡法，讓每一杯咖啡都成為美好的回憶
            </p>
          </div>

          <!-- 功能卡片區塊 -->
          <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <!-- 咖啡筆記卡片 -->
            <div class="group cursor-pointer" @click="router.push('/notes')">
              <div class="glass-effect card-shadow rounded-2xl p-8 border-gradient
                          transform hover:-translate-y-4 hover:scale-105 transition-all duration-500
                          hover:rotate-1">
                <div class="text-center">
                  <div class="text-6xl mb-6 transform group-hover:scale-110 transition-transform duration-300">📝</div>
                  <h3 class="text-2xl font-bold text-gradient mb-4">咖啡筆記</h3>
                  <p class="text-latte dark:text-gray-300 mb-6 text-lg">管理您的咖啡品嚐記錄</p>
                  <button class="btn-enhanced w-full">
                    查看筆記 →
                  </button>
                </div>
              </div>
            </div>

            <!-- 統計分析卡片 -->
            <div class="group cursor-pointer" @click="loadStats">
              <div class="glass-effect card-shadow rounded-2xl p-8 border-gradient
                          transform hover:-translate-y-4 hover:scale-105 transition-all duration-500
                          hover:-rotate-1">
                <div class="text-center">
                  <div class="text-6xl mb-6 transform group-hover:scale-110 transition-transform duration-300">📊</div>
                  <h3 class="text-2xl font-bold text-gradient mb-4">統計分析</h3>
                  <p class="text-latte dark:text-gray-300 mb-6 text-lg">查看您的品嚐統計</p>
                  <button class="btn-enhanced w-full">
                    查看統計 →
                  </button>
                </div>
              </div>
            </div>

            <!-- 系統狀態卡片 -->
            <div class="group cursor-pointer" @click="checkHealth">
              <div class="glass-effect card-shadow rounded-2xl p-8 border-gradient
                          transform hover:-translate-y-4 hover:scale-105 transition-all duration-500
                          hover:rotate-1">
                <div class="text-center">
                  <div class="text-6xl mb-6 transform group-hover:scale-110 transition-transform duration-300">⚡</div>
                  <h3 class="text-2xl font-bold text-gradient mb-4">系統狀態</h3>
                  <p class="text-latte dark:text-gray-300 mb-6 text-lg">檢查後端服務狀態</p>
                  <button class="btn-enhanced w-full">
                    檢查狀態 →
                  </button>
                </div>
              </div>
            </div>
          </div>

          <!-- 狀態區塊 -->
          <div v-if="healthStatus" class="mt-8">
            <div class="flex items-center bg-green-50 dark:bg-green-900/20 border-l-4 border-green-500
                        text-green-700 dark:text-green-300 p-4 rounded-lg">
              <span class="text-xl">✅</span>
              <span class="ml-3 font-medium">Java 後端狀態：{{ healthStatus }}</span>
            </div>
          </div>
          <div v-if="error" class="mt-8">
            <div class="flex items-center bg-red-50 dark:bg-red-900/20 border-l-4 border-red-500
                        text-red-700 dark:text-red-300 p-4 rounded-lg">
              <span class="text-xl">❌</span>
              <span class="ml-3 font-medium">{{ error }}</span>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>


<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { healthApi } from '@/services/api'
import ThemeToggle from '@/components/ThemeToggle.vue'
const router = useRouter()
const authStore = useAuthStore()
const healthStatus = ref('')
const error = ref('')

const handleLogout = async () => {
  await authStore.logout()
  router.push('/login')
}

const checkHealth = async () => {
  error.value = ''
  healthStatus.value = ''
  
  try {
    const response = await healthApi.checkJavaBackend()
    healthStatus.value = response.status || '正常運行'
  } catch (err: any) {
    error.value = '無法連接到 Java 後端服務'
    console.error('健康檢查失敗:', err)
  }
}

const loadStats = () => {
  // 未來可以實現統計功能
  alert('統計功能即將推出！')
}
</script>
