<script setup lang="ts">
import { ContextMenuTrigger, type ContextMenuTriggerProps } from "reka-ui";
import { cn } from "@/lib/utils";

const props = defineProps<ContextMenuTriggerProps & { class?: string }>();
</script>

<template>
    <ContextMenuTrigger v-bind="props" :class="cn(props.class)">
        <slot />
    </ContextMenuTrigger>
</template>
