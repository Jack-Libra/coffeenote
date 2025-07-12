<template>
  <div class="min-h-screen bg-gray-50">
    <!-- 導航欄 -->
    <nav class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center">
            <h1 class="text-xl font-semibold text-gray-900">☕ Coffee Journal</h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-gray-700">歡迎, {{ authStore.user?.name }}</span>
            <button
              @click="handleLogout"
              class="bg-red-600 hover:bg-red-700 text-white px-3 py-2 rounded-md text-sm font-medium"
            >
              登出
            </button>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主要內容 -->
    <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <div class="px-4 py-6 sm:px-0">
        <div class="border-4 border-dashed border-gray-200 rounded-lg p-8">
          <div class="text-center">
            <h2 class="text-3xl font-extrabold text-gray-900 mb-4">
              歡迎來到您的咖啡日記
            </h2>
            <p class="text-lg text-gray-600 mb-8">
              記錄您的咖啡品嚐體驗，追蹤您最喜愛的咖啡豆和沖泡方法。
            </p>
            
            <!-- 功能卡片 -->
            <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-8">
              <!-- 筆記管理卡片 -->
              <div class="bg-white overflow-hidden shadow rounded-lg">
                <div class="p-6">
                  <div class="flex items-center">
                    <div class="flex-shrink-0">
                      <div class="w-8 h-8 bg-indigo-500 rounded-md flex items-center justify-center">
                        <span class="text-white font-bold">📝</span>
                      </div>
                    </div>
                    <div class="ml-4">
                      <h3 class="text-lg font-medium text-gray-900">咖啡筆記</h3>
                      <p class="text-sm text-gray-500">管理您的咖啡品嚐記錄</p>
                    </div>
                  </div>
                  <div class="mt-4">
                    <router-link
                      to="/notes"
                      class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500"
                    >
                      查看筆記
                    </router-link>
                  </div>
                </div>
              </div>

              <!-- 統計卡片 -->
              <div class="bg-white overflow-hidden shadow rounded-lg">
                <div class="p-6">
                  <div class="flex items-center">
                    <div class="flex-shrink-0">
                      <div class="w-8 h-8 bg-green-500 rounded-md flex items-center justify-center">
                        <span class="text-white font-bold">📊</span>
                      </div>
                    </div>
                    <div class="ml-4">
                      <h3 class="text-lg font-medium text-gray-900">統計分析</h3>
                      <p class="text-sm text-gray-500">查看您的品嚐統計</p>
                    </div>
                  </div>
                  <div class="mt-4">
                    <button
                      @click="loadStats"
                      class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-green-600 hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500"
                    >
                      查看統計
                    </button>
                  </div>
                </div>
              </div>

              <!-- 系統狀態卡片 -->
              <div class="bg-white overflow-hidden shadow rounded-lg">
                <div class="p-6">
                  <div class="flex items-center">
                    <div class="flex-shrink-0">
                      <div class="w-8 h-8 bg-blue-500 rounded-md flex items-center justify-center">
                        <span class="text-white font-bold">⚡</span>
                      </div>
                    </div>
                    <div class="ml-4">
                      <h3 class="text-lg font-medium text-gray-900">系統狀態</h3>
                      <p class="text-sm text-gray-500">檢查後端服務狀態</p>
                    </div>
                  </div>
                  <div class="mt-4">
                    <button
                      @click="checkHealth"
                      class="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-blue-600 hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500"
                    >
                      檢查狀態
                    </button>
                  </div>
                </div>
              </div>
            </div>

            <!-- 狀態信息 -->
            <div v-if="healthStatus" class="mt-8 p-4 bg-green-100 border border-green-400 text-green-700 rounded">
              <p><strong>Java 後端狀態:</strong> {{ healthStatus }}</p>
            </div>

            <div v-if="error" class="mt-8 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
              <p><strong>錯誤:</strong> {{ error }}</p>
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
