<template>
    <div class="h-full flex flex-col">
        <!-- Toolbar -->
        <div
            class="px-6 py-4 bg-white dark:bg-zinc-900 border-b border-gray-200 dark:border-zinc-700 flex justify-between items-center">
            <div class="flex items-center gap-4">
                <h2 class="text-xl font-bold text-gray-800 dark:text-gray-100 flex items-center gap-2">
                    <button class="p-1 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded transition-colors"
                        @click="prevPeriod">
                        <ChevronLeftIcon class="w-5 h-5" />
                    </button>
                    <CalendarDatePicker :currentDate="currentDate" @update-date="setDate">
                        {{ currentYear }}.{{ currentMonth + 1 }}.{{ currentDate.getDate() }} ({{ dayName }})
                    </CalendarDatePicker>
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

        <!-- Day View -->
        <div class="flex-1 flex flex-col overflow-hidden bg-white dark:bg-zinc-900">
            <div class="flex-1 overflow-y-auto">
                <div class="grid grid-cols-[60px_1fr] relative min-h-[600px]">
                    <!-- Time Column -->
                    <div class="border-r border-gray-100 dark:border-zinc-800">
                        <div v-for="hour in 24" :key="hour" class="h-16 text-xs text-right pr-2 text-gray-400"
                            :class="hour === 1 ? '' : '-mt-2.5'">
                            {{ hour - 1 }}:00
                        </div>
                    </div>

                    <!-- Day Column -->
                    <div class="relative select-none group" @mousedown="startDrag($event, currentDate)"
                        @mousemove="updateDrag($event)" @mouseup="endDrag(currentDate)"
                        @dblclick="handleDoubleClick($event, currentDate)" @dragover="onDragOver"
                        @drop="onDrop($event, currentDate)">

                        <!-- Grid Lines -->
                        <div v-for="hour in 24" :key="hour"
                            class="h-16 border-b border-gray-50 dark:border-zinc-800/50 pointer-events-none"></div>

                        <!-- Drag Indicator -->
                        <div v-if="isDragging"
                            class="absolute left-0 right-0 bg-blue-500/20 border-l-4 border-blue-500 z-10 pointer-events-none"
                            :style="dragStyle">
                            <div class="text-xs text-blue-700 font-bold px-1">
                                {{ dragTimeString }}
                            </div>
                        </div>

                        <!-- Schedules -->
                        <template v-for="sch in getSchedulesByDate(currentDate)" :key="sch.id">
                            <div v-if="!sch.allDay"
                                class="absolute left-2 right-2 rounded px-3 py-2 text-sm overflow-hidden cursor-pointer hover:shadow-md transition-shadow"
                                draggable="true" @dragstart.stop="onScheduleDragStart($event, sch)"
                                :style="getScheduleStyle(sch)" :class="[
                                    sch.type === 'personal' ? 'bg-blue-100 text-blue-700 border-l-4 border-blue-500' :
                                        sch.type === 'work' ? 'bg-green-100 text-green-700 border-l-4 border-green-500' :
                                            'bg-purple-100 text-purple-700 border-l-4 border-purple-500'
                                ]" @click="$emit('edit-schedule', sch)">
                                <div class="font-bold flex justify-between">
                                    <span>{{ sch.title }}</span>
                                    <span class="text-xs opacity-75">{{ getTimeString(sch.date) }} - {{
                                        getTimeString(sch.endDate) }}</span>
                                </div>
                                <div class="mt-1 opacity-75">{{ sch.location }}</div>
                                <div class="mt-1 text-xs opacity-60">{{ sch.description }}</div>
                            </div>
                        </template>
                    </div>
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

const emit = defineEmits(['edit-schedule', 'add-schedule', 'add-schedule-range', 'update-schedule']);
const router = useRouter();
const { t } = useI18n();

const {
    currentDate, viewMode, currentYear, currentMonth,
    prevPeriod, nextPeriod, goToToday,
    setViewMode, getSchedulesByDate, setDate
} = useCalendar();

const viewModes = computed(() => [
    { label: t('calendar.view.month'), value: 'month' },
    { label: t('calendar.view.week'), value: 'week' },
    { label: t('calendar.view.day'), value: 'day' }
]);

const currentViewMode = computed(() => viewMode.value);

const changeViewMode = (mode: string) => {
    setViewMode(mode as any);
    if (mode === 'week') router.push({ name: 'schedule-week' });
    if (mode === 'day') router.push({ name: 'schedule-day' });
    if (mode === 'month') router.push({ name: 'schedule-month' });
};

const weekDayNames = computed(() => [
    t('calendar.days.sun'), t('calendar.days.mon'), t('calendar.days.tue'),
    t('calendar.days.wed'), t('calendar.days.thu'), t('calendar.days.fri'),
    t('calendar.days.sat')
]);

const dayName = computed(() => weekDayNames.value[currentDate.value.getDay()]);

const getTimeString = (d: Date | string) => {
    const date = new Date(d);
    return `${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};

const getScheduleStyle = (sch: any) => {
    const start = new Date(sch.date);
    const end = sch.endDate ? new Date(sch.endDate) : new Date(start.getTime() + 60 * 60 * 1000);

    const startHour = start.getHours() + start.getMinutes() / 60;
    const endHour = end.getHours() + end.getMinutes() / 60;

    // 1 hour = 64px (h-16)
    const top = startHour * 64;
    const height = (endHour - startHour) * 64;

    return {
        top: `${top}px`,
        height: `${Math.max(height, 32)}px`
    };
};

// Schedule Drag & Drop Logic
const draggedSchedule = ref<any>(null);
const dragOffsetMinutes = ref(0);

const onScheduleDragStart = (e: DragEvent, sch: any) => {
    e.dataTransfer!.effectAllowed = 'move';
    e.dataTransfer!.dropEffect = 'move';
    draggedSchedule.value = sch;

    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const clickY = e.clientY - rect.top;
    // 64px = 1 hour => 1 min = 64/60
    const clickMinutes = Math.floor(clickY / (64 / 60));
    dragOffsetMinutes.value = clickMinutes;
};

const onDragOver = (e: DragEvent) => {
    e.preventDefault();
    e.dataTransfer!.dropEffect = 'move';
};

const onDrop = (e: DragEvent, day: Date) => {
    e.preventDefault();
    if (!draggedSchedule.value) return;

    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const dropY = e.clientY - rect.top;

    // Snap to 30 min (32px)
    const snappedY = Math.floor(dropY / 32) * 32;
    const dropHour = snappedY / 64;

    let newStartHour = dropHour - (dragOffsetMinutes.value / 60);
    newStartHour = Math.round(newStartHour * 2) / 2;
    if (newStartHour < 0) newStartHour = 0;

    const originalStart = new Date(draggedSchedule.value.date);
    const originalEnd = new Date(draggedSchedule.value.endDate);
    const duration = originalEnd.getTime() - originalStart.getTime();

    const newStartDate = new Date(day);
    newStartDate.setHours(Math.floor(newStartHour), (newStartHour % 1) * 60);

    const newEndDate = new Date(newStartDate.getTime() + duration);

    emit('update-schedule', draggedSchedule.value.id, {
        date: newStartDate,
        endDate: newEndDate
    });

    draggedSchedule.value = null;
    dragOffsetMinutes.value = 0;
};

// Interaction Logic (Similar to Week, but simpler as day is fixed)
const isDragging = ref(false);
const dragStartY = ref(0);
const dragEndY = ref(0);

// Use 64px per hour for Day view (vs 48px for Week)
const HOUR_HEIGHT = 64;
const SNAP_HEIGHT = 32; // 30 mins

const startDrag = (e: MouseEvent, day: Date) => {
    if (e.button !== 0) return;
    if ((e.target as HTMLElement).closest('.cursor-pointer.absolute')) return;

    isDragging.value = true;

    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const y = e.clientY - rect.top;

    const snappedY = Math.floor(y / SNAP_HEIGHT) * SNAP_HEIGHT;

    dragStartY.value = snappedY;
    dragEndY.value = snappedY + SNAP_HEIGHT;
};

const updateDrag = (e: MouseEvent) => {
    if (!isDragging.value) return;

    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const y = e.clientY - rect.top;

    const snappedY = Math.floor(y / SNAP_HEIGHT) * SNAP_HEIGHT;

    dragEndY.value = Math.max(snappedY + SNAP_HEIGHT, dragStartY.value + SNAP_HEIGHT);
};

const endDrag = (day: Date) => {
    if (!isDragging.value) return;

    const startHour = dragStartY.value / HOUR_HEIGHT;
    const endHour = dragEndY.value / HOUR_HEIGHT;

    const start = new Date(day);
    start.setHours(Math.floor(startHour), (startHour % 1) * 60);

    const end = new Date(day);
    end.setHours(Math.floor(endHour), (endHour % 1) * 60);

    emit('add-schedule-range', start, end);

    isDragging.value = false;
};

const handleDoubleClick = (e: MouseEvent, day: Date) => {
    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const y = e.clientY - rect.top;
    const snappedY = Math.floor(y / SNAP_HEIGHT) * SNAP_HEIGHT;
    const hour = snappedY / HOUR_HEIGHT;

    const date = new Date(day);
    date.setHours(Math.floor(hour), (hour % 1) * 60);

    emit('add-schedule', date);
};

// Visual helpers
const dragStyle = computed(() => {
    const top = Math.min(dragStartY.value, dragEndY.value);
    const height = Math.abs(dragEndY.value - dragStartY.value);
    return {
        top: `${top}px`,
        height: `${height}px`
    };
});

const dragTimeString = computed(() => {
    const startHour = dragStartY.value / HOUR_HEIGHT;
    const endHour = dragEndY.value / HOUR_HEIGHT;

    const format = (h: number) => {
        const hh = Math.floor(h);
        const mm = (h % 1) * 60;
        return `${String(hh).padStart(2, '0')}:${String(mm).padStart(2, '0')}`;
    };

    return `${format(startHour)} - ${format(endHour)}`;
});
</script>
