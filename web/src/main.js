import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'

const app = createApp(App)

app.use(router)
app.use(store)

app.mount('#app')

// 密码合法性 不能包含*$#_/\+=(只能包含数字字母.!) (在后端的创建用户函数中, 后期还可以修改密码)
// 修改密码 头像 用户名 (这个可以在UserBotIndexView.vue实现,点击出现框框即可)
// 每个ip 只能每天只能注册5次，服务器每天最多注册10000个用户