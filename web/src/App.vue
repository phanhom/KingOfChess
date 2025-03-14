<template>
  <Navbar></Navbar>
  <router-view></router-view>
</template>

<script setup>
// import { RouterLink, RouterView } from 'vue-router'
import { ref } from 'vue'
import axios from 'axios'
import Navbar from '@/components/NavBar.vue'
import "bootstrap/dist/css/bootstrap.min.css"
import "bootstrap/dist/js/bootstrap.min.js"

</script>


<style>
* {
    font-family: monospace;
    font-size: large;
}
body {
  background-image: url("@/assets/images/background.jpg");
  background-size: cover;
}
</style>
