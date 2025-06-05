import { defineStore } from 'pinia'
import { userLoginApi, userLogoutApi, userRegisterApi, getLoginUserApi } from '../api/user'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', {
  state: () => ({
    isAuthenticated: localStorage.getItem('isAuthenticated') === 'true',
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || null
  }),
  
  getters: {
    isLoggedIn: (state) => state.isAuthenticated,
    isAdmin: (state) => state.userInfo?.userRole === 'admin'
  },
  
  actions: {
    /**
     * 用户登录
     * @param {Object} credentials - 登录凭证 {userAccount, userPassword, checkPassword}
     */
    async login(credentials) {
      try {
        const data = await userLoginApi(credentials)
        
        // 设置登录状态和用户信息
        this.isAuthenticated = true
        this.userInfo = {
          id: data.id,
          account: data.userAccount,
          userName: data.userName,
          userRole: data.userRole,
          userAvatar: data.userAvatar,
          gender: data.gender
        }
        
        // 存储到本地
        localStorage.setItem('isAuthenticated', 'true')
        localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
        
        ElMessage.success('登录成功')
        return data
      } catch (error) {
        console.error('登录失败:', error)
        throw error
      }
    },
    
    /**
     * 用户注册
     * @param {Object} data - 注册信息
     */
    async register(data) {
      try {
        const result = await userRegisterApi(data)
        ElMessage.success('注册成功，请登录')
        return result
      } catch (error) {
        console.error('注册失败:', error)
        throw error
      }
    },
    
    /**
     * 用户登出
     */
    async logout() {
      try {
        await userLogoutApi()
        
        // 清除登录状态和用户信息
        this.isAuthenticated = false
        this.userInfo = null
        localStorage.removeItem('isAuthenticated')
        localStorage.removeItem('userInfo')
        
        ElMessage.success('退出登录成功')
      } catch (error) {
        console.error('登出失败:', error)
        
        // 即使API调用失败，也清除本地状态
        this.isAuthenticated = false
        this.userInfo = null
        localStorage.removeItem('isAuthenticated')
        localStorage.removeItem('userInfo')
      }
    },
    
    /**
     * 获取当前登录用户信息
     */
    async fetchCurrentUser() {
      if (!this.isAuthenticated) return null
      
      try {
        const data = await getLoginUserApi()
        
        // 更新用户信息
        this.userInfo = {
          id: data.id,
          account: data.userAccount,
          userName: data.userName,
          userRole: data.userRole,
          userAvatar: data.userAvatar,
          gender: data.gender
        }
        
        // 更新本地存储
        localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
        
        return this.userInfo
      } catch (error) {
        console.error('获取用户信息失败:', error)
        
        // 如果获取失败（可能是session过期），清除登录状态
        if (error.response && error.response.status === 401) {
          this.isAuthenticated = false
          this.userInfo = null
          localStorage.removeItem('isAuthenticated')
          localStorage.removeItem('userInfo')
        }
        
        throw error
      }
    },
    
    /**
     * 更新用户信息
     * @param {Object} userInfo - 用户信息
     */
    updateUserInfo(userInfo) {
      this.userInfo = { ...this.userInfo, ...userInfo }
      localStorage.setItem('userInfo', JSON.stringify(this.userInfo))
    }
  }
}) 