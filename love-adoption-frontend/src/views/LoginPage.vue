<template>
  <div class="login-page">
    <div class="login-container">
      <h2>用户登录</h2>
      <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-width="0" class="login-form">
        <el-form-item prop="userAccount">
          <el-input v-model="loginForm.userAccount" placeholder="账号" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="userPassword">
          <el-input v-model="loginForm.userPassword" type="password" placeholder="密码" prefix-icon="el-icon-lock" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="login-button" @click="handleLogin">登录</el-button>
        </el-form-item>
        <div class="login-options">
          <router-link to="/register">没有账号？立即注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/userStore'

const router = useRouter()
const userStore = useUserStore()
const loginFormRef = ref(null)
const loading = ref(false)

const loginForm = reactive({
  userAccount: '',
  userPassword: ''
})

const rules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ]
}

const handleLogin = () => {
  loginFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      const result = await userStore.login(loginForm)
      
      // 根据用户角色跳转
      if (result.userRole === 'admin') {
        router.push('/admin/dashboard')
      } else {
        router.push('/')
      }
    } catch (error) {
      console.error('登录失败:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.login-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}

.login-container {
  width: 400px;
  padding: 30px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.login-container h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.login-form {
  width: 100%;
}

.login-button {
  width: 100%;
}

.login-options {
  display: flex;
  justify-content: center;
  margin-top: 15px;
  font-size: 14px;
}

.login-options a {
  color: #409eff;
  text-decoration: none;
}

.login-options a:hover {
  text-decoration: underline;
}
</style> 