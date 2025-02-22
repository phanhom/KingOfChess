<template>
  <nav class="navbar navbar-expand-lg bg-body-tertiary">
    <div class="container">
      <!-- <a class="navbar-brand" href="/">KOB</a> -->
      <router-link to="/" class="navbar-brand">
        <img src="@/assets/images/snake.ico" alt="Logo" width="30" height="24" class="d-inline-block align-text-top">
        KOB
      </router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarText"
        aria-controls="navbarText" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarText">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <!-- <a class="nav-link" aria-current="page" href="/pk">对战</a> -->
            <router-link to="/pk" :class="routeName == 'pk_index' ? 'nav-link active' : 'nav-link'">对战</router-link>
          </li>
          <li class="nav-item">
            <!-- <a class="nav-link" href="/record">对局列表</a> -->
            <router-link to="/record"
              :class="routeName == 'record_index' ? 'nav-link active' : 'nav-link'">对局列表</router-link>
          </li>
          <li class="nav-item">
            <!-- <a class="nav-link" href="/ranklist">排行榜</a> -->
            <router-link to="/ranklist"
              :class="routeName == 'ranklist_index' ? 'nav-link active' : 'nav-link'">排行榜</router-link>
          </li>
        </ul>
        <ul class="navbar-nav">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
              phanhom
            </a>
            <ul class="dropdown-menu">
              <!-- <li><a class="dropdown-item" href="/user/bot">我的 Bot</a></li> -->
              <li><a class="dropdown-item" href="/user/bot">我的 Bot</a></li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <!-- <li><a class="dropdown-item" href="/user/bot">退出</a></li> -->
              <li><a class="dropdown-item" href="/user/bot">退出</a></li>
            </ul>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { computed } from 'vue'

const route = useRoute();
let routeName = computed(() => {
  return route.name;
});

</script>


<style scoped></style>