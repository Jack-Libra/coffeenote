<template>
  <div class="min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8">
    <div class="max-w-md w-full space-y-8">
      <div>
        <h2 class="mt-6 text-center text-3xl font-extrabold text-gray-900">
          ☕ Coffee Journal
        </h2>
        <p class="mt-2 text-center text-sm text-gray-600">
          創建新帳戶
        </p>
      </div>
      
      <form class="mt-8 space-y-6" @submit.prevent="handleRegister">
        <div class="space-y-4">
          <div>
            <label for="name" class="block text-sm font-medium text-gray-700">姓名</label>
            <input
              id="name"
              v-model="form.name"
              name="name"
              type="text"
              required
              class="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="您的姓名"
            />
          </div>
          
          <div>
            <label for="email" class="block text-sm font-medium text-gray-700">Email</label>
            <input
              id="email"
              v-model="form.email"
              name="email"
              type="email"
              autocomplete="email"
              required
              class="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="Email 地址"
            />
          </div>
          
          <div>
            <label for="password" class="block text-sm font-medium text-gray-700">密碼</label>
            <input
              id="password"
              v-model="form.password"
              name="password"
              type="password"
              required
              class="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="密碼 (至少 6 個字符)"
            />
          </div>
          
          <div>
            <label for="password_confirmation" class="block text-sm font-medium text-gray-700">確認密碼</label>
            <input
              id="password_confirmation"
              v-model="form.passwordConfirmation"
              name="password_confirmation"
              type="password"
              required
              class="mt-1 appearance-none relative block w-full px-3 py-2 border border-gray-300 placeholder-gray-500 text-gray-900 rounded-md focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm"
              placeholder="再次輸入密碼"
            />
          </div>
        </div>

        <div v-if="authStore.error" class="text-red-600 text-sm text-center">
          {{ authStore.error }}
        </div>

        <div v-if="successMessage" class="text-green-600 text-sm text-center">
          {{ successMessage }}
        </div>

        <div>
          <button
            type="submit"
            :disabled="authStore.isLoading"
            class="group relative w-full flex justify-center py-2 px-4 border border-transparent text-sm font-medium rounded-md text-white bg-indigo-600 hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-indigo-500 disabled:opacity-50 disabled:cursor-not-allowed"
          >
            <span v-if="authStore.isLoading">註冊中...</span>
            <span v-else>註冊</span>
          </button>
        </div>

        <div class="text-center">
          <router-link
            to="/login"
            class="font-medium text-indigo-600 hover:text-indigo-500"
          >
            已有帳戶？立即登入
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
