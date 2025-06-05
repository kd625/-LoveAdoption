<template>
  <div class="user-applications-page">
    <div class="page-header">
      <h1>我的申请</h1>
    </div>
    
    <div class="applications-container">
      <el-table
        :data="applicationList"
        style="width: 100%"
        v-loading="loading"
        border
      >
        <el-table-column prop="id" label="申请ID" width="80" />
        <el-table-column label="动物信息" min-width="200">
          <template #default="scope">
            <div class="animal-info">
              <el-avatar :size="40" :src="scope.row.animal?.image_url || 'https://via.placeholder.com/40'" />
              <div class="animal-text">
                <div>{{ scope.row.animal?.name }}</div>
                <div class="animal-species">{{ scope.row.animal?.species }} / {{ scope.row.animal?.gender === 0 ? '公' : '母' }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="application_reason" label="申请理由" min-width="250" show-overflow-tooltip />
        <el-table-column prop="create_time" label="申请时间" width="180" />
        <el-table-column label="状态" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="danger">已拒绝</el-tag>
            <el-tag v-else-if="scope.row.status === 3" type="info">已撤回</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审核意见" min-width="200" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.review_comments || '暂无审核意见' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 0" 
              type="danger" 
              size="small" 
              @click="withdrawApplication(scope.row)"
            >
              撤回申请
            </el-button>
            <el-button 
              v-else 
              type="primary" 
              size="small" 
              @click="viewDetail(scope.row)"
            >
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :page-size="pageSize"
          :current-page="currentPage"
          @current-change="handlePageChange"
        />
      </div>
    </div>
    
    <!-- 申请详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="申请详情"
      width="600px"
    >
      <div v-if="currentApplication" class="application-detail">
        <el-descriptions title="申请信息" :column="1" border>
          <el-descriptions-item label="申请ID">{{ currentApplication.id }}</el-descriptions-item>
          <el-descriptions-item label="动物名称">{{ currentApplication.animal?.name }}</el-descriptions-item>
          <el-descriptions-item label="动物物种">{{ currentApplication.animal?.species }}</el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ currentApplication.create_time }}</el-descriptions-item>
          <el-descriptions-item label="申请状态">
            <el-tag v-if="currentApplication.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="currentApplication.status === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="currentApplication.status === 2" type="danger">已拒绝</el-tag>
            <el-tag v-else-if="currentApplication.status === 3" type="info">已撤回</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请理由">{{ currentApplication.application_reason }}</el-descriptions-item>
          <el-descriptions-item label="审核意见">{{ currentApplication.review_comments || '暂无审核意见' }}</el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
          <el-button 
            v-if="currentApplication && currentApplication.status === 0" 
            type="danger" 
            @click="withdrawApplication(currentApplication)"
          >
            撤回申请
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listApplicationByPageApi, updateApplicationApi } from '../api/application'
import { useUserStore } from '../stores/userStore'

const loading = ref(false)
const applicationList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const detailDialogVisible = ref(false)
const currentApplication = ref(null)
const userStore = useUserStore()

// 获取用户申请列表
const fetchApplicationList = async () => {
  loading.value = true
  try {
    // 检查用户是否登录
    if (!userStore.isLoggedIn || !userStore.userInfo?.id) {
      ElMessage.error('请先登录')
      loading.value = false
      return
    }
    
    // 构建请求参数
    const params = {
      current: currentPage.value,
      pageSize: pageSize.value,
      userId: userStore.userInfo.id
    }
    
    // 调用接口获取数据
    const data = await listApplicationByPageApi(params)
    
    applicationList.value = data.records || []
    total.value = data.total || 0
    loading.value = false
  } catch (error) {
    console.error('获取申请列表失败:', error)
    ElMessage.error('获取申请列表失败')
    loading.value = false
  }
}

// 页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchApplicationList()
}

// 查看申请详情
const viewDetail = (application) => {
  currentApplication.value = application
  detailDialogVisible.value = true
}

// 撤回申请
const withdrawApplication = (application) => {
  ElMessageBox.confirm('确定要撤回该申请吗？', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      // 调用更新状态API
      await updateApplicationApi({
        id: application.id,
        status: 3 // 3: 已撤回
      })
      
      // 更新列表中的状态
      const index = applicationList.value.findIndex(item => item.id === application.id)
      if (index !== -1) {
        applicationList.value[index].status = 3
        applicationList.value[index].review_comments = '用户已撤回申请'
      }
      
      // 如果是在详情对话框中撤回的，也更新当前申请的状态
      if (currentApplication.value && currentApplication.value.id === application.id) {
        currentApplication.value.status = 3
        currentApplication.value.review_comments = '用户已撤回申请'
      }
      
      ElMessage.success('申请已成功撤回')
      detailDialogVisible.value = false
    } catch (error) {
      console.error('撤回申请失败:', error)
      ElMessage.error('撤回申请失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

onMounted(() => {
  fetchApplicationList()
})
</script>

<style scoped>
.user-applications-page {
  padding: 20px 0;
}

.page-header {
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 24px;
  color: #303133;
}

.applications-container {
  background-color: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  padding: 20px;
}

.animal-info {
  display: flex;
  align-items: center;
}

.animal-text {
  margin-left: 10px;
}

.animal-species {
  font-size: 12px;
  color: #909399;
}

.pagination-container {
  margin-top: 20px;
  text-align: center;
}

.application-detail {
  margin-bottom: 20px;
}
</style> 