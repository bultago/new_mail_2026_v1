<template>
    <div v-if="isOpen" class="fixed inset-0 z-50 flex items-center justify-center bg-black/50 backdrop-blur-sm"
        @click.self="close" data-testid="schedule-popup">
        <div
            class="bg-white dark:bg-zinc-900 w-[600px] max-h-[90vh] rounded-lg shadow-xl flex flex-col overflow-hidden border border-gray-200 dark:border-zinc-700">
            <!-- Header -->
            <div
                class="px-6 py-4 border-b border-gray-200 dark:border-zinc-700 flex justify-between items-center bg-gray-50 dark:bg-zinc-800">
                <h3 class="text-lg font-bold text-gray-800 dark:text-gray-100">
                    {{ isEdit ? t('calendar.popup.edit_title') : t('calendar.popup.add_title') }}
                </h3>
                <button
                    class="text-gray-500 hover:text-gray-700 dark:text-gray-400 dark:hover:text-gray-200 transition-colors"
                    @click="close">
                    <XIcon class="w-5 h-5" />
                </button>
            </div>

            <!-- Body -->
            <div class="p-6 overflow-y-auto space-y-4">
                <!-- Title -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">{{
                        t('calendar.popup.title') }}</label>
                    <input type="text" v-model="form.title" :placeholder="t('calendar.popup.title')"
                        class="w-full px-3 py-2 border border-gray-300 dark:border-zinc-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-zinc-800 dark:text-white" />
                </div>

                <!-- Date & Time -->
                <div class="grid grid-cols-2 gap-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">{{
                            t('calendar.popup.start') }}</label>
                        <div class="flex gap-2">
                            <input type="date" v-model="form.startDate"
                                class="flex-1 px-3 py-2 border border-gray-300 dark:border-zinc-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-zinc-800 dark:text-white" />
                            <input v-if="!form.allDay" type="time" v-model="form.startTime"
                                class="w-24 px-2 py-2 border border-gray-300 dark:border-zinc-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-zinc-800 dark:text-white" />
                        </div>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">{{
                            t('calendar.popup.end') }}</label>
                        <div class="flex gap-2">
                            <input type="date" v-model="form.endDate"
                                class="flex-1 px-3 py-2 border border-gray-300 dark:border-zinc-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-zinc-800 dark:text-white" />
                            <input v-if="!form.allDay" type="time" v-model="form.endTime"
                                class="w-24 px-2 py-2 border border-gray-300 dark:border-zinc-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-zinc-800 dark:text-white" />
                        </div>
                    </div>
                </div>

                <!-- Checkboxes -->
                <div class="flex items-center gap-6">
                    <div class="flex items-center gap-2">
                        <input type="checkbox" id="allDay" v-model="form.allDay"
                            class="rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
                        <label for="allDay" class="text-sm text-gray-700 dark:text-gray-300 cursor-pointer">{{
                            t('calendar.popup.all_day') }}</label>
                    </div>
                    <div class="flex items-center gap-2">
                        <input type="checkbox" id="repeat" v-model="form.isRepeat"
                            class="rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
                        <label for="repeat" class="text-sm text-gray-700 dark:text-gray-300 cursor-pointer">{{
                            t('calendar.popup.repeat') }}</label>
                    </div>
                    <div class="flex items-center gap-2">
                        <input type="checkbox" id="shared" v-model="form.isShared"
                            class="rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
                        <label for="shared" class="text-sm text-gray-700 dark:text-gray-300 cursor-pointer">{{
                            t('calendar.popup.share') }}</label>
                    </div>
                </div>

                <!-- Location -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">{{
                        t('calendar.popup.location') }}</label>
                    <input type="text" v-model="form.location" :placeholder="t('calendar.popup.location')"
                        class="w-full px-3 py-2 border border-gray-300 dark:border-zinc-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-zinc-800 dark:text-white" />
                </div>

                <!-- Description -->
                <div>
                    <label class="block text-sm font-medium text-gray-700 dark:text-gray-300 mb-1">{{
                        t('calendar.popup.content') }}</label>
                    <textarea v-model="form.description" rows="4" :placeholder="t('calendar.popup.content')"
                        class="w-full px-3 py-2 border border-gray-300 dark:border-zinc-700 rounded-md focus:ring-2 focus:ring-blue-500 dark:bg-zinc-800 dark:text-white"></textarea>
                </div>
            </div>

            <!-- Footer -->
            <div
                class="px-6 py-4 bg-gray-50 dark:bg-zinc-800 border-t border-gray-200 dark:border-zinc-700 flex justify-end gap-2">
                <button
                    class="px-4 py-2 border border-gray-300 dark:border-zinc-600 rounded text-gray-700 dark:text-gray-200 hover:bg-gray-100 dark:hover:bg-zinc-700 transition-colors"
                    @click="close">
                    {{ t('calendar.popup.cancel') }}
                </button>
                <button class="px-4 py-2 bg-blue-600 text-white rounded hover:bg-blue-700 transition-colors shadow-sm"
                    @click="save">
                    {{ isEdit ? t('calendar.popup.edit') : t('calendar.popup.save') }}
                </button>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue';
import { useI18n } from 'vue-i18n';
import { XIcon } from 'lucide-vue-next';

const props = defineProps<{
    isOpen: boolean;
    initialDate?: Date;
    initialEndDate?: Date;
    editItem?: any;
    // Add t prop if relying on parent, but better to use composable
}>();

const emit = defineEmits(['close', 'save']);
const { t } = useI18n();

const form = ref({
    title: '',
    startDate: '',
    startTime: '09:00',
    endDate: '',
    endTime: '10:00',
    allDay: false,
    isRepeat: false,
    isShared: false,
    location: '',
    description: ''
});

const isEdit = ref(false);

watch(() => props.isOpen, (newVal) => {
    if (newVal) {
        if (props.editItem) {
            isEdit.value = true;
            form.value = { ...props.editItem };
        } else {
            isEdit.value = false;
            const d = props.initialDate || new Date();
            const dateStr = d.toISOString().split('T')[0];

            // Handle end date (default to start date if not provided)
            let endDateStr = dateStr;
            if (props.initialEndDate) {
                endDateStr = props.initialEndDate.toISOString().split('T')[0];
            }

            form.value = {
                title: '',
                startDate: dateStr,
                startTime: '09:00',
                endDate: endDateStr,
                endTime: '10:00',
                allDay: false,
                isRepeat: false,
                isShared: false,
                location: '',
                description: ''
            };
        }
    }
});

const close = () => {
    emit('close');
};

const save = () => {
    if (!form.value.title) {
        alert(t('calendar.popup.title') + ' ' + t('common.required')); // Simplified alert
        return;
    }
    emit('save', form.value);
};
</script>
