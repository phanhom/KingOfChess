<template>
    <div class="matchground">
        <div class="card">
            <div class="avatar-wrapper">
                <div class="player-info">
                    <img :src="store.state.user.photo" class="avatar" alt="Player Avatar">
                    <p class="username" :style="getRatingStyle(store.state.user.rating)" v-if="before_matching || matched">{{ store.state.user.username }}</p>
                    <p class="rating" v-if="before_matching || matched">rating: {{ store.state.user.rating }}</p>
                </div>

                <div v-if="isMatching" class="loading-spinner"></div>
                <p v-if="matched">-</p>

                <div class="opponent-info" v-if="matched">
                    <img :src="store.state.pk.opponent_photo" class="avatar" alt="Player Avatar">
                    <p class="username" :style="getRatingStyle(store.state.pk.opponent_rating)">{{ store.state.pk.opponent_username }}</p>
                    <p class="rating">rating: {{ store.state.pk.opponent_rating }}</p>
                </div>
            </div>
            <p v-if="isMatching"> </p>
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
const before_matching = ref(true);
const isMatching = ref(false);
const matched = ref(false);
const matchTime = ref(0);
let timer = null;
const matchSound = new Audio('/sounds/letsroll.mp3'); // 替换为实际的音频文件路径

const toggleMatching = () => {
    before_matching.value = !before_matching.value;
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
                before_matching.value = false;
                isMatching.value = false;
                matched.value = true;
                // store.state.pk.status = "playing"   // 进入之后直接在那个页面写等5s才开始游戏
                // 可以写成英雄联盟那样，就是匹配成功后给10s点击确认，不点击就要等1min再进行下次匹配?
                clearInterval(timer);
            }
        }, 1000);
    } else {
        before_matching.value = true;
        store.state.pk.socket.send(JSON.stringify({
            event: 'stop-matching',
        }))
        clearInterval(timer);
    }
};

const getRatingStyle = (rating) => {
    if (rating <= 1500) {
        return { color: '#aaaaaa' };
    } else if (rating <= 1550) {
        return { color: '#00bcd4' }; // 青色
    } else if (rating <= 1650) {
        return { color: '#2F14EC' }; // 蓝色
    } else if (rating <= 1750) {
        return { color: '#C600FF' }; // 紫色
    } else if (rating <= 1850) {
        return { color: '#FBBB00' }; // 黄色
    } else if (rating <= 2000) {
        return { color: '#FF2A19' }; // 红色
    } else {
        return { color: '#000000' }; // 2000以上
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
    margin-bottom: 10px;
    gap: 30px;
}

.avatar {
    width: 100px;
    height: 100px;
    border-radius: 50%;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);

    object-fit: cover;         /* 等比缩放并裁剪，确保图片铺满容器 */
    object-position: center;   /* 将图片的中心部分作为显示区域 */
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

.username {
    font-weight: bold;
    font-size: 20px;
    color: #2c3e50; /* 深蓝色，稳重且突出 */
    text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.15);
    margin-top: 10px;
}

.rating {
    color: #7f8c8d; /* 稍浅的灰色，作为次要信息 */
    font-size: 14px;
    display: flex;
    align-items: center;
    gap: 4px;
}

.rating::before {
    content: "⭐";  /* 星星图标 */
    color: #f1c40f;
    font-size: 16px;
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