<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Plus, Trash2, Edit2 } from 'lucide-vue-next'

const { t } = useI18n()

// Mock Rules
const rules = ref([
    { id: 1, name: '쇼핑몰 뉴스레터', condition: '제목에 "특가" 포함', action: '쇼핑 편지함으로 이동' },
    { id: 2, name: '사내 공지', condition: '보낸사람이 "admin@company.com"', action: '중요 편지함으로 이동' }
])

const isModalOpen = ref(false)
</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center justify-between bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.mail') }} > {{ t('menu_conf.filter') }}
            </h1>
            <button @click="isModalOpen = true"
                class="flex items-center gap-2 px-3 py-1.5 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700 transition">
                <Plus class="w-4 h-4" />
                <span>규칙 추가</span>
            </button>
        </div>

        <!-- content -->
        <div class="flex-1 overflow-y-auto p-6">
            <div
                class="bg-white dark:bg-zinc-800 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm overflow-hidden">
                <table class="w-full text-sm text-left">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-zinc-700 dark:text-gray-400">
                        <tr>
                            <th class="px-6 py-3">규칙 이름</th>
                            <th class="px-6 py-3">조건</th>
                            <th class="px-6 py-3">실행 동작</th>
                            <th class="px-6 py-3 text-right">관리</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-if="rules.length === 0">
                            <td colspan="4" class="px-6 py-8 text-center text-gray-500">
                                등록된 자동 분류 규칙이 없습니다.
                            </td>
                        </tr>
                        <tr v-for="rule in rules" :key="rule.id"
                            class="bg-white border-b dark:bg-zinc-800 dark:border-zinc-700 hover:bg-gray-50 dark:hover:bg-zinc-700/50">
                            <td class="px-6 py-4 font-medium text-gray-900 dark:text-white">{{ rule.name }}</td>
                            <td class="px-6 py-4 text-gray-600 dark:text-gray-300">{{ rule.condition }}</td>
                            <td class="px-6 py-4 text-blue-600 dark:text-blue-400">{{ rule.action }}</td>
                            <td class="px-6 py-4 text-right flex items-center justify-end gap-2">
                                <button class="p-1 text-gray-500 hover:text-blue-600 transition">
                                    <Edit2 class="w-4 h-4" />
                                </button>
                                <button class="p-1 text-gray-500 hover:text-red-600 transition">
                                    <Trash2 class="w-4 h-4" />
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <p class="mt-4 text-xs text-gray-500">
                * 메일이 도착할 때 위 목록의 위에서부터 순서대로 규칙이 적용됩니다.
            </p>
        </div>
    </div>
</template>
