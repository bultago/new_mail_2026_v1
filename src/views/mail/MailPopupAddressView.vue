<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useI18n } from 'vue-i18n'
import { useTheme } from '@/composables/useTheme'
import { Button } from '@/components/ui/button'
import {
    X, Users, Folder, ChevronDown, Plus, ArrowRight
} from 'lucide-vue-next'

const { t } = useI18n()
const { initTheme, currentTheme } = useTheme()

onMounted(() => {
    initTheme()
})

const handleClose = () => {
    window.close()
}

// Tabs
const activeTab = ref<'personal' | 'shared' | 'org'>('org')


// Mock Data for address book tree
const personalGroups = ref([
    { id: 'p1', name: '가족', type: 'group' },
    { id: 'p2', name: '친구', type: 'group' }
])

const sharedGroups = ref([
    { id: 's1', name: '프로젝트 A팀', type: 'group' },
    { id: 's2', name: 'TF팀', type: 'group' }
])

// Mock User Data (Expanded)
// Mock User Data (Expanded)
const allUsers = [
    { id: '1', name: '김서강', email: 'kim@sogang.ac.kr', dept: '총무팀', pos: '팀장', rank: '부장' },
    { id: '2', name: '이메일', email: 'lee@sogang.ac.kr', dept: '인사팀', pos: '과장', rank: '과장' },
    { id: '3', name: '박전산', email: 'park@sogang.ac.kr', dept: '정보통신원', pos: '대리', rank: '대리' },
    { id: '4', name: '최보안', email: 'choi@sogang.ac.kr', dept: '정보통신원', pos: '사원', rank: '사원' },
    { id: '5', name: '정행정', email: 'jung@sogang.ac.kr', dept: '행정부서', pos: '주임', rank: '주임' },
    // Mock Personal/Shared contacts
    { id: 'p_u1', name: '아버지', email: 'dad@home.test', dept: '가족', pos: '', rank: '' },
    { id: 's_u1', name: '홍길동', email: 'hong@project.test', dept: '프로젝트 A팀', pos: 'PM', rank: '책임' }
]

const selectedDept = ref('sogang')
const searchQuery = ref('')


// Checkbox selection in center list
const selectedSourceUsers = ref<string[]>([])

// "Select All" computed
const isAllSelected = computed({
    get: () => filteredUsers.value.length > 0 && selectedSourceUsers.value.length === filteredUsers.value.length,
    set: (val) => {
        if (val) {
            selectedSourceUsers.value = filteredUsers.value.map(u => u.id)
        } else {
            selectedSourceUsers.value = []
        }
    }
})

const filteredUsers = computed(() => {
    let users = allUsers

    // Filter by Dept/Group First
    if (activeTab.value === 'org') {
        if (selectedDept.value === 'it') users = allUsers.filter(u => u.dept === '정보통신원')
        // Add more mock filters if needed
    } else if (activeTab.value === 'personal') {
        const group = personalGroups.value.find(g => g.id === selectedDept.value)
        if (group) users = allUsers.filter(u => u.dept === group.name)
        else users = []
    } else if (activeTab.value === 'shared') {
        const group = sharedGroups.value.find(g => g.id === selectedDept.value)
        if (group) users = allUsers.filter(u => u.dept === group.name)
        else users = []
    }

    // Filter by Rank (Legacy UI: Class)
    if (filterClass.value.code && !filterClass.value.include) {
        // Only filter list if "Include" is NOT checked? 
        // ACTUALLY: Legacy UI allows filtering list regardless of "Include" check? 
        // Legacy: "Include" is for ADDING the rule. The list filtering is automatic or separate?
        // Let's assume selecting the dropdown filters the list provided.
        // Wait, legacy orgPopupList.jsp uses "selectbox". It doesn't seem to trigger reload immediately?
        // But for modern UX, let's filter the list if a value is selected.
        // mapping: g1=부장, g2=과장, g3=대리, g4=사원 (Mock)
        const codeMap: any = { g1: t('address_popup.filter.director'), g2: t('address_popup.filter.manager'), g3: t('address_popup.filter.assistant'), g4: t('address_popup.filter.staff') }
        if (codeMap[filterClass.value.code]) {
            users = users.filter(u => u.rank === codeMap[filterClass.value.code])
        }
    }

    // Filter by Position (Legacy UI: Title)
    if (filterTitle.value.code) {
        const codeMap: any = { team: t('address_popup.filter.team_leader'), part: t('address_popup.filter.part_leader') }
        if (codeMap[filterTitle.value.code]) {
            users = users.filter(u => u.pos === codeMap[filterTitle.value.code])
        }
    }

    return users
})

// Target Lists
const toList = ref<any[]>([])
const ccList = ref<any[]>([])
const bccList = ref<any[]>([])

// Filter State (Legacy Parity)
const filterTitle = ref({ include: false, code: '', subAll: false })
const filterClass = ref({ include: false, code: '', subAll: false })
const filterAll = ref({ include: false, subAll: false })

// Actions (Legacy Parity)
const addToLegacy = (type: 'to' | 'cc' | 'bcc') => {
    const targetList = type === 'to' ? toList : type === 'cc' ? ccList : bccList
    let added = false

    // 1. Org Special Filters
    if (activeTab.value === 'org') {
        const orgCodeVal = selectedDept.value // e.g. "sogang", "it"
        // const orgName = "MockOrgName" // In real app, look up name

        // Title Filter (Position)
        if (filterTitle.value.include) {
            const text = `"#${orgCodeVal}.title.${filterTitle.value.code}.${filterTitle.value.subAll}"`
            targetList.value.push({ id: `special_${Date.now()}_1`, name: text, email: 'Group' })
            added = true
        }

        // Class Filter (Rank)
        if (filterClass.value.include) {
            const text = `"#${orgCodeVal}.class.${filterClass.value.code}.${filterClass.value.subAll}"`
            targetList.value.push({ id: `special_${Date.now()}_2`, name: text, email: 'Group' })
            added = true
        }

        // Current All Filter
        if (filterAll.value.include) {
            // If orgName known: "OrgName"<#code.all.all.sub>
            const text = `"#${orgCodeVal}.all.all.${filterAll.value.subAll}"`
            targetList.value.push({ id: `special_${Date.now()}_3`, name: text, email: 'Group' })
            added = true
        }
    }

    // 2. Selected Users (Standard)
    const usersToAdd = allUsers.filter(u => selectedSourceUsers.value.includes(u.id))
    usersToAdd.forEach(u => {
        if (!targetList.value.find(existing => existing.id === u.id)) {
            targetList.value.push(u)
            added = true
        }
    })

    if (added) {
        selectedSourceUsers.value = [] // Clear selection
    } else {
        if (!filterTitle.value.include && !filterClass.value.include && !filterAll.value.include && selectedSourceUsers.value.length === 0) {
            alert(t('common.no_selection'))
        }
    }
}

const addLegacyGroup = (type: 'to' | 'cc' | 'bcc') => {
    // Legacy behavior: Adds group as a special string
    const targetList = type === 'to' ? toList : type === 'cc' ? ccList : bccList

    if (activeTab.value === 'personal' || activeTab.value === 'shared') {
        const group = activeTab.value === 'personal'
            ? personalGroups.value.find(g => g.id === selectedDept.value)
            : sharedGroups.value.find(g => g.id === selectedDept.value)

        if (group) {
            // Personal: $GroupSeq, Shared: &BookSeq.GroupSeq
            // Mocking sequences for simplicity
            const prefix = activeTab.value === 'personal' ? '$' : '&1.'
            const text = `"${prefix}${group.name}"`
            targetList.value.push({ id: `group_${group.id}`, name: text, email: 'Group' })
        } else {
            alert(t('common.no_group_selected'))
        }
    }
}

// applySelection: update to handle these special "Group" type entries logic if needed
// For now, they are just passed as objects with 'name' property which applySelection uses.
// applySelection: update to handle these special "Group" type entries logic if needed
const removeUser = (type: 'to' | 'cc' | 'bcc', userId: string) => {
    const targetList = type === 'to' ? toList : type === 'cc' ? ccList : bccList
    const idx = targetList.value.findIndex(u => u.id === userId)
    if (idx !== -1) targetList.value.splice(idx, 1)
}

const addNewGroup = () => {
    const name = prompt(t('address_popup.prompt.new_group'))
    if (name) {
        if (activeTab.value === 'personal') {
            personalGroups.value.push({ id: `p${Date.now()}`, name, type: 'group' })
        } else if (activeTab.value === 'shared') {
            sharedGroups.value.push({ id: `s${Date.now()}`, name, type: 'group' })
        } else {
            alert(t('address_popup.alert.no_group_org'))
        }
    }
}

const applySelection = () => {
    // Format list as "Name" <email>, ...
    const formatList = (list: any[]) => {
        return list.map(u => {
            // Handle Group special format (legacy parity)
            if (u.email === 'Group') return u.name
            return `"${u.name}" <${u.email}>`
        }).join(', ')
    }

    const t = formatList(toList.value)
    const c = formatList(ccList.value)
    const b = formatList(bccList.value)

    if (window.opener && window.opener.setAddress) {
        window.opener.setAddress(t, c, b)
    } else {
        console.warn('Opener window not found or setAddress not defined')
    }
    window.close()
}
</script>

<template>
    <div class="flex flex-col h-screen bg-[#F0F0F2] dark:bg-zinc-900 font-dotum overflow-hidden">
        <!-- Header -->
        <div class="h-[40px] px-4 flex items-center justify-between bg-legacy-blue dark:bg-zinc-800 select-none shadow-sm flex-shrink-0"
            :class="currentTheme === 'modern' ? 'text-slate-900 bg-white border-b border-gray-200' : 'text-white'">
            <div class="flex items-center gap-2">
                <Users class="h-4 w-4" :class="currentTheme === 'modern' ? 'text-slate-600' : 'text-white'" />
                <h2 class="text-[13px] font-bold">{{ t('address_popup.title') }}</h2>
            </div>
            <button @click="handleClose" class="p-1 rounded transition-colors"
                :class="currentTheme === 'modern' ? 'hover:bg-gray-100 text-slate-500' : 'hover:bg-white/20 text-white'"
                title="닫기">
                <X class="h-4 w-4" />
            </button>
        </div>

        <!-- Tabs -->
        <div class="flex px-2 pt-2 gap-1 border-b border-gray-300 dark:border-zinc-700 bg-gray-100 dark:bg-zinc-900">
            <button @click="activeTab = 'personal'"
                class="px-4 py-1.5 text-[12px] border-t border-l border-r rounded-t-sm"
                :class="activeTab === 'personal' ? 'bg-white border-gray-300 z-10 font-bold dark:bg-zinc-800 dark:border-zinc-600 dark:text-white' : 'bg-gray-200 border-transparent text-gray-500 hover:bg-gray-300 dark:bg-zinc-900 dark:text-gray-500'">{{
                    t('address_popup.tabs.personal') }}</button>
            <button @click="activeTab = 'shared'"
                class="px-4 py-1.5 text-[12px] border-t border-l border-r rounded-t-sm"
                :class="activeTab === 'shared' ? 'bg-white border-gray-300 z-10 font-bold dark:bg-zinc-800 dark:border-zinc-600 dark:text-white' : 'bg-gray-200 border-transparent text-gray-500 hover:bg-gray-300 dark:bg-zinc-900 dark:text-gray-500'">{{
                    t('address_popup.tabs.shared') }}</button>
            <button @click="activeTab = 'org'" class="px-4 py-1.5 text-[12px] border-t border-l border-r rounded-t-sm"
                :class="activeTab === 'org' ? 'bg-white border-gray-300 z-10 font-bold dark:bg-zinc-800 dark:border-zinc-600 dark:text-white' : 'bg-gray-200 border-transparent text-gray-500 hover:bg-gray-300 dark:bg-zinc-900 dark:text-gray-500'">{{
                    t('address_popup.tabs.org') }}</button>
        </div>

        <!-- Content Area -->
        <div class="flex-1 flex p-2 gap-2 overflow-hidden bg-white dark:bg-zinc-900">

            <!-- Left: Tree View -->
            <div class="w-[200px] border border-gray-300 dark:border-zinc-700 flex flex-col">
                <div
                    class="p-2 bg-gray-50 border-b border-gray-200 text-[12px] font-bold text-gray-700 dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-300 flex justify-between items-center">
                    <span v-if="activeTab === 'personal'">{{ t('address_popup.headers.personal_group') }}</span>
                    <span v-if="activeTab === 'shared'">{{ t('address_popup.headers.shared_group') }}</span>
                    <span v-if="activeTab === 'org'">{{ t('address_popup.headers.department') }}</span>

                    <!-- Group Add Button -->
                    <button v-if="activeTab !== 'org'" @click="addNewGroup"
                        :title="t('address_popup.actions.add_group')" class="p-0.5 hover:bg-gray-200 rounded">
                        <Plus class="w-3 h-3 text-gray-500" />
                    </button>
                </div>
                <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900">
                    <div class="p-1">
                        <!-- Org Chart Tree -->
                        <div v-if="activeTab === 'org'" class="cursor-pointer">
                            <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                                @click="selectedDept = 'info'">
                                <ChevronDown class="w-3.5 h-3.5 text-gray-400 mr-1" />
                                <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                                <span class="text-[12px] font-bold">{{ t('address_popup.tree.sogang') }}</span>
                            </div>
                            <div class="pl-5">
                                <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                                    @click="selectedDept = 'pres'">
                                    <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                                    <span class="text-[12px]">{{ t('address_popup.tree.pres') }}</span>
                                </div>
                                <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                                    @click="selectedDept = 'admin'">
                                    <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                                    <span class="text-[12px]">{{ t('address_popup.tree.admin') }}</span>
                                </div>
                                <div class="pl-5 border-l border-dotted border-gray-300 ml-2">
                                    <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                                        @click="selectedDept = 'it'">
                                        <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                                        <span class="text-[12px]">{{ t('address_popup.tree.it') }}</span>
                                    </div>
                                    <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                                        @click="selectedDept = 'gen'">
                                        <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                                        <span class="text-[12px]">{{ t('address_popup.tree.gen') }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <!-- Personal Groups -->
                        <div v-if="activeTab === 'personal'">
                            <div v-for="group in personalGroups" :key="group.id"
                                class="flex items-center py-1 px-2 hover:bg-blue-50 dark:hover:bg-zinc-800 cursor-pointer"
                                :class="selectedDept === group.id ? 'bg-blue-100 dark:bg-zinc-700' : ''"
                                @click="selectedDept = group.id">
                                <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-2" />
                                <span class="text-[12px]">{{ group.name }}</span>
                            </div>
                        </div>

                        <!-- Shared Groups -->
                        <div v-if="activeTab === 'shared'">
                            <div v-for="group in sharedGroups" :key="group.id"
                                class="flex items-center py-1 px-2 hover:bg-blue-50 dark:hover:bg-zinc-800 cursor-pointer"
                                :class="selectedDept === group.id ? 'bg-blue-100 dark:bg-zinc-700' : ''"
                                @click="selectedDept = group.id">
                                <Users class="w-4 h-4 text-blue-500 mr-2" />
                                <span class="text-[12px]">{{ group.name }}</span>
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <!-- Middle: User List & Legacy Filters -->
            <div class="flex-1 border border-gray-300 dark:border-zinc-700 flex flex-col min-w-[300px]">

                <!-- Legacy Filter Table (Org Tab Only) -->
                <div v-if="activeTab === 'org'"
                    class="bg-gray-50 border-b border-gray-200 p-2 dark:bg-zinc-800 dark:border-zinc-700">
                    <table class="w-full text-[11px] text-gray-700 dark:text-gray-300">
                        <tbody>
                            <!-- Row 1: Title (Position) -->
                            <tr>
                                <td class="py-1 flex items-center gap-1">
                                    <input type="checkbox" v-model="filterTitle.include" id="titleInclude" />
                                    <label for="titleInclude">{{ t('address_popup.filters.include_title') }}</label>
                                    <select v-model="filterTitle.code"
                                        class="h-[20px] border border-gray-300 mx-1 dark:bg-zinc-700 dark:border-zinc-600">
                                        <option value="">{{ t('common.select') }}</option>
                                        <option value="team">{{ t('address_popup.filter.team_leader') }}</option>
                                        <option value="part">{{ t('address_popup.filter.part_leader') }}</option>
                                    </select>
                                    <input type="checkbox" v-model="filterTitle.subAll" id="titleSubAll" />
                                    <label for="titleSubAll">{{ t('address_popup.filters.include_sub') }}</label>
                                </td>
                            </tr>
                            <!-- Row 2: Class (Rank) -->
                            <tr>
                                <td class="py-1 flex items-center gap-1">
                                    <input type="checkbox" v-model="filterClass.include" id="classInclude" />
                                    <label for="classInclude">{{ t('address_popup.filters.include_class') }}</label>
                                    <select v-model="filterClass.code"
                                        class="h-[20px] border border-gray-300 mx-1 dark:bg-zinc-700 dark:border-zinc-600">
                                        <option value="">{{ t('common.select') }}</option>
                                        <option value="g1">{{ t('address_popup.filter.director') }}</option>
                                        <option value="g2">{{ t('address_popup.filter.manager') }}</option>
                                        <option value="g3">{{ t('address_popup.filter.assistant') }}</option>
                                        <option value="g4">{{ t('address_popup.filter.staff') }}</option>
                                    </select>
                                    <input type="checkbox" v-model="filterClass.subAll" id="classSubAll" />
                                    <label for="classSubAll">{{ t('address_popup.filters.include_sub') }}</label>
                                </td>
                            </tr>
                            <!-- Row 3: Current All -->
                            <tr>
                                <td class="py-1 flex items-center gap-1">
                                    <input type="checkbox" v-model="filterAll.include" id="currentAll" />
                                    <label for="currentAll">{{ t('address_popup.filters.current_all') }}</label>
                                    <span class="w-4"></span>
                                    <input type="checkbox" v-model="filterAll.subAll" id="currentSubAll" />
                                    <label for="currentSubAll">{{ t('address_popup.filters.include_sub') }}</label>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <!-- Standard Search (All Tabs) -->
                <div class="p-2 bg-white border-b border-gray-200 flex gap-1 dark:bg-zinc-900 dark:border-zinc-700">
                    <select
                        class="h-[24px] text-[11px] border border-gray-300 px-1 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white">
                        <option>{{ t('address_popup.filters.name') }}</option>
                        <option>{{ t('address_popup.filters.id') }}</option>
                    </select>
                    <input type="text" v-model="searchQuery"
                        class="flex-1 h-[24px] border border-gray-300 px-2 text-[11px] outline-none focus:border-blue-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white" />
                    <button
                        class="h-[24px] px-2 bg-white border border-gray-300 text-[11px] hover:bg-gray-50 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white">{{
                            t('address_popup.actions.search') }}</button>
                </div>

                <!-- List -->
                <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900">
                    <table class="w-full border-collapse">
                        <thead class="bg-gray-100 sticky top-0 dark:bg-zinc-800">
                            <tr
                                class="text-[11px] text-gray-600 dark:text-gray-400 border-b border-gray-300 dark:border-zinc-700">
                                <th class="w-[30px] py-1 border-r border-gray-200 dark:border-zinc-700"><input
                                        type="checkbox" v-model="isAllSelected" /></th>
                                <th class="py-1 border-r border-gray-200 dark:border-zinc-700 text-left px-2">{{
                                    t('address_popup.columns.name') }}</th>
                                <th class="py-1 border-r border-gray-200 dark:border-zinc-700 text-left px-2">{{
                                    t('address_popup.columns.pos') }}</th>
                                <th class="py-1 text-left px-2">{{ t('address_popup.columns.email') }}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="user in filteredUsers" :key="user.id"
                                class="hover:bg-blue-50 dark:hover:bg-zinc-800 text-[12px] border-b border-gray-100 dark:border-zinc-800 group">
                                <td class="text-center py-1"><input type="checkbox" :value="user.id"
                                        v-model="selectedSourceUsers" /></td>
                                <td class="px-2 py-1 text-gray-800 dark:text-gray-200">{{ user.name }}</td>
                                <td class="px-2 py-1 text-gray-600 dark:text-gray-400">{{ user.pos }}</td>
                                <td class="px-2 py-1 text-gray-500 dark:text-gray-500">{{ user.email }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>

            <!-- Action Buttons (Middle) -->
            <div class="flex flex-col justify-center gap-2 px-1">
                <!-- Group Add Button (Personal/Shared Only) -->
                <button v-if="activeTab !== 'org'" @click="addLegacyGroup('to')"
                    class="w-[30px] h-[30px] flex items-center justify-center bg-white border border-gray-300 rounded hover:bg-gray-50 dark:bg-zinc-800 dark:border-zinc-600 dark:text-white"
                    :title="t('address_popup.actions.add_group_tooltip')">
                    <Folder class="w-3.5 h-3.5 text-yellow-600" />
                </button>
                <!-- Normal Add -->
                <button @click="addToLegacy('to')"
                    class="w-[30px] h-[30px] flex items-center justify-center bg-white border border-gray-300 rounded hover:bg-gray-50 dark:bg-zinc-800 dark:border-zinc-600 dark:text-white"
                    :title="t('address_popup.actions.to') + ' ' + t('common.add')">
                    <span class="text-[10px] font-bold">To</span>
                    <ArrowRight class="w-3 h-3 ml-0.5" />
                </button>

                <div class="h-2"></div>

                <button v-if="activeTab !== 'org'" @click="addLegacyGroup('cc')"
                    class="w-[30px] h-[30px] flex items-center justify-center bg-white border border-gray-300 rounded hover:bg-gray-50 dark:bg-zinc-800 dark:border-zinc-600 dark:text-white"
                    :title="t('address_popup.actions.add_group_tooltip')">
                    <Folder class="w-3.5 h-3.5 text-yellow-600" />
                </button>
                <button @click="addToLegacy('cc')"
                    class="w-[30px] h-[30px] flex items-center justify-center bg-white border border-gray-300 rounded hover:bg-gray-50 dark:bg-zinc-800 dark:border-zinc-600 dark:text-white"
                    :title="t('address_popup.actions.cc') + ' ' + t('common.add')">
                    <span class="text-[10px] font-bold">Cc</span>
                    <ArrowRight class="w-3 h-3 ml-0.5" />
                </button>

                <div class="h-2"></div>

                <button v-if="activeTab !== 'org'" @click="addLegacyGroup('bcc')"
                    class="w-[30px] h-[30px] flex items-center justify-center bg-white border border-gray-300 rounded hover:bg-gray-50 dark:bg-zinc-800 dark:border-zinc-600 dark:text-white"
                    :title="t('address_popup.actions.add_group_tooltip')">
                    <Folder class="w-3.5 h-3.5 text-yellow-600" />
                </button>
                <button @click="addToLegacy('bcc')"
                    class="w-[30px] h-[30px] flex items-center justify-center bg-white border border-gray-300 rounded hover:bg-gray-50 dark:bg-zinc-800 dark:border-zinc-600 dark:text-white"
                    :title="t('address_popup.actions.bcc') + ' ' + t('common.add')">
                    <span class="text-[10px] font-bold">Bcc</span>
                    <ArrowRight class="w-3 h-3 ml-0.5" />
                </button>
            </div>

            <!-- Right: Selected Targets -->
            <div class="w-[200px] flex flex-col gap-2">

                <!-- To Box -->
                <div class="flex-1 flex flex-col border border-gray-300 dark:border-zinc-700">
                    <div
                        class="bg-gray-100 px-2 py-1 text-[11px] font-bold border-b border-gray-300 text-gray-700 flex justify-between items-center dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-300">
                        <span>{{ t('address_popup.counts.to') }}</span>
                        <span>{{ toList.length }}{{ t('address_popup.counts.people') }}</span>
                    </div>
                    <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900 p-1 space-y-1">
                        <div v-for="u in toList" :key="u.id"
                            class="flex items-center justify-between text-[11px] bg-blue-50 px-2 py-1 rounded border border-blue-100 dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-200">
                            <span class="truncate">{{ u.name }}</span>
                            <X class="w-3 h-3 cursor-pointer text-gray-400 hover:text-red-500"
                                @click="removeUser('to', u.id)" />
                        </div>
                    </div>
                </div>

                <!-- Cc Box -->
                <div class="flex-1 flex flex-col border border-gray-300 dark:border-zinc-700">
                    <div
                        class="bg-gray-100 px-2 py-1 text-[11px] font-bold border-b border-gray-300 text-gray-700 flex justify-between items-center dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-300">
                        <span>{{ t('address_popup.counts.cc') }}</span>
                        <span>{{ ccList.length }}{{ t('address_popup.counts.people') }}</span>
                    </div>
                    <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900 p-1 space-y-1">
                        <div v-for="u in ccList" :key="u.id"
                            class="flex items-center justify-between text-[11px] bg-gray-50 px-2 py-1 rounded border border-gray-100 dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-200">
                            <span class="truncate">{{ u.name }}</span>
                            <X class="w-3 h-3 cursor-pointer text-gray-400 hover:text-red-500"
                                @click="removeUser('cc', u.id)" />
                        </div>
                    </div>
                </div>

                <!-- Bcc Box -->
                <div class="h-[80px] flex flex-col border border-gray-300 dark:border-zinc-700">
                    <div
                        class="bg-gray-100 px-2 py-1 text-[11px] font-bold border-b border-gray-300 text-gray-700 flex justify-between items-center dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-300">
                        <span>{{ t('address_popup.counts.bcc') }}</span>
                        <span>{{ bccList.length }}{{ t('address_popup.counts.people') }}</span>
                    </div>
                    <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900 p-1 space-y-1">
                        <div v-for="u in bccList" :key="u.id"
                            class="flex items-center justify-between text-[11px] bg-gray-50 px-2 py-1 rounded border border-gray-100 dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-200">
                            <span class="truncate">{{ u.name }}</span>
                            <X class="w-3 h-3 cursor-pointer text-gray-400 hover:text-red-500"
                                @click="removeUser('bcc', u.id)" />
                        </div>
                    </div>
                </div>

            </div>

        </div>

        <!-- Footer Buttons -->
        <div
            class="h-[45px] bg-[#F7F7F7] border-t border-gray-300 flex items-center justify-center gap-2 dark:bg-zinc-900 dark:border-zinc-800">
            <Button class="h-[28px] bg-legacy-blue hover:bg-blue-700 text-white text-[12px] w-[80px]"
                @click="applySelection">{{ t('address_popup.actions.apply') }}</Button>
            <Button variant="outline"
                class="h-[28px] border-gray-300 bg-white hover:bg-gray-50 text-[12px] w-[80px] dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-200"
                @click="handleClose">{{ t('address_popup.actions.cancel') }}</Button>
        </div>

    </div>
</template>
