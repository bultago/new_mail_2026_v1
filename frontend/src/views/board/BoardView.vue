<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import BoardSidebar from '@/components/board/BoardSidebar.vue'
import BoardList from '@/components/board/BoardList.vue'
import BoardWrite from '@/components/board/BoardWrite.vue'
import BoardRead from '@/components/board/BoardRead.vue'
import { Button } from '@/components/ui/button'
import { PenSquare, Trash2 } from 'lucide-vue-next'

const { t } = useI18n()
const currentBoardId = ref('notice')

const handleBoardSelect = (id: string) => {
    currentBoardId.value = id
    isWriting.value = false
    isReading.value = false
}

const isWriting = ref(false)
const isReading = ref(false)
const currentPostId = ref<string>('1')

const handleWrite = () => {
    isWriting.value = true
    isReading.value = false
}

const handlePostClick = (postId: string) => {
    currentPostId.value = postId
    isReading.value = true
    isWriting.value = false
}

const closeWrite = () => {
    isWriting.value = false
}

const closeRead = () => {
    isReading.value = false
}
</script>

<template>
    <div class="flex h-full bg-[#F0F0F2] dark:bg-zinc-900 font-dotum overflow-hidden">
        <!-- Sidebar -->
        <BoardSidebar @select="handleBoardSelect" />

        <!-- Main Content -->
        <div class="flex-1 flex flex-col min-w-0">
            <!-- Toolbar -->
            <div
                class="px-4 py-3 bg-white dark:bg-zinc-900 border-b border-legacy-border flex items-center justify-between shadow-sm">
                <h2 class="text-lg font-bold text-gray-800 dark:text-gray-100 flex items-center gap-2">
                    <span class="text-sm font-normal text-gray-500">게시판 ></span>
                    {{ currentBoardId }}
                </h2>

                <div class="flex items-center gap-2">
                    <Button variant="outline" size="sm" @click="handleWrite"
                        class="bg-blue-600 text-white hover:bg-blue-700 border-none h-[28px] text-xs">
                        <PenSquare class="w-3.5 h-3.5 mr-1.5" />
                        {{ t('common.write') || '글쓰기' }}
                    </Button>
                    <Button variant="outline" size="sm"
                        class="bg-white text-red-600 border-gray-200 hover:bg-red-50 h-[28px] text-xs">
                        <Trash2 class="w-3.5 h-3.5 mr-1.5" />
                        {{ t('common.delete') || '삭제' }}
                    </Button>
                </div>
            </div>

            <!-- List Area -->
            <div class="flex-1 overflow-hidden p-4">
                <div
                    class="h-full bg-white dark:bg-zinc-800 border border-legacy-border shadow-sm rounded-sm overflow-hidden flex flex-col">
                    <div class="flex-1 overflow-hidden">
                        <BoardWrite v-if="isWriting" :boardId="currentBoardId" @close="closeWrite" />
                        <BoardRead v-else-if="isReading" @close="closeRead" />
                        <BoardList v-else :boardId="currentBoardId" @post-click="handlePostClick" />
                    </div>

                    <!-- Pagination (Mock) - Hide when writing or reading -->
                    <div v-if="!isWriting && !isReading"
                        class="h-10 border-t border-gray-100 dark:border-zinc-700 flex items-center justify-center gap-2 bg-gray-50 dark:bg-zinc-900">
                        <button
                            class="w-6 h-6 flex items-center justify-center rounded hover:bg-gray-200 dark:hover:bg-zinc-700 text-xs text-gray-500">&lt;</button>
                        <button
                            class="w-6 h-6 flex items-center justify-center rounded bg-blue-600 text-white text-xs font-bold">1</button>
                        <button
                            class="w-6 h-6 flex items-center justify-center rounded hover:bg-gray-200 dark:hover:bg-zinc-700 text-xs text-gray-500">2</button>
                        <button
                            class="w-6 h-6 flex items-center justify-center rounded hover:bg-gray-200 dark:hover:bg-zinc-700 text-xs text-gray-500">&gt;</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
