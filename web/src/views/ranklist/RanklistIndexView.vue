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
                        <tr v-for="user in users" :key="user.id" @mouseover="showUserPhoto(user.id, $event)"
                            @mouseleave="hideUserPhoto">
                            <td class="userid">{{ user.id }}</td>
                            <td class="username" :style="getRatingStyle(user.rating)">
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
    <!-- <div v-if="photoUrl" class="photo-popup">
        <img :src="photoUrl" alt="User Photo" />
    </div> -->
    <div v-show="photoUrl" class="photo-popup"
        :style="{ top: photoPosition.top + 'px', left: photoPosition.left + 'px' }">
        <img :src="photoUrl" alt="User Photo" />
    </div>
</template>

<script setup>
import { onMounted, ref, reactive} from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';

let users = ref([]);
const store = useStore();
let photoUrl = ref(null);
let hoverTimeout = ref(null);
let time_cnt = ref(0)
let photoPosition = reactive({ top: 10, left: 10 });

// Handle hover event to show photo after 1 second
const showUserPhoto = async (id, event) => {
    hoverTimeout.value = setTimeout(async () => {
        const res = await axios.post('http://127.0.0.1:3000/user/account/photo/userid', {
            id: id,
        }, {
            headers: {
                Authorization: `Bearer ${store.state.user.token}`,
            }
        });
        photoUrl.value = res.data.photo; // Assuming response contains the URL of the photo
        // if(photoPosition.top == 0) {
        //     photoPosition.top = event.clientY - 100; // 100px above the cursor
        //     photoPosition.left = event.clientX - 50;
        // }
    }, 500); // 1 second delay
};

const hideUserPhoto = () => {
    clearTimeout(hoverTimeout.value);
    photoUrl.value = null;
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

.username {
    font-weight: bold;
    width: 180px;
    max-width: 300px;
    word-break: break-word;
    white-space: pre-wrap;
}


.rating {
    text-align: right;
}

.photo-popup {
    position: absolute;
    padding: 10px;
    background: rgba(0, 0, 0, 0.7);
    border-radius: 5px;
    transition: opacity 0.3s ease-in-out;
}

.photo-popup img {
    width: 100px;
    height: 100px;
    object-fit: cover;
}
</style>