<template>
  <PlayGround v-if="store.state.pk.status === 'playing'">
  </PlayGround>
  <MatchGround v-if="store.state.pk.status != 'playing'">
  </MatchGround>
</template>

<script setup>
import PlayGround from '@/components/PlayGround.vue'
import MatchGround from '@/components/MatchGround.vue';
import { onMounted, onUnmounted, ref } from 'vue';
import { useStore } from 'vuex';

const store = useStore();
const socketUrl = `ws://localhost:3000/websocket/${store.state.user.token}`;

let socket = null;

onMounted(() => {

  socket = new WebSocket(socketUrl);
  socket.onopen = () => {
    console.log("WebSocket连接成功");
    store.commit('updateSocket', socket);
  }
  socket.onmessage = (msg) => {
    const data = JSON.parse(msg.data);
    // console.log(data);
    if (data.event == 'matched') {
      store.commit('updateOpponentInfo', {
        username: data.opponent_username,
        photo: data.opponent_photo,
      });
      store.commit('updateGameMap', data.game_map);
      store.commit('updateStatus', data.event);
      setTimeout(() => {
        store.state.pk.status = "playing";
      }, 2000);
    }
  }
});
onUnmounted(() => {
  socket.onclose = () => {
    console.log("WebSocket关闭");
  };
  socket.close();
  store.state.pk.status = "matching";
});
</script>

<style scoped></style>