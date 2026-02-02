<script setup lang="ts">
import { useI18n } from 'vue-i18n'
import { Button } from '@/components/ui/button'
import { ArrowLeft, Edit, Trash2, FileText, Download } from 'lucide-vue-next'

const { t } = useI18n()
const emit = defineEmits(['close'])

// Mock data - would come from props or API
const post = {
    id: '1',
    subject: '게시판 UI 개선 작업 진행 상황',
    author: '김개발',
    date: '2026-01-30 14:00',
    views: 42,
    content: `
안녕하세요,

게시판 UI 개선 작업이 진행 중입니다.

주요 개선 사항:
1. 게시글 작성 화면 완성
2. 에디터 툴바 추가
3. 파일 첨부 기능 구현
4. 다국어 지원

감사합니다.
    `,
    attachments: [
        { name: 'screenshot.png', size: 245680 },
        { name: 'document.pdf', size: 1024000 }
    ]
}

const goBack = () => {
    emit('close')
}

const handleEdit = () => {
    alert('수정 기능 (Mock)')
}

const handleDelete = () => {
    if (confirm('게시글을 삭제하시겠습니까?')) {
        alert('삭제되었습니다. (Mock)')
        emit('close')
    }
}

const downloadFile = (file: { name: string, size: number }) => {
    alert(`파일 다운로드: ${file.name} (Mock)`)
}
</script>

<template>
    <div class="flex flex-col h-full bg-[#F0F0F2] dark:bg-zinc-900 font-dotum">
        <!-- Title Header -->
        <div class="px-4 py-3 bg-white border-b border-gray-200 dark:bg-zinc-900 dark:border-zinc-700">
            <h2 class="text-lg font-bold text-gray-800 dark:text-gray-100">
                {{ t('board.read.title') || '게시글 보기' }}
            </h2>
        </div>

        <!-- Toolbar -->
        <div
            class="px-4 py-2 border-b border-legacy-border bg-[#F7F7F7] dark:bg-zinc-900 flex flex-wrap items-center gap-1 shadow-sm">
            <Button variant="outline" size="sm" @click="goBack"
                class="h-[28px] bg-white text-[#444] text-[11px] border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <ArrowLeft class="h-3.5 w-3.5 mr-1.5" /> {{ t('common.back') || '목록' }}
            </Button>
            <Button variant="outline" size="sm" @click="handleEdit"
                class="h-[28px] bg-white text-[#444] text-[11px] border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Edit class="h-3.5 w-3.5 mr-1.5 text-blue-600 dark:text-blue-400" /> {{ t('common.edit') }}
            </Button>
            <Button variant="outline" size="sm" @click="handleDelete"
                class="h-[28px] bg-white text-[#444] text-[11px] border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Trash2 class="h-3.5 w-3.5 mr-1.5 text-red-600 dark:text-red-400" /> {{ t('common.delete') }}
            </Button>
        </div>

        <!-- Content Area -->
        <div class="flex-1 overflow-auto p-4 bg-[#F0F0F2] dark:bg-zinc-900">
            <div class="w-full border border-legacy-border bg-white dark:bg-zinc-800 shadow-sm">
                <!-- Post Header -->
                <div class="border-b border-legacy-border bg-[#F9F9F9] dark:bg-zinc-700/50 p-4">
                    <h3 class="text-[16px] font-bold text-gray-800 dark:text-gray-100 mb-3">
                        {{ post.subject }}
                    </h3>
                    <div class="flex items-center gap-4 text-[12px] text-gray-600 dark:text-gray-400">
                        <div class="flex items-center gap-1">
                            <span class="font-bold">{{ t('board.author') || '작성자' }}:</span>
                            <span>{{ post.author }}</span>
                        </div>
                        <div class="flex items-center gap-1">
                            <span class="font-bold">{{ t('board.date') || '작성일' }}:</span>
                            <span>{{ post.date }}</span>
                        </div>
                        <div class="flex items-center gap-1">
                            <span class="font-bold">{{ t('board.views') || '조회수' }}:</span>
                            <span>{{ post.views }}</span>
                        </div>
                    </div>
                </div>

                <!-- Attachments -->
                <div v-if="post.attachments.length > 0"
                    class="border-b border-legacy-border bg-[#FAFAFA] dark:bg-zinc-700/30 p-3">
                    <div class="flex items-center gap-2 mb-2">
                        <FileText class="h-4 w-4 text-gray-600 dark:text-gray-400" />
                        <span class="text-[12px] font-bold text-gray-700 dark:text-gray-300">
                            {{ t('board.attach') || '첨부파일' }} ({{ post.attachments.length }})
                        </span>
                    </div>
                    <div class="space-y-1">
                        <div v-for="(file, idx) in post.attachments" :key="idx"
                            class="flex items-center gap-2 text-[11px] bg-white dark:bg-zinc-700 p-2 rounded border border-gray-200 dark:border-zinc-600">
                            <FileText class="h-3 w-3 text-blue-600 dark:text-blue-400 flex-shrink-0" />
                            <span class="flex-1">{{ file.name }}</span>
                            <span class="text-gray-400 flex-shrink-0">{{ Math.round(file.size / 1024) }}KB</span>
                            <button @click="downloadFile(file)"
                                class="flex-shrink-0 text-blue-600 hover:text-blue-700 dark:text-blue-400">
                                <Download class="h-3 w-3" />
                            </button>
                        </div>
                    </div>
                </div>

                <!-- Post Content -->
                <div class="p-6">
                    <div class="text-[13px] leading-relaxed text-gray-800 dark:text-gray-200 whitespace-pre-wrap">
                        {{ post.content }}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
