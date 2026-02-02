<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'

const { t } = useI18n()

const history = ref([
    { id: 1, date: '2024-01-29 14:30:22', ip: '192.168.0.101', location: 'Seoul, KR', device: 'Chrome / Windows', status: 'success' },
    { id: 2, date: '2024-01-28 09:12:11', ip: '192.168.0.101', location: 'Seoul, KR', device: 'Chrome / Windows', status: 'success' },
    { id: 3, date: '2024-01-25 18:45:33', ip: '210.111.22.33', location: 'Busan, KR', device: 'Firefox / Mac', status: 'failed' }
])

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.security') }} > {{ t('menu_conf.history') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6">
            <div
                class="bg-white dark:bg-zinc-800 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm overflow-hidden">
                <table class="w-full text-sm text-left">
                    <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-zinc-700 dark:text-gray-400">
                        <tr>
                            <th class="px-6 py-3">일시</th>
                            <th class="px-6 py-3">IP 주소 (위치)</th>
                            <th class="px-6 py-3">기기 정보</th>
                            <th class="px-6 py-3">상태</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="item in history" :key="item.id"
                            class="bg-white border-b dark:bg-zinc-800 dark:border-zinc-700 hover:bg-gray-50 dark:hover:bg-zinc-700/50">
                            <td class="px-6 py-4 text-gray-500">{{ item.date }}</td>
                            <td class="px-6 py-4 font-medium text-gray-900 dark:text-white">
                                {{ item.ip }}
                                <span class="text-xs text-gray-400 ml-1">({{ item.location }})</span>
                            </td>
                            <td class="px-6 py-4 text-gray-600 dark:text-gray-300">{{ item.device }}</td>
                            <td class="px-6 py-4">
                                <span v-if="item.status === 'success'"
                                    class="bg-green-100 text-green-800 text-xs font-medium px-2.5 py-0.5 rounded dark:bg-green-900 dark:text-green-300">성공</span>
                                <span v-else
                                    class="bg-red-100 text-red-800 text-xs font-medium px-2.5 py-0.5 rounded dark:bg-red-900 dark:text-red-300">실패</span>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <p class="mt-4 text-xs text-gray-500 text-center">최근 90일간의 로그인 이력이 표시됩니다.</p>
        </div>
    </div>
</template>
