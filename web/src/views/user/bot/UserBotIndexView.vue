<template>
    <ContentField>jiqiren</ContentField>
</template>

<script setup>
import ContentField from '@/components/ContentField.vue'
import axios from 'axios';
import { useStore } from 'vuex';

const store = useStore();
const res = axios.post('http://localhost:3000/user/bot/add',
    {
        botName: 'jiqiren',
        description: 'jiqiren',
        content: 'jiqiren',
    },
    {
        headers: {
            Authorization: `Bearer ${store.state.user.token}`,
        }
    }
)
console.log(res)

</script>

<style scoped></style>