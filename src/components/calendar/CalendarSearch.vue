<template>
    <div class="h-full flex flex-col">
        <!-- Toolbar -->
        <div
            class="px-6 py-4 bg-white dark:bg-zinc-900 border-b border-gray-200 dark:border-zinc-700 flex justify-between items-center">
            <h2 class="text-xl font-bold text-gray-800 dark:text-gray-100 flex items-center gap-2">
                {{ t('calendar.search_result') }}: "{{ query }}"
            </h2>
            <button
                class="px-3 py-1.5 text-sm font-medium border border-gray-300 dark:border-zinc-600 rounded hover:bg-gray-50 dark:hover:bg-zinc-800"
                @click="$router.push('/schedule')">
                {{ t('calendar.back_to_calendar') }}
            </button>
        </div>

        <!-- Result List -->
        <div class="flex-1 overflow-y-auto bg-white dark:bg-zinc-900 p-6">
            <div v-if="results.length === 0" class="text-center text-gray-500 py-10">
                {{ t('calendar.no_result') }}
            </div>
            <div v-else class="space-y-2">
                <div v-for="sch in results" :key="sch.id"
                    class="p-4 border border-gray-200 dark:border-zinc-700 rounded-lg hover:shadow-md transition-shadow cursor-pointer bg-white dark:bg-zinc-800"
                    @click="$emit('edit-schedule', sch)">
                    <div class="flex items-start justify-between">
                        <div>
                            <h3 class="font-bold text-gray-800 dark:text-gray-100 mb-1">{{ sch.title }}</h3>
                            <div class="text-sm text-gray-500 dark:text-gray-400 flex items-center gap-2">
                                <span>{{ formatDate(sch.date) }}</span>
                                <span v-if="sch.location"
                                    class="px-1.5 py-0.5 bg-gray-100 dark:bg-zinc-700 rounded text-xs">
                                    {{ sch.location }}
                                </span>
                            </div>
                        </div>
                        <span class="text-xs px-2 py-1 rounded font-medium" :class="[
                            sch.type === 'personal' ? 'bg-blue-100 text-blue-700 dark:bg-blue-900/30 dark:text-blue-300' :
                                sch.type === 'work' ? 'bg-green-100 text-green-700 dark:bg-green-900/30 dark:text-green-300' :
                                    'bg-purple-100 text-purple-700 dark:bg-purple-900/30 dark:text-purple-300'
                        ]">
                            {{ sch.type === 'personal' ? t('calendar.my_calendar') : sch.type === 'work' ?
                                t('calendar.work') : t('calendar.shared_calendar') }}
                        </span>
                    </div>
                    <div v-if="sch.description" class="mt-2 text-sm text-gray-600 dark:text-gray-300 line-clamp-2">
                        {{ sch.description }}
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, watch, computed } from 'vue';
import { useRoute } from 'vue-router';
import { useI18n } from 'vue-i18n';

const route = useRoute();
const { t } = useI18n();
const query = computed(() => route.query.q as string || '');

// Mock Data (Should match CalendarMonth or come from store)
const mockSchedules = [
    { id: 1, title: '주간회의', date: new Date(2026, 0, 5), type: 'work', location: '회의실 A', description: '매주 진행되는 주간 업무 보고 회의' },
    { id: 2, title: '점심 약속', date: new Date(2026, 0, 7), type: 'personal', location: '강남역', description: '친구와 점심' },
    { id: 3, title: '프로젝트 마감', date: new Date(2026, 0, 9), type: 'work', description: 'Q1 프로젝트 1차 마감' },
    { id: 4, title: '팀 회식', date: new Date(2026, 0, 9), type: 'shared', location: '고깃집', description: '신년 맞이 팀 회식' },
    { id: 5, title: '가족 여행', date: new Date(2026, 0, 24), type: 'personal', location: '제주도', description: '3박 4일 가족 여행' },
];

const results = ref<any[]>([]);

const performSearch = () => {
    const q = query.value.toLowerCase();
    if (!q) {
        results.value = [];
        return;
    }
    results.value = mockSchedules.filter(s =>
        s.title.toLowerCase().includes(q) ||
        (s.location && s.location.toLowerCase().includes(q)) ||
        (s.description && s.description.toLowerCase().includes(q))
    );
};

watch(query, performSearch, { immediate: true });

const formatDate = (date: Date) => {
    return date.toLocaleDateString(t('calendar.locale') || 'ko-KR', { year: 'numeric', month: 'long', day: 'numeric', weekday: 'long' });
};
</script>
