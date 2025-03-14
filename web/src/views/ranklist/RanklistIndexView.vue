<template>
    <div class="col-9" id="ranklist">
        <div class="card">
            <div class="card-body">
                <table class="table table-hover">
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>用户名</th>
                            <th>简介</th>
                            <th class="rating">Rating</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="user in users" :key="user.id">
                            <td class="userid">{{ user.id }}</td>
                            <td class="username" :style="getRatingStyle(user.rating)">
                                <!-- <span v-if="user.rating > 2000">
                                    <span style="color: black">⭐️</span>
                                    <span style="color: red">{{ user.username }}</span>
                                </span> -->
                                <span>{{ user.username }}</span>
                            </td>
                            <td class="description">{{ user.description }}</td>
                            <td class="rating">{{ user.rating }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, reactive } from 'vue';
import axios from 'axios';

let users = ref([]);

const getRatingStyle = (rating) => {
    if (rating <= 1500) {
        return { color: '#aaaaaa'};
    } else if (rating <= 1550) {
        return { color: '#00bcd4'}; // 青色
    } else if (rating <= 1650) {
        return { color: '#2F14EC'}; // 蓝色
    } else if (rating <= 1750) {
        return { color: '#C600FF'}; // 紫色
    } else if (rating <= 1850) {
        return { color: '#FBBB00'}; // 黄色
    } else if (rating <= 2000) {
        return { color: '#FF2A19'}; // 红色
    } else {
        return { color: '#000000' }; // 2000以上
    }
};

onMounted(async () => {
    const res = await axios.get('http://127.0.0.1:3000/user/ranklist');
    users.value = res.data;
});
</script>

<style scoped>
#ranklist {
    margin: 0 auto;
}

.card {
    width: 100%;
    margin-top: 20px;
}

.card-body {
    display: flex;
    justify-content: center;
    /* 居中 */
    align-items: center;
    /* 居中 */
    height: 100%;
    /* 确保有足够的高度 */
}

.description {
    max-width: 50vh;
    word-break: break-word;
    /* 防止长单词撑破布局 */
    white-space: pre-wrap;
}

.userid {
    width: 50px;
    word-break: break-word;
    white-space: pre-wrap;
}

.username{
    font-weight: bold;
    width: 180px;
    max-width: 300px;
    word-break: break-word;
    white-space: pre-wrap;
}


.rating {
    text-align: right;
}
</style>