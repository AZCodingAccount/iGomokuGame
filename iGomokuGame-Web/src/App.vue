<script setup>
// 项目入口
</script>
<template>
  <div id="app">
    <router-view :key="$route.fullPath"></router-view>
  </div>
</template>
<style scoped>
#app {
  width: 100%;
  height: 100%;
  background-color: #fff;
}
</style>
