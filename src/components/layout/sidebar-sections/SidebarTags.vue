<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { Tag } from 'lucide-vue-next'
import SidebarTreeItem from '../SidebarTreeItem.vue'
import { Separator } from '@/components/ui/separator'

defineProps<{
    tags: any[]
    currentFolderId: string
}>()

const { t } = useI18n()
</script>

<template>
    <div>
        <div class="flex items-center px-1 mb-1 text-legacy-text dark:text-gray-200">
            <div
                class="border border-legacy-text-muted bg-legacy-bg dark:bg-zinc-800 dark:border-zinc-600 w-[9px] h-[9px] flex items-center justify-center mr-1">
                <span class="text-[8px] leading-none mb-[2px] select-none">-</span>
            </div>
            <Tag class="h-3.5 w-3.5 text-legacy-text-muted mr-1" />
            <span>{{ t('sidebar.tags') }}</span>
        </div>

        <div class="relative pt-0.5">
            <SidebarTreeItem v-for="(tag, index) in tags" :key="tag.id" :folder="tag" :depth="0"
                :current-folder-id="currentFolderId" :is-last-child="index === tags.length - 1" />
        </div>
        <Separator class="bg-legacy-border my-2 block" />
    </div>
</template>
