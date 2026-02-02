<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Bell, Shield, Megaphone } from 'lucide-vue-next'

const { t } = useI18n()

// Mock Preferences
const mailBrowser = ref(true)
const mailPush = ref(false)
const securityLogin = ref(true)
const marketingAgree = ref(false)

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.notification') }} > {{ t('menu_conf.notification') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-6">

            <!-- Mail Notifications -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <Bell class="w-5 h-5 text-gray-500" />
                    메일 알림
                </h2>
                <div class="space-y-4">
                    <div class="flex items-center justify-between py-2 border-b border-gray-100 dark:border-zinc-700">
                        <div>
                            <div class="text-sm font-medium text-gray-900 dark:text-white">브라우저 알림</div>
                            <div class="text-xs text-gray-500">새 메일 도착 시 브라우저 데스크탑 알림을 받습니다.</div>
                        </div>
                        <label class="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="mailBrowser" class="sr-only peer">
                            <div
                                class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600">
                            </div>
                        </label>
                    </div>
                    <div class="flex items-center justify-between py-2">
                        <div>
                            <div class="text-sm font-medium text-gray-900 dark:text-white">모바일 앱 푸시</div>
                            <div class="text-xs text-gray-500">모바일 앱에서 푸시 알림을 받습니다.</div>
                        </div>
                        <label class="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="mailPush" class="sr-only peer">
                            <div
                                class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600">
                            </div>
                        </label>
                    </div>
                </div>
            </div>

            <!-- Security Notifications -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <Shield class="w-5 h-5 text-gray-500" />
                    보안 알림
                </h2>
                <div class="space-y-4">
                    <div class="flex items-center justify-between py-2">
                        <div>
                            <div class="text-sm font-medium text-gray-900 dark:text-white">로그인 알림</div>
                            <div class="text-xs text-gray-500">새로운 환경에서 로그인 시 알림을 받습니다.</div>
                        </div>
                        <label class="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="securityLogin" class="sr-only peer">
                            <div
                                class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600">
                            </div>
                        </label>
                    </div>
                </div>
            </div>

            <!-- Marketing Notifications -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <Megaphone class="w-5 h-5 text-gray-500" />
                    혜택 및 소식
                </h2>
                <div class="space-y-4">
                    <div class="flex items-center justify-between py-2">
                        <div>
                            <div class="text-sm font-medium text-gray-900 dark:text-white">마케팅 정보 수신 동의</div>
                            <div class="text-xs text-gray-500">이벤트 및 프로모션 혜택 알림을 받습니다.</div>
                        </div>
                        <label class="relative inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="marketingAgree" class="sr-only peer">
                            <div
                                class="w-11 h-6 bg-gray-200 peer-focus:outline-none rounded-full peer dark:bg-gray-700 peer-checked:after:translate-x-full peer-checked:after:border-white after:content-[''] after:absolute after:top-[2px] after:left-[2px] after:bg-white after:border-gray-300 after:border after:rounded-full after:h-5 after:w-5 after:transition-all peer-checked:bg-blue-600">
                            </div>
                        </label>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
