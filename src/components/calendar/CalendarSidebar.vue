<template>
    <div class="h-full bg-white dark:bg-zinc-900 border-r border-gray-200 dark:border-zinc-700 flex flex-col p-4">
        <!-- Add Schedule Button -->
        <button
            class="w-full mb-4 py-2.5 px-4 bg-blue-600 hover:bg-blue-700 text-white font-bold rounded-md flex items-center justify-center gap-2 transition-colors shadow-sm"
            @click="$emit('add-schedule')">
            <PlusIcon class="w-4 h-4" />
            <span>{{ t('calendar.add_schedule') }}</span>
        </button>

        <!-- Search Input -->
        <div class="relative mb-6">
            <SearchIcon class="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
            <input type="text" v-model="searchQuery" @keyup.enter="handleSearch"
                :placeholder="t('calendar.search_placeholder')"
                class="w-full pl-9 pr-3 py-2 bg-gray-50 dark:bg-zinc-800 border border-gray-200 dark:border-zinc-700 rounded-md text-sm focus:outline-none focus:ring-2 focus:ring-blue-500 transition-shadow" />
        </div>

        <!-- Mini Calendar -->
        <div class="mb-6">
            <div class="flex items-center justify-between mb-2 px-1">
                <button class="p-1 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded-full" @click="prevPeriod">
                    <ChevronLeftIcon class="w-4 h-4 text-gray-500" />
                </button>
                <div class="text-sm font-bold text-gray-800 dark:text-gray-200">
                    <CalendarDatePicker :currentDate="currentDate" @update-date="setDate" />
                </div>
                <!-- <span class="text-sm font-bold text-gray-800 dark:text-gray-200">
                    {{ currentYear }}.{{ String(currentMonth + 1).padStart(2, '0') }}
                </span> -->
                <button class="p-1 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded-full" @click="nextPeriod">
                    <ChevronRightIcon class="w-4 h-4 text-gray-500" />
                </button>
            </div>

            <div class="grid grid-cols-7 gap-1 text-center mb-1">
                <div v-for="day in weekDays" :key="day" class="text-[11px] text-gray-400 font-medium">
                    {{ day }}
                </div>
            </div>

            <div class="grid grid-cols-7 gap-1 text-center">
                <button v-for="(date, index) in calendarDays" :key="index"
                    class="w-6 h-6 mx-auto flex items-center justify-center text-[12px] rounded-full transition-colors"
                    :class="[
                        !date.isCurrentMonth ? 'text-gray-300 dark:text-gray-600' : 'text-gray-700 dark:text-gray-300 hover:bg-gray-100 dark:hover:bg-zinc-800',
                        isToday(date.date) ? 'bg-blue-100 text-blue-600 font-bold dark:bg-blue-900/30 dark:text-blue-400' : '',
                        isSelected(date.date) ? 'bg-blue-600 text-white hover:bg-blue-700 dark:bg-blue-600 dark:hover:bg-blue-500' : ''
                    ]" @click="selectDate(date.date)">
                    {{ date.day }}
                </button>
            </div>
        </div>

        <div class="border-t border-gray-200 dark:border-zinc-700 pt-4 flex-1 overflow-y-auto">
            <!-- My Calendars -->
            <div class="mb-4">
                <button
                    class="flex items-center gap-2 text-sm font-bold text-gray-700 dark:text-gray-300 mb-2 w-full text-left"
                    @click="toggleSection('my')">
                    <ChevronDownIcon class="w-4 h-4 transition-transform" :class="{ '-rotate-90': !sections.my }" />
                    {{ t('calendar.my_calendar') }}
                </button>
                <div v-if="sections.my" class="pl-6 space-y-2">
                    <div class="flex items-center gap-2">
                        <input type="checkbox" id="cal_default" checked
                            class="rounded border-gray-300 text-blue-600 focus:ring-blue-500" />
                        <label for="cal_default" class="text-sm text-gray-600 dark:text-gray-400 cursor-pointer">{{
                            t('calendar.default') }}</label>
                    </div>
                    <div class="flex items-center gap-2">
                        <input type="checkbox" id="cal_work"
                            class="rounded border-gray-300 text-green-600 focus:ring-green-500" />
                        <label for="cal_work" class="text-sm text-gray-600 dark:text-gray-400 cursor-pointer">{{
                            t('calendar.work') }}</label>
                    </div>
                </div>
            </div>

            <!-- Shared Calendars -->
            <div class="mb-4">
                <button
                    class="flex items-center gap-2 text-sm font-bold text-gray-700 dark:text-gray-300 mb-2 w-full text-left"
                    @click="toggleSection('shared')">
                    <ChevronDownIcon class="w-4 h-4 transition-transform" :class="{ '-rotate-90': !sections.shared }" />
                    {{ t('calendar.shared_calendar') }}
                </button>
                <div v-if="sections.shared" class="pl-6 space-y-2">
                    <div class="flex items-center gap-2">
                        <input type="checkbox" id="cal_team"
                            class="rounded border-gray-300 text-purple-600 focus:ring-purple-500" />
                        <label for="cal_team" class="text-sm text-gray-600 dark:text-gray-400 cursor-pointer">{{
                            t('calendar.team') }}</label>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { PlusIcon, ChevronLeftIcon, ChevronRightIcon, ChevronDownIcon, Search as SearchIcon } from 'lucide-vue-next';
import { useCalendar } from '../../composables/useCalendar';
import CalendarDatePicker from './CalendarDatePicker.vue';

const emit = defineEmits(['add-schedule', 'select-date']);
const router = useRouter();
const { t } = useI18n();

const {
    currentDate, selectedDate, searchQuery,
    currentYear, currentMonth,
    prevPeriod, nextPeriod, setDate, isSameDate
} = useCalendar();

const handleSearch = () => {
    if (searchQuery.value.trim()) {
        router.push({ path: '/schedule/search', query: { q: searchQuery.value } });
    }
};

const weekDays = computed(() => [
    t('calendar.days.sun'), t('calendar.days.mon'), t('calendar.days.tue'),
    t('calendar.days.wed'), t('calendar.days.thu'), t('calendar.days.fri'),
    t('calendar.days.sat')
]);

const sections = ref({
    my: true,
    shared: true
});

const toggleSection = (key: keyof typeof sections.value) => {
    sections.value[key] = !sections.value[key];
};

const calendarDays = computed(() => {
    const year = currentYear.value;
    const month = currentMonth.value;

    const firstDayOfMonth = new Date(year, month, 1);
    const lastDayOfMonth = new Date(year, month + 1, 0);

    const days = [];

    // Previous month padding
    const firstDayOfWeek = firstDayOfMonth.getDay();
    for (let i = firstDayOfWeek - 1; i >= 0; i--) {
        const d = new Date(year, month, -i);
        days.push({ date: d, day: d.getDate(), isCurrentMonth: false });
    }

    // Current month
    for (let i = 1; i <= lastDayOfMonth.getDate(); i++) {
        const d = new Date(year, month, i);
        days.push({ date: d, day: i, isCurrentMonth: true });
    }

    // Next month padding
    const remainingCells = 42 - days.length; // 6 rows * 7 cols
    for (let i = 1; i <= remainingCells; i++) {
        const d = new Date(year, month + 1, i);
        days.push({ date: d, day: i, isCurrentMonth: false });
    }

    return days;
});

const isToday = (date: Date) => {
    const today = new Date();
    return isSameDate(date, today);
};

const isSelected = (date: Date) => {
    return isSameDate(date, selectedDate.value);
};

const selectDate = (date: Date) => {
    setDate(date);
    // Optionally navigate to specific day view or just update state
    emit('select-date', date);
};
</script>
