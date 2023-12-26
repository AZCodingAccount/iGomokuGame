import { useUserIderStore } from '@/stores/userIder'
import axios from 'axios'
import router from '@/router'
import { ElMessage } from 'element-plus'
import 'element-plus/theme-chalk/el-loading.css'
import 'element-plus/theme-chalk/el-message.css'
import { useAdminStore } from '@/stores/adminInfo'

// 拦截器
let baseURL = ''
// 根据不同环境配置不同的url
if (import.meta.env.MODE === 'development') {
  // baseURL = 'http://192.168.253.191:8080'
  // baseURL = 'http://10.51.15.145:8080'
  // baseURL = 'http://localhost:8080'
  baseURL = 'http://localhost:8080/api'
} else {
  baseURL = 'http://39.105.211.248:8080/api'
}

const instance = axios.create({
  baseURL,
  timeout: 10000 // 请求超时时间
})

instance.interceptors.request.use(
  // 给配置对象加上token
  (config) => {
    const userIderStore = useUserIderStore() // 先从用户这里拿，拿不到去管理员那里拿
    const adminStore = useAdminStore() // 获取管理员的仓库
    const path = config.url
    const method = config.method
    // 用户反馈，带上用户的jwt
    if (method.toUpperCase() == 'POST' && path === '/management/feedbacks') {
      config.headers.jwt = userIderStore.userInfo.jwt
    } else if (path.startsWith('/analysis') || path.startsWith('/management')) {
      // console.log(adminStore.getAdminInfo().jwt)
      config.headers.jwt = adminStore.getAdminInfo().jwt
    } else {
      config.headers.jwt = userIderStore.userInfo.jwt
    }
    return config
  },
  (err) => Promise.reject(err)
)

instance.interceptors.response.use(
  (res) => {
    if (res.data.code === 1) {
      return res.data
    }
    ElMessage({ message: res.data.msg || '未知异常，请联系管理员', type: 'error' })
    return Promise.reject(res.data)
  },
  (err) => {
    if (!err.response) {
      ElMessage({ message: '服务器无法访问，请稍后再试。', type: 'error' })
    } else {
      ElMessage({
        message: err.response.data.msg || '未知异常，请联系管理员',
        type: 'error'
      })
      // 如果是401，跳转到登录页面
      if (err.response?.status === 401) {
        router.push('/introduction')
      }
    }
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }
