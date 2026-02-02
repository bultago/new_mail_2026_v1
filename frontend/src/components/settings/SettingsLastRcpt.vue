<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Save, Trash2 } from 'lucide-vue-next'

const { t } = useI18n()

const autoSave = ref(true)
const maxSaveCount = ref(20)
const savedList = ref([
    { email: 'partner@biz.com', name: '김사업', date: '2024-01-20' },
    { email: 'client@hello.org', name: '이고객', date: '2024-01-19' }
])

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.mail') }} > {{ t('conf.lastrcpt.menu') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-6">
            <!-- Settings Card -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <Save class="w-5 h-5 text-gray-500" />
                    기본 설정
                </h2>

                <div class="space-y-4">
                    <div class="flex items-center justify-between py-2">
                        <span class="text-sm font-medium text-gray-700 dark:text-gray-300">보낸 사람 자동 저장</span>
                        <label class="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="autoSave" class="sr-only peer">
                            <div
                                class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600">
                            </div>
                        </label>
                    </div>

                    <div class="flex items-center justify-between py-2">
                        <span class="text-sm font-medium text-gray-700 dark:text-gray-300">최대 저장 개수</span>
                        <select v-model="maxSaveCount"
                            class="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block p-2.5 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white">
                            <option value="10">10개</option>
                            <option value="20">20개</option>
                            <option value="50">50개</option>
                            <option value="100">100개</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- List Card -->
            <div
                class="bg-white dark:bg-zinc-800 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm overflow-hidden">
                <div
                    class="px-6 py-4 border-b border-gray-200 dark:border-zinc-700 bg-gray-50 dark:bg-zinc-800/50 flex justify-between items-center">
                    <h3 class="font-medium text-gray-900 dark:text-white">저장된 목록 ({{ savedList.length }}/{{ maxSaveCount
                        }})</h3>
                    <button class="text-xs text-red-600 hover:text-red-800 flex items-center gap-1">
                        <Trash2 class="w-3 h-3" />
                        전체 삭제
                    </button>
                </div>

                <table class="w-full text-sm text-left">
                    <thead
                        class="text-xs text-gray-700 uppercase bg-white dark:bg-zinc-800 dark:text-gray-400 border-b border-gray-200 dark:border-zinc-700">
                        <tr>
                            <th class="px-6 py-3">이름/이메일</th>
                            <th class="px-6 py-3">최근 발송일</th>
                            <th class="px-6 py-3 text-right">관리</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="item in savedList" :key="item.email"
                            class="bg-white border-b dark:bg-zinc-800 dark:border-zinc-700">
                            <td class="px-6 py-4">
                                <div class="font-medium text-gray-900 dark:text-white">{{ item.name }}</div>
                                <div class="text-xs text-gray-500">{{ item.email }}</div>
                            </td>
                            <td class="px-6 py-4 text-gray-500">{{ item.date }}</td>
                            <td class="px-6 py-4 text-right">
                                <button class="text-gray-400 hover:text-red-600">
                                    <Trash2 class="w-4 h-4" />
                                </button>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>
