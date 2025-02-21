<template>
  <div id="test">hello world</div>
  <br>
  <div>
    <h1>{{res}}</h1>
  </div>

  <!-- <RouterView /> -->
</template>

<script setup>
// import { RouterLink, RouterView } from 'vue-router'
import { ref } from 'vue'
import { onMounted } from 'vue'
import axios from 'axios'

const res = ref('')

const fetch = async () => {
  try {
      const response = await axios.get('http://localhost:3000/pk');
      res.value = response.data; // 获取响应数据
      console.log(res.value);
  } catch (error) {
      console.error('请求失败:', error);
  }
}
fetch()
// onMounted(() => {
//   fetch()
// })
</script>


<style>
body {
  background-image: url("@/assets/background.jpg");
  background-size: cover;
}
</style>
