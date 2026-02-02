<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import AppHeader from '@/components/layout/AppHeader.vue'
import { useTheme } from '@/composables/useTheme'

const { initTheme } = useTheme()
const route = useRoute()

// Determine if we should show header (hide for login/auth pages)
const showHeader = ref(true)

watch(() => route.path, (newPath) => {
  showHeader.value = !newPath.startsWith('/login') && !newPath.startsWith('/auth') && !newPath.startsWith('/popup')
}, { immediate: true })

onMounted(() => {
  initTheme()
})
</script>

<template>
  <div class="flex min-h-screen w-full flex-col bg-background font-sans">
    <!-- Fixed Header - No Transition -->
    <AppHeader v-if="showHeader" />

    <!-- Content Area with Transition -->
    <div class="flex-1 overflow-hidden">
      <router-view v-slot="{ Component }">
        <transition name="fade" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </div>
  </div>
</template>

<style>
/* Prevent layout shift when scrollbar appears/disappears */
html {
  overflow-y: scroll;
  /* Always show vertical scrollbar space */
  scrollbar-gutter: stable;
  /* Reserve space for scrollbar */
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.15s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
