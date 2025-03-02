<template>
    <ContentField>
        <div class="not-found">
            <h1>404</h1>
            <p>抱歉，您访问的页面不存在。</p>
            <router-link to="/" class="btn">返回首页</router-link>
        </div>
    </ContentField>
</template>

<script setup>
import ContentField from '@/components/ContentField.vue'
</script>

<style scoped>
.not-found {
    background-color: white;
    border-radius: 12px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    text-align: center;
    padding: 40px;
}

h1 {
    font-size: 6rem;
    font-weight: bold;
    color: #ff4757;
    margin: 0;
}

p {
    font-size: 1.5rem;
    color: #666;
    margin: 10px 0 20px;
}

.btn {
    padding: 10px 20px;
    font-size: 1.2rem;
    color: white;
    background-color: #007bff;
    border-radius: 5px;
    text-decoration: none;
    transition: background 0.3s;
}

.btn:hover {
    background-color: #0056b3;
}
</style>
