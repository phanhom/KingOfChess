<template>
  <PlayGround v-if="store.state.pk.status === 'playing'">
  </PlayGround>
  <MatchGround v-if="store.state.pk.status != 'playing'">
  </MatchGround>
  <div class="online-count">
    <span class="dot"></span>
    在线人数：{{ store.state.pk.online_count }}
  </div>
</template>

<script setup>
import PlayGround from '@/components/PlayGround.vue'
import MatchGround from '@/components/MatchGround.vue';
import { onMounted, onUnmounted, ref } from 'vue';
import { useStore } from 'vuex';

const store = useStore();
const socketUrl = `ws://47.96.154.38:3000/websocket/${store.state.user.token}`;

let socket = null;

onMounted(() => {

  socket = new WebSocket(socketUrl);
  socket.onopen = () => {
    // console.log("WebSocket连接成功");
    store.commit('updateSocket', socket);
  }
  socket.onmessage = (msg) => {
    const data = JSON.parse(msg.data);
    // console.log(data);
    if (data.event == 'matched') {
      store.commit('updateOpponentInfo', {
        username: data.opponent_username,
        photo: data.opponent_photo,
        rating: data.opponent_rating,
      });
      store.commit('updateGameMap', data.game_map);
      store.commit('updateStatus', data.event);
      setTimeout(() => {
        store.state.pk.status = "playing";
      }, 2000);
    } else if (data.event == 'move') {
      store.commit('updateGame', data.gamedata);
      // console.log(store.state.pk.game_obj.planes);
      if (data.gamedata.p1_direction != null && data.gamedata.p2_direction != null && data.gamedata.p1_direction != undefined && data.gamedata.p2_direction != undefined && store.state.pk.game_obj != null) {
        const [p1, p2] = store.state.pk.game_obj.planes;
        // console.log(p1);
        p1.set_direction(data.gamedata.p1_direction)
        p2.set_direction(data.gamedata.p2_direction)
      }
      // console.log(data.gamedata);
    } else if (data.event == 'shoot') {
      store.commit('updateBullets', data.bullet)
    } else if (data.event == 'game_over') {
      store.commit('updateResult', data.result);
    } else if (data.event == 'candy') {
      store.commit('updateCandy', data.candy)
    } else if (data.event == 'time_update') {
      store.commit('updateTime', data.time)
    } else if (data.event == 'update_score') {
      store.commit('updateScore', data.score)
    } else if (data.event == 'chat_message') {
      store.commit('updateChatMessage', data.chat) // ????
    } else if (data.event == 'heartbeat') {
      store.commit('updateOnlineCount', data.data)
    } else if (data.event == 'result') {
      store.commit('updateRatingChange', data.change)
    }
  }
});
onUnmounted(() => {
  socket.onclose = () => {
    // console.log("WebSocket关闭");
  };
  socket.close();
  store.state.pk.status = "matching";
});
</script>

<style scoped>
.online-count {
  position: fixed;
  bottom: 10px;
  right: 10px;
  background-color: rgba(0,0,0,0.2);
  color: #fff;
  padding: 8px 15px;
  border-radius: 20px;
  box-shadow: 0 0px 5px rgba(0, 0, 0, 0.5);
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 5px;
  z-index: 10;
}

.dot {
  width: 10px;
  height: 10px;
  background-color: #00ff00;
  border-radius: 50%;
  box-shadow: 0 0 8px #00ff00;
}
</style>