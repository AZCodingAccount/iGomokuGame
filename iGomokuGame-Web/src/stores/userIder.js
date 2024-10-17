import { defineStore } from 'pinia'
import { ref } from 'vue'
import { timestampToTime } from '@/utils/time'
import { baseURL } from '@/utils/request'
export const useUserIderStore = defineStore(
  'userIder',
  () => {
    // data
    const userInfo = ref({
      id: -1,
      username: '',
      nickname: '',
      userLevel: '',
      imageUrl: '',
      online: '',
      description: '这个用户很懒，还没有自我介绍~',
      lastOnlineTime: '',
      jwt: '',
      rankingList: []
    })
    const setUserInfo = (data) => {      
      userInfo.value = { ...data }
    }
    const setMyImageUrl = (url) => {
      userInfo.value.imageUrl = url
    }

    const loginForm = ref({}) // 存储用户名和密码，用于刷新排行榜
    const setLoginForm = (data) => {
      loginForm.value = data
    }

    const chatSocket = ref(null)
    const allChatInfo = ref([])
    const limitConnect = ref(5)
    const lockReconnect = ref(false)
    const getChatSocket = () => {
      const url = new URL(baseURL)
      const hostAndPort = `${url.hostname}:${url.port}`
      chatSocket.value = new WebSocket('ws://' + hostAndPort + '/user/friend/' + userInfo.value.id)
      chatSocket.value.onopen = (event) => {
        console.log('chatSocket open', event)
      }
      chatSocket.value.onclose = (event) => {
        console.log('chatSocket close', event)
        if (event.code != 3000) reconnectChatSocket()
      }
      chatSocket.value.onerror = (event) => {
        console.log('chatSocket error', event)
        reconnectChatSocket()
      }
      chatSocket.value.onmessage = (event) => {
        const obj = JSON.parse(event.data)
        allChatInfo.value.push({
          userId: obj.userId,
          targetUserId: obj.targetUserId,
          nickname: obj.nickname,
          message: obj.message,
          messageTime: timestampToTime(new Date())
        })
      }
      chatSocket.value.close
    }
    const chatSocketSendObj = (obj) => {
      const json = JSON.stringify(obj)
      if (chatSocket.value.url != undefined) chatSocket.value.send(json)
      else {
        getChatSocket()
        chatSocket.value.send(json)
      }
    }
    const closeChatSochet = () => {
      if (chatSocket.value.url != undefined) chatSocket.value.close(3000)
    }
    const reconnectChatSocket = () => {
      if (!lockReconnect.value && limitConnect.value > 0) {
        lockReconnect.value = true
        limitConnect.value--
        console.log('第 ' + (5 - limitConnect.value) + ' 次重连chatSocket')
        setTimeout(() => {
          getChatSocket()
          lockReconnect.value = false
        }, 5000)
      } else {
        if (limitConnect.value == 0) console.log('TCP连接已超时')
      }
    }

    return {
      userInfo,
      setUserInfo,
      setMyImageUrl,
      loginForm,
      setLoginForm,
      chatSocket,
      getChatSocket,
      chatSocketSendObj,
      closeChatSochet,
      allChatInfo
    }
  },
  {
    persist: true
  }
)
