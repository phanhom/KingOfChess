import { createRouter, createWebHistory } from 'vue-router'
import store from '../store/index.js'
import PkIndexView from '@/views/pk/PkIndexView.vue'
import RanklistIndexView from '@/views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '@/views/record/RecordIndexView.vue'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView.vue'
import NotFound from '@/views/error/NotFound.vue'
import UserAccountLoginView from '@/views/user/account/UserAccountLoginView.vue'
import UserAccountRegisterView from '@/views/user/account/UserAccountRegisterView.vue'
import ChessView from '@/views/gamelist/ChessView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/pk',
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/pk',
      name: 'pk_index',
      component: PkIndexView,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/ranklist',
      name: 'ranklist_index',
      component: RanklistIndexView,
    },
    {
      path: '/ranklist/:page',
      name: 'ranklist_index_page',
      component: RanklistIndexView,
    },
    {
      path: '/record',
      name: 'record_index',
      component: RecordIndexView,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/user/bot',
      name: 'user_bot_index',
      component: UserBotIndexView,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/gamelist/chess',
      name: 'gamelist_chess',
      component: ChessView,
      meta: {
        requiresAuth: true,
      },
    },
    {
      path: '/404',
      name: '404',
      component: NotFound,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: '/user/account/login',
      name: 'user_account_login',
      component: UserAccountLoginView,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: '/user/account/register',
      name: 'user_account_register',
      component: UserAccountRegisterView,
      meta: {
        requiresAuth: false,
      },
    },
    {
      path: '/:catchAll(.*)',
      name: '404_redirect',
      redirect: '/404',
    }
  ],
})

router.beforeEach(async(to, from, next) => {
  let flag = 1;
  const jwt_token = localStorage.getItem('jwt_token');
  if (jwt_token) {
    await store.commit("updateToken", jwt_token);
    await store.dispatch("getinfo", {
      success: () => {
      },
      error: () => {
        alert("登录信息过期，请重新登录");
        localStorage.removeItem('jwt_token');
        store.commit("logout");
        router.push({ name: 'user_account_login' });
      }
    });
    // console.log("token: ", store.state.user.id);
  } else {
    flag = 2;
  }


  if (to.meta.requiresAuth && !store.state.user.is_login) {
    if (flag == 1) {
      next();
    } else {
      next('/user/account/login');
    }
  } else if (to.name === 'user_account_register' && store.state.user.is_login) {
    alert("请先退出登录，再进行注册。");
    console.log(from.fullPath);
    next(from.fullPath);
  } else {
    next();
  }
})

export default router