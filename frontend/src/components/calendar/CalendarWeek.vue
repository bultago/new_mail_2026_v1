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
                        {{ weekTitle }}
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

        <!-- Week Grid -->
        <div class="flex-1 flex flex-col overflow-hidden bg-white dark:bg-zinc-900">
            <!-- Header -->
            <div class="grid grid-cols-8 border-b border-gray-200 dark:border-zinc-700">
                <div class="w-16 border-r border-gray-100 dark:border-zinc-800"></div> <!-- Time Col -->
                <div v-for="(day, index) in weekDates" :key="index"
                    class="py-2 text-center text-sm font-bold border-r border-gray-100 dark:border-zinc-800 last:border-r-0"
                    :class="[
                        isToday(day) ? 'text-blue-600' : 'text-gray-700 dark:text-gray-300'
                    ]">
                    <div>{{ weekDayNames[index] }}</div>
                    <div class="text-lg">{{ day.getDate() }}</div>
                </div>
            </div>

            <!-- Time Grid -->
            <div class="flex-1 overflow-y-auto">
                <div class="grid grid-cols-8 relative min-h-[600px]">
                    <!-- Time Column -->
                    <div class="border-r border-gray-100 dark:border-zinc-800">
                        <div v-for="hour in 24" :key="hour" class="h-12 text-xs text-right pr-2 text-gray-400"
                            :class="hour === 1 ? '' : '-mt-2.5'">
                            {{ hour - 1 }}:00
                        </div>
                    </div>

                    <!-- Day Columns -->
                    <div v-for="(day, dIndex) in weekDates" :key="dIndex"
                        class="relative border-r border-gray-100 dark:border-zinc-800 select-none group"
                        @mousedown="startDrag($event, day)" @mousemove="updateDrag($event, day)" @mouseup="endDrag(day)"
                        @dblclick="handleDoubleClick($event, day)" @dragover="onDragOver" @drop="onDrop($event, day)">

                        <!-- Grid Lines -->
                        <div v-for="hour in 24" :key="hour"
                            class="h-12 border-b border-gray-50 dark:border-zinc-800/50 pointer-events-none"></div>

                        <!-- Drag Indicator -->
                        <div v-show="isDragging"
                            class="absolute left-0 right-0 bg-blue-500/20 border-l-2 border-blue-500 z-10 pointer-events-none"
                            :style="getDragIndicatorStyle(day)">
                            <div class="text-xs text-blue-700 font-bold px-1">
                                {{ dragTimeString }}
                            </div>
                        </div>

                        <!-- Schedules -->
                        <template v-for="sch in getSchedulesByDate(day)" :key="sch.id">
                            <div v-if="!sch.allDay"
                                class="absolute left-1 right-1 rounded px-1.5 py-1 text-xs overflow-hidden cursor-pointer hover:shadow-md transition-shadow"
                                draggable="true" @dragstart.stop="onScheduleDragStart($event, sch)"
                                :style="getScheduleStyle(sch)" :class="[
                                    sch.type === 'personal' ? 'bg-blue-100 text-blue-700 border-l-2 border-blue-500' :
                                        sch.type === 'work' ? 'bg-green-100 text-green-700 border-l-2 border-green-500' :
                                            'bg-purple-100 text-purple-700 border-l-2 border-purple-500'
                                ]" @click="$emit('edit-schedule', sch)">
                                <div class="font-bold truncate">{{ sch.title }}</div>
                                <div class="truncate opacity-75">{{ sch.location }}</div>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { computed, watch, ref } from 'vue';
import { useRouter } from 'vue-router';
import { useI18n } from 'vue-i18n';
import { ChevronLeftIcon, ChevronRightIcon } from 'lucide-vue-next';
import { useCalendar } from '../../composables/useCalendar';
import CalendarDatePicker from './CalendarDatePicker.vue';

const emit = defineEmits(['edit-schedule', 'add-schedule', 'add-schedule-range', 'update-schedule']);
const router = useRouter();
const { t } = useI18n();

const {
    currentDate, viewMode,
    prevPeriod, nextPeriod, goToToday,
    setViewMode, getSchedulesByDate, isSameDate, setDate
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

const weekDates = computed(() => {
    const curr = currentDate.value;
    const first = curr.getDate() - curr.getDay();

    const days = [];
    for (let i = 0; i < 7; i++) {
        days.push(new Date(curr.getFullYear(), curr.getMonth(), first + i));
    }
    return days;
});

const weekTitle = computed(() => {
    const start = weekDates.value[0];
    const end = weekDates.value[6];
    return `${start.getFullYear()}.${start.getMonth() + 1}.${start.getDate()} - ${end.getMonth() + 1}.${end.getDate()}`;
});

const isToday = (date: Date) => {
    const today = new Date();
    return isSameDate(date, today);
};

const getScheduleStyle = (sch: any) => {
    const start = new Date(sch.date);
    const end = sch.endDate ? new Date(sch.endDate) : new Date(start.getTime() + 60 * 60 * 1000); // Default 1hr

    const startHour = start.getHours() + start.getMinutes() / 60;
    const endHour = end.getHours() + end.getMinutes() / 60;

    const top = startHour * 48; // 12 * 4 (h-12 is 3rem -> 48px)
    const height = (endHour - startHour) * 48;

    return {
        top: `${top}px`,
        height: `${Math.max(height, 24)}px` // Min height 24px
    };
};

// Schedule Drag & Drop Logic
const draggedSchedule = ref<any>(null);
const dragOffsetMinutes = ref(0);

const onScheduleDragStart = (e: DragEvent, sch: any) => {
    e.dataTransfer!.effectAllowed = 'move';
    e.dataTransfer!.dropEffect = 'move';
    draggedSchedule.value = sch;

    // Calculate offset from start time
    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const clickY = e.clientY - rect.top;
    // 48px = 1 hour => 1 min = 48/60 = 0.8px
    const clickMinutes = Math.floor(clickY / (48 / 60));
    dragOffsetMinutes.value = clickMinutes;
};

const onDragOver = (e: DragEvent) => {
    e.preventDefault(); // Allow drop
    e.dataTransfer!.dropEffect = 'move';
};

const onDrop = (e: DragEvent, day: Date) => {
    e.preventDefault();
    if (!draggedSchedule.value) return;

    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const dropY = e.clientY - rect.top;

    // Snap to 30 min (24px)
    const snappedY = Math.floor(dropY / 24) * 24;
    const dropHour = snappedY / 48; // e.g. 10.5

    // Calculate New Start Time = (DropY / 48) - (dragOffsetMinutes / 60)
    let newStartHour = dropHour - (dragOffsetMinutes.value / 60);

    // Snap newStartHour to nearest 30 min (0.5)
    newStartHour = Math.round(newStartHour * 2) / 2;

    // Ensure it's non-negative
    if (newStartHour < 0) newStartHour = 0;

    // Calculate changes
    const originalStart = new Date(draggedSchedule.value.date);
    const originalEnd = new Date(draggedSchedule.value.endDate);
    const duration = originalEnd.getTime() - originalStart.getTime(); // ms

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

// Interaction Logic
const isDragging = ref(false);
const dragStartDay = ref<Date | null>(null);
const dragEndDay = ref<Date | null>(null);
const dragStartY = ref(0);
const dragEndY = ref(0);

const startDrag = (e: MouseEvent, day: Date) => {
    if (e.button !== 0) return;
    if ((e.target as HTMLElement).closest('.cursor-pointer.absolute')) return;

    isDragging.value = true;
    dragStartDay.value = day;
    dragEndDay.value = day;

    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const y = e.clientY - rect.top;
    const snappedY = Math.floor(y / 24) * 24;

    dragStartY.value = snappedY;
    dragEndY.value = snappedY + 24;
};

const updateDrag = (e: MouseEvent, day: Date) => {
    if (!isDragging.value || !dragStartDay.value) return;

    // Update end day if we moved to a different column
    if (!isSameDate(dragEndDay.value!, day)) {
        dragEndDay.value = day;
    }

    // Only update Y if we are on the same day as start or end (logic simplification)
    // Actually, we should track the Y of the current mouse position regardless of day.
    // If we are on a different day, the visual indicator logic handles the full height.
    // But we need to know the 'end' Y relative to the 'end' day.

    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const y = e.clientY - rect.top;
    const snappedY = Math.floor(y / 24) * 24;

    // If we are on the start day, dragEndY updates (visual only relevant if start==end)
    // If we are on end day, dragEndY updates.
    // We just store the current Y as dragEndY.
    dragEndY.value = snappedY + 24;
};

const endDrag = (day: Date) => {
    console.log('endDrag', day, isDragging.value);
    if (!isDragging.value || !dragStartDay.value || !dragEndDay.value) return;

    let start = new Date(dragStartDay.value);
    let end = new Date(dragEndDay.value);

    // Swap if end is before start
    if (start.getTime() > end.getTime()) {
        const temp = start;
        start = end;
        end = temp;
    }

    const startHour = dragStartY.value / 48;
    const endHour = dragEndY.value / 48; // This is the Y on the 'current' day (which is endDay in our logic)

    // Set time for start date
    // If start == end, use startHour/endHour as is (with swap check for Y)
    // If multi-day: 
    //   Start Date starts at dragStartY (or if swapped, dragEndY?) 
    //   Actually, simpler approach:
    //   - Start Date: Always use dragStartY (time on the click-down day)
    //   - End Date: Always use dragEndY (time on the mouse-up day)

    // However, if days were swapped (dragged backwards), we need to handle that.
    // Let's assume startDay <= endDay (chronologically).
    if (dragStartDay.value.getTime() > dragEndDay.value.getTime()) {
        // Dragged backwards.
        // Start Time: dragEndY on the 'new' start day (which was the dragEndDay).
        start.setHours(Math.floor(endHour), (endHour % 1) * 60);

        // End Time: dragStartY on the 'new' end day (which was the dragStartDay).
        end.setHours(Math.floor(startHour), (startHour % 1) * 60);
    } else {
        // Dragged forwards or same day.
        start.setHours(Math.floor(startHour), (startHour % 1) * 60);
        end.setHours(Math.floor(endHour), (endHour % 1) * 60);
    }

    // Ensure Start < End if exactly same time or inverted Y on same day
    if (start.getTime() > end.getTime()) {
        const temp = start;
        start = end;
        end = temp;
    }

    emit('add-schedule-range', start, end);

    isDragging.value = false;
    dragStartDay.value = null;
    dragEndDay.value = null;
};

const handleDoubleClick = (e: MouseEvent, day: Date) => {
    const rect = (e.currentTarget as HTMLElement).getBoundingClientRect();
    const y = e.clientY - rect.top;
    const snappedY = Math.floor(y / 24) * 24;
    const hour = snappedY / 48;

    const date = new Date(day);
    date.setHours(Math.floor(hour), (hour % 1) * 60);

    emit('add-schedule', date);
};

// Visual helpers
const getDragIndicatorStyle = (day: Date) => {
    if (!isDragging.value || !dragStartDay.value || !dragEndDay.value) return {};

    const start = dragStartDay.value;
    const end = dragEndDay.value;
    const current = day;

    // Determine chronological order
    const isStartBeforeEnd = start.getTime() <= end.getTime();
    const chronStart = isStartBeforeEnd ? start : end;
    const chronEnd = isStartBeforeEnd ? end : start;

    // Check if day is within range
    if (current.getTime() < chronStart.getTime() || current.getTime() > chronEnd.getTime()) return { display: 'none' };

    let top = '0px';
    let height = '0px';

    if (isSameDate(current, chronStart) && isSameDate(current, chronEnd)) {
        // Single day drag
        const y1 = dragStartY.value;
        const y2 = dragEndY.value;
        top = `${Math.min(y1, y2)}px`;
        height = `${Math.abs(y1 - y2)}px`;
    }
    else if (isSameDate(current, chronStart)) {
        // First day of multi-day
        // If originally start, use dragStartY. If originally end, use dragEndY.
        const y = isStartBeforeEnd ? dragStartY.value : dragEndY.value;
        top = `${y}px`;
        height = `calc(100% - ${y}px)`; // To bottom
    }
    else if (isSameDate(current, chronEnd)) {
        // Last day of multi-day
        const y = isStartBeforeEnd ? dragEndY.value : dragStartY.value;
        top = `0px`;
        height = `${y}px`; // From top
    }
    else {
        // Middle days
        top = `0px`;
        height = `100%`;
    }

    return {
        top,
        height,
        display: 'block'
    };
};

const dragTimeString = computed(() => {
    if (!isDragging.value || !dragStartDay.value || !dragEndDay.value) return '';
    // Simplify for multi-day: just show times if same day, or nothing/simple text if multi
    if (isSameDate(dragStartDay.value, dragEndDay.value)) {
        const startHour = dragStartY.value / 48;
        const endHour = dragEndY.value / 48;
        const format = (h: number) => {
            const hh = Math.floor(h);
            const mm = (h % 1) * 60;
            return `${String(hh).padStart(2, '0')}:${String(mm).padStart(2, '0')}`;
        };
        return `${format(Math.min(startHour, endHour))} - ${format(Math.max(startHour, endHour))}`;
    }
    return ''; // Hide time text for multi-day to avoid clutter or complex calculation
});
</script>
