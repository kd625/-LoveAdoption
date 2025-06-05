<template>
  <div class="home-page">
    <div class="page-header">
      <h1>欢迎来到爱心领养网站</h1>
      <p>这里有许多可爱的小动物等待着您的领养</p>
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
        <el-form-item>
          <el-button type="primary" @click="handleFilter" :loading="loading">筛选</el-button>
          <el-button @click="resetFilter" :disabled="loading">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="animal-list" v-loading="loading">
      <el-empty v-if="animalList.length === 0 && !loading" description="暂无动物信息"></el-empty>
      <el-row :gutter="20" v-else>
        <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="animal in animalList" :key="animal.id">
          <el-card class="animal-card" shadow="hover" @click="viewAnimalDetail(animal.id)">
            <div class="animal-image">
              <img :src="animal.image_url || 'https://via.placeholder.com/300x200'" alt="动物图片" />
            </div>
            <div class="animal-info">
              <h3>{{ animal.name }}</h3>
              <p>物种：{{ animal.species }}</p>
              <p>性别：{{ animal.gender === 0 ? '公' : '母' }}</p>
              <p>年龄：{{ animal.age }}岁</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
    
    <div class="pagination-container" v-if="total > 0">
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
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { listAnimalByPageApi } from '../api/animal'

const router = useRouter()
const animalList = ref([])
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(8)
const loading = ref(false)

const filterForm = reactive({
  species: '',
  gender: '',
  adoptionStatus: 0  // 默认只显示可领养的动物
})

// 获取动物列表
const fetchAnimalList = async () => {
  loading.value = true
  try {
    // 构建请求参数
    const params = {
      current: currentPage.value,
      pageSize: pageSize.value,
      adoptionStatus: 0  // 始终只显示可领养的动物
    }
    
    // 添加可选参数
    if (filterForm.species) {
      params.species = filterForm.species
    }
    
    if (filterForm.gender) {
      params.gender = filterForm.gender
    }
    
    // 调用后端接口
    const data = await listAnimalByPageApi(params)
    
    // 更新数据
    animalList.value = data.records || []
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
  currentPage.value = 1
  fetchAnimalList()
}

// 页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchAnimalList()
}

// 查看动物详情
const viewAnimalDetail = (id) => {
  router.push(`/animal/${id}`)
}

onMounted(() => {
  fetchAnimalList()
})
</script>

<style scoped>
.home-page {
  padding: 20px 0;
}

.page-header {
  text-align: center;
  margin-bottom: 30px;
}

.page-header h1 {
  font-size: 28px;
  color: #303133;
  margin-bottom: 10px;
}

.page-header p {
  font-size: 16px;
  color: #606266;
}

.filter-section {
  margin-bottom: 20px;
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.animal-list {
  margin-bottom: 20px;
}

.animal-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.animal-card:hover {
  transform: translateY(-5px);
}

.animal-image {
  height: 200px;
  overflow: hidden;
}

.animal-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.animal-info {
  padding: 15px 0 5px;
}

.animal-info h3 {
  margin-bottom: 10px;
  font-size: 18px;
}

.animal-info p {
  margin-bottom: 5px;
  font-size: 14px;
  color: #606266;
}

.pagination-container {
  text-align: center;
  margin-top: 30px;
}
</style> 