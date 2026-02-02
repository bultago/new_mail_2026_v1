<script setup lang="ts">
import { ref, shallowRef } from 'vue'
import SettingsSidebar from '@/components/settings/SettingsSidebar.vue'

// Import all setting components
import SettingsProfile from '@/components/settings/SettingsProfile.vue'
import SettingsSign from '@/components/settings/SettingsSign.vue'
import SettingsGeneral from '@/components/settings/SettingsGeneral.vue'
import SettingsSpam from '@/components/settings/SettingsSpam.vue'
import SettingsFilter from '@/components/settings/SettingsFilter.vue'
import SettingsAutoReply from '@/components/settings/SettingsAutoReply.vue'
import SettingsExtMail from '@/components/settings/SettingsExtMail.vue'
import SettingsForward from '@/components/settings/SettingsForward.vue'
import SettingsMailbox from '@/components/settings/SettingsMailbox.vue'
import SettingsHistory from '@/components/settings/SettingsHistory.vue'
import Settings2FA from '@/components/settings/Settings2FA.vue'
import SettingsSessions from '@/components/settings/SettingsSessions.vue'
import SettingsNotification from '@/components/settings/SettingsNotification.vue'
import SettingsHome from '@/components/settings/SettingsHome.vue'
import SettingsScheduler from '@/components/settings/SettingsScheduler.vue'
import SettingsLastRcpt from '@/components/settings/SettingsLastRcpt.vue'
import SettingsNotificationList from '@/views/notification/NotificationView.vue'

const activeComponent = shallowRef(SettingsProfile)

const handleSelect = (id: string) => {
    switch (id) {
        case 'profile':
            activeComponent.value = SettingsProfile
            break
        case 'general':
            activeComponent.value = SettingsGeneral
            break
        case 'home':
            activeComponent.value = SettingsHome
            break
        case 'sign':
            activeComponent.value = SettingsSign
            break
        case 'security': // Fallback if group header clicked or ambiguous
            activeComponent.value = Settings2FA
            break
        case 'history':
            activeComponent.value = SettingsHistory
            break
        case '2fa':
            activeComponent.value = Settings2FA
            break
        case 'sessions':
            activeComponent.value = SettingsSessions
            break
        case 'spam':
            activeComponent.value = SettingsSpam
            break
        case 'filter':
            activeComponent.value = SettingsFilter
            break
        case 'forward':
            activeComponent.value = SettingsForward
            break
        case 'mailbox':
            activeComponent.value = SettingsMailbox
            break
        case 'reply':
            activeComponent.value = SettingsAutoReply
            break
        case 'external':
            activeComponent.value = SettingsExtMail
            break
        case 'lastrcpt':
            activeComponent.value = SettingsLastRcpt
            break
        case 'scheduler':
            activeComponent.value = SettingsScheduler
            break
        case 'notification':
            activeComponent.value = SettingsNotification
            break
        case 'notification_list':
            activeComponent.value = SettingsNotificationList
            break
        default:
            activeComponent.value = SettingsProfile
    }
}
</script>

<template>
    <div class="flex h-full bg-white dark:bg-zinc-900">
        <!-- Sidebar (Fixed Width) -->
        <div class="w-[240px] flex-shrink-0 h-full border-r border-gray-200 dark:border-zinc-700">
            <SettingsSidebar @select="handleSelect" />
        </div>

        <!-- Main Content Area -->
        <div class="flex-1 h-full overflow-hidden bg-[#FAFBFC] dark:bg-zinc-900">
            <div class="h-full overflow-y-auto p-0">
                <!-- Dynamic Component Rendering -->
                <transition name="fade" mode="out-in">
                    <component :is="activeComponent" />
                </transition>
            </div>
        </div>
    </div>
</template>

<style scoped>
.fade-enter-active,
.fade-leave-active {
    transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
    opacity: 0;
}
</style>
