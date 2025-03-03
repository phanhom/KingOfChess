<template>
    <div ref="parent" class="gamemap">
        <!-- 左侧 Plane0 信息 -->
        <div class="card" style="width: 20%;">
            <div class="row">
                <div class="col-6">
                    <img src="https://userpic.codeforces.org/no-title.jpg" class="card-img-top" alt="..."
                        style="width: 100%;">
                </div>
                <div class="col-6">
                    <p class="card-text">姓名，最高bot胜率</p>
                </div>
            </div>
            <div class="card-body">
                <h5 class="card-title">蓝方</h5>
                <p class="card-text">分数: {{ plane0_score }}</p>
            </div>
        </div>
        <canvas ref="canvas" tabindex="0"></canvas>
        <!-- 右侧 Plane1 信息 -->
        <div class="card" style="width: 20%;">
            <div class="row">
                <div class="col-6">
                    <img src="https://userpic.codeforces.org/no-title.jpg" class="card-img-top" alt="..."
                        style="width: 100%;">
                </div>
                <div class="col-6">
                    <p class="card-text">姓名，最高bot胜率</p>
                </div>
            </div>
            <div class="card-body">
                <h5 class="card-title">红方</h5>
                <p class="card-text">分数: {{ plane1_score }}</p>
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
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { GameMap } from '@/assets/scripts/GameMap.js'
import { Modal } from 'bootstrap/dist/js/bootstrap'

const parent = ref(null);
const canvas = ref(null);
const game_map = ref(null);

// 响应式数据存储飞机信息
// 左右两个card上显示作者头像名字，bot名字，rating?
// 显示死亡信息，并且定时从新开始显示一个按钮，点击之后重新开始游戏? 
const winner = ref("");
const plane0_score = ref(0);
const plane1_score = ref(0);


onMounted(() => {
    game_map.value = new GameMap(canvas.value.getContext('2d'), parent.value);
    console.log(game_map.value)

    setInterval(() => {
        if (game_map.value) {
            const [plane0, plane1] = game_map.value.planes;

            // 更新飞机分数
            plane0_score.value = plane0.score;
            plane1_score.value = plane1.score;

            // 不能重新开始，而是等时间结束才重新开始?
            if (winner.value == "" && (plane0.status == "dead" || plane1.status == "dead")) {
                if (plane0.status == "dead" && plane1.status == "dead") {
                    winner.value = "平局, 没有玩家";
                } else {
                    winner.value = plane0.status == "dead" ? "红方" : "蓝方";
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
    margin: 10px;
}
</style>