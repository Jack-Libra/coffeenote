<script setup lang="ts">
import { onMounted } from 'vue'
import { RouterView } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

onMounted(async () => {
  // 初始化認證狀態
  await authStore.initializeAuth()
})
</script>

<template>
  <div id="app">
    <RouterView />
  </div>
</template>


