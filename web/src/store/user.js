import axios from "axios";

export default {
    state: {
        id: '',
        username: '',
        photo: '',
        token: '',
        is_login: false,
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = true;
        },
        updateToken(state, token) {
            state.token = token;
        }
    },
    actions: {
        async login({ commit }, payload) {
            try {
                const response = await axios.post('http://localhost:3000/user/account/token', {
                    username: payload.username,
                    password: payload.password,
                });

                if (response.data.error_message == "success") {
                    commit('updateToken', response.data.token);
                    // console.log("登录成功", response.data);
                    return { success: true, message: "登录成功" }; // 返回成功
                } else {
                    console.error("登录失败", response.data);
                    return { success: false, message: "用户名或密码错误" }; // 返回错误信息
                }
            } catch (error) {
                console.error("请求出错", error);
                return { success: false, message: "用户名或密码错误" };
            }
        },
        async getinfo({ commit }) {
            try {
                const response = await axios.get('http://localhost:3000/user/account/info', {
                    headers: {
                        Authorization: `Bearer ${this.state.token}`,
                    },
                });

                if (response.data.error_message == "success") {
                    commit('updateUser', response.data.data);
                    // console.log("获取用户信息成功", response.data);
                    return { success: true, message: "获取用户信息成功"}
                }
            } catch (error) {
                console.error("请求出错", error);
                return { success: false, message: "获取用户信息失败" };
            }
            // ?
        }
    },
    getters: {

    }
}