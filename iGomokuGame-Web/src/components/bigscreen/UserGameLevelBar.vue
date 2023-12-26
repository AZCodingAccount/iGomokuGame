<script setup>
import { reactive, toRefs, ref, onMounted, nextTick } from 'vue'
import * as echarts from 'echarts'
import { getUserList } from '@/api/admin/management.js' // 获取所有用户数据

const barContainer = ref(null) // 柱状图的ref对象
const barChart = ref(null) // 柱状图的echarts对象
onMounted(async () => {
  let barOption
  barChart.value = barContainer.value.offsetHeight > 0 && echarts.init(barContainer.value) // 判断是不是有，有再渲染
  barChart.value.showLoading()
  const res = await getUserList()
  // 把数据汇总处理一下，一共是6个等级，LV1-LV6，分别是初入江湖，略知一二，小有所成，登峰造极，胜天半子，渡劫飞升。
  // 根据这个userLevel字段聚合统计一下，然后返回一个LV1-LV6的数组回来，格式[1,2,3,4,5,6]
  // 定义一个映射，将用户等级映射到LV1-LV6
  const levelMapping = {
    初入江湖: 'LV1',
    略知一二: 'LV2',
    小有所成: 'LV3',
    登峰造极: 'LV4',
    胜天半子: 'LV5',
    渡劫飞升: 'LV6'
  }

  // 初始化一个对象来存储每个等级的用户数量
  const levelCounts = {
    LV1: 0,
    LV2: 0,
    LV3: 0,
    LV4: 0,
    LV5: 0,
    LV6: 0
  }

  // 遍历用户数组，统计每个等级的用户数量
  res.data.forEach((user) => {
    const level = levelMapping[user.userLevel]
    if (level) {
      levelCounts[level]++
    }
  })

  // 将统计结果转换成数组形式
  const levelDistribution = Object.values(levelCounts)

  // 设置柱状图相关属性
  barOption = {
    darkMode: true,
    color: 'rgb(46, 198, 201)',
    title: {
      text: '网站等级用户数量分布',
      textStyle: {
        color: '#fff' // 设置文字颜色
      }
    },
    textStyle: {
      color: '#fff' // 设置文字颜色
    },
    xAxis: {
      type: 'category',
      data: ['LV1', 'LV2', 'LV3', 'LV4', 'LV5', 'LV6']
    },
    yAxis: {
      type: 'value',
      splitLine: {
        show: false
      }
    },
    series: [
      {
        data: levelDistribution,
        type: 'bar'
      }
    ]
  }
  barChart.value.hideLoading()

  barChart.value.setOption(barOption)
})
</script>
<template>
  <!-- 地图下边的柱状图 -->
  <div ref="barContainer" style="width: 40%; height: 30%; margin-left: 20vw; margin-top: 2vh"></div>
</template>
