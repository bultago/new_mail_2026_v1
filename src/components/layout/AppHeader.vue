<script setup lang="ts">
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuLabel,
  DropdownMenuSeparator,
  DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'
import { Button } from '@/components/ui/button'
import { useRouter } from 'vue-router'
import { useTheme } from '@/composables/useTheme'
import { Palette, Moon, Sun, Monitor, Menu, Mail, BookUser, Calendar, ClipboardList, HardDrive, Settings } from 'lucide-vue-next'

const { setTheme, currentTheme } = useTheme()
const router = useRouter()

defineEmits(['toggle-sidebar'])

const handleLogout = () => {
  // TODO: Api Logout
  router.push('/login')
}
</script>

<template>
  <header class="flex flex-col md:flex-row h-auto md:h-[60px] items-center px-4 bg-gradient-to-r from-legacy-blue-dark to-legacy-blue shadow-sm justify-between gap-2 py-2 md:py-0 relative z-20">
    <!-- Logo & Title -->
    <div class="flex items-center w-full md:w-auto justify-between md:justify-start">
        <div class="flex items-center">
            <button 
                class="md:hidden text-legacy-text-header mr-3 hover:bg-white/10 p-1 rounded"
                @click="$emit('toggle-sidebar')"
            >
                <Menu class="w-6 h-6" />
            </button>
            
            <div class="flex items-center gap-2">
                <div class="w-7 h-7 rounded bg-white/20 flex items-center justify-center">
                    <svg xmlns="http://www.w3.org/2000/svg" class="w-4 h-4 text-legacy-text-header" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
                </div>
                <!-- Title hidden on very small screens if needed, but keeping for now -->
                <h1 class="font-bold text-[18px] text-legacy-text-header tracking-tight">Terrace Mail</h1>
            </div>
        </div>
    </div>

    <!-- Centered Icon Menu (Restored & Refined) -->
    <!-- "상단메뉴 가운데 정렬, 아이콘으로 표시" -->
    <nav class="flex flex-1 justify-center items-center gap-1 w-full md:w-auto overflow-x-auto">
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] rounded-md transition-all group hover:bg-white/10" :class="currentTheme === 'modern' ? 'text-slate-700 hover:text-slate-900 hover:bg-slate-100' : 'text-white/90 hover:text-white'">
            <Mail class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" />
            <span class="text-[11px] font-bold">메일</span>
        </a>
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] rounded-md transition-all group hover:bg-white/10" :class="currentTheme === 'modern' ? 'text-slate-700 hover:text-slate-900 hover:bg-slate-100' : 'text-white/80 hover:text-white'">
            <BookUser class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" />
            <span class="text-[11px] font-medium">주소록</span>
        </a>
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] rounded-md transition-all group hover:bg-white/10" :class="currentTheme === 'modern' ? 'text-slate-700 hover:text-slate-900 hover:bg-slate-100' : 'text-white/80 hover:text-white'">
            <Calendar class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" />
            <span class="text-[11px] font-medium">일정관리</span>
        </a>
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] rounded-md transition-all group hover:bg-white/10" :class="currentTheme === 'modern' ? 'text-slate-700 hover:text-slate-900 hover:bg-slate-100' : 'text-white/80 hover:text-white'">
            <ClipboardList class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" />
            <span class="text-[11px] font-medium">게시판</span>
        </a>
         <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] rounded-md transition-all group hover:bg-white/10" :class="currentTheme === 'modern' ? 'text-slate-700 hover:text-slate-900 hover:bg-slate-100' : 'text-white/80 hover:text-white'">
            <HardDrive class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" />
            <span class="text-[11px] font-medium">자료실</span>
        </a>
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] rounded-md transition-all group hover:bg-white/10" :class="currentTheme === 'modern' ? 'text-slate-700 hover:text-slate-900 hover:bg-slate-100' : 'text-white/80 hover:text-white'">
            <Settings class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" />
            <span class="text-[11px] font-medium">환경설정</span>
        </a>
    </nav>

    <!-- User Info & Theme -->
    <div class="flex items-center gap-2 w-full md:w-auto justify-end">
       <!-- Theme Toggle -->
       <DropdownMenu>
            <DropdownMenuTrigger as-child>
                <button class="w-8 h-8 flex items-center justify-center rounded hover:bg-white/10 text-legacy-text-header transition-colors">
                    <Palette class="w-4 h-4" />
                </button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" class="w-40 z-50">
                <DropdownMenuLabel>테마 설정</DropdownMenuLabel>
                <DropdownMenuSeparator />
                <DropdownMenuItem @click="setTheme('default')" class="cursor-pointer">
                    <Monitor class="mr-2 h-4 w-4" /> 기본 (Blue)
                </DropdownMenuItem>
                <DropdownMenuItem @click="setTheme('modern')" class="cursor-pointer">
                    <Sun class="mr-2 h-4 w-4" /> 모던 (Light)
                </DropdownMenuItem>
                <DropdownMenuItem @click="setTheme('dark')" class="cursor-pointer">
                    <Moon class="mr-2 h-4 w-4" /> 다크 모드
                </DropdownMenuItem>
            </DropdownMenuContent>
       </DropdownMenu>

       <div class="h-3 w-[1px] bg-white/30 mx-2 hidden sm:block"></div>

       <div class="flex items-center text-legacy-text-header text-[12px] hidden sm:flex">
          <span class="font-bold">mail administrator</span>
          <span class="text-legacy-text-header/70 ml-1 truncate max-w-[150px]">(mailadm@sogang.ac.kr)</span>
       </div>
       
       <button @click="handleLogout" class="ml-2 px-2 py-0.5 bg-white/10 hover:bg-white/20 border border-white/30 rounded text-[11px] text-legacy-text-header transition-colors">
          로그아웃
       </button>
    </div>
  </header>
</template>
