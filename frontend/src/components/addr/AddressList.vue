<template>
    <div class="h-full flex flex-col bg-white dark:bg-zinc-900">
        <!-- Current Context Title -->
        <div class="px-5 py-3 bg-white dark:bg-zinc-900 border-b border-gray-200 dark:border-zinc-700">
            <h2 class="text-base font-bold text-gray-800 dark:text-gray-100 flex items-center gap-2">
                <span class="text-gray-500 dark:text-gray-400 font-normal text-sm">
                    {{ selectedGroup.type === 'org' ? t('address_popup.tabs.org') :
                        selectedGroup.type === 'shared' ? t('address_popup.tabs.shared') :
                            t('address_popup.tabs.personal') }}
                    <span class="mx-1">/</span>
                </span>
                {{ selectedGroup.name }}
            </h2>
        </div>

        <!-- Toolbar -->
        <div
            class="px-4 py-2 border-b border-gray-200 dark:border-zinc-700 flex flex-wrap items-center justify-between bg-gray-50 dark:bg-zinc-900/50 gap-y-2">
            <div class="flex items-center gap-1">
                <!-- Group 1: Member Management (Hidden in Org Chart) -->
                <div v-if="selectedGroup.type !== 'org'" class="flex items-center gap-0.5 mr-2">
                    <button
                        class="px-2 py-1.5 text-xs font-medium text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700 flex items-center gap-1"
                        @click="showAddMember = true">
                        <UserPlusIcon class="w-3.5 h-3.5" />
                        {{ t('address_popup.actions.add_member') }}
                    </button>
                    <button
                        class="p-1.5 text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700"
                        :title="t('address_popup.actions.mod_member')">
                        <Edit2Icon class="w-3.5 h-3.5" />
                    </button>
                    <button
                        class="p-1.5 text-red-600 dark:text-red-400 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700"
                        :title="t('address_popup.actions.del_member')">
                        <Trash2Icon class="w-3.5 h-3.5" />
                    </button>
                </div>

                <!-- Group 2: Group Management & Move/Copy (Hidden in Org Chart) -->
                <div v-if="selectedGroup.type !== 'org'"
                    class="flex items-center gap-0.5 mr-2 border-l border-gray-300 dark:border-zinc-600 pl-2">
                    <button
                        class="p-1.5 text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700"
                        :title="t('address_popup.actions.add_group')">
                        <FolderPlusIcon class="w-3.5 h-3.5" />
                    </button>
                    <button
                        class="p-1.5 text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700"
                        :title="t('address_popup.actions.copy_member')">
                        <CopyIcon class="w-3.5 h-3.5" />
                    </button>
                    <button
                        class="p-1.5 text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700"
                        :title="t('address_popup.actions.move_member')">
                        <FolderInputIcon class="w-3.5 h-3.5" />
                    </button>
                </div>

                <!-- Group 3: Mail -->
                <div class="flex items-center gap-0.5 mr-2 pl-2"
                    :class="selectedGroup.type === 'org' ? '' : 'border-l border-gray-300 dark:border-zinc-600'">
                    <button
                        class="px-2 py-1.5 text-xs font-medium text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700 flex items-center gap-1">
                        <MailIcon class="w-3.5 h-3.5" />
                        {{ t('address_popup.actions.send_mail') }}
                    </button>
                    <button
                        class="px-2 py-1.5 text-xs font-medium text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700 flex items-center gap-1">
                        <MailIcon class="w-3.5 h-3.5" />
                        {{ t('address_popup.actions.send_all_mail') }}
                    </button>
                </div>
            </div>

            <!-- Group 4: Search & Tools -->
            <div class="flex items-center gap-2">
                <div class="relative flex items-center gap-1">
                    <div class="relative">
                        <SearchIcon class="absolute left-2.5 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-gray-400" />
                        <input type="text" :placeholder="t('common.search')" v-model="simpleSearchText"
                            @keyup.enter="applySimpleSearch"
                            class="pl-8 pr-4 py-1.5 text-xs bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded-md focus:outline-none focus:ring-1 focus:ring-blue-500 w-40">
                    </div>
                    <button
                        class="px-2 py-1.5 text-xs font-medium text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700 flex items-center gap-1"
                        :class="{ 'bg-blue-50 border-blue-300 text-blue-700': showDetailSearch }"
                        @click="showDetailSearch = !showDetailSearch">
                        {{ t('address_popup.actions.detail_search') }}
                        <ChevronDownIcon class="w-3 h-3 transition-transform"
                            :class="{ 'rotate-180': showDetailSearch }" />
                    </button>
                </div>
                <div class="flex items-center gap-0.5 border-l border-gray-300 dark:border-zinc-600 pl-2">
                    <button v-if="selectedGroup.type !== 'org'"
                        class="p-1.5 text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700"
                        :title="t('address_popup.actions.import')">
                        <UploadIcon class="w-3.5 h-3.5" />
                    </button>
                    <button
                        class="p-1.5 text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700"
                        :title="t('address_popup.actions.export')">
                        <DownloadIcon class="w-3.5 h-3.5" />
                    </button>
                    <button
                        class="p-1.5 text-gray-700 dark:text-gray-200 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-700"
                        :title="t('address_popup.actions.print')">
                        <PrinterIcon class="w-3.5 h-3.5" />
                    </button>
                </div>
            </div>
        </div>

        <!-- Detail Search Panel -->
        <div v-if="showDetailSearch"
            class="px-4 py-3 bg-gray-50 dark:bg-zinc-800/50 border-b border-gray-200 dark:border-zinc-700">
            <div class="grid grid-cols-4 gap-4">
                <div class="flex flex-col gap-1">
                    <label class="text-xs font-medium text-gray-500">{{ t('address_popup.info.label.004') }}</label>
                    <input type="text" v-model="searchParams.name"
                        class="px-2 py-1.5 text-xs border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:border-blue-500">
                </div>
                <div class="flex flex-col gap-1">
                    <label class="text-xs font-medium text-gray-500">{{ t('address_popup.info.label.006') }}</label>
                    <input type="text" v-model="searchParams.email"
                        class="px-2 py-1.5 text-xs border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:border-blue-500">
                </div>
                <div class="flex flex-col gap-1">
                    <label class="text-xs font-medium text-gray-500">{{ t('address_popup.info.label.009') }}</label>
                    <input type="text" v-model="searchParams.mobile"
                        class="px-2 py-1.5 text-xs border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:border-blue-500">
                </div>
                <div class="flex flex-col gap-1">
                    <label class="text-xs font-medium text-gray-500">{{ t('address_popup.info.label.031') }}</label>
                    <input type="text" v-model="searchParams.company"
                        class="px-2 py-1.5 text-xs border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:border-blue-500">
                </div>
            </div>
            <!-- Org Specific Fields -->
            <div v-if="selectedGroup.type === 'org'" class="grid grid-cols-4 gap-4 mt-2">
                <div class="flex flex-col gap-1">
                    <label class="text-xs font-medium text-gray-500">{{ t('address_popup.filters.search_position')
                        }}</label>
                    <input type="text" v-model="searchParams.position"
                        class="px-2 py-1.5 text-xs border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:border-blue-500">
                </div>
                <!-- Duty (Skipping for now as per minimal req, or can add if defined) - 'Level' and 'Duty' keys added to json -->
                <div class="flex flex-col gap-1">
                    <label class="text-xs font-medium text-gray-500">{{ t('address_popup.filters.search_duty')
                        }}</label>
                    <input type="text" v-model="searchParams.duty"
                        class="px-2 py-1.5 text-xs border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:border-blue-500">
                </div>
                <div class="flex flex-col gap-1">
                    <label class="text-xs font-medium text-gray-500">{{ t('address_popup.filters.search_level')
                        }}</label>
                    <input type="text" v-model="searchParams.level"
                        class="px-2 py-1.5 text-xs border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:border-blue-500">
                </div>
                <div class="flex items-end pb-1">
                    <label class="flex items-center gap-2 cursor-pointer">
                        <input type="checkbox" v-model="searchParams.includeConcurrent"
                            class="rounded border-gray-300 dark:border-zinc-600 text-blue-600 focus:ring-blue-500">
                        <span class="text-xs text-gray-700 dark:text-gray-300">{{
                            t('address_popup.filters.include_concurrent') }}</span>
                    </label>
                </div>
            </div>

            <div class="flex justify-end mt-3 gap-2">
                <button
                    class="px-3 py-1.5 text-xs font-medium text-gray-600 bg-white border border-gray-300 rounded hover:bg-gray-50"
                    @click="resetSearch">
                    {{ t('address_popup.actions.reset') }}
                </button>
                <button
                    class="px-3 py-1.5 text-xs font-medium text-white bg-blue-600 border border-blue-600 rounded hover:bg-blue-700"
                    @click="applySearch">
                    {{ t('address_popup.actions.search') }}
                </button>
            </div>
        </div>

        <!-- Active Search Tags -->
        <div v-if="hasActiveSearch"
            class="px-4 py-2 bg-gray-50 dark:bg-zinc-800 border-b border-gray-200 dark:border-zinc-700 flex flex-wrap items-center gap-2">
            <span class="text-xs font-medium text-gray-500 mr-1">{{ t('common.search') }}:</span>

            <template v-for="(value, key) in activeFilters" :key="key">
                <div
                    class="flex items-center gap-1 px-2 py-0.5 bg-blue-50 dark:bg-blue-900/30 text-blue-700 dark:text-blue-300 rounded-full text-xs border border-blue-100 dark:border-blue-800">
                    <span class="font-medium">{{ getFilterLabel(key) }}:</span>
                    <span>{{ value === true ? 'Yes' : value }}</span>
                    <button @click="removeFilter(key)"
                        class="ml-1 p-0.5 hover:text-red-600 dark:hover:text-red-400 rounded-full">
                        <XIcon class="w-3 h-3" />
                    </button>
                </div>
            </template>

            <button @click="resetSearch"
                class="ml-auto text-xs text-gray-500 hover:text-gray-700 dark:hover:text-gray-300 underline">
                {{ t('address_popup.actions.reset') }}
            </button>
        </div>

        <!-- Add Member Modal -->
        <AddressWriteForm v-if="showAddMember" @close="showAddMember = false" @save="handleAddMember" />

        <!-- List Header -->
        <div
            class="grid grid-cols-[auto_2fr_1fr_2fr_1.5fr_1.5fr] gap-4 px-6 py-2 bg-gray-50 dark:bg-zinc-800 border-b border-gray-200 dark:border-zinc-700 text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">
            <div class="flex items-center">
                <input type="checkbox"
                    class="rounded border-gray-300 dark:border-zinc-600 text-blue-600 focus:ring-blue-500">
            </div>
            <div>{{ t('address_popup.info.label.004') }}</div>
            <div>{{ t('address_popup.info.label.033') }}</div>
            <div>{{ t('address_popup.info.label.006') }}</div>
            <div>{{ t('address_popup.info.label.009') }}</div>
            <div>{{ t('address_popup.info.label.031') }}</div>
        </div>

        <!-- List Body -->
        <div class="flex-1 overflow-y-auto">
            <div v-for="contact in filteredContacts" :key="contact.id"
                class="grid grid-cols-[auto_2fr_1fr_2fr_1.5fr_1.5fr] gap-4 px-6 py-3 border-b border-gray-100 dark:border-zinc-800 hover:bg-gray-50 dark:hover:bg-zinc-800/50 items-center text-sm transition-colors group">
                <div class="flex items-center">
                    <input type="checkbox"
                        class="rounded border-gray-300 dark:border-zinc-600 text-blue-600 focus:ring-blue-500">
                </div>
                <!-- Name -->
                <div class="flex items-center gap-3">
                    <div
                        class="w-8 h-8 rounded-full bg-blue-100 dark:bg-blue-900/30 text-blue-600 dark:text-blue-400 flex items-center justify-center text-xs font-bold">
                        {{ contact.name.charAt(0) }}
                    </div>
                    <span class="font-medium text-gray-900 dark:text-gray-100">{{ contact.name }}</span>
                </div>
                <!-- Position -->
                <div class="text-gray-600 dark:text-gray-400">{{ contact.position }}</div>
                <!-- Email -->
                <div class="text-gray-600 dark:text-gray-400 truncate">{{ contact.email }}</div>
                <!-- Mobile -->
                <div class="text-gray-500 dark:text-gray-500">{{ contact.mobile }}</div>
                <!-- Company/Dept -->
                <div class="text-gray-500 dark:text-gray-500 truncate">{{ contact.company }}</div>
            </div>
            <!-- Empty State -->
            <div v-if="filteredContacts.length === 0"
                class="flex flex-col items-center justify-center h-48 text-gray-500">
                <p>{{ t('common.search') }} {{ t('mail.result.fail') }}</p>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import {
    SearchIcon,
    Trash2Icon,
    FolderInputIcon,
    UserPlusIcon,
    Edit2Icon,
    FolderPlusIcon,
    CopyIcon,
    MailIcon,
    UploadIcon,
    DownloadIcon,
    PrinterIcon,
    ChevronDownIcon,
    XIcon
} from 'lucide-vue-next';
import AddressWriteForm from '../../views/addr/AddressWriteForm.vue';

const { t } = useI18n();

const showAddMember = ref(false);

const handleAddMember = (member: any) => {
    console.log('Adding member:', member);
    // Add logic to push to list or call API
    showAddMember.value = false;
};

const showDetailSearch = ref(false);
const simpleSearchText = ref('');
const searchParams = ref({
    name: '',
    email: '',
    mobile: '',
    company: '',
    position: '',
    duty: '',
    level: '',
    includeConcurrent: false
});

const resetSearch = () => {
    simpleSearchText.value = '';
    searchParams.value = {
        name: '', email: '', mobile: '', company: '',
        position: '', duty: '', level: '', includeConcurrent: false
    };
};

const applySearch = () => {
    // Detail Search Trigger
    simpleSearchText.value = ''; // Clear simple search
    console.log('Detail Searching with:', searchParams.value);
};

const applySimpleSearch = () => {
    // Simple Search Trigger (Enter on toolbar input)
    resetSearch(); // Reset detail params
    // We intentionally cleared it above, but we need to keep the simple text. 
    // Actually resetSearch clears everything. Let's adjust helper.
    searchParams.value = {
        name: '', email: '', mobile: '', company: '',
        position: '', duty: '', level: '', includeConcurrent: false
    };
    showDetailSearch.value = false;
    // Note: simpleSearchText is already set by v-model
    console.log('Simple Searching with:', simpleSearchText.value);
};

const hasActiveSearch = computed(() => {
    const hasDetail = Object.values(searchParams.value).some(val => val !== '' && val !== false);
    return hasDetail || !!simpleSearchText.value;
});

const activeFilters = computed(() => {
    const filters: Record<string, any> = {};

    if (simpleSearchText.value) {
        filters['common_search'] = simpleSearchText.value;
    }

    for (const [key, value] of Object.entries(searchParams.value)) {
        if (value !== '' && value !== false) {
            filters[key] = value;
        }
    }
    return filters;
});

const getFilterLabel = (key: string) => {
    const labels: Record<string, string> = {
        common_search: t('common.search'),
        name: t('address_popup.info.label.004'),
        email: t('address_popup.info.label.006'),
        mobile: t('address_popup.info.label.009'),
        company: t('address_popup.info.label.031'),
        position: t('address_popup.filters.search_position'),
        duty: t('address_popup.filters.search_duty'),
        level: t('address_popup.filters.search_level'),
        includeConcurrent: t('address_popup.filters.include_concurrent')
    };
    return labels[key] || key;
};

const removeFilter = (key: string) => {
    if (key === 'common_search') {
        simpleSearchText.value = '';
    } else {
        searchParams.value = {
            ...searchParams.value,
            [key]: typeof searchParams.value[key as keyof typeof searchParams.value] === 'boolean' ? false : ''
        };
    }
};

const props = defineProps<{
    selectedGroup: { id: string | number, type: string, name: string }
}>();

watch(() => props.selectedGroup, () => {
    resetSearch();
    showDetailSearch.value = false; // Also collapse the panel
}, { deep: true });

// Mock Data (Expanded)
const allContacts = [
    { id: 1, name: '김철수', position: '과장', email: 'kim@terracetech.com', mobile: '010-1234-5678', company: '개발팀', dept: 'development', groupId: 'pg1' },
    { id: 2, name: '이영희', position: '대리', email: 'lee@terracetech.com', mobile: '010-9876-5432', company: '디자인팀', dept: 'design', groupId: 'pg1' },
    { id: 3, name: '박민수', position: '사원', email: 'park@terracetech.com', mobile: '010-5555-5555', company: '기획팀', dept: 'planning', groupId: 'pg2' },
    { id: 4, name: '최지우', position: '팀장', email: 'choi@terracetech.com', mobile: '010-1111-2222', company: '영업팀', dept: 'sales', groupId: 'sg1' },
    { id: 5, name: 'John Doe', position: 'Manager', email: 'john@terracetech.com', mobile: '010-7777-8888', company: 'Overseas', dept: 'overseas', groupId: 'sg2' },
    // Org Data
    { id: 6, name: '총장님', position: '총장', email: 'president@sogang.ac.kr', mobile: '010-0000-0001', company: '서강대학교', dept: 'pres', type: 'org' },
    { id: 7, name: '행정처장', position: '처장', email: 'admin_head@sogang.ac.kr', mobile: '010-0000-0002', company: '행정부서', dept: 'admin', type: 'org' },
    { id: 8, name: '정보원장', position: '원장', email: 'it_head@sogang.ac.kr', mobile: '010-0000-0003', company: '정보통신원', dept: 'it', type: 'org' },
    { id: 9, name: '개발팀장', position: '팀장', email: 'dev_lead@sogang.ac.kr', mobile: '010-0000-0004', company: '정보통신원', dept: 'it', type: 'org' },
    { id: 10, name: '총무팀원', position: '사원', email: 'general@sogang.ac.kr', mobile: '010-0000-0005', company: '총무팀', dept: 'gen', type: 'org' }
];

import { computed } from 'vue';

const filteredContacts = computed(() => {
    if (!props.selectedGroup) return [];

    const { id, type } = props.selectedGroup;

    if (type === 'org') {
        if (id === 'info') return allContacts.filter(c => c.type === 'org'); // All Org
        return allContacts.filter(c => c.dept === id);
    } else {
        // Personal or Shared
        return allContacts.filter(c => c.groupId === id);
    }
});

</script>
