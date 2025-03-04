<template>
  <PlayGround>
  </PlayGround>
</template>

<script setup>
import PlayGround from '@/components/PlayGround.vue'
import { onMounted, onUnmounted, ref } from 'vue';
import { useStore } from 'vuex';

const store = useStore();
const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}`;

let socket = null;

onMounted(() => {
  socket = new WebSocket(socketUrl);
  socket.onopen = () => {
    console.log("WebSocket连接成功");
  }
  socket.onmessage = (event) => {
    console.log(JSON.parse(event.data));
  }
});
onUnmounted(() => {
  socket.onclose = () => {
    console.log("WebSocket关闭");
  };
  socket.close();
});
</script>

<style scoped></style>