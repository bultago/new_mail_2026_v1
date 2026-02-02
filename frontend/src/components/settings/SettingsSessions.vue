<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Monitor, Phone, XCircle } from 'lucide-vue-next'

const { t } = useI18n()

const sessions = ref([
    { id: 1, current: true, device: 'Chrome on Windows', ip: '192.168.0.101', location: 'Seoul, KR', time: '방금 전' },
    { id: 2, current: false, device: 'Safari on iPhone', ip: '223.11.22.33', location: 'Seoul, KR', time: '1시간 전' }
])

const removeSession = (id: number) => {
    sessions.value = sessions.value.filter(s => s.id !== id)
}

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.security') }} > {{ t('menu_conf.sessions') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-6">
            <div class="bg-blue-50 dark:bg-blue-900/20 p-4 rounded-md border border-blue-100 dark:border-blue-800">
                <p class="text-sm text-blue-800 dark:text-blue-300">
                    현재 접속 중인 모든 기기 목록입니다. 의심스러운 활동이 있다면 해당 세션을 즉시 종료하세요.
                </p>
            </div>

            <div class="space-y-4">
                <div v-for="session in sessions" :key="session.id"
                    class="bg-white dark:bg-zinc-800 p-4 rounded-lg border shadow-sm flex items-center justify-between"
                    :class="session.current ? 'border-blue-500 ring-1 ring-blue-500' : 'border-gray-200 dark:border-zinc-700'">

                    <div class="flex items-center gap-4">
                        <div class="p-3 rounded-full bg-gray-100 dark:bg-zinc-700 text-gray-600 dark:text-gray-300">
                            <Monitor v-if="session.device.includes('Windows') || session.device.includes('Mac')"
                                class="w-6 h-6" />
                            <Phone v-else class="w-6 h-6" />
                        </div>
                        <div>
                            <div class="font-bold text-gray-900 dark:text-white flex items-center gap-2">
                                {{ session.device }}
                                <span v-if="session.current"
                                    class="text-xs bg-blue-100 text-blue-700 px-2 py-0.5 rounded-full dark:bg-blue-900 dark:text-blue-300">현재
                                    접속 중</span>
                            </div>
                            <div class="text-sm text-gray-500">
                                {{ session.ip }} ({{ session.location }}) · {{ session.time }}
                            </div>
                        </div>
                    </div>

                    <button v-if="!session.current" @click="removeSession(session.id)"
                        class="px-3 py-1.5 text-sm text-red-600 border border-red-200 rounded hover:bg-red-50 dark:border-red-900 dark:hover:bg-red-900/20">
                        접속 종료
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>
