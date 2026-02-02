<script setup lang="ts">
import { ref } from 'vue'
import { Folder, FolderOpen, Megaphone, Hash } from 'lucide-vue-next'

defineEmits(['select'])

// Mock Tree Data
const boards = ref([
    { id: 'notice', name: '공지사항', type: 'notice', level: 0, expanded: true, children: [] },
    {
        id: 'dept', name: '부서 게시판', type: 'folder', level: 0, expanded: true, children: [
            { id: 'dev', name: '개발팀', type: 'board', level: 1 },
            { id: 'design', name: '디자인팀', type: 'board', level: 1 },
            { id: 'planning', name: '기획팀', type: 'board', level: 1 },
        ]
    },
    { id: 'free', name: '자유게시판', type: 'board', level: 0 },
    { id: 'qna', name: 'Q&A', type: 'board', level: 0 },
])

const activeId = ref('notice')

const toggle = (board: any) => {
    if (board.children) {
        board.expanded = !board.expanded
    }
}

const select = (board: any, emit: any) => {
    activeId.value = board.id
    emit('select', board.id)
}
</script>

<template>
    <div class="h-full bg-white dark:bg-zinc-800 border-r border-legacy-border flex flex-col w-[240px]">
        <div class="p-4 border-b border-legacy-border font-bold text-gray-700 dark:text-gray-200">
            게시판
        </div>
        <div class="flex-1 overflow-y-auto py-2">
            <div class="px-2">
                <template v-for="board in boards" :key="board.id">
                    <!-- Parent -->
                    <div class="flex items-center gap-2 px-2 py-1.5 rounded cursor-pointer transition-colors"
                        :class="activeId === board.id ? 'bg-blue-50 text-blue-600 dark:bg-zinc-700 dark:text-blue-400' : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-zinc-700'"
                        @click="board.children ? toggle(board) : select(board, $emit)">
                        <!-- Icon -->
                        <component
                            :is="board.type === 'notice' ? Megaphone : (board.children ? (board.expanded ? FolderOpen : Folder) : Hash)"
                            class="w-4 h-4 flex-shrink-0"
                            :class="board.type === 'notice' ? 'text-red-500' : (board.children ? 'text-yellow-500' : 'text-gray-400')" />
                        <span class="text-sm truncate select-none">{{ board.name }}</span>
                    </div>

                    <!-- Children -->
                    <div v-if="board.children && board.expanded"
                        class="ml-4 border-l border-gray-200 dark:border-zinc-700 pl-1">
                        <div v-for="child in board.children" :key="child.id"
                            class="flex items-center gap-2 px-2 py-1.5 rounded cursor-pointer transition-colors"
                            :class="activeId === child.id ? 'bg-blue-50 text-blue-600 dark:bg-zinc-700 dark:text-blue-400' : 'text-gray-600 dark:text-gray-400 hover:bg-gray-100 dark:hover:bg-zinc-700'"
                            @click="select(child, $emit)">
                            <Hash class="w-3.5 h-3.5 flex-shrink-0 opacity-70" />
                            <span class="text-sm truncate select-none">{{ child.name }}</span>
                        </div>
                    </div>
                </template>
            </div>
        </div>
    </div>
</template>
