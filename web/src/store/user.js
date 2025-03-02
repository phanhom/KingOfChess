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
            state.is_login = true;
            state.token = token;
        },
        logout(state) {
            state.id = '';
            state.username = '';
            state.photo = '';
            state.token = '';
            state.is_login = false;
        }
    },
    actions: {
        async login(context, data) {
            try {
                const response = await axios.post('http://localhost:3000/user/account/token', {
                    username: data.username,
                    password: data.password,
                });

                if (response.data.error_message == "success") {
                    localStorage.setItem('jwt_token', response.data.token);
                    localStorage.setItem('test', "phanhom");
                    context.commit('updateToken', response.data.token);
                    // console.log("登录成功", response.data);
                    data.success && data.success(response);
                } else {
                    console.error("登录失败", response.data);
                    data.error && data.error(response);
                }
            } catch (error) {
                console.error("请求出错", error);
                data.error && data.error(error);
            }
        },
        async getinfo(context, data) {
            try {
                const response = await axios.get('http://localhost:3000/user/account/info', {
                    headers: {
                        Authorization: `Bearer ${context.state.token}`,
                    },
                });

                if (response.data.error_message == "success") {
                    context.commit('updateUser', response.data);
                    // console.log("获取用户信息成功", response.data);
                    data.success && data.success(response);
                } else {
                    console.error("获取用户信息失败", response.data);
                    data.error && data.error(response);
                }
            } catch (error) {
                console.error("请求出错", error);
                data.error && data.error(error);
            }
        },
        async logout(context) {
            localStorage.removeItem('jwt_token');
            context.commit("logout");
        }
    },
    getters: {

    }
}