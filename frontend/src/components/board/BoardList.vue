<script setup lang="ts">
import { ref, computed } from 'vue'
import { Paperclip, Megaphone, Search } from 'lucide-vue-next'
import { useI18n } from 'vue-i18n'
import { Button } from '@/components/ui/button'

const { t } = useI18n()
defineProps<{ boardId: string }>()
const emit = defineEmits<{ 'post-click': [postId: string] }>()

// State
const searchType = ref('subject')
const searchKeyword = ref('')

// Mock Data
const notices = ref([
    { id: 10, subject: '[공지] 서버 정기 점검 안내', author: '관리자', date: '2026-01-29', read: 120, attach: false },
    { id: 9, subject: '[공지] 보안 정책 업데이트', author: '보안팀', date: '2026-01-20', read: 95, attach: true },
])

const posts = ref([
    { id: 101, subject: '서비스 점검 안내', author: '관리자', date: '2026-01-29', read: 45, attach: false, depth: 0 },
    { id: 100, subject: '개인정보 처리방침 변경 안내', author: '운영팀', date: '2026-01-28', read: 12, attach: true, depth: 0 },
    { id: 99, subject: '개발팀 워크샵 일정 관련', author: '김철수', date: '2026-01-27', read: 88, attach: false, depth: 0 },
    { id: 98, subject: 'Re: 개발팀 워크샵 일정 관련', author: '이영희', date: '2026-01-27', read: 32, attach: false, depth: 1 },
    { id: 97, subject: '1월 식단표', author: '총무팀', date: '2026-01-01', read: 150, attach: true, depth: 0 },
])

const handleSearch = () => {
    alert(`Searching for [${searchKeyword.value}] in [${searchType.value}]`)
}
</script>

<template>
    <div class="flex flex-col h-full overflow-hidden bg-white dark:bg-zinc-900">
        <!-- Search Bar (Top) -->
        <div
            class="px-4 py-3 bg-gray-50 dark:bg-zinc-800 border-b border-legacy-border flex items-center justify-end gap-2">
            <select v-model="searchType"
                class="h-7 text-xs border border-gray-300 rounded px-2 dark:bg-zinc-700 dark:border-zinc-600">
                <option value="subject">제목</option>
                <option value="content">내용</option>
                <option value="creator">작성자</option>
                <option value="attach">첨부파일</option>
            </select>
            <input type="text" v-model="searchKeyword" @keyup.enter="handleSearch"
                class="h-7 w-48 text-xs border border-gray-300 rounded px-2 dark:bg-zinc-700 dark:border-zinc-600" />
            <Button size="sm" class="h-7 px-3 bg-gray-600 hover:bg-gray-700 text-white text-xs" @click="handleSearch">
                <Search class="w-3 h-3 mr-1" /> 검색
            </Button>
        </div>

        <!-- Table Header -->
        <div
            class="grid grid-cols-[40px_60px_40px_1fr_100px_100px_60px] bg-gray-100 dark:bg-zinc-800 border-b border-gray-300 dark:border-zinc-700 font-bold text-xs text-gray-700 dark:text-gray-300 text-center py-2 select-none">
            <div><input type="checkbox" /></div>
            <div>No</div>
            <div>File</div>
            <div>제목</div>
            <div>작성자</div>
            <div>날짜</div>
            <div>조회</div>
        </div>

        <!-- Table Body -->
        <div class="flex-1 overflow-y-auto">
            <!-- Notices -->
            <div v-for="notice in notices" :key="'notice-' + notice.id" @click="emit('post-click', String(notice.id))"
                class="grid grid-cols-[40px_60px_40px_1fr_100px_100px_60px] bg-blue-50/50 dark:bg-blue-900/10 border-b border-blue-100 dark:border-zinc-700 hover:bg-blue-100/50 dark:hover:bg-zinc-800 items-center py-2 text-sm text-center transition-colors cursor-pointer font-medium">
                <!-- Custom Background for Notices -->

                <div class="flex justify-center"><input type="checkbox" disabled /></div>
                <div class="flex justify-center">
                    <span class="px-1.5 py-0.5 bg-blue-100 text-blue-600 text-[10px] rounded font-bold">공지</span>
                </div>
                <div class="flex justify-center">
                    <Paperclip v-if="notice.attach" class="w-3.5 h-3.5 text-gray-400" />
                </div>
                <div class="text-left px-2 truncate text-blue-800 dark:text-blue-300 font-bold">
                    {{ notice.subject }}
                </div>
                <div class="text-gray-600 dark:text-gray-400">{{ notice.author }}</div>
                <div class="text-gray-500 text-xs">{{ notice.date }}</div>
                <div class="text-gray-500 text-xs">{{ notice.read }}</div>
            </div>

            <!-- Normal Posts -->
            <div v-for="post in posts" :key="post.id" @click="emit('post-click', String(post.id))"
                class="grid grid-cols-[40px_60px_40px_1fr_100px_100px_60px] border-b border-gray-100 dark:border-zinc-800 hover:bg-gray-50 dark:hover:bg-zinc-800 items-center py-2 text-sm text-center transition-colors cursor-pointer">

                <div class="flex justify-center"><input type="checkbox" /></div>
                <div class="text-gray-500">{{ post.id }}</div>
                <div class="flex justify-center">
                    <Paperclip v-if="post.attach" class="w-3.5 h-3.5 text-gray-400" />
                </div>

                <!-- Subject with Indentation -->
                <div class="text-left px-2 truncate font-medium text-gray-800 dark:text-gray-200">
                    <div :style="{ paddingLeft: post.depth * 20 + 'px' }" class="flex items-center">
                        <span v-if="post.depth > 0"
                            class="mr-1 text-gray-400 transform rotate-90 inline-block text-[10px]">└</span>
                        {{ post.subject }}
                    </div>
                </div>

                <div class="text-gray-600 dark:text-gray-400">{{ post.author }}</div>
                <div class="text-gray-500 text-xs">{{ post.date }}</div>
                <div class="text-gray-500 text-xs">{{ post.read }}</div>
            </div>

            <!-- Empty State -->
            <div v-if="posts.length === 0 && notices.length === 0"
                class="flex items-center justify-center h-full text-gray-400 text-sm">
                게시물이 없습니다.
            </div>
        </div>
    </div>
</template>
