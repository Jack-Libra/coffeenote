<template>
  <div
    class="bg-latte shadow hover:shadow-md transition rounded-xl p-6 flex flex-col justify-between"
  >
    <div class="flex items-center mb-4">
      <div :class="`w-10 h-10 rounded-md flex items-center justify-center text-xl text-white bg-${color}-500`">
        {{ icon }}
      </div>
      <div class="ml-4">
        <h3 class="text-lg font-semibold text-gray-800">{{ title }}</h3>
        <p class="text-sm text-gray-500">{{ description }}</p>
      </div>
    </div>
    <button
      @click="$emit('click')"
      :class="`mt-4 bg-${color}-600 hover:bg-${color}-700 text-white text-sm py-2 px-4 rounded-full`"
    >
      {{ buttonText }}
    </button>
  </div>
</template>

<script setup lang="ts">
defineProps<{
  title: string
  icon: string
  description: string
  buttonText: string
  color: string
}>()
</script>
