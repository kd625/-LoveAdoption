<template>
  <div class="admin-layout">
    <el-container>
      <el-aside width="250px" class="admin-sidebar">
        <div class="logo">
          <router-link to="/admin/dashboard">管理员后台</router-link>
        </div>
        <el-menu
          router
          :default-active="$route.path"
          class="admin-menu"
          background-color="#304156"
          text-color="#bfcbd9"
          active-text-color="#409EFF"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><Menu /></el-icon>
            <span>仪表盘</span>
          </el-menu-item>
          
          <el-sub-menu index="/admin/animals">
            <template #title>
              <el-icon><List /></el-icon>
              <span>动物管理</span>
            </template>
            <el-menu-item index="/admin/animals">动物列表</el-menu-item>
            <el-menu-item index="/admin/animals/add">添加动物</el-menu-item>
          </el-sub-menu>
          
          <el-sub-menu index="/admin/users">
            <template #title>
              <el-icon><User /></el-icon>
              <span>用户管理</span>
            </template>
            <el-menu-item index="/admin/users">用户列表</el-menu-item>
            <el-menu-item index="/admin/users/add">添加用户</el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/admin/applications">
            <el-icon><Document /></el-icon>
            <span>领养申请管理</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <el-header class="admin-header">
          <div class="header-right">
            <el-dropdown trigger="click">
              <span class="user-dropdown">
                {{ userStore.userInfo?.userName || '管理员' }}
                <el-icon><ArrowDown /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="goToFrontend">前台首页</el-dropdown-item>
                  <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <el-main class="admin-main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Menu, List, User, Document, ArrowDown } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/userStore'

const router = useRouter()
const userStore = useUserStore()

// 检查管理员权限
onMounted(() => {
  if (!userStore.isLoggedIn) {
    ElMessage.error('请先登录')
    router.push('/login')
    return
  }
  
  userStore.fetchCurrentUser().then(() => {
    if (!userStore.isAdmin) {
      ElMessage.error('您没有管理员权限')
      router.push('/')
    }
  }).catch(() => {
    router.push('/login')
  })
})

// 跳转到前台
const goToFrontend = () => {
  router.push('/')
}

// 退出登录
const handleLogout = async () => {
  await userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
}

.admin-sidebar {
  background-color: #304156;
  height: 100vh;
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  overflow-y: auto;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #263445;
}

.logo a {
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  text-decoration: none;
}

.admin-menu {
  border-right: none;
}

.admin-header {
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  display: flex;
  align-items: center;
  justify-content: flex-end;
  height: 60px;
  padding: 0 20px;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-dropdown {
  cursor: pointer;
  display: flex;
  align-items: center;
  color: #606266;
  font-size: 14px;
}

.user-dropdown .el-icon {
  margin-left: 5px;
}

.admin-main {
  padding: 20px;
  margin-left: 250px;
}
</style> 