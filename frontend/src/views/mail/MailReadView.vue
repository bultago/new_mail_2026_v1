<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Button } from '@/components/ui/button'
import {
    Reply, Forward, Trash2,
    ChevronLeft, Paperclip, Download, FileText,

} from 'lucide-vue-next'
import { mockMessages } from '@/mocks/mailData'
import { useI18n } from 'vue-i18n'

const route = useRoute()
const router = useRouter()
const { t } = useI18n()
const messageId = route.params.id as string

const message = computed(() => {
    return mockMessages.find(m => m.id === messageId)
})

const handleBack = () => {
    router.back()
}

// Mock formatting details
const headerDate = computed(() => {
    if (!message.value) return ''
    return new Date(message.value.date).toLocaleString('ko-KR', {
        year: 'numeric', month: '2-digit', day: '2-digit',
        hour: '2-digit', minute: '2-digit', weekday: 'short'
    })
})
</script>

<template>
    <div class="flex flex-col h-full bg-white dark:bg-zinc-900" v-if="message">

        <!-- Top Action Bar -->
        <div class="px-4 py-2 border-b border-legacy-border bg-[#F7F7F7] dark:bg-zinc-900 flex items-center gap-1">
            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700"
                @click="handleBack">
                <ChevronLeft class="h-3.5 w-3.5 mr-1" /> {{ t('mail.read.back') }}
            </Button>

            <div class="h-4 w-[1px] bg-[#CCC] mx-2"></div>

            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Reply class="h-3.5 w-3.5 mr-1 text-blue-600 dark:text-blue-400" /> {{ t('mail.list.toolbar.reply') }}
            </Button>
            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Forward class="h-3.5 w-3.5 mr-1 text-blue-600 dark:text-blue-400" /> {{ t('mail.list.toolbar.forward')
                }}
            </Button>

            <div class="h-4 w-[1px] bg-[#CCC] mx-2"></div>

            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Trash2 class="h-3.5 w-3.5 mr-1 text-red-600 dark:text-red-400" /> {{ t('mail.list.toolbar.delete') }}
            </Button>
        </div>

        <!-- Read Content Area -->
        <div class="p-6 flex-1 overflow-auto bg-white dark:bg-zinc-900">
            <div class="border border-legacy-border bg-white dark:bg-zinc-100 shadow-sm max-w-[1000px]">
                <!-- Header Info (Light in dark mode for contrast with body or keep dark? User said 'only body bright'. So header should be dark?) -->
                <!-- User said: '메일 조회 화면의 본문만 밝은색을 유지하고 나머지는 다크모드에 맞게 수정해' -->
                <!-- So Outer container = Dark. Header Info = Dark. Body = Light. -->
                <div class="bg-[#F9F9F9] dark:bg-zinc-800 border-b border-legacy-border p-4">
                    <div class="mb-3">
                        <h1 class="text-[18px] font-bold text-[#333] dark:text-white tracking-tight leading-tight">
                            <span
                                class="inline-block px-1.5 py-0.5 rounded bg-[#EEE] dark:bg-zinc-700 text-[#666] dark:text-gray-300 text-[11px] font-normal align-middle mr-2 border border-[#DDD] dark:border-zinc-600">{{
                                    t('mail.read.received_mail') }}</span>
                            {{ message.subject }}
                        </h1>
                    </div>
                    <div class="grid grid-cols-[80px_1fr] gap-y-1 text-[12px]">
                        <div class="font-bold text-[#555] dark:text-gray-400 text-right pr-4">{{
                            t('mail.list.headers.from') }}</div>
                        <div class="flex items-center">
                            <span class="font-bold text-[#333] dark:text-gray-200">{{ message.from.name }}</span>
                            <span class="text-[#666] dark:text-gray-400 ml-1">&lt;{{ message.from.email }}&gt;</span>
                            <Button variant="ghost" size="sm"
                                class="h-[20px] px-1 ml-2 text-[10px] text-blue-600 dark:text-blue-400 hover:text-blue-800">+
                                {{ t('mail.read.add_address') }}</Button>
                        </div>

                        <div class="font-bold text-[#555] dark:text-gray-400 text-right pr-4">{{ t('mail.write.to') }}
                        </div>
                        <div class="text-[#444] dark:text-gray-300">
                            <span v-for="(to, idx) in message.to" :key="idx">
                                {{ to.name }} &lt;{{ to.email }}&gt;<span v-if="idx < message.to.length - 1">, </span>
                            </span>
                        </div>

                        <div class="font-bold text-[#555] dark:text-gray-400 text-right pr-4">{{ t('mail.read.date') }}
                        </div>
                        <div class="text-[#666] dark:text-gray-400 font-tahoma">{{ headerDate }}</div>
                    </div>
                </div>

                <!-- Attachments -->
                <div v-if="message.hasAttachment"
                    class="bg-[#FFFBE6] dark:bg-yellow-900/20 px-4 py-3 border-b border-legacy-border flex items-start gap-4">
                    <div class="pt-0.5">
                        <Paperclip class="h-4 w-4 text-[#555] dark:text-gray-400" />
                    </div>
                    <div class="flex-1">
                        <div class="text-[12px] font-bold text-[#333] dark:text-gray-200 mb-1">
                            {{ t('mail.read.attachments') }} <span class="text-legacy-blue dark:text-blue-400">1{{
                                t('common.count', '개') }}</span> (25KB)
                            <span class="text-[#999] font-normal mx-2">|</span>
                            <a href="#" class="text-legacy-blue dark:text-blue-400 hover:underline">{{
                                t('mail.read.save_all') }}</a>
                        </div>
                        <!-- Mock Attachment Item -->
                        <div
                            class="flex items-center gap-2 text-[12px] p-1 hover:bg-[#F0F0F0] dark:hover:bg-zinc-700 rounded cursor-pointer max-w-fit">
                            <FileText class="h-3.5 w-3.5 text-gray-500 dark:text-gray-400" />
                            <span class="text-[#333] dark:text-gray-200">Project_Specs_v1.pdf</span>
                            <span class="text-[#888] dark:text-gray-500">(25KB)</span>
                            <Download class="h-3.5 w-3.5 text-gray-400 hover:text-blue-600" />
                        </div>
                    </div>
                </div>

                <!-- Body (Explicitly Light Background for readability of email content) -->
                <div class="p-8 min-h-[300px] text-[#333] text-[13px] leading-relaxed bg-white">
                    <!-- Mock Body Content -->
                    <div v-if="message.preview">
                        {{ message.preview }}
                        <br /><br />
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut
                            labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco
                            laboris nisi ut aliquip ex ea commodo consequat.</p>
                        <br />
                        <div class="border-t border-[#EEE] pt-4 mt-4 text-[#888]">
                            <p class="font-bold">{{ message.from.name }}</p>
                            <p>Terrace Technologies Inc.</p>
                            <p>Mobile: 010-1234-5678</p>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>

    <div class="flex flex-col h-full bg-white text-center text-gray-500 p-8" v-else>
        Message not found.
    </div>
</template>

<style scoped>
.font-tahoma {
    font-family: Tahoma, sans-serif;
}
</style>
