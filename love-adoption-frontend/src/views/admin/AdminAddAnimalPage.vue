<template>
  <div class="admin-add-animal-page">
    <div class="page-header">
      <h1>添加动物</h1>
    </div>
    
    <div class="form-container">
      <el-form
        :model="animalForm"
        :rules="rules"
        ref="animalFormRef"
        label-width="100px"
        class="animal-form"
      >
        <el-form-item label="名称" prop="name">
          <el-input v-model="animalForm.name" placeholder="请输入动物名称"></el-input>
        </el-form-item>
        
        <el-form-item label="物种" prop="species">
          <el-select v-model="animalForm.species" placeholder="请选择物种">
            <el-option label="猫咪" value="猫咪" />
            <el-option label="狗狗" value="狗狗" />
            <el-option label="兔子" value="兔子" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        
        <el-form-item label="品种" prop="breed">
          <el-input v-model="animalForm.breed" placeholder="请输入品种"></el-input>
        </el-form-item>
        
        <el-form-item label="年龄" prop="age">
          <el-input-number v-model="animalForm.age" :min="0" :max="20" placeholder="请输入年龄"></el-input-number>
        </el-form-item>
        
        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="animalForm.gender">
            <el-radio :label="0">公</el-radio>
            <el-radio :label="1">母</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="健康状况" prop="health_status">
          <el-input v-model="animalForm.health_status" placeholder="请输入健康状况"></el-input>
        </el-form-item>
        
        <el-form-item label="领养状态" prop="adoption_status">
          <el-radio-group v-model="animalForm.adoption_status">
            <el-radio :label="0">可领养</el-radio>
            <el-radio :label="1">已领养</el-radio>
            <el-radio :label="2">已删除</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="图片" prop="image_url">
          <div class="animal-image-uploader">
            <img v-if="animalForm.image_url" :src="animalForm.image_url" class="animal-image" />
            <div v-else class="upload-placeholder">
              <FileUpload
                :tip="'支持jpg、png格式，不超过5MB'"
                :max-size="5"
                :validate-file-type="validateImageType"
                @upload-success="handleImageSuccess"
              >
                <el-button type="primary">
                  <el-icon><el-icon-plus /></el-icon>
                  上传图片
                </el-button>
              </FileUpload>
            </div>
          </div>
          <div class="upload-tip">点击上传图片，只能上传jpg/png文件，且不超过5MB</div>
          <div v-if="animalForm.image_url" class="change-image">
            <FileUpload
              :tip="'支持jpg、png格式，不超过5MB'"
              :max-size="5"
              :validate-file-type="validateImageType"
              @upload-success="handleImageSuccess"
            >
              <el-button type="primary" size="small">
                更换图片
              </el-button>
            </FileUpload>
          </div>
        </el-form-item>
        
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="animalForm.description"
            type="textarea"
            :rows="4"
            placeholder="请输入动物描述"
          ></el-input>
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="submitForm">提交</el-button>
          <el-button @click="$router.push('/admin/animals')">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus as ElIconPlus } from '@element-plus/icons-vue'
import { addAnimalApi } from '../../api/animal'
import FileUpload from '../../components/FileUpload.vue'

const router = useRouter()
const animalFormRef = ref(null)
const loading = ref(false)

// 表单数据
const animalForm = reactive({
  name: '',
  species: '',
  breed: '',
  age: 0,
  gender: 0,
  health_status: '',
  adoption_status: 0, // 默认可领养
  image_url: '',
  description: '',
  userId: 0 // 会被后端自动填充为当前登录的管理员ID
})

// 表单验证规则
const rules = {
  name: [
    { required: true, message: '请输入动物名称', trigger: 'blur' }
  ],
  species: [
    { required: true, message: '请选择物种', trigger: 'change' }
  ],
  breed: [
    { required: true, message: '请输入品种', trigger: 'blur' }
  ],
  age: [
    { required: true, message: '请输入年龄', trigger: 'blur' }
  ],
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  health_status: [
    { required: true, message: '请输入健康状况', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入动物描述', trigger: 'blur' }
  ],
  image_url: [
    { required: true, message: '请上传动物图片', trigger: 'change' }
  ]
}

// 图片类型校验
const validateImageType = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  if (!isJPG) {
    ElMessage.error('上传图片只能是 JPG 或 PNG 格式!')
    return false
  }
  return true
}

// 图片上传成功的回调
const handleImageSuccess = (response) => {
  animalForm.image_url = response
  ElMessage.success('图片上传成功')
}

// 提交表单
const submitForm = () => {
  animalFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    loading.value = true
    try {
      await addAnimalApi(animalForm)
      ElMessage.success('添加动物成功')
      router.push('/admin/animals')
    } catch (error) {
      console.error('添加动物失败:', error)
      ElMessage.error('添加动物失败')
    } finally {
      loading.value = false
    }
  })
}
</script>

<style scoped>
.admin-add-animal-page {
  padding: 20px;
}

.page-header {
  margin-bottom: 20px;
}

.page-header h1 {
  font-size: 24px;
  color: #303133;
  margin: 0;
}

.form-container {
  background-color: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.animal-form {
  max-width: 600px;
}

.animal-image-uploader {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  width: 178px;
  height: 178px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.animal-image-uploader:hover {
  border-color: #409EFF;
}

.upload-placeholder {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 100%;
}

.animal-image {
  width: 178px;
  height: 178px;
  display: block;
  object-fit: cover;
}

.upload-tip {
  font-size: 12px;
  color: #606266;
  margin-top: 10px;
}

.change-image {
  margin-top: 10px;
}
</style> 