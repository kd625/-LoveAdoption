<template>
  <div class="user-profile-page">
    <div class="page-header">
      <h1>个人信息</h1>
    </div>
    
    <div class="profile-container" v-if="userInfo">
      <el-card class="profile-card">
        <div class="avatar-container">
          <el-avatar :size="100" :src="userInfo.userAvatar || 'https://via.placeholder.com/100'"></el-avatar>
          <div class="avatar-uploader">
            <FileUpload
              :tip="'支持jpg、png格式，不超过2MB'"
              :max-size="2"
              :validate-file-type="validateAvatarType"
              @upload-success="handleAvatarSuccess"
            >
              <el-button size="small" type="primary">更换头像</el-button>
            </FileUpload>
          </div>
        </div>
        
        <el-form
          :model="profileForm"
          :rules="rules"
          ref="profileFormRef"
          label-width="100px"
          class="profile-form"
        >
          <el-form-item label="账号">
            <el-input v-model="userInfo.account" disabled></el-input>
          </el-form-item>
          <el-form-item label="用户昵称" prop="userName">
            <el-input v-model="profileForm.userName"></el-input>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-radio-group v-model="profileForm.gender">
              <el-radio :label="0">男</el-radio>
              <el-radio :label="1">女</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="用户角色">
            <el-tag v-if="userInfo.userRole === 'admin'" type="danger">管理员</el-tag>
            <el-tag v-else type="info">普通用户</el-tag>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="loading" @click="updateProfile">保存修改</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    
    <div v-else class="loading-container">
      <el-skeleton :rows="6" animated />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/userStore'
import { updateUserApi } from '../api/user'
import FileUpload from '../components/FileUpload.vue'

const userStore = useUserStore()
const userInfo = computed(() => userStore.userInfo)
const loading = ref(false)
const profileFormRef = ref(null)

const profileForm = reactive({
  id: '',
  userName: '',
  gender: 0,
  userAvatar: ''
})

const rules = {
  userName: [
    { required: true, message: '请输入用户昵称', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ]
}

// 获取用户信息
const fetchUserInfo = () => {
  if (!userStore.isLoggedIn) {
    ElMessage.error('请先登录')
    return
  }
  
  // 从store中获取用户信息
  userStore.fetchCurrentUser().then(() => {
    // 初始化表单数据
    if (userInfo.value) {
      profileForm.id = userInfo.value.id
      profileForm.userName = userInfo.value.userName
      profileForm.gender = typeof userInfo.value.gender === 'number' ? userInfo.value.gender : 0
      profileForm.userAvatar = userInfo.value.userAvatar
    }
  }).catch(error => {
    console.error('获取用户信息失败:', error)
    ElMessage.error('获取用户信息失败')
  })
}

// 更新个人信息
const updateProfile = () => {
  profileFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      await updateUserApi(profileForm)
      
      // 更新store中的用户信息
      userStore.updateUserInfo({
        userName: profileForm.userName,
        gender: profileForm.gender,
        userAvatar: profileForm.userAvatar
      })
      
      ElMessage.success('个人信息更新成功')
    } catch (error) {
      console.error('更新个人信息失败:', error)
      ElMessage.error('更新个人信息失败')
    } finally {
      loading.value = false
    }
  })
}

// 头像类型校验
const validateAvatarType = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJPG) {
    ElMessage.error('头像图片只能是 JPG 或 PNG 格式!')
    return false
  }
  return true
}

// 头像上传成功的回调
const handleAvatarSuccess = (response) => {
  profileForm.userAvatar = response
  ElMessage.success('头像上传成功')
}

onMounted(() => {
  fetchUserInfo()
})
</script>

<style scoped>
.user-profile-page {
  padding: 20px 0;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 24px;
  color: #303133;
}

.profile-container {
  max-width: 600px;
  margin: 0 auto;
}

.profile-card {
  padding: 20px;
}

.avatar-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 30px;
}

.avatar-uploader {
  margin-top: 15px;
}

.profile-form {
  margin-top: 20px;
}

.loading-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}
</style>