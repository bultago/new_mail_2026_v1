<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Plus, Trash2, Edit2, X } from 'lucide-vue-next'

const { t } = useI18n()

// --- State ---
const signApply = ref('T') // 'T' | 'F'
const signLocation = ref('outside') // 'outside' (Bottom) | 'inside' (Content)

interface SignData {
    signSeq: string
    signName: string
    defaultSign: 'T' | 'F'
    signMode: 'text' | 'html'
    signText: string
}

const signList = ref<SignData[]>([
    { signSeq: '1', signName: '기본 서명', defaultSign: 'T', signMode: 'text', signText: '감사합니다.\n홍길동 드림' },
    { signSeq: '2', signName: '영문 서명', defaultSign: 'F', signMode: 'html', signText: 'Best Regards,<br><b>Gil-Dong Hong</b>' }
])

// Modal State
const showModal = ref(false)
const modalMode = ref<'write' | 'modify'>('write')
const currentSign = ref<SignData>({
    signSeq: '',
    signName: '',
    defaultSign: 'F',
    signMode: 'text',
    signText: ''
})

// --- Actions ---
const openNewSign = () => {
    modalMode.value = 'write'
    currentSign.value = { signSeq: Date.now().toString(), signName: '', defaultSign: 'F', signMode: 'html', signText: '' }
    showModal.value = true
}

const openEditSign = (sign: SignData) => {
    modalMode.value = 'modify'
    currentSign.value = { ...sign }
    showModal.value = true
}

const saveSign = () => {
    if (!currentSign.value.signName.trim()) {
        alert(t('conf.sign.alert.signName.empty', '서명 이름을 입력해주세요.'))
        return
    }

    if (modalMode.value === 'write') {
        // If default, unset others
        if (currentSign.value.defaultSign === 'T') {
            signList.value.forEach(s => s.defaultSign = 'F')
        }
        signList.value.push({ ...currentSign.value })
    } else {
        const idx = signList.value.findIndex(s => s.signSeq === currentSign.value.signSeq)
        if (idx !== -1) {
            if (currentSign.value.defaultSign === 'T') {
                signList.value.forEach(s => s.defaultSign = 'F')
            }
            signList.value[idx] = { ...currentSign.value }
        }
    }
    showModal.value = false
}

const deleteSigns = () => {
    if (!confirm(t('conf.filter.3', '삭제하시겠습니까?'))) return
    // Mock delete - ideally check selected items
    alert('선택한 서명이 삭제되었습니다.')
}

</script>

<template>
    <div class="h-full flex flex-col bg-white dark:bg-zinc-900 text-sm">
        <!-- Header -->
        <div class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center justify-between">
            <div class="flex items-center gap-2 text-gray-800 dark:text-gray-100 font-bold">
                <span>{{ t('menu_conf.group.basic') }}</span>
                <span class="text-gray-400">|</span>
                <span>{{ t('menu_conf.sign') }}</span>
            </div>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-8">

            <!-- Explanation -->
            <ul class="list-disc list-inside text-gray-500 dark:text-gray-400 space-y-1 pl-2">
                <li>메일 쓰기 시 본문 하단에 삽입될 서명을 관리합니다.</li>
            </ul>

            <!-- Basic Settings -->
            <div class="space-y-4">
                <div class="border-b border-gray-200 dark:border-zinc-700 pb-2 mb-4">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">기본 설정</h3>
                </div>

                <table class="w-full text-left border-t border-gray-200 dark:border-zinc-700">
                    <tbody>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300">
                                서명 사용 여부
                            </th>
                            <td class="px-4 py-3">
                                <select v-model="signApply"
                                    class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600">
                                    <option value="T">사용함</option>
                                    <option value="F">사용안함</option>
                                </select>
                            </td>
                        </tr>
                        <tr class="border-b border-gray-200 dark:border-zinc-700">
                            <th
                                class="bg-gray-50 dark:bg-zinc-800 w-40 px-4 py-3 font-medium text-gray-700 dark:text-gray-300">
                                서명 위치
                            </th>
                            <td class="px-4 py-3">
                                <select v-model="signLocation"
                                    class="px-2 py-1 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600">
                                    <option value="outside">본문 하단 (Bottom)</option>
                                    <option value="inside">본문 내용 포함 (Inside)</option>
                                </select>
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

            <!-- Edit Settings (List) -->
            <div class="space-y-4">
                <div class="border-b border-gray-200 dark:border-zinc-700 pb-2 mb-4 flex justify-between items-end">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">서명 편집</h3>
                    <div class="flex gap-2">
                        <button @click="openNewSign"
                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600">
                            <Plus class="w-3 h-3" /> 신규
                        </button>
                        <button @click="deleteSigns"
                            class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1 dark:bg-zinc-800 dark:border-zinc-600">
                            <Trash2 class="w-3 h-3" /> 삭제
                        </button>
                    </div>
                </div>

                <div class="border border-gray-200 dark:border-zinc-700 rounded overflow-hidden">
                    <table class="w-full text-left">
                        <thead class="bg-gray-50 dark:bg-zinc-800 border-b border-gray-200 dark:border-zinc-700">
                            <tr>
                                <th class="w-10 px-4 py-2"><input type="checkbox"></th>
                                <th class="px-4 py-2 font-medium text-gray-700 dark:text-gray-300">서명 제목</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200 dark:divide-zinc-700 bg-white dark:bg-zinc-900">
                            <tr v-if="signList.length === 0">
                                <td colspan="2" class="p-8 text-center text-gray-500">등록된 서명이 없습니다.</td>
                            </tr>
                            <tr v-for="sign in signList" :key="sign.signSeq"
                                :class="{ 'bg-blue-50/50 dark:bg-blue-900/10': sign.defaultSign === 'T' }">
                                <td class="px-4 py-3"><input type="checkbox"></td>
                                <td class="px-4 py-3">
                                    <a href="#" @click.prevent="openEditSign(sign)"
                                        class="hover:underline text-blue-600 dark:text-blue-400 font-medium">
                                        {{ sign.signName }}
                                    </a>
                                    <span v-if="sign.defaultSign === 'T'" class="text-red-500 text-xs ml-2">(기본
                                        설정)</span>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <!-- Modal -->
        <div v-if="showModal" class="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
            <div class="bg-white dark:bg-zinc-800 w-[800px] max-h-[90vh] flex flex-col rounded-lg shadow-xl">
                <!-- Modal Header -->
                <div class="flex justify-between items-center p-4 border-b border-gray-200 dark:border-zinc-700">
                    <h3 class="font-bold text-lg">서명 {{ modalMode === 'write' ? '등록' : '수정' }}</h3>
                    <button @click="showModal = false">
                        <X class="w-5 h-5" />
                    </button>
                </div>

                <!-- Modal Body -->
                <div class="p-6 space-y-4 overflow-y-auto">
                    <div class="flex items-center gap-4">
                        <label class="w-20 font-medium text-gray-700 dark:text-gray-300">서명 제목</label>
                        <input type="text" v-model="currentSign.signName"
                            class="flex-1 px-3 py-2 border border-gray-300 rounded dark:bg-zinc-700 dark:border-zinc-600">
                    </div>

                    <div class="flex items-center gap-4 pl-24">
                        <label class="flex items-center gap-2 cursor-pointer">
                            <input type="checkbox" v-model="currentSign.defaultSign" true-value="T" false-value="F">
                            <span class="text-sm">기본 서명으로 설정</span>
                        </label>
                    </div>

                    <div class="flex items-center gap-4">
                        <label class="w-20 font-medium text-gray-700 dark:text-gray-300">에디터 모드</label>
                        <div class="flex gap-4">
                            <label class="flex items-center gap-1"><input type="radio" v-model="currentSign.signMode"
                                    value="html"> HTML</label>
                            <label class="flex items-center gap-1"><input type="radio" v-model="currentSign.signMode"
                                    value="text"> Text</label>
                        </div>
                    </div>

                    <!-- Mock SmartEditor -->
                    <div class="border border-gray-300 dark:border-zinc-600 rounded">
                        <div
                            class="bg-gray-100 dark:bg-zinc-700 p-2 border-b border-gray-300 dark:border-zinc-600 flex gap-2">
                            <span class="text-xs text-gray-500">[SmartEditor Toolbar Placeholder]</span>
                            <button class="text-xs bg-white px-2 border rounded">Bold</button>
                            <button class="text-xs bg-white px-2 border rounded">Image</button>
                        </div>
                        <textarea v-model="currentSign.signText" rows="10"
                            class="w-full p-4 resize-none border-none focus:ring-0 dark:bg-zinc-900"></textarea>
                    </div>
                </div>

                <!-- Modal Footer -->
                <div class="p-4 border-t border-gray-200 dark:border-zinc-700 flex justify-end gap-2">
                    <button @click="saveSign"
                        class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700">저장</button>
                    <button @click="showModal = false"
                        class="px-4 py-2 bg-gray-200 text-gray-700 rounded hover:bg-gray-300">닫기</button>
                </div>
            </div>
        </div>
    </div>
</template>
