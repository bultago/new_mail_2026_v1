<template>
    <div class="h-full flex flex-col">
        <!-- Title Header Removed as per user request -->

        <div class="flex-1 flex overflow-hidden">
            <!-- Sidebar -->
            <div class="w-64 flex-shrink-0">
                <AddressSidebar @select-group="handleGroupSelect" />
            </div>

            <!-- Main Content -->
            <div class="flex-1 min-w-0">
                <AddressList :selected-group="selectedGroup" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useI18n } from 'vue-i18n';
import AddressSidebar from '../../components/addr/AddressSidebar.vue';
import AddressList from '../../components/addr/AddressList.vue';

const { t } = useI18n();

const selectedGroup = ref<{ id: string | number, type: string, name: string }>({ id: 'pg1', type: 'personal', name: '가족' });

const handleGroupSelect = (group: { id: string | number, type: string, name: string }) => {
    selectedGroup.value = group;
};
</script>
