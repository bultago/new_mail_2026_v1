<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useTheme } from '@/composables/useTheme'
import { Button } from '@/components/ui/button'
import {
    Reply, ReplyAll, Forward, Trash2, Printer,
    Paperclip, FileText, X, AlertCircle, Loader2
} from 'lucide-vue-next'
import { mockMessages } from '@/mocks/mailData'

const route = useRoute()
const { initTheme } = useTheme()
const messageId = route.params.id as string

onMounted(() => {
    initTheme()
})

const message = computed(() => {
    return mockMessages.find(m => m.id === messageId)
})

const handleClose = () => {
    window.close()
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
    <div class="flex flex-col h-screen bg-white dark:bg-zinc-900 overflow-hidden" v-if="message">

        <!-- Popup Title Bar (Legacy Style) -->
        <div
            class="h-[40px] px-4 flex items-center justify-between text-white bg-legacy-blue dark:bg-zinc-800 select-none">
            <h2 class="text-[13px] font-bold truncate pr-4">{{ message.subject }}</h2>
            <button @click="handleClose" class="hover:bg-white/20 p-1 rounded transition-colors">
                <X class="h-4 w-4 text-white" />
            </button>
        </div>

        <!-- Popup Actions (Legacy: Delete, Spam, Reply, Print) -->
        <div class="px-4 py-2 border-b border-legacy-border bg-[#F7F7F7] dark:bg-zinc-900 flex items-center gap-1">
            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Trash2 class="h-3.5 w-3.5 mr-1 text-red-600 dark:text-red-400" /> 삭제
            </Button>
            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <AlertCircle class="h-3.5 w-3.5 mr-1 text-red-500 dark:text-red-400" /> 스팸신고
            </Button>

            <div class="h-4 w-[1px] bg-[#CCC] mx-2"></div>

            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Reply class="h-3.5 w-3.5 mr-1 text-blue-600 dark:text-blue-400" /> 답장
            </Button>
            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <ReplyAll class="h-3.5 w-3.5 mr-1 text-blue-600 dark:text-blue-400" /> 전체답장
            </Button>
            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Forward class="h-3.5 w-3.5 mr-1 text-blue-600 dark:text-blue-400" /> 전달
            </Button>

            <div class="h-4 w-[1px] bg-[#CCC] mx-2"></div>

            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Printer class="h-3.5 w-3.5 mr-1 text-gray-500 dark:text-gray-400" /> 인쇄
            </Button>
            <Button variant="outline" size="sm"
                class="h-[28px] bg-white text-[#444] text-[11px] ml-auto dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700"
                @click="handleClose">
                <X class="h-3.5 w-3.5 mr-1" /> 닫기
            </Button>
        </div>

        <!-- Read Content Area -->
        <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900 p-4">
            <!-- Header Info -->
            <div
                class="border border-legacy-border bg-white dark:bg-zinc-100 shadow-sm max-w-[1000px] mx-auto min-h-full flex flex-col">
                <div class="bg-[#F9F9F9] dark:bg-zinc-800 border-b border-legacy-border p-4">
                    <div class="mb-3">
                        <h1 class="text-[18px] font-bold text-[#333] dark:text-white tracking-tight leading-tight">
                            {{ message.subject }}
                        </h1>
                    </div>
                    <!-- Compact Info Grid for Popup -->
                    <div class="grid grid-cols-[70px_1fr] gap-y-1 text-[12px]">
                        <div class="font-bold text-[#555] dark:text-gray-400 text-right pr-4">보낸사람</div>
                        <div class="flex items-center">
                            <span class="font-bold text-[#333] dark:text-gray-200">{{ message.from.name }}</span>
                            <span class="text-[#666] dark:text-gray-400 ml-1">&lt;{{ message.from.email }}&gt;</span>
                        </div>

                        <div class="font-bold text-[#555] dark:text-gray-400 text-right pr-4">받는사람</div>
                        <div class="text-[#444] dark:text-gray-300">
                            <span v-for="(to, idx) in message.to" :key="idx">
                                {{ to.name }}<span v-if="idx < message.to.length - 1">, </span>
                            </span>
                        </div>

                        <div class="font-bold text-[#555] dark:text-gray-400 text-right pr-4">보낸날짜</div>
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
                            첨부파일 <span class="text-legacy-blue dark:text-blue-400">1개</span> (25KB)
                        </div>
                        <div
                            class="flex items-center gap-2 text-[12px] p-1 hover:bg-[#F0F0F0] dark:hover:bg-zinc-700 rounded cursor-pointer max-w-fit">
                            <FileText class="h-3.5 w-3.5 text-gray-500 dark:text-gray-400" />
                            <span class="text-[#333] dark:text-gray-200">Attachment.pdf</span>
                        </div>
                    </div>
                </div>

                <!-- Body -->
                <div class="p-6 text-[#333] text-[13px] leading-relaxed bg-white flex-1">
                    <div v-if="message.preview">
                        {{ message.preview }}
                        <br /><br />
                        <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit...</p>
                        <br />
                        <div class="border-t border-[#EEE] pt-4 mt-4 text-[#888]">
                            <p class="font-bold">{{ message.from.name }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="flex flex-col h-full bg-white items-center justify-center text-gray-500" v-else>
        <Loader2 class="h-8 w-8 animate-spin text-legacy-blue mb-2" />
        <span>Loading...</span>
    </div>
</template>

<style scoped>
.font-tahoma {
    font-family: Tahoma, sans-serif;
}
</style>
