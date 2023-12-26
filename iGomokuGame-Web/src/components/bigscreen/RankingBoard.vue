<template>
  <dv-border-box-12 style="width: 30%; margin-right: 10px">
    <div id="ranking-board">
      <div class="ranking-board-title">网站访问量(省)</div>
      <dv-scroll-ranking-board :config="config" />
    </div>
  </dv-border-box-12>
</template>

<script setup>
import { getRegionData } from '@/api/admin/analysis.js'
import { ref, onMounted, onUnmounted } from 'vue'
const config = ref({
  data: [],
  rowNum: 6
})
// 定义一个轮询函数
const pollData = async () => {
  try {
    const res = await getRegionData()
    let res2 = res.data
      .slice(0, 6) // 只取数组的前6个元素
      .map((item) => {
        return {
          name: item.areaName,
          value: Number(item.visitorCount) // 如果不是数字则默认为0
        }
      })

    config.value = {
      data: res2,
      rowNum: 6
    }
  } catch (error) {
    console.error('数据获取失败:', error)
  }
}
let polling
onMounted(() => {
  // 首次执行
  pollData()

  // 设置轮询间隔，每30秒轮询一次
  const interval = 1000 * 30
  polling = setInterval(pollData, interval)
})
// 在组件销毁时清除轮询
onUnmounted(() => {
  clearInterval(polling)
})
</script>

<style lang="less">
#ranking-board {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-sizing: border-box;
  padding: 0px 30px;

  .ranking-board-title {
    font-weight: bold;
    height: 50px;
    display: flex;
    align-items: center;
    font-size: 20px;
  }

  .dv-scroll-ranking-board {
    flex: 1;
  }
}
</style>
