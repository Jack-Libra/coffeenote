import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { authApi, tokenManager } from '@/services/api'

export interface User {
  id: number
  name: string
  email: string
  email_verified_at?: string
  created_at: string
  updated_at: string
}

export interface LoginResponse {
  access_token: string
  token_type: string
  expires_in: number
  user: User
}

export const useAuthStore = defineStore('auth', () => {
  // State
  const user = ref<User | null>(null)
  const token = ref<string | null>(null)
  const isLoading = ref(false)
  const error = ref<string | null>(null)

  // Getters
  const isAuthenticated = computed(() => !!token.value && !!user.value)

  // Actions
  const login = async (email: string, password: string): Promise<boolean> => {
    isLoading.value = true
    error.value = null

    try {
      const response: LoginResponse = await authApi.login(email, password)
      
      // 保存 token 和用戶信息
      token.value = response.access_token
      user.value = response.user
      
      // 保存到 localStorage 並設置 API header
      tokenManager.setToken(response.access_token)
      
      return true
    } catch (err: any) {
      error.value = err.response?.data?.error || '登入失敗'
      return false
    } finally {
      isLoading.value = false
    }
  }

  const register = async (
    name: string, 
    email: string, 
    password: string, 
    passwordConfirmation: string
  ): Promise<boolean> => {
    isLoading.value = true
    error.value = null

    try {
      await authApi.register(name, email, password, passwordConfirmation)
      return true
    } catch (err: any) {
      error.value = err.response?.data?.message || '註冊失敗'
      return false
    } finally {
      isLoading.value = false
    }
  }

  const logout = async (): Promise<void> => {
    isLoading.value = true

    try {
      if (token.value) {
        await authApi.logout()
      }
    } catch (err) {
      console.error('登出時發生錯誤:', err)
    } finally {
      // 清除本地狀態
      user.value = null
      token.value = null
      tokenManager.removeToken()
      isLoading.value = false
    }
  }

  const refreshToken = async (): Promise<boolean> => {
    if (!token.value) return false

    try {
      const response: LoginResponse = await authApi.refresh()
      
      token.value = response.access_token
      user.value = response.user
      tokenManager.setToken(response.access_token)
      
      return true
    } catch (err) {
      console.error('Token 刷新失敗:', err)
      await logout()
      return false
    }
  }

  const loadUserProfile = async (): Promise<boolean> => {
    if (!token.value) return false

    try {
      const userData: User = await authApi.getUserProfile()
      user.value = userData
      return true
    } catch (err) {
      console.error('載入用戶資料失敗:', err)
      await logout()
      return false
    }
  }

  const initializeAuth = async (): Promise<void> => {
    const savedToken = tokenManager.getToken()
    
    if (savedToken) {
      token.value = savedToken
      tokenManager.setAuthHeader(savedToken)
      
      // 嘗試載入用戶資料
      const success = await loadUserProfile()
      if (!success) {
        // 如果載入失敗，清除無效的 token
        await logout()
      }
    }
  }

  const clearError = (): void => {
    error.value = null
  }

  return {
    // State
    user,
    token,
    isLoading,
    error,
    
    // Getters
    isAuthenticated,
    
    // Actions
    login,
    register,
    logout,
    refreshToken,
    loadUserProfile,
    initializeAuth,
    clearError
  }
})
