<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-10">
                <h2 class="title">注册</h2>
                <form @submit.prevent="register">
                    <div class="mb-3">
                        <label for="username" class="form-label">用户名</label>
                        <input v-model="username" type="text" class="form-control" id="username" placeholder="请输入用户名">
                    </div>
                    <div class="mb-3">
                        <label for="password" class="form-label">密码</label>
                        <input v-model="password" type="password" class="form-control" id="password" placeholder="请输入密码">
                    </div>
                    <div class="mb-3">
                        <label for="confirmedPassword" class="form-label">确认密码</label>
                        <input v-model="confirmedPassword" type="password" class="form-control" id="confirmedPassword" placeholder="请再次输入密码">
                    </div>
                    <div class="error_message">{{ error_message }}</div>
                    <button type="submit" class="btn btn-primary">
                        <span v-if="loading" class="spinner-border spinner-border-sm" role="status" aria-hidden="true"></span>
                        {{ loading ? '' : '注册' }}
                    </button>
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
import axios from "axios"
import { debounce } from "lodash";
// 创建提交图片的时候必须以png jpg jpeg结尾

const username = ref('')
const password = ref('')
const confirmedPassword = ref('')
let error_message = ref('')
const router = useRouter()

const loading = ref(false) // 控制加载状态

const register = async () => {
    if(loading.value==true) return;
    loading.value = true; // 开始加载，禁用按钮
    // 注册
    // console.log(store.state.user.token);
    const res = await axios.post('http://47.96.154.38:3000/api/user/account/register', {
        username: username.value,
        password: password.value,
        confirmedPassword: confirmedPassword.value,
    });
    if(res.data.error_message == "success") {
        router.push({ name: 'user_account_login' });
        alert("注册成功");
        // console.log('注册成功', res);
    } else {
        error_message.value = res.data.error_message;
        // console.log(res.data.error_message);
    }
    // console.log('注册', res);
    loading.value = false; // 请求结束，恢复按钮可用
}

// const debouncedRegister = debounce(register, 2000);

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