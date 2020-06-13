import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      redirect: '/real_total'
    },
    {
      path: '/',
      component: () => import(/* webpackChunkName: "home" */ '../components/common/Home.vue'),
      meta: { title: '自述文件' },
      children: [
        {
          path: '/real_total',
          component: () => import(/* webpackChunkName: "icon" */ '../components/pages/Real_total.vue'),
          meta: { title: '数据总览' }
        },
        {
          path: '/his_total',
          component: () => import(/* webpackChunkName: "icon" */ '../components/pages/His_total.vue'),
          meta: { title: '数据总览' }
        },
        {
          path: '/user_local',
          component: () => import(/* webpackChunkName: "icon" */ '../components/pages/User_local.vue'),
          meta: { title: '用户地域' }
        },
        {
          path: '/his_redirect',
          component: () => import(/* webpackChunkName: "icon" */ '../components/pages/His_redirect.vue'),
          meta: { title: '流量流向' }
        },
        {
          path: '/user_popular',
          component: () => import(/* webpackChunkName: "icon" */ '../components/pages/User_popular.vue'),
          meta: { title: '用户流行' }
        },
        {
          path: '/user_hot',
          component: () => import(/* webpackChunkName: "icon" */ '../components/pages/User_hot.vue'),
          meta: { title: '用户热点' }
        },
        {
          path: '/search',
          component: () => import(/* webpackChunkName: "table" */ '../components/pages/Search.vue'),
          meta: { title: '条件查找' }
        }
      ]
    }
  ]
});
