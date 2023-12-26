import { defineStore } from 'pinia'
import { ref } from 'vue'

// 这个没用到，因为每个组件获取数据的顺序不固定，如果存进来的话也是可以的，但是有点脱裤子放屁了。
export const useBigScreenStore = defineStore(
  'bigScreenStore',
  () => {
    // 定义userList，用于柱状图和饼图的展示
    const userInfoList = ref(null)
    // 定义regionInfoList，用于排名的展示
    const regionInfoList = ref(null)

    const getUserInfoList = () => {
      return userInfoList
    }
    const setUserInfoList = (list) => {
      userInfoList.value = list
    }

    const getRegionInfoList = () => {
      return regionInfoList
    }
    const setRegionInfoList = (list) => {
      regionInfoList.value = list
    }
    // 暴露出去
    return {
      getUserInfoList,
      setUserInfoList,
      getRegionInfoList,
      setRegionInfoList
    }
  },
  {
    persist: true
  }
)
