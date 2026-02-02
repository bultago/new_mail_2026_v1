<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Mail, Plus, Trash2, RefreshCw } from 'lucide-vue-next'

const { t } = useI18n()

// Mock External Accounts
const accounts = ref([
    { id: 1, email: 'my.personal@gmail.com', server: 'pop.gmail.com', port: 995, lastSync: '2024-01-29 14:00' },
    { id: 2, email: 'work.old@naver.com', server: 'pop.naver.com', port: 995, lastSync: '2024-01-29 12:00' }
])

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center justify-between bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.mail') }} > {{ t('menu_conf.external') }}
            </h1>
            <button
                class="flex items-center gap-2 px-3 py-1.5 bg-blue-600 text-white rounded-md text-sm hover:bg-blue-700 transition">
                <Plus class="w-4 h-4" />
                <span>계정 추가</span>
            </button>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-6">

            <div class="grid grid-cols-1 gap-4">
                <div v-for="acc in accounts" :key="acc.id"
                    class="bg-white dark:bg-zinc-800 p-4 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm flex items-center justify-between">
                    <div class="flex items-center gap-4">
                        <div class="p-3 bg-blue-50 dark:bg-blue-900/20 rounded-full text-blue-600 dark:text-blue-400">
                            <Mail class="w-6 h-6" />
                        </div>
                        <div>
                            <h3 class="font-bold text-gray-900 dark:text-white">{{ acc.email }}</h3>
                            <div class="text-sm text-gray-500 flex items-center gap-2">
                                <span>{{ acc.server }}:{{ acc.port }}</span>
                                <span class="text-gray-300">|</span>
                                <span class="flex items-center gap-1">
                                    <RefreshCw class="w-3 h-3" /> {{ acc.lastSync }}
                                </span>
                            </div>
                        </div>
                    </div>

                    <div class="flex gap-2">
                        <button
                            class="px-3 py-1.5 text-sm border border-gray-300 rounded text-gray-700 hover:bg-gray-50 dark:border-zinc-600 dark:text-gray-300 dark:hover:bg-zinc-700">설정</button>
                        <button class="p-2 text-red-600 hover:bg-red-50 rounded dark:hover:bg-red-900/20">
                            <Trash2 class="w-4 h-4" />
                        </button>
                    </div>
                </div>
            </div>

            <div v-if="accounts.length === 0" class="text-center py-12 text-gray-500">
                등록된 외부 메일 계정이 없습니다.
            </div>

        </div>
    </div>
</template>
