<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Globe, Layout, Save } from 'lucide-vue-next'

const { t, locale } = useI18n()

const settings = ref({
    language: 'ko',
    pageSize: '20',
    writeMode: 'html',
    encoding: 'UTF-8',
    saveSent: true
})

const changeLanguage = () => {
    locale.value = settings.value.language
}
</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.basic') }} > {{ t('menu_conf.general') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-6">

            <!-- Global Settings -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <Globe class="w-5 h-5 text-gray-500" />
                    지역 및 언어 설정
                </h2>
                <div class="space-y-6 max-w-xl">
                    <div class="flex items-center justify-between">
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">언어 (Language)</label>
                        <select v-model="settings.language" @change="changeLanguage"
                            class="w-48 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block p-2.5 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white">
                            <option value="ko">한국어</option>
                            <option value="en-US">English</option>
                            <option value="ja">日本語</option>
                            <option value="zh-CN">简体中文</option>
                        </select>
                    </div>

                    <div class="flex items-center justify-between">
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">기본 인코딩</label>
                        <select v-model="settings.encoding"
                            class="w-48 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block p-2.5 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white">
                            <option value="UTF-8">UTF-8 (권장)</option>
                            <option value="EUC-KR">EUC-KR</option>
                        </select>
                    </div>
                </div>
            </div>

            <!-- Display Settings -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <Layout class="w-5 h-5 text-gray-500" />
                    화면 및 쓰기 설정
                </h2>
                <div class="space-y-6 max-w-xl">
                    <div class="flex items-center justify-between">
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">목록 개수 (페이지당)</label>
                        <select v-model="settings.pageSize"
                            class="w-48 bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block p-2.5 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white">
                            <option value="10">10개씩 보기</option>
                            <option value="15">15개씩 보기</option>
                            <option value="20">20개씩 보기</option>
                            <option value="50">50개씩 보기</option>
                        </select>
                    </div>

                    <div class="flex items-center justify-between">
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">기본 쓰기 모드</label>
                        <div class="flex gap-4">
                            <div class="flex items-center">
                                <input type="radio" value="html" v-model="settings.writeMode"
                                    class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500">
                                <label class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">에디터
                                    (HTML)</label>
                            </div>
                            <div class="flex items-center">
                                <input type="radio" value="text" v-model="settings.writeMode"
                                    class="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500">
                                <label class="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">텍스트</label>
                            </div>
                        </div>
                    </div>

                    <div class="flex items-center justify-between">
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">보낸 메일 저장</label>
                        <label class="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="settings.saveSent" class="sr-only peer">
                            <div
                                class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600">
                            </div>
                        </label>
                    </div>
                </div>
            </div>

            <div class="flex justify-end">
                <button
                    class="px-6 py-2.5 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition flex items-center gap-2 font-medium">
                    <Save class="w-4 h-4" />
                    설정 저장
                </button>
            </div>

        </div>
    </div>
</template>
