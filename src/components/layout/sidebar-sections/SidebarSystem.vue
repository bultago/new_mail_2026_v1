<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import SidebarTreeItem from '../SidebarTreeItem.vue'
import { Separator } from '@/components/ui/separator'

defineProps<{
    folders: any[]
    currentFolderId: string
}>()

const { t } = useI18n()
</script>

<template>
    <div class="mb-2">
        <div class="space-y-[1px]">
            <SidebarTreeItem v-for="(folder, index) in folders" :key="folder.id" :folder="folder" :depth="0"
                :current-folder-id="currentFolderId" :is-last-child="index === folders.length - 1" />
        </div>
        <Separator class="bg-legacy-border my-2 block" />
    </div>
</template>
