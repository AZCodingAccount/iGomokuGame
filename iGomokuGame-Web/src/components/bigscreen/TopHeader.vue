<template>
  <div id="top-header">
    <dv-decoration-11 style="width: 120px; height: 50px; cursor: pointer"
      ><div @click="handleGoOut">首页</div></dv-decoration-11
    >
    <dv-decoration-8 class="header-left-decoration" />
    <dv-decoration-5 class="header-center-decoration" />
    <dv-decoration-8 class="header-right-decoration" :reverse="true" />
    <div class="center-title">iGomokuGame数据大屏</div>
    <dv-decoration-11 style="width: 120px; height: 50px">统计报告</dv-decoration-11>
  </div>
</template>

<script setup>
import router from '@/router'

// 退出可视化大屏
const handleGoOut = () => {
  router.push('/dashboard/index')
}
</script>

<style lang="less">
#top-header {
  position: relative;
  width: 100%;
  height: 100px;
  display: flex;
  justify-content: space-between;
  flex-shrink: 0;

  .header-center-decoration {
    width: 40%;
    height: 50px;
    margin-top: 30px;
  }

  .header-left-decoration,
  .header-right-decoration {
    width: 23%;
    height: 50px;
  }

  .center-title {
    position: absolute;
    font-size: 28px;
    font-weight: bold;
    left: 50%;
    top: 10px;
    transform: translateX(-50%);
  }
}
</style>
