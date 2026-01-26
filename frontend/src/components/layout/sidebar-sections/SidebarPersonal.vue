<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import SidebarTreeItem from '../SidebarTreeItem.vue'
import { Separator } from '@/components/ui/separator'

defineProps<{
    folders: any[]
    currentFolderId: string
}>()

const { t } = useI18n()
const isOpen = ref(true)
</script>

<template>
    <div>
        <div class="flex items-center px-1 mb-1 cursor-pointer text-legacy-text dark:text-gray-200 hover:text-legacy-blue group"
            @click="isOpen = !isOpen">
            <div
                class="border border-legacy-text-muted bg-legacy-bg dark:bg-zinc-800 dark:border-zinc-600 w-[9px] h-[9px] flex items-center justify-center mr-1">
                <span class="text-[8px] leading-none select-none">{{ isOpen ? '-' : '+' }}</span>
            </div>
            <!-- FILLED YELLOW FOLDER ICON -->
            <svg class="h-4 w-4 mr-1 text-[#FBC02D]" viewBox="0 0 24 24" fill="currentColor" stroke="none">
                <path
                    d="M22 11V19C22 20.1 21.1 21 20 21H4C2.9 21 2 20.1 2 19V7C2 5.9 2.9 5 4 5H10L12 7H20C21.1 7 22 7.9 22 9V11Z" />
            </svg>
            <span>{{ t('sidebar.personal_folders') }}</span>
        </div>

        <div v-if="isOpen" class="space-y-0">
            <div class="relative pt-0.5">
                <SidebarTreeItem v-for="(folder, index) in folders" :key="folder.id" :folder="folder" :depth="0"
                    :current-folder-id="currentFolderId" :is-last-child="index === folders.length - 1" />
            </div>
        </div>
        <Separator class="bg-legacy-border my-2 block" />
    </div>
</template>
