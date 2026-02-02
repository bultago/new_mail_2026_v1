<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Button } from '@/components/ui/button'
import { Separator } from '@/components/ui/separator'
import {
  Tag
} from 'lucide-vue-next'
import { userFolders, tags, systemFolders } from '@/mocks/mailData'

const props = defineProps<{
  width?: number
}>()

const route = useRoute()

const currentFolder = computed(() => {
  if (route.query.folder) return route.query.folder as string
  if (route.path.includes('/mail/receipt')) return 'receipt'
  return (route.params.folder as string) || 'inbox'
})

// Mock tree data structure for Personal Folders
const isPersonalOpen = ref(true)

// Legacy Style Custom Icons (Svg inline for specific colors)
const openPopupWrite = () => {
  window.open('/popup/write', '_blank', 'width=800,height=700,scrollbars=yes')
}
import { useI18n } from 'vue-i18n'
import SidebarTreeItem from './SidebarTreeItem.vue'
import draggable from 'vuedraggable'
import SidebarSystem from './sidebar-sections/SidebarSystem.vue'
import SidebarPersonal from './sidebar-sections/SidebarPersonal.vue'
import SidebarTags from './sidebar-sections/SidebarTags.vue'

const { t } = useI18n()

const sections = ref([
  { id: 'system', component: SidebarSystem },
  { id: 'personal', component: SidebarPersonal },
  { id: 'tags', component: SidebarTags }
])
</script>

<template>
  <div
    class="border-r border-legacy-border-sidebar bg-white dark:bg-zinc-900 text-legacy-text flex flex-col h-screen font-dotum text-[12px]"
    :style="{ width: width ? `${width}px` : '230px' }">

    <!-- Top Action Buttons -->
    <div class="p-3 pb-2 grid grid-cols-[1fr_40px_40px] gap-1 bg-white dark:bg-zinc-900 border-b border-legacy-border">
      <!-- Main Write Button -->
      <Button
        class="h-[36px] bg-[#005fb0] hover:bg-[#004e92] text-white shadow-sm rounded-md flex items-center justify-center gap-2 transition-colors px-0"
        as-child>
        <router-link to="/mail/write">
          <div class="relative w-4 h-3 flex items-center justify-center">
            <div class="absolute inset-0 bg-white/20 rounded-[1px]"></div>
            <div
              class="absolute top-0 w-full h-1.5 border-t border-l border-r border-white/50 transform origin-top rotate-0">
            </div>
          </div>
          <span class="text-[13px] leading-none pb-[1px] font-bold">{{ t('sidebar.compose') }}</span>
        </router-link>
      </Button>

      <!-- Popup Write Button -->
      <Button
        class="h-[36px] p-0 bg-white hover:bg-gray-50 border border-legacy-border shadow-sm rounded-md flex items-center justify-center text-legacy-text transition-colors dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-200"
        :title="t('sidebar.popup_compose')" as-child>
        <a href="/popup/write" @click.prevent="openPopupWrite" class="flex items-center justify-center w-full h-full">
          <svg class="w-4 h-4 text-gray-500 dark:text-gray-400" viewBox="0 0 24 24" fill="none" stroke="currentColor"
            stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"></path>
            <polyline points="15 3 21 3 21 9"></polyline>
            <line x1="10" y1="14" x2="21" y2="3"></line>
          </svg>
        </a>
      </Button>

      <!-- Receipt Confirmation Button -->
      <Button
        class="h-[36px] p-0 bg-white hover:bg-gray-50 border border-legacy-border shadow-sm rounded-md flex items-center justify-center text-legacy-text transition-colors dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-200"
        :title="t('sidebar.receipt')" as-child>
        <router-link to="/mail/receipt" class="flex items-center justify-center w-full h-full"
          :class="{ 'bg-blue-50 dark:bg-zinc-700 ring-1 ring-blue-200 dark:ring-blue-800': currentFolder === 'receipt' }">
          <div class="relative w-4 h-3 flex items-center justify-center">
            <div class="absolute inset-0 bg-blue-500 rounded-[1px]"></div>
            <div
              class="absolute top-0 w-full h-1.5 border-t border-l border-r border-white transform origin-top rotate-0">
            </div>
            <div class="absolute bottom-[-2px] right-[-2px] w-2 h-2 bg-green-500 rounded-full border border-white">
            </div>
          </div>
        </router-link>
      </Button>
    </div>

    <!-- Quota Bar (Mock) -->
    <div class="px-2 mb-2">
      <div class="flex justify-between text-[11px] text-legacy-text-muted mb-0.5">
        <span>{{ t('sidebar.quota') }}</span>
        <span>1.2G / 10G</span>
      </div>
      <div class="h-[8px] w-full bg-legacy-border rounded-sm overflow-hidden">
        <div class="h-full bg-gradient-to-r from-blue-300 to-blue-500 w-[12%]"></div>
      </div>
    </div>

    <ScrollArea class="flex-1 bg-legacy-sidebar-bg">
      <div class="p-2 pt-0 h-full">
        <draggable v-model="sections" item-key="id" handle=".drag-handle" :animation="200" ghost-class="opacity-50">
          <template #item="{ element }">
            <div class="drag-handle cursor-move">
              <component :is="element.component"
                :folders="element.id === 'system' ? systemFolders : (element.id === 'personal' ? userFolders : [])"
                :tags="element.id === 'tags' ? tags : []" :current-folder-id="currentFolder" />
            </div>
          </template>
        </draggable>
      </div>
    </ScrollArea>

    <!-- Language Switcher moved to Header -->
  </div>
</template>
