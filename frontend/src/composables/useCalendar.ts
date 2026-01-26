import { ref, computed } from 'vue';

// Global state (lifted out of the function to be shared across components)
const currentDate = ref(new Date());
const selectedDate = ref(new Date());
const viewMode = ref<'month' | 'week' | 'day'>('month');
const searchQuery = ref('');

// Mock Data
const schedules = ref([
    { id: 1, title: '주간회의', date: new Date(2026, 0, 5, 10, 0), endDate: new Date(2026, 0, 5, 11, 0), type: 'work', location: '회의실 A', description: '매주 진행되는 주간 업무 보고 회의', allDay: false },
    { id: 2, title: '점심 약속', date: new Date(2026, 0, 7, 12, 0), endDate: new Date(2026, 0, 7, 13, 0), type: 'personal', location: '강남역', description: '친구와 점심', allDay: false },
    { id: 3, title: '프로젝트 마감', date: new Date(2026, 0, 9), endDate: new Date(2026, 0, 9), type: 'work', description: 'Q1 프로젝트 1차 마감', allDay: true },
    { id: 4, title: '팀 회식', date: new Date(2026, 0, 9, 18, 0), endDate: new Date(2026, 0, 9, 21, 0), type: 'shared', location: '고깃집', description: '신년 맞이 팀 회식', allDay: false },
    { id: 5, title: '가족 여행', date: new Date(2026, 0, 24), endDate: new Date(2026, 0, 27), type: 'personal', location: '제주도', description: '3박 4일 가족 여행', allDay: true },
]);

// Helper
const isSameDate = (d1: Date, d2: Date) => {
    return d1.getFullYear() === d2.getFullYear() &&
        d1.getMonth() === d2.getMonth() &&
        d1.getDate() === d2.getDate();
};

export function useCalendar() {

    // Actions
    const setDate = (date: Date) => {
        currentDate.value = new Date(date);
        selectedDate.value = new Date(date);
    };

    const prevPeriod = () => {
        const d = new Date(currentDate.value);
        if (viewMode.value === 'month') {
            d.setMonth(d.getMonth() - 1);
        } else if (viewMode.value === 'week') {
            d.setDate(d.getDate() - 7);
        } else {
            d.setDate(d.getDate() - 1);
        }
        currentDate.value = d;
    };

    const nextPeriod = () => {
        const d = new Date(currentDate.value);
        if (viewMode.value === 'month') {
            d.setMonth(d.getMonth() + 1);
        } else if (viewMode.value === 'week') {
            d.setDate(d.getDate() + 7);
        } else {
            d.setDate(d.getDate() + 1);
        }
        currentDate.value = d;
    };

    const setViewMode = (mode: 'month' | 'week' | 'day') => {
        viewMode.value = mode;
    };

    const goToToday = () => {
        currentDate.value = new Date();
        selectedDate.value = new Date();
    };

    const updateSchedule = (id: number, updates: Partial<any>) => {
        const index = schedules.value.findIndex(s => s.id === id);
        if (index !== -1) {
            schedules.value[index] = { ...schedules.value[index], ...updates };
        }
    };

    // Getters
    const currentYear = computed(() => currentDate.value.getFullYear());
    const currentMonth = computed(() => currentDate.value.getMonth());

    const getSchedulesByDate = (date: Date) => {
        return schedules.value.filter(s => {
            if (s.allDay) {
                // Check intersect
                const start = new Date(s.date);
                start.setHours(0, 0, 0, 0);
                const end = new Date(s.endDate || s.date);
                end.setHours(23, 59, 59, 999);
                const target = new Date(date);
                target.setHours(12, 0, 0, 0);
                return target >= start && target <= end;
            } else {
                return isSameDate(new Date(s.date), date);
            }
        });
    };

    return {
        currentDate,
        selectedDate,
        viewMode,
        searchQuery,
        schedules,
        currentYear,
        currentMonth,
        setDate,
        prevPeriod,
        nextPeriod,
        setViewMode,
        goToToday,
        getSchedulesByDate,
        isSameDate,
        updateSchedule
    };
}
