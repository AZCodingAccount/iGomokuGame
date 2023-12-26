<script setup>
import { ref, defineProps, defineEmits } from 'vue'
import ShowTab from '@/components/ShowTab.vue'
import ModifyInfoForm from '@/components/ModifyInfoForm.vue'
import { logout, uploadAvatar } from '@/api/account.js'
import { useUserIderStore } from '@/stores/userIder'
import { ElMessage } from 'element-plus'
import router from '@/router'
const props = defineProps({
    isMine: Boolean,
    userInfo: Object
})
const emit = defineEmits(['initMainWindow'])
const userIderStore = useUserIderStore()
const showingAvatar = ref(props.userInfo.imageUrl)
const beforeImageUpload = async (rawFile) => { // 上传头像
    const formData = new FormData()
    formData.append('file', rawFile)
    if (rawFile.size / 1024 / 1024 > 10) { // 检验文件是否过大
        ElMessage.error('文件过大')
        return;
    }
    const res = await uploadAvatar(formData) // 头像上传成功
    showingAvatar.value = res.data
    // 将新头像上传到pinia
    userIderStore.setMyImageUrl(res.data)
    // 刷新主窗口的个人信息，将新上传的头像显示出来
    emit('initMainWindow')
}

const logoutDialogVisible = ref(false)
const onLogout = async () => { // 注销账号
    await logout(userIderStore.userInfo.id)
    // 注销成功
    logoutDialogVisible.value = false
    localStorage.removeItem(userIderStore.$id) // 移除本地存储的信息
    if (JSON.stringify(useUserIderStore.chatSocket) != "{}") { // 关闭聊天用的socket
        useUserIderStore.chatSocket.close()
    }
    router.push('/introduction')
}

const outDialogVisible = ref(false)
const onOut = () => { // 退出账号
    outDialogVisible.value = false
    userIderStore.closeChatSochet()
    // 移除本地存储的信息
    localStorage.removeItem(userIderStore.$id)
    router.push('/introduction')
}

const avatarDialogVisible = ref(false)
</script>

<template>
    <div style="display: inline-block; min-width: 200px; max-width: 300px;">
        <!-- 顶部信息 -->
        <div class="topShow">
            <div style="font-size: 25px;">{{ isMine ? userIderStore.userInfo.nickname : userInfo.nickname }}</div>
            <div>{{ userInfo.userLevel }}</div>
            <div>
                <span style="position: relative;">
                    <el-avatar :size="100" :src="isMine ? showingAvatar : props.userInfo.imageUrl"
                        @click="avatarDialogVisible = true" />

                    <el-upload style="position: absolute; left: 76px; bottom: -3px;" v-show="props.isMine"
                        :show-file-list="false" :before-upload="beforeImageUpload" list-type="picture" accept="image/*">
                        <el-button circle>
                            <el-icon>
                                <Plus />
                            </el-icon>
                        </el-button>
                    </el-upload>

                </span>
            </div>
        </div>
        <el-dialog v-model="avatarDialogVisible" width="55%" :show-close="false" center>
            <div style="text-align: center;">
                <el-image style="height: 620px; min-width: 620px;" :src="isMine ? showingAvatar : props.userInfo.imageUrl">
                    <template #error>
                        <div class="image-slot">
                            <el-icon><Picture></Picture></el-icon>
                        </div>
                    </template>
                </el-image>
            </div>
        </el-dialog>


        <!-- 中部信息 -->
        <ModifyInfoForm v-if="props.isMine" :userInfo="props.userInfo"></ModifyInfoForm>
        <ShowTab v-else :userInfo="props.userInfo"></ShowTab>

        <!-- 底部信息 -->
        <el-card>
            <div class="count">
                <div>
                    <span>得分</span><span>{{ userInfo.userScore }}</span>
                </div>
                <div>
                    <span>总局数</span><span>{{ userInfo.gameTotalCounts }}</span>
                </div>
                <div>
                    <span>好友对战局数</span><span>{{ userInfo.gamePersonCounts }}</span>
                </div>
                <div>
                    <span>人机对战局数</span><span>{{ userInfo.gameAiCounts }}</span>
                </div>
                <div>
                    <span>获胜局数</span><span>{{ userInfo.gameSuccessCounts
                    }}</span>
                </div>
                <div>
                    <span>失败局数</span><span>{{ userInfo.gameFailCounts }}</span>
                </div>
                <div>
                    <span>平局数</span><span>{{ userInfo.gameDeadHeatCounts }}</span>
                </div>
            </div>
        </el-card>


        <div v-show="props.isMine" style="text-align: right;">

            <!-- 退出按钮 -->
            <el-button style="margin-right: 10px;" link @click="outDialogVisible = true">退出</el-button>
            <el-dialog v-model="outDialogVisible" title="退出登录" width="30%" center>
                <div style="text-align: center; font-size: 25px;">你确定要退出登录吗？</div>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button size="large" type="primary" @click="outDialogVisible = false">取消</el-button>
                        <el-button size="large" @click="onOut">确定</el-button>
                    </span>
                </template>
            </el-dialog>

            <!-- 注销按钮 -->
            <el-button link @click="logoutDialogVisible = true">注销</el-button>
            <el-dialog v-model="logoutDialogVisible" title="账号注销" width="30%" center>
                <div style="text-align: center; font-size: 25px;">你确定要注销你的账号吗？</div>
                <template #footer>
                    <span class="dialog-footer">
                        <el-button size="large" type="primary" @click="logoutDialogVisible = false">取消</el-button>
                        <el-button size="large" @click="onLogout">确定</el-button>
                    </span>
                </template>
            </el-dialog>
        </div>

    </div>
</template>

<style scoped>
.image-slot {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 100%;
    height: 100%;
    background: var(--el-fill-color-light);
    color: var(--el-text-color-secondary);
    font-size: 30px;
}

.topShow {
    background-color: #2c3e50;
    color: white;
    padding-bottom: 10px;
    line-height: 30px;
    padding: 5px;
}

.topShow>div {
    text-align: center;
}

.count {
    line-height: 25px;
}

.count>div {
    display: flex;
    justify-content: space-between;
}
</style>
