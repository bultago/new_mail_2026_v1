<template>
    <div class="h-full flex flex-col">
        <!-- Toolbar -->
        <div
            class="px-6 py-4 bg-white dark:bg-zinc-900 border-b border-gray-200 dark:border-zinc-700 flex justify-between items-center">
            <div class="flex items-center gap-4">
                <h2 data-testid="calendar-main-header"
                    class="text-xl font-bold text-gray-800 dark:text-gray-100 flex items-center gap-2">
                    <button class="p-1 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded transition-colors"
                        @click="prevPeriod">
                        <ChevronLeftIcon class="w-5 h-5" />
                    </button>

                    <CalendarDatePicker :currentDate="currentDate" @update-date="setDate" />

                    <button class="p-1 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded transition-colors"
                        @click="nextPeriod">
                        <ChevronRightIcon class="w-5 h-5" />
                    </button>
                </h2>
                <button
                    class="px-3 py-1.5 text-sm font-medium border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-800"
                    @click="goToToday">
                    {{ t('calendar.view.today') }}
                </button>
            </div>

            <div class="flex items-center gap-2 bg-gray-100 dark:bg-zinc-800 p-1 rounded-md">
                <button v-for="mode in viewModes" :key="mode.value"
                    class="px-3 py-1 text-sm font-medium rounded transition-colors"
                    :class="currentViewMode === mode.value ? 'bg-white dark:bg-zinc-700 shadow-sm text-blue-600 dark:text-blue-400' : 'text-gray-500 hover:text-gray-700 dark:hover:text-gray-300'"
                    @click="changeViewMode(mode.value)">
                    {{ mode.label }}
                </button>
            </div>
        </div>

        <!-- Month Grid -->
        <div class="flex-1 flex flex-col bg-white dark:bg-zinc-900">
            <!-- Header Row -->
            <div class="grid grid-cols-7 border-b border-gray-200 dark:border-zinc-700">
                <div v-for="(day, index) in weekDays" :key="day"
                    class="py-2 text-center text-sm font-bold border-r border-gray-100 dark:border-zinc-800 last:border-r-0"
                    :class="index === 0 ? 'text-red-500' : index === 6 ? 'text-blue-500' : 'text-gray-500 dark:text-gray-400'">
                    {{ day }}
                </div>
            </div>

            <!-- Calendar Cells / Weeks -->
            <div class="flex-1 overflow-y-auto select-none" @mouseleave="endDrag">
                <div v-for="(week, weekIdx) in calendarWeeks" :key="weekIdx"
                    class="grid grid-cols-7 border-b border-gray-200 dark:border-zinc-700 relative min-h-[100px]"
                    @dragover.prevent @drop="onWeekDrop($event, week)">

                    <!-- Day Cells (Background & Interaction) -->
                    <div v-for="(date, index) in week" :key="index"
                        class="relative border-r border-gray-100 dark:border-zinc-800 p-1 transition-colors cursor-pointer group hover:bg-gray-50 dark:hover:bg-zinc-800/50"
                        :class="[
                            !date.isCurrentMonth ? 'bg-gray-50/50 dark:bg-zinc-900/50' : '',
                            isToday(date.date) ? 'bg-blue-50/30 dark:bg-blue-900/10' : '',
                            isInDragRange(date.date) ? 'bg-blue-100/50 dark:bg-blue-900/20' : ''
                        ]" @mousedown="startDrag(date.date)" @mouseenter="updateDrag(date.date)" @mouseup="endDrag"
                        @dblclick="handleDoubleClick(date.date)" @click="selectDate(date.date)">

                        <!-- Date Number -->
                        <div class="flex items-center justify-between mb-1 pointer-events-none">
                            <span class="text-sm font-medium w-6 h-6 flex items-center justify-center rounded-full"
                                :class="[
                                    isToday(date.date) ? 'bg-blue-600 text-white' :
                                        date.date.getDay() === 0 ? 'text-red-500' :
                                            date.date.getDay() === 6 ? 'text-blue-500' :
                                                !date.isCurrentMonth ? 'text-gray-400' : 'text-gray-700 dark:text-gray-200'
                                ]">
                                {{ date.day }}
                            </span>
                            <span v-if="getHoliday(date.date)" class="text-xs text-red-500 font-medium truncate px-1">
                                {{ getHoliday(date.date) }}
                            </span>
                        </div>
                    </div>

                    <!-- Schedules Overlay -->
                    <template v-for="item in getWeekSchedules(week)" :key="item.data.id">
                        <div class="text-xs px-1.5 py-0.5 rounded truncate transition-opacity hover:opacity-80 cursor-move z-10 mx-1"
                            :class="[
                                item.data.type === 'personal' ? 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300' :
                                    item.data.type === 'work' ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-300' :
                                        'bg-purple-100 text-purple-700 dark:bg-purple-900/30 dark:text-purple-300'
                            ]" :style="item.style" draggable="true"
                            @dragstart.stop="onScheduleDragStart($event, item.data)" @mousedown.stop
                            @click.stop="$emit('edit-schedule', item.data)">
                            {{ item.data.title }}
                        </div>
                    </template>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { ChevronLeftIcon, ChevronRightIcon } from 'lucide-vue-next';
import { useCalendar } from '../../composables/useCalendar';
import CalendarDatePicker from './CalendarDatePicker.vue';

const emit = defineEmits(['select-date', 'edit-schedule', 'add-schedule', 'add-schedule-range', 'update-schedule']);
const router = useRouter();
const { t } = useI18n();

const {
    currentDate, viewMode,
    currentYear, currentMonth,
    prevPeriod, nextPeriod, goToToday,
    setDate, setViewMode,
    getSchedulesByDate, isSameDate
} = useCalendar();

const viewModes = computed(() => [
    { label: t('calendar.view.month'), value: 'month' },
    { label: t('calendar.view.week'), value: 'week' },
    { label: t('calendar.view.day'), value: 'day' }
]);

const currentViewMode = computed(() => viewMode.value);

const changeViewMode = (mode: string) => {
    setViewMode(mode as any);
    if (mode === 'week') router.push({ name: 'schedule-week' }); // Assuming route creation
    if (mode === 'day') router.push({ name: 'schedule-day' });
    if (mode === 'month') router.push({ name: 'schedule-month' });
};

const weekDays = computed(() => [
    t('calendar.days.sun'), t('calendar.days.mon'), t('calendar.days.tue'),
    t('calendar.days.wed'), t('calendar.days.thu'), t('calendar.days.fri'),
    t('calendar.days.sat')
]);

const getHoliday = (date: Date) => {
    if (date.getMonth() === 0 && date.getDate() === 1) return '신정';
    // Simplified holiday logic
    return '';
};

const isToday = (date: Date) => {
    const today = new Date();
    return isSameDate(date, today);
};

const selectDate = (date: Date) => {
    // If dragging, don't just select (or handle in mouseup?)
    // Actually, mousedown starts drag, mouseup ends it. Click might fire after mouseup?
    // We can rely on router navigation or simply update state.
    setDate(date);
    emit('select-date', date);
};

const handleDoubleClick = (date: Date) => {
    // Open add popup for specific date
    // Current CalendarView uses @add-schedule="openAddPopup" but that's on sidebar.
    // We need CalendarView to listen for an event here too?
    // CalendarView uses <component :is="Component" @edit-schedule="openEditPopup" />
    // We should probably emit 'add-schedule' from here too if we want to reuse that.
    // But wait, CalendarView only listens for 'edit-schedule' on the router-view component?
    // Actually, let's emit 'add-schedule-with-range' or reuse 'edit-schedule' with null?
    // The cleanest is to emit a new event that CalendarView handles.
    // Let's check CalendarView again. It has @edit-schedule="openEditPopup".
    // We should add @add-schedule="openAddPopup" to the router-view component in CalendarView.
    emit('add-schedule', date);
};

// Drag Logic
const isDragging = ref(false);
const dragStart = ref<Date | null>(null);
const dragCurrent = ref<Date | null>(null);

const startDrag = (date: Date) => {
    isDragging.value = true;
    dragStart.value = date;
    dragCurrent.value = date;
};

const updateDrag = (date: Date) => {
    if (isDragging.value) {
        dragCurrent.value = date;
    }
};

const endDrag = () => {
    if (!isDragging.value || !dragStart.value || !dragCurrent.value) {
        isDragging.value = false;
        return;
    }

    // Determine range
    const start = dragStart.value < dragCurrent.value ? dragStart.value : dragCurrent.value;
    const end = dragStart.value < dragCurrent.value ? dragCurrent.value : dragStart.value;

    if (start.getTime() !== end.getTime()) {
        emit('add-schedule-range', start, end);
    }

    isDragging.value = false;
    dragStart.value = null;
    dragCurrent.value = null;
};

// Schedule DnD
const draggedSchedule = ref<any>(null);

const onScheduleDragStart = (e: DragEvent, sch: any) => {
    e.dataTransfer!.effectAllowed = 'move';
    e.dataTransfer!.dropEffect = 'move';
    draggedSchedule.value = sch;
};

const onWeekDrop = (e: DragEvent, week: any[]) => {
    e.preventDefault();
    if (!draggedSchedule.value) return;

    const currentTarget = e.currentTarget as HTMLElement;
    const rect = currentTarget.getBoundingClientRect();
    const x = e.clientX - rect.left;
    const colWidth = rect.width / 7;
    let dayIndex = Math.floor(x / colWidth);

    // Clamp index
    if (dayIndex < 0) dayIndex = 0;
    if (dayIndex > 6) dayIndex = 6;

    const targetDate = week[dayIndex].date;
    onScheduleDrop(e, targetDate); // Reuse existing logic
};

const onScheduleDrop = (e: DragEvent, targetDate: Date) => {
    // Stop propagation if we call this manually? 
    // Actually onWeekDrop is the handler now.
    // Logic extraction:
    if (!draggedSchedule.value) return;

    // Calculate duration
    const originalStart = new Date(draggedSchedule.value.date);
    const originalEnd = new Date(draggedSchedule.value.endDate);
    const duration = originalEnd.getTime() - originalStart.getTime();

    // New Start is the target date (preserves time)
    const newStart = new Date(targetDate);
    newStart.setHours(originalStart.getHours(), originalStart.getMinutes(), 0, 0);

    const newEnd = new Date(newStart.getTime() + duration);

    emit('update-schedule', draggedSchedule.value.id, {
        date: newStart,
        endDate: newEnd
    });

    draggedSchedule.value = null;
};

const isInDragRange = (date: Date) => {
    if (!isDragging.value || !dragStart.value || !dragCurrent.value) return false;

    const start = dragStart.value < dragCurrent.value ? dragStart.value : dragCurrent.value;
    const end = dragStart.value < dragCurrent.value ? dragCurrent.value : dragStart.value;

    // Normalize times to compare dates
    const t = date.getTime();
    const s = start.getTime();
    const e = end.getTime();

    // Simple check (might need to strip hours for robust comparison if dates have times)
    // Assuming dates from calendarDays are 00:00:00
    return t >= s && t <= e;
};

const calendarWeeks = computed(() => {
    const year = currentYear.value;
    const month = currentMonth.value;

    const firstDayOfMonth = new Date(year, month, 1);
    const lastDayOfMonth = new Date(year, month + 1, 0);

    const checkDays = [];

    const firstDayOfWeek = firstDayOfMonth.getDay();
    for (let i = firstDayOfWeek - 1; i >= 0; i--) {
        const d = new Date(year, month, -i);
        checkDays.push({ date: d, day: d.getDate(), isCurrentMonth: false });
    }

    for (let i = 1; i <= lastDayOfMonth.getDate(); i++) {
        const d = new Date(year, month, i);
        checkDays.push({ date: d, day: i, isCurrentMonth: true });
    }

    const remainingCells = 42 - checkDays.length;
    for (let i = 1; i <= remainingCells; i++) {
        const d = new Date(year, month + 1, i);
        checkDays.push({ date: d, day: i, isCurrentMonth: false });
    }

    // Chunk into weeks
    const weeks = [];
    for (let i = 0; i < checkDays.length; i += 7) {
        weeks.push(checkDays.slice(i, i + 7));
    }
    return weeks;
});

// Import schedules directly for efficient filtering
const { schedules } = useCalendar(); // Accessing schedules ref from useCalendar

const getWeekSchedules = (weekDays: any[]) => {
    const weekStart = weekDays[0].date;
    const weekEnd = new Date(weekDays[6].date);
    weekEnd.setHours(23, 59, 59, 999);

    // 1. Filter overlapping schedules
    const visibleSchedules = schedules.value.filter(s => {
        const start = new Date(s.date);
        start.setHours(0, 0, 0, 0);
        const end = new Date(s.endDate || s.date);
        end.setHours(23, 59, 59, 999);

        return start <= weekEnd && end >= weekStart;
    });

    // 2. Sort: Longest duration first, or just start date?
    // Usually: Start Date ASC, then Duration DESC
    visibleSchedules.sort((a, b) => {
        const startA = new Date(a.date).getTime();
        const startB = new Date(b.date).getTime();
        if (startA !== startB) return startA - startB;

        const durA = (new Date(a.endDate || a.date).getTime()) - startA;
        const durB = (new Date(b.endDate || b.date).getTime()) - startB;
        return durB - durA; // Longer first
    });

    // 3. Layout (Stacking)
    const processed = [];
    const dayRows = Array(7).fill(0).map(() => []); // Track occupied rows per day

    for (const sch of visibleSchedules) {
        const schStart = new Date(sch.date);
        schStart.setHours(0, 0, 0, 0);
        const schEnd = new Date(sch.endDate || sch.date);
        schEnd.setHours(23, 59, 59, 999);

        // Determine Start Index (0-6)
        let startIndex = 0;
        if (schStart >= weekStart) {
            // Find index match
            const offset = Math.floor((schStart.getTime() - weekStart.getTime()) / (24 * 60 * 60 * 1000));
            // Be careful with DST/Timezones if using simple division. 
            // Better: getDay() if we are sure week starts Sunday.
            startIndex = schStart.getDay();
        } else {
            startIndex = 0; // Starts before this week
        }

        // Determine End Index (0-6)
        let endIndex = 6;
        if (schEnd <= weekEnd) {
            const offset = Math.floor((schEnd.getTime() - weekStart.getTime()) / (24 * 60 * 60 * 1000)); // Rough check
            endIndex = schEnd.getDay();
        } else {
            endIndex = 6; // Ends after this week
        }

        // Safety bound
        if (startIndex < 0) startIndex = 0;
        if (endIndex > 6) endIndex = 6;

        const colSpan = endIndex - startIndex + 1;

        // Find available Row Index
        let rowIndex = 0;
        while (true) {
            let conflict = false;
            for (let i = startIndex; i <= endIndex; i++) {
                if (dayRows[i][rowIndex]) {
                    conflict = true;
                    break;
                }
            }
            if (!conflict) break;
            rowIndex++;
        }

        // Mark occupied
        for (let i = startIndex; i <= endIndex; i++) {
            dayRows[i][rowIndex] = true;
        }

        processed.push({
            data: sch,
            style: {
                gridColumnStart: startIndex + 1,
                gridColumnEnd: `span ${colSpan}`,
                top: `${rowIndex * 24 + 24}px`, // 24px per row + offset for date number
                position: 'absolute',
                left: '2px', // small margin
                right: '2px'
            }
        });
    }

    return processed;
};
</script>
