<template>
  <div class="file-upload-component">
    <el-upload
      v-if="!customRequest"
      :action="uploadUrl"
      :headers="headers"
      :show-file-list="showFileList"
      :multiple="multiple"
      :limit="limit"
      :accept="accept"
      :disabled="disabled"
      :on-success="handleUploadSuccess"
      :on-error="handleUploadError"
      :on-exceed="handleExceed"
      :before-upload="beforeUpload"
      :list-type="listType"
      :auto-upload="autoUpload"
      :drag="drag"
    >
      <slot>
        <el-button type="primary">点击上传</el-button>
        <div class="el-upload__tip" v-if="tip">{{ tip }}</div>
      </slot>
    </el-upload>
    
    <el-upload
      v-else
      :show-file-list="showFileList"
      :multiple="multiple"
      :limit="limit"
      :accept="accept"
      :disabled="disabled"
      :http-request="customUploadRequest"
      :on-exceed="handleExceed"
      :before-upload="beforeUpload"
      :list-type="listType"
      :auto-upload="autoUpload"
      :drag="drag"
    >
      <slot>
        <el-button type="primary">点击上传</el-button>
        <div class="el-upload__tip" v-if="tip">{{ tip }}</div>
      </slot>
    </el-upload>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { uploadFileApi } from '../api/common'

const props = defineProps({
  // 是否使用自定义上传请求
  customRequest: {
    type: Boolean,
    default: true
  },
  // 上传URL，当customRequest为false时使用
  action: {
    type: String,
    default: '/api/admin/common/upload'
  },
  // 请求头，当customRequest为false时使用
  headers: {
    type: Object,
    default: () => ({})
  },
  // 是否显示已上传文件列表
  showFileList: {
    type: Boolean,
    default: false
  },
  // 是否支持多选
  multiple: {
    type: Boolean,
    default: false
  },
  // 最大允许上传个数
  limit: {
    type: Number,
    default: 1
  },
  // 接受上传的文件类型
  accept: {
    type: String,
    default: 'image/*'
  },
  // 是否禁用
  disabled: {
    type: Boolean,
    default: false
  },
  // 上传文件列表的类型
  listType: {
    type: String,
    default: 'text' // text, picture, picture-card
  },
  // 是否自动上传
  autoUpload: {
    type: Boolean,
    default: true
  },
  // 是否启用拖拽上传
  drag: {
    type: Boolean,
    default: false
  },
  // 上传提示文本
  tip: {
    type: String,
    default: ''
  },
  // 文件大小限制（MB）
  maxSize: {
    type: Number,
    default: 5
  },
  // 文件类型校验函数
  validateFileType: {
    type: Function,
    default: null
  }
})

const emit = defineEmits(['upload-success', 'upload-error', 'exceed', 'before-upload'])

// 计算上传URL
const uploadUrl = computed(() => {
  return props.action
})

// 自定义上传请求
const customUploadRequest = async (options) => {
  const { file, onProgress, onSuccess, onError } = options
  
  // 创建FormData对象
  const formData = new FormData()
  formData.append('file', file)
  
  try {
    // 调用API上传文件
    const result = await uploadFileApi(formData)
    
    // 上传成功
    onSuccess(result)
    handleUploadSuccess(result, file)
  } catch (error) {
    // 上传失败
    onError(error)
    handleUploadError(error, file)
  }
}

// 上传成功处理
const handleUploadSuccess = (response, file) => {
  if (response) {
    ElMessage.success('文件上传成功')
    emit('upload-success', response, file)
  }
}

// 上传失败处理
const handleUploadError = (error, file) => {
  ElMessage.error('文件上传失败')
  emit('upload-error', error, file)
}

// 文件超出限制处理
const handleExceed = (files, fileList) => {
  ElMessage.warning(`当前限制选择 ${props.limit} 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`)
  emit('exceed', files, fileList)
}

// 上传前校验
const beforeUpload = (file) => {
  // 文件大小校验
  const isLtMaxSize = file.size / 1024 / 1024 < props.maxSize
  if (!isLtMaxSize) {
    ElMessage.error(`文件大小不能超过 ${props.maxSize}MB!`)
    return false
  }
  
  // 自定义文件类型校验
  if (props.validateFileType && !props.validateFileType(file)) {
    return false
  }
  
  emit('before-upload', file)
  return true
}
</script>

<style scoped>
.file-upload-component {
  display: inline-block;
}
</style> 