<script setup>
// 好友模块
import { storeToRefs } from 'pinia'
import { ref, watch, nextTick } from 'vue'
import { useShowingFrienderStore } from '@/stores/showingFriender'
import { useUserIderStore } from '@/stores/userIder';
import UserInfoShow from '@/components/UserInfoShow.vue';
import FriendItem from '@/components/FriendItem.vue';
import ChatItem from '@/components/ChatItem.vue';
import { timestampToTime } from '@/utils/time'
import { getUserInfo } from '@/api/account.js'
import { getFriendList, getChatInfo, addFriend } from '@/api/friend.js'
import { ElMessage } from 'element-plus'

const showingFrienderStore = useShowingFrienderStore()
const { showingFriendInfo, unreadMessageObj } = storeToRefs(showingFrienderStore)
const userIderStore = useUserIderStore()
const myInfo = ref({}) // 我的信息
const friendList = ref([]) // 好友列表
const chatList = ref([]) // 与当前选择的好友的聊天信息
const isShowingNone = ref(true) // 当前是否选择了好友

const init = async () => { // 初始化
    var res = await getUserInfo(userIderStore.userInfo.id) // 获取个人信息
    myInfo.value = res.data
    res = await getFriendList() // 获取好友列表
    friendList.value = res.data
    showingFriendInfo.value.id = -1 // 最开始不显示任何好友的信息
    userIderStore.allChatInfo = []
}
init()
const refresh = async () => { // 添加好友后更新好友列表
    let res = await getFriendList()
    friendList.value = res.data
}

const getUnreadCount = (friendId) => { // 获取某位好友的未读消息数量
    for (let i in unreadMessageObj.value.userMessageResultList) {
        if (friendId === unreadMessageObj.value.userMessageResultList[i].friendId) {
            return unreadMessageObj.value.userMessageResultList[i].count
        }
    }
    return 0
}
const removeFriendInList = (friendId) => { // 在好友列表中移除某位好友
    friendList.value = friendList.value.filter((item) => item.id != friendId)
}
const scroll = ref(null)
watch(chatList, async () => { // 发送聊天信息后，聊天窗口聚焦到最底部
    await nextTick()
    scroll.value.setScrollTop(2000000005)
}, {
    deep: true
})

const select = async () => { // 选择好友后更新聊天界面
    var res = await getChatInfo(showingFriendInfo.value.id)
    chatList.value = res.data.reverse() // 获取历史聊天信息
    console.log('get chatList', chatList.value)
    isShowingNone.value = false
}
watch(() => userIderStore.allChatInfo, (newValue) => { // 收到聊天信息
    if (newValue.length == 0)
        return
    if (newValue[0].userId == showingFriendInfo.value.id) {
        var showingChatInfo = newValue[0]
        userIderStore.allChatInfo = []
        showingChatInfo['imageUrl'] = showingFriendInfo.value.imageUrl
        chatList.value.push(showingChatInfo)
    }

}, {
    deep: true
})
const message = ref('')
const onSendMessage = () => { // 发送聊天信息
    if (message.value == ''){
        ElMessage.error('发送的消息不能为空')
        return 
    }
    userIderStore.chatSocketSendObj({
        userId: myInfo.value.id,
        targetUserId: showingFriendInfo.value.id,
        message: message.value
    })

    // 在聊天界面中显示聊天信息
    chatList.value.push({
        //id: ...,
        userId: myInfo.value.id,
        imageUrl: myInfo.value.imageUrl,
        nickname: myInfo.value.nickname,
        friendId: showingFriendInfo.value.id,
        message: message.value,
        messageTime: timestampToTime(new Date())
    })
    message.value = '' // 清空聊天输入框
}

const addFriendvisible = ref(false) // 控制显示添加好友的气泡对话框
const nameType = ref('username') // 添加好友用 username / nickname
const friendUsernameOrNickname = ref('') // 要添加好友输入的用户名或昵称
const onAddFriend = async () => { // 添加好友
    if (friendUsernameOrNickname.value && nameType.value) {
        var obj = {}
        obj[nameType.value] = friendUsernameOrNickname.value
        await addFriend(obj)
        addFriendvisible.value = false
        ElMessage.success('好友 ' + friendUsernameOrNickname.value + ' 添加成功')
        refresh()
    } else {
        ElMessage.error('请输入好友用户名或昵称')
    }
}

const subTotalCount = (num) => { // 总未读消息数量中减去已读的数量
    unreadMessageObj.value.totalCount -= num
    if (unreadMessageObj.value.totalCount < 0)
        unreadMessageObj.value.totalCount = 0
}
</script>

<template>
    <div style="height: 100%; background-color: rgb(242, 241, 241);">
        <span style="display: flex; height: 100%;">
            <!-- 好友列表 -->
            <div style="min-width: 230px; height: 100%;">
                <div style="text-align: right;">
                    <el-tag v-show="unreadMessageObj.totalCount != 0" style="margin-right: 5px;" type="danger" round>{{
                        unreadMessageObj.totalCount < 100 ? unreadMessageObj.totalCount : '99+' }}</el-tag>

                            <!-- 添加好友按钮 -->
                            <el-popover :visible="addFriendvisible" placement="right" :width="330" trigger="click">
                                <template #reference>
                                    <el-button style="margin-right: 20px;" type="primary" @click="addFriendvisible = true">
                                        <el-icon>
                                            <Plus />
                                        </el-icon>
                                    </el-button>
                                </template>
                                <div style="text-align: center;">
                                    <el-input v-model="friendUsernameOrNickname" placeholder="用户名/昵称">
                                        <template #prepend>
                                            <el-select v-model="nameType" placeholder="用户名/昵称" style="width: 85px">
                                                <el-option label="用户名" value="username" />
                                                <el-option label="昵称" value="nickname" />
                                            </el-select>
                                        </template>
                                    </el-input>
                                    <div style=" margin-top: 10px;">
                                        <el-button size="small" type="primary"
                                            @click="addFriendvisible = false">取消</el-button>
                                        <el-button size="small" @click="onAddFriend">确定</el-button>
                                    </div>
                                </div>
                            </el-popover>
                </div>
                <el-scrollbar max-height="100%">
                    <FriendItem style="margin-top: 10px; border-bottom: 1px rgb(224, 221, 221) solid;" v-for="item in friendList" :key="item" :friendInfo="item"
                        :unreadCount="getUnreadCount(item.id)" @remove="removeFriendInList" @subTotalCount="subTotalCount"
                        @select="select">
                    </FriendItem>
                </el-scrollbar>
            </div>

            <el-divider style="height: auto;" direction="vertical" />

            <!-- 聊天窗口 -->
            <div style="display: inline-block; width: 500px; max-height: 100%; margin: 0 5px; position: relative;">
                <el-scrollbar ref="scroll" class="chat" max-height="94%">
                    <ChatItem style="margin-bottom: 5px;" v-for="item in chatList" :key="item" :myInfo="myInfo"
                        :chatItemInfo="item"></ChatItem>
                </el-scrollbar>
                <div style="position: absolute; display: flex; bottom: 0; width: 100%;">
                    <el-input style="width: 100%;" :disabled="isShowingNone" v-model="message"
                        :autosize="{ minRows: 2, maxRows: 5 }" type="textarea" />
                    <span style="margin-top: 10px; margin-left: 7px; margin-right: -6px;">
                        <el-button type="success" :disabled="isShowingNone" @click="onSendMessage">
                            <el-icon>
                                <Upload />
                            </el-icon>
                        </el-button>
                    </span>
                </div>
            </div>

            <el-divider style="height: auto" direction="vertical" />
            <span style="width: 210px;">
                <UserInfoShow v-show="!isShowingNone" :isMine="false" :userInfo="showingFriendInfo"></UserInfoShow>
            </span>

        </span>
    </div>
</template>

<style scoped>

.chat {
    display: inline-block;
    width: 100%;
    height: 93%;
    padding: 5px;
}
</style>
@/utils/time