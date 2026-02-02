<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Shield, Ban, CheckCircle } from 'lucide-vue-next'

const { t } = useI18n()
const activeTab = ref('basic')

const tabs = [
    { id: 'basic', label: 'menu_conf.spam_basic', icon: Shield, fallback: '기본 설정' },
    { id: 'blocked', label: 'menu_conf.spam_blocked', icon: Ban, fallback: '수신 차단' },
    { id: 'safe', label: 'menu_conf.spam_safe', icon: CheckCircle, fallback: '수신 허용' }
]

const getLabel = (key: string, fallback: string) => {
    return t(key) !== key ? t(key) : fallback
}

// Mock Data
const useSpamFilter = ref(true)
const spamScore = ref(5)
const blockedList = ref([
    { email: 'spam@example.com', date: '2024-01-15' },
    { email: 'bad@domain.com', date: '2024-01-14' }
])
const safeList = ref([
    { email: 'boss@company.com', date: '2024-01-12' }
])
</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.mail') }} > {{ t('menu_conf.spam') }}
            </h1>
        </div>

        <!-- Tabs -->
        <div class="px-6 pt-4 bg-white dark:bg-zinc-900 border-b border-gray-200 dark:border-zinc-700">
            <div class="flex gap-4">
                <button v-for="tab in tabs" :key="tab.id" @click="activeTab = tab.id"
                    class="pb-2 px-1 text-sm font-medium border-b-2 transition-colors flex items-center gap-2"
                    :class="activeTab === tab.id
                        ? 'border-blue-600 text-blue-600 dark:border-blue-400 dark:text-blue-400'
                        : 'border-transparent text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200'">
                    <component :is="tab.icon" class="w-4 h-4" />
                    {{ getLabel(tab.label, tab.fallback) }}
                </button>
            </div>
        </div>

        <!-- Content -->
        <div class="flex-1 overflow-y-auto p-6">
            <!-- Basic Settings -->
            <div v-if="activeTab === 'basic'" class="max-w-2xl space-y-6">
                <!-- Master Toggle -->
                <div
                    class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                    <div class="flex items-center justify-between">
                        <div>
                            <h3 class="font-medium text-gray-900 dark:text-gray-100 mb-1">스팸 필터 사용</h3>
                            <p class="text-sm text-gray-500 dark:text-gray-400">자동으로 스팸 메일을 분류하여 스팸 편지함으로 이동합니다.</p>
                        </div>
                        <label class="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="useSpamFilter" class="sr-only peer">
                            <div
                                class="w-11 h-6 bg-gray-200 peer-focus:outline-none peer-focus:ring-4 peer-focus:ring-blue-300 dark:peer-focus:ring-blue-800 rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all dark:border-gray-600 peer-checked:bg-blue-600">
                            </div>
                        </label>
                    </div>
                </div>

                <!-- Score Setting -->
                <div
                    class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                    <h3 class="font-medium text-gray-900 dark:text-gray-100 mb-4">스팸 점수 설정</h3>
                    <div class="space-y-4">
                        <div class="flex justify-between text-sm text-gray-600 dark:text-gray-400">
                            <span>낮음 (엄격)</span>
                            <span>높음 (느슨)</span>
                        </div>
                        <input type="range" v-model="spamScore" min="1" max="10" step="0.5"
                            class="w-full h-2 bg-gray-200 rounded-lg appearance-none cursor-pointer dark:bg-gray-700">
                        <div class="text-center font-medium text-blue-600">{{ spamScore }}점</div>
                    </div>
                </div>
            </div>

            <!-- Blocked List -->
            <div v-else-if="activeTab === 'blocked'" class="max-w-2xl">
                <div
                    class="bg-white dark:bg-zinc-800 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm overflow-hidden">
                    <table class="w-full text-sm text-left">
                        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-zinc-700 dark:text-gray-400">
                            <tr>
                                <th class="px-6 py-3">이메일</th>
                                <th class="px-6 py-3">등록일</th>
                                <th class="px-6 py-3 text-right">관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in blockedList" :key="item.email"
                                class="bg-white border-b dark:bg-zinc-800 dark:border-zinc-700">
                                <td class="px-6 py-4 font-medium">{{ item.email }}</td>
                                <td class="px-6 py-4 text-gray-500">{{ item.date }}</td>
                                <td class="px-6 py-4 text-right">
                                    <button class="text-red-600 hover:text-red-800 font-medium">삭제</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Safe List -->
            <div v-else class="max-w-2xl">
                <div
                    class="bg-white dark:bg-zinc-800 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm overflow-hidden">
                    <table class="w-full text-sm text-left">
                        <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-zinc-700 dark:text-gray-400">
                            <tr>
                                <th class="px-6 py-3">이메일</th>
                                <th class="px-6 py-3">등록일</th>
                                <th class="px-6 py-3 text-right">관리</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="item in safeList" :key="item.email"
                                class="bg-white border-b dark:bg-zinc-800 dark:border-zinc-700">
                                <td class="px-6 py-4 font-medium">{{ item.email }}</td>
                                <td class="px-6 py-4 text-gray-500">{{ item.date }}</td>
                                <td class="px-6 py-4 text-right">
                                    <button class="text-red-600 hover:text-red-800 font-medium">삭제</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>
