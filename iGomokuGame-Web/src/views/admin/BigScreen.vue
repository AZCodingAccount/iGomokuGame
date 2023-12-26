<template>
  <!-- 数据可视化大屏主入口，这里面渲染了一个轮播表，其他的都封装成组件引入的。不要问我为什么这个没封装，因为懒...... -->
  <dv-full-screen-container>
    <dv-loading v-if="showLoading"> 稍等，加载中...... </dv-loading>

    <div class="container" style="width: 100%; height: 100%">
      <topHeader />
      <div class="block-left-right-content">
        <!-- 左边 -->
        <div class="left">
          <!-- 访问的滚动条 -->
          <dv-border-box-11
            title="网站实时访客信息"
            style="height: 360px; padding: 10px; padding-top: 50px"
          >
            <dv-scroll-board
              :config="config"
              class="scroll-container"
              style="width: 500px; height: 300px"
            />
          </dv-border-box-11>
          <!-- 用户年龄饼状图 -->
          <UserAgePie />
        </div>
        <!-- 右边 -->
        <div class="block-top-bottom-content">
          <div class="block-top-content">
            <!-- 地图 -->
            <UserAccessMap />
            <!-- 排行榜 -->
            <ranking-board />
          </div>
          <!-- 用户等级柱状图 -->
          <UserGameLevelBar />
        </div>
      </div></div
  ></dv-full-screen-container>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import rankingBoard from '@/components/bigscreen/RankingBoard.vue' // 导入排行榜
import topHeader from '@/components/bigscreen/TopHeader.vue' // 导入头部
import UserGameLevelBar from '@/components/bigscreen/UserGameLevelBar.vue' // 导入用户等级柱状图
import UserAgePie from '@/components/bigscreen/UserAgePie.vue' // 导入用户年龄饼状图
import UserAccessMap from '@/components/bigscreen/UserAccessMap.vue' // 导入用户分布地图
import { getWebsiteVisitorDetails } from '@/api/admin/analysis.js'
import { formatDateTime } from '@/utils/formatTime.js' // 导入转换时间的工具方法

const config = ref({
  header: null,
  data: null, // 初始化时给config一个具有data属性的结构
  index: false,
  columnWidth: [],
  align: [],
  waitTime: 4000, // 等待时间
  rowNum: 6,
  hoverPause: true
})

const showLoading = ref(true)
// 定义轮询的时间间隔和定时器对象,1分钟20秒轮询一次
const pollingInterval = 4000 * 20
let pollInterval
onMounted(async () => {
  // 获取网站实时访客信息
  fetchData()
  pollInterval = setInterval(fetchData, pollingInterval) // 启动定时器进行轮询
})
const fetchData = async () => {
  const res = await getWebsiteVisitorDetails()
  const data = res.data
    .map(({ id, ...rest }) => rest) // 去除id列
    .map((item) => {
      item.accessTime = formatDateTime(item.accessTime)
      return item
    }) // 格式化时间
    .map(Object.values)

  // 给滚动的图复制
  config.value = {
    header: ['用户名', 'ip', '详细地址', '访问时间'],
    data: data,
    index: false,
    columnWidth: [100, 130, 100, 170],
    align: ['center', 'center', 'center', 'center'],
    waitTime: 4000, // 等待时间
    rowNum: 6,
    hoverPause: true
  }
}
// 组件卸载时清除定时器
onUnmounted(() => {
  clearInterval(pollInterval) // 清除轮询
})
setTimeout(() => {
  showLoading.value = false
}, 1500) // 再等1.5秒，防止没有加载完成
</script>
<style scoped>
.container {
  width: 100%;
  height: 100%;
  background-color: #030409;
  color: #fff;
  background-image: url('/public/img/bg.png');
  background-size: 100% 100%;
  background-repeat: no-repeat;
  background-position: center center;
  box-shadow: 0 0 3px blue;
  display: flex;
  flex-direction: column;
}
/deep/.el-container {
  border-radius: 0;
}

.block-left-right-content {
  flex: 1;
  display: flex;
  flex-direction: row;
  margin-top: 20px;
}

.block-top-bottom-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding-left: 20px;
}

.block-top-content {
  height: 55%;
  display: flex;
  flex-grow: 0;
  box-sizing: border-box;
  padding-bottom: 20px;
}
/* 我自己定义的样式 */
/* .left {
  display: flex;
  flex-direction: column;
  align-items: center;
} */
/* .pie-container {
  margin-top: 200px;
  width: 500px;
  height: 300px;
  background-color: blue;
}
.scroller-container {
  width: 300px;
  height: 100px;
}
.map-container {
  width: 600px;
  height: 400px;
  background-color: blue;
} */
</style>
