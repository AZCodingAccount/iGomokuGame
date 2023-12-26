<script setup>
import { defineProps } from 'vue'
import { useUserIderStore } from '@/stores/userIder'
const props = defineProps({
    myInfo: Object,
    chatItemInfo: Object
})
const userIderStore = useUserIderStore()

</script>

<template>
    <!-- 我的 -->
    <div v-if="props.myInfo.id === props.chatItemInfo.userId" class="item" style="flex-direction: row-reverse;">
        <!-- 要实现同步更新，则头像url从pinia拿 -->
        <el-avatar size="large" :src="userIderStore.userInfo.imageUrl" />
        <div>
            <div style="text-align: right;">
                <span class="time">{{ chatItemInfo.messageTime }}</span><span class="name" style="margin: 0 5px;">{{
                    userIderStore.userInfo.nickname }}</span>
            </div>
            <div>
                <span style="float: right; margin: 5px;">
                    <el-card style="text-align: left;" :body-style="{ padding: '5px' }">
                        <span>
                            {{ chatItemInfo.message }}
                        </span>
                    </el-card>
                </span>
            </div>

        </div>
    </div>
    <!-- 好友的 -->
    <div v-else class="item">
        <el-avatar size="large" :src="props.chatItemInfo.imageUrl" />
        <div>
            <div style="text-align: left;">
                <span class="name" style="margin: 0 5px;">{{ chatItemInfo.nickname }}</span><span class="time">{{
                    chatItemInfo.messageTime }}</span>
            </div>
            <div>
                <span style="float: left; margin: 5px;">
                    <el-card style="text-align: left;" :body-style="{ padding: '5px' }">
                        <span>
                            {{ chatItemInfo.message }}
                        </span>
                        
                    </el-card>
                </span>
            </div>

        </div>
    </div>
</template>

<style scoped>
.item {
    display: flex;
}

.item>div {
    max-width: 350px;
}

.time {
    color: gray;
    font-size: 13px;
}

.name {
    font-size: 20px;
}

</style>
