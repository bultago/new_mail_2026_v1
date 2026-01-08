<template>
    <div class="h-full bg-white dark:bg-zinc-900 border-r border-gray-200 dark:border-zinc-700 flex flex-col">
        <!-- Header removed as per user request (Only Tabs Visible) -->

        <!-- Sidebar Tabs -->
        <div class="flex px-4 border-b border-gray-200 dark:border-zinc-700">
            <button @click="switchTab('addr')" class="flex-1 py-2 text-sm font-medium border-b-2 transition-colors"
                :class="activeTab === 'addr' ? 'border-blue-600 text-blue-600 dark:border-blue-400 dark:text-blue-400' : 'border-transparent text-gray-500 hover:text-gray-700 dark:text-gray-400'">
                {{ t('common.address_book') }}
            </button>
            <button @click="switchTab('org')" class="flex-1 py-2 text-sm font-medium border-b-2 transition-colors"
                :class="activeTab === 'org' ? 'border-blue-600 text-blue-600 dark:border-blue-400 dark:text-blue-400' : 'border-transparent text-gray-500 hover:text-gray-700 dark:text-gray-400'">
                {{ t('address_popup.tabs.org') }}
            </button>
        </div>

        <div class="flex-1 overflow-y-auto p-2">

            <!-- Address Book Tab Content -->
            <div v-if="activeTab === 'addr'">
                <!-- Personal Address Book -->
                <div class="mb-4">
                    <button
                        class="w-full flex items-center gap-2 px-3 py-2 text-left text-sm font-medium text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded-md"
                        @click="toggleSection('personal')">
                        <ChevronRightIcon class="w-4 h-4 transition-transform"
                            :class="{ 'rotate-90': sections.personal }" />
                        <UserIcon class="w-4 h-4 text-gray-500" />
                        <span>{{ t('address_popup.tabs.personal') }}</span>
                    </button>
                    <div v-if="sections.personal" class="ml-6 mt-1 space-y-1">
                        <button v-for="group in mockPersonalGroups" :key="group.id"
                            class="w-full text-left px-3 py-1.5 text-sm text-gray-600 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-md truncate transition-colors"
                            :class="{ 'bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400': selectedGroupId === group.id }"
                            @click="selectGroup(group.id, 'personal')">
                            {{ group.name }}
                        </button>
                    </div>
                </div>

                <!-- Shared Address Book -->
                <div class="mb-4">
                    <button
                        class="w-full flex items-center gap-2 px-3 py-2 text-left text-sm font-medium text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded-md"
                        @click="toggleSection('shared')">
                        <ChevronRightIcon class="w-4 h-4 transition-transform"
                            :class="{ 'rotate-90': sections.shared }" />
                        <UsersIcon class="w-4 h-4 text-gray-500" />
                        <span>{{ t('address_popup.tabs.shared') }}</span>
                    </button>
                    <div v-if="sections.shared" class="ml-6 mt-1 space-y-1">
                        <button v-for="group in mockSharedGroups" :key="group.id"
                            class="w-full text-left px-3 py-1.5 text-sm text-gray-600 dark:text-gray-400 hover:text-blue-600 dark:hover:text-blue-400 hover:bg-blue-50 dark:hover:bg-blue-900/20 rounded-md truncate transition-colors"
                            :class="{ 'bg-blue-50 dark:bg-blue-900/20 text-blue-600 dark:text-blue-400': selectedGroupId === group.id }"
                            @click="selectGroup(group.id, 'shared')">
                            {{ group.name }}
                        </button>
                    </div>
                </div>
            </div>

            <!-- Org Chart Tree -->
            <div v-if="activeTab === 'org'" class="cursor-pointer font-dotum ml-1">
                <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                    @click="selectGroup('info', 'org')">
                    <ChevronDown class="w-3.5 h-3.5 text-gray-400 mr-1" />
                    <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                    <span class="text-[12px] font-bold">서강대학교</span>
                </div>
                <div class="pl-5">
                    <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                        :class="{ 'bg-blue-100 dark:bg-blue-900/20': selectedGroupId === 'pres' }"
                        @click="selectGroup('pres', 'org')">
                        <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                        <span class="text-[12px]">총장실</span>
                    </div>
                    <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                        :class="{ 'bg-blue-100 dark:bg-blue-900/20': selectedGroupId === 'admin' }"
                        @click="selectGroup('admin', 'org')">
                        <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                        <span class="text-[12px]">행정부서</span>
                    </div>
                    <div class="pl-5 border-l border-dotted border-gray-300 ml-2">
                        <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                            :class="{ 'bg-blue-100 dark:bg-blue-900/20': selectedGroupId === 'it' }"
                            @click="selectGroup('it', 'org')">
                            <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                            <span class="text-[12px]">정보통신원</span>
                        </div>
                        <div class="flex items-center py-1 px-1 hover:bg-blue-50 dark:hover:bg-zinc-800"
                            :class="{ 'bg-blue-100 dark:bg-blue-900/20': selectedGroupId === 'gen' }"
                            @click="selectGroup('gen', 'org')">
                            <Folder class="w-4 h-4 text-yellow-500 fill-yellow-500 mr-1.5" />
                            <span class="text-[12px]">총무팀</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import {
    PlusIcon,
    ChevronRightIcon,
    UserIcon,
    UsersIcon,
    BuildingIcon,
    ChevronDown, // Added
    Folder       // Added
} from 'lucide-vue-next';

const { t } = useI18n();

const activeTab = ref<'addr' | 'org'>('addr');

const sections = ref({
    personal: true,
    shared: false,
    org: true
});

const selectedGroupId = ref<string | number>('pg1');

const toggleSection = (section: keyof typeof sections.value) => {
    sections.value[section] = !sections.value[section];
};

const emit = defineEmits(['select-group']);

const selectGroup = (id: string | number, type: 'personal' | 'shared' | 'org' = 'org') => {
    selectedGroupId.value = id;
    let name = '';

    if (type === 'personal') {
        name = mockPersonalGroups.find(g => g.id === id)?.name || '';
    } else if (type === 'shared') {
        name = mockSharedGroups.find(g => g.id === id)?.name || '';
    } else if (type === 'org') {
        // Mock org names lookup (since we removed the heavy mockDepartments array to simplify, we reconstruct basic lookups here or use a helper)
        // For now, simple switch for the static tree items
        const orgNames: Record<string, string> = {
            'info': '서강대학교',
            'pres': '총장실',
            'admin': '행정부서',
            'it': '정보통신원',
            'gen': '총무팀'
        };
        name = orgNames[id as string] || '';
    }

    emit('select-group', { id, type, name });
};

const switchTab = (tab: 'addr' | 'org') => {
    activeTab.value = tab;
    if (tab === 'org') {
        // Default to root org node
        selectGroup('info', 'org');
    } else {
        // Default to first personal group
        selectGroup('pg1', 'personal');
    }
};

// Mock Data (Static)
const mockPersonalGroups = [
    { id: 'pg1', name: '가족' },
    { id: 'pg2', name: '친구' },
    { id: 'pg3', name: '동호회' }
];

const mockSharedGroups = [
    { id: 'sg1', name: '공용 주소록 1' },
    { id: 'sg2', name: '프로젝트 팀' }
];

/* 
// No longer used with hardcoded tree for exact visual match
const mockDepartments = [
    { id: 'd1', name: '총장실' },
    { id: 'd2', name: '행정부서' },
    { id: 'd3', name: '정보통신원' },
    { id: 'd4', name: '총무팀' }
]; 
*/

</script>
