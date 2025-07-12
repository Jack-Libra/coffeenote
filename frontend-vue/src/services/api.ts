import axios from 'axios'

// Laravel 後端 API 基礎 URL
const LARAVEL_API_URL = 'http://localhost:8000/api'
// Java 後端 API 基礎 URL  
const JAVA_API_URL = 'http://localhost:8080/api'

// Laravel API 實例 (用於認證)
export const laravelApi = axios.create({
  baseURL: LARAVEL_API_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// Java API 實例 (用於筆記 CRUD)
export const javaApi = axios.create({
  baseURL: JAVA_API_URL,
  headers: {
    'Content-Type': 'application/json',
    'Accept': 'application/json'
  }
})

// Token 管理
export const tokenManager = {
  getToken(): string | null {
    return localStorage.getItem('jwt_token')
  },

  setToken(token: string): void {
    localStorage.setItem('jwt_token', token)
    this.setAuthHeader(token)
  },

  removeToken(): void {
    localStorage.removeItem('jwt_token')
    this.removeAuthHeader()
  },

  setAuthHeader(token: string): void {
    javaApi.defaults.headers.common['Authorization'] = `Bearer ${token}`
  },

  removeAuthHeader(): void {
    delete javaApi.defaults.headers.common['Authorization']
  },

  // 初始化時設置 token
  init(): void {
    const token = this.getToken()
    if (token) {
      this.setAuthHeader(token)
    }
  }
}

// 認證 API
export const authApi = {
  async login(email: string, password: string) {
    const response = await laravelApi.post('/auth/login', {
      email,
      password
    })
    return response.data
  },

  async register(name: string, email: string, password: string, password_confirmation: string) {
    const response = await laravelApi.post('/auth/register', {
      name,
      email,
      password,
      password_confirmation
    })
    return response.data
  },

  async logout() {
    const response = await laravelApi.post('/auth/logout')
    return response.data
  },

  async refresh() {
    const response = await laravelApi.post('/auth/refresh')
    return response.data
  },

  async getUserProfile() {
    const response = await laravelApi.get('/auth/user-profile')
    return response.data
  }
}

// 筆記 API (調用 Java 後端)
export const notesApi = {
  async getNotes() {
    const response = await javaApi.get('/notes')
    return response.data
  },

  async getNote(id: number) {
    const response = await javaApi.get(`/notes/${id}`)
    return response.data
  },

  async createNote(note: any) {
    const response = await javaApi.post('/notes', note)
    return response.data
  },

  async updateNote(id: number, note: any) {
    const response = await javaApi.put(`/notes/${id}`, note)
    return response.data
  },

  async deleteNote(id: number) {
    const response = await javaApi.delete(`/notes/${id}`)
    return response.data
  },

  async searchNotes(keyword: string) {
    const response = await javaApi.get(`/notes/search?keyword=${encodeURIComponent(keyword)}`)
    return response.data
  }
}

// 健康檢查 API
export const healthApi = {
  async checkJavaBackend() {
    const response = await javaApi.get('/health')
    return response.data
  }
}

// 初始化 token
tokenManager.init()
