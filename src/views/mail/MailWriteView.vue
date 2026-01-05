<script setup lang="ts">
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { Button } from '@/components/ui/button'
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from '@/components/ui/select'
import { Checkbox } from '@/components/ui/checkbox'
import { Paperclip, Send, Save, Eye, X } from 'lucide-vue-next'

const router = useRouter()

const to = ref('')
const cc = ref('')
const subject = ref('')
const body = ref('')
const isMe = ref(false)
const isIndividual = ref(false)

const handleSend = () => {
  // Mock send
  alert('메일을 발송했습니다.')
  router.push('/mail/list/sent')
}

const handleCancel = () => {
    router.back()
}
</script>

<template>
  <div class="flex flex-col h-full bg-white">
    <!-- Top Action Bar -->
    <div class="px-4 py-2 border-b border-legacy-border bg-[#F7F7F7] flex items-center gap-2">
      <Button 
        size="sm" 
        class="bg-legacy-blue hover:bg-[#004e90] text-white h-[30px] rounded-sm gap-1 px-4 text-[12px] font-bold"
        @click="handleSend"
      >
        <Send class="h-3.5 w-3.5" />
        보내기
      </Button>
      <Button 
        variant="outline" 
        size="sm" 
        class="h-[30px] rounded-sm gap-1 px-3 text-[12px] bg-white border-legacy-border text-[#444]"
      >
        <Eye class="h-3.5 w-3.5" />
        미리보기
      </Button>
      <Button 
        variant="outline" 
        size="sm" 
        class="h-[30px] rounded-sm gap-1 px-3 text-[12px] bg-white border-legacy-border text-[#444]"
      >
        <Save class="h-3.5 w-3.5" />
        임시저장
      </Button>
    </div>

    <!-- Write Form -->
    <div class="p-6 flex-1 overflow-auto">
        <div class="max-w-[1000px] border border-legacy-border bg-white p-6 shadow-sm">
            
            <!-- To Field -->
            <div class="flex items-center mb-4">
                <label class="w-[100px] text-right pr-4 text-[12px] font-bold text-[#444]">받는사람</label>
                <div class="flex-1 flex items-center gap-2">
                    <input 
                        v-model="to"
                        type="text" 
                        class="flex-1 h-[30px] border border-legacy-border px-2 text-[12px] focus:outline-none focus:border-legacy-blue"
                    />
                    <div class="flex items-center gap-1.5 ml-2">
                        <Checkbox id="me" v-model:checked="isMe" />
                        <label for="me" class="text-[12px] cursor-pointer select-none">내게쓰기</label>
                    </div>
                     <div class="flex items-center gap-1.5 ml-3">
                         <Checkbox id="individual" v-model:checked="isIndividual" />
                        <label for="individual" class="text-[12px] cursor-pointer select-none text-[#666]">개별발송</label>
                    </div>
                     <Button variant="outline" size="sm" class="h-[28px] text-[11px] ml-2">주소록</Button>
                </div>
            </div>

            <!-- Cc Field -->
             <div class="flex items-center mb-4">
                <label class="w-[100px] text-right pr-4 text-[12px] font-bold text-[#444]">참조</label>
                <div class="flex-1 flex items-center gap-2">
                    <input 
                        v-model="cc"
                        type="text" 
                        class="flex-1 h-[30px] border border-legacy-border px-2 text-[12px] focus:outline-none focus:border-legacy-blue"
                    />
                     <Button variant="outline" size="sm" class="h-[28px] text-[11px] ml-2">주소록</Button>
                </div>
            </div>

            <!-- Subject Field -->
            <div class="flex items-center mb-4">
                <label class="w-[100px] text-right pr-4 text-[12px] font-bold text-[#444]">제목</label>
                <div class="flex-1">
                     <input 
                        v-model="subject"
                        type="text" 
                        class="w-full h-[30px] border border-legacy-border px-2 text-[12px] focus:outline-none focus:border-legacy-blue font-bold"
                    />
                </div>
            </div>

            <!-- Editor Area -->
            <div class="flex mb-4">
                 <label class="w-[100px] text-right pr-4 text-[12px] font-bold text-[#444] pt-2">내용</label>
                 <div class="flex-1">
                     <textarea 
                        v-model="body"
                        class="w-full h-[400px] border border-legacy-border p-3 text-[13px] leading-relaxed resize-none focus:outline-none focus:border-legacy-blue"
                        placeholder="메일 내용을 입력하세요..."
                     ></textarea>
                 </div>
            </div>
            
            <!-- Attachment Area -->
             <div class="flex items-center border-t border-legacy-border pt-4">
                <label class="w-[100px] text-right pr-4 text-[12px] font-bold text-[#444]">파일첨부</label>
                <div class="flex-1 bg-[#F9F9F9] border border-dashed border-[#CCC] p-4 rounded text-center">
                    <Button variant="secondary" size="sm" class="h-[28px] text-[11px]">
                         <Paperclip class="h-3 w-3 mr-1" /> 파일 선택
                    </Button>
                    <span class="ml-2 text-[11px] text-[#888]">또는 파일을 여기로 드래그하세요</span>
                </div>
            </div>

        </div>
    </div>
  </div>
</template>
