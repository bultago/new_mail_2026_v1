<script setup lang="ts">
import { ref } from 'vue'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardDescription, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { Checkbox } from '@/components/ui/checkbox'
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from '@/components/ui/select'

const userid = ref('')
const password = ref('')
const domain = ref('terracetech.com') // Default domain or empty
const saveId = ref(false)
const secureLogin = ref(false)

const handleLogin = () => {
  console.log('Login attempt', { userid: userid.value, password: password.value, domain: domain.value, saveId: saveId.value, secureLogin: secureLogin.value })
  // Mock login success
  import('../router').then(({ default: router }) => {
     router.push('/mail/list/inbox')
  })
}
</script>

<template>
  <div class="flex items-center justify-center min-h-screen bg-gray-100 dark:bg-zinc-900">
    <Card class="w-full max-w-md">
      <CardHeader>
        <CardTitle class="text-2xl font-bold text-center">WebMail Login</CardTitle>
        <CardDescription class="text-center">
          Enter your credentials to access your mail.
        </CardDescription>
      </CardHeader>
      <CardContent class="grid gap-4">
        <div class="grid gap-2">
          <Label for="userid">User ID</Label>
          <Input id="userid" v-model="userid" placeholder="Enter your ID" />
        </div>
        <div class="grid gap-2">
          <Label for="password">Password</Label>
          <Input id="password" type="password" v-model="password" placeholder="Enter your password" />
        </div>
        <div class="grid gap-2">
           <Label for="domain">Domain</Label>
           <Select v-model="domain">
              <SelectTrigger>
                <SelectValue placeholder="Select a domain" />
              </SelectTrigger>
              <SelectContent>
                <SelectItem value="terracetech.com">terracetech.com</SelectItem>
                <SelectItem value="example.com">example.com</SelectItem>
              </SelectContent>
           </Select>
        </div>
        <div class="flex items-center space-x-2">
          <Checkbox id="saveId" v-model:checked="saveId" />
          <Label for="saveId" class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
            Save ID
          </Label>
        </div>
         <div class="flex items-center space-x-2">
          <Checkbox id="secureLogin" v-model:checked="secureLogin" />
          <Label for="secureLogin" class="text-sm font-medium leading-none peer-disabled:cursor-not-allowed peer-disabled:opacity-70">
            Secure Login
          </Label>
        </div>
      </CardContent>
      <CardFooter>
        <Button class="w-full bg-legacy-blue hover:bg-legacy-blue-hover text-white shadow-none rounded-sm" @click="handleLogin">
          Sign in
        </Button>
      </CardFooter>
    </Card>
  </div>
</template>
