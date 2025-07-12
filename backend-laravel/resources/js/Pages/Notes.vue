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

// 獲取 CSRF token
const getCsrfToken = () => {
  return document.querySelector('meta[name="csrf-token"]')?.getAttribute('content')
}

// 獲取或生成 JWT token
const getJwtToken = async () => {
  // 先檢查 localStorage 中是否有有效的 token
  let token = localStorage.getItem('jwt_token')

  if (token) {
    // 驗證 token 是否仍然有效
    try {
      const verifyRes = await fetch('/jwt/verify', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'X-CSRF-TOKEN': getCsrfToken(),
          'X-Requested-With': 'XMLHttpRequest'
        },
        credentials: 'same-origin',
        body: JSON.stringify({ token })
      })

      if (verifyRes.ok) {
        return token // token 仍然有效
      }
    } catch (e) {
      console.log('Token 驗證失敗，重新生成')
    }
  }

  // 生成新的 JWT token
  try {
    const tokenRes = await fetch('/jwt/token', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'X-CSRF-TOKEN': getCsrfToken(),
        'X-Requested-With': 'XMLHttpRequest'
      },
      credentials: 'same-origin'
    })

    if (tokenRes.ok) {
      const data = await tokenRes.json()
      token = data.token
      localStorage.setItem('jwt_token', token)
      return token
    } else {
      console.error('JWT token 生成失敗:', tokenRes.status, tokenRes.statusText)
    }
  } catch (e) {
    console.error('JWT token 生成失敗:', e)
  }

  return null
}

const fetchNotes = async () => {
  try {
    // 獲取 JWT token
    const token = await getJwtToken()
    if (!token) {
      console.error('無法獲取 JWT token')
      notes.value = []
      return
    }

    // 調用 Java 後端 API
    const res = await fetch('http://localhost:8080/api/notes', {
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`,
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
    // 獲取 JWT token
    const token = await getJwtToken()
    if (!token) {
      console.error('無法獲取 JWT token')
      return
    }

    const url = editingId.value
      ? `http://localhost:8080/api/notes/${editingId.value}`
      : 'http://localhost:8080/api/notes'

    const method = editingId.value ? 'PUT' : 'POST'

    const res = await fetch(url, {
      method,
      headers: {
        'Content-Type': 'application/json',
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
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
    // 獲取 JWT token
    const token = await getJwtToken()
    if (!token) {
      console.error('無法獲取 JWT token')
      return
    }

    const res = await fetch(`http://localhost:8080/api/notes/${id}`, {
      method: 'DELETE',
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
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

// 測試 JWT 流程
const testJwtFlow = async () => {
  console.log('=== 測試 JWT 流程 ===')

  try {
    // 1. 生成 JWT token
    console.log('1. 生成 JWT token...')
    const token = await getJwtToken()
    console.log('JWT Token:', token)

    if (!token) {
      console.error('JWT token 生成失敗')
      return
    }

    // 2. 測試 Java 後端健康檢查
    console.log('2. 測試 Java 後端連接...')
    const healthRes = await fetch('http://localhost:8080/api/health')
    if (healthRes.ok) {
      const healthData = await healthRes.json()
      console.log('Java 後端健康狀態:', healthData)
    } else {
      console.error('Java 後端連接失敗:', healthRes.status)
    }

    // 3. 測試認證端點
    console.log('3. 測試 Java 後端認證...')
    const authRes = await fetch('http://localhost:8080/api/notes', {
      headers: {
        'Accept': 'application/json',
        'Authorization': `Bearer ${token}`
      }
    })
    console.log('認證測試結果:', authRes.status, authRes.statusText)

    if (authRes.ok) {
      const data = await authRes.json()
      console.log('筆記數據:', data)
    } else {
      const errorText = await authRes.text()
      console.error('認證失敗:', errorText)
    }

  } catch (error) {
    console.error('JWT 流程測試失敗:', error)
  }
}

onMounted(fetchNotes)
</script>

<template>
  <Head title="Coffee Notes" />
  <div class="p-4 max-w-xl mx-auto">
    <div class="flex justify-between items-center mb-4">
      <h1 class="text-2xl font-bold">☕ Coffee Journal</h1>
      <div class="space-x-2">
        <button @click="testJwtFlow" class="bg-green-500 text-white px-3 py-1 rounded text-sm hover:bg-green-600">
          測試 JWT
        </button>
        <Link href="/dashboard" class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600">
          前往 Dashboard
        </Link>
      </div>
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
