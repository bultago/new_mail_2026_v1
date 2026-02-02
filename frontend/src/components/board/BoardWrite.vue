<script setup lang="ts">
import { ref, computed } from 'vue'
import { useI18n } from 'vue-i18n'
import { Button } from '@/components/ui/button'
import {
    Save, X, Upload, Eye, FileText,
    Bold, Italic, Underline,
    AlignLeft, AlignCenter, AlignRight,
    List, ListOrdered, Link2, Image, Code
} from 'lucide-vue-next'

const { t } = useI18n()
defineProps<{ boardId: string }>()
const emit = defineEmits(['close'])

const subject = ref('')
const content = ref('')
const editorMode = ref('HTML')
const attachedFiles = ref<{ name: string, size: number }[]>([])

const charCount = computed(() => content.value.length)

const handleSave = () => {
    alert('저장되었습니다. (Mock)')
    emit('close')
}

const handlePreview = () => {
    alert('미리보기 (Mock)')
}

const handleFileSelect = () => {
    const input = document.createElement('input')
    input.type = 'file'
    input.multiple = true
    input.onchange = (e) => {
        const files = (e.target as HTMLInputElement).files
        if (files) {
            for (let i = 0; i < files.length; i++) {
                attachedFiles.value.push({
                    name: files[i].name,
                    size: files[i].size
                })
            }
        }
    }
    input.click()
}

const removeFile = (index: number) => {
    attachedFiles.value.splice(index, 1)
}

const applyFormat = (command: string) => {
    if (editorMode.value === 'HTML') {
        alert(`${t('board.format_applied') || '포맷 적용'}: ${command} (Mock)`)
    }
}
</script>

<template>
    <div class="flex flex-col h-full bg-[#F0F0F2] dark:bg-zinc-900 font-dotum">
        <!-- Title Header -->
        <div class="px-4 py-3 bg-white border-b border-gray-200 dark:bg-zinc-900 dark:border-zinc-700">
            <h2 class="text-lg font-bold text-gray-800 dark:text-gray-100">
                {{ t('board.write.title') || '게시글 작성' }}
            </h2>
        </div>

        <!-- Toolbar -->
        <div
            class="px-4 py-2 border-b border-legacy-border bg-[#F7F7F7] dark:bg-zinc-900 flex flex-wrap items-center gap-1 shadow-sm">
            <Button variant="outline" size="sm" @click="handleSave"
                class="h-[28px] bg-white text-[#444] text-[11px] font-bold border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Save class="h-3.5 w-3.5 mr-1.5 text-blue-600 dark:text-blue-400" /> {{ t('common.save') || '저장' }}
            </Button>
            <Button variant="outline" size="sm" @click="handlePreview"
                class="h-[28px] bg-white text-[#444] text-[11px] border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <Eye class="h-3.5 w-3.5 mr-1.5 text-gray-600 dark:text-gray-400" /> {{ t('common.preview') || '미리보기' }}
            </Button>
            <Button variant="outline" size="sm" @click="$emit('close')"
                class="h-[28px] bg-white text-[#444] text-[11px] border-legacy-border hover:bg-gray-50 dark:bg-zinc-800 dark:text-gray-200 dark:border-zinc-700">
                <X class="h-3.5 w-3.5 mr-1.5 text-red-600 dark:text-red-400" /> {{ t('common.cancel') || '취소' }}
            </Button>
        </div>

        <!-- Form -->
        <div class="flex-1 overflow-auto p-4 bg-[#F0F0F2] dark:bg-zinc-900">
            <div
                class="flex-1 flex flex-col w-full border border-legacy-border bg-white dark:bg-zinc-800 shadow-sm p-4">
                <!-- Fields Grid - Auto Height Content -->
                <div class="grid grid-cols-[90px_1fr] gap-y-2 items-center mb-4 flex-shrink-0">
                    <!-- Subject -->
                    <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400">
                        {{ t('board.subject') || '제목' }}
                    </div>
                    <div class="flex flex-col gap-1">
                        <input type="text" v-model="subject"
                            class="w-full h-[28px] border border-legacy-border px-2 text-[12px] focus:border-blue-500 outline-none dark:bg-zinc-700 dark:border-zinc-600 dark:text-white"
                            :placeholder="t('board.subject_placeholder')" />
                        <div class="text-right text-[11px] text-gray-500 dark:text-gray-400">
                            {{ subject.length }} {{ t('common.characters') }}
                        </div>
                    </div>

                    <!-- Attachments -->
                    <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400 self-start pt-1.5">
                        {{ t('board.attach') || '첨부파일' }}
                    </div>
                    <div
                        class="border border-legacy-border bg-[#F9F9F9] dark:bg-zinc-700/50 p-2 min-h-[60px] dark:border-zinc-600">
                        <div class="flex items-center gap-2 mb-2">
                            <Button variant="outline" size="sm" @click="handleFileSelect"
                                class="h-[24px] text-[11px] bg-white hover:bg-gray-50 gap-1 dark:bg-zinc-700 dark:text-gray-200 dark:border-zinc-600">
                                <Upload class="h-3 w-3" /> {{ t('board.file_browse') }}
                            </Button>
                            <span class="text-[11px] text-gray-400 ml-auto">{{ t('board.file_limit') }}</span>
                        </div>

                        <!-- File List -->
                        <div v-if="attachedFiles.length > 0" class="space-y-1 mb-2">
                            <div v-for="(file, idx) in attachedFiles" :key="idx"
                                class="flex items-center gap-2 text-[11px] bg-white dark:bg-zinc-700 p-1.5 rounded border border-gray-200 dark:border-zinc-600">
                                <FileText class="h-3 w-3 text-blue-600 dark:text-blue-400 flex-shrink-0" />
                                <span class="flex-1 truncate">{{ file.name }}</span>
                                <span class="text-gray-400 flex-shrink-0">{{ Math.round(file.size / 1024) }}KB</span>
                                <button @click="removeFile(idx)"
                                    class="flex-shrink-0 text-red-600 hover:text-red-700 dark:text-red-400">
                                    <X class="h-3 w-3" />
                                </button>
                            </div>
                        </div>

                        <!-- Empty State -->
                        <div v-else
                            class="text-[11px] text-gray-400 text-center py-2 border-2 border-dashed border-gray-200 rounded dark:border-zinc-600">
                            {{ t('board.drag_drop_hint') }}
                        </div>
                    </div>
                </div>

                <!-- Editor - Flex Grow to fill remaining space -->
                <div class="flex-1 flex flex-col min-h-[200px]">
                    <div class="grid grid-cols-[90px_1fr] h-full">
                        <div class="text-left pl-1 font-bold text-[12px] text-[#555] dark:text-gray-400 pt-1.5">
                            {{ t('board.content') || '내용' }}
                        </div>
                        <div class="border border-legacy-border flex flex-col dark:border-zinc-600 h-full">
                            <!-- Editor Toolbar -->
                            <div
                                class="h-[32px] bg-gray-100 border-b border-legacy-border flex items-center px-2 gap-1 dark:bg-zinc-700 dark:border-zinc-600 flex-shrink-0">
                                <select v-model="editorMode"
                                    class="h-[22px] text-[11px] border border-gray-300 rounded px-1 dark:bg-zinc-600 dark:border-zinc-500 dark:text-gray-200">
                                    <option>HTML</option>
                                    <option>TEXT</option>
                                </select>
                                <div class="w-[1px] h-[16px] bg-gray-300 mx-1 dark:bg-zinc-500"></div>

                                <!-- Text Formatting -->
                                <button @click="applyFormat('bold')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.bold') || '굵게'">
                                    <Bold class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>
                                <button @click="applyFormat('italic')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.italic') || '기울임'">
                                    <Italic class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>
                                <button @click="applyFormat('underline')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.underline') || '밑줄'">
                                    <Underline class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>

                                <div class="w-[1px] h-[16px] bg-gray-300 mx-1 dark:bg-zinc-500"></div>

                                <!-- Alignment -->
                                <button @click="applyFormat('alignLeft')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.align_left') || '왼쪽 정렬'">
                                    <AlignLeft class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>
                                <button @click="applyFormat('alignCenter')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.align_center') || '가운데 정렬'">
                                    <AlignCenter class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>
                                <button @click="applyFormat('alignRight')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.align_right') || '오른쪽 정렬'">
                                    <AlignRight class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>

                                <div class="w-[1px] h-[16px] bg-gray-300 mx-1 dark:bg-zinc-500"></div>

                                <!-- Lists -->
                                <button @click="applyFormat('insertUnorderedList')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.list') || '글머리 기호'">
                                    <List class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>
                                <button @click="applyFormat('insertOrderedList')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.ordered_list') || '번호 매기기'">
                                    <ListOrdered class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>

                                <div class="w-[1px] h-[16px] bg-gray-300 mx-1 dark:bg-zinc-500"></div>

                                <!-- Insert -->
                                <button @click="applyFormat('createLink')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.link') || '링크'">
                                    <Link2 class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>
                                <button @click="applyFormat('insertImage')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.image') || '이미지'">
                                    <Image class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>
                                <button @click="applyFormat('formatBlock')"
                                    class="h-[22px] w-[22px] flex items-center justify-center hover:bg-gray-200 dark:hover:bg-zinc-600 rounded"
                                    :title="t('board.toolbar.code') || '코드'">
                                    <Code class="h-3.5 w-3.5 text-gray-700 dark:text-gray-300" />
                                </button>
                            </div>

                            <!-- Content Area -->
                            <textarea v-model="content"
                                class="flex-1 resize-none p-4 outline-none text-[13px] leading-relaxed dark:bg-zinc-800 dark:text-white h-full"
                                :placeholder="t('board.content_placeholder')"></textarea>

                            <!-- Character Count -->
                            <div
                                class="text-right text-[11px] text-gray-500 dark:text-gray-400 px-2 py-1 bg-gray-50 dark:bg-zinc-800 border-t border-legacy-border dark:border-zinc-600">
                                {{ charCount }} {{ t('common.characters') }}
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
