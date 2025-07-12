<template>
  <div class="min-h-screen bg-gray-50">
    <!-- 導航欄 -->
    <nav class="bg-white shadow">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex items-center space-x-4">
            <router-link to="/dashboard" class="text-indigo-600 hover:text-indigo-500">
              ← 返回儀表板
            </router-link>
            <h1 class="text-xl font-semibold text-gray-900">☕ 咖啡筆記</h1>
          </div>
          <div class="flex items-center space-x-4">
            <span class="text-gray-700">{{ authStore.user?.name }}</span>
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
        <!-- 操作按鈕 -->
        <div class="mb-6 flex justify-between items-center">
          <button
            @click="showCreateForm = true"
            class="bg-indigo-600 hover:bg-indigo-700 text-white px-4 py-2 rounded-md text-sm font-medium"
          >
            新增筆記
          </button>
          
          <div class="flex space-x-2">
            <input
              v-model="searchKeyword"
              type="text"
              placeholder="搜尋筆記..."
              class="px-3 py-2 border border-gray-300 rounded-md text-sm"
              @keyup.enter="searchNotes"
            />
            <button
              @click="searchNotes"
              class="bg-gray-600 hover:bg-gray-700 text-white px-4 py-2 rounded-md text-sm font-medium"
            >
              搜尋
            </button>
            <button
              @click="loadNotes"
              class="bg-green-600 hover:bg-green-700 text-white px-4 py-2 rounded-md text-sm font-medium"
            >
              刷新
            </button>
          </div>
        </div>

        <!-- 錯誤信息 -->
        <div v-if="error" class="mb-4 p-4 bg-red-100 border border-red-400 text-red-700 rounded">
          {{ error }}
        </div>

        <!-- 載入中 -->
        <div v-if="isLoading" class="text-center py-8">
          <p class="text-gray-600">載入中...</p>
        </div>

        <!-- 筆記列表 -->
        <div v-else-if="notes.length > 0" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          <div
            v-for="note in notes"
            :key="note.id"
            class="bg-white overflow-hidden shadow rounded-lg hover:shadow-md transition-shadow"
          >
            <div class="p-6">
              <h3 class="text-lg font-medium text-gray-900 mb-2">{{ note.beanName }}</h3>
              <div class="space-y-1 text-sm text-gray-600">
                <p><strong>產地:</strong> {{ note.origin || '未指定' }}</p>
                <p><strong>烘焙程度:</strong> {{ note.roastLevel || '未指定' }}</p>
                <p><strong>沖泡方法:</strong> {{ note.brewingMethod || '未指定' }}</p>
                <p><strong>評分:</strong> 
                  <span class="text-yellow-500">
                    {{ '★'.repeat(note.rating || 0) }}{{ '☆'.repeat(5 - (note.rating || 0)) }}
                  </span>
                </p>
                <p v-if="note.flavorNotes" class="mt-2">
                  <strong>風味筆記:</strong> {{ note.flavorNotes }}
                </p>
              </div>
              <div class="mt-4 flex space-x-2">
                <button
                  @click="editNote(note)"
                  class="text-indigo-600 hover:text-indigo-500 text-sm font-medium"
                >
                  編輯
                </button>
                <button
                  @click="deleteNote(note.id)"
                  class="text-red-600 hover:text-red-500 text-sm font-medium"
                >
                  刪除
                </button>
              </div>
            </div>
          </div>
        </div>

        <!-- 空狀態 -->
        <div v-else class="text-center py-12">
          <p class="text-gray-500 text-lg">還沒有任何筆記</p>
          <p class="text-gray-400 text-sm mt-2">點擊「新增筆記」開始記錄您的咖啡體驗</p>
        </div>
      </div>
    </main>

    <!-- 創建/編輯筆記模態框 -->
    <div
      v-if="showCreateForm || editingNote"
      class="fixed inset-0 bg-gray-600 bg-opacity-50 overflow-y-auto h-full w-full z-50"
      @click="closeForm"
    >
      <div
        class="relative top-20 mx-auto p-5 border w-96 shadow-lg rounded-md bg-white"
        @click.stop
      >
        <h3 class="text-lg font-bold text-gray-900 mb-4">
          {{ editingNote ? '編輯筆記' : '新增筆記' }}
        </h3>
        
        <form @submit.prevent="submitForm" class="space-y-4">
          <div>
            <label class="block text-sm font-medium text-gray-700">咖啡豆名稱 *</label>
            <input
              v-model="form.beanName"
              type="text"
              required
              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700">產地</label>
            <input
              v-model="form.origin"
              type="text"
              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700">烘焙程度</label>
            <select
              v-model="form.roastLevel"
              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            >
              <option value="">請選擇</option>
              <option value="Light">淺烘焙</option>
              <option value="Medium">中烘焙</option>
              <option value="Dark">深烘焙</option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700">沖泡方法</label>
            <input
              v-model="form.brewingMethod"
              type="text"
              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            />
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700">評分 (1-5)</label>
            <select
              v-model="form.rating"
              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            >
              <option value="">請選擇</option>
              <option value="1">1 ★</option>
              <option value="2">2 ★★</option>
              <option value="3">3 ★★★</option>
              <option value="4">4 ★★★★</option>
              <option value="5">5 ★★★★★</option>
            </select>
          </div>
          
          <div>
            <label class="block text-sm font-medium text-gray-700">風味筆記</label>
            <textarea
              v-model="form.flavorNotes"
              rows="3"
              class="mt-1 block w-full px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-indigo-500 focus:border-indigo-500"
            ></textarea>
          </div>
          
          <div class="flex space-x-3">
            <button
              type="submit"
              :disabled="isSubmitting"
              class="flex-1 bg-indigo-600 hover:bg-indigo-700 text-white py-2 px-4 rounded-md text-sm font-medium disabled:opacity-50"
            >
              {{ isSubmitting ? '保存中...' : '保存' }}
            </button>
            <button
              type="button"
              @click="closeForm"
              class="flex-1 bg-gray-300 hover:bg-gray-400 text-gray-700 py-2 px-4 rounded-md text-sm font-medium"
            >
              取消
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
