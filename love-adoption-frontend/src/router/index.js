import { createRouter, createWebHistory } from 'vue-router'
import MainLayout from '../layouts/MainLayout.vue'
import AdminLayout from '../layouts/AdminLayout.vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/userStore'

const routes = [
  {
    path: '/',
    component: MainLayout,
    children: [
      {
        path: '',
        name: 'home',
        component: () => import('../views/HomePage.vue')
      },
      {
        path: 'login',
        name: 'login',
        component: () => import('../views/LoginPage.vue'),
        meta: { guest: true }
      },
      {
        path: 'register',
        name: 'register',
        component: () => import('../views/RegisterPage.vue'),
        meta: { guest: true }
      },
      {
        path: 'animal/:id',
        name: 'animalDetail',
        component: () => import('../views/AnimalDetailPage.vue')
      },
      {
        path: 'user/profile',
        name: 'userProfile',
        component: () => import('../views/UserProfilePage.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'user/applications',
        name: 'userApplications',
        component: () => import('../views/UserApplicationsPage.vue'),
        meta: { requiresAuth: true }
      }
    ]
  },
  {
    path: '/admin',
    component: AdminLayout,
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'adminDashboard',
        component: () => import('../views/admin/AdminDashboardPage.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
      },
      {
        path: 'animals',
        name: 'adminAnimalList',
        component: () => import('../views/admin/AdminAnimalListPage.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
      },
      {
        path: 'animals/add',
        name: 'adminAddAnimal',
        component: () => import('../views/admin/AdminAddAnimalPage.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
      },
      {
        path: 'animals/edit/:id',
        name: 'adminEditAnimal',
        component: () => import('../views/admin/AdminEditAnimalPage.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
      },
      {
        path: 'users',
        name: 'adminUserList',
        component: () => import('../views/admin/AdminUserListPage.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
      },
      {
        path: 'applications',
        name: 'adminApplicationList',
        component: () => import('../views/admin/AdminApplicationListPage.vue'),
        meta: { requiresAuth: true, requiresAdmin: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 全局前置守卫
router.beforeEach((to, from, next) => {
  // 从localStorage获取登录状态
  const isAuthenticated = localStorage.getItem('isAuthenticated') === 'true'
  const userInfo = JSON.parse(localStorage.getItem('userInfo'))
  const isAdmin = userInfo?.userRole === 'admin'

  // 需要登录权限的页面
  if (to.meta.requiresAuth && !isAuthenticated) {
    ElMessage.warning('请先登录')
    next('/login')
    return
  }

  // 需要管理员权限的页面
  if (to.meta.requiresAdmin && !isAdmin) {
    ElMessage.error('您没有权限访问该页面')
    next('/')
    return
  }

  // 已登录用户不能访问登录和注册页面
  if (to.meta.guest && isAuthenticated) {
    next('/')
    return
  }

  next()
})

export default router 