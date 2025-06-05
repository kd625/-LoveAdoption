<template>
  <div class="admin-dashboard-page">
    <h1 class="page-title">管理员仪表盘</h1>
    
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.animalCount }}</div>
          <div class="stat-label">动物总数</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.userCount }}</div>
          <div class="stat-label">用户总数</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card">
          <div class="stat-value">{{ stats.applicationCount }}</div>
          <div class="stat-label">申请总数</div>
        </el-card>
      </el-col>
    </el-row>
    
    <el-card class="recent-section">
      <template #header>
        <div class="card-header">
          <span>最近的申请</span>
          <el-button class="button" text @click="goToApplications">查看全部</el-button>
        </div>
      </template>
      <el-table :data="recentApplications" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="动物名称" width="120">
          <template #default="scope">
            {{ scope.row.animal?.name }}
          </template>
        </el-table-column>
        <el-table-column label="申请人" width="120">
          <template #default="scope">
            {{ scope.row.user?.userName }}
          </template>
        </el-table-column>
        <el-table-column prop="create_time" label="申请时间" width="180" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="danger">已拒绝</el-tag>
            <el-tag v-else-if="scope.row.status === 3" type="info">已撤回</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 0" 
              type="primary" 
              size="small" 
              @click="reviewApplication(scope.row)"
            >
              审核
            </el-button>
            <el-button 
              v-else 
              type="info" 
              size="small" 
              @click="viewApplicationDetail(scope.row.id)"
            >
              查看
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { listApplicationApi, listApplicationByPageApi } from '../../api/application'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)

// 统计数据
const stats = ref({
  animalCount: 0,
  userCount: 0,
  applicationCount: 0
})

// 最近的申请
const recentApplications = ref([])

// 获取仪表盘数据
const fetchDashboardData = async () => {
  loading.value = true
  try {
    // 获取最近的申请数据
    const params = {
      pageSize: 5,  // 获取最近的5条记录
      current: 1,
      sort: 'create_time,desc'  // 按创建时间降序排序
    }
    
    const applicationData = await listApplicationByPageApi(params)
    recentApplications.value = applicationData.records || []
    
    // 统计数据暂时保留模拟数据，实际项目中应从后端获取
    stats.value = {
      animalCount: 25,
      userCount: 50,
      applicationCount: applicationData.total || 0
    }
    
    loading.value = false
  } catch (error) {
    console.error('获取仪表盘数据失败:', error)
    ElMessage.error('获取申请数据失败')
    loading.value = false
  }
}

// 跳转到申请列表页面
const goToApplications = () => {
  router.push('/admin/applications')
}

// 审核申请
const reviewApplication = (application) => {
  router.push(`/admin/applications?id=${application.id}`)
}

// 查看申请详情
const viewApplicationDetail = (id) => {
  router.push(`/admin/applications?id=${id}`)
}

onMounted(() => {
  fetchDashboardData()
})
</script>

<style scoped>
.admin-dashboard-page {
  padding: 20px;
}

.page-title {
  margin-bottom: 20px;
  font-size: 24px;
  color: #303133;
}

.stat-card {
  text-align: center;
  padding: 20px;
  height: 120px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.stat-value {
  font-size: 36px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 10px;
}

.stat-label {
  font-size: 16px;
  color: #606266;
}

.recent-section {
  margin-top: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style> 