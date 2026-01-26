<template>
    <div class="relative" ref="containerRef">
        <!-- Trigger -->
        <button
            class="px-2 py-1 rounded hover:bg-gray-100 dark:hover:bg-zinc-800 font-bold text-inherit transition-colors flex items-center gap-1"
            @click="toggle">
            <slot>
                {{ currentYear }}.{{ String(currentMonth + 1).padStart(2, '0') }}
            </slot>
        </button>

        <!-- Popup -->
        <div v-if="isOpen"
            class="absolute top-full left-0 mt-2 bg-white dark:bg-zinc-900 border border-gray-200 dark:border-zinc-700 rounded-lg shadow-xl z-50 w-64 p-4">

            <!-- Header -->
            <div class="flex items-center justify-between mb-4">
                <button class="p-1 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded text-gray-600 dark:text-gray-400"
                    @click.stop="navigate(-1)">
                    <ChevronLeftIcon class="w-4 h-4" />
                </button>
                <div class="flex items-center gap-1 font-bold text-gray-800 dark:text-gray-200 select-none">
                    <span @click.stop="setViewMode('year')" data-testid="picker-year"
                        class="cursor-pointer hover:text-blue-600 dark:hover:text-blue-400 transition-colors">
                        {{ displayYear }}
                    </span>
                    <span v-if="viewMode === 'month'" class="text-gray-400">.</span>
                    <span v-if="viewMode === 'month'" class="cursor-default">
                        {{ String(currentMonth + 1).padStart(2, '0') }}
                    </span>
                </div>
                <button class="p-1 hover:bg-gray-100 dark:hover:bg-zinc-800 rounded text-gray-600 dark:text-gray-400"
                    @click.stop="navigate(1)">
                    <ChevronRightIcon class="w-4 h-4" />
                </button>
            </div>

            <!-- Month Grid -->
            <div v-if="viewMode === 'month'" class="grid grid-cols-4 gap-2">
                <button v-for="month in 12" :key="month"
                    class="py-2 text-sm rounded hover:bg-gray-100 dark:hover:bg-zinc-800 transition-colors" :class="[
                        isCurrentMonth(month - 1) ? 'bg-blue-100 text-blue-600 font-bold dark:bg-blue-900/30 dark:text-blue-400' : 'text-gray-700 dark:text-gray-300'
                    ]" @click.stop="selectMonth(month - 1)">
                    {{ month }}
                </button>
            </div>

            <!-- Year Grid -->
            <div v-else class="grid grid-cols-4 gap-2">
                <button v-for="year in yearRange" :key="year"
                    class="py-2 text-sm rounded hover:bg-gray-100 dark:hover:bg-zinc-800 transition-colors" :class="[
                        year === displayYear ? 'bg-blue-100 text-blue-600 font-bold dark:bg-blue-900/30 dark:text-blue-400' : 'text-gray-700 dark:text-gray-300'
                    ]" @click.stop="selectYear(year)" :data-testid="`year-${year}`">
                    {{ year }}
                </button>
            </div>
        </div>

        <!-- Backdrop (Transparent) -->
        <div v-if="isOpen" class="fixed inset-0 z-40" @click="close"></div>
    </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, onMounted, onUnmounted } from 'vue';
import { ChevronLeftIcon, ChevronRightIcon } from 'lucide-vue-next';

const props = defineProps<{
    currentDate: Date;
}>();

const emit = defineEmits(['update-date']);

const isOpen = ref(false);
const containerRef = ref<HTMLElement | null>(null);

const currentYear = computed(() => props.currentDate.getFullYear());
const currentMonth = computed(() => props.currentDate.getMonth());

// Internal state for navigation within popup
const displayYear = ref(currentYear.value);
const viewMode = ref<'month' | 'year'>('month');

const yearRange = computed(() => {
    // Show 12 years centered around displayYear (approx)
    const start = displayYear.value - 6;
    return Array.from({ length: 12 }, (_, i) => start + i);
});

watch(isOpen, (newVal) => {
    if (newVal) {
        displayYear.value = currentYear.value;
        viewMode.value = 'month';
    }
});

const toggle = () => {
    isOpen.value = !isOpen.value;
};

const close = () => {
    isOpen.value = false;
};

const setViewMode = (mode: 'month' | 'year') => {
    viewMode.value = mode;
};

const navigate = (delta: number) => {
    if (viewMode.value === 'month') {
        displayYear.value += delta;
    } else {
        // In year mode, jump by 12 years
        displayYear.value += delta * 12;
    }
};

const selectYear = (year: number) => {
    displayYear.value = year;
    viewMode.value = 'month';
};

const selectMonth = (monthIndex: number) => {
    const newDate = new Date(displayYear.value, monthIndex, 1);
    emit('update-date', newDate);
    close();
};

const isCurrentMonth = (monthIndex: number) => {
    return displayYear.value === currentYear.value && monthIndex === currentMonth.value;
};

// Click outside handler for extra safety (though backdrop handles most)
const handleClickOutside = (event: MouseEvent) => {
    if (containerRef.value && !containerRef.value.contains(event.target as Node)) {
        close();
    }
};

onMounted(() => {
    document.addEventListener('click', handleClickOutside);
});

onUnmounted(() => {
    document.removeEventListener('click', handleClickOutside);
});
</script>
