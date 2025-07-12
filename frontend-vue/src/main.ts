import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import { useThemeStore } from './stores/theme'

const app = createApp(App)

app.use(createPinia())
app.use(router)

// 初始化主題系統
const themeStore = useThemeStore()
themeStore.initTheme()
themeStore.watchSystemTheme()

app.mount('#app')
