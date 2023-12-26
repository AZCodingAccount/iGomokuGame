<script setup>
// 后台主要布局文件
import { ref, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAdminStore } from '@/stores/adminInfo'

// 使用路由实例
const route = useRoute()
const router = useRouter()
// 默认激活的菜单项
const defaultActive = ref(route.path)

const dialogVisible = ref(false) // 确认显示隐藏的变量
// 获取管理员仓库
const adminStore = useAdminStore()
const adminName = adminStore.getAdminInfo().nickname
const adminAvatar = '/img/admin-avatar.jpg'

// 更新当前时间
const currentTime = ref(new Date().toLocaleTimeString())
const intervalId = setInterval(() => {
  currentTime.value = new Date().toLocaleTimeString()
}, 1000)

// 组件卸载时清除定时器
onUnmounted(() => {
  clearInterval(intervalId)
})

// 处理菜单项选择的方法
const handleSelect = (key, keyPath) => {
  console.log(key, keyPath)
}

const handleExit = () => {
  dialogVisible.value = true
}
const handleFinalExit = () => {
  dialogVisible.value = false
  // 退出登录
  adminStore.logout()
  // 跳转路由到登录界面
  router.replace('/admin')
}
</script>

<template>
  <div class="app1">
    <el-container>
      <el-aside class="aside" width="60">
        <el-menu
          :default-active="defaultActive"
          class="el-menu-vertical-demo"
          @select="handleSelect"
          :unique-opened="true"
          :router="true"
        >
          <el-menu-item index="/dashboard/index">
            <el-icon><House /></el-icon>
            <span>首页</span>
          </el-menu-item>
          <el-menu-item index="/dashboard/bigscreen">
            <el-icon><FullScreen /></el-icon> 可视化大屏</el-menu-item
          >
          <el-sub-menu index="1">
            <template #title>
              <el-icon><Management /></el-icon> <span>网站管理</span>
            </template>
            <el-menu-item index="/dashboard/management/feedback">
              <el-icon><location /></el-icon> 反馈管理</el-menu-item
            >
            <el-menu-item index="/dashboard/management/user">
              <el-icon><User /></el-icon> 用户管理</el-menu-item
            >
          </el-sub-menu>
          <el-sub-menu index="2">
            <template #title>
              <el-icon><DataAnalysis /></el-icon> <span>网站分析</span>
            </template>
            <el-menu-item index="/dashboard/analysis/game">
              <el-icon><location /></el-icon> 五子棋分析</el-menu-item
            >
            <el-menu-item index="/dashboard/analysis/website">
              <el-icon><StarFilled /></el-icon> 流量分析</el-menu-item
            >
          </el-sub-menu>

          <el-menu-item index="/dashboard/website/monitor">
            <el-icon><Service /></el-icon> <span>服务器性能监控</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <div>
            当前时间：<span class="current-time">{{ currentTime }}</span>
          </div>
          <div class="right">
            <div class="my">
              你好：
              <span class="name" style="font-weight: 600;">{{ adminName }}</span>
            </div>
            <el-avatar :size="40" :src="adminAvatar" />
            <el-button style="margin-left: 20px" type="danger" @click="handleExit">退出</el-button>
          </div>
          <!-- 确认解决对话框 -->
          <el-dialog v-model="dialogVisible" title="确认退出对话框" width="30%">
            <span>确认退出吗？</span>
            <template #footer>
              <span class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleFinalExit"> 确认 </el-button>
              </span>
            </template>
          </el-dialog>
        </el-header>
        <el-divider />

        <el-main> <router-view></router-view> </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<style scoped>
html,
body,
.app1 {
  height: 100%;
  margin: 0;
  padding: 0;
}

.container {
  display: flex;
  width: 100vw;
  height: 100vh;
}

/deep/ .el-aside {
  height: 100vh;
  border-right: 2px solid #ccc; /* 设置线的颜色和宽度 */
}

.el-col,
.el-main {
  height: 100%;
}

/deep/ .el-menu-item-group__title {
  height: 0;
}
.header >>> {
  display: flex;
  justify-content: space-between;
  align-items: center;
  /* background-color: #f5f5f5; 添加浅灰色背景 */
  color: #333; /* 深灰色文字 */
  height: 50px;
  padding: 0 20px; /* 增加一些内边距 */
  font-size: 16px; /* 设置字体大小 */
}
.el-divider--horizontal >>> {
  margin: 0;
}

.current-time {
  color: #1a73e8; /* 设置当前时间的颜色为蓝色 */
}
.right {
  display: flex;
  align-items: center;
  justify-content: flex-end;
}
.my {
  margin-right: 20px;
}
</style>
