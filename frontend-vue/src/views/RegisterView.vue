<template>
  <div class="min-h-screen flex items-center justify-center bg-cream dark:bg-darkbg py-12 px-4 sm:px-6 lg:px-8 transition-colors duration-300">
    <!-- 主題切換按鈕 -->
    <div class="absolute top-4 right-4">
      <ThemeToggle />
    </div>

    <div class="max-w-md w-full space-y-8">
      <div class="text-center">
        <div class="mb-4">
          <span class="text-6xl">☕</span>
        </div>
        <h2 class="text-3xl font-bold text-coffee-dark dark:text-cream font-serif">
          Coffee Journal
        </h2>
        <p class="mt-2 text-sm text-latte dark:text-gray-300">
          創建新帳戶，開始您的咖啡探索之旅
        </p>
      </div>

      <form class="mt-8 space-y-6 bg-white dark:bg-gray-800 p-8 rounded-xl shadow-lg border border-coffee-light dark:border-gray-700" @submit.prevent="handleRegister">
        <div class="space-y-6">
          <div>
            <label for="name" class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              姓名
            </label>
            <input
              id="name"
              v-model="form.name"
              name="name"
              type="text"
              required
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200"
              placeholder="請輸入您的姓名"
            />
          </div>

          <div>
            <label for="email" class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              Email 地址
            </label>
            <input
              id="email"
              v-model="form.email"
              name="email"
              type="email"
              autocomplete="email"
              required
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200"
              placeholder="請輸入您的 Email"
            />
          </div>

          <div>
            <label for="password" class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              密碼
            </label>
            <input
              id="password"
              v-model="form.password"
              name="password"
              type="password"
              required
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200"
              placeholder="密碼 (至少 6 個字符)"
            />
          </div>

          <div>
            <label for="password_confirmation" class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              確認密碼
            </label>
            <input
              id="password_confirmation"
              v-model="form.passwordConfirmation"
              name="password_confirmation"
              type="password"
              required
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200"
              placeholder="請再次輸入密碼"
            />
          </div>
        </div>

        <div v-if="authStore.error" class="bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800 rounded-lg p-3">
          <p class="text-red-600 dark:text-red-400 text-sm text-center">
            {{ authStore.error }}
          </p>
        </div>

        <div v-if="successMessage" class="bg-green-50 dark:bg-green-900/20 border border-green-200 dark:border-green-800 rounded-lg p-3">
          <p class="text-green-600 dark:text-green-400 text-sm text-center">
            {{ successMessage }}
          </p>
        </div>

        <div>
          <button
            type="submit"
            :disabled="authStore.isLoading"
            class="w-full flex justify-center py-3 px-4 border border-transparent text-sm font-medium rounded-lg
                   text-white bg-coffee hover:bg-coffee-dark
                   focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-coffee
                   disabled:opacity-50 disabled:cursor-not-allowed
                   transition-all duration-200 transform hover:scale-105 disabled:hover:scale-100"
          >
            <span v-if="authStore.isLoading" class="flex items-center">
              <svg class="animate-spin -ml-1 mr-3 h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
              </svg>
              註冊中...
            </span>
            <span v-else>☕ 註冊</span>
          </button>
        </div>

        <div class="text-center">
          <router-link
            to="/login"
            class="font-medium text-coffee hover:text-coffee-dark dark:text-coffee-light dark:hover:text-coffee transition-colors duration-200"
          >
            已有帳戶？立即登入 →
          </router-link>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import ThemeToggle from '@/components/ThemeToggle.vue'

const router = useRouter()
const authStore = useAuthStore()
const successMessage = ref('')

const form = reactive({
  name: '',
  email: '',
  password: '',
  passwordConfirmation: ''
})

const handleRegister = async () => {
  authStore.clearError()
  successMessage.value = ''
  
  if (form.password !== form.passwordConfirmation) {
    authStore.error = '密碼確認不匹配'
    return
  }
  
  const success = await authStore.register(
    form.name,
    form.email,
    form.password,
    form.passwordConfirmation
  )
  
  if (success) {
    successMessage.value = '註冊成功！請登入您的帳戶。'
    setTimeout(() => {
      router.push('/login')
    }, 2000)
  }
}
</script>
