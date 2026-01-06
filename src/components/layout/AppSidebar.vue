<script setup lang="ts">
import { computed, ref } from 'vue'
import { useRoute } from 'vue-router'
import { ScrollArea } from '@/components/ui/scroll-area'
import { Button } from '@/components/ui/button'
import { Separator } from '@/components/ui/separator'
import { Badge } from '@/components/ui/badge'
import { 
  Inbox, Send, File, Clock, AlertOctagon, Trash2, 
  Tag, Folder, Plus, ChevronRight, ChevronDown 
} from 'lucide-vue-next'
import { systemFolders, userFolders, tags } from '@/mocks/mailData'

const props = defineProps<{
  width?: number
}>()

const route = useRoute()
const currentFolder = computed(() => route.params.folder || 'inbox')

// Mock tree data structure for Personal Folders
const isPersonalOpen = ref(true)

// Legacy Style Custom Icons (Svg inline for specific colors)
</script>

<template>
  <div 
    class="border-r border-legacy-border-sidebar bg-legacy-sidebar-bg text-legacy-text flex flex-col h-screen font-dotum text-[12px]"
    :style="{ width: width ? `${width}px` : '230px' }"
  >
    
    <!-- Top Action Buttons -->
    <div class="p-3 pb-2 flex gap-2 justify-center bg-white dark:bg-zinc-900 border-b border-legacy-border">
        <Button 
            class="flex-1 h-[36px] bg-[#005fb0] hover:bg-[#004e92] text-white shadow-sm rounded-md flex items-center justify-center gap-2 transition-colors"
            as-child
        >
             <router-link to="/mail/write">
                 <div class="relative w-4 h-3 flex items-center justify-center">
                     <div class="absolute inset-0 bg-white/20 rounded-[1px]"></div>
                     <div class="absolute top-0 w-full h-1.5 border-t border-l border-r border-white/50 transform origin-top rotate-0"></div> 
                 </div>
                 <span class="text-[13px] leading-none pb-[1px]">메일쓰기</span>
             </router-link>
        </Button>
        <Button class="w-[36px] h-[36px] p-0 bg-white hover:bg-gray-50 border border-legacy-border shadow-sm rounded-md flex items-center justify-center text-legacy-text transition-colors dark:bg-zinc-800 dark:border-zinc-700 dark:text-gray-200" title="수신확인">
             <div class="relative w-4 h-3 flex items-center justify-center">
                 <div class="absolute inset-0 bg-blue-500 rounded-[1px]"></div>
                 <div class="absolute top-0 w-full h-1.5 border-t border-l border-r border-white transform origin-top rotate-0"></div>
             </div>
        </Button>
    </div>

    <!-- Quota Bar (Mock) -->
    <div class="px-2 mb-2">
         <div class="flex justify-between text-[11px] text-legacy-text-muted mb-0.5">
             <span>용량</span>
             <span>1.2G / 10G</span>
         </div>
         <div class="h-[8px] w-full bg-legacy-border rounded-sm overflow-hidden">
             <div class="h-full bg-gradient-to-r from-blue-300 to-blue-500 w-[12%]"></div>
         </div>
    </div>

    <ScrollArea class="flex-1 bg-legacy-sidebar-bg">
      <div class="p-2 pt-0">
        
        <!-- System Folders -->
        <div class="mb-2">
           <!-- Inbox -->
           <div class="relative">
              <Button 
                variant="ghost" 
                class="w-full justify-start h-[30px] px-3 rounded-md transition-all mb-0.5"
                :class="currentFolder === 'inbox' ? '!bg-[#005fb0] !text-white shadow-sm' : 'hover:!bg-[#f0f0f0] text-legacy-text dark:text-gray-200 dark:hover:bg-zinc-800'"
                as-child
              >
                <router-link to="/mail/list/inbox" class="flex items-center">
                    <!-- Custom Colored Inbox Icon -->
                   <svg class="mr-2 h-4.5 w-4.5 transition-colors" :class="currentFolder === 'inbox' ? 'text-white' : 'text-[#8B5CF6]'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M4 15V9C4 7.89543 4.89543 7 6 7H18C19.1046 7 20 7.89543 20 9V15M4 15V16C4 17.1046 4.89543 18 6 18H18C19.1046 18 20 17.1046 20 16V15M4 15L8 15C8.55228 15 9 15.4477 9 16V16C9 16.5523 9.44772 17 10 17H14C14.5523 17 15 16.5523 15 16V16C15 15.4477 15.4477 15 16 15L20 15" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
                   <span class="text-[13px] pt-0.5">받은메일함</span>
                   <span class="ml-auto text-[11px] pt-0.5" :class="currentFolder === 'inbox' ? '!text-white' : '!text-[#D32F2F]'">3</span>
                </router-link>
              </Button>
           </div>
           <!-- Sent -->
           <div class="relative">
              <Button 
                variant="ghost" 
                 class="w-full justify-start h-[30px] px-3 rounded-md transition-all mb-0.5"
                :class="currentFolder === 'sent' ? '!bg-[#005fb0] !text-white shadow-sm' : 'hover:!bg-[#f0f0f0] text-legacy-text dark:text-gray-200 dark:hover:bg-zinc-800'"
                as-child
              >
                <router-link to="/mail/list/sent" class="flex items-center">
                    <svg class="mr-2 h-4.5 w-4.5 transition-colors" :class="currentFolder === 'sent' ? 'text-white' : 'text-[#2563EB]'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M22 2L11 13M22 2L15 22L11 13M11 13L2 9L22 2" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
                    <span class="text-[13px] pt-0.5">보낸메일함</span>
                </router-link>
              </Button>
           </div>
           <!-- Drafts -->
           <div class="relative">
              <Button 
                variant="ghost" 
                class="w-full justify-start h-[30px] px-3 rounded-md transition-all mb-0.5"
                :class="currentFolder === 'drafts' ? '!bg-[#005fb0] !text-white shadow-sm' : 'hover:!bg-[#f0f0f0] text-legacy-text dark:text-gray-200 dark:hover:bg-zinc-800'"
                as-child
              >
                <router-link to="/mail/list/drafts" class="flex items-center">
                    <svg class="mr-2 h-4.5 w-4.5 transition-colors" :class="currentFolder === 'drafts' ? 'text-white' : 'text-[#F59E0B]'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7"/><path d="M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
                   <span class="text-[13px] pt-0.5">임시보관함</span>
                   <span class="ml-auto text-[11px] pt-0.5" :class="currentFolder === 'drafts' ? '!text-white' : '!text-[#D32F2F]'">2</span>
                </router-link>
              </Button>
           </div>
           <!-- Spam -->
           <div class="relative">
              <Button 
                variant="ghost" 
                class="w-full justify-start h-[30px] px-3 rounded-md transition-all mb-0.5"
                :class="currentFolder === 'spam' ? '!bg-[#005fb0] !text-white shadow-sm' : 'hover:!bg-[#f0f0f0] text-legacy-text dark:text-gray-200 dark:hover:bg-zinc-800'"
                as-child
              >
                <router-link to="/mail/list/spam" class="flex items-center">
                   <svg class="mr-2 h-4.5 w-4.5 transition-colors" :class="currentFolder === 'spam' ? 'text-white' : 'text-[#7C3AED]'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M12 22s8-4 8-10V5l-8-3-8 3v7c0 6 8 10 8 10z" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
                   <span class="text-[13px] pt-0.5">스팸메일함</span>
                </router-link>
              </Button>
           </div>
           <!-- Trash -->
           <div class="relative">
              <Button 
                variant="ghost" 
                class="w-full justify-start h-[30px] px-3 rounded-md transition-all mb-0.5"
                :class="currentFolder === 'trash' ? '!bg-[#005fb0] !text-white shadow-sm' : 'hover:!bg-[#f0f0f0] text-legacy-text dark:text-gray-200 dark:hover:bg-zinc-800'"
                as-child
              >
                <router-link to="/mail/list/trash" class="flex items-center">
                    <svg class="mr-2 h-4.5 w-4.5 transition-colors" :class="currentFolder === 'trash' ? 'text-white' : 'text-[#DC2626]'" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><path d="M3 6H5H21M19 6V20C19 20.5304 18.7893 21.0391 18.4142 21.4142C18.0391 21.7893 17.5304 22 17 22H7C6.46957 22 5.96086 21.7893 5.58579 21.4142C5.21071 21.0391 5 20.5304 5 20V6M8 6V4C8 3.46957 8.21071 2.96086 8.58579 2.58579C8.96086 2.21071 9.46957 2 10 2H14C14.5304 2 15.0391 2.21071 15.4142 2.58579C15.7893 2.96086 16 3.46957 16 4V6M10 11V17M14 11V17" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"/></svg>
                   <span class="text-[13px] pt-0.5">휴지통</span>
                   <span class="ml-auto text-[11px] pt-0.5" :class="currentFolder === 'trash' ? '!text-white' : '!text-[#D32F2F]'">112</span>
                </router-link>
              </Button>
           </div>
        </div>

        <Separator class="bg-legacy-border my-2" />

        <!-- User Folders (Tree Style) -->
        <div>
            <div class="flex items-center px-1 mb-1 cursor-pointer text-legacy-text dark:text-gray-200 hover:text-legacy-blue group" @click="isPersonalOpen = !isPersonalOpen">
                <div class="border border-legacy-text-muted bg-legacy-bg dark:bg-zinc-800 dark:border-zinc-600 w-[9px] h-[9px] flex items-center justify-center mr-1">
                    <span class="text-[8px] leading-none select-none">{{ isPersonalOpen ? '-' : '+' }}</span>
                </div>
                <!-- FILLED YELLOW FOLDER ICON -->
                <svg class="h-4 w-4 mr-1 text-[#FBC02D]" viewBox="0 0 24 24" fill="currentColor" stroke="none"><path d="M22 11V19C22 20.1 21.1 21 20 21H4C2.9 21 2 20.1 2 19V7C2 5.9 2.9 5 4 5H10L12 7H20C21.1 7 22 7.9 22 9V11Z"/></svg>
                <span>개인메일함</span>
            </div>
            
            <div v-if="isPersonalOpen" class="ml-[5px] border-l border-dotted border-legacy-text-muted pl-3 py-0.5 space-y-0.5">
                <template v-for="(folder, index) in userFolders" :key="folder.id">
                     <div class="relative flex items-center h-[28px]">
                        <!-- Dotted line to item -->
                        <div class="absolute -left-[13px] top-[14px] w-[12px] border-t border-dotted border-legacy-text-muted"></div>
                        <Button 
                            variant="ghost" 
                            class="w-full justify-start h-[28px] px-1 rounded-none transition-colors"
                             :class="currentFolder === folder.id ? '!bg-[#005fb0] !text-white' : 'hover:!bg-[#f0f0f0] text-legacy-text dark:text-gray-200 dark:hover:bg-zinc-800'"
                            as-child
                        >
                            <router-link :to="`/mail/list/${folder.id}`" class="flex items-center">
                                <!-- FILLED YELLOW FOLDER ICON -->
                                <svg class="h-4 w-4 mr-2" :class="currentFolder === folder.id ? 'text-white' : 'text-[#FBC02D]'" viewBox="0 0 24 24" fill="currentColor" stroke="none"><path d="M22 11V19C22 20.1 21.1 21 20 21H4C2.9 21 2 20.1 2 19V7C2 5.9 2.9 5 4 5H10L12 7H20C21.1 7 22 7.9 22 9V11Z"/></svg>
                                <span class="text-[12px] pt-0.5">{{ folder.name }}</span>
                                <span v-if="folder.unreadCount" class="ml-auto text-[10px] pt-0.5" :class="currentFolder === folder.id ? '!text-white' : '!text-[#D32F2F]'">{{ folder.unreadCount }}</span>
                            </router-link>
                        </Button>
                     </div>
                </template>
            </div>
        </div>

        <Separator class="bg-legacy-border my-2" />

        <!-- Tags -->
        <div>
            <div class="flex items-center px-1 mb-1 text-legacy-text dark:text-gray-200">
                 <div class="border border-legacy-text-muted bg-legacy-bg dark:bg-zinc-800 dark:border-zinc-600 w-[9px] h-[9px] flex items-center justify-center mr-1">
                    <span class="text-[8px] leading-none mb-[2px] select-none">-</span>
                </div>
                <Tag class="h-3.5 w-3.5 text-legacy-text-muted mr-1"/>
                <span>태그</span>
            </div>
             <div class="ml-[5px] border-l border-dotted border-legacy-text-muted pl-3 py-0.5 space-y-0.5">
                 <template v-for="tag in tags" :key="tag.id">
                     <div class="relative flex items-center h-[24px]">
                        <div class="absolute -left-[13px] top-[12px] w-[12px] border-t border-dotted border-legacy-text-muted"></div>
                        <Button variant="ghost" class="w-full justify-start h-[24px] px-1 hover:bg-legacy-sidebar-hover text-legacy-text dark:text-gray-200 dark:hover:bg-zinc-800 rounded-none transition-colors text-[12px] font-dotum">
                            <div :class="tag.icon === 'tag-red' ? 'bg-red-500' : 'bg-blue-500'" class="w-2.5 h-2.5 rounded-full mr-2"></div>
                            {{ tag.name }}
                        </Button>
                     </div>
                 </template>
            </div>
        </div>

      </div>
    </ScrollArea>
  </div>
</template>
