import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver()],
    }),
  ],
  server: {
    proxy: {
      '/api': { // 将匹配到 /api 的请求代理到 target
        target: 'http://localhost:8080', // 后端API地址
        changeOrigin: true, // 需要虚拟主机站点
        // rewrite: (path) => path.replace(/^\/api/, '') // 如果后端接口路径没有 /api 前缀，则需要重写
      }
    }
  }
})
