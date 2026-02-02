<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Search, Users, UserPlus } from 'lucide-vue-next'

const { t } = useI18n()

// Mock Shared Groups
const sharedGroups = ref([
    { id: 1, name: '기획팀 공유', items: ['홍길동', '김철수', '개발팀'] },
    { id: 2, name: '전사 공지', items: ['전체', '임원'] }
])

const activeGroup = ref(null)

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.schedule') }} > {{ t('menu_conf.scheduler') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6">
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <div class="flex items-center justify-between mb-6">
                    <h2 class="text-lg font-bold text-gray-900 dark:text-white">공유 일정 그룹 관리</h2>
                    <button
                        class="px-3 py-1.5 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700 transition flex items-center gap-2">
                        <UserPlus class="w-4 h-4" />
                        그룹 추가
                    </button>
                </div>

                <!-- Group List -->
                <div class="space-y-4">
                    <div v-for="group in sharedGroups" :key="group.id"
                        class="border border-gray-200 dark:border-zinc-700 rounded-md p-4 hover:bg-gray-50 dark:hover:bg-zinc-700/50 transition">
                        <div class="flex justify-between items-start">
                            <div>
                                <h3 class="font-medium text-gray-900 dark:text-white flex items-center gap-2">
                                    <Users class="w-4 h-4 text-gray-500" />
                                    {{ group.name }}
                                </h3>
                                <p class="text-sm text-gray-500 mt-1">
                                    멤버: {{ group.items.join(', ') }}
                                </p>
                            </div>
                            <div class="flex gap-2 text-sm">
                                <button class="text-blue-600 hover:text-blue-800">수정</button>
                                <span class="text-gray-300">|</span>
                                <button class="text-red-600 hover:text-red-800">삭제</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
