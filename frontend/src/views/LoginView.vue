<script setup lang="ts">
import { ref } from 'vue';
import { useAuthStore } from '@/stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();

const id = ref('');
const password = ref('');
const domain = ref('terracetech.com'); // Default Domain

const handleLogin = async () => {
  if (!id.value || !password.value) return;

  const success = await authStore.login(id.value, password.value, domain.value);
  if (success) {
    router.push('/');
  }
};
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100 dark:bg-gray-900">
    <div class="w-full max-w-md p-8 space-y-6 bg-white rounded-lg shadow-md dark:bg-gray-800">
      <div class="text-center">
        <h1 class="text-3xl font-bold text-gray-900 dark:text-white">TMail 2026</h1>
        <p class="mt-2 text-gray-600 dark:text-gray-400">Sign in to your account</p>
      </div>

      <form @submit.prevent="handleLogin" class="space-y-4">
        <div>
          <label for="domain" class="block text-sm font-medium text-gray-700 dark:text-gray-300">Domain</label>
          <input v-model="domain" id="domain" type="text"
            class="w-full px-3 py-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="terracetech.com" />
        </div>

        <div>
          <label for="id" class="block text-sm font-medium text-gray-700 dark:text-gray-300">User ID</label>
          <input v-model="id" id="id" type="text" required
            class="w-full px-3 py-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="Enter your ID" />
        </div>

        <div>
          <label for="password" class="block text-sm font-medium text-gray-700 dark:text-gray-300">Password</label>
          <input v-model="password" id="password" type="password" required
            class="w-full px-3 py-2 mt-1 border rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:text-white"
            placeholder="Enter your password" />
        </div>

        <div v-if="authStore.getError"
          class="p-3 text-sm text-red-600 bg-red-100 rounded-md dark:bg-red-900 dark:text-red-200">
          {{ authStore.getError }}
        </div>

        <button type="submit" :disabled="authStore.loading"
          class="w-full px-4 py-2 font-bold text-white bg-blue-600 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:opacity-50">
          <span v-if="authStore.loading">Signing in...</span>
          <span v-else>Sign In</span>
        </button>
      </form>
    </div>
  </div>
</template>
