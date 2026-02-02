<script setup lang="ts">
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { Plus, Trash2, Search, CheckSquare } from 'lucide-vue-next'

const { t } = useI18n()

// --- State ---
const forwardMode = ref('none') // 'none' | 'forwarding' | 'forwardingonly'

// Basic Forwarding List
const inputEmail = ref('')
const forwardList = ref<string[]>([])
const selectedForward = ref<string[]>([])

// Conditional Forwarding
const defineType = ref('mail') // 'mail' | 'domain'
const defineValue = ref('')
const defineInputEmail = ref('')
const defineTargetList = ref<string[]>([])
const defineSelectedTarget = ref<string[]>([])

// Rules List
interface ForwardRule {
    id: string
    fromType: 'mail' | 'domain'
    fromValue: string
    targets: string[]
}
const rulesList = ref<ForwardRule[]>([
    { id: '1', fromType: 'domain', fromValue: 'example.com', targets: ['admin@company.com'] }
])

// --- Address Popup Integration ---
const openAddressSearch = () => {
    window.open('/popup/address?mode=single&callback=setForwardAddr', 'AddressPopup', 'width=800,height=600')
}

// Global Callback setup
onMounted(() => {
    (window as any).setForwardAddr = (selectedStr: string) => {
        // Format: "Name" <email>, ...
        // We extract emails.
        const emails = selectedStr.split(',').map(s => {
            const match = s.match(/<(.+)>/)
            return match ? match[1] : s.trim().replace(/"/g, '')
        }).filter(e => e)

        // Add to list, avoiding duplicates
        emails.forEach(email => {
            if (!forwardList.value.includes(email)) {
                forwardList.value.push(email)
            }
        })
    }
})

onUnmounted(() => {
    delete (window as any).setForwardAddr
})

// --- Actions (Basic) ---
const addForward = () => {
    if (!inputEmail.value) return
    if (!forwardList.value.includes(inputEmail.value)) {
        forwardList.value.push(inputEmail.value)
    }
    inputEmail.value = ''
}

const deleteForward = () => {
    if (selectedForward.value.length === 0) return
    forwardList.value = forwardList.value.filter(email => !selectedForward.value.includes(email))
    selectedForward.value = []
}

// --- Actions (Conditional) ---
const addDefineTarget = () => {
    if (!defineInputEmail.value) return
    if (!defineTargetList.value.includes(defineInputEmail.value)) {
        defineTargetList.value.push(defineInputEmail.value)
    }
    defineInputEmail.value = ''
}

const deleteDefineTarget = () => {
    if (defineSelectedTarget.value.length === 0) return
    defineTargetList.value = defineTargetList.value.filter(email => !defineSelectedTarget.value.includes(email))
    defineSelectedTarget.value = []
}

const addRule = () => {
    if (!defineValue.value || defineTargetList.value.length === 0) {
        alert(t('conf.alert.incomplete', '조건과 대상을 모두 입력해주세요.'))
        return
    }
    rulesList.value.push({
        id: Date.now().toString(),
        fromType: defineType.value as 'mail' | 'domain',
        fromValue: defineValue.value,
        targets: [...defineTargetList.value]
    })
    // Reset form
    defineValue.value = ''
    defineTargetList.value = []
}

const deleteRule = (id: string) => {
    if (!confirm(t('common.delete.confirm', '삭제하시겠습니까?'))) return
    rulesList.value = rulesList.value.filter(r => r.id !== id)
}

</script>

<template>
    <div class="h-full flex flex-col bg-white dark:bg-zinc-900 text-sm">
        <!-- Header -->
        <div class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center justify-between">
            <div class="flex items-center gap-2 text-gray-800 dark:text-gray-100 font-bold">
                <span>{{ t('menu_conf.group.mail') }}</span>
                <span class="text-gray-400">|</span>
                <span>{{ t('menu_conf.forward') }}</span>
            </div>
            <span class="text-xs text-gray-500">({{ t('conf.forward.max', { n: 10 }, '최대 10개') }})</span>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-8">
            <!-- Explanation -->
            <ul class="list-disc list-inside text-gray-500 dark:text-gray-400 space-y-1 pl-2">
                <li>수신된 메일을 다른 메일 주소로 자동 전달합니다.</li>
            </ul>

            <!-- Basic Forwarding -->
            <div class="space-y-4">
                <div class="border-b border-gray-200 dark:border-zinc-700 pb-2 mb-4">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">자동 전달 설정</h3>
                </div>

                <table class="w-full text-left border-t border-gray-200 dark:border-zinc-700">
                    <tbody>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300">
                                전달 방식
                            </th>
                            <td class="px-4 py-3">
                                <select v-model="forwardMode"
                                    class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600">
                                    <option value="none">사용 안함</option>
                                    <option value="forwarding">전달 후 내 편지함에 보관</option>
                                    <option value="forwardingonly">전달 후 내 편지함에서 삭제</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300 align-top pt-4">
                                전달 주소 목록
                            </th>
                            <td class="px-4 py-3">
                                <div class="flex gap-4">
                                    <div class="flex flex-col gap-2 w-64">
                                        <input type="text" v-model="inputEmail" @keyup.enter="addForward"
                                            placeholder="이메일 입력"
                                            class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600" />
                                        <select multiple v-model="selectedForward"
                                            class="h-32 border border-gray-300 rounded p-1 text-xs dark:bg-zinc-800 dark:border-zinc-600">
                                            <option v-for="email in forwardList" :key="email" :value="email">{{ email }}
                                            </option>
                                        </select>
                                    </div>
                                    <div class="flex flex-col gap-2">
                                        <button @click="addForward"
                                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600">
                                            <Plus class="w-3 h-3" /> 추가
                                        </button>
                                        <button @click="openAddressSearch"
                                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600">
                                            <Search class="w-3 h-3" /> 검색
                                        </button>
                                        <button @click="deleteForward"
                                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600">
                                            <Trash2 class="w-3 h-3" /> 삭제
                                        </button>
                                    </div>
                                </div>
                            </td>
                        </tr>
                        <!-- Conditional Forwarding Input -->
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300 align-top pt-4">
                                조건부 전달
                            </th>
                            <td class="px-4 py-3 space-y-4">
                                <!-- Condition -->
                                <div class="flex items-center gap-2">
                                    <select v-model="defineType"
                                        class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600">
                                        <option value="mail">메일주소</option>
                                        <option value="domain">도메인</option>
                                    </select>
                                    <input type="text" v-model="defineValue"
                                        class="px-2 py-1 border border-gray-300 rounded w-40 dark:bg-zinc-700 dark:border-zinc-600" />
                                    <span>에서 온 메일</span>
                                </div>

                                <!-- Target List -->
                                <div class="flex gap-4">
                                    <div class="flex flex-col gap-2 w-64">
                                        <input type="text" v-model="defineInputEmail" @keyup.enter="addDefineTarget"
                                            placeholder="전달받을 주소 입력"
                                            class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600" />
                                        <select multiple v-model="defineSelectedTarget"
                                            class="h-32 border border-gray-300 rounded p-1 text-xs dark:bg-zinc-800 dark:border-zinc-600">
                                            <option v-for="email in defineTargetList" :key="email" :value="email">{{
                                                email }}</option>
                                        </select>
                                    </div>
                                    <div class="flex flex-col gap-2 pt-8">
                                        <button @click="addDefineTarget"
                                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600">
                                            <Plus class="w-3 h-3" /> 추가
                                        </button>
                                        <button @click="deleteDefineTarget"
                                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600">
                                            <Trash2 class="w-3 h-3" /> 삭제
                                        </button>
                                        <button @click="addRule"
                                            class="px-3 py-1 bg-blue-600 text-white rounded hover:bg-blue-700 text-xs flex items-center gap-1 mt-auto">
                                            <CheckSquare class="w-3 h-3" /> 규칙 등록
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

            <!-- Define Forward List Table -->
            <div class="space-y-4">
                <div class="border-b border-gray-200 dark:border-zinc-700 pb-2 mb-4">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">조건부 전달 목록</h3>
                </div>

                <div class="border border-gray-200 dark:border-zinc-700 rounded overflow-hidden">
                    <table class="w-full text-left">
                        <thead class="bg-gray-50 dark:bg-zinc-800 border-b border-gray-200 dark:border-zinc-700">
                            <tr>
                                <th class="w-10 px-4 py-2"><input type="checkbox"></th>
                                <th class="px-4 py-2 font-medium text-gray-700 dark:text-gray-300">조건 (보낸 사람)</th>
                                <th class="px-4 py-2 font-medium text-gray-700 dark:text-gray-300">전달 주소</th>
                                <th class="px-4 py-2 font-medium text-gray-700 dark:text-gray-300 w-24">관리</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200 dark:divide-zinc-700 bg-white dark:bg-zinc-900">
                            <tr v-if="rulesList.length === 0">
                                <td colspan="4" class="p-8 text-center text-gray-500">등록된 규칙이 없습니다.</td>
                            </tr>
                            <tr v-for="rule in rulesList" :key="rule.id">
                                <td class="px-4 py-3"><input type="checkbox"></td>
                                <td class="px-4 py-3">
                                    <span v-if="rule.fromType === 'mail'">[메일]</span>
                                    <span v-else>[도메인]</span>
                                    {{ rule.fromValue }}
                                </td>
                                <td class="px-4 py-3">
                                    <div v-for="target in rule.targets" :key="target">{{ target }}</div>
                                </td>
                                <td class="px-4 py-3">
                                    <button @click="deleteRule(rule.id)" class="text-gray-400 hover:text-red-500">
                                        <Trash2 class="w-4 h-4" />
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</template>
