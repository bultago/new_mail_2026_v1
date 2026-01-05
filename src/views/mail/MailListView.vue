<script setup lang="ts">
import { computed, ref } from 'vue' // Import ref
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
import { Button } from '@/components/ui/button'
import { mockMessages } from '@/mocks/mailData'
import { 
  Paperclip, Star, Trash2, Reply, ReplyAll, Forward, 
  FolderInput, Copy, Printer, ShieldAlert, Ban, Search,
  Mail, MailOpen, ChevronDown, FileText
} from 'lucide-vue-next'

const route = useRoute()
const router = useRouter()
const currentFolder = computed(() => route.params.folder || 'inbox')

const folderName = computed(() => {
  const f = currentFolder.value
  if (f === 'inbox') return '받은메일함'
  if (f === 'sent') return '보낸메일함'
  if (f === 'drafts') return '임시보관함'
  if (f === 'spam') return '스팸메일함'
  if (f === 'trash') return '휴지통'
  return f
})

const filteredMessages = computed(() => {
  return mockMessages.filter(msg => {
    // If currenFolder is 'all', show all? (Legacy usually doesn't have 'all' view like this, but logic strictly:
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
  <div class="h-full flex flex-col bg-white">
    <!-- Header Area with Detached Search Bar -->
    <div class="flex justify-between items-end px-3 py-2 border-b-2 border-[#666]">
         <div class="flex items-end gap-2">
             <h2 class="text-[14px] font-bold text-[#333] tracking-tight leading-none mb-0.5">
               {{ folderName }}
               <span class="text-[12px] font-normal text-[#666] ml-1">전체 메일 <span class="text-[#005fb0] font-bold">{{ filteredMessages.length }}</span>통 / 안읽은 메일 <span class="text-[#005fb0] font-bold">1</span>통</span>
             </h2>
         </div>

         <!-- Detached Search Bar -->
         <div class="flex gap-1 items-center">
             <div class="flex items-center">
                 <select class="h-[22px] text-[11px] border border-[#7F9DB9] mr-1 bg-white">
                     <option>제목</option>
                     <option>보낸사람</option>
                 </select>
                 <div class="relative">
                    <input type="text" class="h-[22px] w-[150px] border border-[#7F9DB9] pl-5 pr-2 text-[11px] shadow-inner focus:outline-none" placeholder="검색">
                    <Search class="absolute left-1 top-0.5 h-3.5 w-3.5 text-gray-400" />
                 </div>
             </div>
             <button class="h-[22px] bg-[#005fb0] hover:bg-[#004e90] border border-[#003e73] text-white text-[11px] px-3 shadow-sm rounded-sm">검색</button>
             <button class="h-[22px] bg-white hover:bg-gray-50 border border-[#C0C0C0] text-[#555] text-[11px] px-2 shadow-sm rounded-sm flex items-center gap-0.5">
                 상세검색 <ChevronDown class="h-3 w-3" />
             </button>
         </div>
    </div>

    <!-- Toolbar Tabs -->
    <div class="flex px-2 pt-1 bg-[#F0F4F7] border-b border-[#CCC] gap-0">
        <button
            @click="activeTab = 'basic'"
            class="px-3 py-1 text-[11px] border-t border-l border-r rounded-t-sm mb-[-1px] z-10"
            :class="activeTab === 'basic' ? 'bg-white border-[#999] font-bold text-[#333]' : 'bg-[#E0E0E0] border-[#CCC] text-[#666]'"
        >
            기본기능
        </button>
        <button
            @click="activeTab = 'advanced'"
            class="px-3 py-1 text-[11px] border-t border-l border-r rounded-t-sm mb-[-1px] z-10"
            :class="activeTab === 'advanced' ? 'bg-white border-[#999] font-bold text-[#333]' : 'bg-[#E0E0E0] border-[#CCC] text-[#666]'"
        >
            부가기능
        </button>
    </div>

    <!-- Toolbar Content -->
    <div class="bg-[#F0F4F7] border-b border-[#A0A0A0] flex items-center p-1 overflow-x-auto min-h-[56px]">

        <!-- Basic Toolbar -->
        <template v-if="activeTab === 'basic'">
            <!-- Common Group 1: Delete -->
            <div class="flex gap-0.5 mx-1">
                <button class="flex flex-col items-center justify-center w-[44px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <Trash2 class="h-5 w-5 text-red-600 mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">삭제</span>
                </button>
                <button v-if="currentFolder === 'trash' || currentFolder === 'spam'" class="flex flex-col items-center justify-center w-[50px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                     <div class="relative">
                    <Trash2 class="h-5 w-5 text-red-600 mb-0.5" stroke-width="2" />
                    <span class="absolute -right-1 -top-1 text-[8px] text-red-600 font-bold">X</span>
                    </div>
                    <span class="text-[11px] text-[#444] leading-none">완전삭제</span>
                </button>
            </div>

            <div class="mx-1 h-[30px] border-l border-[#CCC] border-r border-[#FFF]"></div>

            <!-- Group 2: Reply/Forward (Not in Trash/Spam usually, but keeping for now or toggling based on logic) -->
            <template v-if="currentFolder !== 'trash' && currentFolder !== 'spam'">
                <div class="flex gap-0.5 mx-1">
                    <button class="flex flex-col items-center justify-center w-[44px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                        <Reply class="h-5 w-5 text-blue-600 mb-0.5" stroke-width="2" />
                        <span class="text-[11px] text-[#444] leading-none">답장</span>
                    </button>
                    <button class="flex flex-col items-center justify-center w-[48px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                        <ReplyAll class="h-5 w-5 text-blue-600 mb-0.5" stroke-width="2" />
                        <span class="text-[11px] text-[#444] leading-none">전체답장</span>
                    </button>
                    <button class="flex flex-col items-center justify-center w-[44px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                        <Forward class="h-5 w-5 text-blue-600 mb-0.5" stroke-width="2" />
                        <span class="text-[11px] text-[#444] leading-none">전달</span>
                    </button>
                </div>
                <div class="mx-1 h-[30px] border-l border-[#CCC] border-r border-[#FFF]"></div>
            </template>

            <!-- Group 3: Move/Copy -->
            <div class="flex gap-0.5 mx-1">
                <button class="flex flex-col items-center justify-center w-[44px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <FolderInput class="h-5 w-5 text-[#E65100] mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">이동</span>
                </button>
                <button class="flex flex-col items-center justify-center w-[44px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <Copy class="h-5 w-5 text-[#E65100] mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">복사</span>
                </button>
            </div>

             <div class="mx-1 h-[30px] border-l border-[#CCC] border-r border-[#FFF]"></div>

            <!-- Group 4: Spam/Block (Only Inbox/Spam) -->
            <div class="flex gap-0.5 mx-1" v-if="currentFolder === 'inbox' || currentFolder === 'spam'">
                <button class="flex flex-col items-center justify-center w-[50px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <ShieldAlert class="h-5 w-5 text-green-600 mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">스팸신고</span>
                </button>
            </div>
            
            <!-- Group 5: Trash Specific (Empty, Restore) -->
            <div class="flex gap-0.5 mx-1" v-if="currentFolder === 'trash'">
                 <button class="flex flex-col items-center justify-center w-[50px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <Reply class="h-5 w-5 text-green-600 mb-0.5" stroke-width="2" /> 
                    <span class="text-[11px] text-[#444] leading-none">복구</span>
                </button>
                 <button class="flex flex-col items-center justify-center w-[50px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <Trash2 class="h-5 w-5 text-red-600 mb-0.5" stroke-width="2" /> 
                    <span class="text-[11px] text-[#444] leading-none">비우기</span>
                </button>
            </div>
        </template>

        <!-- Advanced Toolbar (부가기능) -->
        <template v-else>
             <!-- Group 1: Print/Save -->
            <div class="flex gap-0.5 mx-1">
                <button class="flex flex-col items-center justify-center w-[44px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <Printer class="h-5 w-5 text-[#555] mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">인쇄</span>
                </button>
                 <button class="flex flex-col items-center justify-center w-[44px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <FileText class="h-5 w-5 text-[#555] mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">저장</span>
                </button>
            </div>

            <div class="mx-1 h-[30px] border-l border-[#CCC] border-r border-[#FFF]"></div>

            <!-- Group 2: Block/Spam (Only Inbox) -->
             <div class="flex gap-0.5 mx-1" v-if="currentFolder === 'inbox'">
                <button class="flex flex-col items-center justify-center w-[50px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <ShieldAlert class="h-5 w-5 text-green-600 mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">스팸신고</span>
                </button>
                <button class="flex flex-col items-center justify-center w-[50px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <Ban class="h-5 w-5 text-red-600 mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">수신거부</span>
                </button>
            </div>

            <div class="mx-1 h-[30px] border-l border-[#CCC] border-r border-[#FFF]" v-if="currentFolder === 'inbox'"></div>

            <!-- Group 3: Quick Search -->
            <div class="flex gap-0.5 mx-1">
                 <button class="flex flex-col items-center justify-center w-[50px] h-[48px] hover:bg-[#E0E8F0] hover:border hover:border-[#A0A0A0] rounded-[2px] border border-transparent transition-all">
                    <Search class="h-5 w-5 text-[#005fb0] mb-0.5" stroke-width="2" />
                    <span class="text-[11px] text-[#444] leading-none">빠른검색</span>
                </button>
            </div>
        </template>
    </div>

    <!-- Mail List Table -->
    <div class="flex-1 overflow-auto">
      <Table>
        <TableHeader class="bg-gradient-to-b from-legacy-bg-header_gradient_start to-legacy-bg-header_gradient_end sticky top-0 z-10 shadow-sm">
          <TableRow class="hover:bg-transparent border-b-legacy-border-light h-[30px]">
            <TableHead class="w-[30px] border-r border-[#D6D6D6] h-[30px] p-0 text-center"><input type="checkbox" class="mx-auto" /></TableHead>
            <TableHead class="w-[30px] border-r border-[#D6D6D6] h-[30px] p-0 text-center text-legacy-text font-bold text-[11px]">F</TableHead>
            <TableHead class="w-[30px] border-r border-[#D6D6D6] h-[30px] p-0 text-center text-legacy-text font-bold text-[11px]">R</TableHead>
            <TableHead class="w-[30px] border-r border-[#D6D6D6] h-[30px] p-0 text-center text-legacy-text font-bold text-[11px]">A</TableHead>
            <TableHead class="w-[150px] border-r border-[#D6D6D6] h-[30px] text-legacy-text font-bold text-[11px] pl-2">보낸사람</TableHead>
            <TableHead class="border-r border-[#D6D6D6] h-[30px] text-legacy-text font-bold text-[11px] pl-2">제목</TableHead>
            <TableHead class="w-[120px] h-[30px] text-center text-legacy-text font-bold text-[11px]">받은날짜</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          <template v-if="filteredMessages.length > 0">
             <TableRow v-for="message in filteredMessages" :key="message.id" @click="handleRowClick(message.id)" class="hover:bg-legacy-bg-row_hover border-b border-[#E0E0E0] h-[30px] group cursor-pointer transition-colors duration-0">
                <TableCell class="p-0 text-center border-r border-transparent group-hover:border-[#E0E0E0]">
                   <input type="checkbox" />
                </TableCell>
                <TableCell class="p-0 text-center border-r border-transparent group-hover:border-[#E0E0E0]">
                  <Star v-if="message.flagged" class="h-3.5 w-3.5 fill-red-500 text-red-500 mx-auto" />
                  <Star v-else class="h-3.5 w-3.5 text-gray-300 mx-auto" />
                </TableCell>
                <TableCell class="p-0 text-center border-r border-transparent group-hover:border-[#E0E0E0]">
                   <Mail v-if="!message.read" class="h-3.5 w-3.5 fill-yellow-200 text-yellow-600 mx-auto" />
                   <MailOpen v-else class="h-3.5 w-3.5 text-gray-400 mx-auto" />
                </TableCell>
                <TableCell class="p-0 text-center border-r border-transparent group-hover:border-[#E0E0E0]">
                   <Paperclip v-if="message.hasAttachment" class="h-3.5 w-3.5 text-gray-500 mx-auto"/>
                </TableCell>
                <TableCell class="p-1 pl-2 border-r border-transparent group-hover:border-[#E0E0E0] text-[11px] text-legacy-text-body truncate">
                   <span :class="{'font-bold text-black': !message.read}">{{ message.from.name }}</span>
                </TableCell>
                <TableCell class="p-1 pl-2 border-r border-transparent group-hover:border-[#E0E0E0] text-[11px] text-legacy-text-body truncate">
                   <div class="flex items-center gap-1">
                     <span :class="{'font-bold text-black': !message.read}" class="hover:underline hover:text-legacy-blue">{{ message.subject }}</span>
                     <Badge v-for="tag in message.tags" :key="tag" variant="outline" class="text-[10px] px-1 py-0 h-3.5 border-legacy-border text-legacy-text-muted">{{ tag }}</Badge>
                   </div>
                </TableCell>
                <TableCell class="text-center p-1 text-[11px] text-legacy-text-muted font-tahoma">
                    {{ formatDate(message.date) }}
                </TableCell>
             </TableRow>
          </template>
          <template v-else>
             <TableRow>
               <TableCell colspan="7" class="h-24 text-center text-legacy-text-muted text-[12px]">
                 메일이 없습니다.
               </TableCell>
             </TableRow>
          </template>
        </TableBody>
      </Table>
    </div>
    
    <!-- Footer / Pagination -->
     <div class="h-[40px] border-t border-legacy-border flex items-center justify-center bg-[#F7F7F7]">
        <div class="flex gap-2 text-[11px] font-tahoma">
            <span class="text-legacy-text-muted cursor-pointer hover:underline">[Prev]</span>
            <span class="font-bold text-legacy-orange cursor-pointer">1</span>
            <span class="text-legacy-text-muted cursor-pointer hover:underline">2</span>
            <span class="text-legacy-text-muted cursor-pointer hover:underline">3</span>
            <span class="text-legacy-text-muted cursor-pointer hover:underline">4</span>
            <span class="text-legacy-text-muted cursor-pointer hover:underline">5</span>
            <span class="text-legacy-text-muted cursor-pointer hover:underline">[Next]</span>
        </div>
     </div>
  </div>
</template>

<style scoped>
.font-tahoma {
    font-family: Tahoma, sans-serif;
}
</style>
