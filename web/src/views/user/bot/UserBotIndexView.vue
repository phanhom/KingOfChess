<template>
    <div class="container">
        <div class="row">
            <div class="col-3" id="profile">
                <div class="card">
                    <img :src="store.state.user.photo" class="card-img-top" alt="..." style="width: 100%;">
                    <div class="card-body">
                        <h5 class="card-title"
                            style="font-size: 1.5rem; font-weight: 600; color: #333; margin-bottom: 5px;">
                            {{ store.state.user.username }}
                        </h5>
                        <p style="font-size: 1rem; color: #666; margin-bottom: 10px;">
                            Rating: {{ store.state.user.rating }}
                        </p>
                        <p class="card-text">{{ store.state.user.description }}</p>

                        <button type="button" class="btn btn-outline-dark" data-bs-toggle="modal"
                            data-bs-target="#changeProfileModal">
                            修改个人信息
                        </button>



                        <!-- Change Profile Modal -->
                        <div class="modal fade" id="changeProfileModal" tabindex="-1"
                            aria-labelledby="changeProfileModalLabel" aria-hidden="true">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h5 class="modal-title" id="changeProfileModalLabel">更改个人资料</h5>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <label for="username" class="form-label">新用户名</label>
                                            <input v-model="modify_info.username" type="text" class="form-control"
                                                id="username" placeholder="请输入新用户名">
                                        </div>
                                        <div class="mb-3">
                                            <label for="description" class="form-label">新简介</label>
                                            <input v-model="modify_info.description" type="text" class="form-control"
                                                id="description" placeholder="请输入新简介">
                                        </div>
                                        <div class="mb-3">
                                            <label for="photo" class="form-label">新头像网址</label>
                                            <input v-model="modify_info.photo" type="text" class="form-control"
                                                id="photo" placeholder="请输入头像图片网址">
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="error-message" style="color: red;">{{ modify_info.error_message }}
                                        </div>
                                        <button type="button" class="btn btn-primary"
                                            @click="update_user_info">提交</button>
                                        <button type="button" class="btn btn-secondary"
                                            data-bs-dismiss="modal">关闭</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="col-9" id="botcontent">
                <div class="card">
                    <div class="card-header">
                        <span style="font-size: 150%;">我的Bot</span>
                        <button type="button" class="btn btn-outline-dark float-end" data-bs-toggle="modal"
                            data-bs-target="#addbot">
                            创建Bot
                        </button>

                        <!-- Modal -->
                        <div class="modal fade" id="addbot" tabindex="-1">
                            <div class="modal-dialog modal-xl">
                                <div class="modal-content">
                                    <div class="modal-header">
                                        <h1 class="modal-title fs-5" id="addbot-title">创建Bot</h1>
                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                            aria-label="Close"></button>
                                    </div>
                                    <div class="modal-body">
                                        <div class="mb-3">
                                            <!--  -->
                                            <label for="addbot-botName" class="form-label">名称</label>
                                            <input v-model="addbot.botName" type="text" class="form-control"
                                                id="addbot-botName" placeholder="请输入Bot名称">
                                        </div>
                                        <!--  -->
                                        <div class="mb-3">
                                            <label for="addbot-description" class="form-label">简介</label>
                                            <textarea v-model="addbot.description" class="form-control"
                                                id="addbot-description" rows="3" placeholder="请输入Bot描述"></textarea>
                                        </div>
                                        <div class="mb-3">
                                            <label for="addbot-content" class="form-label">代码</label>
                                            <!-- <textarea v-model="addbot.content" class="form-control" id="addbot-content"
                                                rows="8" placeholder="请输入Bot代码"></textarea> -->
                                            <VAceEditor v-model:value="addbot.content" lang="c_cpp" theme="textmate"
                                                style="height: 300px" :options="{ fontSize: '16px' }" />
                                        </div>
                                    </div>
                                    <div class="modal-footer">
                                        <div class="error-message" style="color: red;">{{ addbot.error_message }}</div>
                                        <button type="button" class="btn btn-primary" @click="addbot_func">提交</button>
                                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal"
                                            @click="clear_error_message">关闭</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card-body">
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>Rating</th>
                                    <th>BOT ID</th>
                                    <th>名称</th>
                                    <th>简介</th>
                                    <th>创建时间</th>
                                </tr>
                            </thead>
                            <tbody>
                                <tr v-for="bot in bots" :key="bot.id">
                                    <td>{{ bot.rating }}</td>
                                    <td>{{ bot.id }}</td>
                                    <td>{{ bot.botName }}</td>
                                    <td>{{ bot.description }}</td>
                                    <td>{{ bot.createtime }}</td>
                                    <td>
                                        <button type="button" class="btn btn-outline-dark float-end"
                                            data-bs-toggle="modal" :data-bs-target="'#updatebot' + bot.id">修改</button>
                                        <button type="button" class="btn btn-outline-danger float-end"
                                            @click="removebot_func(bot)">删除</button>

                                        <div class="modal fade" :id="'updatebot' + bot.id" tabindex="-1">
                                            <div class="modal-dialog modal-xl">
                                                <div class="modal-content">
                                                    <div class="modal-header">
                                                        <h1 class="modal-title fs-5" id="updatebot-title">修改Bot</h1>
                                                        <button type="button" class="btn-close" data-bs-dismiss="modal"
                                                            aria-label="Close"></button>
                                                    </div>
                                                    <div class="modal-body">
                                                        <div class="mb-3">
                                                            <!--  -->
                                                            <label for="updatebot-botName" class="form-label">名称</label>
                                                            <input v-model="bot.botName" type="text"
                                                                class="form-control" id="updatebot-botName"
                                                                placeholder="请输入Bot名称">
                                                        </div>
                                                        <!--  -->
                                                        <div class="mb-3">
                                                            <label for="updatebot-description"
                                                                class="form-label">简介</label>
                                                            <textarea v-model="bot.description" class="form-control"
                                                                id="updatebot-description" rows="3"
                                                                placeholder="请输入Bot描述">{{ bot.description }}</textarea>
                                                        </div>
                                                        <div class="mb-3">
                                                            <label for="updatebot-content" class="form-label">代码</label>
                                                            <VAceEditor v-model:value="bot.content" lang="c_cpp"
                                                                theme="textmate" style="height: 300px"
                                                                :options="{ fontSize: '16px' }" />
                                                        </div>
                                                    </div>
                                                    <div class="modal-footer">
                                                        <div class="error-message" style="color: red;">{{
                                                            addbot.error_message }}</div>
                                                        <button type="button" class="btn btn-primary"
                                                            @click="updatebot_func(bot)">提交</button>
                                                        <button type="button" class="btn btn-secondary"
                                                            data-bs-dismiss="modal"
                                                            @click="clear_error_message">关闭</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>

</template>

<script setup>
import ContentField from '@/components/ContentField.vue'
import axios from 'axios';
import { useStore } from 'vuex';
import { onMounted, ref, reactive } from 'vue';
import { Modal } from 'bootstrap/dist/js/bootstrap'
import { VAceEditor } from "vue3-ace-editor";
import ace from "ace-builds";
import("ace-builds/src-noconflict/mode-c_cpp"); // C++
import("ace-builds/src-noconflict/theme-textmate"); // 主题

ace.config.set("basePath", "https://cdn.jsdelivr.net/npm/ace-builds@1.4.12/src-noconflict/");

const store = useStore();
let bots = ref([]);
const confirmNum = ref(0);
const addbot = reactive({
    botName: '',
    description: '',
    content: '',
    error_message: '',
});
const modify_info = reactive({
    username: store.state.user.username,
    photo: store.state.user.photo,
    description: store.state.user.description,
    error_message: '',
});

const update_user_info = async () => {
    modify_info.error_message = '';
    const res = await axios.post('http://127.0.0.1:3000/user/account/modifyinfo', {
        username: modify_info.username,
        photo: modify_info.photo,
        description: modify_info.description,
    }, {
        headers: {
            Authorization: `Bearer ${store.state.user.token}`,
        },
    });
    modify_info.error_message = res.data.error_message;
    console.log(res.data);
    if (res.data.error_message == "success") {
        Modal.getInstance('#changeProfileModal').hide();
        document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
        alert("修改成功");
        location.reload();
    }
}

const clear_error_message = () => {
    addbot.error_message = '';
}

const addbot_func = async () => {
    addbot.error_message = '';
    const res = await axios.post('http://127.0.0.1:3000/user/bot/add', {
        botName: addbot.botName,
        description: addbot.description,
        content: addbot.content,
    }, {
        headers: {
            Authorization: `Bearer ${store.state.user.token}`,
        }
    });
    if (res.data.error_message == 'success') {
        addbot.botName = '';
        addbot.description = '';
        addbot.content = '';
        addbot.error_message = '';
        Modal.getInstance('#addbot').hide();
        alert('创建成功');
        document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
        refresh();
    } else {
        addbot.error_message = res.data.error_message;
    }
}

const updatebot_func = async (bot) => {
    addbot.error_message = '';
    console.log(bot.id);
    const res = await axios.post('http://127.0.0.1:3000/user/bot/update', {
        botId: bot.id,
        botName: bot.botName,
        description: bot.description,
        content: bot.content,
    }, {
        headers: {
            Authorization: `Bearer ${store.state.user.token}`,
        }
    });
    if (res.data.error_message == 'success') {
        alert('修改成功');
        Modal.getInstance('#updatebot' + bot.id).hide();
        document.querySelectorAll('.modal-backdrop').forEach(el => el.remove());
        refresh();
    } else {
        addbot.error_message = res.data.error_message;
    }
}

const removebot_func = async (bot) => {
    // if (confirmNum.value < 1) {
    //     confirmNum.value++;
    //     alert('再次点击删除按钮以便删除Bot');
    //     return;
    // }
    const isConfirmed = window.confirm(`确定要删除 ${bot.botName} 吗？`);
    if (!isConfirmed) return;
    // confirmNum.value = 0;
    const res = await axios.post('http://127.0.0.1:3000/user/bot/remove', {
        bot_id: bot.id,
    }, {
        headers: {
            Authorization: `Bearer ${store.state.user.token}`,
        }
    });
    if (res.data.error_message == 'success') {
        alert('删除成功');
        refresh();
    } else {
        alert('删除失败');
    }
}

const refresh = () => {
    axios.post('http://127.0.0.1:3000/user/bot/get', null, {
        headers: {
            Authorization: `Bearer ${store.state.user.token}`,
        }
    }).then(res => {
        bots.value = res.data;
        // console.log(res.data);
    }).catch(err => {
        console.log(err);
    });
}

onMounted(async () => {
    refresh();
});

</script>

<style scoped>
.row {
    margin-top: 20px;
}

.btn {
    margin-right: 5px;
}
</style>