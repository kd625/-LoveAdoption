<template>
  <div class="main-layout">
    <header class="header">
      <div class="container">
        <div class="logo">
          <router-link to="/">爱心领养网站</router-link>
        </div>
        <nav class="nav">
          <router-link to="/">首页</router-link>
          <template v-if="!userStore.isLoggedIn">
            <router-link to="/login">登录</router-link>
            <router-link to="/register">注册</router-link>
          </template>
          <template v-else>
            <router-link to="/user/applications">我的申请</router-link>
            <router-link to="/admin" v-if="userStore.isAdmin">后台管理</router-link>
            <a href="javascript:;" @click="handleLogout">退出</a>
            <div class="user-avatar" @click="router.push('/user/profile')">
              <el-avatar :size="32" :src="userStore.userInfo?.userAvatar || 'https://via.placeholder.com/32'"></el-avatar>
            </div>
          </template>
        </nav>
      </div>
    </header>
    
    <main class="main-content">
      <div class="container">
        <router-view />
      </div>
    </main>
    
    <footer class="footer">
      <div class="container">
        <p>© 2024 爱心领养网站 - 版权所有</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/userStore'
import { ElAvatar } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

// 退出登录
const handleLogout = async () => {
  await userStore.logout()
  router.push('/login')
}

// 初始化时检查登录状态
onMounted(() => {
  if (userStore.isLoggedIn) {
    userStore.fetchCurrentUser()
  }
})
</script>

<style scoped>
.main-layout {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 15px;
}

.header {
  background-color: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 15px 0;
}

.header .container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo a {
  font-size: 20px;
  font-weight: bold;
  color: #409eff;
  text-decoration: none;
}

.nav {
  display: flex;
  gap: 20px;
  align-items: center;
}

.nav a {
  color: #606266;
  text-decoration: none;
  transition: color 0.3s;
}

.nav a:hover, .nav a.router-link-active {
  color: #409eff;
}

.user-avatar {
  cursor: pointer;
  margin-left: 10px;
  transition: transform 0.2s;
}

.user-avatar:hover {
  transform: scale(1.1);
}

.main-content {
  flex: 1;
  padding: 30px 0;
}

.footer {
  background-color: #f5f7fa;
  padding: 20px 0;
  text-align: center;
  color: #909399;
}
</style>