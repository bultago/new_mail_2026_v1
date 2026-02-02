<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { ShieldCheck, Smartphone, Check } from 'lucide-vue-next'

const { t } = useI18n()

const isEnabled = ref(false)
const isSetupMode = ref(false)

const startSetup = () => {
    isSetupMode.value = true
}

const completeSetup = () => {
    isSetupMode.value = false
    isEnabled.value = true
}

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.security') }} > {{ t('menu_conf.2fa') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6">
            <!-- Status Card -->
            <div
                class="bg-white dark:bg-zinc-800 p-8 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm text-center">
                <div class="inline-flex items-center justify-center w-16 h-16 rounded-full mb-6"
                    :class="isEnabled ? 'bg-green-100 text-green-600' : 'bg-gray-100 text-gray-500'">
                    <ShieldCheck class="w-8 h-8" />
                </div>

                <h2 class="text-2xl font-bold mb-2 text-gray-900 dark:text-white">
                    {{ isEnabled ? '2단계 인증이 활성화되었습니다' : '2단계 인증을 사용하지 않고 있습니다' }}
                </h2>
                <p class="text-gray-500 mb-8 max-w-md mx-auto">
                    로그인 시 아이디/비밀번호 외에 모바일 앱의 OTP 번홀르 추가로 입력하여 계정을 더욱 안전하게 보호하세요.
                </p>

                <div v-if="!isEnabled && !isSetupMode">
                    <button @click="startSetup"
                        class="px-6 py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 font-medium transition">
                        2단계 인증 설정하기
                    </button>
                </div>

                <div v-if="isEnabled">
                    <button @click="isEnabled = false"
                        class="px-6 py-3 border border-gray-300 text-gray-700 rounded-lg hover:bg-gray-50 font-medium transition dark:border-zinc-600 dark:text-gray-300 dark:hover:bg-zinc-700">
                        해제하기
                    </button>
                </div>
            </div>

            <!-- Setup Wizard Mock -->
            <div v-if="isSetupMode"
                class="mt-8 bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm animate-in fade-in slide-in-from-bottom-4">
                <h3 class="font-bold text-lg mb-4">OTP 앱 등록</h3>
                <div class="flex items-start gap-8">
                    <div class="w-40 h-40 bg-gray-100 rounded-lg flex items-center justify-center">
                        <span class="text-xs text-gray-400">QR Code Mock</span>
                    </div>
                    <div class="space-y-4">
                        <p class="text-sm text-gray-600 dark:text-gray-300">
                            1. Google Authenticator 등 OTP 앱을 실행하세요.<br>
                            2. QR 코드를 스캔하세요.<br>
                            3. 앱에 표시된 인증번호 6자리를 입력하세요.
                        </p>
                        <div class="flex gap-2">
                            <input type="text" placeholder="000000"
                                class="w-32 px-3 py-2 border rounded-md text-center tracking-widest font-mono text-lg">
                            <button @click="completeSetup"
                                class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700">확인</button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
