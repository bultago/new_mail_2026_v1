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
// In a real app, this would be recursive. 
// For this mock, we'll manually nest 'Projects' under a parent if needed, 
// but let's simulate the visual tree first.

const isPersonalOpen = ref(true)

const iconMap: Record<string, any> = {
  'inbox': Inbox,
  'send': Send,
  'file': File,
  'clock': Clock,
  'alert-octagon': AlertOctagon,
  'trash-2': Trash2,
  'tag-red': Tag,
  'tag-blue': Tag,
}

const getIcon = (iconName?: string) => {
  return iconMap[iconName || ''] || Folder
}

const getIconColor = (iconName?: string) => {
    switch(iconName) {
        case 'inbox': return 'text-[#8B5CF6]'; // Purple-ish
        case 'send': return 'text-[#3B82F6]'; // Blue
        case 'trash-2': return 'text-[#EF4444]'; // Red
        case 'alert-octagon': return 'text-[#F59E0B]'; // Orange
        default: return 'text-[#6B7280]'; // Gray
    }
}
</script>

<template>
  <div 
    class="border border-[#3D83C1] bg-white text-[#555555] flex flex-col h-screen font-dotum text-[12px]"
    :style="{ width: width ? `${width}px` : '230px' }"
  >
    <!-- Sidebar Header (Title) -->
    <!-- Note: User said sidebar header might not be blue, but let's keep the benchmarked gradient title bar for 'Mailbox' if it exists, or remove if user implied strict white. 
         Re-checking benchmark: "Header: There is no blue header within the sidebar box itself." -> So I should remove the blue/gradient header box "Mailbox".
         However, usually there is some title. Let's look at the screenshot 'sidebar_top_view_1767588658329.png' (I can't see it but the agent did).
         The agent said "The folder list starts immediately below the buttons and quota bar in the white area."
         So I will remove the "Mailbox" header bar and place the buttons at the very top.
    -->
    
    <!-- Top Action Buttons -->
    <div class="p-2 pb-1 flex gap-1 justify-center bg-white">
        <Button 
            class="w-[79px] h-[30px] p-0 bg-gradient-to-b from-[#FFFFFF] to-[#E4E4E4] border border-[#A5ACB2] hover:bg-[#F0F0F0] shadow-sm rounded-[2px] flex items-center justify-center gap-1"
            as-child
        >
             <router-link to="/mail/write">
                 <div class="relative w-4 h-3 flex items-center justify-center">
                     <div class="absolute inset-0 bg-[#FBC02D] rounded-[1px]"></div> <!-- Yellow Envelope Body -->
                     <div class="absolute top-0 w-full h-1.5 border-t border-l border-r border-[#FFF] transform origin-top rotate-0"></div> <!-- Flap mock -->
                 </div>
                 <span class="text-[12px] text-[#434343] font-normal leading-none pointer-events-none">메일쓰기</span>
             </router-link>
        </Button>
        <Button class="w-[79px] h-[30px] p-0 bg-gradient-to-b from-[#FFFFFF] to-[#E4E4E4] border border-[#A5ACB2] hover:bg-[#F0F0F0] shadow-sm rounded-[2px] flex items-center justify-center gap-1">
             <div class="relative w-4 h-3 flex items-center justify-center">
                 <div class="absolute inset-0 bg-[#3B82F6] rounded-[1px]"></div> <!-- Blue Envelope Body -->
                 <div class="absolute top-0 w-full h-1.5 border-t border-l border-r border-[#FFF] transform origin-top rotate-0"></div>
             </div>
             <span class="text-[12px] text-[#434343] font-normal leading-none">수신확인</span>
        </Button>
    </div>

    <!-- Quota Bar (Mock) -->
    <div class="px-2 mb-2">
         <div class="flex justify-between text-[11px] text-[#888] mb-0.5">
             <span>용량</span>
             <span>1.2G / 10G</span>
         </div>
         <div class="h-[8px] w-full bg-[#E0E0E0] rounded-sm overflow-hidden">
             <div class="h-full bg-gradient-to-r from-blue-300 to-blue-500 w-[12%]"></div>
         </div>
    </div>

    <ScrollArea class="flex-1 bg-white">
      <div class="p-2 pt-0">
        
        <!-- System Folders -->
        <div class="mb-2">
           <div v-for="folder in systemFolders" :key="folder.id" class="relative">
              <Button 
                variant="ghost" 
                class="w-full justify-start h-[24px] px-2 hover:bg-[#FFF9C4] hover:text-black rounded-none"
                :class="{ 'font-bold text-[#005fb0]': currentFolder === folder.id }"
                as-child
              >
                <router-link :to="`/mail/list/${folder.id}`" class="flex items-center">
                   <component :is="getIcon(folder.icon)" class="mr-2 h-4 w-4" :class="getIconColor(folder.icon)"/>
                   {{ folder.name }}
                   <span v-if="folder.unreadCount" class="ml-auto text-red-500 font-bold text-[11px]">{{ folder.unreadCount }}</span>
                </router-link>
              </Button>
           </div>
        </div>

        <Separator class="bg-[#D6D6D6] my-2" />

        <!-- User Folders (Tree Style) -->
        <div>
            <div class="flex items-center px-1 mb-1 cursor-pointer" @click="isPersonalOpen = !isPersonalOpen">
                <div class="border border-[#777] bg-white w-[9px] h-[9px] flex items-center justify-center mr-1">
                    <span class="text-[8px] leading-none">{{ isPersonalOpen ? '-' : '+' }}</span>
                </div>
                <Folder class="h-4 w-4 text-[#FBC02D] fill-[#FBC02D] mr-1"/>
                <span class="font-bold">개인메일함</span>
            </div>
            
            <div v-if="isPersonalOpen" class="ml-[5px] border-l border-dotted border-[#999] pl-3 py-0.5 space-y-0.5">
                <template v-for="(folder, index) in userFolders" :key="folder.id">
                     <div class="relative flex items-center">
                        <div class="absolute -left-[13px] top-[12px] w-[12px] border-t border-dotted border-[#999]"></div>
                        <Button 
                            variant="ghost" 
                            class="w-full justify-start h-[24px] px-1 hover:bg-[#FFF9C4] hover:text-black rounded-none"
                            :class="{ 'font-bold text-[#005fb0]': currentFolder === folder.id }"
                            as-child
                        >
                            <router-link :to="`/mail/list/${folder.id}`" class="flex items-center">
                                <Folder class="mr-2 h-3.5 w-3.5 text-[#FBC02D] fill-[#FBC02D]"/>
                                {{ folder.name }}
                                <span v-if="folder.unreadCount" class="ml-auto text-red-500 text-[10px]">{{ folder.unreadCount }}</span>
                            </router-link>
                        </Button>
                     </div>
                </template>
            </div>
        </div>

        <Separator class="bg-[#D6D6D6] my-2" />

        <!-- Tags -->
        <div>
            <div class="flex items-center px-1 mb-1">
                 <div class="border border-[#777] bg-white w-[9px] h-[9px] flex items-center justify-center mr-1">
                    <span class="text-[8px] leading-none">-</span>
                </div>
                <Tag class="h-4 w-4 text-[#777] mr-1"/>
                <span class="font-bold">태그</span>
            </div>
             <div class="ml-[5px] border-l border-dotted border-[#999] pl-3 py-0.5 space-y-0.5">
                 <template v-for="tag in tags" :key="tag.id">
                     <div class="relative flex items-center">
                        <div class="absolute -left-[13px] top-[12px] w-[12px] border-t border-dotted border-[#999]"></div>
                        <Button variant="ghost" class="w-full justify-start h-[24px] px-1 hover:bg-[#FFF9C4] rounded-none">
                            <component :is="getIcon(tag.icon)" class="mr-2 h-3.5 w-3.5" :class="tag.icon === 'tag-red' ? 'text-red-500' : 'text-blue-500'"/>
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
