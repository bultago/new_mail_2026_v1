<script setup lang="ts">
import MailWriteForm from './MailWriteForm.vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()
</script>

<template>
  <!-- Standard View Layout -->
  <div class="flex flex-col h-full bg-white dark:bg-zinc-900">

    <!-- Header Area (Consistent with MailListView) -->
    <div
      class="flex justify-between items-center px-4 py-3 border-b border-legacy-border bg-white dark:bg-zinc-900 shadow-sm h-[50px] flex-shrink-0">
      <div class="flex items-center gap-2">
        <h2 class="text-[16px] text-legacy-text tracking-tight leading-none dark:text-white font-bold">
          메일쓰기
        </h2>
      </div>
    </div>

    <div class="flex-1 overflow-hidden">
      <MailWriteForm />
    </div>
  </div>
</template>
