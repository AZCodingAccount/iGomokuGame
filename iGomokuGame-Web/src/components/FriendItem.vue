<script setup>
import { ref, defineProps, defineEmits } from 'vue'
import { delFriend, modifyUnread } from '@/api/friend.js'
import { useShowingFrienderStore } from '@/stores/showingFriender'
import { timeToText } from '@/utils/time'
const showingFrienderStore = useShowingFrienderStore()

const props = defineProps({
    friendInfo: Object,
    unreadCount: Number
})
const unreadCount = ref(props.unreadCount)
const emit = defineEmits(['select', 'subTotalCount', 'remove'])
const onClick = async () => {
    // console.log('onClick friend!')
    // 切换展示当前好友的聊天对话框和好友信息
    showingFrienderStore.setShowingFriendInfo(props.friendInfo)
    emit('select') // 更新聊天信息

    // 去除未读消息
    if (unreadCount.value != 0) {
        await modifyUnread(props.friendInfo.id)
        // 移除未读消息成功
        emit('subTotalCount', unreadCount.value)
        unreadCount.value = 0
    }
}

const visible = ref(false)
const friendId = props.friendInfo.id
const sureDelFriend = async () => {
    if (unreadCount.value != 0) {
        emit('subTotalCount', unreadCount.value)
        await modifyUnread(friendId)
        unreadCount.value = 0
    }
    await delFriend(friendId)
    // 删除成功
    emit('remove', friendId) // 将好友从列表中删除
    visible.value = false
    // console.log('del success!')
}

</script>

<template>
    <div style="display: flex; justify-content: space-between;">
        <!-- 人物按钮 -->
        <el-button link class="item" @click="onClick">
            <div>
                <span style="position: relative;">
                    <el-avatar :src="props.friendInfo.imageUrl" />
                    <div class="unreadRedPoint"
                        :style="props.friendInfo.online != '在线' ? 'background-color: red;' : 'background-color: rgb(104, 244, 104);'">
                    </div>
                </span>
                <div>
                    <div style="display: flex;">
                        <div style="text-align: left; margin-left: 5px;">
                            <div style="font-size: 20px;">{{ props.friendInfo.nickname }}</div>
                            <div>{{ props.friendInfo.userLevel }}</div>
                        </div>
                        <el-tag v-show="unreadCount != 0" style="margin-top: 5px; margin-left: 10px;" type="danger"
                            size="small" round>{{ unreadCount < 100 ? unreadCount : '99+' }}</el-tag>
                    </div>
                    <div v-show="props.friendInfo.online != '在线'">
                        {{ timeToText(props.friendInfo.lastOnlineTime) }}
                    </div>
                </div>


            </div>
        </el-button>

        <!-- 删除按钮 -->
        <el-popover :visible="visible" placement="right" :width="250" trigger="click">
            <template #reference>
                <el-button style="margin-top: 7px; margin-right: 19px;" size="small" type="danger"
                    @click="visible = true"><el-icon>
                        <Minus />
                    </el-icon></el-button>
            </template>
            <div style="text-align: center; line-height: 25px;">
                <div>确定要删除他/她为你的好友？</div>
                <div style="color: rgb(103, 103, 245);">- {{ props.friendInfo.nickname }} -</div>
                <div>
                    <el-button size="small" type="primary" @click="visible = false">取消</el-button>
                    <el-button size="small" @click="sureDelFriend">确定</el-button>
                </div>
            </div>
        </el-popover>


    </div>
</template>

<style scoped>
.item {
    border-radius: 0;
}

.item>span>div {
    display: flex;
}

.item>span>div>div {
    display: inline-block;
}

.unreadRedPoint {
    position: absolute;
    display: inline-block;
    width: 15px;
    height: 15px;
    border-radius: 10px;
    top: -2px;
    right: -4px;
}
</style>
