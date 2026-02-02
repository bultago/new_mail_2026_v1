<script setup lang="ts">
import { ref } from 'vue'
import { useI18n } from 'vue-i18n'
import { User, Lock, Save } from 'lucide-vue-next'

const { t } = useI18n()

// Mock User Data
const profile = ref({
    name: '홍길동',
    id: 'hong',
    dept: '기획팀',
    pos: '과장',
    email: 'hong@company.com',
    phone: '010-1234-5678',
    birthday: '1980-01-01'
})

// Password Form
const pwdForm = ref({
    current: '',
    new: '',
    confirm: ''
})

</script>

<template>
    <div class="h-full flex flex-col">
        <!-- Header -->
        <div
            class="h-[50px] px-6 border-b border-gray-200 dark:border-zinc-700 flex items-center bg-white dark:bg-zinc-900">
            <h1 class="text-lg font-semibold text-gray-800 dark:text-gray-100">
                {{ t('menu_conf.group.basic') }} > {{ t('menu_conf.profile') }}
            </h1>
        </div>

        <div class="flex-1 overflow-y-auto p-6 space-y-6">

            <!-- Basic Info -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <User class="w-5 h-5 text-gray-500" />
                    기본 정보
                </h2>
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">이름</label>
                        <input type="text" v-model="profile.name" disabled
                            class="mt-1 w-full bg-gray-50 border-gray-300 rounded-md text-gray-500 cursor-not-allowed">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">아이디</label>
                        <input type="text" v-model="profile.id" disabled
                            class="mt-1 w-full bg-gray-50 border-gray-300 rounded-md text-gray-500 cursor-not-allowed">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">부서 / 직위</label>
                        <div class="mt-1 w-full bg-gray-50 border border-gray-300 rounded-md px-3 py-2 text-gray-500">
                            {{ profile.dept }} / {{ profile.pos }}
                        </div>
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">이메일</label>
                        <input type="email" v-model="profile.email"
                            class="mt-1 w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">휴대전화</label>
                        <input type="tel" v-model="profile.phone"
                            class="mt-1 w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 dark:bg-zinc-700 dark:border-zinc-600 dark:text-white">
                    </div>
                </div>
                <div class="mt-6 flex justify-end">
                    <button
                        class="px-4 py-2 bg-blue-600 text-white rounded-md hover:bg-blue-700 transition flex items-center gap-2">
                        <Save class="w-4 h-4" />
                        저장
                    </button>
                </div>
            </div>

            <!-- Password Change -->
            <div class="bg-white dark:bg-zinc-800 p-6 rounded-lg border border-gray-200 dark:border-zinc-700 shadow-sm">
                <h2 class="text-lg font-bold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                    <Lock class="w-5 h-5 text-gray-500" />
                    비밀번호 변경
                </h2>
                <div class="max-w-md space-y-4">
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">현재 비밀번호</label>
                        <input type="password" v-model="pwdForm.current"
                            class="mt-1 w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 dark:bg-zinc-700 dark:border-zinc-600">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">새 비밀번호</label>
                        <input type="password" v-model="pwdForm.new"
                            class="mt-1 w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 dark:bg-zinc-700 dark:border-zinc-600">
                    </div>
                    <div>
                        <label class="block text-sm font-medium text-gray-700 dark:text-gray-300">새 비밀번호 확인</label>
                        <input type="password" v-model="pwdForm.confirm"
                            class="mt-1 w-full border-gray-300 rounded-md shadow-sm focus:ring-blue-500 focus:border-blue-500 dark:bg-zinc-700 dark:border-zinc-600">
                    </div>
                    <div class="pt-2">
                        <button
                            class="px-4 py-2 border border-gray-300 text-gray-700 rounded-md hover:bg-gray-50 dark:border-zinc-600 dark:text-gray-300 dark:hover:bg-zinc-700">
                            비밀번호 변경
                        </button>
                    </div>
                </div>
            </div>

        </div>
    </div>
</template>
