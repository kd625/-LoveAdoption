<template>
  <div class="admin-user-list-page">
    <div class="page-header">
      <h1>用户管理</h1>
      <el-button type="primary" @click="$router.push('/admin/users/add')">添加用户</el-button>
    </div>
    
    <div class="filter-section">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="账号">
          <el-input v-model="filterForm.account" placeholder="搜索账号"></el-input>
        </el-form-item>
        <el-form-item label="用户角色">
          <el-select v-model="filterForm.userRole" placeholder="选择角色" clearable>
            <el-option label="普通用户" value="user" />
            <el-option label="管理员" value="admin" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">筛选</el-button>
          <el-button @click="resetFilter">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="user-table-container">
      <el-table
        :data="userList"
        style="width: 100%"
        v-loading="loading"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="头像" width="100">
          <template #default="scope">
            <el-avatar :size="40" :src="scope.row.userAvatar || 'https://via.placeholder.com/40'"></el-avatar>
          </template>
        </el-table-column>
        <el-table-column prop="account" label="账号" width="150" />
        <el-table-column prop="userName" label="用户昵称" width="150" />
        <el-table-column label="性别" width="80">
          <template #default="scope">
            {{ scope.row.gender === 0 ? '男' : '女' }}
          </template>
        </el-table-column>
        <el-table-column label="用户角色" width="120">
          <template #default="scope">
            <el-tag v-if="scope.row.userRole === 'admin'" type="danger">管理员</el-tag>
            <el-tag v-else type="info">普通用户</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="editUser(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="deleteUser(scope.row)"
              :disabled="scope.row.id === currentUserId"
            >
              删除
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
          :page-size="pageSize"
          :current-page="currentPage"
          @size-change="handleSizeChange"
          @current-change="handlePageChange"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { listUserByPageApi, deleteUserApi } from '../../api/user'

const router = useRouter()
const userList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)
const currentUserId = ref(null)

const filterForm = reactive({
  account: '',
  userRole: ''
})

// 获取用户列表
const fetchUserList = async () => {
  loading.value = true
  try {
    // 获取当前用户ID
    const userInfo = JSON.parse(localStorage.getItem('userInfo') || '{}')
    currentUserId.value = userInfo.id
    
    // 构建请求参数
    const params = {
      current: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 添加可选参数
    if (filterForm.account) {
      params.userAccount = filterForm.account
    }
    
    if (filterForm.userRole) {
      params.userRole = filterForm.userRole
    }
    
    // 调用接口获取用户列表
    const res = await listUserByPageApi(params)
    userList.value = res.records || []
    total.value = res.total || 0
  } catch (error) {
    console.error('获取用户列表失败:', error)
    ElMessage.error('获取用户列表失败')
  } finally {
    loading.value = false
  }
}

// 筛选
const handleFilter = () => {
  currentPage.value = 1
  fetchUserList()
}

// 重置筛选
const resetFilter = () => {
  filterForm.account = ''
  filterForm.userRole = ''
  currentPage.value = 1
  fetchUserList()
}

// 页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchUserList()
}

// 每页条数变化
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchUserList()
}

// 编辑用户
const editUser = (user) => {
  router.push(`/admin/users/edit/${user.id}`)
}

// 删除用户
const deleteUser = (user) => {
  // 不能删除自己
  if (user.id === currentUserId.value) {
    ElMessage.warning('不能删除当前登录的用户')
    return
  }
  
  ElMessageBox.confirm(`确定要删除用户"${user.userName}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteUserApi({ id: user.id })
      
      // 从列表中移除该用户
      userList.value = userList.value.filter(item => item.id !== user.id)
      total.value--
      
      ElMessage.success('删除成功')
    } catch (error) {
      console.error('删除用户失败:', error)
      ElMessage.error('删除用户失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

onMounted(() => {
  fetchUserList()
})
</script>

<style scoped>
.admin-user-list-page {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h1 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.filter-section {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.user-table-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style> 