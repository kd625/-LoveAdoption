<script setup>
import { onMounted } from 'vue'
import { useUserStore } from './stores/userStore'
import HelloWorld from './components/HelloWorld.vue'

const userStore = useUserStore()

// 在应用初始化时检查用户登录状态
onMounted(() => {
  // 无论本地状态如何，总是尝试获取用户信息
  // 如果HTTP Session有效，会成功获取用户信息并更新状态
  // 如果HTTP Session无效，会清除本地状态
  userStore.fetchCurrentUser().catch(error => {
    console.error('获取用户信息失败:', error)
    // 错误处理已在fetchCurrentUser方法中完成
  })
})
</script>

<template>
  <router-view />
</template>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #333;
  background-color: #f5f7fa;
}

a {
  text-decoration: none;
  color: #409eff;
}
</style>
