import { defineStore } from 'pinia'
import { ref } from 'vue'
export const useAdminStore = defineStore(
  'adminInfo',
  () => {
    const adminInfo = ref({
      username: '',
      password: '',
      nickname: '超级管理员', // 默认
      jwt: '',
      isRememberMe: true
    })
    const setToken = (jwt) => {
      adminInfo.value.jwt = jwt
    }
    const getAdminInfo = () => {
      return adminInfo.value
    }
    const logout = () => {
      // 勾选了记住我
      if (adminInfo.value.isRememberMe) {
        adminInfo.value.jwt = ''
      } else {
        adminInfo.value = {
          username: '',
          password: '',
          nickname: '超级管理员', // 默认
          jwt: '',
          isRememberMe: true
        }
      }
    }
    return {
      adminInfo,
      setToken,
      getAdminInfo,
      logout
    }
  },
  {
    persist: true
  }
)
