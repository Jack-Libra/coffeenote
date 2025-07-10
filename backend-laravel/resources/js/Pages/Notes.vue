<script setup>
import { ref, onMounted } from 'vue'
import { Head, Link } from '@inertiajs/vue3'

const notes = ref([])
const editingId = ref(null)

const form = ref({
  bean_name: '',
  origin: '',
  roast_level: '',
  flavor_notes: '',
  rating: null,
  brewing_method: ''
})

// 獲取 JWT token（從 localStorage 或其他地方）
const getJwtToken = () => {
  // 這裡應該從 Laravel 認證後獲取 JWT token
  // 暫時使用測試 token，實際應用中需要實現完整的認證流程
  return localStorage.getItem('jwt_token') || 'test-token'
}

const fetchNotes = async () => {
  try {
    // 調用 Java 後端 API
    const res = await fetch('http://localhost:8080/api/notes', {
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${getJwtToken()}`,
        'Content-Type': 'application/json'
      }
    })
    if (res.ok) {
      const data = await res.json()
      // 如果是分頁數據，提取 content 陣列
      notes.value = data.content || data
    } else {
      console.error('獲取筆記失敗:', res.status, res.statusText)
      notes.value = []
    }
  } catch (error) {
    console.error('獲取筆記失敗:', error)
    notes.value = []
  }
}

const submitNote = async () => {
  try {
    const url = editingId.value
      ? `http://localhost:8080/api/notes/${editingId.value}`
      : 'http://localhost:8080/api/notes'

    const method = editingId.value ? 'PUT' : 'POST'

    const res = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${getJwtToken()}`
      },
      body: JSON.stringify({
        beanName: form.value.bean_name,
        origin: form.value.origin,
        roastLevel: form.value.roast_level,
        flavorNotes: form.value.flavor_notes,
        rating: form.value.rating,
        brewingMethod: form.value.brewing_method
      })
    })

    if (res.ok) {
      form.value = {
        bean_name: '',
        origin: '',
        roast_level: '',
        flavor_notes: '',
        rating: null,
        brewing_method: ''
      }
      editingId.value = null
      fetchNotes()
    } else {
      console.error('提交失敗:', res.status, res.statusText)
    }
  } catch (error) {
    console.error('提交筆記失敗:', error)
  }
}

const editNote = (note) => {
  editingId.value = note.id
  form.value = {
    bean_name: note.beanName || note.bean_name,
    origin: note.origin,
    roast_level: note.roastLevel || note.roast_level,
    flavor_notes: note.flavorNotes || note.flavor_notes,
    rating: note.rating,
    brewing_method: note.brewingMethod || note.brewing_method
  }
}

const deleteNote = async (id) => {
  if (!confirm('確定要刪除這筆筆記嗎？')) return
  try {
    const res = await fetch(`http://localhost:8080/api/notes/${id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${getJwtToken()}`
      }
    })
    if (res.ok) {
      fetchNotes()
    } else {
      console.error('刪除失敗:', res.status, res.statusText)
    }
  } catch (error) {
    console.error('刪除筆記失敗:', error)
  }
}

onMounted(fetchNotes)
</script>

<template>
  <Head title="Coffee Notes" />
  <div class="p-4 max-w-xl mx-auto">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">☕ Coffee Journal</h1>
      <Link href="/dashboard" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
        前往 Dashboard
      </Link>
    </div>

    <!-- 表單 -->
    <form @submit.prevent="submitNote" class="space-y-2 mb-6">
      <input v-model="form.bean_name" placeholder="豆子名稱" required class="border p-2 w-full" />
      <input v-model="form.origin" placeholder="產地" class="border p-2 w-full" />
      <input v-model="form.roast_level" placeholder="烘焙程度" required class="border p-2 w-full" />
      <input v-model="form.flavor_notes" placeholder="風味" class="border p-2 w-full" />
      <input v-model.number="form.rating" type="number" placeholder="評分 1~5" class="border p-2 w-full" min="1" max="5" />
      <input v-model="form.brewing_method" placeholder="沖煮法" class="border p-2 w-full" />
      <button type="submit" class="bg-green-600 text-white px-4 py-2 rounded">
        {{ editingId ? '更新筆記' : '新增筆記' }}
      </button>
      <button v-if="editingId" @click="editingId = null" type="button" class="ml-2 text-gray-500 underline">
        取消編輯
      </button>
    </form>

    <!-- 筆記清單 -->
    <div v-if="notes.length">
      <h2 class="text-lg font-semibold mb-2">筆記列表：</h2>
      <ul>
        <li v-for="note in notes" :key="note.id" class="border-b py-2 space-y-1">
          <div><strong>{{ note.beanName || note.bean_name }}</strong>（{{ note.origin || '未知產地' }}）</div>
          <div>烘焙：{{ note.roastLevel || note.roast_level }}｜評分：{{ note.rating || '-' }}/5</div>
          <div>風味：{{ note.flavorNotes || note.flavor_notes }}</div>
          <div>沖煮法：{{ note.brewingMethod || note.brewing_method }}</div>
          <div class="space-x-2 text-sm mt-1">
            <button @click="editNote(note)" class="text-blue-600">編輯</button>
            <button @click="deleteNote(note.id)" class="text-red-600">刪除</button>
          </div>
        </li>
      </ul>
    </div>

    <div v-else class="text-gray-500">目前尚無筆記</div>
  </div>
</template>
