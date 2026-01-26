<script setup lang="ts">
import { onMounted } from 'vue'
import { X } from 'lucide-vue-next'
import { useTheme } from '@/composables/useTheme'
import MailWriteForm from './MailWriteForm.vue'

const { initTheme, currentTheme } = useTheme()

onMounted(() => {
    initTheme()
})

const handleClose = () => {
    window.close()
}
</script>

<template>
  <div class="flex flex-col h-screen overflow-hidden">
      
    <!-- Popup Title Bar -->
    <div class="h-[40px] px-4 flex items-center justify-between bg-legacy-blue dark:bg-zinc-800 select-none shadow-sm flex-shrink-0"
         :class="currentTheme === 'modern' ? 'text-slate-900 bg-white border-b border-gray-200' : 'text-white'"
    >
        <h2 class="text-[13px] font-bold">메일쓰기</h2>
        <button @click="handleClose" class="p-1 rounded transition-colors" :class="currentTheme === 'modern' ? 'hover:bg-gray-100 text-slate-500' : 'hover:bg-white/20 text-white'" title="닫기">
            <X class="h-4 w-4" />
        </button>
    </div>

    <!-- Reusable Write Form -->
    <div class="flex-1 overflow-hidden">
        <MailWriteForm @close="handleClose" />
    </div>

  </div>
</template>
