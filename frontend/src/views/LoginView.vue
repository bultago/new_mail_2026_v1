<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Checkbox } from '@/components/ui/checkbox'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'

import LoadingOverlay from '@/components/common/LoadingOverlay.vue'

const { t } = useI18n()
const userid = ref('')
const password = ref('')
const domain = ref('terracetech.com') // Default domain or empty
const saveId = ref(false)
const secureLogin = ref(false)
const isLoading = ref(false)

const handleLogin = () => {
  console.log('Login attempt', { userid: userid.value, password: password.value, domain: domain.value, saveId: saveId.value, secureLogin: secureLogin.value })

  isLoading.value = true

  // Mock login success with delay
  setTimeout(() => {
    isLoading.value = false
    import('../router').then(({ default: router }) => {
      router.push('/mail/list/inbox')
    })
  }, 2000)
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100 dark:bg-zinc-900 relative overflow-hidden">
    <!-- Loading Overlay -->
    <LoadingOverlay v-if="isLoading" text="Logging in...">
      <template #skeleton>
        <div class="w-full h-full max-w-5xl flex gap-4 p-4 opacity-50">
          <!-- Sidebar Skeleton -->
          <div class="w-64 h-[600px] bg-white dark:bg-zinc-800 rounded-lg shadow-sm animate-pulse hidden md:block">
          </div>
          <!-- Main Content Skeleton -->
          <div class="flex-1 flex flex-col gap-4">
            <!-- Toolbar -->
            <div class="w-full h-12 bg-white dark:bg-zinc-800 rounded shadow-sm animate-pulse"></div>
            <!-- Email List -->
            <div class="flex-1 bg-white dark:bg-zinc-800 rounded shadow-sm p-4 space-y-4 animate-pulse">
              <div class="h-8 bg-gray-200 dark:bg-zinc-700 rounded w-full"></div>
              <div class="h-8 bg-gray-200 dark:bg-zinc-700/80 rounded w-full"></div>
              <div class="h-8 bg-gray-200 dark:bg-zinc-700/60 rounded w-full"></div>
              <div class="h-8 bg-gray-200 dark:bg-zinc-700/40 rounded w-full"></div>
              <div class="h-8 bg-gray-200 dark:bg-zinc-700/20 rounded w-full"></div>
            </div>
          </div>
        </div>
      </template>
    </LoadingOverlay>

    <Card class="w-full max-w-md z-10">
      <CardHeader>
        <CardTitle class="text-2xl font-bold text-center">{{ t('login.title') }}</CardTitle>
        <CardDescription class="text-center">
          {{ t('login.desc') }}
        </CardDescription>
      </CardHeader>
      <CardContent class="grid gap-4">
        <div class="grid gap-2">
          <Label for="userid">{{ t('login.user_id') }}</Label>
          <Input id="userid" v-model="userid" :placeholder="t('login.placeholder_id')" />
        </div>
        <div class="grid gap-2">
          <Label for="password">{{ t('login.password') }}</Label>
          <Input id="password" type="password" v-model="password" :placeholder="t('login.placeholder_pw')" />
        </div>
        <div class="grid gap-2">
          <Label for="domain">{{ t('login.domain') }}</Label>
          <Select v-model="domain">
            <SelectTrigger>
              <SelectValue :placeholder="t('login.select_domain')" />
            </SelectTrigger>
            <SelectContent>
              <SelectItem value="terracetech.com">terracetech.com</SelectItem>
              <SelectItem value="example.com">example.com</SelectItem>
            </SelectContent>
          </Select>
        </div>
        <div class="flex items-center space-x-2">
          <Checkbox id="saveId" v-model:checked="saveId" />
          <Label for="saveId"
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
            {{ t('login.save_id') }}
          </Label>
        </div>
        <div class="flex items-center space-x-2">
          <Checkbox id="secureLogin" v-model:checked="secureLogin" />
          <Label for="secureLogin"
            class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
            {{ t('login.secure_login') }}
          </Label>
        </div>
      </CardContent>
      <CardFooter>
        <Button class="w-full bg-legacy-blue hover:bg-legacy-blue-hover text-white shadow-none rounded-sm"
          @click="handleLogin">
          {{ t('login.sign_in') }}
        </Button>
      </CardFooter>
    </Card>
  </div>
</template>
