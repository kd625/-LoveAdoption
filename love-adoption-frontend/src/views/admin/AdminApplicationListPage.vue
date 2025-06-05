<template>
  <div class="admin-application-list-page">
    <div class="page-header">
      <h1>领养申请管理</h1>
    </div>
    
    <el-card class="filter-card">
      <el-form :inline="true" :model="queryParams" class="filter-form">
        <el-form-item label="申请状态">
          <el-select v-model="queryParams.status" placeholder="全部状态" clearable>
            <el-option label="待审核" :value="0" />
            <el-option label="已通过" :value="1" />
            <el-option label="已拒绝" :value="2" />
            <el-option label="已撤回" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
          <el-button @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    
    <el-card class="table-card">
      <el-table
        :data="applicationList"
        style="width: 100%"
        v-loading="loading"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="动物信息" min-width="180">
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
        <el-table-column label="申请人" width="150">
          <template #default="scope">
            <div>{{ scope.row.user?.userName }}</div>
            <div class="user-email">{{ scope.row.user?.email }}</div>
          </template>
        </el-table-column>
        <el-table-column prop="application_reason" label="申请理由" min-width="200" show-overflow-tooltip />
        <el-table-column prop="create_time" label="申请时间" width="180" />
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.status === 0" type="warning">待审核</el-tag>
            <el-tag v-else-if="scope.row.status === 1" type="success">已通过</el-tag>
            <el-tag v-else-if="scope.row.status === 2" type="danger">已拒绝</el-tag>
            <el-tag v-else-if="scope.row.status === 3" type="info">已撤回</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="审核意见" min-width="150" show-overflow-tooltip>
          <template #default="scope">
            {{ scope.row.review_comments || '暂无审核意见' }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button 
              v-if="scope.row.status === 0" 
              type="primary" 
              size="small" 
              @click="handleReview(scope.row)"
            >
              审核
            </el-button>
            <el-button 
              type="info" 
              size="small" 
              @click="handleDetail(scope.row)"
            >
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="queryParams.pageSize"
          :current-page="queryParams.current"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>
    
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
          <el-descriptions-item label="申请人">{{ currentApplication.user?.userName }}</el-descriptions-item>
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
            type="primary" 
            @click="handleReview(currentApplication)"
          >
            审核
          </el-button>
        </span>
      </template>
    </el-dialog>
    
    <!-- 审核对话框 -->
    <el-dialog
      v-model="reviewDialogVisible"
      title="审核申请"
      width="500px"
    >
      <div v-if="currentApplication" class="review-form">
        <el-form :model="reviewForm" :rules="reviewRules" ref="reviewFormRef" label-width="100px">
          <el-form-item label="申请ID">
            <span>{{ currentApplication.id }}</span>
          </el-form-item>
          <el-form-item label="动物名称">
            <span>{{ currentApplication.animal?.name }}</span>
          </el-form-item>
          <el-form-item label="申请人">
            <span>{{ currentApplication.user?.userName }}</span>
          </el-form-item>
          <el-form-item label="审核结果" prop="status">
            <el-radio-group v-model="reviewForm.status">
              <el-radio :label="1">通过</el-radio>
              <el-radio :label="2">拒绝</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="审核意见" prop="review_comments">
            <el-input v-model="reviewForm.review_comments" type="textarea" :rows="4" placeholder="请输入审核意见"></el-input>
          </el-form-item>
        </el-form>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="reviewDialogVisible = false">取消</el-button>
          <el-button type="primary" :loading="submitting" @click="submitReview">确认</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, nextTick } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listApplicationByPageApi, updateApplicationApi, getApplicationByIdApi } from '../../api/application'

const route = useRoute()
const router = useRouter()
const loading = ref(false)
const submitting = ref(false)
const applicationList = ref([])
const total = ref(0)
const detailDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const currentApplication = ref(null)
const reviewFormRef = ref(null)

// 查询参数
const queryParams = reactive({
  current: 1,
  pageSize: 10,
  status: undefined
})

// 审核表单
const reviewForm = reactive({
  id: '',
  status: 1,
  review_comments: ''
})

// 审核表单校验规则
const reviewRules = {
  status: [
    { required: true, message: '请选择审核结果', trigger: 'change' }
  ],
  review_comments: [
    { required: true, message: '请填写审核意见', trigger: 'blur' },
    { min: 2, message: '审核意见不能少于2个字符', trigger: 'blur' }
  ]
}

// 获取申请列表
const getApplicationList = async () => {
  loading.value = true
  try {
    const res = await listApplicationByPageApi(queryParams)
    applicationList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('获取申请列表失败:', error)
    ElMessage.error('获取申请列表失败')
  } finally {
    loading.value = false
  }
}

// 查询
const handleQuery = () => {
  queryParams.current = 1
  getApplicationList()
}

// 重置查询
const resetQuery = () => {
  queryParams.status = undefined
  handleQuery()
}

// 处理页码变化
const handleCurrentChange = (page) => {
  queryParams.current = page
  getApplicationList()
}

// 处理每页条数变化
const handleSizeChange = (size) => {
  queryParams.pageSize = size
  queryParams.current = 1
  getApplicationList()
}

// 查看详情
const handleDetail = (row) => {
  currentApplication.value = row
  detailDialogVisible.value = true
}

// 审核申请
const handleReview = (row) => {
  currentApplication.value = row
  reviewForm.id = row.id
  reviewForm.status = 1
  reviewForm.review_comments = ''
  reviewDialogVisible.value = true
  
  // 重置表单校验
  nextTick(() => {
    if (reviewFormRef.value) {
      reviewFormRef.value.resetFields()
    }
  })
}

// 提交审核
const submitReview = () => {
  reviewFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    submitting.value = true
    try {
      await updateApplicationApi(reviewForm)
      
      // 更新列表中的状态
      const index = applicationList.value.findIndex(item => item.id === reviewForm.id)
      if (index !== -1) {
        applicationList.value[index].status = reviewForm.status
        applicationList.value[index].review_comments = reviewForm.review_comments
      }
      
      // 如果是查看详情中的审核，也更新当前申请的状态
      if (currentApplication.value && currentApplication.value.id === reviewForm.id) {
        currentApplication.value.status = reviewForm.status
        currentApplication.value.review_comments = reviewForm.review_comments
      }
      
      ElMessage.success('审核成功')
      reviewDialogVisible.value = false
    } catch (error) {
      console.error('审核失败:', error)
      ElMessage.error('审核失败')
    } finally {
      submitting.value = false
    }
  })
}

// 处理URL中的ID参数，如果存在则直接打开详情或审核对话框
const handleUrlParams = async () => {
  const id = route.query.id
  if (id) {
    try {
      loading.value = true
      const application = await getApplicationByIdApi(id)
      currentApplication.value = application
      
      if (application.status === 0) {
        // 如果是待审核状态，打开审核对话框
        reviewForm.id = application.id
        reviewForm.status = 1
        reviewForm.review_comments = ''
        reviewDialogVisible.value = true
      } else {
        // 否则打开详情对话框
        detailDialogVisible.value = true
      }
      
      loading.value = false
    } catch (error) {
      console.error('获取申请详情失败:', error)
      ElMessage.error('获取申请详情失败')
      loading.value = false
    }
  }
}

onMounted(() => {
  getApplicationList()
  handleUrlParams()
})
</script>

<style scoped>
.admin-application-list-page {
  padding: 20px 0;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

.filter-card {
  margin-bottom: 20px;
}

.filter-form {
  display: flex;
  align-items: center;
}

.table-card {
  margin-bottom: 20px;
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

.user-email {
  font-size: 12px;
  color: #909399;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.application-detail {
  margin-bottom: 20px;
}

.review-form {
  padding: 10px;
}
</style> 