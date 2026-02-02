<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { LayoutDashboard, CheckSquare, Calendar, Mail } from 'lucide-vue-next'

const { t } = useI18n()

// Layout Options
const layoutType = ref('3col') // '2col', '3col'

// Widget Options
const widgets = ref({
    unread: true,
    calendar: true,
    notices: true,
    weather: false,
    todo: false
})

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.basic') }} > {{ t('menu_conf.home') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-6">

            <!-- Dashboard Layout -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <LayoutDashboard class="w-5 h-5 text-gray-500" />
                    초기화면 레이아웃 설정
                </h2>

                <div class="flex gap-6">
                    <label class="cursor-pointer relative">
                        <input type="radio" value="3col" v-model="layoutType" class="sr-only peer">
                        <div
                            class="w-32 h-20 border-2 rounded-lg p-2 flex gap-1 peer-checked:border-blue-600 peer-checked:bg-blue-50 dark:border-zinc-600 hover:bg-gray-50 transition">
                            <div class="w-1/3 bg-gray-200 rounded"></div>
                            <div class="w-1/3 bg-gray-200 rounded"></div>
                            <div class="w-1/3 bg-gray-200 rounded"></div>
                        </div>
                        <span class="block text-center text-sm mt-2 font-medium text-gray-700 dark:text-gray-300">3단형
                            (기본)</span>
                    </label>

                    <label class="cursor-pointer relative">
                        <input type="radio" value="2col" v-model="layoutType" class="sr-only peer">
                        <div
                            class="w-32 h-20 border-2 rounded-lg p-2 flex gap-1 peer-checked:border-blue-600 peer-checked:bg-blue-50 dark:border-zinc-600 hover:bg-gray-50 transition">
                            <div class="w-1/2 bg-gray-200 rounded"></div>
                            <div class="w-1/2 bg-gray-200 rounded"></div>
                        </div>
                        <span
                            class="block text-center text-sm mt-2 font-medium text-gray-700 dark:text-gray-300">2단형</span>
                    </label>
                </div>
            </div>

            <!-- Widget Selection -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <CheckSquare class="w-5 h-5 text-gray-500" />
                    위젯 설정
                </h2>
                <p class="text-sm text-gray-500 mb-4">홈 화면에 표시할 정보를 선택하세요.</p>

                <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                    <div
                        class="flex items-center justify-between p-3 border border-gray-100 rounded bg-gray-50 dark:bg-zinc-700/50 dark:border-zinc-700">
                        <div class="flex items-center gap-2">
                            <Mail class="w-4 h-4 text-gray-500" />
                            <span class="font-medium">읽지 않은 메일</span>
                        </div>
                        <input type="checkbox" v-model="widgets.unread" class="w-4 h-4 text-blue-600 rounded">
                    </div>

                    <div
                        class="flex items-center justify-between p-3 border border-gray-100 rounded bg-gray-50 dark:bg-zinc-700/50 dark:border-zinc-700">
                        <div class="flex items-center gap-2">
                            <Calendar class="w-4 h-4 text-gray-500" />
                            <span class="font-medium">오늘의 일정</span>
                        </div>
                        <input type="checkbox" v-model="widgets.calendar" class="w-4 h-4 text-blue-600 rounded">
                    </div>

                    <div
                        class="flex items-center justify-between p-3 border border-gray-100 rounded bg-gray-50 dark:bg-zinc-700/50 dark:border-zinc-700">
                        <div class="flex items-center gap-2">
                            <span class="font-medium">사내 공지사항</span>
                        </div>
                        <input type="checkbox" v-model="widgets.notices" class="w-4 h-4 text-blue-600 rounded">
                    </div>

                    <div
                        class="flex items-center justify-between p-3 border border-gray-100 rounded bg-gray-50 dark:bg-zinc-700/50 dark:border-zinc-700">
                        <div class="flex items-center gap-2">
                            <span class="font-medium">날씨 정보</span>
                        </div>
                        <input type="checkbox" v-model="widgets.weather" class="w-4 h-4 text-blue-600 rounded">
                    </div>
                </div>
            </div>

            <div class="flex justify-end">
                <button class="px-6 py-2.5 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition font-medium">
                    저장
                </button>
            </div>
        </div>
    </div>
</template>
