<template>
    <div ref="parent" class="gamemap">
        <!-- 左侧 Plane0 信息 -->
        <div class="card" style="width: 20%;">
            <div class="row">
                <div class="col-6">
                    <img :src="store.state.user.photo" class="card-img-top" alt="...">
                </div>
                <div class="col-6">
                    <p class="card-text">姓名，最高bot胜率</p>
                    <h5 style="color: red;">5连胜</h5>
                </div>
            </div>
            <div class="card-body">
                <h5 class="card-title" v-if="store.state.user.id == store.state.pk.p1_id">蓝方</h5>
                <h5 class="card-title" v-else>红方</h5>
                <p class="card-text" v-if="store.state.user.id == store.state.pk.p1_id">分数: {{ store.state.pk.p1_score }}</p>
                <p class="card-text" v-else>分数: {{ store.state.pk.p2_score }}</p>
            </div>
        </div>
        <canvas ref="canvas" tabindex="0"></canvas>
        <!-- 右侧 Plane1 信息 -->
        <div class="card" style="width: 20%;">
            <div class="row">
                <div class="col-6">
                    <img :src="store.state.pk.opponent_photo" class="card-img-top" alt="...">
                </div>
                <div class="col-6">
                    <p class="card-text">姓名，最高bot胜率</p>
                    <h5 style="color: red;">5连胜</h5>
                </div>
            </div>
            <div class="card-body">
                <h5 class="card-title" v-if="store.state.user.id == store.state.pk.p1_id">红方</h5>
                <h5 class="card-title" v-else>蓝方</h5>
                <p class="card-text" v-if="store.state.user.id == store.state.pk.p1_id">分数: {{ store.state.pk.p2_score }}</p>
                <p class="card-text" v-else>分数: {{ store.state.pk.p1_score }}</p>
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
        aria-valuenow="store.state.pk.time_used"
        aria-valuemin="0"
        :aria-valuemax="store.state.pk.total_time">
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

</style>