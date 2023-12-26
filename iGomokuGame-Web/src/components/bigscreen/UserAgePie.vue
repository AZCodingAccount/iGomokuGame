<script setup>
import { ref, onMounted } from 'vue'
import * as echarts from 'echarts'
import { getUserList } from '@/api/admin/management.js' // 获取所有用户数据

const pieContainer = ref(null) // 饼图的ref对象
const pieChart = ref(null) // 饼图的echarts对象

onMounted(async () => {
  pieChart.value = pieContainer.value.offsetHeight > 0 && echarts.init(pieContainer.value) // 判断是不是有，有再渲染

  pieChart.value.showLoading()
  const res = await getUserList()
  // console.log(res.data)
  // 把数据汇总处理一下，分成几个年龄段，18以下，18-25,25-35,35-45,45以上5个年龄段，然后以name value 的数组形式返回
  const ageGroups = res.data.reduce((acc, user) => {
    if (user.age < 18) {
      acc['18以下'] = (acc['18以下'] || 0) + 1
    } else if (user.age >= 18 && user.age <= 25) {
      acc['18-25'] = (acc['18-25'] || 0) + 1
    } else if (user.age > 25 && user.age <= 35) {
      acc['25-35'] = (acc['25-35'] || 0) + 1
    } else if (user.age > 35 && user.age <= 45) {
      acc['35-45'] = (acc['35-45'] || 0) + 1
    } else {
      acc['45以上'] = (acc['45以上'] || 0) + 1
    }
    return acc
  }, {})

  const ageDistribution = Object.entries(ageGroups).map(([name, value]) => ({ name, value })) // 把数据处理成echarts需要的格式

  pieChart.value.hideLoading()

  let pieOption
  // 设置饼状图相关属性
  pieOption = {
    darkMode: true, // 暗黑模式
    title: {
      text: '网站年龄分布',
      left: 'center',
      textStyle: {
        color: '#fff',
        fontWeight: 'bold',
        fontFamily: 'Arial',
        fontSize: 20
      }
    },
    textStyle: {
      color: '#fff'
    },
    tooltip: {
      formatter: '{a} <br/>{b} : {c} ({d}%)',
      trigger: 'item'
    },
    legend: {
      orient: 'vertical',
      left: '70%',
      y: 'center',
      textStyle: {
        color: '#fff',
        fontFamily: 'Arial',
        fontSize: 12
      }
    },
    label: {
      show: true,
      // 格式化标签文本以显示值和百分比
      formatter: function (params) {
        // params.value 是数据项的值，params.percent 是百分比
        return `${params.name}(${Math.round(params.percent)}%)`
      }
    },
    series: [
      {
        name: '年龄段用户数量',
        type: 'pie',
        radius: '50%',
        center: ['35%', '50%'],
        data: ageDistribution,
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: 'rgba(0, 0, 0, 0.5)'
          }
        }
      }
    ]
  }
  pieChart.value.setOption(pieOption)
})
</script>
<template>
  <!-- 饼图 -->
  <div
    ref="pieContainer"
    class="pie-container"
    style="width: 500px; height: 250px; margin-top: 8vh"
  ></div>
</template>
<style scoped></style>
