<script setup>
import { ref } from 'vue'
import { updateInfo } from '@/api/account.js';
import { ElMessage } from 'element-plus'
import { defineProps } from 'vue'
import { useUserIderStore } from '@/stores/userIder'
const props = defineProps({
  userInfo: Object
})

const userIderStore = useUserIderStore()
const userInfoRev = ref({
  id: props.userInfo.id,
  username: props.userInfo.username,
  nickname: props.userInfo.nickname,
  gender: props.userInfo.gender,
  age: props.userInfo.age,
  description: props.userInfo.description,
  socialAccount: props.userInfo.socialAccount
}) // 绑定表单

const canModify = ref(false)
const toModify = () => {
  canModify.value = true
}
const onCancel = () => {
  canModify.value = false
}
const onModify = async () => { // 修改信息
  if (userInfoRev.value.username == ''){
    ElMessage.error('用户名不能为空')
    return 
  }
  if (userInfoRev.value.nickname == ''){
    ElMessage.error('昵称不能为空')
    return 
  }
  await updateInfo(userInfoRev.value)
  // 修改成功
  userIderStore.setUserInfo(userInfoRev.value)
  ElMessage({
    message: '修改成功',
    type: 'success',
  })
  canModify.value = false
}

</script>

<template>
  <div>
    <div style="text-align: right; margin-bottom: 5px;">
      <el-button size="small" @click="toModify" link>编辑</el-button>
    </div>

    <div>
      <el-form :disabled="!canModify" :model="userInfoRev" label-width="70px">
        <el-form-item label="用户名">
          <el-input v-model="userInfoRev.username" />
        </el-form-item>

        <el-form-item label="昵称">
          <el-input v-model="userInfoRev.nickname" />
        </el-form-item>

        <el-form-item label="性别">
          <el-radio-group v-model="userInfoRev.gender">
            <el-radio label="男" />
            <el-radio label="女" />
          </el-radio-group>
        </el-form-item>

        <el-form-item label="年龄">
          <el-input-number v-model="userInfoRev.age" :min="1" :max="999" />
        </el-form-item>

        <el-form-item label="自我介绍">
          <el-input v-model="userInfoRev.description" type="textarea" :autosize="{ minRows: 2, maxRows: 2 }" />
        </el-form-item>

        <el-form-item label="联系方式">
          <el-input v-model="userInfoRev.socialAccount" type="textarea" :autosize="{ minRows: 2, maxRows: 2 }" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onModify">修改</el-button>
          <el-button type="primary" @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<style scoped></style>
