import { createRouter, createWebHistory } from 'vue-router'
import PkIndexView from '@/views/pk/PkIndexView.vue'
import RanklistIndexView from '@/views/ranklist/RanklistIndexView.vue'
import RecordIndexView from '@/views/record/RecordIndexView.vue'
import UserBotIndexView from '@/views/user/bot/UserBotIndexView.vue'
import NotFound from '@/views/error/NotFound.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      redirect: '/pk',
    },
    {
      path: '/pk',
      name: 'pk_index',
      component: PkIndexView,
    },
    {
      path: '/ranklist',
      name: 'ranklist_index',
      component: RanklistIndexView,
    },
    {
      path: '/record',
      name: 'record_index',
      component: RecordIndexView,
    },
    {
      path: '/user/bot',
      name: 'user_bot_index',
      component: UserBotIndexView,
    },
    {
      path: '/404',
      name: '404',
      component: NotFound,
    },
    {
      path: '/:catchAll(.*)',
      name: '404_redirect',
      redirect: '/404',
    }
  ],
})

export default router