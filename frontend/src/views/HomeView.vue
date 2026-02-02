<script setup lang="ts">
import { useAuthStore } from '@/stores/auth';

const authStore = useAuthStore();
</script>

<template>
  <div class="p-8">
    <h1 class="text-2xl font-bold">Welcome, {{ authStore.user?.name || 'User' }}</h1>
    <p class="mt-4">This is the dashboard for the Hybrid IMAP Web Client.</p>
    
    <div class="mt-4 p-4 bg-gray-100 rounded">
      <h2 class="font-semibold">Debug Info:</h2>
      <pre class="mt-2 text-sm">{{ authStore.user }}</pre>
    </div>

    <button 
      @click="authStore.logout()" 
      class="mt-4 px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
    >
      Logout
    </button>
  </div>
</template>
