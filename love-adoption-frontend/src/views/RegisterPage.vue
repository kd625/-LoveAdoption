<template>
  <div class="register-page">
    <div class="register-container">
      <h2>用户注册</h2>
      <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-width="0" class="register-form">
        <el-form-item prop="userAccount">
          <el-input v-model="registerForm.userAccount" placeholder="账号" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="userName">
          <el-input v-model="registerForm.userName" placeholder="用户昵称" prefix-icon="el-icon-user"></el-input>
        </el-form-item>
        <el-form-item prop="userPassword">
          <el-input v-model="registerForm.userPassword" type="password" placeholder="密码" prefix-icon="el-icon-lock" show-password></el-input>
        </el-form-item>
        <el-form-item prop="checkPassword">
          <el-input v-model="registerForm.checkPassword" type="password" placeholder="确认密码" prefix-icon="el-icon-lock" show-password></el-input>
        </el-form-item>
        <el-form-item prop="gender">
          <el-radio-group v-model="registerForm.gender">
            <el-radio :label="0">男</el-radio>
            <el-radio :label="1">女</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" class="register-button" @click="handleRegister">注册</el-button>
        </el-form-item>
        <div class="register-options">
          <router-link to="/login">已有账号？立即登录</router-link>
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
const registerFormRef = ref(null)
const loading = ref(false)

const registerForm = reactive({
  userAccount: '',
  userName: '',
  userPassword: '',
  checkPassword: '',
  gender: 0,
  userRole: 'user' // 默认为普通用户
})

const validateCheckPassword = (rule, value, callback) => {
  if (value !== registerForm.userPassword) {
    callback(new Error('两次输入的密码不一致'))
  } else {
    callback()
  }
}

const rules = {
  userAccount: [
    { required: true, message: '请输入账号', trigger: 'blur' },
    { min: 4, message: '账号长度不能小于4位', trigger: 'blur' }
  ],
  userName: [
    { required: true, message: '请输入用户昵称', trigger: 'blur' }
  ],
  userPassword: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  checkPassword: [
    { required: true, message: '请确认密码', trigger: 'blur' },
    { validator: validateCheckPassword, trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ]
}

const handleRegister = () => {
  registerFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      await userStore.register(registerForm)
      router.push('/login')
    } catch (error) {
      console.error('注册失败:', error)
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.register-page {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 70vh;
}

.register-container {
  width: 400px;
  padding: 30px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.register-container h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.register-form {
  width: 100%;
}

.register-button {
  width: 100%;
}

.register-options {
  display: flex;
  justify-content: center;
  margin-top: 15px;
  font-size: 14px;
}

.register-options a {
  color: #409eff;
  text-decoration: none;
}

.register-options a:hover {
  text-decoration: underline;
}
</style> 