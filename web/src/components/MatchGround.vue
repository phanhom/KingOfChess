<template>
    <div class="matchground">
        <div class="card">
            <div class="avatar-wrapper">
                <img :src="store.state.user.photo" class="avatar" alt="Player Avatar">
                <div v-if="isMatching" class="loading-spinner"></div>
                <p v-if="matched">-</p>
                <img v-if="matched" :src="store.state.pk.opponent_photo" class="avatar" alt="Player Avatar">
            </div>
            <button class="match-button" @click="toggleMatching">
                {{ isMatching ? '取消匹配' : '开始匹配' }}
            </button>
            <p v-if="isMatching" class="timer">匹配时间: {{ matchTime }}s</p>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useStore } from 'vuex';
// import store from '../store/index.js'

const store = useStore();
const isMatching = ref(false);
const matched = ref(false);
const matchTime = ref(0);
let timer = null;
const matchSound = new Audio('/sounds/letsroll.mp3'); // 替换为实际的音频文件路径

const toggleMatching = () => {
    isMatching.value = !isMatching.value;
    if (isMatching.value) {
        store.state.pk.socket.send(JSON.stringify({
            event: 'start-matching',
        }));
        matchTime.value = 0;
        timer = setInterval(() => {
            matchTime.value++;
            if (store.state.pk.status === 'matched') {
                matchSound.play();
                isMatching.value = false;
                matched.value = true;
                // store.state.pk.status = "playing"   // 进入之后直接在那个页面写等5s才开始游戏
                // 可以写成英雄联盟那样，就是匹配成功后给10s点击确认，不点击就要等1min再进行下次匹配?
                clearInterval(timer);
            }
        }, 1000);
    } else {
        store.state.pk.socket.send(JSON.stringify({
            event: 'stop-matching',
        }))
        clearInterval(timer);
    }
};

onUnmounted(() => {
    clearInterval(timer);
});
</script>

<style scoped>
div.matchground {
    width: 100vw;
    height: 70vh;
    display: flex;
    justify-content: center;
    align-items: center;
}

.card {
    background: white;
    border-radius: 20px;
    padding: 40px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    text-align: center;
    width: 400px;
}

.avatar-wrapper {
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
    margin-bottom: 30px;
    gap: 30px;
}

.avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.loading-spinner {
    position: absolute;
    width: 110px;
    height: 110px;
    border: 4px solid rgba(0, 0, 0, 0.1);
    border-top-color: #3498db;
    border-radius: 50%;
    animation: spin 1s linear infinite;
}

@keyframes spin {
    from {
        transform: rotate(0deg);
    }

    to {
        transform: rotate(360deg);
    }
}

.match-button {
    width: 100%;
    padding: 10px;
    border: none;
    border-radius: 10px;
    background-color: #3498db;
    color: white;
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.match-button:hover {
    background-color: #2980b9;
}

.timer {
    margin-top: 10px;
    font-size: 14px;
    color: #666;
}
</style>