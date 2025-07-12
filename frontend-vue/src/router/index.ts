import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import LoginView from '../views/LoginView.vue'
import RegisterView from '../views/RegisterView.vue'
import DashboardView from '../views/DashboardView.vue'
import NotesView from '../views/NotesView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/login',
      name: 'login',
      component: LoginView,
      meta: { requiresGuest: true },
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
      meta: { requiresGuest: true },
    },
    {
      path: '/dashboard',
      name: 'dashboard',
      component: DashboardView,
      meta: { requiresAuth: true },
    },
    {
      path: '/notes',
      name: 'notes',
      component: NotesView,
      meta: { requiresAuth: true },
    },
  ],
})

// 路由守衛
router.beforeEach(async (to, from, next) => {
  const authStore = useAuthStore()

  // 初始化認證狀態
  if (!authStore.user && authStore.token) {
    await authStore.initializeAuth()
  }

  // 檢查是否需要認證
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    next('/login')
    return
  }

  // 檢查是否需要訪客狀態（已登入用戶不能訪問登入/註冊頁面）
  if (to.meta.requiresGuest && authStore.isAuthenticated) {
    next('/dashboard')
    return
  }

  next()
})

export default router
