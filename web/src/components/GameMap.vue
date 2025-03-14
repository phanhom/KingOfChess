<template>
    <div ref="parent" class="gamemap">
        <!-- 左侧 Plane0 信息 -->
        <div class="column" style="width: 20%;">
            <div class="card" style="width: 100%;">
                <div class="row">
                    <div class="col-5">
                        <img :src="store.state.user.photo" class="card-img-top" alt="...">
                    </div>
                    <div class="col-6">
                        <p class="card-text">用户名：{{ store.state.user.username }}</p>
                        <p>天梯：{{ store.state.pk.opponent_rating }}</p>
                    </div>
                </div>
                <div class="card-body">
                    <h5 class="card-title" style="color: blue;" v-if="store.state.user.id == store.state.pk.p1_id">蓝方
                    </h5>
                    <h5 class="card-title" style="color: red;" v-else>红方</h5>
                    <p class="card-text" v-if="store.state.user.id == store.state.pk.p1_id">分数: {{
                        store.state.pk.p1_score }}</p>
                    <p class="card-text" v-else>当前分数: {{ store.state.pk.p2_score }}</p>
                </div>
            </div>

            <section class="section">
                <div class="card chat-card" id="chat2">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">聊天室</h5>
                    </div>
                    <div class="card-body chat-body" ref="chatContainer">
                        <div v-for="(msg, index) in chat_history" :key="index"
                            :class="['chat-message', msg.userid === store.state.user.id ? 'self-message' : 'other-message']">
                            <img :src="msg.userid === store.state.user.id ? store.state.user.photo : store.state.pk.opponent_photo"
                                alt="avatar" class="avatar">
                            <div class="message">
                                <p class="small">{{ msg.message }}</p>
                                <!-- <p class="timestamp">{{ new Date().toLocaleTimeString() }}</p> -->
                            </div>
                        </div>
                    </div>
                    <div class="card-footer d-flex align-items-center">
                        <input v-model="chat_input" type="text" class="form-control chat-input" placeholder="输入消息..."
                            @keydown.enter="send_message">
                        <button type="button" class="btn btn-primary ms-2 send-btn" @click="send_message">
                            <img src="@/assets/images/snake.ico" alt="发送" class="plane-icon">
                        </button>
                    </div>
                </div>
            </section>
        </div>
        <canvas ref="canvas" tabindex="0"></canvas>
        <!-- 右侧 Plane1 信息 -->
        <div class="card" style="width: 20%;">
            <div class="row">
                <div class="col-5">
                    <img :src="store.state.pk.opponent_photo" class="card-img-top" alt="...">
                </div>
                <div class="col-6">
                    <p class="card-text">用户名：{{ store.state.pk.opponent_username }}</p>
                    <p>天梯：{{ store.state.pk.opponent_rating }}</p>
                </div>
            </div>
            <div class="card-body">
                <h5 class="card-title" style="color: red;" v-if="store.state.user.id == store.state.pk.p1_id">红方</h5>
                <h5 class="card-title" style="color: blue;" v-else>蓝方</h5>
                <p class="card-text" v-if="store.state.user.id == store.state.pk.p1_id">当前分数: {{ store.state.pk.p2_score
                }}</p>
                <p class="card-text" v-else>当前分数: {{ store.state.pk.p1_score }}</p>
            </div>
        </div>
        <!-- 游戏结束弹出框 -->
        <div class="modal fade" id="gameOverModal" tabindex="-1" aria-hidden="true">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">游戏结束</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body">
                        <p>{{ winner }} 获胜！</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-primary" @click="restartGame">重新开始</button>
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">关闭</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <div class="progress">
        <div class="progress-bar" role="progressbar"
            :style="{ width: (store.state.pk.time_used / store.state.pk.total_time) * 100 + '%' }"
            aria-valuenow="store.state.pk.time_used" aria-valuemin="0" :aria-valuemax="store.state.pk.total_time">
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { GameMap } from '@/assets/scripts/GameMap.js'
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { useStore } from 'vuex';

const parent = ref(null);
const canvas = ref(null);
const game_map = ref(null);
const store = useStore();

// 到时候便变成国际象棋?
// 响应式数据存储飞机信息
// 左右两个card上显示作者头像名字，bot名字，rating?
// 显示死亡信息，并且定时从新开始显示一个按钮，点击之后重新开始游戏? 
const winner = ref("");
const plane0_score = ref(0);
const plane1_score = ref(0);
const chat_history = ref([]);
const chat_input = ref("");
const chatContainer = ref(null);


const send_message = () => {
    if (chat_input.value == "") return;
    if (store.state.pk.p1_id == store.state.user.id) {
        store.commit("send_message", {
            from_id: store.state.pk.p1_id,
            to_id: store.state.pk.p2_id,
            msg: chat_input.value
        });
    } else {
        store.commit("send_message", {
            from_id: store.state.pk.p2_id,
            to_id: store.state.pk.p1_id,
            msg: chat_input.value
        });
    }
    chat_history.value.push({
        userid: store.state.user.id,
        message: chat_input.value,
    });
    setTimeout(() => {
        if (chatContainer.value) {
            chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
        }
    }, 100);
    chat_input.value = "";
}

onMounted(() => {
    game_map.value = new GameMap(canvas.value.getContext('2d'), parent.value, store);
    store.commit("updateGameObj", game_map.value)
    // console.log(game_map.value)

    setInterval(() => {
        if (game_map.value) {
            const [plane0, plane1] = game_map.value.planes;

            // 更新飞机分数
            plane0_score.value = plane0.score;
            plane1_score.value = plane1.score;

            // 不能重新开始，而是等时间结束才重新开始?
            if (winner.value == "" && store.state.pk.result != "none") {
                if (store.state.pk.result == "deuce") {
                    winner.value = "平局, 没有玩家";
                } else {
                    winner.value = store.state.pk.result == "p2" ? "红方" : "蓝方";
                }
                console.log("游戏结束");
                showModal();
            }
        }
        if (store.state.pk.new_message != '') {
            if (store.state.pk.p1_id == store.state.user.id) {
                chat_history.value.push({
                    userid: store.state.pk.p2_id,
                    message: store.state.pk.new_message
                });
            } else {
                chat_history.value.push({
                    userid: store.state.pk.p1_id,
                    message: store.state.pk.new_message
                });
            }
            store.state.pk.new_message = '';
            setTimeout(() => {
                if (chatContainer.value) {
                    chatContainer.value.scrollTop = chatContainer.value.scrollHeight;
                }
            }, 100);
        }
    }, 100);
});


// 显示模态框
const showModal = () => {
    const modal = new Modal(document.getElementById('gameOverModal'));
    modal.show();
};

// 重新开始游戏
const restartGame = () => {
    window.location.reload();  // 直接刷新页面
};
</script>

<style scoped>
canvas:focus {
    outline: none;
}

.column {
    width: 20%;
    display: flex;
    flex-direction: column;
    /* 子元素垂直排列 */
    height: 100%;
    /* 确保有足够的高度 */
    gap: 20px;
    /* 可选，增加顶部和底部元素之间的间距 */
}

div.gamemap {
    /* position: relative; */
    /* background-color: aqua; */
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: flex-start;
    flex-direction: row;
    gap: 20px;
}

.row {
    margin-top: 15px;
    margin-left: 5px;
    height: 100px;
    /* margin: 10px; */
}

.card-img-top {
    width: 90px;
    height: 90px;
    border-radius: 50%;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
}

.progress {
    width: 100%;
    height: 20px;
    background-color: #e0e0e0;
    border-radius: 10px;
    overflow: hidden;
}

.progress-bar {
    height: 100%;
    background-color: #007bff;
    transition: width 0.1s linear;
}

.section {
    flex-grow: 1;
    /* 让 section 填满剩余空间 */
    display: flex;
    flex-direction: column;
}

.chat-card {
    width: 100%;
    height: 40vh;
    border-radius: 7px;
    box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
    /* overflow: hidden; */
}

.chat-body {
    max-height: 35vh;
    overflow-y: auto;
    scrollbar-width: thin;
    /* Firefox 滚动条样式 */
    padding: 15px;
    background-color: #f8f9fa;
}

.card-footer {
    display: flex;
    align-items: center;
}


.chat-message {
    display: flex;
    align-items: flex-start;
    margin-bottom: 10px;
}

.self-message {
    flex-direction: row-reverse;
    text-align: right;
}

.message {
    max-width: 70%;
    padding: 7px 10px;
    background-color: #e3f2fd;
    border-radius: 10px;
    margin-right: 7px;
    margin-left: 7px;
    margin-top: 5px;

    display: flex;
    align-items: center;
    /* 垂直居中 */

    height: 100%;
    /* 确保消息框高度充满内容区域 */
    word-break: break-word;
    /* 防止长单词撑破布局 */
    white-space: pre-wrap;
    /* 保留空格和换行 */
}

.message p {
    margin: 0;
    /* 清除 p 标签默认的上下边距 */
    padding: 0;
    /* 清除 p 标签的默认内边距 */
    display: inline-block;
    /* 让 p 标签内的内容成为行内块级元素，保持垂直居中 */
}

.self-message .message {
    background-color: #d1e7dd;
}

.avatar {
    width: 40px;
    height: 40px;
    border-radius: 50%;
}

.timestamp {
    font-size: 0.75rem;
    color: #999;
    margin-top: 2px;
}

.chat-input {
    border-radius: 20px;
    padding: 8px 15px;
}

.send-btn {
    background-color: #007bff;
    border: none;
    border-radius: 50%;
    width: 40px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 3px 10px rgba(0, 0, 0, 0.2);
    transition: background-color 0.3s ease;
}

.send-btn:hover {
    background-color: #0056b3;
}

.plane-icon {
    width: 18px;
    /* 控制SVG图标大小 */
    height: 18px;
    filter: invert(1);
    /* 将图标变成白色 */
}
</style>