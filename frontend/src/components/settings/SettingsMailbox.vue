<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Plus, Trash2, Edit2, Archive, Share2, Upload, Download, Tag } from 'lucide-vue-next'

const { t } = useI18n()

const activeTab = ref('basic') // 'basic', 'personal', 'shared', 'tag'

// Mock Data
const basicMailboxes = [
    { name: '받은편지함', count: 120, size: '45.2 MB' },
    { name: '보낸편지함', count: 85, size: '12.5 MB' },
    { name: '임시보관함', count: 3, size: '0.1 MB' },
    { name: '스팸메일함', count: 5, size: '2.3 MB' },
    { name: '휴지통', count: 12, size: '5.6 MB' }
]

const personalMailboxes = ref([
    { id: 1, name: '프로젝트 A', retention: '무제한', count: 15, size: '150 MB', shared: false },
    { id: 2, name: '개인 보관', retention: '30일', count: 2, size: '1 MB', shared: false },
    { id: 3, name: '공유 폴더', retention: '무제한', count: 50, size: '500 MB', shared: true }
])

const tags = ref([
    { id: 1, name: '중요', color: '#ff0000' },
    { id: 2, name: '업무', color: '#0000ff' },
    { id: 3, name: '개인', color: '#00ff00' }
])

// Actions
const emptyMaibox = (name: string) => {
    if (confirm(`${name}을(를) 비우시겠습니까?`)) {
        alert('비우기 완료')
    }
}

const backupMailbox = (name: string) => {
    alert(`${name} 백업을 시작합니다.`)
}

const addPersonalBox = () => {
    const name = prompt('새 메일함 이름:')
    if (name) {
        personalMailboxes.value.push({
            id: Date.now(),
            name,
            retention: '무제한',
            count: 0,
            size: '0 KB',
            shared: false
        })
    }
}

const editPersonalBox = (id: number) => {
    const box = personalMailboxes.value.find(b => b.id === id)
    if (box) {
        const newName = prompt('메일함 이름 수정:', box.name)
        if (newName) box.name = newName
    }
}

const deletePersonalBox = (id: number) => {
    if (confirm('메일함을 삭제하시겠습니까? (메일도 함께 삭제됩니다)')) {
        personalMailboxes.value = personalMailboxes.value.filter(b => b.id !== id)
    }
}

const toggleShare = (id: number) => {
    const box = personalMailboxes.value.find(b => b.id === id)
    if (box) {
        box.shared = !box.shared
        alert(box.shared ? '공유 설정되었습니다.' : '공유 해제되었습니다.')
    }
}

const addTag = () => {
    const name = prompt('태그 이름:')
    if (name) {
        tags.value.push({ id: Date.now(), name, color: '#000000' })
    }
}

</script>

<template>
    <div class="h-full flex flex-col bg-white dark:bg-zinc-900 text-sm">
        <!-- Header -->
        <div class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center justify-between">
            <div class="flex items-center gap-2 text-gray-800 dark:text-gray-100 font-bold">
                <span>{{ t('menu_conf.group.mail') }}</span>
                <span class="text-gray-400">|</span>
                <span>{{ t('menu_conf.mailbox', '메일함 관리') }}</span>
            </div>
        </div>

        <!-- Tabs -->
        <div class="flex border-b border-gray-200 dark:border-zinc-700 px-6">
            <button v-for="tab in ['basic', 'personal', 'shared', 'tag']" :key="tab" @click="activeTab = tab"
                class="px-4 py-3 text-sm font-medium border-b-2 transition-colors duration-200"
                :class="activeTab === tab ? 'border-blue-600 text-blue-600' : 'border-transparent text-gray-500 hover:text-gray-700'">
                <span v-if="tab === 'basic'">기본 메일함</span>
                <span v-if="tab === 'personal'">개인 메일함</span>
                <span v-if="tab === 'shared'">공유 메일함</span>
                <span v-if="tab === 'tag'">태그 관리</span>
            </button>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-8">

            <!-- Basic Mailbox Tab -->
            <div v-if="activeTab === 'basic'" class="space-y-4">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">기본 메일함 목록</h3>
                    <span class="text-xs text-gray-500">기본 메일함은 삭제하거나 변경할 수 없습니다.</span>
                </div>
                <div class="border border-gray-200 dark:border-zinc-700 rounded overflow-hidden">
                    <table class="w-full text-left">
                        <thead class="bg-gray-50 dark:bg-zinc-800 border-b border-gray-200 dark:border-zinc-700">
                            <tr>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">메일함</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">메일수</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">사용량</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300 w-40">관리</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200 dark:divide-zinc-700">
                            <tr v-for="box in basicMailboxes" :key="box.name">
                                <td class="px-4 py-3 font-medium">{{ box.name }}</td>
                                <td class="px-4 py-3">{{ box.count }}</td>
                                <td class="px-4 py-3">{{ box.size }}</td>
                                <td class="px-4 py-3 flex gap-2">
                                    <button @click="emptyMaibox(box.name)"
                                        class="text-gray-500 hover:text-red-600 text-xs border border-gray-300 rounded px-2 py-1">비우기</button>
                                    <button @click="backupMailbox(box.name)"
                                        class="text-gray-500 hover:text-blue-600 text-xs border border-gray-300 rounded px-2 py-1">백업</button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Personal Mailbox Tab -->
            <div v-if="activeTab === 'personal'" class="space-y-4">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">개인 메일함 목록</h3>
                    <button @click="addPersonalBox"
                        class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1">
                        <Plus class="w-3 h-3" /> 메일함 추가
                    </button>
                </div>
                <div class="border border-gray-200 dark:border-zinc-700 rounded overflow-hidden">
                    <table class="w-full text-left">
                        <thead class="bg-gray-50 dark:bg-zinc-800 border-b border-gray-200 dark:border-zinc-700">
                            <tr>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">메일함</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">보관기간</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">메일수</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">용량</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300 w-48">관리</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200 dark:divide-zinc-700">
                            <tr v-if="personalMailboxes.length === 0">
                                <td colspan="5" class="p-8 text-center text-gray-500">생성된 개인 메일함이 없습니다.</td>
                            </tr>
                            <tr v-for="box in personalMailboxes" :key="box.id">
                                <td class="px-4 py-3 font-medium flex items-center gap-2">
                                    {{ box.name }}
                                    <Share2 v-if="box.shared" class="w-3 h-3 text-blue-500" />
                                </td>
                                <td class="px-4 py-3">{{ box.retention }}</td>
                                <td class="px-4 py-3">{{ box.count }}</td>
                                <td class="px-4 py-3">{{ box.size }}</td>
                                <td class="px-4 py-3 flex gap-2">
                                    <button @click="editPersonalBox(box.id)" class="text-gray-500 hover:text-blue-600"
                                        title="수정">
                                        <Edit2 class="w-3.5 h-3.5" />
                                    </button>
                                    <button @click="toggleShare(box.id)" class="text-gray-500 hover:text-green-600"
                                        title="공유">
                                        <Share2 class="w-3.5 h-3.5" />
                                    </button>
                                    <button class="text-gray-500 hover:text-orange-600" title="업로드">
                                        <Upload class="w-3.5 h-3.5" />
                                    </button>
                                    <button class="text-gray-500 hover:text-blue-600" title="백업">
                                        <Download class="w-3.5 h-3.5" />
                                    </button>
                                    <button @click="deletePersonalBox(box.id)" class="text-gray-500 hover:text-red-600"
                                        title="삭제">
                                        <Trash2 class="w-3.5 h-3.5" />
                                    </button>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Shared Mailbox Tab -->
            <div v-if="activeTab === 'shared'" class="space-y-4">
                <div
                    class="bg-yellow-50 dark:bg-yellow-900/20 p-4 rounded text-sm text-yellow-800 dark:text-yellow-200 mb-4">
                    개인 메일함 탭에서 '공유' 아이콘을 클릭하여 공유 설정을 관리할 수 있습니다.
                </div>
                <div class="border border-gray-200 dark:border-zinc-700 rounded overflow-hidden">
                    <table class="w-full text-left">
                        <thead class="bg-gray-50 dark:bg-zinc-800 border-b border-gray-200 dark:border-zinc-700">
                            <tr>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">공유받은 메일함</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">소유자</th>
                                <th class="px-4 py-2 text-gray-700 dark:text-gray-300">권한</th>
                            </tr>
                        </thead>
                        <tbody class="divide-y divide-gray-200 dark:divide-zinc-700">
                            <tr>
                                <td class="px-4 py-3">팀 공유 자료</td>
                                <td class="px-4 py-3">관리자</td>
                                <td class="px-4 py-3">읽기/쓰기</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Tag Management Tab -->
            <div v-if="activeTab === 'tag'" class="space-y-4">
                <div class="flex justify-between items-center mb-4">
                    <h3 class="font-bold text-gray-900 dark:text-gray-100">태그 관리 목록</h3>
                    <button @click="addTag"
                        class="px-3 py-1 bg-white border border-gray-300 rounded hover:bg-gray-50 text-xs flex items-center gap-1">
                        <Plus class="w-3 h-3" /> 태그 추가
                    </button>
                </div>
                <div class="grid grid-cols-2 md:grid-cols-3 gap-4">
                    <div v-for="tag in tags" :key="tag.id"
                        class="flex items-center justify-between p-3 border border-gray-200 rounded hover:shadow-sm">
                        <div class="flex items-center gap-2">
                            <span class="w-3 h-3 rounded-full" :style="{ backgroundColor: tag.color }"></span>
                            <span class="font-medium">{{ tag.name }}</span>
                        </div>
                        <div class="flex gap-2">
                            <button class="text-gray-400 hover:text-blue-600">
                                <Edit2 class="w-3 h-3" />
                            </button>
                            <button class="text-gray-400 hover:text-red-600">
                                <Trash2 class="w-3 h-3" />
                            </button>
                        </div>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
