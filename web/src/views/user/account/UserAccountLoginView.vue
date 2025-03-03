<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <h2 class="title">登录</h2>
            <div class="col-10">
                <form @submit.prevent="login">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="error_message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">提交</button>
                </form>
            </div>
        </div>
    </ContentField>
</template>

<script setup>
import ContentField from '@/components/ContentField.vue'
import { ref } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
// console.log('PkIndexView')

const username = ref('')
const password = ref('')
let error_message = ref('')
const store = useStore()
const router = useRouter()

const login = async () => {
    // console.log('login')
    // console.log(username.value)
    // console.log(password.value)

    // 登录
    store.dispatch('login', {
        username: username.value,
        password: password.value,
        success: (res) => {
            store.dispatch('getinfo', {
                success: (res) => {
                    console.log('登录成功', res);
                    router.push({ name: 'home' });
                },
                error: (res) => {
                    error_message.value = "获取用户信息失败";
                    console.log('获取用户信息失败', res);
                }
            });
        },
        error: (res) => {
            error_message.value = "用户名或密码错误";
            console.log('用户名或密码错误', res);
        }
    });
}



</script>

<style scoped>
div.row {
    margin: 0 auto;
    width: 40%;
    padding: 20px;
    border-radius: 12px;
    background-color: white;
    box-shadow: 0 0 20px rgba(0, 0, 0, 0.2);
}
button {
    width: 100%;
}
.title {
    text-align: center;
}

.error_message {
    color: red;
}
</style>