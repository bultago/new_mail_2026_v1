<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from '@/components/ui/table'
import { Badge } from '@/components/ui/badge'
import { mockMessages } from '@/mocks/mailData'
import { 
  Paperclip, Star, Trash2, Reply, ReplyAll, Forward, 
  FolderInput, Copy, Printer, ShieldAlert, Ban, Search,
  ChevronDown, FileText, AlertCircle, RefreshCw
} from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()
const currentFolder = computed(() => route.params.folder || 'inbox')

const folderName = computed(() => {
  const f = currentFolder.value
  if (f === 'inbox') return '받은메일함'
  if (f === 'sent') return '보낸메일함'
  if (f === 'trash') return '휴지통'
  return f
})

const filteredMessages = computed(() => {
  return mockMessages.filter(msg => {
    if (currentFolder.value === 'inbox') return msg.folderId === 'inbox'
    return msg.folderId === currentFolder.value
  })
})

const activeTab = ref<'basic' | 'advanced'>('basic')

const formatDate = (dateString: string) => {
  return new Date(dateString).toLocaleDateString()
}

const handleRowClick = (id: string) => {
    router.push(`/mail/read/${id}`)
}
</script>

<template>
  <div class="h-full flex flex-col bg-legacy-bg">
    <!-- Header Area with Integrated Search -->
    <div class="flex justify-between items-center px-4 py-3 border-b border-legacy-border bg-white dark:bg-zinc-900 shadow-sm h-[50px]">
         <div class="flex items-center gap-2">
             <h2 class="text-[16px] text-legacy-text tracking-tight leading-none dark:text-white">
               {{ folderName }}
             </h2>
             <span class="text-[12px] text-legacy-text-muted">전체 <span class="text-legacy-blue dark:text-blue-400">{{ filteredMessages.length }}</span> / 안읽음 <span class="text-red-500">1</span></span>
         </div>

         <!-- Compact Search Bar -->
         <div class="flex gap-1 items-center">
             <select class="h-[26px] text-[12px] border border-legacy-border px-1 bg-white text-legacy-text rounded-sm dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200">
                 <option>제목</option>
                 <option>보낸사람</option>
             </select>
             <div class="relative">
                <input type="text" class="h-[26px] w-[200px] border border-legacy-border pl-2 pr-7 text-[12px] focus:outline-none focus:border-legacy-blue bg-white text-legacy-text rounded-sm dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200" placeholder="메일 검색">
                <Search class="absolute right-1 top-1.5 h-4 w-4 text-legacy-text-muted cursor-pointer hover:text-legacy-blue" />
             </div>
             <button class="h-[26px] bg-white hover:bg-gray-50 border border-legacy-border text-legacy-text text-[12px] px-2 rounded-sm flex items-center gap-1 ml-1 dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                 상세 <ChevronDown class="h-3 w-3" />
             </button>
         </div>
    </div>

    <!-- Basic / Advanced Tabs (Restored) -->
    <div class="flex px-4 pt-4 pb-0 bg-legacy-bg gap-1 border-b border-legacy-border">
        <button 
            @click="activeTab = 'basic'"
            class="px-4 py-1.5 text-[12px] font-bold rounded-t-md border-t border-l border-r border-legacy-border focus:outline-none transition-all relative top-[1px]"
            :class="activeTab === 'basic' ? 'bg-legacy-bg-toolbar text-[#005fb0] dark:text-blue-400 border-b-transparent z-10' : 'bg-gray-100 text-legacy-text hover:bg-gray-200 dark:bg-zinc-800 dark:text-gray-400 dark:border-zinc-700'"
        >
            기본기능
        </button>
        <button 
            @click="activeTab = 'advanced'"
            class="px-4 py-1.5 text-[12px] font-bold rounded-t-md border-t border-l border-r border-legacy-border focus:outline-none transition-all relative top-[1px]"
            :class="activeTab === 'advanced' ? 'bg-legacy-bg-toolbar text-[#005fb0] dark:text-blue-400 border-b-transparent z-10' : 'bg-gray-100 text-legacy-text hover:bg-gray-200 dark:bg-zinc-800 dark:text-gray-400 dark:border-zinc-700'"
        >
            부가기능
        </button>
    </div>

    <!-- Toolbar (Context Aware) -->
    <div class="bg-legacy-bg-toolbar border-b border-legacy-border flex items-center px-4 h-[40px] gap-2 shadow-sm">
        
        <!-- Basic Actions -->
        <template v-if="activeTab === 'basic'">
             <div class="flex items-center gap-1">
               <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <Trash2 class="h-3.5 w-3.5 text-legacy-text-muted group-hover:text-red-600 dark:text-gray-400" />
                   <span class="text-[12px]">삭제</span>
               </button>
               <div class="h-4 w-[1px] bg-legacy-border mx-2 dark:bg-zinc-700"></div>
               <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <Reply class="h-3.5 w-3.5 text-legacy-text-muted group-hover:text-blue-600 dark:text-gray-400" />
                   <span class="text-[12px]">답장</span>
               </button>
               <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <ReplyAll class="h-3.5 w-3.5 text-legacy-text-muted group-hover:text-blue-600 dark:text-gray-400" />
                   <span class="text-[12px]">전체답장</span>
               </button>
               <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <Forward class="h-3.5 w-3.5 text-legacy-text-muted group-hover:text-blue-600 dark:text-gray-400" />
                   <span class="text-[12px]">전달</span>
               </button>
            </div>
        </template>

        <!-- Advanced Actions -->
        <template v-else>
             <div class="flex items-center gap-1">
               <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <FolderInput class="h-3.5 w-3.5 text-legacy-text-muted group-hover:text-orange-600 dark:text-gray-400" />
                   <span class="text-[12px]">이동</span>
               </button>
               <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <Copy class="h-3.5 w-3.5 text-legacy-text-muted dark:text-gray-400" />
                   <span class="text-[12px]">복사</span>
               </button>
               <div class="h-4 w-[1px] bg-legacy-border mx-2 dark:bg-zinc-700"></div>
               <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <ShieldAlert class="h-3.5 w-3.5 text-legacy-text-muted group-hover:text-green-600 dark:text-gray-400" />
                   <span class="text-[12px]">스팸신고</span>
               </button>
               <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <Printer class="h-3.5 w-3.5 text-legacy-text-muted dark:text-gray-400" />
                   <span class="text-[12px]">인쇄</span>
               </button>
                <button class="flex items-center gap-1 px-3 h-[28px] bg-white border border-gray-300 hover:bg-legacy-bg-hover hover:border-legacy-blue text-legacy-text rounded-sm transition-colors group shadow-[0_1px_2px_rgba(0,0,0,0.05)] dark:bg-zinc-800 dark:border-zinc-600 dark:text-gray-200 dark:hover:bg-zinc-700">
                   <FileText class="h-3.5 w-3.5 text-legacy-text-muted dark:text-gray-400" />
                   <span class="text-[12px]">PC저장</span>
               </button>
            </div>
        </template>

        <div class="flex-1"></div>

        <!-- Refresh -->
        <button class="p-1 hover:bg-white rounded-sm text-legacy-text-muted hover:text-legacy-blue transition-colors" title="새로고침">
            <RefreshCw class="h-4 w-4" />
        </button>
    </div>

    <!-- Dense Mail List Table (Excel Style) -->
    <div class="flex-1 overflow-auto bg-white dark:bg-zinc-900 p-4 pt-2">
      <Table class="w-full border-collapse border border-legacy-border/50 dark:border-zinc-700">
        <!-- Header -->
        <TableHeader class="bg-legacy-bg-table_header_start sticky top-0 z-10 shadow-sm ring-1 ring-legacy-border/50 dark:ring-zinc-700 dark:bg-zinc-800">
          <TableRow class="h-[28px] border-b border-legacy-border divide-x divide-legacy-border/50 dark:border-zinc-700 dark:divide-zinc-700">
            <TableHead class="w-[30px] p-0 text-center bg-transparent"><input type="checkbox" /></TableHead>
            <TableHead class="w-[30px] p-0 text-center text-legacy-text font-bold text-[11px] bg-transparent">!</TableHead>
            <TableHead class="w-[30px] p-0 text-center text-legacy-text font-bold text-[11px] bg-transparent"><Star class="h-3.5 w-3.5 mx-auto text-legacy-text-muted"/></TableHead>
            <TableHead class="w-[30px] p-0 text-center text-legacy-text font-bold text-[11px] bg-transparent">
                <!-- Header Icon (Mail) -->
                <svg class="h-3.5 w-3.5 mx-auto text-legacy-text-muted" viewBox="0 0 24 24" fill="currentColor" stroke="none"><path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 8L12 13L4 8V6L12 11L20 6V8Z"/></svg>
            </TableHead>
            <TableHead class="w-[30px] p-0 text-center text-legacy-text font-bold text-[11px] bg-transparent"><Paperclip class="h-3.5 w-3.5 mx-auto text-legacy-text-muted"/></TableHead>
            <TableHead class="w-[120px] px-2 text-left text-legacy-text font-bold text-[12px] bg-transparent border-r border-legacy-border/50 dark:border-zinc-700">보낸사람</TableHead>
            <TableHead class="px-2 text-left text-legacy-text font-bold text-[12px] bg-transparent border-r border-legacy-border/50 dark:border-zinc-700">제목</TableHead>
            <TableHead class="w-[100px] text-center text-legacy-text font-bold text-[12px] bg-transparent">날짜</TableHead>
            <TableHead class="w-[60px] text-center text-legacy-text font-bold text-[12px] bg-transparent">크기</TableHead>
          </TableRow>
        </TableHeader>
        
        <!-- Body -->
        <TableBody class="bg-white dark:bg-zinc-900">
          <template v-if="filteredMessages.length > 0">
             <TableRow 
                v-for="message in filteredMessages" 
                :key="message.id" 
                @click="handleRowClick(message.id)" 
                class="group h-[26px] border-b border-legacy-border hover:bg-legacy-bg-row_hover cursor-pointer transition-none dark:border-zinc-700 dark:hover:bg-zinc-800"
             >
                <TableCell class="p-0 text-center"><input type="checkbox" /></TableCell>
                <TableCell class="p-0 text-center">
                   <AlertCircle v-if="message.isUrgent" class="h-3.5 w-3.5 text-red-600 mx-auto" />
                </TableCell>
                <TableCell class="p-0 text-center">
                   <Star v-if="message.flagged" class="h-3.5 w-3.5 fill-red-500 text-red-500 mx-auto" />
                   <Star v-else class="h-3.5 w-3.5 text-gray-200 mx-auto" />
                </TableCell>
                <TableCell class="p-0 text-center">
                   <!-- Closed Envelope (Unread): Yellow -->
                   <svg v-if="!message.read" class="h-3.5 w-3.5 mx-auto text-[#FFC107]" viewBox="0 0 24 24" fill="currentColor" stroke="none"><path d="M20 4H4C2.9 4 2.01 4.9 2.01 6L2 18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4ZM20 8L12 13L4 8V6L12 11L20 6V8Z"/></svg>
                   <!-- Open Envelope (Read): White/Gray -->
                   <svg v-else class="h-3.5 w-3.5 mx-auto text-gray-400" viewBox="0 0 24 24" fill="currentColor" stroke="none"><path d="M20 8V6L12 11L4 6V8L12 13L20 8ZM20 4H4C2.9 4 2 4.9 2 6V18C2 19.1 2.9 20 4 20H20C21.1 20 22 19.1 22 18V6C22 4.9 21.1 4 20 4Z"/></svg>
                </TableCell>
                <TableCell class="p-0 text-center">
                   <Paperclip v-if="message.hasAttachment" class="h-3.5 w-3.5 text-legacy-text-muted mx-auto"/>
                </TableCell>
                <TableCell class="px-2 py-0 text-[12px] text-legacy-text-body truncate border-r border-legacy-border/30">
                   <span :class="{'font-bold text-black dark:text-white': !message.read}">{{ message.from.name }}</span>
                </TableCell>
                <TableCell class="px-2 py-0 text-[12px] text-legacy-text-body truncate border-r border-legacy-border/30">
                   <div class="flex items-center gap-1">
                     <span :class="{'font-bold text-black dark:text-white': !message.read, 'text-legacy-blue dark:text-blue-400': !message.read}">{{ message.subject }}</span>
                     <Badge v-for="tag in message.tags" :key="tag" variant="outline" class="text-[10px] px-1 py-0 h-3.5 border-gray-300 text-gray-500 font-normal rounded-sm">{{ tag }}</Badge>
                   </div>
                </TableCell>
                <TableCell class="text-center py-0 text-[11px] text-legacy-text-body font-tahoma border-r border-legacy-border/30">
                    {{ formatDate(message.date) }}
                </TableCell>
                 <TableCell class="text-center py-0 text-[11px] text-legacy-text-muted font-tahoma">
                    24KB
                </TableCell>
             </TableRow>
          </template>
        </TableBody>
      </Table>
    </div>
    
    <!-- Excel Style Footer -->
     <div class="h-[34px] border-t border-legacy-border flex items-center bg-legacy-content-bg px-4 justify-between">
        <div class="text-[12px] text-legacy-text-muted">
            <span class="mr-2">선택: 0개</span>
            <span>전체: {{ filteredMessages.length }}개</span>
        </div>
        <div class="flex gap-1 text-[11px] font-tahoma">
             <button class="px-1.5 py-0.5 hover:bg-white border border-transparent hover:border-gray-300 rounded-sm disabled:opacity-50" disabled>&lt;</button>
             <button class="px-2.5 py-0.5 font-bold bg-white border border-gray-300 rounded-sm shadow-sm">1</button>
             <button class="px-2.5 py-0.5 hover:bg-white border border-transparent hover:border-gray-300 rounded-sm">2</button>
             <button class="px-2.5 py-0.5 hover:bg-white border border-transparent hover:border-gray-300 rounded-sm">3</button>
             <button class="px-1.5 py-0.5 hover:bg-white border border-transparent hover:border-gray-300 rounded-sm">&gt;</button>
        </div>
     </div>
  </div>
</template>

<style scoped>
.font-tahoma {
    font-family: Tahoma, sans-serif;
}
/* Ensure table borders are crisp */
:deep(table) {
    border-collapse: collapse;
}
:deep(th), :deep(td) {
    vertical-align: middle;
}
</style>
