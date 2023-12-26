<script setup>
// 用户管理
import { getUserList, disableUser } from '@/api/admin/management.js'
import { onMounted, ref } from 'vue'
import { ElMessage } from 'element-plus'
import * as XLSX from 'xlsx'

const userList = ref([])
const user = ref({})
const dialogVisible = ref(false) // 确认解决的对话框显否变量
const showDialogVisible = ref(false) // 显示解决对话框
const feedbackContent = ref('') // 存储当前的feedback内容
const exportTableRef = ref(null) // 存储要导出的表格对象
const username = ref('') // 要搜索的用户名
const nickname = ref('') // 要搜索的昵称

const getList = async () => {
  // 获取数据
  const res = await getUserList()
  userList.value = userList.value = res.data.map((item) => {
    item.deleted = item.deleted == 1 ? '已注销' : '正常'
    return item
  })
}
// 挂载完毕渲染
onMounted(getList)

// 初始化下拉框
const value = ref('all')

const options = [
  {
    value: 'all',
    label: '查看所有'
  },
  {
    value: 1,
    label: '已注销'
  },
  {
    value: 0,
    label: '正常'
  }
]
// 给不同单元格的定义不同的样式
const tableRowClassName = ({ row, rowIndex }) => {
  if (row.deleted === '正常') {
    return 'warning-row'
  } else if (row.deleted === '已注销') {
    return 'success-row'
  }
  return ''
}

// 处理查询的时间
const handleSelect = async () => {
  let params = {}
  if (value.value === 'all') {
    // 拼装数据
    params = {
      username: username.value,
      nickname: nickname.value
    }
  } else {
    // 拼装数据
    params = {
      username: username.value,
      nickname: nickname.value,
      deleted: value.value
    }
  }
  const res = await getUserList(params)
  userList.value = res.data.map((item) => {
    item.deleted = item.deleted == 1 ? '已注销' : '正常'
    return item
  })
}

// 处理禁用事件
const handleDisable = (selectedUser) => {
  user.value = selectedUser
  // console.log(user.value.id)

  // 让确认框显示
  dialogVisible.value = true
}
const handleFinalDisable = async () => {
  // console.log(user.value.id)
  // console.log(typeof user.value.id)
  // 调用禁用接口
  await disableUser(user.value.id)
  // 重新刷新页面
  getList()
  // 关闭确认框
  dialogVisible.value = false
  ElMessage.success('禁用成功')
}

// 处理数据导出的事件
const handleExport = () => {
  // 导出数据
  const tableDom = exportTableRef.value?.$el
  if (!tableDom) {
    return
  }
  const wb = XLSX.utils.table_to_book(tableDom)
  XLSX.writeFile(wb, `${value.value}用户数据.xlsx`)
}
const handleAllDataExport = () => {
  const data = XLSX.utils.json_to_sheet(userList.value)
  const wb = XLSX.utils.book_new()
  XLSX.utils.book_append_sheet(wb, data, '用户数据')
  XLSX.writeFile(wb, `${value.value}用户数据.xlsx`)
}
</script>
<template>
  <div id="app">
    <!-- 操作部分 -->
    <div class="header" style="margin: 20px 0px; display: flex; justify-content: space-between">
      <el-select v-model="value" class="m-2" placeholder="请选择账号状态">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-input v-model="username" placeholder="输入用户名查询" clearable style="width: 200px" />
      <el-input v-model="nickname" placeholder="输入昵称查询" clearable style="width: 200px" />
      <el-button type="primary" style="text-align: right" @click="handleSelect">
        查询用户<el-icon class="el-icon--right"><Search /></el-icon>
      </el-button>
      <el-button type="primary" style="text-align: right" @click="handleExport">
        导出数据<el-icon class="el-icon--right"><Upload /></el-icon>
      </el-button>
      <el-button type="primary" style="text-align: right" @click="handleAllDataExport">
        导出所有用户数据<el-icon class="el-icon--right"><Upload /></el-icon>
      </el-button>
    </div>
    <!-- 表格部分 -->
    <el-scrollbar height="600px">
      <el-table
        ref="exportTableRef"
        :data="userList"
        style="width: 100%"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="id" label="用户id" align="center" />
        <el-table-column label="图片" width="150px">
          <template #default="scope">
            <el-image :src="scope.row.imageUrl" style="width: 50px"></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="username" label="用户名" align="center" />
        <el-table-column prop="password" label="用户密码" align="center" max-width="600px">
        </el-table-column>
        <el-table-column prop="nickname" label="用户昵称" align="center" />
        <el-table-column prop="gender" label="性别" align="center" />
        <el-table-column prop="age" label="年龄" align="center" />
        <el-table-column prop="deleted" label="账号状态" align="center" />
        <el-table-column label="操作" width="200px" align="center">
          <template #default="scope">
            <el-button
              size="small"
              type="danger"
              :disabled="scope.row.deleted === '已注销'"
              @click="handleDisable(scope.row)"
              >禁用</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 确认解决对话框 -->
      <el-dialog v-model="dialogVisible" title="禁用账号确认框" width="30%">
        <span>确认禁用该用户账号吗</span>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleFinalDisable"> 确认 </el-button>
          </span>
        </template>
      </el-dialog>
    </el-scrollbar>
  </div>
</template>
<style scoped>
.el-table >>> .warning-row {
  --el-table-tr-bg-color: var(--el-color-warning-light-9);
}
/deep/ .el-table .success-row {
  --el-table-tr-bg-color: var(--el-color-success-light-9);
}

.text-ellipsis >>> {
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  max-width: 300px;
}
.tip {
  color: red;
}
</style>
