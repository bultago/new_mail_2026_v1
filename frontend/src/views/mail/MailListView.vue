<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table'
import { Badge } from '@/components/ui/badge'
import { mockMessages } from '@/mocks/mailData'
import {
    Paperclip, Star, Trash2, Reply, ReplyAll, Forward,
    FolderInput, Copy, Printer, ShieldAlert, Ban, Search,
    ChevronDown, FileText, AlertCircle, RefreshCw, Undo2,
    ShieldCheck, PenLine, Eraser, Mail, Tag, ArrowUpFromLine,
    Send, XCircle, CheckCircle2, ExternalLink
} from 'lucide-vue-next'

import { useI18n } from 'vue-i18n'
const route = useRoute()
const router = useRouter()
const { t } = useI18n()
const currentFolder = computed(() => route.params.folder || 'inbox')

const folderName = computed(() => {
    const f = currentFolder.value
    // Check if it's a known system folder key
    if (['inbox', 'sent', 'trash', 'spam', 'drafts', 'reserved'].includes(f)) {
        return t(`sidebar.system.${f}`)
    }
    // Tag check (simple heuristic or lookup)
    if (['urgent', 'todo'].includes(f)) return f // Tags might need their own translation logic or pass through
    return f
})

const filteredMessages = computed(() => {
    return mockMessages.filter(msg => {
        if (currentFolder.value === 'inbox') return msg.folderId === 'inbox'
        return msg.folderId === currentFolder.value
    })
})

const activeTab = ref<'basic' | 'advanced'>('basic')

const formatDate = (dateString: string) => {
    const date = new Date(dateString)
    const year = date.getFullYear()
    const month = String(date.getMonth() + 1).padStart(2, '0')
    const day = String(date.getDate()).padStart(2, '0')
    const hours = String(date.getHours()).padStart(2, '0')
    const minutes = String(date.getMinutes()).padStart(2, '0')
    return `${year}-${month}-${day} ${hours}:${minutes}`
}

const handleRowClick = (id: string) => {
    router.push(`/mail/read/${id}`)
}

const openPopupRead = (id: string, event: Event) => {
    event.stopPropagation() // Prevent row click
    window.open(`/popup/read/${id}`, '_blank', 'width=800,height=600,scrollbars=yes')
}

// --- Toolbar Configuration (Legacy Match 192.168.0.45) ---
type ToolbarButton = {
    labelKey: string
    icon: any
    color?: string // Tailwind text color class for icon
    action: string
}

const toolbarConfig: Record<string, { basic: ToolbarButton[], advanced: ToolbarButton[] }> = {
    // 1. Inbox (받은메일함)
    default: {
        basic: [
            { labelKey: 'mail.list.toolbar.delete', icon: Trash2, color: 'group-hover:text-red-600', action: 'delete' },
            { labelKey: 'mail.list.toolbar.perm_delete', icon: XCircle, color: 'group-hover:text-red-700', action: 'permDelete' },
            { labelKey: 'mail.list.toolbar.reply', icon: Reply, color: 'group-hover:text-blue-600', action: 'reply' },
            { labelKey: 'mail.list.toolbar.reply_all', icon: ReplyAll, color: 'group-hover:text-blue-600', action: 'replyAll' },
            { labelKey: 'mail.list.toolbar.forward', icon: Forward, color: 'group-hover:text-blue-600', action: 'forward' },
            { labelKey: 'mail.list.toolbar.move', icon: FolderInput, color: 'group-hover:text-orange-600', action: 'move' },
            { labelKey: 'mail.list.toolbar.copy', icon: Copy, action: 'copy' },
            { labelKey: 'mail.list.toolbar.print', icon: Printer, action: 'print' },
            { labelKey: 'mail.list.toolbar.spam', icon: ShieldAlert, color: 'group-hover:text-red-500', action: 'spam' },
            { labelKey: 'mail.list.toolbar.block', icon: Ban, color: 'group-hover:text-red-600', action: 'block' },
        ],
        advanced: [
            { labelKey: 'mail.list.toolbar.mark_read', icon: Mail, action: 'markRead' },
            { labelKey: 'mail.list.toolbar.tag', icon: Tag, action: 'tag' },
            { labelKey: 'mail.list.toolbar.auto_sort', icon: ArrowUpFromLine, action: 'autoSort' },
            { labelKey: 'mail.list.toolbar.pc_save', icon: FileText, action: 'pcSave' },
        ]
    },
    // 2. Sent (보낸메일함)
    sent: {
        basic: [
            { labelKey: 'mail.list.toolbar.delete', icon: Trash2, color: 'group-hover:text-red-600', action: 'delete' },
            { labelKey: 'mail.list.toolbar.perm_delete', icon: XCircle, color: 'group-hover:text-red-700', action: 'permDelete' },
            { labelKey: 'mail.list.toolbar.reply', icon: Reply, color: 'group-hover:text-blue-600', action: 'reply' },
            { labelKey: 'mail.list.toolbar.reply_all', icon: ReplyAll, color: 'group-hover:text-blue-600', action: 'replyAll' },
            { labelKey: 'mail.list.toolbar.forward', icon: Forward, color: 'group-hover:text-blue-600', action: 'forward' },
            { labelKey: 'mail.list.toolbar.move', icon: FolderInput, color: 'group-hover:text-orange-600', action: 'move' },
            { labelKey: 'mail.list.toolbar.copy', icon: Copy, action: 'copy' },
            { labelKey: 'mail.list.toolbar.print', icon: Printer, action: 'print' },
        ],
        advanced: [
            { labelKey: 'mail.list.toolbar.mark_read', icon: Mail, action: 'markRead' },
            { labelKey: 'mail.list.toolbar.tag', icon: Tag, action: 'tag' },
            { labelKey: 'mail.list.toolbar.pc_save', icon: FileText, action: 'pcSave' },
            { labelKey: 'mail.list.toolbar.resend', icon: Send, color: 'group-hover:text-blue-600', action: 'resend' },
        ]
    },
    // 3. Drafts (임시보관함)
    drafts: {
        basic: [
            { labelKey: 'mail.list.toolbar.delete', icon: Trash2, color: 'group-hover:text-red-600', action: 'delete' },
            { labelKey: 'mail.list.toolbar.perm_delete', icon: XCircle, color: 'group-hover:text-red-700', action: 'permDelete' },
            { labelKey: 'mail.list.toolbar.move', icon: FolderInput, color: 'group-hover:text-orange-600', action: 'move' },
            { labelKey: 'mail.list.toolbar.copy', icon: Copy, action: 'copy' },
            { labelKey: 'mail.list.toolbar.print', icon: Printer, action: 'print' },
        ],
        advanced: [
            { labelKey: 'mail.list.toolbar.mark_read', icon: Mail, action: 'markRead' },
            { labelKey: 'mail.list.toolbar.tag', icon: Tag, action: 'tag' },
            { labelKey: 'mail.list.toolbar.pc_save', icon: FileText, action: 'pcSave' },
        ]
    },
    // 4. Spam (스팸메일함)
    spam: {
        basic: [
            { labelKey: 'mail.list.toolbar.delete', icon: Trash2, color: 'group-hover:text-red-600', action: 'delete' },
            { labelKey: 'mail.list.toolbar.perm_delete', icon: XCircle, color: 'group-hover:text-red-700', action: 'permDelete' },
            { labelKey: 'mail.list.toolbar.reply', icon: Reply, color: 'group-hover:text-blue-600', action: 'reply' },
            { labelKey: 'mail.list.toolbar.reply_all', icon: ReplyAll, color: 'group-hover:text-blue-600', action: 'replyAll' },
            { labelKey: 'mail.list.toolbar.forward', icon: Forward, color: 'group-hover:text-blue-600', action: 'forward' },
            { labelKey: 'mail.list.toolbar.move', icon: FolderInput, color: 'group-hover:text-orange-600', action: 'move' },
            { labelKey: 'mail.list.toolbar.copy', icon: Copy, action: 'copy' },
            { labelKey: 'mail.list.toolbar.print', icon: Printer, action: 'print' },
            { labelKey: 'mail.list.toolbar.not_spam', icon: ShieldCheck, color: 'group-hover:text-green-600', action: 'notSpam' },
        ],
        advanced: [
            { labelKey: 'mail.list.toolbar.mark_read', icon: Mail, action: 'markRead' },
            { labelKey: 'mail.list.toolbar.tag', icon: Tag, action: 'tag' },
            { labelKey: 'mail.list.toolbar.auto_sort', icon: ArrowUpFromLine, action: 'autoSort' },
            { labelKey: 'mail.list.toolbar.pc_save', icon: FileText, action: 'pcSave' },
        ]
    },
    // 5. Trash (휴지통)
    trash: {
        basic: [
            { labelKey: 'mail.list.toolbar.delete', icon: Trash2, color: 'group-hover:text-red-600', action: 'delete' },
            { labelKey: 'mail.list.toolbar.perm_delete', icon: XCircle, color: 'group-hover:text-red-700', action: 'permDelete' },
            { labelKey: 'mail.list.toolbar.reply', icon: Reply, color: 'group-hover:text-blue-600', action: 'reply' },
            { labelKey: 'mail.list.toolbar.reply_all', icon: ReplyAll, color: 'group-hover:text-blue-600', action: 'replyAll' },
            { labelKey: 'mail.list.toolbar.forward', icon: Forward, color: 'group-hover:text-blue-600', action: 'forward' },
            { labelKey: 'mail.list.toolbar.move', icon: FolderInput, color: 'group-hover:text-orange-600', action: 'move' },
            { labelKey: 'mail.list.toolbar.copy', icon: Copy, action: 'copy' },
            { labelKey: 'mail.list.toolbar.print', icon: Printer, action: 'print' },
            { labelKey: 'mail.list.toolbar.restore', icon: Undo2, color: 'group-hover:text-green-600', action: 'restore' },
        ],
        advanced: [
            { labelKey: 'mail.list.toolbar.mark_read', icon: Mail, action: 'markRead' },
            { labelKey: 'mail.list.toolbar.tag', icon: Tag, action: 'tag' },
            { labelKey: 'mail.list.toolbar.auto_sort', icon: ArrowUpFromLine, action: 'autoSort' },
            { labelKey: 'mail.list.toolbar.pc_save', icon: FileText, action: 'pcSave' },
        ]
    }
}

const currentToolbarButtons = computed(() => {
    // Map folder names to config keys (inbox->default, etc)
    const folder = currentFolder.value
    let key = folder
    // Check if key exists in toolbarConfig (type guard)
    if (!(key in toolbarConfig)) {
        key = 'default'
    }

    return activeTab.value === 'basic' ? toolbarConfig[key].basic : toolbarConfig[key].advanced
})
</script>

<template>
    <div class="h-full flex flex-col bg-legacy-bg">
        <!-- Header Area with Integrated Search -->
        <div
            class="flex justify-between items-center px-4 py-3 border-b border-legacy-border bg-white dark:bg-zinc-900 shadow-sm h-[50px]">
            <div class="flex items-center gap-2">
                <h2 class="text-[16px] text-legacy-text tracking-tight leading-none dark:text-white">
                    {{ folderName }}
                </h2>
                <span class="text-[12px] text-legacy-text-muted">{{ t('mail.list.total') }} <span
                        class="text-legacy-blue dark:text-blue-400">{{
                            filteredMessages.length }}</span> / {{ t('mail.list.unread') }} <span
                        class="text-red-500">1</span></span>
            </div>

            <!-- Compact Search Bar -->
            <div class="flex gap-1 items-center">
                <select
                    class="h-[26px] text-[12px] border border-legacy-border px-1 bg-white text-legacy-text rounded-sm dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200">
                    <option>{{ t('mail.list.headers.subject') }}</option>
                    <option>{{ t('mail.list.headers.from') }}</option>
                </select>
                <div class="relative">
                    <input type="text"
                        class="h-[26px] w-[200px] border border-legacy-border pl-2 pr-7 text-[12px] focus:outline-none focus:border-gray-400 bg-white text-legacy-text rounded-sm dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200"
                        :placeholder="t('mail.list.search_placeholder')">
                    <Search
                        class="absolute right-1 top-1.5 h-4 w-4 text-legacy-text-muted cursor-pointer hover:text-gray-600" />
                </div>
                <button
                    class="h-[26px] bg-white hover:bg-gray-50 border border-legacy-border text-legacy-text text-[12px] px-2 rounded-sm flex items-center gap-1 ml-1 dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                    {{ t('mail.list.search_detail') }}
                    <ChevronDown class="h-3 w-3" />
                </button>
            </div>
        </div>

        <!-- Basic / Advanced Tabs (Restored) -->
        <div class="flex px-4 pt-4 pb-0 bg-legacy-bg gap-1 border-b border-legacy-border">
            <button @click="activeTab = 'basic'"
                class="px-4 py-1.5 text-[12px] font-bold rounded-t-md border-t border-l border-r border-legacy-border focus:outline-none transition-all relative top-[1px]"
                :class="activeTab === 'basic' ? 'bg-legacy-bg-toolbar text-slate-900 dark:text-gray-100 border-b-transparent z-10' : 'bg-gray-100 text-legacy-text hover:bg-gray-200 dark:bg-zinc-800 dark:text-gray-400 dark:border-zinc-700'">
                {{ t('mail.list.tabs.basic') }}
            </button>
            <button @click="activeTab = 'advanced'"
                class="px-4 py-1.5 text-[12px] font-bold rounded-t-md border-t border-l border-r border-legacy-border focus:outline-none transition-all relative top-[1px]"
                :class="activeTab === 'advanced' ? 'bg-legacy-bg-toolbar text-slate-900 dark:text-gray-100 border-b-transparent z-10' : 'bg-gray-100 text-legacy-text hover:bg-gray-200 dark:bg-zinc-800 dark:text-gray-400 dark:border-zinc-700'">
                {{ t('mail.list.tabs.advanced') }}
            </button>
        </div>

        <!-- Toolbar (Context Aware) -->
        <div
            class="bg-legacy-bg-toolbar border-b border-legacy-border flex items-center px-4 h-[56px] gap-2 shadow-sm overflow-hidden">

            <!-- Dynamic Actions -->
            <div class="flex items-center gap-1 overflow-x-auto no-scrollbar">
                <template v-for="(btn, index) in currentToolbarButtons" :key="index">
                    <button
                        class="flex flex-col items-center justify-center gap-0.5 px-3 min-w-[50px] h-full py-1 bg-transparent hover:bg-legacy-bg-hover rounded-sm transition-colors group">
                        <component :is="btn.icon"
                            class="h-5 w-5 text-gray-500 dark:text-gray-400 group-hover:scale-110 transition-transform"
                            :class="btn.color" />
                        <span class="text-[11px] text-gray-600 dark:text-gray-300 whitespace-nowrap">{{ t(btn.labelKey)
                        }}</span>
                    </button>
                    <!-- Separator logic -->
                    <div v-if="[0, 4, 7].includes(index) && currentToolbarButtons.length > 8"
                        class="h-8 w-[1px] bg-gray-300 dark:bg-zinc-700 mx-1"></div>
                </template>
            </div>

            <div class="flex-1"></div>

            <!-- Refresh -->
            <button class="p-1 hover:bg-white rounded-sm text-legacy-text-muted hover:text-gray-600 transition-colors"
                title="새로고침">
                <RefreshCw class="h-4 w-4" />
            </button>
        </div>

        <!-- Modern Mail List Table -->
        <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900 p-4 pt-2">
            <Table class="w-full border-collapse border border-gray-200 dark:border-zinc-700 font-sans">
                <!-- Header -->
                <TableHeader
                    class="bg-gray-50 sticky top-0 z-10 shadow-sm ring-1 ring-gray-200 dark:ring-zinc-700 dark:bg-zinc-800">
                    <TableRow
                        class="h-[36px] border-b border-gray-200 divide-x divide-gray-200 dark:border-zinc-700 dark:divide-zinc-700">
                        <TableHead class="w-[36px] p-0 text-center bg-transparent"><input type="checkbox" /></TableHead>
                        <TableHead
                            class="w-[36px] p-0 text-center text-gray-600 font-medium text-[12px] bg-transparent">!
                        </TableHead>
                        <TableHead
                            class="w-[36px] p-0 text-center text-gray-600 font-medium text-[12px] bg-transparent">
                            <Star class="h-4 w-4 mx-auto text-gray-400" />
                        </TableHead>
                        <TableHead
                            class="w-[36px] p-0 text-center text-gray-600 font-medium text-[12px] bg-transparent">
                            <!-- Header Icon (Mail) -->
                            <Mail class="h-4 w-4 mx-auto text-gray-400" />
                        </TableHead>
                        <TableHead
                            class="w-[36px] p-0 text-center text-gray-600 font-medium text-[12px] bg-transparent">
                            <Paperclip class="h-4 w-4 mx-auto text-gray-400" />
                        </TableHead>
                        <TableHead
                            class="w-[140px] px-3 text-left text-gray-600 font-medium text-[13px] bg-transparent border-r border-gray-200 dark:border-zinc-700">
                            {{ t('mail.list.headers.from') }}</TableHead>
                        <TableHead
                            class="px-3 text-left text-gray-600 font-medium text-[13px] bg-transparent border-r border-gray-200 dark:border-zinc-700">
                            {{ t('mail.list.headers.subject') }}</TableHead>
                        <TableHead class="w-[36px] p-0 text-center text-gray-600 font-medium text-[12px] bg-transparent"
                            title="팝업읽기">
                            <ExternalLink class="h-4 w-4 mx-auto text-gray-400" />
                        </TableHead>
                        <TableHead class="w-[140px] text-center text-gray-600 font-medium text-[13px] bg-transparent">{{
                            t('mail.list.headers.date') }}
                        </TableHead>
                        <TableHead class="w-[80px] text-center text-gray-600 font-medium text-[13px] bg-transparent">{{
                            t('mail.list.headers.size') }}
                        </TableHead>
                    </TableRow>
                </TableHeader>

                <!-- Body -->
                <TableBody class="bg-white dark:bg-zinc-900">
                    <template v-if="filteredMessages.length > 0">
                        <TableRow v-for="message in filteredMessages" :key="message.id"
                            @click="handleRowClick(message.id)"
                            class="group h-[40px] border-b border-gray-100 hover:bg-gray-50 cursor-pointer transition-colors dark:border-zinc-800 dark:hover:bg-zinc-800/50">
                            <TableCell class="p-0 text-center"><input type="checkbox" /></TableCell>
                            <TableCell class="p-0 text-center">
                                <AlertCircle v-if="message.isUrgent" class="h-4 w-4 text-red-600 mx-auto" />
                            </TableCell>
                            <TableCell class="p-0 text-center">
                                <Star v-if="message.flagged" class="h-4 w-4 fill-amber-400 text-amber-400 mx-auto" />
                                <Star v-else class="h-4 w-4 text-gray-200 mx-auto" />
                            </TableCell>
                            <TableCell class="p-0 text-center">
                                <!-- Closed Envelope (Unread): Yellow -->
                                <svg v-if="!message.read" class="h-4 w-4 mx-auto text-blue-500" viewBox="0 0 24 24"
                                    fill="currentColor" stroke="none">
                                    <path
                                        d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 8L12 13L4 8V6L12 11L20 6V8Z" />
                                </svg>
                                <!-- Open Envelope (Read): White/Gray -->
                                <svg v-else class="h-4 w-4 mx-auto text-gray-300" viewBox="0 0 24 24"
                                    fill="currentColor" stroke="none">
                                    <path
                                        d="M20 8V6L12 11L4 6V8L12 13L20 8ZM20 4H4C2.9 4 2 4.9 2 6V18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4Z" />
                                </svg>
                            </TableCell>
                            <TableCell class="p-0 text-center">
                                <Paperclip v-if="message.hasAttachment" class="h-4 w-4 text-gray-400 mx-auto" />
                            </TableCell>
                            <TableCell
                                class="px-3 py-0 text-[13px] text-gray-700 dark:text-gray-300 truncate border-r border-transparent">
                                <span :class="{ 'font-medium text-black dark:text-white': !message.read }">{{
                                    message.from.name }}</span>
                            </TableCell>
                            <TableCell
                                class="px-3 py-0 text-[13px] text-gray-700 dark:text-gray-300 truncate border-r border-transparent">
                                <div class="flex items-center gap-1.5">
                                    <span
                                        :class="{ 'font-medium text-black dark:text-white': !message.read, 'text-blue-600 dark:text-blue-400': !message.read }">{{
                                            message.subject }}</span>
                                    <Badge v-for="tag in message.tags" :key="tag" variant="outline"
                                        class="text-[10px] px-1.5 py-0 h-4 border-gray-200 bg-gray-50 text-gray-500 font-normal rounded-full">
                                        {{ tag }}</Badge>
                                </div>
                            </TableCell>
                            <!-- Popup Read Column -->
                            <TableCell class="p-0 text-center">
                                <button
                                    class="p-1 hover:bg-gray-100 rounded-sm text-gray-400 hover:text-blue-600 transition-colors"
                                    title="팝업읽기" @click="(e) => openPopupRead(message.id, e)">
                                    <ExternalLink class="h-3.5 w-3.5 mx-auto" />
                                </button>
                            </TableCell>
                            <TableCell
                                class="text-center py-0 text-[12px] text-gray-500 font-tahoma border-r border-transparent">
                                {{ formatDate(message.date) }}
                            </TableCell>
                            <TableCell class="text-center py-0 text-[12px] text-gray-500 font-tahoma">
                                24KB
                            </TableCell>
                        </TableRow>
                    </template>
                </TableBody>
            </Table>
            <!-- Excel Style Footer (Maintained but softened) -->
            <div
                class="h-[40px] border-t border-gray-200 flex items-center bg-gray-50 px-4 justify-between dark:bg-zinc-900 dark:border-zinc-800 mt-auto">
                <div class="text-[13px] text-gray-500">
                    <span class="mr-2">{{ t('mail.list.selected') }}: 0</span>
                    <span>{{ t('mail.list.total') }}: {{ filteredMessages.length }}</span>
                </div>
                <div class="flex gap-1 text-[12px] font-sans">
                    <button
                        class="px-2 py-1 hover:bg-white border border-transparent hover:border-gray-300 rounded-md disabled:opacity-50 transition-colors"
                        disabled>&lt;</button>
                    <button
                        class="px-3 py-1 font-bold bg-white border border-gray-300 rounded-md shadow-sm text-blue-600">1</button>
                    <button
                        class="px-3 py-1 hover:bg-white border border-transparent hover:border-gray-300 rounded-md transition-colors">2</button>
                    <button
                        class="px-3 py-1 hover:bg-white border border-transparent hover:border-gray-300 rounded-md transition-colors">3</button>
                    <button
                        class="px-2 py-1 hover:bg-white border border-transparent hover:border-gray-300 rounded-md transition-colors">&gt;</button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.font-tahoma {
    font-family: Tahoma, sans-serif;
}

/* Ensure table borders are crisp */
:deep(table) {
    border-collapse: collapse;
}

:deep(th),
:deep(td) {
    vertical-align: middle;
}

/* Hide scrollbar for toolbar */
.no-scrollbar::-webkit-scrollbar {
    display: none;
}

.no-scrollbar {
    -ms-overflow-style: none;
    scrollbar-width: none;
}
</style>
