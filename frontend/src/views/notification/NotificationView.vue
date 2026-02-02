<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Bell, Mail, ShieldAlert, X } from 'lucide-vue-next'
import { useRouter } from 'vue-router'

const { t } = useI18n()
const router = useRouter()

const notifications = ref([
    { id: 1, type: 'mail', title: '새로운 메일이 도착했습니다.', content: '홍길동님이 보낸 "주간 업무 보고" 메일이 도착했습니다.', time: '방금 전', read: false },
    { id: 2, type: 'security', title: '새로운 기기에서 로그인되었습니다.', content: 'Chrome on Windows (Seoul, KR)에서 로그인되었습니다.', time: '1시간 전', read: true },
    { id: 3, type: 'mail', title: '예약 메일 발송 완료', content: '예약하신 "회의록 송부" 메일이 성공적으로 발송되었습니다.', time: '어제', read: true }
])

const markAsRead = (id: number) => {
    const noti = notifications.value.find(n => n.id === id)
    if (noti) noti.read = true
}

const deleteNoti = (id: number) => {
    notifications.value = notifications.value.filter(n => n.id !== id)
}

const getIcon = (type: string) => {
    switch (type) {
        case 'mail': return Mail
        case 'security': return ShieldAlert
        default: return Bell
    }
}

const getColor = (type: string) => {
    switch (type) {
        case 'mail': return 'text-blue-500 bg-blue-100 dark:bg-blue-900/30'
        case 'security': return 'text-red-500 bg-red-100 dark:bg-red-900/30'
        default: return 'text-gray-500 bg-gray-100 dark:bg-gray-700'
    }
}
</script>

<template>
    <div class="h-full flex flex-col bg-white dark:bg-zinc-900">
        <!-- Header -->
        <div class="h-[60px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center justify-between">
            <h1 class="text-xl font-bold text-gray-900 dark:text-white flex items-center gap-2">
                <Bell class="w-6 h-6" />
                {{ t('menu_conf.group.notification') }}
            </h1>
            <button class="text-sm text-gray-500 hover:text-blue-600 dark:text-gray-400">
                모두 읽음 처리
            </button>
        </div>

        <!-- List -->
        <div class="flex-1 overflow-y-auto p-0">
            <div v-if="notifications.length === 0"
                class="flex flex-col items-center justify-center h-full text-gray-500">
                <Bell class="w-12 h-12 mb-4 opacity-20" />
                <p>알림이 없습니다.</p>
            </div>

            <div v-else class="divide-y divide-gray-100 dark:divide-zinc-800">
                <div v-for="noti in notifications" :key="noti.id"
                    class="p-4 hover:bg-gray-50 dark:hover:bg-zinc-800/50 transition relative group"
                    :class="{ 'bg-blue-50/50 dark:bg-blue-900/10': !noti.read }">

                    <div class="flex gap-4">
                        <div class="flex-shrink-0 w-10 h-10 rounded-full flex items-center justify-center"
                            :class="getColor(noti.type)">
                            <component :is="getIcon(noti.type)" class="w-5 h-5" />
                        </div>
                        <div class="flex-1">
                            <div class="flex justify-between items-start">
                                <h3 class="font-medium text-gray-900 dark:text-white mb-1"
                                    :class="{ 'font-bold': !noti.read }">
                                    {{ noti.title }}
                                </h3>
                                <span class="text-xs text-gray-400 whitespace-nowrap ml-2">{{ noti.time }}</span>
                            </div>
                            <p class="text-sm text-gray-600 dark:text-gray-300 line-clamp-2">{{ noti.content }}</p>
                        </div>
                    </div>

                    <!-- Actions -->
                    <div class="absolute top-2 right-2 hidden group-hover:flex gap-1">
                        <button @click.stop="deleteNoti(noti.id)"
                            class="p-1 text-gray-400 hover:text-red-500 rounded-full hover:bg-white dark:hover:bg-zinc-700">
                            <X class="w-4 h-4" />
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
