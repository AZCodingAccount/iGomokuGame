<script setup>
// 反馈管理。
import { getFeedbackList, fixFeedback } from '@/api/admin/management.js'
import { onMounted, ref } from 'vue'
import { formatDateTime, reverseFormatDateTime } from '@/utils/formatTime'
import { ElMessage } from 'element-plus'
import * as XLSX from 'xlsx'

const feedbackList = ref([])
const originalFeedbackList = ref([]) // 用于存储原始数据
const dialogVisible = ref(false) // 确认解决的对话框显否变量
const showDialogVisible = ref(false) // 显示解决对话框
const feedbackContent = ref('') // 存储当前的feedback内容
const exportTableRef = ref(null) // 存储要导出的表格对象

const getList = async () => {
  // 获取数据
  const res = await getFeedbackList()
  feedbackList.value = feedbackList.value = res.data.map((item) => {
    item.fixed = item.fixed == true ? '已解决' : '未解决'
    item.feedbackTime = formatDateTime(item.feedbackTime)
    item.fixedTime = item.fixedTime ? formatDateTime(item.fixedTime) : '-'
    return item
  })
  originalFeedbackList.value = [...feedbackList.value] // 初始时复制原始数据，用于选择的时候渲染
  handleChange()
}
const feedbackObj = ref({})

onMounted(getList)

// 初始化下拉框
const value = ref('noFixed')

const options = [
  {
    value: 'all',
    label: '查看所有'
  },
  {
    value: 'isFixed',
    label: '已解决'
  },
  {
    value: 'noFixed',
    label: '未解决'
  }
]
// 给不同单元格的定义不同的样式
const tableRowClassName = ({ row, rowIndex }) => {
  if (row.fixed === '未解决') {
    return 'warning-row'
  } else if (row.fixed === '已解决') {
    return 'success-row'
  }
  return ''
}

const handleFeedbackCheck = (feedbackObj) => {
  feedbackContent.value = feedbackObj.feedbackContent
  showDialogVisible.value = true
}
// 处理修复用户反馈事件
const handleFixed = (feedback) => {
  feedbackObj.value = feedback
  dialogVisible.value = true
}
const handleFinalFixed = async () => {
  // 有点脱裤子放屁了，其实就发送一个put请求不传参数也可以，后续也不用扩展。
  feedbackObj.value = Object.keys(feedbackObj.value).reduce((newObj, key) => {
    // 复制当前的属性到新对象
    newObj[key] = feedbackObj.value[key]
    // 检查并修改特定的属性
    if (key === 'fixed') {
      newObj[key] = true
    } else if (key === 'feedbackTime') {
      newObj[key] = feedbackObj.value[key] ? reverseFormatDateTime(feedbackObj.value[key]) : '-'
    } else if (key === 'fixedTime' && feedbackObj.value[key] === '-') {
      newObj[key] = null
    }
    return newObj
  }, {})
  // 向后端发送请求
  await fixFeedback(feedbackObj.value)
  // 重新渲染页面
  getList()
  // console.log('此时下拉框的值为：', value.value)
  handleChange()
  dialogVisible.value = false // 关闭弹框
  ElMessage.success('更新成功')
}
// 处理下拉框变化事件
const handleChange = () => {
  if (value.value === 'isFixed') {
    feedbackList.value = originalFeedbackList.value.filter((item) => item.fixed === '已解决')
  } else if (value.value === 'noFixed') {
    // console.log('noFixed')
    feedbackList.value = originalFeedbackList.value.filter((item) => item.fixed === '未解决')
  } else {
    // console.log('全量复制')
    feedbackList.value = originalFeedbackList.value
  }
}

// 处理数据导出的事件
const handleExport = () => {
  // 导出数据
  const tableDom = exportTableRef.value?.$el
  if (!tableDom) {
    return
  }
  const wb = XLSX.utils.table_to_book(tableDom)
  XLSX.writeFile(wb, '表格数据.xlsx')
}
</script>
<template>
  <div id="app">
    <!-- 操作部分 -->
    <div class="header" style="margin: 20px 0px; display: flex; justify-content: space-between">
      <el-select v-model="value" class="m-2" placeholder="请选择反馈状态" @change="handleChange">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value"
        />
      </el-select>
      <el-button type="primary" style="text-align: right" @click="handleExport">
        导出数据<el-icon class="el-icon--right"><Upload /></el-icon>
      </el-button>
    </div>
    <!-- 表格部分 -->
    <el-scrollbar height="600px">
      <el-table
        ref="exportTableRef"
        :data="feedbackList"
        style="width: 100%"
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="id" label="反馈编号" align="center" />
        <el-table-column prop="userId" label="反馈用户id" align="center" />
        <el-table-column prop="feedbackContent" label="反馈内容" align="center" max-width="600px">
          <template #default="scope">
            <div class="text-ellipsis">
              {{ scope.row.feedbackContent }}
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="feedbackTime" label="反馈时间" align="center" />
        <el-table-column prop="fixed" label="是否解决" align="center" />
        <el-table-column prop="fixedTime" label="解决时间" align="center" />
        <el-table-column label="操作" width="200px" align="center">
          <template #default="scope">
            <el-button size="small" @click="handleFeedbackCheck(scope.row)">查看</el-button>
            <el-button
              size="small"
              type="danger"
              :disabled="scope.row.fixed === '已解决'"
              @click="handleFixed(scope.row)"
              >已解决</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <!-- 查看对话框 -->
      <el-dialog
        v-model="showDialogVisible"
        title="用户反馈内容"
        width="40%"
        draggable
        close-on-press-escape
      >
        <span><span class="tip">&nbsp;&nbsp;&nbsp;&nbsp;</span> {{ feedbackContent }}</span>
      </el-dialog>
      <!-- 确认解决对话框 -->
      <el-dialog
        v-model="dialogVisible"
        title="确认反馈解决"
        width="30%"
      >
        <span>确认标记为已解决吗？</span>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="handleFinalFixed"> 确认 </el-button>
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
