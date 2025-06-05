<template>
  <div class="admin-animal-list-page">
    <div class="page-header">
      <h1>动物管理</h1>
      <el-button type="primary" @click="$router.push('/admin/animals/add')">添加动物</el-button>
    </div>
    
    <div class="filter-section">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="物种">
          <el-select v-model="filterForm.species" placeholder="选择物种" clearable>
            <el-option label="猫咪" value="猫咪" />
            <el-option label="狗狗" value="狗狗" />
            <el-option label="兔子" value="兔子" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="性别">
          <el-select v-model="filterForm.gender" placeholder="选择性别" clearable>
            <el-option label="公" :value="0" />
            <el-option label="母" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item label="领养状态">
          <el-select v-model="filterForm.adoptionStatus" placeholder="选择状态" clearable>
            <el-option label="可领养" :value="0" />
            <el-option label="已领养" :value="1" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter" :loading="loading">筛选</el-button>
          <el-button @click="resetFilter" :disabled="loading">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="animal-table-container">
      <el-table
        :data="animalList"
        style="width: 100%"
        v-loading="loading"
        border
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="图片" width="100">
          <template #default="scope">
            <el-image
              style="width: 60px; height: 60px"
              :src="scope.row.image_url || 'https://via.placeholder.com/60'"
              fit="cover"
              :preview-src-list="scope.row.image_url ? [scope.row.image_url] : []"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="名称" width="120" />
        <el-table-column prop="species" label="物种" width="100" />
        <el-table-column prop="breed" label="品种" width="100" />
        <el-table-column label="性别" width="80">
          <template #default="scope">
            {{ scope.row.gender === 0 ? '公' : '母' }}
          </template>
        </el-table-column>
        <el-table-column prop="age" label="年龄" width="80" />
        <el-table-column prop="healthStatus" label="健康状况" min-width="150" show-overflow-tooltip />
        <el-table-column label="领养状态" width="100">
          <template #default="scope">
            <el-tag v-if="scope.row.adoptionStatus === 0" type="success">可领养</el-tag>
            <el-tag v-else-if="scope.row.adoptionStatus === 1" type="info">已领养</el-tag>
            <el-tag v-else-if="scope.row.adoptionStatus === 2" type="danger">已删除</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button 
              type="primary" 
              size="small" 
              @click="editAnimal(scope.row)"
            >
              编辑
            </el-button>
            <el-button 
              type="danger" 
              size="small" 
              @click="deleteAnimal(scope.row)"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container" v-if="total > 0">
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
import { listAnimalByPageApi, deleteAnimalApi } from '../../api/animal'

const router = useRouter()
const animalList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)
const loading = ref(false)

const filterForm = reactive({
  species: '',
  gender: '',
  adoptionStatus: ''
})

// 获取动物列表
const fetchAnimalList = async () => {
  loading.value = true
  try {
    // 构建请求参数
    const params = {
      current: currentPage.value,
      pageSize: pageSize.value
    }
    
    // 添加可选参数
    if (filterForm.species) {
      params.species = filterForm.species
    }
    
    if (filterForm.gender) {
      params.gender = filterForm.gender
    }
    
    if (filterForm.adoptionStatus !== '') {
      params.adoptionStatus = filterForm.adoptionStatus
    }
    
    // 调用后端接口
    const data = await listAnimalByPageApi(params)
    
    // 处理字段名不一致的问题
    const records = data.records || []
    records.forEach(item => {
      if (item.adoption_status !== undefined && item.adoptionStatus === undefined) {
        item.adoptionStatus = item.adoption_status
      }
      if (item.health_status !== undefined && item.healthStatus === undefined) {
        item.healthStatus = item.health_status
      }
    })
    
    // 更新数据
    animalList.value = records
    total.value = data.total || 0
  } catch (error) {
    console.error('获取动物列表失败:', error)
    ElMessage.error('获取动物列表失败')
  } finally {
    loading.value = false
  }
}

// 筛选
const handleFilter = () => {
  currentPage.value = 1
  fetchAnimalList()
}

// 重置筛选
const resetFilter = () => {
  filterForm.species = ''
  filterForm.gender = ''
  filterForm.adoptionStatus = ''
  currentPage.value = 1
  fetchAnimalList()
}

// 页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchAnimalList()
}

// 每页条数变化
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchAnimalList()
}

// 编辑动物
const editAnimal = (animal) => {
  router.push(`/admin/animals/edit/${animal.id}`)
}

// 删除动物
const deleteAnimal = (animal) => {
  ElMessageBox.confirm(`确定要删除动物"${animal.name}"吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      await deleteAnimalApi({ id: animal.id })
      ElMessage.success('删除成功')
      
      // 重新加载数据
      fetchAnimalList()
    } catch (error) {
      console.error('删除动物失败:', error)
      ElMessage.error('删除动物失败')
    }
  }).catch(() => {
    // 用户取消操作
  })
}

onMounted(() => {
  fetchAnimalList()
})
</script>

<style scoped>
.admin-animal-list-page {
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

.animal-table-container {
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