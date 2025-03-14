<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container">
      <router-link to="/" class="navbar-brand">
        <img src="@/assets/images/brain.gif" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
        <!-- <img src="@/assets/images/snake.ico" alt="Logo" width="30" height="24" class="d-inline-block align-text-top"> -->
        PHD
      </router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText"
        aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link to="/pk" :class="routeName == 'pk_index' ? 'nav-link active' : 'nav-link'">对战</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/record"
              :class="routeName == 'record_index' ? 'nav-link active' : 'nav-link'">对局列表</router-link>
          </li>
          <li class="nav-item">
            <router-link to="/ranklist"
              :class="routeName == 'ranklist_index' ? 'nav-link active' : 'nav-link'">排行榜</router-link>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              小游戏
            </a>
            <ul class="dropdown-menu">
              <li><router-link class="dropdown-item" to="/gamelist/chess">♟️国际象棋</router-link></li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li><a class="dropdown-item">待开发</a></li>
            </ul>
          </li>

          <li class="nav-item">
            <router-link to="/pk" :class="routeName == 'pk_index' ? 'nav-link active' : 'nav-link'">(内测中，数据请用户自行保存)</router-link>
          </li>
        </ul>
        <ul class="navbar-nav" v-if="user.is_login">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              <img :src="user.photo" alt="User Avatar" class="rounded-circle me-2" width="30" height="30">
              {{ user.username }}
            </a>
            <ul class="dropdown-menu">
              <li><router-link class="dropdown-item" to="/user/bot">个人中心</router-link></li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li><a class="dropdown-item" href="/user/account/login" @click="logout">退出</a></li>
            </ul>
          </li>
        </ul>
        <ul class="navbar-nav" v-else>
          <li class="nav-item">
            <router-link class="nav-link" to="/user/account/login" role="button">
              登录
            </router-link>
          </li>
          <li class="nav-item dropdown">
            <router-link class="nav-link" to="/user/account/register" role="button">
              注册
            </router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { RouterLink, useRoute } from 'vue-router'
import { computed } from 'vue'
import { useStore } from 'vuex'
import 'bootstrap/dist/js/bootstrap.bundle.min.js';


const route = useRoute();
const store = useStore();

let routeName = computed(() => {
  return route.name;
});
let user = computed(() => {
  return store.state.user;
});
const logout = () => {
  store.dispatch('logout');
};

</script>


<style scoped></style>