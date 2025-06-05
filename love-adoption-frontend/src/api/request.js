import axios from 'axios'
import { ElMessage } from 'element-plus'

// 创建axios实例
const request = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  timeout: 10000, // 请求超时时间
  withCredentials: true // 允许跨域请求携带cookie，对于Session认证必须设为true
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    // 使用HTTP Session认证，不需要手动添加token
    // cookie会自动被浏览器携带
    return config
  },
  error => {
    console.error('请求错误：', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    const res = response.data
    // 如果响应状态码不为0，则判断为错误
    if (res.code !== 0) {
      ElMessage({
        message: res.message || '请求失败',
        type: 'error',
        duration: 5 * 1000
      })
      
      // 如果是未登录或session过期
      if (res.code === 401) {
        // 清除本地用户信息
        localStorage.removeItem('userInfo')
        // 重定向到登录页
        setTimeout(() => {
          window.location.href = '/login'
        }, 1500)
      }
      
      return Promise.reject(new Error(res.message || '请求失败'))
    } else {
      // 返回数据中的data字段
      return res.data
    }
  },
  error => {
    console.error('响应错误：', error)
    ElMessage({
      message: error.message || '网络错误',
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
)

export default request 