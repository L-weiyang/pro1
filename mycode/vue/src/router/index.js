import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {path: '/register',name: 'register',component:() => import('../views/Register.vue')},
  {path: '/login', name: 'login', component: () => import('../views/Login.vue')},


  {
    path: '/manager',
    name: 'manager',
    component : () => import('../views/ManagerLayout.vue'),
    children:[
      {path: 'home', component: () => import('../views/manager/HomeView.vue')},
      {path: 'admin', component: () => import('../views/manager/Admin.vue')},
      {path: 'user', component: () => import('../views/manager/User.vue')},
      {path: 'grade', component: () => import('../views/manager/Grade.vue')},
      {path: 'news', component: () => import('../views/manager/News.vue')},
      {path: 'activity', component: () => import('../views/manager/Activity.vue')},
      {path: 'record', component: () => import('../views/manager/Record.vue')},
      {path: 'takeoff', component: () => import('../views/manager/TakeOff.vue')},
      {path: 'comment', component: () => import('../views/manager/Comment.vue')},


    ]
  },
    //前台路由
  //   前台路由
  {path: '/front', name: 'front', component: () => import('../views/FrontLayout.vue'),
    // 二级路由
    children:[
      {path: 'index', component: () => import('../views/front/Index.vue')},
      {path: 'activity', component: () => import('../views/front/Activity.vue')},
      {path: 'news', component: () => import('../views/front/News.vue')},
      {path: 'record', component: () => import('../views/front/Record.vue')},
      {path: 'newsDetail', component: () => import('../views/front/NewsDetail.vue')},

    ]
  },

]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})
const originalPush = VueRouter.prototype.push
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch(err => err)
}

// 路由守卫
router.beforeEach((to ,from, next) => {
  if (to.path === '/') {
    return next("/login");
  }
  let user = localStorage.getItem("user");
  if (!user && to.path !== '/login' && to.path !== '/register' && to.path !== '/front/index') {
    return next("/login");
  }
  next();
})
export default router
