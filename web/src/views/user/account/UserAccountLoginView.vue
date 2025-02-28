<template>
    <ContentField>
        <div class="row justify-content-md-center">
            <div class="col-3">
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
// console.log('PkIndexView')

const username = ref('')
const password = ref('')
let error_message = ref('')
const store = useStore()

const login = async () => {
    // console.log('login')
    // console.log(username.value)
    // console.log(password.value)

    // 登录
    const res = await store.dispatch('login', {
        username: username.value,
        password: password.value,
    });
    console.log(res);

    if(!res.success) {
        error_message.value = res.message;
    }
}



</script>

<style scoped>
button {
    width: 100%;
}

.error_message {
    color: red;
}
</style>