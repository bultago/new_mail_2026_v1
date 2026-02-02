<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import AppSidebar from '@/components/layout/AppSidebar.vue'

const sidebarWidth = ref(230)
const isResizing = ref(false)
const isMobile = ref(false)
const isSidebarOpen = ref(false)

const startResize = () => {
    isResizing.value = true
    document.addEventListener('mousemove', handleResize)
    document.addEventListener('mouseup', stopResize)
    document.body.style.cursor = 'col-resize'
    document.body.style.userSelect = 'none'
}

const handleResize = (e: MouseEvent) => {
    if (isResizing.value) {
        const newWidth = Math.max(180, Math.min(e.clientX, 400))
        sidebarWidth.value = newWidth
    }
}

const stopResize = () => {
    isResizing.value = false
    document.removeEventListener('mousemove', handleResize)
    document.removeEventListener('mouseup', stopResize)
    document.body.style.cursor = ''
    document.body.style.userSelect = ''
}

const toggleSidebar = () => {
    isSidebarOpen.value = !isSidebarOpen.value
}

const checkMobile = () => {
    isMobile.value = window.innerWidth < 768
    if (!isMobile.value) {
        isSidebarOpen.value = true
    } else {
        isSidebarOpen.value = false
    }
}

onMounted(() => {
    checkMobile()
    window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
    window.removeEventListener('resize', checkMobile)
})
</script>

<template>
    <div class="flex flex-1 overflow-hidden relative">
        <!-- Mobile Sidebar Overlay -->
        <div v-if="isMobile && isSidebarOpen" class="absolute inset-0 bg-black/50 z-40 md:hidden"
            @click="isSidebarOpen = false"></div>

        <!-- Sidebar -->
        <AppSidebar
            v-if="!$route.path.startsWith('/addr') && !$route.path.startsWith('/schedule') && !$route.path.startsWith('/settings')"
            class="z-[100] transition-transform duration-300 ease-in-out md:translate-x-0 absolute md:relative h-full"
            :class="[
                isMobile && !isSidebarOpen ? '-translate-x-full' : 'translate-x-0'
            ]" :width="sidebarWidth" />

        <!-- Resizer Handle (Desktop Only) -->
        <div class="hidden md:flex w-[5px] cursor-col-resize hover:bg-blue-200 active:bg-blue-400 z-30 justify-center bg-gray-50"
            @mousedown="startResize">
            <div class="h-full w-[1px] border-l border-dotted border-gray-400"></div>
        </div>

        <!-- Content -->
        <div class="flex flex-col flex-1 min-w-0 transition-all duration-300">
            <main class="flex-1 overflow-auto text-foreground bg-[#F7F7F7] dark:bg-zinc-900">
                <router-view />
            </main>
        </div>
    </div>
</template>
