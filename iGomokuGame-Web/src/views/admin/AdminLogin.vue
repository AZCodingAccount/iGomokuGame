<script setup>
// 该view是进行登录操作的页面
import { ref, computed } from 'vue'
import { User, Lock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { loginService } from '@/api/admin/admin'
import { useRouter } from 'vue-router'
import { useAdminStore } from '@/stores/adminInfo'

const adminStore = useAdminStore()
const rememberMe = ref(true)
const router = useRouter()
const formModel = ref({
  username: '',
  password: ''
})
// 每次进来先看看本地存储里面有没有值，有值就渲染
if (adminStore.getAdminInfo().username != '' && adminStore.getAdminInfo().password != '') {
  formModel.value = adminStore.getAdminInfo()
}

const handleLogin = async () => {
  const res = await loginService(formModel.value)
  ElMessage.success('登录成功')

  adminStore.setToken(res.data)

  // 勾选了记住我就把数据存到pinia中
  if (rememberMe.value == true) {
    // console.log('存储到pinia仓库中')
    adminStore.adminInfo = { ...adminStore.adminInfo, ...formModel.value }
  } else {
    adminStore.rememberMe.value = false
  }
  router.replace('/dashboard') // 跳转到首页
}

const isMobile = computed(() => window.innerWidth <= 768)
</script>

<template>
  <div class="app">
    <h1
      style="text-align: center; padding-top: 5vh; color: white; padding-left: 6vw; font-size: 35px"
    >
      iGomokuGame后台管理系统
    </h1>

    <!-- Desktop UI -->
    <div v-if="!isMobile" class="form">
      <el-form :model="formModel" label-width="120px" style="margin: 0 auto">
        <el-form-item label="用户名" style="color>>>: white">
          <el-input
            v-model="formModel.username"
            placeholder="请输入用户名"
            :prefix-icon="User"
            style="width: 400px"
            size="large"
            auto-complete
          />
        </el-form-item>
        <el-form-item label="密码">
          <el-input
            v-model="formModel.password"
            placeholder="请输入密码"
            show-password
            :prefix-icon="Lock"
            style="width: 400px"
            size="large"
            auto-complete
          />
        </el-form-item>
        <el-form-item class="flex">
          <div class="flex">
            <el-checkbox v-model="rememberMe" style="color: white">记住我</el-checkbox>
          </div>
        </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            @click="handleLogin"
            size="large"
            auto-insert-space
            style="width: 100%"
            >登录</el-button
          >
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped>
.app {
  width: 100vw; /* 视口宽度 */
  height: 100vh; /* 视口高度 */
  background-image: url('/img/admin-login-bg.png');
  background-size: cover;
  background-repeat: no-repeat;
  background-position: center;
}

.flex {
  width: 100%;
  display: flex;
  justify-content: flex-start;
}
.form {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 60vh;
}

::v-deep .el-form-item__label {
  color: white;
}

.align-items {
  margin: 15px 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.forget-link {
  margin-left: 8px;
}
</style>
