<template>
    <div class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm"
        @click.self="$emit('close')">
        <div
            class="bg-white dark:bg-zinc-900 w-[600px] h-[650px] flex flex-col rounded-lg shadow-xl border border-gray-200 dark:border-zinc-700">
            <!-- Header -->
            <div class="px-5 py-4 border-b border-gray-200 dark:border-zinc-700 flex items-center justify-between">
                <h3 class="text-lg font-bold text-gray-900 dark:text-gray-100">{{ t('address_popup.dialog.title.004') ||
                    'Add Member' }}</h3>
                <button @click="$emit('close')" class="text-gray-500 hover:text-gray-700 dark:hover:text-gray-300">
                    <XIcon class="w-5 h-5" />
                </button>
            </div>

            <!-- Tabs -->
            <div class="flex border-b border-gray-200 dark:border-zinc-700 px-5 pt-2">
                <button v-for="tab in tabs" :key="tab.id"
                    class="px-4 py-2 text-sm font-medium border-b-2 transition-colors duration-200"
                    :class="activeTab === tab.id ? 'border-blue-500 text-blue-600 dark:text-blue-400' : 'border-transparent text-gray-500 hover:text-gray-700 dark:hover:text-gray-300'"
                    @click="activeTab = tab.id">
                    {{ tab.label }}
                </button>
            </div>

            <!-- Body -->
            <div class="flex-1 overflow-y-auto p-6">
                <!-- Fragment 1: Basic Info -->
                <div v-show="activeTab === 'basic'" class="space-y-4">
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.003') || 'Last Name' }}</label>
                        <input v-model="form.lastName" type="text"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.001') || 'First Name' }}</label>
                        <input v-model="form.firstName" type="text"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label
                            class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right font-bold text-red-500">*
                            {{ t('address_popup.info.label.004') || 'Name' }}</label>
                        <input v-model="form.name" type="text"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.005') || 'Nickname' }}</label>
                        <input v-model="form.nickname" type="text"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label
                            class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right font-bold text-red-500">*
                            {{ t('address_popup.info.label.006') || 'Email' }}</label>
                        <input v-model="form.email" type="email"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.009') || 'Mobile' }}</label>
                        <input v-model="form.mobile" type="tel"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                </div>

                <!-- Fragment 2: Home Info -->
                <div v-show="activeTab === 'home'" class="space-y-4">
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.017') || 'Home Tel' }}</label>
                        <input v-model="form.homeTel" type="tel"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.011') || 'Zip Code' }}</label>
                        <div class="flex gap-2">
                            <input v-model="form.homeZip1" type="text"
                                class="w-16 px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                            <span class="text-gray-500 self-center">-</span>
                            <input v-model="form.homeZip2" type="text"
                                class="w-16 px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                        </div>
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.015') || 'Address' }}</label>
                        <input v-model="form.homeAddr" type="text"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                </div>

                <!-- Fragment 3: Company Info -->
                <div v-show="activeTab === 'company'" class="space-y-4">
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.031') || 'Company' }}</label>
                        <input v-model="form.company" type="text"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.032') || 'Department' }}</label>
                        <input v-model="form.department" type="text"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.033') || 'Title' }}</label>
                        <input v-model="form.title" type="text"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                    <div class="grid grid-cols-[100px_1fr] gap-4 items-center">
                        <label class="text-sm font-medium text-gray-700 dark:text-gray-300 text-right">{{
                            t('address_popup.info.label.027') || 'Office Tel' }}</label>
                        <input v-model="form.officeTel" type="tel"
                            class="px-3 py-2 border border-gray-300 dark:border-zinc-600 rounded bg-white dark:bg-zinc-800 focus:outline-none focus:ring-1 focus:ring-blue-500">
                    </div>
                </div>
            </div>

            <!-- Footer -->
            <div
                class="px-6 py-4 border-t border-gray-200 dark:border-zinc-700 bg-gray-50 dark:bg-zinc-800/50 flex justify-end gap-3 rounded-b-lg">
                <button @click="$emit('close')"
                    class="px-4 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 bg-white dark:bg-zinc-800 border border-gray-300 dark:border-zinc-600 rounded-md hover:bg-gray-50 dark:hover:bg-zinc-700">
                    {{ t('common.cancel') || 'Cancel' }}
                </button>
                <button @click="save"
                    class="px-4 py-2 text-sm font-medium text-white bg-blue-600 hover:bg-blue-700 rounded-md">
                    {{ t('common.save') || 'Save' }}
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useI18n } from 'vue-i18n';
import { XIcon } from 'lucide-vue-next';

const { t } = useI18n();

const props = defineProps<{
    initialData?: any;
}>();

const emit = defineEmits(['close', 'save']);

const activeTab = ref('basic');
const tabs = computed(() => [
    { id: 'basic', label: t('address_popup.dialog.header.tab1.title') || 'Basic Info' },
    { id: 'home', label: t('address_popup.dialog.header.tab2.title') || 'Home Info' },
    { id: 'company', label: t('address_popup.dialog.header.tab3.title') || 'Company Info' },
]);

// Initialize form
const form = ref({
    firstName: '',
    lastName: '',
    name: '',
    nickname: '',
    email: '',
    mobile: '',
    homeTel: '',
    homeZip1: '',
    homeZip2: '',
    homeAddr: '',
    company: '',
    department: '',
    title: '',
    officeTel: '',
    ...props.initialData
});

const save = () => {
    // Basic validation
    if (!form.value.name || !form.value.email) {
        alert(t('address_popup.info.msg.052') || 'Please enter required fields.');
        return;
    }
    emit('save', form.value);
};
</script>
