<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import * as echarts from 'echarts'
import axios from 'axios'
import zhongguo from '@/assets/map.json'
import { getRegionData } from '@/api/admin/analysis.js'

const chartsContainer = ref(null)
const myChart = ref(null)
// 获取数据并设置图表选项
const fetchDataAndUpdateChart = async () => {
  if (myChart.value) {
    try {
      const res = await getRegionData()
      let res2 = res.data.map((item) => {
        return {
          name: item.areaName,
          value: Number(item.visitorCount)
        }
      })
      let option
      // 注册地图，China为地图名，chinaMap为地图的json数据
      echarts.registerMap('China', zhongguo)
      option = {
        darkMode: true,
        title: {
          text: '全国访问量分布图',
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
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.7)', // 暗色背景
          extraCssText: 'box-shadow: 0 0 10px rgba(0, 0, 0, 0.5);',
          textStyle: {
            color: '#fff'
          },
          formatter: function (params) {
            return `${params.name}<br/>销量：${params.value}`
          }
        },
        visualMap: {
          left: 'left',
          top: 'bottom',
          min: 0,
          max: 100,
          text: ['高', '低'],
          realtime: false,
          calculable: true,
          inRange: {
            color: ['#00baff', '#0057ff'] // 从浅蓝到深蓝的渐变色
          },

          textStyle: {
            color: '#fff',
            textAlign: 'left'
          }
        },

        series: [
          {
            name: '访问量',
            type: 'map',
            map: 'China',
            backgroundColor: 'rgb(16, 26, 42)',
            layoutCenter: ['50%', '70%'], // 地图中心位置
            layoutSize: '100%', // 地图大小，可以是百分比或像素值
            roam: true,
            zoom: 1.4,
            itemStyle: {
              borderColor: '#aaa',
              areaColor: 'rgb(6, 37, 83)'
            },
            emphasis: {
              itemStyle: {
                areaColor: '#fe994e',
                borderWidth: 0
              },
              label: {
                show: true,
                color: 'rgb(20, 86, 150)'
              }
            },
            label: {
              show: true, // 这里是 true 以显示省份名称
              color: '#fff', //
              fontSize: 11 // 字号大小
            },
            data: res2
          }
        ]
      }
      myChart.value.hideLoading()
      myChart.value.setOption(option)
    } catch (error) {
      console.error('数据获取失败:', error)
      myChart.value.hideLoading()
    }
  }
}
let polling
onMounted(() => {
  // 初始化图表
  const initChart = () => {
    if (chartsContainer.value.offsetHeight > 0) {
      myChart.value = echarts.init(chartsContainer.value)
      myChart.value.showLoading()
    }
  }
  // 初始化图表
  initChart()
  // 设置轮询间隔，每30秒轮询一次
  const interval = 1000 * 30
  polling = setInterval(fetchDataAndUpdateChart, interval)
  // 首次执行数据获取和更新图表
  fetchDataAndUpdateChart()
})
// 组件销毁时清除轮询
onUnmounted(() => {
  clearInterval(polling)
})
</script>
<template>
  <!-- 地图 -->
  <div ref="chartsContainer" class="map-container" style="width: 600px; height: 50vh"></div>
</template>
<style scoped></style>
