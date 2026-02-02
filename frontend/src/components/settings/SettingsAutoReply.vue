<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { Plus, Trash2, Search, Calendar } from 'lucide-vue-next'

const { t } = useI18n()

// --- State ---
const activeMode = ref('off') // 'on' | 'off'
const startTime = ref('')
const endTime = ref('')
const subject = ref('')
const content = ref('')

// Reply Target
const replyMode = ref('REPLYALL') // 'REPLYALL' | 'REPLYWHITE'
const whitelistInput = ref('')
const whitelist = ref<string[]>([])
const selectedWhitelist = ref<string[]>([])

const isEnabled = computed(() => activeMode.value === 'on')

// --- Address Popup Integration ---
const openAddressSearch = () => {
    window.open('/popup/address?mode=single&callback=setReplyWhitelist', 'AddressPopup', 'width=800,height=600')
}

// Global Callback setup
onMounted(() => {
    (window as any).setReplyWhitelist = (selectedStr: string) => {
        // Format: "Name" <email>, ...
        const emails = selectedStr.split(',').map(s => {
            const match = s.match(/<(.+)>/)
            return match ? match[1] : s.trim().replace(/"/g, '')
        }).filter(e => e)

        // Add to whitelist, avoiding duplicates
        emails.forEach(email => {
            if (!whitelist.value.includes(email)) {
                whitelist.value.push(email)
            }
        })
    }
})

onUnmounted(() => {
    delete (window as any).setReplyWhitelist
})

// --- Actions ---
const addWhitelist = () => {
    if (!whitelistInput.value) return
    if (!whitelist.value.includes(whitelistInput.value)) {
        whitelist.value.push(whitelistInput.value)
    }
    whitelistInput.value = ''
}

const deleteWhitelist = () => {
    if (selectedWhitelist.value.length === 0) return
    whitelist.value = whitelist.value.filter(email => !selectedWhitelist.value.includes(email))
    selectedWhitelist.value = []
}

</script>

<template>
    <div class="h-full flex flex-col bg-white dark:bg-zinc-900 text-sm">
        <!-- Header -->
        <div class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center justify-between">
            <div class="flex items-center gap-2 text-gray-800 dark:text-gray-100 font-bold">
                <span>{{ t('menu_conf.group.mail') }}</span>
                <span class="text-gray-400">|</span>
                <span>{{ t('menu_conf.reply') }}</span>
            </div>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-8">
            <!-- Explanation -->
            <ul class="list-disc list-inside text-gray-500 dark:text-gray-400 space-y-1 pl-2">
                <li>부재 중이거나 메일 확인이 어려울 때 자동으로 답장을 보냅니다.</li>
            </ul>

            <!-- Basic Settings -->
            <div class="space-y-4">
                <div class="border-b border-gray-200 dark:border-zinc-700 pb-2 mb-4">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">자동 응답 설정</h3>
                </div>

                <table class="w-full text-left border-t border-gray-200 dark:border-zinc-700">
                    <tbody>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300">
                                사용 여부
                            </th>
                            <td class="px-4 py-3">
                                <select v-model="activeMode"
                                    class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600">
                                    <option value="on">사용함</option>
                                    <option value="off">사용 안함</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300">
                                설정 기간
                            </th>
                            <td class="px-4 py-3">
                                <div class="flex items-center gap-2">
                                    <input type="date" v-model="startTime" :disabled="!isEnabled"
                                        class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600 disabled:bg-gray-100 disabled:text-gray-400" />
                                    <span>~</span>
                                    <input type="date" v-model="endTime" :disabled="!isEnabled"
                                        class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600 disabled:bg-gray-100 disabled:text-gray-400" />
                                </div>
                            </td>
                        </tr>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300">
                                제목
                            </th>
                            <td class="px-4 py-3">
                                <input type="text" v-model="subject" :disabled="!isEnabled"
                                    class="w-full max-w-lg px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600 disabled:bg-gray-100 disabled:text-gray-400" />
                            </td>
                        </tr>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300 align-top pt-4">
                                내용
                            </th>
                            <td class="px-4 py-3">
                                <textarea v-model="content" :disabled="!isEnabled" rows="10"
                                    class="w-full p-2 border border-gray-300 rounded resize-none dark:bg-zinc-700 dark:border-zinc-600 disabled:bg-gray-100 disabled:text-gray-400"></textarea>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <!-- Reply Target -->
            <div class="space-y-4">
                <div class="border-b border-gray-200 dark:border-zinc-700 pb-2 mb-4">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">응답 대상 설정</h3>
                </div>
                <table class="w-full text-left border-t border-gray-200 dark:border-zinc-700">
                    <tbody>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300">
                                대상 선택
                            </th>
                            <td class="px-4 py-3">
                                <select v-model="replyMode" :disabled="!isEnabled"
                                    class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600 disabled:bg-gray-100 disabled:text-gray-400">
                                    <option value="REPLYALL">모든 사용자에게 응답</option>
                                    <option value="REPLYWHITE">특정 사용자에게만 응답 (Whitelist)</option>
                                </select>
                            </td>
                        </tr>
                        <tr v-if="replyMode === 'REPLYWHITE'" class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300 align-top pt-4">
                                허용 목록
                            </th>
                            <td class="px-4 py-3">
                                <div class="flex gap-4">
                                    <div class="flex flex-col gap-2 w-64">
                                        <input type="text" v-model="whitelistInput" :disabled="!isEnabled"
                                            @keyup.enter="addWhitelist" placeholder="이메일 입력"
                                            class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600 disabled:bg-gray-100 disabled:text-gray-400" />
                                        <select multiple v-model="selectedWhitelist" :disabled="!isEnabled"
                                            class="h-32 border border-gray-300 rounded p-1 text-xs dark:bg-zinc-800 dark:border-zinc-600 disabled:bg-gray-100 disabled:text-gray-400">
                                            <option v-for="email in whitelist" :key="email" :value="email">{{ email }}
                                            </option>
                                        </select>
                                    </div>
                                    <div class="flex flex-col gap-2">
                                        <button @click="addWhitelist" :disabled="!isEnabled"
                                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600 disabled:opacity-50">
                                            <Plus class="w-3 h-3" /> 추가
                                        </button>
                                        <button @click="openAddressSearch" :disabled="!isEnabled"
                                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600 disabled:opacity-50">
                                            <Search class="w-3 h-3" /> 검색
                                        </button>
                                        <button @click="deleteWhitelist" :disabled="!isEnabled"
                                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600 disabled:opacity-50">
                                            <Trash2 class="w-3 h-3" /> 삭제
                                        </button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="flex justify-center gap-2 pt-2">
                    <button class="px-4 py-1.5 bg-blue-600 text-white rounded hover:bg-blue-700 transition">저장</button>
                    <button
                        class="px-4 py-1.5 bg-gray-200 text-gray-700 rounded hover:bg-gray-300 transition dark:bg-zinc-700 dark:text-gray-300">취소</button>
                </div>
            </div>
        </div>
    </div>
</template>
