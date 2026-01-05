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
import { Avatar, AvatarFallback, AvatarImage } from '@/components/ui/avatar'
import { useRouter } from 'vue-router'

const router = useRouter()

defineEmits(['toggle-sidebar'])

const handleLogout = () => {
  // TODO: Api Logout
  router.push('/login')
}
</script>

<template>
  <header class="flex flex-col md:flex-row h-auto md:h-[60px] items-center px-4 bg-gradient-to-r from-legacy-blue-dark to-[#005fb0] shadow-sm justify-between gap-2 py-2 md:py-0">
    <!-- Logo & Title -->
    <div class="flex items-center w-full md:w-auto justify-between">
        <div class="flex items-center">
            <div class="w-8 h-8 rounded bg-white/20 flex items-center justify-center mr-2">
                <!-- Simple Mail Icon -->
                 <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 text-white" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
            </div>
            <h1 class="font-bold text-[18px] text-white tracking-tight">Terrace Mail</h1>
        </div>
        
        <!-- Mobile Menu Toggle (Visible only on small screens) -->
        <button 
            class="md:hidden text-white p-1 hover:bg-white/10 rounded"
            @click="$emit('toggle-sidebar')"
        >
            <svg xmlns="http://www.w3.org/2000/svg" class="w-6 h-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
            </svg>
        </button>
    </div>

    <!-- Top Menu (Icon Based) -->
    <nav class="flex flex-wrap gap-1 justify-center md:justify-center flex-1 w-full md:w-auto px-4">
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] text-white/80 hover:text-white hover:bg-white/10 rounded-md transition-all group">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="20" height="16" x="2" y="4" rx="2"/><path d="m22 7-8.97 5.7a1.94 1.94 0 0 1-2.06 0L2 7"/></svg>
            <span class="text-[11px] font-bold">메일</span>
        </a>
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] text-white/80 hover:text-white hover:bg-white/10 rounded-md transition-all group">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/><circle cx="9" cy="7" r="4"/><path d="M23 21v-2a4 4 0 0 0-3-3.87"/><path d="M16 3.13a4 4 0 0 1 0 7.75"/></svg>
            <span class="text-[11px]">주소록</span>
        </a>
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] text-white/80 hover:text-white hover:bg-white/10 rounded-md transition-all group">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><rect width="18" height="18" x="3" y="4" rx="2" ry="2"/><line x1="16" x2="16" y1="2" y2="6"/><line x1="8" x2="8" y1="2" y2="6"/><line x1="3" x2="21" y1="10" y2="10"/></svg>
            <span class="text-[11px]">일정관리</span>
        </a>
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] text-white/80 hover:text-white hover:bg-white/10 rounded-md transition-all group">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M3 3h18v18H3zM12 8v8M8 12h8"/></svg>
            <span class="text-[11px]">게시판</span>
        </a>
         <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] text-white/80 hover:text-white hover:bg-white/10 rounded-md transition-all group">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M14.5 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V7.5L14.5 2z"/><polyline points="14 2 14 8 20 8"/></svg>
            <span class="text-[11px]">자료실</span>
        </a>
        <a href="#" class="flex flex-col items-center justify-center w-[60px] h-[50px] text-white/80 hover:text-white hover:bg-white/10 rounded-md transition-all group">
            <svg xmlns="http://www.w3.org/2000/svg" class="w-5 h-5 mb-1 group-hover:scale-110 transition-transform" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M12.22 2h-.44a2 2 0 0 0-2 2v.18a2 2 0 0 1-1 1.73l-.43.25a2 2 0 0 1-2 0l-.15-.08a2 2 0 0 0-2.73.73l-.22.38a2 2 0 0 0 .73 2.73l.15.1a2 2 0 0 1 1 1.72v.51a2 2 0 0 1-1 1.74l-.15.09a2 2 0 0 0-.73 2.73l.22.38a2 2 0 0 0 2.73.73l.15-.08a2 2 0 0 1 2 0l.43.25a2 2 0 0 1 1 1.73V20a2 2 0 0 0 2 2h.44a2 2 0 0 0 2-2v-.18a2 2 0 0 1 1-1.73l.43-.25a2 2 0 0 1 2 0l.15.08a2 2 0 0 0 2.73-.73l.22-.38a2 2 0 0 0-.73-2.73l-.15-.1a2 2 0 0 1-1-1.72v-.51a2 2 0 0 1 1-1.74l.15-.09a2 2 0 0 0 .73-2.73l-.22-.38a2 2 0 0 0-2.73-.73l-.15.08a2 2 0 0 1-2 0l-.43-.25a2 2 0 0 1-1-1.73V4a2 2 0 0 0-2-2z"/><circle cx="12" cy="12" r="3"/></svg>
            <span class="text-[11px]">환경설정</span>
        </a>
    </nav>

    <!-- User Info & Logout -->
    <div class="flex items-center gap-2 mt-2 md:mt-0 w-full md:w-auto justify-end">
       <div class="flex items-center text-white text-[12px]">
          <span class="font-bold hidden sm:inline">mail administrator</span>
          <span class="text-white/70 ml-1 truncate max-w-[150px]">(mailadm@sogang.ac.kr)</span>
       </div>
       <div class="h-3 w-[1px] bg-white/30 mx-2"></div>
       <button @click="handleLogout" class="px-2 py-0.5 bg-white/10 hover:bg-white/20 border border-white/30 rounded text-[11px] text-white transition-colors">
          로그아웃
       </button>
    </div>
  </header>
</template>
