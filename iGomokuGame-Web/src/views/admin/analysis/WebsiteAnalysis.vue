<script setup>
// 网站分析，一个折线图一个折线图+柱状图
import * as echarts from 'echarts'
import { getWebsiteInfo } from '@/api/admin/analysis.js'

import { onMounted, ref } from 'vue'

const chartContainer = ref(null)
const myChart = ref(null)

const newUserChartContainer = ref(null)
const newUserChart = ref(null)

onMounted(() => {
  initializeChart()
})

const initializeChart = async () => {
  const res = await getWebsiteInfo()

  if (chartContainer.value) {
    myChart.value = echarts.init(chartContainer.value)
    setChartOptions(res.data)
  }
  if (newUserChartContainer.value) {
    newUserChart.value = echarts.init(newUserChartContainer.value)
    setNewUserChartOptions(res.data)
  }
}
const setChartOptions = (data) => {
  // 解构后端返回的数据
  const dates = data.map((item) => item.recordDate)
  const newUserCounts = data.map((item) => item.newUserCount)
  const visitorCounts = data.map((item) => item.visitorCount)
  const websiteClicks = data.map((item) => item.websiteClicks)
  const clicksVisitorRatio = data.map((item) =>
    item.visitorCount ? (item.visitorCount / item.websiteClicks).toFixed(2) : 0
  ) // 计算比值，注意避免除以0

  const option = {
    title: {
      text: '网站数据统计'
    },
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        crossStyle: {
          color: '#999'
        }
      }
    },
    toolbox: {
      feature: {
        dataView: { show: true, readOnly: false },
        magicType: { show: true, type: ['line', 'bar'] },
        restore: { show: true },
        saveAsImage: { show: true }
      }
    },
    legend: {
      data: ['新用户数', '访客数', '网站点击数', '点击/访客比值']
    },
    xAxis: [
      {
        type: 'category',
        data: dates,
        axisPointer: {
          type: 'shadow'
        }
      }
    ],
    yAxis: [
      {
        type: 'value',
        name: '用户数/点击数',
        min: 0,
        position: 'left'
      },
      {
        type: 'value',
        name: '比值',
        min: 0,
        position: 'right'
      }
    ],
    series: [
      {
        name: '新用户数',
        type: 'line',
        data: newUserCounts
      },
      {
        name: '访客数',
        type: 'line',
        yAxisIndex: 0,
        data: visitorCounts
      },
      {
        name: '网站点击数',
        type: 'line',
        yAxisIndex: 0,
        data: websiteClicks
      },
      {
        name: '点击/访客比值',
        type: 'bar',
        yAxisIndex: 1,
        data: clicksVisitorRatio
      }
    ]
  }

  myChart.value.setOption(option)
}

const setNewUserChartOptions = (data) => {
  const dates = data.map((item) => item.recordDate)
  const newUserCounts = data.map((item) => item.newUserCount)

  const newUserOption = {
    title: {
      text: '每日新用户数'
    },
    tooltip: {
      trigger: 'axis'
    },
    xAxis: {
      type: 'category',
      data: dates
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        data: newUserCounts,
        type: 'line'
      }
    ]
  }
  newUserChart.value.setOption(newUserOption)
}
</script>

<template>
  <div class="charts">
    <div ref="newUserChartContainer" style="width: 1000px; height: 280px"></div>
    <div ref="chartContainer" style="width: 1000px; height: 400px"></div>
  </div>
</template>

<style scoped>
.charts {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-direction: column;
}
</style>
