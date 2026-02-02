<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { ArrowLeft, Mail, RotateCw, Trash2, User, Clock, CheckCircle2, ChevronLeft, RefreshCw } from 'lucide-vue-next'
import { Button } from '@/components/ui/button'

const { t } = useI18n()
const route = useRoute()
const router = useRouter()

const receiptId = route.params.id as string
// Mock: Original Mail ID linked to this receipt
const originalMailId = receiptId

// Mock Data
const receiptDetail = ref({
    id: receiptId,
    subject: '프로젝트 A 주간 보고 (상세)',
    sendDate: '2026-01-29 09:00:00',
    recipients: [
        { email: 'kim@company.com', name: '김철수', readDate: '2026-01-29 09:15:23', status: 'read', isInternal: true },
        { email: 'park@company.com', name: '박민수', readDate: '-', status: 'unread', isInternal: true },
        { email: 'external@gmail.com', name: 'External User', readDate: '-', status: 'unread', isInternal: false } // Cannot cancel external
    ]
})

const goBack = () => {
    router.push('/mail/receipt')
}

const goToOriginalMail = () => {
    // Navigate to MailReadView for the sent mail
    router.push({
        path: `/mail/read/${originalMailId}`,
        query: { ...route.query, receipt: 'true' }
    })
}

const refresh = () => {
    console.log('Refresh detail')
}

const cancelSending = (recipient: any) => {
    if (confirm(t('common.confirm_cancel_sending'))) {
        console.log('Cancel sending for', recipient.email)
        // API call to delete mail from recipient's inbox
    }
}
</script>

<template>
    <div class="flex flex-col h-full bg-white dark:bg-zinc-900 font-dotum">
        <!-- Top Action Bar (Matching MailReadView exactly) -->
        <div
            class="px-4 py-2 border-b border-legacy-border bg-[#F7F7F7] dark:bg-zinc-900 flex items-center gap-1 shrink-0">
            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700 hover:bg-gray-50 dark:hover:bg-zinc-700"
                @click="goBack">
                <ChevronLeft class="h-3.5 w-3.5 mr-1" /> {{ t('mail.read.back') }}
            </Button>

            <div class="h-4 w-[1px] bg-[#CCC] mx-2 dark:bg-zinc-700"></div>

            <Button variant="outline" size="sm" @click="refresh"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700 hover:bg-gray-50 dark:hover:bg-zinc-700">
                <RefreshCw class="h-3.5 w-3.5 mr-1 text-slate-500 dark:text-gray-400" /> {{ t('common.refresh') }}
            </Button>

            <Button variant="outline" size="sm" @click="goToOriginalMail"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700 hover:bg-gray-50 dark:hover:bg-zinc-700">
                <Mail class="h-3.5 w-3.5 mr-1 text-blue-600 dark:text-blue-400" /> {{ t('mail.receipt.view_original') }}
            </Button>
        </div>

        <!-- Content Area -->
        <div class="p-6 flex-1 overflow-auto bg-white dark:bg-zinc-900">
            <div class="border border-legacy-border bg-white dark:bg-zinc-100 shadow-sm max-w-[1000px]">
                <!-- Header Info (Matching MailReadView) -->
                <div class="bg-[#F9F9F9] dark:bg-zinc-800 border-b border-legacy-border p-4">
                    <div class="mb-3">
                        <h1
                            class="text-[18px] font-bold text-[#333] dark:text-white tracking-tight leading-tight flex items-center gap-2">
                            <span
                                class="inline-block px-1.5 py-0.5 rounded bg-[#EEE] dark:bg-zinc-700 text-[#666] dark:text-gray-300 text-[11px] font-normal align-middle border border-[#DDD] dark:border-zinc-600">
                                {{ t('sidebar.receipt') }}
                            </span>
                            {{ receiptDetail.subject }}
                        </h1>
                    </div>
                    <div class="grid grid-cols-[80px_1fr] gap-y-1 text-[12px]">
                        <div class="font-bold text-[#555] dark:text-gray-400 text-right pr-4">{{
                            t('mail.receipt.send_date') }}</div>
                        <div class="text-[#666] dark:text-gray-400 font-tahoma">{{ receiptDetail.sendDate }}</div>

                        <div class="font-bold text-[#555] dark:text-gray-400 text-right pr-4">{{
                            t('mail.receipt.total_recipients') }}</div>
                        <div class="text-[#333] dark:text-gray-200 font-bold">{{ receiptDetail.recipients.length }}{{
                            t('common.people') || '명' }}</div>
                    </div>
                </div>

                <!-- Recipient List Table (Inside Content) -->
                <div class="p-0">
                    <table class="w-full text-[12px] border-collapse bg-white dark:bg-zinc-900">
                        <thead class="bg-[#F9F9F9] dark:bg-zinc-800 border-b border-gray-200 dark:border-zinc-700">
                            <tr>
                                <th
                                    class="py-2 px-3 text-left font-medium text-gray-600 dark:text-gray-400 border-r border-gray-200 dark:border-zinc-700 w-[40%] text-[13px]">
                                    {{ t('mail.write.to') }}</th>
                                <th
                                    class="py-2 px-3 text-center font-medium text-gray-600 dark:text-gray-400 border-r border-gray-200 dark:border-zinc-700 w-[150px] text-[13px]">
                                    {{ t('mail.receipt.read_status') }}</th>
                                <th
                                    class="py-2 px-3 text-center font-medium text-gray-600 dark:text-gray-400 border-r border-gray-200 dark:border-zinc-700 w-[180px] text-[13px]">
                                    {{ t('mail.receipt.read_date') }}</th>
                                <th
                                    class="py-2 px-3 text-center font-medium text-gray-600 dark:text-gray-400 w-[100px] text-[13px]">
                                    {{ t('common.cancel') }}</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-100 dark:divide-zinc-800">
                            <tr v-for="(item, idx) in receiptDetail.recipients" :key="idx"
                                class="hover:bg-gray-50 dark:hover:bg-zinc-800/50 transition-colors">
                                <td class="py-2 px-3 border-r border-gray-100 dark:border-zinc-800">
                                    <span class="font-bold text-gray-700 dark:text-gray-300">{{ item.name }}</span>
                                    <span class="text-gray-500 ml-1">&lt;{{ item.email }}&gt;</span>
                                    <span v-if="!item.isInternal"
                                        class="ml-2 text-[10px] bg-gray-100 dark:bg-zinc-700 text-gray-500 px-1 rounded">External</span>
                                </td>
                                <td class="py-2 px-3 text-center border-r border-gray-100 dark:border-zinc-800">
                                    <div class="flex items-center justify-center gap-1">
                                        <span v-if="item.status === 'read'"
                                            class="text-blue-600 font-bold flex items-center gap-1">
                                            <CheckCircle2 class="w-3 h-3" /> {{ t('mail.receipt.read') || 'Read' }}
                                        </span>
                                        <span v-else class="text-gray-400">{{ t('mail.receipt.unread') }}</span>
                                    </div>
                                </td>
                                <td
                                    class="py-2 px-3 text-center border-r border-gray-100 dark:border-zinc-800 font-tahoma text-gray-600 dark:text-gray-400">
                                    {{ item.readDate !== '-' ? item.readDate : '-' }}
                                </td>
                                <td class="py-2 px-3 text-center">
                                    <Button v-if="item.status === 'unread' && item.isInternal" size="xs"
                                        variant="outline"
                                        class="h-[22px] text-[11px] px-2 text-red-600 border-red-200 hover:bg-red-50 dark:border-red-900/50 dark:hover:bg-red-900/20"
                                        @click="cancelSending(item)">
                                        {{ t('mail.receipt.cancel_sending') }}
                                    </Button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>
