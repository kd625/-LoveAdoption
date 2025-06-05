<template>
  <div class="animal-detail-page">
    <div v-if="loading" class="loading-container">
      <el-skeleton :rows="10" animated />
    </div>
    
    <div v-else-if="animal" class="animal-detail">
      <div class="animal-header">
        <h1>{{ animal.name }}</h1>
        <el-tag v-if="animal.adoptionStatus === 0" type="success">可领养</el-tag>
        <el-tag v-else-if="animal.adoptionStatus === 1" type="info">已领养</el-tag>
        <el-tag v-else-if="animal.adoptionStatus === 2" type="danger">已删除</el-tag>
      </div>
      
      <div class="animal-content">
        <div class="animal-image">
          <img :src="animal.image_url || 'https://via.placeholder.com/500x300'" alt="动物图片" />
        </div>
        
        <div class="animal-info">
          <el-descriptions title="基本信息" :column="1" border>
            <el-descriptions-item label="物种">{{ animal.species }}</el-descriptions-item>
            <el-descriptions-item label="品种">{{ animal.breed }}</el-descriptions-item>
            <el-descriptions-item label="年龄">{{ animal.age }}岁</el-descriptions-item>
            <el-descriptions-item label="性别">{{ animal.gender === 0 ? '公' : '母' }}</el-descriptions-item>
            <el-descriptions-item label="健康状况">{{ animal.healthStatus }}</el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      
      <div class="animal-description">
        <h3>详细介绍</h3>
        <p>{{ animal.description }}</p>
      </div>
      
      <div class="adoption-form" v-if="animal.adoptionStatus === 0">
        <h3>申请领养</h3>
        <el-form :model="applicationForm" :rules="rules" ref="applicationFormRef" label-width="100px">
          <el-form-item label="申请理由" prop="application_reason">
            <el-input v-model="applicationForm.application_reason" type="textarea" :rows="4" placeholder="请详细描述您申请领养的理由"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="submitting" @click="submitApplication">提交申请</el-button>
          </el-form-item>
        </el-form>
      </div>
      
      <div class="adoption-notice" v-else-if="animal.adoptionStatus === 1 || animal.adoptionStatus === 2">
        <el-alert
          :title="animal.adoptionStatus === 1 ? '该动物已被领养' : '该动物已被删除'"
          :type="animal.adoptionStatus === 1 ? 'info' : 'error'"
          :description="animal.adoptionStatus === 1 ? '很遗憾，该动物已经被领养了，您可以浏览其他可爱的小动物。' : '该动物信息已被删除。'"
          show-icon
          :closable="false"
        />
      </div>
    </div>
    
    <div v-else class="error-container">
      <el-empty description="未找到该动物信息"></el-empty>
      <el-button type="primary" @click="$router.push('/')">返回首页</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getAnimalByIdApi } from '../api/animal'
import { addApplicationApi } from '../api/application'
import { useUserStore } from '../stores/userStore'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
const animalId = route.params.id
const animal = ref(null)
const loading = ref(true)
const submitting = ref(false)
const applicationFormRef = ref(null)

const applicationForm = reactive({
  animalId: animalId,
  application_reason: '',
})

const rules = {
  application_reason: [
    { required: true, message: '请填写申请理由', trigger: 'blur' },
    { min: 10, message: '申请理由不能少于10个字符', trigger: 'blur' }
  ]
}

// 获取动物详情
const fetchAnimalDetail = async () => {
  loading.value = true
  try {
    const data = await getAnimalByIdApi(animalId)
    // 确保显示健康状况
    if (data.health_status && !data.healthStatus) {
      data.healthStatus = data.health_status
    }
    // 确保显示领养状态
    if (data.adoption_status !== undefined && data.adoptionStatus === undefined) {
      data.adoptionStatus = data.adoption_status
    }
    animal.value = data
    loading.value = false
  } catch (error) {
    console.error('获取动物详情失败:', error)
    ElMessage.error('获取动物详情失败')
    loading.value = false
  }
}

// 提交领养申请
const submitApplication = () => {
  applicationFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    // 检查用户是否登录
    if (!userStore.isLoggedIn) {
      ElMessage.warning('请先登录')
      router.push('/login')
      return
    }
    
    submitting.value = true
    try {
      // 添加userId字段
      const applicationData = {
        ...applicationForm,
        userId: userStore.userInfo.id
      }
      
      await addApplicationApi(applicationData)
      ElMessage.success('申请提交成功，请等待管理员审核')
      router.push('/user/applications')
    } catch (error) {
      console.error('提交申请失败:', error)
      ElMessage.error('提交申请失败，请稍后重试')
    } finally {
      submitting.value = false
    }
  })
}

onMounted(() => {
  fetchAnimalDetail()
})
</script>

<style scoped>
.animal-detail-page {
  padding: 20px 0;
}

.loading-container {
  padding: 20px;
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.animal-detail {
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.animal-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.animal-header h1 {
  margin-right: 15px;
  margin-bottom: 0;
}

.animal-content {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 30px;
}

.animal-image {
  flex: 1;
  min-width: 300px;
}

.animal-image img {
  width: 100%;
  border-radius: 4px;
  object-fit: cover;
}

.animal-info {
  flex: 1;
  min-width: 300px;
}

.animal-description {
  margin-bottom: 30px;
}

.animal-description h3 {
  margin-bottom: 15px;
  font-size: 18px;
  font-weight: bold;
}

.animal-description p {
  line-height: 1.6;
  color: #606266;
}

.adoption-form {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

.adoption-form h3 {
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: bold;
}

.adoption-notice {
  margin-top: 30px;
}

.error-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 20px;
  padding: 40px 0;
}
</style> 