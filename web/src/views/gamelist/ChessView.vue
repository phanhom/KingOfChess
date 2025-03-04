<template>
  <div class="playground">
    <div class="controls">
      <button @click="boardAPI?.toggleOrientation()">切换视角</button>
      <button @click="boardAPI?.resetBoard()">重置棋盘</button>
      <button @click="boardAPI?.undoLastMove()">撤销上一步</button>
      <button @click="boardAPI?.toggleMoves()">显示威胁</button>
    </div>

    <TheChessboard
      :board-config="boardConfig"
      @board-created="(api) => (boardAPI = api)"
      @move="handleMove"
      @checkmate="handleCheckmate"
      @stalemate="handleStalemate"
      @draw="handleDraw"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { TheChessboard } from 'vue3-chessboard';
import 'vue3-chessboard/style.css';

let boardAPI = ref(null);

const boardConfig = {
  coordinates: true, // 显示坐标
};

function handleMove(move) {
  console.log("玩家移动:", move);
}

function handleCheckmate(isMated) {
  alert(isMated === 'w' ? "黑方胜利！" : "白方胜利！");
}

function handleStalemate() {
  alert("平局：僵局！");
}

function handleDraw() {
  alert("平局！");
}
</script>

<style scoped>
canvas:focus {
  outline: none;
}

.playground {
  width: 80vw;
  height: 70vh;
  margin: 50px auto;
  text-align: center;
}

.controls {
  margin-bottom: 10px;
}

button {
  margin: 5px;
  padding: 8px 12px;
  font-size: 16px;
  cursor: pointer;
}
</style>
