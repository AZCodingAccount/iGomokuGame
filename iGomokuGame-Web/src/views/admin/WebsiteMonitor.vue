<script setup>

// 网站性能监控，主要就三个
import { onMounted, ref, onUnmounted } from 'vue'
import { getServerInfo } from '@/api/admin/analysis'

const roomCount = ref(0)
const closedRoomCount = ref(0)
const sessionCount = ref(0)

onMounted(() => {
  const updateServerInfo = async () => {
    const res = await getServerInfo()
    roomCount.value = res.data.roomCount
    closedRoomCount.value = res.data.closedRoomCount
    sessionCount.value = res.data.sessionCount
  }

  // 立即执行一次更新
  updateServerInfo()

  // 设置轮询，每10秒更新一次
  const interval = setInterval(updateServerInfo, 10000)

  // 在组件卸载时清除定时器
  onUnmounted(() => {
    clearInterval(interval)
  })
})
</script>
<template>
  <div id="app">
    <el-card>
      <template #header>
        <div class="card-header" style="text-align: center">
          <span>服务器实时棋局数量</span>
        </div>
      </template>
      <el-row>
        <el-col :span="8">
          <div
            class="room-count"
            style="display: flex; flex-direction: row; align-items: center; justify-content: center"
          >
            <svg
              t="1703325680892"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="5912"
              width="2.5em"
              height="2.5em"
              style="vertical-align: middle; margin-right: 1vw"
            >
              <path
                d="M896 128.128l0.085333 150.314667-233.258666 233.216 120.661333 120.704 60.373333-60.330667 60.330667 60.330667-105.557333 105.6 120.661333 120.704-60.330667 60.330666-120.704-120.704-105.6 105.6-60.330666-60.330666 60.330666-60.373334L512 662.528l-120.661333 120.661333 60.373333 60.373334-60.330667 60.330666-105.6-105.6-120.704 120.704-60.330666-60.330666 120.746666-120.746667-105.642666-105.557333 60.330666-60.330667 60.330667 60.288 120.618667-120.661333-232.96-232.96L128 128l151.296 0.128L512 360.832 744.832 128 896 128.128zM300.8 692.650667l30.208 30.165333 120.618667-120.661333-30.165334-30.165334-120.661333 120.661334zM780.117333 213.376l-207.786666 207.744 30.122666 30.165333 208.256-208.170666v-29.738667h-30.592z m-566.741333 29.866667l479.616 479.616 30.165333-30.165334L243.882667 213.418667l-30.506667-0.042667v29.866667z"
                fill="#1296db"
                p-id="5913"
              ></path>
            </svg>
            <el-statistic title="实时对战房间数" :value="roomCount" />
          </div>
        </el-col>
        <el-col :span="8">
          <div
            class="user-count"
            style="display: flex; flex-direction: row; align-items: center; justify-content: center"
          >
            <svg
              t="1703325946522"
              class="icon"
              viewBox="0 0 1109 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="7798"
              width="2.5em"
              height="2.5em"
              style="vertical-align: middle; margin-right: 1vw"
            >
              <path
                d="M1045.353387 938.666667h-194.133334a431.786667 431.786667 0 0 1 0 85.333333H2.153387a426.752 426.752 0 0 1 253.354666-433.578667A320 320 0 1 1 646.59072 87.466667a298.666667 298.666667 0 0 1 263.552 490.112A426.368 426.368 0 0 1 1109.353387 938.666667h-64z m-32.085334-85.333334A341.674667 341.674667 0 0 0 725.353387 599.978667V593.066667a213.418667 213.418667 0 0 0-14.634667-420.565334A318.634667 318.634667 0 0 1 746.68672 320a319.744 319.744 0 0 1-148.821333 270.421333A427.861333 427.861333 0 0 1 833.812053 853.333333h179.456zM87.99872 938.666667h677.376a341.376 341.376 0 0 0-677.376 0zM426.68672 554.666667a234.666667 234.666667 0 1 0 0-469.333334 234.666667 234.666667 0 0 0 0 469.333334z"
                fill="#1296db"
                p-id="7799"
              ></path>
            </svg>
            <el-statistic title="网站目前登录用户数" :value="sessionCount" />
          </div>
        </el-col>
        <el-col :span="8">
          <div
            class="closed-count"
            style="display: flex; flex-direction: row; align-items: center; justify-content: center"
          >
            <svg
              t="1703326020448"
              class="icon"
              viewBox="0 0 1024 1024"
              version="1.1"
              xmlns="http://www.w3.org/2000/svg"
              p-id="8847"
              width="2.5em"
              height="2.5em"
              style="vertical-align: middle; margin-right: 1vw"
            >
              <path
                d="M509.411556 302.336a24.405333 24.405333 0 0 0-24.405334 24.405333v172.885334l-121.315555 121.315555a24.405333 24.405333 0 1 0 34.503111 34.503111l128.455111-128.455111a24.32 24.32 0 0 0 7.139555-17.237333v-182.983111a24.405333 24.405333 0 0 0-24.376888-24.433778z"
                fill="#1296db"
                p-id="8848"
              ></path>
              <path
                d="M865.621333 632.917333a378.766222 378.766222 0 1 0-231.992889 232.590223 168.334222 168.334222 0 0 0 231.992889-232.590223z m-124.984889 190.577778a22.755556 22.755556 0 0 1-8.049777 5.12 22.983111 22.983111 0 0 1-18.545778 0c-0.711111-0.284444-1.28-0.881778-1.934222-1.223111a27.136 27.136 0 0 1-6.115556-3.896889 25.201778 25.201778 0 0 1-7.082667-17.322667 24.405333 24.405333 0 0 1 1.962667-9.272888 28.188444 28.188444 0 0 1 5.12-8.049778 25.6 25.6 0 0 1 34.645333 0 28.16 28.16 0 0 1 5.12 8.049778 26.908444 26.908444 0 0 1 1.166223 5.688888 22.755556 22.755556 0 0 1 0.768 3.697778 24.149333 24.149333 0 0 1-7.054223 17.180445z m14.563556-179.968l-7.480889 89.457778a22.755556 22.755556 0 0 1-0.910222 3.128889 23.921778 23.921778 0 0 1-19.285333 18.517333 23.182222 23.182222 0 0 1-6.257778 0.739556 24.519111 24.519111 0 0 1-22.385778-22.385778l-7.452444-89.457778a31.971556 31.971556 0 1 1 63.715555-5.319111 35.328 35.328 0 0 1 0.056889 5.290667z m-31.857778-89.031111a168.391111 168.391111 0 0 0-132.124444 273.066667 330.126222 330.126222 0 1 1 236.088889-236.828445 167.822222 167.822222 0 0 0-103.964445-36.266666z"
                fill="#1296db"
                p-id="8849"
              ></path>
            </svg>
            <el-statistic title="因超时被关闭的房间数" :value="closedRoomCount"> </el-statistic>
          </div>
        </el-col>
      </el-row>
    </el-card>
    <div class="tips">完善中，敬请期待 🌟🌟🌟</div>
  </div>
</template>
<style scoped>
#app {
  display: flex;
  flex-direction: column;
}
.tips {
  padding-top: 30%;
  font-size: 25px;
  text-align: center;
}
.el-col {
  text-align: center;
}
</style>
