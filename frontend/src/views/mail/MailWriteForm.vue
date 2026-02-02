<script setup lang="ts">
import { ref } from 'vue'
import { Button } from '@/components/ui/button'
import { useI18n } from 'vue-i18n'
import {
    Send, Eye, FileText, Upload, Plus, Minus, Loader2
} from 'lucide-vue-next'
import { useRouter } from 'vue-router'
import LoadingOverlay from '@/components/common/LoadingOverlay.vue'

const { t } = useI18n()

// Emits for parent to handle if needed (e.g. close)
defineEmits(['close'])

// Mock Data
const to = ref('')
const cc = ref('')
const bcc = ref('')
const showBcc = ref(false)
const subject = ref('')
const content = ref('')

const toMe = ref(false)

const toggleBcc = () => {
    showBcc.value = !showBcc.value
}

// Watch "To Me" checkbox
import { watch, onMounted, onUnmounted, computed } from 'vue'
watch(toMe, (newVal) => {
    // Legacy format: "Name" <email> - Using a distinct format for "To Me" to be easily identifiable/removable
    const myEmail = '"나" <me@sogang.ac.kr>'

    // Split by comma, trim whitespace
    let currentAddresses = to.value
        ? to.value.split(',').map(addr => addr.trim()).filter(addr => addr.length > 0)
        : []

    if (newVal) {
        // Add if not present
        if (!currentAddresses.includes(myEmail)) {
            currentAddresses.push(myEmail)
        }
    } else {
        // Remove if present
        currentAddresses = currentAddresses.filter(addr => addr !== myEmail)
    }

    to.value = currentAddresses.join(', ')
})

const openAddressBook = () => {
    window.open('/popup/address', '_blank', 'width=800,height=600,scrollbars=yes')
}

// Advanced Options Logic
const isReservation = ref(false)
const reservationDate = ref('')
const reservationHour = ref('09')
const reservationMin = ref('00')
const isSecure = ref(false)
const securePassword = ref('')
const secureHint = ref('')

const toggleReservation = () => {
    // Legacy behavior: If both checked, alert and uncheck
    if (isReservation.value && isSecure.value) {
        alert('예약발송과 보안메일은 동시에 설정할 수 없습니다.')
        isReservation.value = false
    }
}

const toggleSecure = () => {
    if (isReservation.value && isSecure.value) {
        alert('예약발송과 보안메일은 동시에 설정할 수 없습니다.')
        isSecure.value = false
    }
}

// Logic for Auto-save
const autoSaveInterval = ref('30s')
let autoSaveTimer: any = null

const startAutoSave = () => {
    if (autoSaveTimer) clearInterval(autoSaveTimer)
    if (autoSaveInterval.value === 'off') return

    const ms = autoSaveInterval.value === '30s' ? 30000 : autoSaveInterval.value === '1m' ? 60000 : 180000
}
// Signature Logic
const selectedSignature = ref('default')
watch(selectedSignature, (val) => {
    if (val === 'default') {
        if (!content.value.includes('Regards')) content.value += `\n\n--\nRegards,\nYour Name`
    } else if (val === 'company') {
        if (!content.value.includes('Sogang')) content.value += `\n\n--\nSogang University\nIT Team`
    }
})

// Auto-complete Mock
const showAutoComplete = ref(false)
const activeField = ref<'to' | 'cc' | 'bcc' | null>(null)

const autoCompleteList = computed(() => {
    let targetValue = ''
    if (activeField.value === 'to') targetValue = to.value
    else if (activeField.value === 'cc') targetValue = cc.value
    else if (activeField.value === 'bcc') targetValue = bcc.value
    else return []

    if (!targetValue || targetValue.endsWith(' ')) return [] // Simple check, usually check last token

    // Check if we are typing a new address (after comma)
    // Check if we are typing a new address (after comma)
    const tokens = targetValue.split(',')
    if (tokens.length === 0) return []

    const lastToken = tokens[tokens.length - 1]!.trim().toLowerCase()

    if (lastToken.length < 1) return []

    return [
        { name: '김서강', email: 'kim@sogang.ac.kr' },
        { name: '박전산', email: 'park@sogang.ac.kr' },
        { name: '이메일', email: 'lee@sogang.ac.kr' },
        { name: '최보안', email: 'choi@sogang.ac.kr' },
        { name: '정행정', email: 'jung@sogang.ac.kr' }
    ].filter(u => u.name.includes(lastToken) || u.email.includes(lastToken))
})

const selectAutoUser = (user: { name: string, email: string }) => {
    // Current Target
    let targetRef = to
    if (activeField.value === 'cc') targetRef = cc
    else if (activeField.value === 'bcc') targetRef = bcc

    // Legacy format: "Name" <email>
    const newItem = `"${user.name}" <${user.email}>`

    // If input already has commas, append
    if (targetRef.value.includes(',')) {
        const parts = targetRef.value.split(',')
        parts.pop() // Remove partial query
        parts.push(` ${newItem}`)
        targetRef.value = parts.join(',')
    } else {
        targetRef.value = newItem
    }
    showAutoComplete.value = false
    activeField.value = null
}

// Recent Recipients Logic
const recentRecipients = ref([
    '"김팀장" <manager@company.com>',
    '"이대리" <lee@company.com>'
])
const selectedRecent = ref('')

watch(selectedRecent, (val) => {
    if (val !== '') {
        if (to.value) {
            to.value += `, ${val}`
        } else {
            to.value = val
        }
        // Reset dropdown
        setTimeout(() => selectedRecent.value = '', 100)
    }
})

// Window global function for Popup to call
const handleBlur = (field: string) => {
    setTimeout(() => {
        if (activeField.value === field) {
            showAutoComplete.value = false
        }
    }, 200)
}

// Loading State
const isLoading = ref(false)
const router = useRouter() // Import router

const handleSend = () => {
    if (!to.value) {
        alert(t('common.no_recipient')) // Need key or hardcode for now
        return
    }

    isLoading.value = true

    // Mock Sending Delay
    setTimeout(() => {
        isLoading.value = false

        // Mock Logic:
        // 1. If subject contains "fail", simulate failure
        // 2. If recipients count > 5, simulate "Batch" success
        // 3. Otherwise General Success

        const recipients = to.value.split(',').length
        const isFail = subject.value.includes('fail')

        let query: any = {}

        if (isFail) {
            query = {
                status: 'fail',
                type: 'general',
                reason: 'SMTP Connection Timeout (Mock code: 421)',
                failed_rcpt: to.value // All failed
            }
        } else if (recipients > 5) {
            query = {
                status: 'success',
                type: 'batch', // Batch registered
                failed_rcpt: '' // Assume none failed immediately in batch
            }
        } else {
            query = {
                status: 'success',
                type: 'general',
                failed_rcpt: ''
            }
        }

        router.push({ name: 'mail-result', query })

    }, 2000) // 2 sec delay
}

onMounted(() => {
    startAutoSave()
        ; (window as any).setAddress = (newTo: string, newCc: string, newBcc: string) => {
            if (newTo) to.value = to.value ? `${to.value}, ${newTo}` : newTo
            if (newCc) cc.value = cc.value ? `${cc.value}, ${newCc}` : newCc
            if (newBcc) {
                bcc.value = bcc.value ? `${bcc.value}, ${newBcc}` : newBcc
                showBcc.value = true
            }
        }
})

onUnmounted(() => {
    if (autoSaveTimer) clearInterval(autoSaveTimer)
    delete (window as any).setAddress
})
</script>

<template>
    <div class="flex flex-col h-full bg-[#F0F0F2] dark:bg-zinc-900 font-dotum relative">
        <!-- Loading Overlay (Reusable Component) -->
        <LoadingOverlay v-if="isLoading" text="Sending Mail...">
            <template #skeleton>
                <div
                    class="bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-700 shadow-sm rounded-lg p-8 w-full max-w-lg flex flex-col items-center gap-6">
                    <div class="w-16 h-16 bg-gray-200 dark:bg-zinc-700 rounded-full animate-pulse"></div> <!-- Icon -->
                    <div class="h-6 w-48 bg-gray-200 dark:bg-zinc-700 rounded animate-pulse"></div> <!-- Title -->

                    <div class="w-full space-y-4 animate-pulse">
                        <div class="w-full h-20 bg-gray-100 dark:bg-zinc-700/50 rounded"></div>
                        <div class="w-full h-32 bg-gray-50 dark:bg-zinc-700/30 rounded"></div>
                    </div>

                    <div
                        class="flex gap-3 w-full border-t border-gray-100 dark:border-zinc-700 pt-6 mt-2 animate-pulse">
                        <div class="flex-1 h-9 bg-gray-200 dark:bg-zinc-700 rounded"></div>
                        <div class="flex-1 h-9 bg-gray-200 dark:bg-zinc-700 rounded"></div>
                        <div class="flex-1 h-9 bg-gray-200 dark:bg-zinc-700 rounded"></div>
                    </div>
                </div>
            </template>
        </LoadingOverlay>

        <!-- Legacy Toolbar -->
        <div class="px-4 py-3 bg-white border-b border-gray-200 dark:bg-zinc-900 dark:border-zinc-700">
            <h2 class="text-lg font-bold text-gray-800 dark:text-gray-100">{{ t('mail.write.title') }}</h2>
        </div>

        <!-- Legacy Toolbar -->
        <div
            class="px-4 py-2 border-b border-legacy-border bg-[#F7F7F7] dark:bg-zinc-900 flex flex-col gap-2 shadow-sm">
            <div class="flex flex-wrap items-center gap-1">
                <Button variant="outline" size="sm" @click="handleSend"
                    class="h-[28px] bg-white text-[#444] text-[11px] font-bold border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                    <Send class="h-3.5 w-3.5 mr-1.5 text-blue-600 dark:text-blue-400" /> {{ t('mail.write.send') }}
                </Button>
                <Button variant="outline" size="sm"
                    class="h-[28px] bg-white text-[#444] text-[11px] border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                    <Eye class="h-3.5 w-3.5 mr-1.5 text-gray-600 dark:text-gray-400" /> {{ t('common.preview') }}
                </Button>
                <Button variant="outline" size="sm"
                    class="h-[28px] bg-white text-[#444] text-[11px] border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                    <FileText class="h-3.5 w-3.5 mr-1.5 text-orange-600 dark:text-orange-400" /> {{ t('mail.write.save')
                    }}
                </Button>

                <div class="h-4 w-[1px] bg-[#CCC] mx-2 hidden sm:block"></div>

                <!-- Options Column -->
                <div class="flex flex-wrap items-center gap-4">
                    <!-- Group 1 -->
                    <div
                        class="flex flex-wrap items-center gap-3 text-[12px] text-gray-700 dark:text-gray-300 select-none">
                        <label
                            class="flex items-center gap-1 cursor-pointer hover:text-black dark:hover:text-white whitespace-nowrap">
                            <input type="checkbox" class="accent-blue-600" checked /> {{ t('mail.write.options.receipt')
                            }}
                        </label>
                        <label
                            class="flex items-center gap-1 cursor-pointer hover:text-black dark:hover:text-white whitespace-nowrap">
                            <input type="checkbox" class="accent-blue-600" /> {{ t('mail.write.options.individual') }}
                        </label>
                        <label
                            class="flex items-center gap-1 cursor-pointer hover:text-black dark:hover:text-white whitespace-nowrap">
                            <input type="checkbox" class="accent-blue-600" checked /> {{
                                t('mail.write.options.save_sent') }}
                        </label>
                    </div>

                    <!-- Divider -->
                    <div class="w-[1px] h-[14px] bg-gray-300 dark:bg-zinc-600 hidden sm:block"></div>

                    <!-- Group 2 -->
                    <div
                        class="flex flex-wrap items-center gap-3 text-[12px] text-gray-700 dark:text-gray-300 select-none">
                        <label
                            class="flex items-center gap-1 cursor-pointer hover:text-black dark:hover:text-white whitespace-nowrap">
                            <input type="checkbox" class="accent-blue-600" v-model="isReservation"
                                @change="toggleReservation" /> {{ t('mail.write.options.reservation') }}
                        </label>
                        <label
                            class="flex items-center gap-1 cursor-pointer hover:text-black dark:hover:text-white whitespace-nowrap">
                            <input type="checkbox" class="accent-blue-600" v-model="isSecure" @change="toggleSecure" />
                            {{ t('mail.write.options.secure') }}
                        </label>
                        <label
                            class="flex items-center gap-1 cursor-pointer hover:text-black dark:hover:text-white whitespace-nowrap">
                            <input type="checkbox" class="accent-blue-600" /> {{ t('mail.write.options.read_receipt') }}
                        </label>
                    </div>
                </div>

                <div class="ml-auto flex items-center gap-2">
                    <select v-model="autoSaveInterval"
                        class="h-[24px] text-[11px] border border-gray-300 rounded px-1 outline-none dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-300">
                        <option value="off">{{ t('mail.write.autosave.off') }}</option>
                        <option value="30s">{{ t('mail.write.autosave.30s') }}</option>
                        <option value="1m">{{ t('mail.write.autosave.1m') }}</option>
                        <option value="3m">{{ t('mail.write.autosave.3m') }}</option>
                    </select>
                </div>
            </div>

            <!-- Detailed Settings Area (Dynamic) -->
            <div v-if="isReservation || isSecure"
                class="border-t border-dotted border-gray-300 pt-2 flex items-center gap-4 text-[12px] bg-[#f0f0f2] p-2 dark:bg-zinc-800 dark:border-zinc-700">
                <!-- Reservation Details -->
                <div v-if="isReservation" class="flex items-center gap-2">
                    <span class="font-bold text-[#444] dark:text-gray-300">{{ t('mail.write.reservation_dt') }}:</span>
                    <input type="date" v-model="reservationDate"
                        class="h-[22px] border border-gray-300 px-1 dark:bg-zinc-700 dark:border-zinc-600" />
                    <select v-model="reservationHour"
                        class="h-[22px] border border-gray-300 dark:bg-zinc-700 dark:border-zinc-600">
                        <option v-for="h in 24" :key="h" :value="String(h - 1).padStart(2, '0')">{{
                            String(h - 1).padStart(2, '0') }}{{ t('mail.write.time_hour') }}</option>
                    </select>
                    <select v-model="reservationMin"
                        class="h-[22px] border border-gray-300 dark:bg-zinc-700 dark:border-zinc-600">
                        <option v-for="m in [0, 10, 20, 30, 40, 50]" :key="m" :value="String(m).padStart(2, '0')">{{
                            String(m).padStart(2, '0') }}{{ t('mail.write.time_min') }}</option>
                    </select>
                    <span class="text-xs text-red-500 ml-1">{{ t('mail.write.reservation_warn') }}</span>
                </div>

                <!-- Secure Details -->
                <div v-if="isSecure" class="flex items-center gap-2">
                    <span class="font-bold text-[#444] dark:text-gray-300">{{ t('mail.write.secure_pw') }}:</span>
                    <input type="password" v-model="securePassword"
                        class="h-[22px] w-[80px] border border-gray-300 px-1 dark:bg-zinc-700 dark:border-zinc-600" />
                    <span class="font-bold text-[#444] dark:text-gray-300 ml-2">{{ t('mail.write.secure_hint')
                    }}:</span>
                    <input type="text" v-model="secureHint"
                        class="h-[22px] w-[100px] border border-gray-300 px-1 dark:bg-zinc-700 dark:border-zinc-600" />
                </div>
            </div>
        </div>

        <!-- Write Form -->
        <div class="flex-1 overflow-auto p-4 bg-white dark:bg-zinc-900 flex flex-col relative">

            <div
                class="flex-1 flex flex-col w-full border border-legacy-border bg-white dark:bg-zinc-800 shadow-sm p-4">

                <!-- Fields Grid - Auto Height Content -->
                <div class="grid grid-cols-[90px_1fr] gap-y-2 items-center mb-4 flex-shrink-0">

                    <!-- To -->
                    <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400">
                        {{ t('mail.write.to') }}
                    </div>
                    <div class="flex gap-2 items-center">
                        <div class="flex-1 relative">
                            <!-- To Me Checkbox (Inside Input) -->
                            <label
                                class="absolute right-2 top-1/2 -translate-y-1/2 flex items-center gap-0.5 cursor-pointer select-none font-normal text-[11px] text-blue-600 z-10">
                                <input type="checkbox" class="accent-blue-600" v-model="toMe" />
                                {{ t('mail.write.to_me') }}
                            </label>

                            <input type="text" v-model="to" @focus="activeField = 'to'; showAutoComplete = true"
                                @blur="handleBlur('to')"
                                class="w-full h-[28px] border border-legacy-border px-2 pr-[70px] text-[12px] focus:border-blue-500 outline-none dark:bg-zinc-700 dark:border-zinc-600 dark:text-white" />

                            <!-- Dropdown -->
                            <div v-if="activeField === 'to' && autoCompleteList.length > 0 && showAutoComplete"
                                class="absolute z-50 bg-white border border-gray-300 shadow-md w-full left-0 top-[30px] dark:bg-zinc-800 dark:border-zinc-600">
                                <div v-for="user in autoCompleteList" :key="user.email"
                                    class="px-2 py-1 hover:bg-blue-50 dark:hover:bg-zinc-700 cursor-pointer text-[12px] flex flex-col"
                                    @click="selectAutoUser(user)">
                                    <span class="font-bold text-gray-800 dark:text-gray-200">{{ user.name }}</span>
                                    <span class="text-gray-500 text-[11px]">{{ user.email }}</span>
                                </div>
                            </div>
                        </div>

                        <select v-model="selectedRecent"
                            class="h-[28px] w-[120px] border border-legacy-border bg-gray-50 text-[11px] px-1 outline-none dark:bg-zinc-700 dark:border-zinc-600 dark:text-gray-200 flex-shrink-0">
                            <option value="">{{ t('mail.write.recent.default') }}</option>
                            <option v-for="addr in recentRecipients" :key="addr" :value="addr">{{ addr }}</option>
                        </select>
                        <Button variant="outline" size="sm"
                            class="h-[28px] px-2 text-[11px] bg-gray-50 hover:bg-gray-100 dark:bg-zinc-700 dark:text-gray-200 dark:border-zinc-600"
                            @click="openAddressBook">{{ t('common.address_book') }}</Button>
                    </div>

                    <!-- Cc -->
                    <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400">{{
                        t('mail.write.cc') }}</div>
                    <div class="flex gap-2 items-center">
                        <div class="flex-1 relative">
                            <input type="text" v-model="cc" @focus="activeField = 'cc'; showAutoComplete = true"
                                @blur="handleBlur('cc')"
                                class="w-full h-[28px] border border-legacy-border px-2 text-[12px] focus:border-blue-500 outline-none dark:bg-zinc-700 dark:border-zinc-600 dark:text-white" />

                            <!-- Dropdown -->
                            <div v-if="activeField === 'cc' && autoCompleteList.length > 0 && showAutoComplete"
                                class="absolute z-50 bg-white border border-gray-300 shadow-md w-full left-0 top-[30px] dark:bg-zinc-800 dark:border-zinc-600">
                                <div v-for="user in autoCompleteList" :key="user.email"
                                    class="px-2 py-1 hover:bg-blue-50 dark:hover:bg-zinc-700 cursor-pointer text-[12px] flex flex-col"
                                    @click="selectAutoUser(user)">
                                    <span class="font-bold text-gray-800 dark:text-gray-200">{{ user.name }}</span>
                                    <span class="text-gray-500 text-[11px]">{{ user.email }}</span>
                                </div>
                            </div>
                        </div>
                        <button type="button" @click="toggleBcc"
                            class="w-[20px] h-[20px] bg-gray-100 border border-gray-300 flex items-center justify-center hover:bg-gray-200 dark:bg-zinc-700 dark:border-zinc-600 text-gray-600 dark:text-gray-300"
                            :title="showBcc ? '숨은참조 숨기기' : '숨은참조 보이기'">
                            <Minus v-if="showBcc" class="h-3 w-3" />
                            <Plus v-else class="h-3 w-3" />
                        </button>
                    </div>

                    <!-- Bcc (Conditional) -->
                    <template v-if="showBcc">
                        <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400">{{
                            t('mail.write.bcc') }}</div>
                        <div class="relative">
                            <input type="text" v-model="bcc" @focus="activeField = 'bcc'; showAutoComplete = true"
                                @blur="handleBlur('bcc')"
                                class="w-full h-[28px] border border-legacy-border px-2 text-[12px] focus:border-blue-500 outline-none dark:bg-zinc-700 dark:border-zinc-600 dark:text-white" />

                            <!-- Dropdown -->
                            <div v-if="activeField === 'bcc' && autoCompleteList.length > 0 && showAutoComplete"
                                class="absolute z-50 bg-white border border-gray-300 shadow-md w-full left-0 top-[30px] dark:bg-zinc-800 dark:border-zinc-600">
                                <div v-for="user in autoCompleteList" :key="user.email"
                                    class="px-2 py-1 hover:bg-blue-50 dark:hover:bg-zinc-700 cursor-pointer text-[12px] flex flex-col"
                                    @click="selectAutoUser(user)">
                                    <span class="font-bold text-gray-800 dark:text-gray-200">{{ user.name }}</span>
                                    <span class="text-gray-500 text-[11px]">{{ user.email }}</span>
                                </div>
                            </div>
                        </div>
                    </template>

                    <!-- Subject -->
                    <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400">{{
                        t('mail.write.subject') }}</div>
                    <div class="flex items-center gap-2">
                        <input type="text" v-model="subject"
                            class="flex-1 h-[28px] border border-legacy-border px-2 text-[12px] focus:border-blue-500 outline-none dark:bg-zinc-700 dark:border-zinc-600 dark:text-white"
                            :placeholder="t('mail.write.subject_placeholder')" />
                    </div>

                    <!-- File Upload -->
                    <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400 self-start pt-1.5">
                        {{ t('mail.write.attach') }}</div>
                    <div
                        class="border border-legacy-border bg-[#F9F9F9] dark:bg-zinc-700/50 p-2 min-h-[60px] dark:border-zinc-600">
                        <div class="flex items-center gap-2 mb-2">
                            <Button variant="outline" size="sm"
                                class="h-[24px] text-[11px] bg-white hover:bg-gray-50 gap-1 dark:bg-zinc-700 dark:text-gray-200 dark:border-zinc-600">
                                <Upload class="h-3 w-3" /> {{ t('mail.write.file_find') }}
                            </Button>
                            <Button variant="outline" size="sm"
                                class="h-[24px] text-[11px] bg-white hover:bg-gray-50 gap-1 dark:bg-zinc-700 dark:text-gray-200 dark:border-zinc-600">
                                {{ t('mail.write.file_big') }}
                            </Button>
                            <span class="text-[11px] text-gray-400 ml-auto">{{ t('mail.write.file_limit') }}</span>
                        </div>
                        <!-- Empty State -->
                        <div
                            class="text-[11px] text-gray-400 text-center py-2 border-2 border-dashed border-gray-200 rounded dark:border-zinc-600">
                            {{ t('mail.write.file_drop') }}
                        </div>
                    </div>

                </div>

                <!-- Editor Mock - Flex Grow to fill remaining space -->
                <div class="flex-1 flex flex-col min-h-[200px]">
                    <div class="grid grid-cols-[90px_1fr] h-full">
                        <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400 pt-1.5">{{
                            t('mail.write.content') }}
                        </div>
                        <div class="border border-legacy-border flex flex-col dark:border-zinc-600 h-full">
                            <!-- Toolbar Mock -->
                            <div
                                class="h-[32px] bg-gray-100 border-b border-legacy-border flex items-center px-2 gap-2 dark:bg-zinc-700 dark:border-zinc-600 flex-shrink-0 justify-between">
                                <div class="flex items-center gap-1">
                                    <select
                                        class="h-[22px] text-[11px] border border-gray-300 rounded px-1 dark:bg-zinc-600 dark:border-zinc-500 dark:text-gray-200">
                                        <option>HTML</option>
                                        <option>TEXT</option>
                                    </select>
                                    <div class="w-[1px] h-[16px] bg-gray-300 mx-1 dark:bg-zinc-500"></div>
                                    <div class="w-4 h-4 bg-gray-300 rounded-sm dark:bg-zinc-500"></div>
                                    <div class="w-4 h-4 bg-gray-300 rounded-sm dark:bg-zinc-500"></div>
                                    <div class="w-1 h-full bg-gray-300 mx-1 dark:bg-zinc-500"></div>
                                    <div
                                        class="w-20 h-5 bg-white border border-gray-300 rounded-sm dark:bg-zinc-600 dark:border-zinc-500">
                                    </div>
                                </div>

                                <div class="flex items-center gap-2">
                                    <label class="flex items-center gap-1 text-[11px] cursor-pointer">
                                        <input type="checkbox" class="accent-red-600" /> {{ t('mail.write.urgent') }}
                                    </label>
                                    <div class="w-[1px] h-[16px] bg-gray-300 mx-1 dark:bg-zinc-500"></div>

                                    <label class="flex items-center gap-1 text-[11px] cursor-pointer">
                                        <input type="checkbox" class="accent-blue-600" /> {{ t('mail.write.sign_attach')
                                        }}
                                    </label>
                                    <select v-model="selectedSignature"
                                        class="h-[22px] text-[11px] border border-gray-300 rounded px-1 dark:bg-zinc-600 dark:border-zinc-500 dark:text-gray-200">
                                        <option value="default">{{ t('mail.write.signature.default') }}</option>
                                        <option value="company">{{ t('mail.write.signature.company') }}</option>
                                    </select>
                                    <div class="w-[1px] h-[16px] bg-gray-300 mx-1 dark:bg-zinc-500"></div>
                                    <select
                                        class="h-[22px] text-[11px] border border-gray-300 rounded px-1 dark:bg-zinc-600 dark:border-zinc-500 dark:text-gray-200">
                                        <option value="utf8">{{ t('mail.write.encoding.utf8') }}</option>
                                        <option value="euckr">{{ t('mail.write.encoding.euckr') }}</option>
                                    </select>
                                </div>
                            </div>
                            <textarea v-model="content"
                                class="flex-1 resize-none p-4 outline-none text-[13px] leading-relaxed dark:bg-zinc-800 dark:text-white h-full"
                                :placeholder="t('mail.write.content_placeholder')"></textarea>
                        </div>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<style scoped>
/* Custom Scrollbar for Tree */
.overflow-y-auto::-webkit-scrollbar {
    width: 4px;
}

.overflow-y-auto::-webkit-scrollbar-track {
    background: transparent;
}

.overflow-y-auto::-webkit-scrollbar-thumb {
    background-color: #CBD5E1;
    border-radius: 2px;
}

.dark .overflow-y-auto::-webkit-scrollbar-thumb {
    background-color: #475569;
}

/* Linear Progress Animation */
@keyframes progress {
    0% {
        width: 0%;
        margin-left: 0%;
    }

    50% {
        width: 70%;
        margin-left: 0%;
    }

    100% {
        width: 100%;
        margin-left: 100%;
    }
}

.animate-progress {
    animation: progress 1.5s infinite ease-in-out;
}
</style>
