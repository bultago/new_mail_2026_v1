<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Button } from '@/components/ui/button'
import { 
  Reply, ReplyAll, Forward, Trash2, Printer, 
  ChevronLeft, Paperclip, Download, FileText 
} from 'lucide-vue-next'
import { mockMessages } from '@/mocks/mailData'

const route = useRoute()
const router = useRouter()
const messageId = route.params.id as string

const message = computed(() => {
    return mockMessages.find(m => m.id === messageId)
})

const handleBack = () => {
    router.back()
}

// Mock formatting details
const headerDate = computed(() => {
    if(!message.value) return ''
    return new Date(message.value.date).toLocaleString('ko-KR', {
        year: 'numeric', month: '2-digit', day: '2-digit', 
        hour: '2-digit', minute: '2-digit', weekday: 'short' 
    })
})
</script>

<template>
  <div class="flex flex-col h-full bg-white" v-if="message">
      
    <!-- Top Action Bar -->
    <div class="px-4 py-2 border-b border-legacy-border bg-[#F7F7F7] flex items-center gap-1">
       <Button variant="outline" size="sm" class="h-[28px] bg-white text-[#444] text-[11px]" @click="handleBack">
           <ChevronLeft class="h-3.5 w-3.5 mr-1" /> 목록
       </Button>
       
       <div class="h-4 w-[1px] bg-[#CCC] mx-2"></div>

       <Button variant="outline" size="sm" class="h-[28px] bg-white text-[#444] text-[11px]">
           <Reply class="h-3.5 w-3.5 mr-1 text-blue-600" /> 답장
       </Button>
       <Button variant="outline" size="sm" class="h-[28px] bg-white text-[#444] text-[11px]">
           <Forward class="h-3.5 w-3.5 mr-1 text-blue-600" /> 전달
       </Button>
       
       <div class="h-4 w-[1px] bg-[#CCC] mx-2"></div>

       <Button variant="outline" size="sm" class="h-[28px] bg-white text-[#444] text-[11px]">
           <Trash2 class="h-3.5 w-3.5 mr-1 text-red-600" /> 삭제
       </Button>
    </div>

    <!-- Read Content Area -->
    <div class="p-6 flex-1 overflow-auto">
        <div class="border border-legacy-border bg-white shadow-sm max-w-[1000px]">
            <!-- Header Info -->
            <div class="bg-[#F9F9F9] border-b border-legacy-border p-4">
                <div class="mb-3">
                    <h1 class="text-[18px] font-bold text-[#333] tracking-tight leading-tight">
                        <span class="inline-block px-1.5 py-0.5 rounded bg-[#EEE] text-[#666] text-[11px] font-normal align-middle mr-2 border border-[#DDD]">받은메일</span>
                        {{ message.subject }}
                    </h1>
                </div>
                <div class="grid grid-cols-[80px_1fr] gap-y-1 text-[12px]">
                    <div class="font-bold text-[#555] text-right pr-4">보낸사람</div>
                    <div class="flex items-center">
                        <span class="font-bold text-[#333]">{{ message.from.name }}</span>
                        <span class="text-[#666] ml-1">&lt;{{ message.from.email }}&gt;</span>
                        <Button variant="ghost" size="sm" class="h-[20px] px-1 ml-2 text-[10px] text-blue-600 hover:text-blue-800">+ 주소록추가</Button>
                    </div>

                    <div class="font-bold text-[#555] text-right pr-4">받는사람</div>
                    <div class="text-[#444]">
                        <span v-for="(to, idx) in message.to" :key="idx">
                            {{ to.name }} &lt;{{ to.email }}&gt;<span v-if="idx < message.to.length - 1">, </span>
                        </span>
                    </div>

                    <div class="font-bold text-[#555] text-right pr-4">보낸날짜</div>
                    <div class="text-[#666] font-tahoma">{{ headerDate }}</div>
                </div>
            </div>

            <!-- Attachments -->
            <div v-if="message.hasAttachment" class="bg-[#FFFBE6] px-4 py-3 border-b border-legacy-border flex items-start gap-4">
                <div class="pt-0.5"><Paperclip class="h-4 w-4 text-[#555]" /></div>
                <div class="flex-1">
                    <div class="text-[12px] font-bold text-[#333] mb-1">
                        첨부파일 <span class="text-legacy-blue">1개</span> (25KB) 
                        <span class="text-[#999] font-normal mx-2">|</span> 
                        <a href="#" class="text-legacy-blue hover:underline">모두저장</a>
                    </div>
                    <!-- Mock Attachment Item -->
                    <div class="flex items-center gap-2 text-[12px] p-1 hover:bg-[#F0F0F0] rounded cursor-pointer max-w-fit">
                        <FileText class="h-3.5 w-3.5 text-gray-500" />
                        <span class="text-[#333]">Project_Specs_v1.pdf</span>
                        <span class="text-[#888]">(25KB)</span>
                        <Download class="h-3.5 w-3.5 text-gray-400 hover:text-blue-600" />
                    </div>
                </div>
            </div>

            <!-- Body -->
            <div class="p-8 min-h-[300px] text-[#333] text-[13px] leading-relaxed">
                <!-- Mock Body Content -->
                <div v-if="message.preview">
                    {{ message.preview }}
                    <br/><br/>
                    <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>
                    <br/>
                    <div class="border-t border-[#EEE] pt-4 mt-4 text-[#888]">
                        <p class="font-bold">{{ message.from.name }}</p>
                        <p>Terrace Technologies Inc.</p>
                        <p>Mobile: 010-1234-5678</p>
                    </div>
                </div>
            </div>

        </div>
    </div>
  </div>

  <div class="flex flex-col h-full bg-white text-center text-gray-500 p-8" v-else>
      Message not found.
  </div>
</template>

<style scoped>
.font-tahoma {
    font-family: Tahoma, sans-serif;
}
</style>
