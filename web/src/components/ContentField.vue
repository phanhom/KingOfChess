<template>
    <div class="container content_field">
        <div class="card">
            <div class="cardbody">
                <slot></slot>
            </div>
        </div>
    </div>
</template>

<script setup>

</script>


<style scoped>
div.content_field {
    margin-top: 20px;
    background-color: transparent;
    width: 80%;
}
div.card{
    background-color: transparent;
    border: none;
}
</style>