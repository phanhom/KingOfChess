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
                        <tr v-for="(user, idx) in users" :key="user.id" @mouseover="showUserPhoto(user.id)"
                            @mouseleave="hideUserPhoto">
                            <td class="userid">{{ currentPage * 20 - 20 + idx + 1 }}</td>
                            <td class="username" :style="getRatingStyle(user.rating)">
                                <span>{{ user.username }}</span>
                            </td>
                            <td class="description">{{ user.description }}</td>
                            <td class="rating">{{ user.rating }}</td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="pagination-controls">
                <button class="btn btn-outline-primary" :disabled="currentPage === 1"
                    @click="goToPage(currentPage - 1)">
                    &lt;
                </button>
                <span>{{ currentPage }} / {{ totalPages }}</span>
                <button class="btn btn-outline-primary" :disabled="currentPage === totalPages"
                    @click="goToPage(currentPage + 1)">
                    &gt;
                </button>
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
import { onMounted, ref, reactive } from 'vue';
import axios from 'axios';
import { useStore } from 'vuex';

let users = ref([]);
const store = useStore();
let photoUrl = ref(null);
let hoverTimeout = ref(null);
let time_cnt = ref(0)
let photoPosition = reactive({ top: 10, left: 10 });

const user_count = ref(0);
let currentPage = ref(1); // Current page
let totalPages = ref(1); // Total number of pages


// Handle hover event to show photo after 1 second
const showUserPhoto = async (id) => {
    hoverTimeout.value = setTimeout(async () => {
        const res = await axios.post('http://127.0.0.1:3000/user/account/photo/userid', {
            id: id,
        }, {
            headers: {
                Authorization: `Bearer ${store.state.user.token}`,
            }
        });
        photoUrl.value = res.data.photo; // Assuming response contains the URL of the photo
    }, 700); // 1 second delay
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

const fetchData = async (page) => {
    const res = await axios.get('http://127.0.0.1:3000/user/ranklist', {
        params: { page: page }
    });
    users.value = res.data;
};

const fetchUserCount = async () => {
    user_count.value = await axios.get('http://127.0.0.1:3000/user/ranklist/count');
    totalPages.value = Math.ceil(user_count.value.data / 20); // Calculate total pages based on count and items per page
};


// Handle page change
const goToPage = (page) => {
    if (page >= 1 && page <= totalPages.value) {
        currentPage.value = page;
        fetchData(page);
    }
};

onMounted(async () => {
    // user_count.value = await axios.get('http://127.0.0.1:3000/user/ranklist/count');
    // totalPages.value = Math.ceil(user_count.value.data / 10);
    // console.log(user_count.value.data)
    // const res = await axios.get('http://127.0.0.1:3000/user/ranklist', {
    //     params: { page: 1 }
    // });
    // users.value = res.data;
    await fetchUserCount();
    await fetchData(currentPage.value);
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

/* .card-body {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
    min-height: 600px;
} */

.card-body {
    display: flex;
    flex-direction: column; /* Align items vertically */
    justify-content: flex-start; /* Align the table to the top */
    align-items: center;
    height: 100%;
    min-height: 600px; /* Ensure card has a minimum height */
}

.description {
    max-width: 50vh;
    word-break: break-word;
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
    background: rgba(0, 0, 0, 0.5);
    border-radius: 5px;
    transition: opacity 0.3s ease-in-out;
}

.photo-popup img {
    width: 100px;
    height: 100px;
    object-fit: cover;
}

.pagination-controls {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-top: 20px;
    margin-bottom: 20px;
}

.pagination-controls button {
    margin: 0 10px;
    padding: 8px 12px;
    font-size: 1rem;
    border-radius: 5px;
}

.pagination-controls button:disabled {
    background-color: #f0f0f0;
    cursor: not-allowed;
}

.pagination-text {
    font-size: 1.2rem;
    font-weight: 600;
}
</style>