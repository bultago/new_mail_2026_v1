<script setup lang="ts">
import { Loader2 } from 'lucide-vue-next'

defineProps({
    text: {
        type: String,
        default: 'Loading...'
    }
})
</script>

<template>
    <div class="absolute inset-0 z-50 flex flex-col bg-[#F0F0F2] dark:bg-zinc-900 pointer-events-none">
        <!-- 1. Linear Progress (Top) -->
        <div class="w-full h-1 bg-gray-200 dark:bg-zinc-700 overflow-hidden">
            <div class="h-full bg-blue-600 animate-progress"></div>
        </div>

        <div class="flex-1 flex items-center justify-center relative p-4">
            <!-- 2. Skeleton Background (Optional Slot) -->
            <div class="opacity-60 w-full flex justify-center">
                <slot name="skeleton"></slot>
            </div>

            <!-- 3. Glassmorphism Spinner (Center Overlay) -->
            <div class="absolute inset-0 flex items-center justify-center backdrop-blur-[2px]">
                <div
                    class="bg-white/90 dark:bg-zinc-800/90 p-6 rounded-2xl shadow-2xl flex flex-col items-center gap-4 border border-white/20 ring-1 ring-black/5">
                    <Loader2 class="w-10 h-10 animate-spin text-blue-600" />
                    <span class="text-sm font-bold text-gray-700 dark:text-gray-200 tracking-wide font-sans">{{ text
                    }}</span>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>
/* Linear Progress Animation */
@keyframes progress {
    0% {
        width: 0%;
        margin-left: 0%;
    }

    50% {
        width: 70%;
        margin-left: 0%;
    }

    100% {
        width: 100%;
        margin-left: 100%;
    }
}

.animate-progress {
    animation: progress 1.5s infinite ease-in-out;
}
</style>
