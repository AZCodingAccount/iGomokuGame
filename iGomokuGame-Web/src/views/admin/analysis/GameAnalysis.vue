<script setup>
// 一个折线图，AI棋局数和联机棋局数

import * as echarts from 'echarts'
import { getChessboardCountsInfo } from '@/api/admin/analysis.js'

import { onMounted, ref } from 'vue'

const chartContainer = ref(null)
const myChart = ref(null)

onMounted(() => {
  initializeChart()
})

const initializeChart = async () => {
  if (chartContainer.value) {
    myChart.value = echarts.init(chartContainer.value)
    const res = await getChessboardCountsInfo()
    setChartOptions(res.data)
  }
}
const setChartOptions = (data) => {
  // 假设 data 是从后端获取的数组
  // 解构后端返回的数据，获取日期、AI游戏数和人类游戏数
  const dates = data.map((item) => item.recordDate)
  const aiGameCounts = data.map((item) => item.aiGameCounts)
  const humanGameCounts = data.map((item) => item.humanGameCount)

  const option = {
    title: {
      text: 'iGomoku网站棋局信息统计'
    },
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['AI棋局数', '联机棋局数'] // 修改图例名称
    },
    grid: {
      left: '3%',
      right: '4%',
      bottom: '3%',
      containLabel: true
    },
    toolbox: {
      feature: {
        saveAsImage: {}
      }
    },
    xAxis: {
      type: 'category',
      boundaryGap: false,
      data: dates // 使用后端返回的日期作为X轴数据
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: 'AI棋局数', // AI游戏数数据系列
        type: 'line',
        data: aiGameCounts
      },
      {
        name: '联机棋局数', // 人类游戏数数据系列
        type: 'line',
        data: humanGameCounts
      }
    ]
  }
  myChart.value.setOption(option)
}
</script>

<template>
  <div ref="chartContainer" style="width: 1000px; height: 620px; margin: 20px auto"></div>
</template>

<style scoped></style>
