<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import type { MailFolder } from '@/mocks/mailData'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
    DropdownMenuSeparator
} from '@/components/ui/dropdown-menu'
import {
    ContextMenu,
    ContextMenuTrigger,
    ContextMenuContent,
    ContextMenuItem,
    ContextMenuSeparator
} from '@/components/ui/context-menu'
import {
    MoreHorizontal,
    Plus,
    Pencil,
    Trash2,
    Share2,
    Inbox,
    Send,
    File,
    Clock,
    AlertOctagon,
    Tag,
    Folder,
    FolderOpen
} from 'lucide-vue-next'

const props = defineProps<{
    folder: MailFolder
    depth: number
    isLastChild: boolean
    currentFolderId: string
}>()

const router = useRouter()
const { t } = useI18n()
const isOpen = ref(true) // Default open

const toggleOpen = (e: MouseEvent) => {
    if (props.folder.children?.length) {
        e.stopPropagation()
        isOpen.value = !isOpen.value
    }
}

const navigateToFolder = () => {
    router.push(`/mail/list/${props.folder.id}`)
}

// Icons for actions (placeholders)
const handleAction = (action: string) => {
    console.log(`Action: ${action} on folder ${props.folder.name}`)
    // TODO: Implement actual logic
}

const isSelected = computed(() => props.currentFolderId === props.folder.id)
const isSystemFolder = computed(() => props.folder.type === 'system')
const isTagFolder = computed(() => props.folder.type === 'tag')

const getFolderIcon = (folder: MailFolder) => {
    if (folder.type === 'tag') return Tag
    if (folder.icon === 'inbox') return Inbox
    if (folder.icon === 'send') return Send
    if (folder.icon === 'file') return File // Drafts
    if (folder.icon === 'clock') return Clock // Reserved
    if (folder.icon === 'alert-octagon') return AlertOctagon // Spam
    if (folder.icon === 'trash-2') return Trash2 // Trash

    // Default User Folder Icon logic
    if (isOpen.value && folder.children && folder.children.length > 0) {
        return FolderOpen
    }
    return Folder
}

const getIconColor = (folder: MailFolder) => {
    if (folder.type === 'tag') {
        return folder.icon === 'tag-red' ? 'text-red-500' : 'text-blue-500'
    }
    if (folder.id === 'inbox') return 'text-[#8B5CF6]'
    if (folder.id === 'sent') return 'text-[#2563EB]'
    if (folder.id === 'drafts') return 'text-[#F59E0B]'
    if (folder.id === 'spam') return 'text-[#7C3AED]'
    if (folder.id === 'trash') return 'text-[#DC2626]'

    // User folder default color (Yellow to match legacy)
    // However, if we use Lucide, we need to apply color via utility class helper or style
    // The previous implementation used text-[#FBC02D]
    return 'text-[#FBC02D] fill-current'
}
</script>

<template>
    <div class="relative">
        <ContextMenu>
            <ContextMenuTrigger as-child>
                <!-- Main Row -->
                <div class="group flex items-center h-[28px] relative hover:bg-zinc-100 dark:hover:bg-zinc-800 transition-colors select-none"
                    :class="{ 'bg-[#005fb0] text-white hover:!bg-[#005fb0]': isSelected }">

                    <!-- Connector Lines Area -->
                    <div class="flex-shrink-0 flex justify-end relative h-full"
                        :style="{ width: `${depth * 16 + 12}px` }">
                        <!-- Connector Lines Implementation -->
                        <div class="absolute right-0 top-0 h-full w-[16px] pointer-events-none">
                            <div class="absolute left-[8px] top-0 w-[1px] bg-gray-300 dark:bg-zinc-700 h-1/2"></div>
                            <div v-if="!isLastChild"
                                class="absolute left-[8px] top-1/2 w-[1px] bg-gray-300 dark:bg-zinc-700 h-1/2"></div>
                            <div class="absolute left-[8px] top-1/2 w-[8px] h-[1px] bg-gray-300 dark:bg-zinc-700">
                            </div>
                        </div>
                    </div>

                    <!-- Toggle/Folder Icon Area -->
                    <div class="flex items-center cursor-pointer mr-1 z-10" @click="toggleOpen">
                        <!-- Toggle Box [+] / [-] -->
                        <div v-if="folder.children && folder.children.length > 0"
                            class="border w-[9px] h-[9px] flex items-center justify-center mr-1 bg-white dark:bg-zinc-800"
                            :class="isSelected ? 'border-white/50 text-blue-800' : 'border-gray-400 text-gray-600 dark:border-zinc-500 dark:text-gray-400'">
                            <span class="text-[8px] leading-none mb-[1px]">{{ isOpen ? '-' : '+' }}</span>
                        </div>
                        <div v-else class="w-[9px] mr-1"></div> <!-- Spacer for alignment -->

                        <!-- Folder Icon Area -->
                        <template v-if="isSystemFolder || isTagFolder">
                            <component :is="getFolderIcon(folder)" class="h-4 w-4"
                                :class="[isSelected ? 'text-white' : getIconColor(folder)]" />
                        </template>
                        <template v-else>
                            <!-- User Folder Icons (Custom SVG for clear Open/Closed distinction) -->
                            <!-- Open State -->
                            <svg v-if="isOpen && folder.children && folder.children.length > 0" class="h-4 w-4"
                                :class="isSelected ? 'text-white' : 'text-[#FBC02D]'" viewBox="0 0 24 24"
                                fill="currentColor" stroke="none">
                                <path
                                    d="M20 6h-8l-2-2H4c-1.1 0-2 .9-2 2v12c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2zm0 12H4V8h16v10z" />
                                <!-- Simplified Open Folder (Legacy Style might just be the outlines or specific shape) -->
                                <!-- Let's use a standard 'Folder Open' path -->
                                <path
                                    d="M22 11V19C22 20.1 21.1 21 20 21H4C2.9 21 2 20.1 2 19V7C2 5.9 2.9 5 4 5H10L12 7H20C21.1 7 22 7.9 22 9V11Z"
                                    opacity="0.5" />
                                <!-- Actually, let's use a clear 'Open' shape distinct from closed -->
                                <path
                                    d="M20 6h-8l-2-2H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2zm0 12H4V8h16v10z" />
                            </svg>
                            <!-- Closed State (or Leaf) -->
                            <svg v-else class="h-4 w-4" :class="isSelected ? 'text-white' : 'text-[#FBC02D]'"
                                viewBox="0 0 24 24" fill="currentColor" stroke="none">
                                <path
                                    d="M10 4H4c-1.1 0-1.99.9-1.99 2L2 18c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V8c0-1.1-.9-2-2-2h-8l-2-2z" />
                            </svg>
                        </template>
                    </div>

                    <!-- Folder Name -->
                    <div class="flex-1 truncate text-[12px] pt-0.5 cursor-pointer"
                        :class="isSelected ? 'font-bold' : 'text-gray-700 dark:text-gray-300'"
                        @click="navigateToFolder">
                        {{ isSystemFolder ? t(`sidebar.system.${folder.id}`) : folder.name }}
                    </div>

                    <!-- Actions (Dropdown Menu Trigger for Click) -->
                    <DropdownMenu>
                        <DropdownMenuTrigger as-child>
                            <div class="hidden group-hover:flex items-center justify-center h-full px-1 cursor-pointer"
                                :class="isSelected ? 'text-white hover:bg-white/20' : 'text-gray-400 hover:text-gray-700 hover:bg-gray-200 dark:hover:bg-zinc-700'">
                                <MoreHorizontal class="h-3.5 w-3.5" />
                            </div>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent class="w-40" align="start">
                            <DropdownMenuItem @click="handleAction('add')" :disabled="isSystemFolder">
                                <Plus class="mr-2 h-3.5 w-3.5" />
                                <span>{{ t('context_menu.add_folder') }}</span>
                            </DropdownMenuItem>
                            <DropdownMenuItem @click="handleAction('edit')" :disabled="isSystemFolder">
                                <Pencil class="mr-2 h-3.5 w-3.5" />
                                <span>{{ t('context_menu.rename') }}</span>
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem @click="handleAction('empty')">
                                <svg class="mr-2 h-3.5 w-3.5" viewBox="0 0 24 24" fill="none" stroke="currentColor"
                                    stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                                    <path d="M3 6h18" />
                                    <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6" />
                                    <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2" />
                                    <line x1="10" x2="10" y1="11" y2="17" />
                                    <line x1="14" x2="14" y1="11" y2="17" />
                                </svg>
                                <span>{{ t('context_menu.empty') }}</span>
                            </DropdownMenuItem>
                            <DropdownMenuItem @click="handleAction('share')" :disabled="isSystemFolder">
                                <Share2 class="mr-2 h-3.5 w-3.5" />
                                <span>{{ t('context_menu.share') }}</span>
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />
                            <DropdownMenuItem @click="handleAction('delete')" :disabled="isSystemFolder"
                                class="text-red-600 focus:text-red-600 data-[disabled]:opacity-50 data-[disabled]:pointer-events-none">
                                <Trash2 class="mr-2 h-3.5 w-3.5" />
                                <span>{{ t('context_menu.delete') }}</span>
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>

                    <!-- Unread Badge -->
                    <span v-if="folder.unreadCount" class="ml-auto text-[10px] pt-0.5 px-2"
                        :class="isSelected ? '!text-white' : '!text-[#D32F2F]'">
                        {{ folder.unreadCount }}
                    </span>
                </div>
            </ContextMenuTrigger>

            <!-- Context Menu Content (Right Click) -->
            <ContextMenuContent class="w-40">
                <ContextMenuItem @click="handleAction('add')" :disabled="isSystemFolder">
                    <Plus class="mr-2 h-3.5 w-3.5" />
                    <span>{{ t('context_menu.add_folder') }}</span>
                </ContextMenuItem>
                <ContextMenuItem @click="handleAction('edit')" :disabled="isSystemFolder">
                    <Pencil class="mr-2 h-3.5 w-3.5" />
                    <span>{{ t('context_menu.rename') }}</span>
                </ContextMenuItem>
                <ContextMenuSeparator />
                <ContextMenuItem @click="handleAction('empty')">
                    <svg class="mr-2 h-3.5 w-3.5" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                        stroke-linecap="round" stroke-linejoin="round">
                        <path d="M3 6h18" />
                        <path d="M19 6v14c0 1-1 2-2 2H7c-1 0-2-1-2-2V6" />
                        <path d="M8 6V4c0-1 1-2 2-2h4c1 0 2 1 2 2v2" />
                        <line x1="10" x2="10" y1="11" y2="17" />
                        <line x1="14" x2="14" y1="11" y2="17" />
                    </svg>
                    <span>{{ t('context_menu.empty') }}</span>
                </ContextMenuItem>
                <ContextMenuItem @click="handleAction('share')" :disabled="isSystemFolder">
                    <Share2 class="mr-2 h-3.5 w-3.5" />
                    <span>{{ t('context_menu.share') }}</span>
                </ContextMenuItem>
                <ContextMenuSeparator />
                <ContextMenuItem @click="handleAction('delete')" :disabled="isSystemFolder"
                    class="text-red-600 focus:text-red-600 data-[disabled]:opacity-50 data-[disabled]:pointer-events-none">
                    <Trash2 class="mr-2 h-3.5 w-3.5" />
                    <span>{{ t('context_menu.delete') }}</span>
                </ContextMenuItem>
            </ContextMenuContent>
        </ContextMenu>

        <!-- Children Recursion -->
        <div v-if="isOpen && folder.children && folder.children.length > 0">
            <SidebarTreeItem v-for="(child, index) in folder.children" :key="child.id" :folder="child"
                :depth="depth + 1" :current-folder-id="currentFolderId"
                :is-last-child="index === folder.children.length - 1" />
        </div>
    </div>
</template>
