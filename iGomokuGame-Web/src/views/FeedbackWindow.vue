<script setup>
// 用户反馈
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { sendFeedbackMessage } from '@/api/account.js'
import { useUserIderStore } from '@/stores/userIder'
const userIderStore = useUserIderStore()
const feedbackMessage = ref('') // 反馈信息
const feedbackInp = ref(null) // 反馈输入框ref
const sendFeedback = async () => { // 发送反馈信息
    if (feedbackMessage.value == ''){
        ElMessage.error('反馈消息不能为空哦~')
        return 
    }
    await sendFeedbackMessage({
        userId: userIderStore.userInfo.id,
        feedbackContent: feedbackMessage.value
    })
    // 反馈发送成功
    ElMessage.success('反馈消息发送成功，我们将在2个工作日内解决您的疑难，请耐心等待 ( ﹡ˆoˆ﹡ )')
    feedbackMessage.value = ''
}
onMounted(() => {
    feedbackInp.value.focus()
})
</script>

<template>
    <div style="text-align: center; margin-top: 100px;">
        <div style="display: inline-block;">
            <div style="display: flex;">
                <el-image style="width: 300px; height: 300px" src="/img/adviceFeedback.png" />
                <div style="text-align: left; line-height: 50px;">
                    <div style="font-size: 20px; margin-top: 90px;">
                        为助力本系统的<span class="underline">改进</span>与<span class="underline">完善</span>，
                    </div>
                    <div>
                        欢迎在此留下您宝贵的意见  (◦˙▽˙◦)
                    </div>
                </div>
            </div>
            <div style="text-align: right; padding: 10px;">
                <el-button size="large" type="success" @click="sendFeedback">发送</el-button>
            </div>
            <el-input style="font-size: 20px;" ref="feedbackInp" v-model="feedbackMessage" :autosize="{ minRows: 10, maxRows: 10 }" type="textarea"
                placeholder="写点什么吧...." />
            
        </div>
    </div>
</template>

<style scoped>
.underline {
    font-size: 50px;
    color: #27ad9a;
}
</style>
