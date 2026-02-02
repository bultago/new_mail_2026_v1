<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import {
    Search,
    RotateCw,
    Mail,
    ChevronDown,
    User,
    RefreshCw,
    ClipboardList,
    Trash2
} from 'lucide-vue-next'
import { Button } from '@/components/ui/button'
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from '@/components/ui/table'

const { t } = useI18n()
const router = useRouter()

// Mock Data
const receiptList = ref([
    {
        id: '1',
        subject: '프로젝트 A 주간 보고',
        recipientSummary: '김철수',
        recipientCount: 1,
        readCount: 1,
        sendDate: '2026-01-29 09:00:00',
        isInternal: true,
        canCancel: false
    },
    {
        id: '2',
        subject: '회의록 공유',
        recipientSummary: '이영희',
        recipientCount: 1,
        readCount: 0,
        sendDate: '2026-01-28 14:30:00',
        isInternal: true,
        canCancel: true
    },
    {
        id: '3',
        subject: '긴급 점검 요청',
        recipientSummary: '박민수 外 1명',
        recipientCount: 2,
        readCount: 1,
        sendDate: '2026-01-28 10:00:00',
        isInternal: true,
        canCancel: false
    }
])

const searchQuery = ref('')
const isSearchDetailOpen = ref(false)

const filteredList = computed(() => {
    if (!searchQuery.value) return receiptList.value
    return receiptList.value.filter(item =>
        item.subject.includes(searchQuery.value) ||
        item.recipientSummary.includes(searchQuery.value)
    )
})

const toggleSearchDetail = () => {
    isSearchDetailOpen.value = !isSearchDetailOpen.value
}

const goSentBox = () => {
    router.push('/mail/list/sent')
}

const goToDetail = (id: string) => {
    router.push({
        path: `/mail/receipt/${id}`,
        query: { ...router.currentRoute.value.query }
    })
}

const refresh = () => {
    console.log('Refresh list')
}

const cancelSending = (id: string) => {
    if (confirm(t('common.confirm_cancel_sending'))) {
        console.log('Cancel sending for mail', id)
    }
}
</script>

<template>
    <div class="h-full flex flex-col bg-legacy-bg font-dotum">
        <!-- 1. Header Area (Matching MailListView) -->
        <div
            class="flex flex-wrap justify-between items-center px-4 py-3 border-b border-legacy-border bg-white dark:bg-zinc-900 shadow-sm min-h-[50px] gap-y-2">
            <div class="flex items-center gap-2">
                <h2
                    class="text-[16px] text-legacy-text tracking-tight leading-none dark:text-gray-100 font-bold flex items-center gap-2">
                    {{ t('sidebar.receipt') }}
                </h2>
                <span class="text-[12px] text-legacy-text-muted dark:text-gray-400">
                    {{ t('mail.list.total') }} <span class="text-legacy-blue dark:text-blue-400">{{ filteredList.length
                    }}</span>
                </span>
            </div>

            <!-- Search Bar -->
            <div class="flex gap-1 items-center relative w-full sm:w-auto sm:flex-none justify-end mt-2 sm:mt-0">
                <select
                    class="h-[26px] text-[12px] border border-legacy-border px-1 bg-white text-legacy-text rounded-sm dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 w-[80px] sm:w-auto">
                    <option>{{ t('mail.write.to') }}</option>
                    <option>{{ t('mail.list.headers.subject') }}</option>
                    <option>{{ t('mail.list.search_subject_content') }}</option>
                </select>
                <div class="relative">
                    <input type="text" v-model="searchQuery"
                        class="h-[26px] w-full sm:w-[200px] border border-legacy-border pl-2 pr-7 text-[12px] focus:outline-none focus:border-gray-400 bg-white text-legacy-text rounded-sm dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200"
                        :placeholder="t('mail.list.search_placeholder')" />
                    <Search
                        class="absolute right-1 top-1.5 h-4 w-4 text-legacy-text-muted cursor-pointer hover:text-gray-600" />
                </div>
                <button @click="toggleSearchDetail"
                    class="h-[26px] bg-white hover:bg-gray-50 border border-legacy-border text-legacy-text text-[12px] px-2 rounded-sm flex items-center gap-1 ml-1 dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200">
                    {{ t('mail.list.search_detail') }}
                    <ChevronDown class="h-3 w-3 transition-transform" :class="{ 'rotate-180': isSearchDetailOpen }" />
                </button>
            </div>
        </div>

        <!-- Detail Search Form -->
        <div v-if="isSearchDetailOpen"
            class="w-full bg-gray-50 dark:bg-zinc-800 border-t border-legacy-border p-3 mt-2 grid grid-cols-1 sm:grid-cols-2 gap-3 text-sm">
            <div class="flex items-center gap-2 sm:col-span-2">
                <label class="w-20 text-right font-medium text-gray-600 dark:text-gray-400">{{
                    t('mail.receipt.read_date') }}</label>
                <input type="date"
                    class="border border-gray-300 rounded px-2 py-1 dark:bg-zinc-700 dark:border-zinc-600" /> ~
                <input type="date"
                    class="border border-gray-300 rounded px-2 py-1 dark:bg-zinc-700 dark:border-zinc-600" />
            </div>
            <div class="flex items-center gap-2 sm:col-span-2 justify-end">
                <button class="bg-blue-600 text-white px-4 py-1 rounded hover:bg-blue-700 transition-colors">{{
                    t('common.search') }}</button>
            </div>
        </div>

        <!-- Tabs (Optional, keeping for visual alignment with list view) -->
        <div class="flex px-4 pt-4 pb-0 bg-legacy-bg gap-1 border-b border-legacy-border dark:bg-zinc-900/50">
            <button
                class="px-4 py-1.5 text-[12px] font-bold rounded-t-md border-t border-l border-r border-legacy-border focus:outline-none transition-all relative top-[1px] bg-legacy-bg-toolbar text-slate-900 border-b-transparent z-10 dark:bg-zinc-800 dark:text-gray-100 dark:border-zinc-700">
                {{ t('mail.receipt.read_status') }}
            </button>
        </div>

        <!-- 2. Toolbar (Actions) -->
        <div
            class="bg-legacy-bg-toolbar border-b border-legacy-border flex flex-wrap items-center px-4 min-h-[56px] py-1 gap-2 shadow-sm overflow-hidden dark:bg-zinc-800 dark:border-zinc-700">
            <!-- Dynamic Actions -->
            <div class="flex items-center gap-1 overflow-x-auto no-scrollbar">
                <button @click="goSentBox"
                    class="flex flex-col items-center justify-center gap-0.5 px-3 min-w-[50px] h-full py-1 bg-transparent hover:bg-legacy-bg-hover rounded-sm transition-colors group">
                    <Mail
                        class="h-5 w-5 text-gray-500 dark:text-gray-400 group-hover:scale-110 transition-transform group-hover:text-blue-600" />
                    <span class="text-[11px] text-gray-600 dark:text-gray-300 whitespace-nowrap">{{
                        t('mail.receipt.go_sent') }}</span>
                </button>
            </div>

            <div class="flex-1"></div>

            <button @click="refresh"
                class="p-1 hover:bg-white rounded-sm text-legacy-text-muted hover:text-gray-600 transition-colors"
                :title="t('common.refresh')">
                <RefreshCw class="h-4 w-4" />
            </button>
        </div>

        <!-- 3. List Table -->
        <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900 p-4 pt-2">
            <Table class="w-full min-w-[600px] border-collapse border border-gray-200 dark:border-zinc-700 font-sans">
                <TableHeader
                    class="bg-gray-50 sticky top-0 z-10 shadow-sm ring-1 ring-gray-200 dark:ring-zinc-700 dark:bg-zinc-800">
                    <TableRow
                        class="h-[36px] border-b border-gray-200 divide-x divide-gray-200 dark:border-zinc-700 dark:divide-zinc-700">
                        <TableHead class="w-[36px] p-0 text-center bg-transparent"><input type="checkbox" /></TableHead>
                        <TableHead
                            class="w-[20%] px-3 text-left text-gray-600 font-medium text-[13px] bg-transparent border-r border-gray-200 dark:border-zinc-700">
                            {{ t('mail.write.to') }}</TableHead>
                        <TableHead
                            class="px-3 text-left text-gray-600 font-medium text-[13px] bg-transparent border-r border-gray-200 dark:border-zinc-700">
                            {{ t('mail.list.headers.subject') }}</TableHead>
                        <TableHead
                            class="w-[15%] text-center text-gray-600 font-medium text-[13px] bg-transparent border-r border-gray-200 dark:border-zinc-700">
                            {{ t('mail.receipt.read_status') }}</TableHead>
                        <TableHead
                            class="w-[15%] text-center text-gray-600 font-medium text-[13px] bg-transparent border-r border-gray-200 dark:border-zinc-700">
                            {{ t('mail.list.headers.date') }}</TableHead>
                        <TableHead class="w-[10%] text-center text-gray-600 font-medium text-[13px] bg-transparent">{{
                            t('common.cancel') }}</TableHead>
                    </TableRow>
                </TableHeader>
                <TableBody class="bg-white dark:bg-zinc-900">
                    <TableRow v-for="item in filteredList" :key="item.id"
                        class="h-[40px] border-b border-gray-100 hover:bg-gray-50 cursor-pointer transition-colors dark:border-zinc-800 dark:hover:bg-zinc-800/50"
                        @click="goToDetail(item.id)">
                        <TableCell class="p-0 text-center border-r border-gray-100 dark:border-zinc-800" @click.stop>
                            <input type="checkbox" />
                        </TableCell>
                        <TableCell
                            class="p-0 px-3 border-r border-gray-100 dark:border-zinc-800 truncate font-bold text-gray-700 dark:text-gray-300"
                            :title="item.recipientSummary">
                            {{ item.recipientSummary }}
                        </TableCell>
                        <TableCell
                            class="p-0 px-3 border-r border-gray-100 dark:border-zinc-800 truncate text-gray-800 dark:text-gray-200">
                            {{ item.subject }}
                        </TableCell>
                        <TableCell class="p-0 text-center border-r border-gray-100 dark:border-zinc-800 text-[11px]">
                            <span
                                class="bg-gray-100 dark:bg-zinc-700 text-gray-600 dark:text-gray-300 px-2 py-0.5 rounded-full">
                                {{ item.readCount }} / {{ item.recipientCount }}
                            </span>
                        </TableCell>
                        <TableCell
                            class="p-0 text-center border-r border-gray-100 dark:border-zinc-800 text-gray-500 dark:text-gray-400 text-[11px] font-tahoma">
                            {{ item.sendDate }}
                        </TableCell>
                        <TableCell class="p-0 text-center" @click.stop>
                            <Button v-if="item.canCancel" size="xs" variant="outline"
                                class="h-[20px] text-[11px] px-1 border-gray-300 text-red-500 hover:bg-red-50 hover:text-red-600 dark:border-red-900/50 dark:hover:bg-red-900/20"
                                @click="cancelSending(item.id)">
                                {{ t('mail.receipt.cancel_sending') }}
                            </Button>
                        </TableCell>
                    </TableRow>
                </TableBody>
            </Table>
        </div>
    </div>
</template>
