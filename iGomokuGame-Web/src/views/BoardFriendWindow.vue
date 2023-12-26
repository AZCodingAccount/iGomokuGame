<script setup>
// 实现联机对战

import { ref, reactive, watch, nextTick } from 'vue'
import { useUserIderStore } from '@/stores/userIder.js'
import { ElMessage } from 'element-plus'
import router from '@/router'
import { getUserInfo } from '@/api/account'
import { baseURL } from '@/utils/request'
const boardStates = reactive(
  Array(15)
    .fill()
    .map(() => Array(15).fill(0))
)
const stepOrder = ref(1) // 当前步数
const gameId = ref(-1) // 游戏ID，从后端获取
const currentPlayer = ref(1) // 1 代表黑棋, 2 代表白棋
const userIderStore = useUserIderStore()
const id = userIderStore.userInfo.id // 我的id
const myInfo = ref({}) // 我的个人信息
const blackUrl = ref('/assets/pieces/black.png')
const whiteUrl = ref('/assets/pieces/white.png')
const roomId = ref('') // 房间号
const verificationCodes = ref(['', '', '', '', '', '']) // 房间号输入框
const friendDialogVisible = ref(true) // 控制输入房间号对话框
const lookerDialogVisible = ref(false) // 控制选择是否观战的对话框
const playerType = ref('') // 当前用户类型：观战者，执黑棋者 或 持白棋者
const fullscreenLoading = ref(false) // 控制加载界面显示
const chatMessage = ref('') // 聊天信息
const playSocket = ref({}) // websocket
const isWait = ref(false) // 是否在等待对手下棋
const actors = ref([])
const chatMessageInp = ref(false)
const isGameing = ref(false) // 游戏是否进行当中
// 计时器
import { computed } from 'vue'
const timer = ref(0)
let intervalId = null
const timerRunning = ref(false)
function startTimer() {
  if (!timerRunning.value) {
    intervalId = setInterval(() => {
      timer.value++
    }, 1000)
    timerRunning.value = true
  }
}
function stopTimer() {
  if (intervalId) {
    clearInterval(intervalId)
    intervalId = null
    timerRunning.value = false
  }
}
const formatTime = computed(() => {
  const minutes = Math.floor(timer.value / 60)
  const seconds = timer.value % 60
  return `${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}`
})

const gameOverToCN = (num) => {
  if (num == 1) {
    return '黑棋胜'
  } else if (num == 2) {
    return '白棋胜'
  } else {
    return '平局'
  }
}

// 房间号输入框
const handleInput = (index, event) => {
  const value = event.target.value
  verificationCodes.value[index] = value

  // 自动跳到下一个输入框
  if (value && index < verificationCodes.value.length - 1) {
    const nextInput = event.target.nextElementSibling
    if (nextInput) {
      nextTick(() => {
        nextInput.focus()
      })
    }
  }
}
const handleKeyDown = (index, event) => {
  // 处理删除操作
  if (event.key === 'Backspace' && !event.target.value && index > 0) {
    const prevInput = event.target.previousElementSibling
    if (prevInput) {
      nextTick(() => {
        prevInput.focus()
      })
    }
  }
}
const onGameWithFriend = async () => {
  if (verificationCodes.value.join('').length < 5) {
    ElMessage.error('请输入正确的房间号')
    return
  }
  let res = await getUserInfo(id)
  myInfo.value = res.data
  friendDialogVisible.value = false
  roomId.value = verificationCodes.value.join('')
  const url = new URL(baseURL)
  const hostAndPort = `${url.hostname}:${url.port}`
  playSocket.value = new WebSocket(
    'ws://' + hostAndPort + '/game/online/gomoku/' + roomId.value + '/' + id
  )
  playSocket.value.onopen = (event) => {
    console.log('socket open', event)
  }
  playSocket.value.onclose = () => {
    isGameing.value = false // 不能下棋
    chatMessageInp.value = true // 不能聊天
    stopTimer() // 停止计时
    ElMessage('连接断开')
  }
  playSocket.value.onerror = (event) => {
    console.log('error', event)
  }
  playSocket.value.onmessage = (event) => {
    const obj = JSON.parse(event.data)
    // console.log('obj', obj)
    if (obj.type == 0) {
      // 警告消息（表示当前房间用户超过两人了）——后端
      fullscreenLoading.value = false // 关闭等待窗口
      lookerDialogVisible.value = true // 打开是否观战选择窗口
    } else if (obj.type == 2) {
      // 标识本次返回的是用户的角色—后端
      isGameing.value = true
      playerType.value = obj.role
      gameId.value = obj.gameId
      if (playerType.value == '执白棋者') {
        isWait.value = true
        currentPlayer.value = 2
      } else {
        isWait.value = false
        currentPlayer.value = 1
      }
      // init
      fullscreenLoading.value = false // 关闭等待页面
      const json = JSON.stringify({
        type: 3,
        role: playerType.value,
        message: '-对战开始 -'
      })
      playSocket.value.send(json)
      addLogList({
        name: myInfo.value.nickname,
        message: '-对战开始 -'
      })
      startTimer()
    } else if (obj.type == 3) {
      // 收到聊天消息
      if (myInfo.value.id != obj.id) {
        // 排除收到自己的广播
        if (obj.role == '执黑棋者') {
          showBlackerMessage(obj.message)
        } else if (obj.role == '执白棋者') {
          showWhiterMessage(obj.message)
        } else {
          addLogList({
            name: actors.value.filter((item) => item.id == obj.id)[0].nickname,
            message: obj.message
          })
        }
      }
    } else if (obj.type == 4) {
      // 收到对手下棋的信息
      let temp = obj.message.split(',')
      const x = temp[0].substring(1)
      const y = temp[1].substring(0, temp[1].length - 1)
      if (playerType.value != '观战者') {
        // 非观战者
        if (obj.isGameOver == 0) {
          if (obj.role != playerType.value) {
            // 排除收到自己的广播
            boardStates[x][y] = (currentPlayer.value % 2) + 1 // 下对手的棋子
            playChess.value.play() // 下棋的音效
            isWait.value = false
            // addLogList({
            //   name: actors.value.filter((item) => item.role == obj.role)[0].nickname + '下棋：',
            //   message: '第' + (+x + 1) + '行 第' + (+y + 1) + '列'
            // })
          }
          // 真的，完全不考虑一点用户易用性吗？？？什么提示都不给。后端又不是没返回状态
        } else {
          // 游戏结束 参与者视角
          boardStates[x][y] = obj.role == '执黑棋者' ? 1 : 2
          playChess.value.play() // 下棋的音效
          addLogList({
            name: actors.value.filter(
              (item) => item.role == (obj.isGameOver == 1 ? '执黑棋者' : '执白棋者')
            )[0].nickname,
            message: gameOverToCN(obj.isGameOver)
          })
          if (obj.isGameOver == 1) {
            ElMessage.success(`游戏结束，执黑子者胜，您的房间将被断开`)
          } else {
            ElMessage.success(`游戏结束，执白子者胜，您的房间将被断开`)
          }
        }
      } else {
        // 观战者收到下棋消息
        boardStates[x][y] = currentPlayer.value
        playChess.value.play() // 下棋的音效
        currentPlayer.value = (currentPlayer.value % 2) + 1
        // addLogList({
        //   name: actors.value.filter((item) => item.role == obj.role)[0].nickname + '下棋：',
        //   message: '第' + (+x + 1) + '行 第' + (+y + 1) + '列'
        // })
        if (obj.isGameOver != 0) {
          // 游戏结束 观察者视角
          addLogList({
            name: actors.value.filter((item) => item.role == obj.role)[0].nickname,
            message: gameOverToCN(obj.isGameOver)
          })
          if (obj.isGameOver == 1) {
            ElMessage.success(`游戏结束，执黑子者胜，您的房间将被断开`)
          } else {
            ElMessage.success(`游戏结束，执白子者胜，您的房间将被断开`)
          }
        }
      }
    } else if (obj.type == 5) {
      // 互动消息
    } else if (obj.type == 6) {
      // 房间人数变化消息
      fullscreenLoading.value = false // 关闭等待窗口
      // 渲染左信息框和观战者框
      actors.value = obj.actors
      setBlacker(obj.actors.filter((item) => item.role == '执黑棋者')[0])
      setWhiter(obj.actors.filter((item) => item.role == '执白棋者')[0])
    } else if (obj.type == 7) {
      // 观察者后进入房间时用于渲染棋局
      let num1 = 0,
        num2 = 0
      for (let i = 0; i < 15; i++) {
        for (let j = 0; j < 15; j++) {
          boardStates[i][j] = obj.message[i][j]
          if (boardStates[i][j] == 1) {
            num1++
          } else if (boardStates[i][j] == 2) {
            num2++
          }
        }
      }
      currentPlayer.value = num1 == num2 ? 1 : 2
    }
  }
  // 第一个人创建棋局，等待好友进入
  fullscreenLoading.value = true
}
window.onbeforeunload = () => {
  playSocket.value.close()
}
// 棋盘
const showHover = reactive(
  Array(15)
    .fill()
    .map(() => Array(15).fill(false))
) // 悬停状态数组

const playChess = ref(null)
const sendMoveToServer = async (x, y) => {
  // console.log(x, y)
  const json = JSON.stringify({
    type: 4,
    role: playerType.value,
    stepOrder: stepOrder.value,
    gameId: gameId.value,
    message: '(' + x + ',' + y + ')'
  }) // 组装发送数据
  playSocket.value.send(json)
}
const makeMove = (x, y) => {
  // 更新棋盘状态的逻辑
  if (boardStates[x][y] !== 0 || isWait.value || !isGameing.value) return // 下过棋或者在等待人机下棋都不能再次下棋了

  playChess.value.play() // 下棋的音效
  boardStates[x][y] = currentPlayer.value // 玩家落子
  stepOrder.value++
  isWait.value = true
  // addLogList({
  //   name: userIderStore.userInfo.nickname + '下棋：',
  //   message: '第' + (x + 1) + '行 第' + (y + 1) + '列'
  // })
  setTimeout(() => {
    sendMoveToServer(x, y)
  }, 1100)
}
const handleMouseOver = (x, y) => {
  // console.log('鼠标悬停', x, y)
  if (x >= 0 && x < 15 && y >= 0 && y < 15) {
    showHover[x][y] = 1
  }
}
const handleMouseLeave = (x, y) => {
  // console.log('鼠标离开', x, y)
  if (x >= 0 && x < 15 && y >= 0 && y < 15) {
    showHover[x][y] = 0
  }
}
const isStarPosition = (x, y) => {
  // 定义星位的坐标
  const starPositions = [
    { x: 3, y: 3 },
    { x: 3, y: 11 },
    { x: 11, y: 3 },
    { x: 11, y: 11 },
    { x: 7, y: 7 }
    // 如果棋盘足够大，还可以添加中心点 { x: 7, y: 7 }
  ]
  // 检查当前坐标是否是星位
  return starPositions.some((position) => position.x === x && position.y === y)
}

//气泡聊天
const friendVisibled = ref(false)
const friendMessage = ref('好友信息')
const showWhiterMessage = async (message) => {
  addLogList({
    name: actors.value.filter((item) => item.role == '执白棋者')[0].nickname + '：',
    message: message
  })
  friendMessage.value = message
  friendVisibled.value = true
  await setTimeout(() => {
    friendVisibled.value = false
  }, 5000)
}
const myVisibled = ref(false)
const myMessage = ref('我的信息')
const showBlackerMessage = async (message) => {
  addLogList({
    name: actors.value.filter((item) => item.role == '执黑棋者')[0].nickname + '：',
    message: message
  })
  myMessage.value = message
  myVisibled.value = true
  await setTimeout(() => {
    myVisibled.value = false
  }, 5000)
}

// 日志
const logList = ref([])
const addLogList = (obj) => {
  logList.value.push(obj)
}
const scroll = ref(null)
watch(logList.value, async () => {
  await nextTick()
  scroll.value.setScrollTop(2000000005)
})

// 发送聊天信息
const sendChatMessage = (message) => {
  if (message == '') {
    ElMessage.error('发送的消息不能为空')
    return
  }
  const json = JSON.stringify({
    type: 3,
    role: playerType.value,
    message: message
  })
  playSocket.value.send(json)

  if (playerType.value == '执黑棋者') {
    showBlackerMessage(message)
  } else if (playerType.value == '执白棋者') {
    showWhiterMessage(message)
  } else {
    // 观战者
    console.log('name', myInfo.value.nickname, 'message', message)
    addLogList({
      name: myInfo.value.nickname,
      message: message
    })
  }
  chatMessage.value = ''
}

// 取消观战
const noLooking = () => {
  playSocket.value.close()
  verificationCodes.value = ['', '', '', '', '', ''] // 清空房间号输入框
  friendDialogVisible.value = true // 打开输入房间号的对话框
  lookerDialogVisible.value = false // 关闭选择是否观战的对话框
}
// 确定观战
const onLooking = () => {
  const json = JSON.stringify({
    type: '1'
  })
  playSocket.value.send(json)
  playerType.value = '观战者'
  sendChatMessage('进入房间') // 发送 进入房间 信息
  isGameing.value = false // 不能下棋
  lookerDialogVisible.value = false // 关闭选择是否观战窗口
}

// 用于渲染左边信息框
const blacker = ref({})
const whiter = ref({})
const setBlacker = (data) => {
  blacker.value = data
}
const setWhiter = (data) => {
  whiter.value = data
}

const onOut = () => {
  if (playSocket.value.url != undefined) playSocket.value.close()
  router.push('/main/game')
}
</script>
<template>
  <div class="background">
    <el-image src="/img/background/44c233fed6164de785eddd57824a862d.jpg" />
  </div>
  <audio :preload="true" ref="playChess" style="display: none" src="/audios/5390.mp3"></audio>
  <!-- 输入房间号 -->
  <div style="position: relative; z-index: 2030">
    <el-dialog
      v-model="friendDialogVisible"
      width="31%"
      center
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <template #title>
        <span style="font-size: 30px">请输入房间号</span>
      </template>

      <div class="verification-container">
        <input
          v-for="(code, index) in verificationCodes"
          :key="index"
          v-model="verificationCodes[index]"
          @input="handleInput(index, $event)"
          @keydown="handleKeyDown(index, $event)"
          maxlength="1"
          class="verification-input"
        />
      </div>

      <template #footer>
        <span>
          <el-button @click="router.push('/main/game')">取消</el-button>
          <el-button type="primary" @click="onGameWithFriend"> 确定 </el-button>
        </span>
      </template>
    </el-dialog>
  </div>

  <div style="position: relative; z-index: 1">
    <!-- 是否观战 -->
    <el-dialog
      v-model="lookerDialogVisible"
      width="31%"
      center
      :show-close="false"
      :close-on-click-modal="false"
      :close-on-press-escape="false"
    >
      <template #title>
        <span style="font-size: 30px">房间满员，是否观战？</span>
      </template>

      <template #footer>
        <span>
          <el-button @click="noLooking">取消</el-button>
          <el-button type="primary" @click="onLooking"> 确定 </el-button>
        </span>
      </template>
    </el-dialog>

    <div style="text-align: center; margin-top: 100px">
      <!-- 观战者框 -->
      <div class="lookerBox">
        <div v-show="actors.length != 0">观战中</div>
        <el-scrollbar>
          <div style="display: flex">
            <div
              class="lookingItem"
              v-for="item in actors"
              :key="item"
              v-show="item.role == '观战者'"
            >
              <el-avatar size="large" :src="item.imageUrl" />
              <div>
                <div>{{ item.nickname }}</div>
                <div>{{ item.userLevel }}</div>
              </div>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </div>
    <div
      class="gomoku-container"
      v-loading.fullscreen.lock="fullscreenLoading"
      customClass="loading"
      :element-loading-text="'等待好友进入房间：' + roomId"
    >
      <div style="flex-grow: 1"></div>
      <!-- 左侧信息框 -->
      <el-card>
        <div style="line-height: 50px">
          <div>
            <!-- 黑棋 -->
            <div class="topShow">
              <div>{{ blacker.nickname }}</div>
              <div>{{ blacker.userLevel }}</div>
              <div>
                <span style="position: relative">
                  <!-- 气泡聊天框 -->
                  <el-popover placement="right" :visible="myVisibled" width="260px">
                    <span class="myMessage"> {{ myMessage }} </span>
                    <template #reference>
                      <el-avatar :size="100" :src="blacker.imageUrl" />
                    </template>
                  </el-popover>
                </span>
              </div>
            </div>
          </div>
          <div>
            <div class="check">
              <div style="flex-grow: 1"></div>
              <div
                v-show="
                  (isWait && playerType == '执白棋者') ||
                  (!isWait && playerType == '执黑棋者') ||
                  (currentPlayer == 1 && playerType == '观战者')
                "
                style="font-size: 35px; margin-right: 10px"
              >
                =>
              </div>
              <el-image style="width: 50px; height: 50px" :src="blackUrl" :fit="fit" />
              <div style="flex-grow: 1"></div>
            </div>
          </div>
          <div style="text-align: center; font-size: 60px">VS</div>
          <div>
            <!-- 白棋 -->
            <div class="check">
              <div style="flex-grow: 1"></div>
              <div
                v-show="
                  (isWait && playerType == '执黑棋者') ||
                  (!isWait && playerType == '执白棋者') ||
                  (currentPlayer == 2 && playerType == '观战者')
                "
                style="font-size: 35px; margin-right: 10px"
              >
                =>
              </div>
              <el-image style="width: 50px; height: 50px" :src="whiteUrl" :fit="fit" />
              <div style="flex-grow: 1"></div>
            </div>
          </div>
          <div>
            <div class="topShow">
              <div>{{ whiter.nickname }}</div>
              <div>{{ whiter.userLevel }}</div>
              <div>
                <span style="position: relative">
                  <!-- 气泡聊天框 -->
                  <el-popover placement="right" :visible="friendVisibled" width="260px">
                    <span class="friendMessage"> {{ friendMessage }} </span>
                    <template #reference>
                      <el-avatar :size="100" :src="whiter.imageUrl" />
                    </template>
                  </el-popover>
                </span>
              </div>
            </div>
          </div>
        </div>
      </el-card>
      <!-- 棋盘 -->
      <span style="margin-left: 200px">
        <div class="gomoku-board">
          <!-- 遍历棋盘上的每个交叉点 -->
          <div class="board-row" v-for="(row, x) in boardStates" :key="x">
            <!-- 遍历行中的每个交叉点 -->
            <div
              class="board-cell"
              v-for="(cell, y) in row"
              :key="y"
              @click="() => makeMove(x, y)"
              @mouseover="() => handleMouseOver(x, y)"
              @mouseleave="() => handleMouseLeave(x, y)"
            >
              <!-- 星位的标记 -->
              <div v-if="isStarPosition(x, y)" class="star-position"></div>
              <!-- 悬停预览 -->
              <div
                v-if="showHover[x][y]"
                class="hover-preview"
                :class="currentPlayer == 1 ? 'black-piece' : 'white-piece'"
              ></div>
              <!-- 实际棋子 -->
              <img
                v-if="cell === 1"
                src="/assets/pieces/black.png"
                class="pieces"
                alt="Black Piece"
              />
              <img
                v-if="cell === 2"
                src="/assets/pieces/white.png"
                class="pieces"
                alt="White Piece"
              />
              <!-- 悬停预览、实际棋子等 -->
            </div>
          </div>
        </div>
      </span>

      <!-- 右侧信息框 -->
      <el-card style="margin-left: 90px">
        <div class="infoBox" style="width: 400px">
          <el-card>
            <div class="count">
              <div>
                <span>回合数</span><span>{{ stepOrder }}</span>
              </div>
              <div>
                <span>游戏时间</span><span>{{ formatTime }}</span>
              </div>
            </div>
          </el-card>
          <el-card style="margin-top: 10px">
            <el-table
              height="350"
              style="width: 100%; position: relative"
              empty-text=" "
              :data="logList"
              :show-header="false"
            >
              <el-table-column prop="name" width="140" />
              <el-table-column prop="message" />
            </el-table>
          </el-card>
          <div style="text-align: right; margin-top: 20px">
            <div style="margin-bottom: 10px">
              <el-input
                :disabled="chatMessageInp"
                v-model="chatMessage"
                placeholder="说点什么吧..."
              >
                <template #append>
                  <el-button type="success" @click="sendChatMessage(chatMessage)">
                    <el-icon>
                      <Upload />
                    </el-icon>
                  </el-button>
                </template>
              </el-input>
            </div>
          </div>
        </div>
      </el-card>
      <div style="flex-grow: 1"></div>
    </div>
  </div>
  <el-popconfirm
    title="你确定要退出吗？"
    confirm-button-text="确定"
    cancel-button-text="取消"
    width="200"
    @confirm="onOut"
  >
    <template #reference>
      <el-button style="position: fixed; right: 160px; bottom: 50px; z-index: 2010" type="danger"
        >退出</el-button
      >
    </template>
  </el-popconfirm>
</template>

<style scoped>
.verification-container {
  text-align: center;
  width: 100%;
  height: 100%;
}

.verification-input {
  width: 54px;
  height: 54px;
  margin-right: 30px;
  text-align: center;
  font-size: 20px;
  border: 1px solid #ebebeb;
  border-radius: 5px;
}

.background {
  position: fixed;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.lookingItem {
  display: flex;
  border-radius: 5px;
  padding: 10px;
  background-color: white;
  box-shadow: var(--el-box-shadow-light);
}

.lookerBox {
  display: inline-block;
  background-color: white;
  width: 535px;
  height: 120px;
  border-radius: 5px;
  margin-right: 92px;
  margin-bottom: 20px;
  padding: 10px;
  opacity: 0.85;
  box-shadow: var(--el-box-shadow-light);
}

.verification-input {
  border: 1px #6cb2f8 solid;
}

.verification-input > .last-child {
  margin-right: 0;
}

.verification-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 5px #007bff;
}

.gomoku-container {
  display: flex;
}
.el-loading-mask >>> {
  z-index: 2000;
}
.infoBox {
  display: inline-block;
  border-radius: 5px;
}

.topShow {
  width: 200px;
  background-color: #2c3e50;
  color: white;
  padding: 10px;
  line-height: 30px;
  border-radius: 5px;
}

.topShow > div {
  text-align: center;
}

.friendMessage {
  font-size: 20px;
}

.myMessage {
  font-size: 20px;
}

.check {
  display: flex;
  text-align: center;
  padding: 10px;
}

.count {
  line-height: 25px;
}

.count > div {
  display: flex;
  justify-content: space-between;
}

.gomoku-board {
  position: relative;
  display: flex;
  flex-direction: column;
  width: 620px;
  margin: auto;
  height: 580px;
  background-color: bisque;
  opacity: 1;
}

.board-row {
  display: flex;
  height: 30px;
  margin: 4px;
}

.board-cell {
  width: 35px;
  height: 35px;
  cursor: pointer;
  background-color: bisque;
  opacity: 0.85;
  position: relative;
  /* 重要：为伪元素定位 */
  margin: 3px;
}

/* 如果需要在中间行切分，为该行添加一个特殊的类 */

.hover-preview {
  position: absolute;
  width: 80%;
  /* 预览图像的大小 */
  height: 80%;
  /* 预览图像的大小 */
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  /* 将预览图像中心对齐到.board-cell中心 */
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
  pointer-events: none;
  opacity: 0.7;
  z-index: 10;
}

.black-piece {
  background-image: url('/assets/pieces/black.png');
}

.white-piece {
  background-image: url('/assets/pieces/white.png');
}

.pieces {
  width: 35px;
}

.star-position {
  position: absolute;
  width: 10px;
  /* 星位的大小 */
  height: 10px;
  background-color: black;
  /* 星位的颜色 */
  border-radius: 50%;
  /* 使其成为圆点 */
  /* 将星位定位到交叉点的中心 */
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  /* 确保星位在线条之上 */
  z-index: 2;
}

.board-cell::after {
  content: '';
  position: absolute;
  top: 50%;
  left: -5px;
  /* 向左延伸10px */
  right: -5px;
  /* 向右延伸10px */
  height: 1px;
  /* 线条宽度 */
  background-color: #333;
  /* 线条颜色 */
}

.board-cell::before,
.board-cell::after {
  /* 其他样式保持不变 */
  z-index: 99;
  /* 将线条置于背景之下 */
}

.board-cell img {
  position: absolute;
  z-index: 100;
  /* 确保棋子在线条之上 */
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.board-cell::before {
  content: '';
  position: absolute;
  left: 50%;
  top: -4px;
  /* 向上延伸10px */
  bottom: -4px;
  /* 向下延伸10px */
  width: 1px;
  /* 线条宽度 */
  background-color: #333;
  /* 线条颜色 */
}

.gomoku-board::before,
.gomoku-board::after {
  content: '';
  position: absolute;
  background-color: #333;
  /* 边界线条的颜色 */
  z-index: 1;
}

/* 棋盘顶部和底部的边界线 */
.gomoku-board::before {
  top: 0;
  right: 0;
  left: 0;
  height: 1px;
  /* 边界线条的厚度 */
}

.gomoku-board::after {
  bottom: 0;
  right: 0;
  left: 0;
  height: 1px;
  /* 边界线条的厚度 */
}

/* 棋盘左侧和右侧的边界线 */
/* 为了不与已有的::before和::after伪元素冲突，我们可以选择添加到.board-row */
.board-row::before,
.board-row::after {
  content: '';
  position: absolute;
  background-color: #333;
  /* 边界线条的颜色 */
  z-index: 1;
}

.board-row::before {
  top: 0;
  bottom: 0;
  left: 0;
  width: 1px;
  /* 边界线条的厚度 */
}

.board-row::after {
  top: 0;
  bottom: 0;
  right: 0;
  width: 1px;
  /* 边界线条的厚度 */
}
</style>
