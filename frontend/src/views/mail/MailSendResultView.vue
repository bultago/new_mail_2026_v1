<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useI18n } from 'vue-i18n'
import { Button } from '@/components/ui/button'
import { CircleCheck, CircleX, ArrowLeft, Home, RotateCcw } from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()
const { t } = useI18n()

// Query Params
const status = computed(() => route.query.status as string || 'success') // success | fail
const type = computed(() => route.query.type as string || 'general') // general | batch
const reason = computed(() => route.query.reason as string || '')
const failedRcptStr = computed(() => route.query.failed_rcpt as string || '')

// Parse Failed Recipients
const failedRecipients = computed(() => {
    if (!failedRcptStr.value) return []
    return failedRcptStr.value.split(',')
})

// Navigation
const goWrite = () => router.push('/mail/write')
const goInbox = () => router.push('/mail/list/inbox')
const goHome = () => router.push('/')

</script>

<template>
    <div class="flex flex-col h-full bg-[#F0F0F2] dark:bg-zinc-900 font-dotum items-center justify-center p-4">

        <div
            class="bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-700 shadow-sm rounded-lg p-8 w-full max-w-lg flex flex-col items-center gap-6">

            <!-- Success Icon -->
            <div v-if="status === 'success'" class="flex flex-col items-center gap-2">
                <CircleCheck class="w-16 h-16 text-green-500" />
                <h2 class="text-xl font-bold text-gray-800 dark:text-gray-100">
                    {{ type === 'batch' ? t('mail.result.batch_success') : t('mail.result.success') }}
                </h2>
            </div>

            <!-- Fail Icon -->
            <div v-else class="flex flex-col items-center gap-2">
                <CircleX class="w-16 h-16 text-red-500" />
                <h2 class="text-xl font-bold text-gray-800 dark:text-gray-100">{{ t('mail.result.fail') }}</h2>
            </div>

            <!-- Details -->
            <div class="w-full text-[13px] space-y-4">

                <!-- Failure Reason -->
                <div v-if="status === 'fail' && reason"
                    class="bg-red-50 dark:bg-red-900/20 border border-red-100 dark:border-red-800 p-3 rounded">
                    <p class="font-bold text-red-700 dark:text-red-400 mb-1">{{ t('mail.result.fail_reason') }}:</p>
                    <p class="text-red-600 dark:text-red-300">{{ reason }}</p>
                </div>

                <!-- Failed Recipients List -->
                <div v-if="failedRecipients.length > 0"
                    class="bg-gray-50 dark:bg-zinc-700/50 border border-gray-200 dark:border-zinc-600 p-3 rounded max-h-[150px] overflow-auto">
                    <p class="font-bold text-gray-700 dark:text-gray-300 mb-2">{{ t('mail.result.failed_recipients') }}:
                    </p>
                    <ul class="space-y-1">
                        <li v-for="(rcpt, index) in failedRecipients" :key="index"
                            class="flex items-center gap-2 text-gray-600 dark:text-gray-400">
                            <CircleX class="w-3 h-3 text-red-400" /> {{ rcpt }}
                        </li>
                    </ul>
                </div>
            </div>

            <!-- Actions -->
            <div class="flex gap-3 w-full border-t border-gray-200 dark:border-zinc-700 pt-6">
                <Button @click="goWrite" variant="outline"
                    class="flex-1 gap-2 bg-white dark:bg-zinc-700 dark:text-white dark:border-zinc-600 hover:bg-gray-50">
                    <RotateCcw class="w-4 h-4" /> {{ t('mail.result.rewrite') }}
                </Button>
                <Button @click="goInbox" variant="outline"
                    class="flex-1 gap-2 bg-white dark:bg-zinc-700 dark:text-white dark:border-zinc-600 hover:bg-gray-50">
                    <ArrowLeft class="w-4 h-4" /> {{ t('mail.result.go_inbox') }}
                </Button>
                <Button @click="goHome" class="flex-1 gap-2 bg-legacy-blue hover:bg-blue-700 text-white">
                    <Home class="w-4 h-4" /> {{ t('mail.result.go_home') }}
                </Button>
            </div>

        </div>

    </div>
</template>
