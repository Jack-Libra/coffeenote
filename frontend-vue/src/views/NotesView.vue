<template>
  <div class="min-h-screen bg-cream dark:bg-darkbg transition-colors duration-300">
    <!-- 導航欄 -->
    <nav class="bg-white dark:bg-gray-800 shadow-lg border-b border-coffee-light dark:border-gray-700">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center space-x-4">
            <router-link
              to="/dashboard"
              class="flex items-center text-coffee hover:text-coffee-dark dark:text-coffee-light dark:hover:text-coffee
                     transition-colors duration-200 font-medium"
            >
              <span class="mr-2">←</span>
              <span>返回儀表板</span>
            </router-link>
            <div class="h-6 w-px bg-coffee-light dark:bg-gray-600"></div>
            <h1 class="text-xl font-bold text-coffee-dark dark:text-cream flex items-center space-x-2 font-serif">
              <span class="text-2xl">☕</span>
              <span>咖啡筆記</span>
            </h1>
          </div>
          <div class="flex items-center space-x-4">
            <ThemeToggle />
            <span class="text-latte dark:text-gray-300">{{ authStore.user?.name || 'Coffee Lover' }}</span>
            <button
              @click="handleLogout"
              class="bg-red-500 hover:bg-red-600 dark:bg-red-600 dark:hover:bg-red-700
                     text-white px-4 py-2 rounded-lg text-sm font-medium
                     transition-all duration-200 transform hover:scale-105"
            >
              登出
            </button>
          </div>
        </div>
      </div>
    </nav>

    <!-- 主要內容 -->
    <main class="max-w-7xl mx-auto py-8 sm:px-6 lg:px-8">
      <div class="px-4 py-6 sm:px-0">
        <!-- 操作按鈕 -->
        <div class="mb-8 flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
          <button
            @click="showCreateForm = true"
            class="btn-enhanced px-8 py-4 flex items-center space-x-3"
          >
            <span class="text-lg">✏️</span>
            <span>新增筆記</span>
          </button>

          <div class="flex flex-col sm:flex-row space-y-2 sm:space-y-0 sm:space-x-3 w-full sm:w-auto">
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="搜尋筆記..."
              class="px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200 min-w-0 sm:w-64"
              @keyup.enter="searchNotes"
            />
            <div class="flex space-x-2">
              <button
                @click="searchNotes"
                class="bg-latte hover:bg-coffee text-white px-4 py-3 rounded-lg font-medium
                       transition-all duration-200 flex items-center space-x-2"
              >
                <span>🔍</span>
                <span class="hidden sm:inline">搜尋</span>
              </button>
              <button
                @click="loadNotes"
                class="bg-green-600 hover:bg-green-700 text-white px-4 py-3 rounded-lg font-medium
                       transition-all duration-200 flex items-center space-x-2"
              >
                <span>🔄</span>
                <span class="hidden sm:inline">刷新</span>
              </button>
            </div>
          </div>
        </div>

        <!-- 錯誤信息 -->
        <div v-if="error" class="mb-6 p-4 bg-red-50 dark:bg-red-900/20 border border-red-200 dark:border-red-800
                                text-red-700 dark:text-red-400 rounded-lg">
          <div class="flex items-center">
            <span class="text-xl mr-3">❌</span>
            <span class="font-medium">{{ error }}</span>
          </div>
        </div>

        <!-- 載入中 -->
        <div v-if="isLoading" class="text-center py-12">
          <div class="inline-flex items-center space-x-3">
            <svg class="animate-spin h-8 w-8 text-coffee" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
              <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
              <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
            </svg>
            <p class="text-latte dark:text-gray-300 font-medium">載入中...</p>
          </div>
        </div>

        <!-- 筆記列表 -->
        <div v-else-if="notes.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          <div
            v-for="note in notes"
            :key="note.id"
            class="glass-effect card-shadow overflow-hidden rounded-2xl border-gradient
                   hover:shadow-xl transition-all duration-500 transform hover:-translate-y-2 hover:scale-105"
          >
            <div class="p-6">
              <!-- 咖啡豆名稱 -->
              <div class="flex items-center mb-4">
                <span class="text-2xl mr-3">☕</span>
                <h3 class="text-xl font-bold text-gradient">{{ note.beanName }}</h3>
              </div>

              <!-- 筆記詳情 -->
              <div class="space-y-3 text-sm">
                <div class="flex items-center">
                  <span class="text-lg mr-2">🌍</span>
                  <span class="font-medium text-coffee dark:text-coffee-light">產地:</span>
                  <span class="ml-2 text-latte dark:text-gray-300">{{ note.origin || '未指定' }}</span>
                </div>

                <div class="flex items-center">
                  <span class="text-lg mr-2">🔥</span>
                  <span class="font-medium text-coffee dark:text-coffee-light">烘焙程度:</span>
                  <span class="ml-2 text-latte dark:text-gray-300">{{ note.roastLevel || '未指定' }}</span>
                </div>

                <div class="flex items-center">
                  <span class="text-lg mr-2">⚗️</span>
                  <span class="font-medium text-coffee dark:text-coffee-light">沖泡方法:</span>
                  <span class="ml-2 text-latte dark:text-gray-300">{{ note.brewingMethod || '未指定' }}</span>
                </div>

                <div class="flex items-center">
                  <span class="text-lg mr-2">⭐</span>
                  <span class="font-medium text-coffee dark:text-coffee-light">評分:</span>
                  <span class="ml-2 text-yellow-500 text-lg">
                    {{ '★'.repeat(note.rating || 0) }}{{ '☆'.repeat(5 - (note.rating || 0)) }}
                  </span>
                </div>

                <div v-if="note.flavorNotes" class="mt-4 p-3 bg-coffee-light dark:bg-gray-700 rounded-lg">
                  <div class="flex items-start">
                    <span class="text-lg mr-2">📝</span>
                    <div>
                      <span class="font-medium text-coffee dark:text-coffee-light block mb-1">風味筆記:</span>
                      <p class="text-coffee-dark dark:text-gray-200 text-sm leading-relaxed">{{ note.flavorNotes }}</p>
                    </div>
                  </div>
                </div>
              </div>

              <!-- 操作按鈕 -->
              <div class="mt-6 flex space-x-3">
                <button
                  @click="editNote(note)"
                  class="btn-enhanced flex-1 text-sm flex items-center justify-center space-x-2"
                >
                  <span>✏️</span>
                  <span>編輯</span>
                </button>
                <button
                  @click="deleteNote(note.id)"
                  class="flex-1 bg-gradient-to-r from-red-500 to-red-600 hover:from-red-600 hover:to-red-700
                         text-white px-4 py-2 rounded-lg text-sm font-medium
                         transition-all duration-300 transform hover:scale-105
                         flex items-center justify-center space-x-2"
                >
                  <span>🗑️</span>
                  <span>刪除</span>
                </button>
              </div>

              <!-- 創建時間 -->
              <div class="mt-4 pt-4 border-t border-coffee-light dark:border-gray-600">
                <p class="text-xs text-latte dark:text-gray-400 text-center">
                  {{ new Date(note.createdAt).toLocaleDateString('zh-TW') }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <!-- 空狀態 -->
        <div v-else class="text-center py-16">
          <div class="mb-6">
            <span class="text-8xl">☕</span>
          </div>
          <h3 class="text-2xl font-bold text-coffee-dark dark:text-cream mb-4">還沒有任何筆記</h3>
          <p class="text-latte dark:text-gray-300 text-lg mb-8 max-w-md mx-auto">
            開始記錄您的咖啡探索之旅，每一杯都值得被記住
          </p>
          <button
            @click="showCreateForm = true"
            class="btn-enhanced px-10 py-5 text-xl flex items-center space-x-4 mx-auto pulse-animation"
          >
            <span class="text-xl">✏️</span>
            <span>創建第一筆記錄</span>
          </button>
        </div>
      </div>
    </main>

    <!-- 創建/編輯筆記模態框 -->
    <div
      v-if="showCreateForm || editingNote"
      class="fixed inset-0 bg-black bg-opacity-50 overflow-y-auto h-full w-full z-50 flex items-center justify-center p-4"
      @click="closeForm"
    >
      <div
        class="relative mx-auto p-10 glass-effect border-gradient
               w-full max-w-2xl card-shadow rounded-3xl
               transform transition-all duration-500 scale-95 hover:scale-100"
        @click.stop
      >
        <!-- 表單標題 -->
        <div class="flex items-center justify-between mb-6">
          <div class="flex items-center space-x-3">
            <span class="text-3xl">☕</span>
            <h3 class="text-3xl font-bold text-gradient">
              {{ editingNote ? '編輯筆記' : '新增筆記' }}
            </h3>
          </div>
          <button
            @click="closeForm"
            class="text-latte hover:text-coffee-dark dark:text-gray-400 dark:hover:text-gray-200
                   transition-colors duration-200 text-2xl"
          >
            ✕
          </button>
        </div>

        <form @submit.prevent="submitForm" class="space-y-6">
          <!-- 咖啡豆名稱 -->
          <div>
            <label class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              <span class="mr-2">☕</span>咖啡豆名稱 *
            </label>
            <input
              v-model="form.beanName"
              type="text"
              required
              placeholder="例如：藍山咖啡、耶加雪菲..."
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200"
            />
          </div>

          <!-- 產地 -->
          <div>
            <label class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              <span class="mr-2">🌍</span>產地
            </label>
            <input
              v-model="form.origin"
              type="text"
              placeholder="例如：牙買加、衣索比亞、哥倫比亞..."
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200"
            />
          </div>

          <!-- 烘焙程度 -->
          <div>
            <label class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              <span class="mr-2">🔥</span>烘焙程度
            </label>
            <select
              v-model="form.roastLevel"
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     transition-colors duration-200"
            >
              <option value="">請選擇烘焙程度</option>
              <option value="Light">淺烘焙 (Light Roast)</option>
              <option value="Medium">中烘焙 (Medium Roast)</option>
              <option value="Dark">深烘焙 (Dark Roast)</option>
            </select>
          </div>

          <!-- 沖泡方法 -->
          <div>
            <label class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              <span class="mr-2">⚗️</span>沖泡方法
            </label>
            <input
              v-model="form.brewingMethod"
              type="text"
              placeholder="例如：手沖、法式壓濾、義式濃縮..."
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200"
            />
          </div>

          <!-- 評分 -->
          <div>
            <label class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              <span class="mr-2">⭐</span>評分 (1-5 星)
            </label>
            <select
              v-model="form.rating"
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     transition-colors duration-200"
            >
              <option value="">請選擇評分</option>
              <option value="1">1 ★ - 不推薦</option>
              <option value="2">2 ★★ - 一般</option>
              <option value="3">3 ★★★ - 不錯</option>
              <option value="4">4 ★★★★ - 很好</option>
              <option value="5">5 ★★★★★ - 極佳</option>
            </select>
          </div>

          <!-- 風味筆記 -->
          <div>
            <label class="block text-sm font-medium text-coffee-dark dark:text-gray-200 mb-2">
              <span class="mr-2">📝</span>風味筆記
            </label>
            <textarea
              v-model="form.flavorNotes"
              rows="4"
              placeholder="描述這杯咖啡的香氣、口感、餘韻等..."
              class="w-full px-4 py-3 border border-coffee-light dark:border-gray-600 rounded-lg
                     focus:ring-2 focus:ring-coffee focus:border-coffee
                     bg-white dark:bg-gray-700 text-coffee-dark dark:text-gray-100
                     placeholder-latte dark:placeholder-gray-400
                     transition-colors duration-200 resize-none"
            ></textarea>
          </div>

          <!-- 表單按鈕 -->
          <div class="flex space-x-4 pt-4">
            <button
              type="submit"
              :disabled="isSubmitting"
              class="btn-enhanced flex-1 py-4 disabled:opacity-50 disabled:cursor-not-allowed
                     flex items-center justify-center space-x-2"
            >
              <span v-if="isSubmitting">
                <svg class="animate-spin h-5 w-5 text-white" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24">
                  <circle class="opacity-25" cx="12" cy="12" r="10" stroke="currentColor" stroke-width="4"></circle>
                  <path class="opacity-75" fill="currentColor" d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"></path>
                </svg>
              </span>
              <span v-else>💾</span>
              <span>{{ isSubmitting ? '保存中...' : '保存筆記' }}</span>
            </button>
            <button
              type="button"
              @click="closeForm"
              class="flex-1 bg-gray-300 hover:bg-gray-400 dark:bg-gray-600 dark:hover:bg-gray-500
                     text-gray-700 dark:text-gray-200 py-3 px-6 rounded-lg font-medium
                     transition-all duration-200 transform hover:scale-105
                     flex items-center justify-center space-x-2"
            >
              <span>❌</span>
              <span>取消</span>
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { notesApi } from '@/services/api'
import ThemeToggle from '@/components/ThemeToggle.vue'

const router = useRouter()
const authStore = useAuthStore()

// 狀態
const notes = ref<any[]>([])
const isLoading = ref(false)
const error = ref('')
const showCreateForm = ref(false)
const editingNote = ref<any>(null)
const isSubmitting = ref(false)
const searchKeyword = ref('')

// 表單
const form = reactive({
  beanName: '',
  origin: '',
  roastLevel: '',
  brewingMethod: '',
  rating: '',
  flavorNotes: ''
})

// 方法
const loadNotes = async () => {
  isLoading.value = true
  error.value = ''
  
  try {
    const response = await notesApi.getNotes()
    notes.value = response.content || response || []
  } catch (err: any) {
    error.value = '載入筆記失敗: ' + (err.response?.data?.message || err.message)
  } finally {
    isLoading.value = false
  }
}

const searchNotes = async () => {
  if (!searchKeyword.value.trim()) {
    await loadNotes()
    return
  }
  
  isLoading.value = true
  error.value = ''
  
  try {
    const response = await notesApi.searchNotes(searchKeyword.value)
    notes.value = response || []
  } catch (err: any) {
    error.value = '搜尋失敗: ' + (err.response?.data?.message || err.message)
  } finally {
    isLoading.value = false
  }
}

const resetForm = () => {
  form.beanName = ''
  form.origin = ''
  form.roastLevel = ''
  form.brewingMethod = ''
  form.rating = ''
  form.flavorNotes = ''
}

const editNote = (note: any) => {
  editingNote.value = note
  form.beanName = note.beanName || ''
  form.origin = note.origin || ''
  form.roastLevel = note.roastLevel || ''
  form.brewingMethod = note.brewingMethod || ''
  form.rating = note.rating?.toString() || ''
  form.flavorNotes = note.flavorNotes || ''
}

const closeForm = () => {
  showCreateForm.value = false
  editingNote.value = null
  resetForm()
}

const submitForm = async () => {
  isSubmitting.value = true
  error.value = ''
  
  try {
    const noteData = {
      beanName: form.beanName,
      origin: form.origin || null,
      roastLevel: form.roastLevel || null,
      brewingMethod: form.brewingMethod || null,
      rating: form.rating ? parseInt(form.rating) : null,
      flavorNotes: form.flavorNotes || null
    }
    
    if (editingNote.value) {
      await notesApi.updateNote(editingNote.value.id, noteData)
    } else {
      await notesApi.createNote(noteData)
    }
    
    closeForm()
    await loadNotes()
  } catch (err: any) {
    error.value = '保存失敗: ' + (err.response?.data?.message || err.message)
  } finally {
    isSubmitting.value = false
  }
}

const deleteNote = async (id: number) => {
  if (!confirm('確定要刪除這筆記錄嗎？')) return
  
  try {
    await notesApi.deleteNote(id)
    await loadNotes()
  } catch (err: any) {
    error.value = '刪除失敗: ' + (err.response?.data?.message || err.message)
  }
}

const handleLogout = async () => {
  await authStore.logout()
  router.push('/login')
}

// 初始化
onMounted(() => {
  loadNotes()
})
</script>
