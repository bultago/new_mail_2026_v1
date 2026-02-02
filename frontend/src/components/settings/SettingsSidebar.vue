<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import {
    User,
    Lock,
    Home,
    Calendar,
    PenTool,
    ShieldAlert,
    Sliders,
    MessageSquare,
    Mail,
    History,
    Settings,
    Bell,
    Share2,
    ChevronDown,
    ChevronRight,
    Activity,
    Archive
} from 'lucide-vue-next'

const { t } = useI18n()
const emit = defineEmits(['select'])

const activeId = ref('profile')
const collapsedGroups = ref<Record<string, boolean>>({})

const toggleGroup = (groupId: string) => {
    collapsedGroups.value[groupId] = !collapsedGroups.value[groupId]
}

const groups = computed(() => [
    {
        id: 'basic',
        label: t('menu_conf.group.basic') !== 'menu_conf.group.basic' ? t('menu_conf.group.basic') : '기본 설정',
        items: [
            { id: 'profile', icon: User, label: t('menu_conf.profile'), fallback: '기본 정보' },
            { id: 'general', icon: Settings, label: t('menu_conf.general'), fallback: '환경설정' }, // NEW
            { id: 'home', icon: Home, label: t('menu_conf.home'), fallback: '홈 설정' },
            { id: 'sign', icon: PenTool, label: t('menu_conf.sign'), fallback: '서명 관리' }
        ]
    },
    {
        id: 'mail',
        label: t('menu_conf.group.mail') !== 'menu_conf.group.mail' ? t('menu_conf.group.mail') : '메일 관리',
        items: [
            { id: 'spam', icon: ShieldAlert, label: t('menu_conf.spam'), fallback: '스팸 차단' },
            { id: 'filter', icon: Sliders, label: t('menu_conf.filter'), fallback: '자동 분류' },
            { id: 'mailbox', icon: Archive, label: t('menu_conf.mailbox'), fallback: '메일함 관리' }, // NEW
            { id: 'forward', icon: Share2, label: t('menu_conf.forward'), fallback: '자동 전달' }, // NEW
            { id: 'reply', icon: MessageSquare, label: t('menu_conf.reply'), fallback: '자동 응답' },
            { id: 'external', icon: Mail, label: t('menu_conf.external'), fallback: '외부메일' },
            { id: 'lastrcpt', icon: History, label: t('conf.lastrcpt.menu'), fallback: '최근 수신자' }
        ]
    },
    {
        id: 'security',
        label: t('menu_conf.group.security') !== 'menu_conf.group.security' ? t('menu_conf.group.security') : '보안 설정',
        items: [
            { id: 'history', icon: History, label: t('menu_conf.history'), fallback: '로그인 이력' }, // NEW
            { id: '2fa', icon: Lock, label: t('menu_conf.2fa'), fallback: '2단계 인증' }, // NEW
            { id: 'sessions', icon: Activity, label: t('menu_conf.sessions'), fallback: '접속 정보' } // NEW
        ]
    },
    {
        id: 'schedule',
        label: t('menu_conf.group.schedule') !== 'menu_conf.group.schedule' ? t('menu_conf.group.schedule') : '일정 관리',
        items: [
            { id: 'scheduler', icon: Calendar, label: t('menu_conf.scheduler'), fallback: '일정 설정' } // Share Group
        ]
    },
    {
        id: 'notification',
        label: t('menu_conf.group.notification') !== 'menu_conf.group.notification' ? t('menu_conf.group.notification') : '알림 센터',
        items: [
            { id: 'notification_list', icon: Bell, label: '알림 목록', fallback: '알림 목록' }, // NEW
            { id: 'notification', icon: Settings, label: t('menu_conf.notification'), fallback: '알림 설정' } // NEW
        ]
    }
])

const getLabel = (item: any) => {
    if (!item.label || item.label.includes('.')) return item.fallback
    return item.label
}

const handleSelect = (id: string) => {
    activeId.value = id
    emit('select', id)
}
</script>

<template>
    <div class="h-full bg-white dark:bg-zinc-900 border-r border-gray-200 dark:border-zinc-700 flex flex-col">
        <!-- Sidebar Header -->
        <div
            class="h-[50px] flex items-center px-4 border-b border-gray-200 dark:border-zinc-700 bg-white dark:bg-zinc-900">
            <h2 class="font-bold text-gray-800 dark:text-gray-100 text-[15px] flex items-center gap-2">
                <Settings class="w-4 h-4 text-gray-500" />
                <span>{{ t('header.nav.settings') !== 'header.nav.settings' ? t('header.nav.settings') : '환경설정'
                    }}</span>
            </h2>
        </div>

        <!-- Menu List -->
        <div class="flex-1 overflow-y-auto p-2 space-y-4">
            <div v-for="group in groups" :key="group.id" class="space-y-1">
                <!-- Group Header -->
                <button @click="toggleGroup(group.id)"
                    class="w-full flex items-center justify-between px-2 py-1 text-xs font-bold text-gray-500 dark:text-gray-500 uppercase tracking-wider hover:text-gray-700 dark:hover:text-gray-400">
                    <span>{{ group.label }}</span>
                    <component :is="collapsedGroups[group.id] ? ChevronRight : ChevronDown" class="w-3 h-3" />
                </button>

                <!-- Group Items -->
                <div v-show="!collapsedGroups[group.id]" class="space-y-0.5 pl-1">
                    <button v-for="item in group.items" :key="item.id" @click="handleSelect(item.id)"
                        class="w-full flex items-center gap-2.5 px-3 py-2 text-left text-sm font-medium rounded-md transition-all duration-200 group"
                        :class="activeId === item.id
                            ? 'bg-blue-50 text-blue-600 dark:bg-blue-900/20 dark:text-blue-400'
                            : 'text-gray-700 hover:bg-gray-100 dark:text-gray-300 dark:hover:bg-zinc-800'">
                        <component :is="item.icon" class="w-4 h-4 transition-colors"
                            :class="activeId === item.id ? 'text-blue-600 dark:text-blue-400' : 'text-gray-500 group-hover:text-gray-700 dark:text-gray-400'" />
                        <span class="truncate">{{ getLabel(item) }}</span>
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
.font-dotum {
    font-family: 'Dotum', sans-serif;
}
</style>
