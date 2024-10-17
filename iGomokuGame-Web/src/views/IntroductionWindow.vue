<script setup>
// 介绍页面，网站首页
import { ref, onBeforeMount } from 'vue'
import { login, register } from '@/api/account.js'
import { useUserIderStore } from '@/stores/userIder'
import { useShowingFrienderStore } from '@/stores/showingFriender'
import router from '@/router'
import { getUnreadMessageObj } from '@/api/friend'
import { ElMessage } from 'element-plus'
import { addWebsiteClick } from '@/api/admin/analysis.js'
onBeforeMount(() => {
  // 进来首先发个请求统计网站流量
  addWebsiteClick()
})

const loginDialogVisible = ref(false)
const userIderStore = useUserIderStore()
const showingFrienderStore = useShowingFrienderStore()
const status = ref(true) // true 为登录，false 为注册
const usernameInp = ref(null)
const passwordInp = ref(null)
const form = ref({
  username: '',
  password: ''
})
const openDialog = () => {
  loginDialogVisible.value = true
  form.value.username = 'demo'
  form.value.password = 'demo'
}
openDialog
const onSubmit = async () => {
  if (form.value.username == '') {
    ElMessage.error('用户名不能为空')
    return
  }
  if (form.value.password == '') {
    ElMessage.error('密码不能为空')
    return
  }
  // console.log(form.value)

  if (!status.value) {
    // 请求注册
    await register(form.value)
    // 注册成功
  }
  // 请求登录
  var res = await login(form.value)
  // 登录成功
  ElMessage.success('登录成功')
  loginDialogVisible.value = false // 关闭弹窗
  // 存储用户信息
  userIderStore.setLoginForm(form.value)
  userIderStore.setUserInfo(res.data)
  // 创建聊天的连接
  userIderStore.getChatSocket()
  res = await getUnreadMessageObj()
  showingFrienderStore.setUnreadMessageObj(res.data)
  console.log('unreadMessageObj', showingFrienderStore.unreadMessageObj)
  router.push('/main')
}

const pressUsername = (event) => {
  if (event.key == 'ArrowDown' || event.key == 'Enter') passwordInp.value.focus()
}
const pressPassword = (event) => {
  if (event.key == 'ArrowUp') usernameInp.value.focus()
  else if (event.key == 'Enter') onSubmit()
}
</script>

<template>
  <!-- 菜单 -->
  <el-menu
    style="width: 100%"
    class="menu"
    mode="horizontal"
    :unique-opened="true"
    :ellipsis="false"
  >
    <div style="flex-grow: 1"></div>

    <!-- 主界面 -->
    <el-menu-item disabled>
      <span style="font-size: 20px">
        <el-icon size="25px">
          <TrophyBase />
        </el-icon>
      </span>
    </el-menu-item>

    <!-- 我的好友 -->
    <el-menu-item disabled>
      <span style="font-size: 20px">
        <el-icon size="25px">
          <ChatDotRound />
        </el-icon>
      </span>
    </el-menu-item>

    <!-- 反馈界面按钮 -->
    <el-menu-item disabled>
      <span style="font-size: 20px">
        <el-icon size="25px">
          <EditPen />
        </el-icon>
      </span>
    </el-menu-item>

    <!-- 登录/个人信息按钮 -->
    <el-menu-item>
      <!-- 登录按钮 -->
      <span>
        <el-button style="margin-bottom: 15px" size="large" link @click="openDialog">
          登录
        </el-button>
        <!-- 登录对话框 -->
        <el-dialog v-model="loginDialogVisible" width="30%">
          <div style="max-width: 600px">
            <h1 style="text-align: center;padding-bottom: 10px;">{{ status ? '登录' : '注册' }}</h1>
            <el-form :model="form" label-width="50px">
              <el-form-item label="用户名">
                <el-input
                  ref="usernameInp"
                  @keydown="pressUsername"
                  v-model="form.username"
                  :placeholder="status ? '请输入用户名' : '设置新用户名'"
                />
              </el-form-item>

              <el-form-item label="密码">
                <el-input
                  ref="passwordInp"
                  @keydown="pressPassword"
                  v-model="form.password"
                  type="password"
                  :placeholder="status ? '请输入密码' : '设置新密码'"
                  show-password
                />
              </el-form-item>
              <div>
                <el-button
                  style="float: right"
                  size="mid"
                  type="primary"
                  @click="status = !status"
                >
                  {{ status ? '注册' : '登录' }}
                </el-button>
              </div>

              <el-form-item>
                <el-button type="primary" @click="onSubmit">{{
                  status ? '登录' : '注册'
                }}</el-button>
                <el-button @click="loginDialogVisible = false">取消</el-button>
              </el-form-item>
            </el-form>
          </div>
        </el-dialog>
      </span>
    </el-menu-item>
  </el-menu>

  <div class="introduction">
    <div style="font-size: 80px; text-align: center; padding-top: 20%">
      <p>棋圣路漫漫</p>
    </div>
  </div>
</template>
<style scoped>
.introduction {
  width: 100vw;
  height: 100vh;
  background-image: url(/img/user-login-bg.png);
  background-size: 100% 100%;
}

.menu {
  position: absolute;
  width: 100%;
  top: 0;
}

.name {
  font-size: 30px;
  text-align: right;
  margin-right: 50px;
  margin-top: 150px;
}
</style>
