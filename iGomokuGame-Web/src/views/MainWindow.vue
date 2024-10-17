<script setup>
// 最上面那个页面
import { ref } from 'vue'
import userInfoShow from '@/components/UserInfoShow.vue'
import { getUserInfo } from '@/api/account.js'
import { useUserIderStore } from '@/stores/userIder'
import { useShowingFrienderStore } from '@/stores/showingFriender'
// import router from '@/router'

// console.log('mainWindow')
const showingFrienderStore = useShowingFrienderStore()
const userIderStore = useUserIderStore()
const userInfo = ref({}) // 用户信息
const init = async () => { // 获取个人信息
    var res = await getUserInfo(userIderStore.userInfo.id)
    userInfo.value = res.data
}
init()
const drawer = ref(false) // 控制个人信息抽屉的显示
</script>

<template>
    <div class="background">
        <el-image src="/img/background/a202c44c2075478dbfac39baeb800046.png" />
    </div>
    <!-- 菜单 -->
    <el-menu class="menu" :default-active="$route.path" mode="horizontal" :unique-opened="true" :ellipsis="false" :router="true">
        <div style="flex-grow: 1;"></div>

        <!-- 游戏界面按钮 -->
        <el-menu-item index="/main/game">
            <span style="font-size: 20px;">
                <el-icon size="25px">
                    <TrophyBase />
                </el-icon>
            </span>
        </el-menu-item>

        <!-- 好友界面按钮 -->
        <el-menu-item index="/main/friend">
            <span style="font-size: 20px;">
                <el-icon style="position: relative;" size="25px">
                    <div class="unreadRedPoint" v-show="showingFrienderStore.unreadMessageObj.totalCount != 0"></div>
                    <ChatDotRound />
                </el-icon>
            </span>
        </el-menu-item>

        <!-- 反馈界面按钮 -->
        <el-menu-item index="/main/feedback">
            <span style="font-size: 20px;">
                <el-icon size="25px">
                    <EditPen />
                </el-icon>
            </span>
        </el-menu-item>

        <!-- 个人信息按钮 -->
        <el-menu-item>
            <span style="padding: 5px; border-radius: 5px;">
                <el-button link @click="drawer = true">
                    <span class="myButton">
                        <div>
                            <el-avatar shape="square" :size="50" :src="userInfo.imageUrl" />
                            <div>
                                <div style="font-size: 25px; color: black;">
                                    {{ userIderStore.userInfo.nickname }}
                                </div>
                                <div>
                                    {{ userInfo.userLevel }}
                                </div>
                            </div>
                        </div>
                    </span>

                </el-button>
            </span>
            <el-drawer v-model="drawer" :with-header="false" :show-close="false" :size="320">
                <span>
                    <userInfoShow :isMine="true" :userInfo="userInfo" @initMainWindow="init"></userInfoShow>
                </span>
            </el-drawer>
        </el-menu-item>
    </el-menu>

    <!-- 主窗口 -->
    <div class="mainWindow">
        <router-view />
    </div>
</template>

<style scoped>
.background {
    position: fixed;
    width: 100%;
    height: 100%;
    z-index: 0;
}

.mainWindow {
    position: fixed;
    left: 30px;
    top: 70px;
    display: inline-block;
    box-shadow: var(--el-box-shadow-dark);
    padding: 10px;
    z-index: 1;
    background-color: white;
    border-radius: 5px;
    height: 820px;
}

.menu {
    position: absolute;
    width: 100%;
    top: 0;
}

body {
    height: 89%;
}

.unreadRedPoint {
    position: absolute;
    display: inline-block;
    background-color: red;
    width: 10px;
    height: 10px;
    border-radius: 10px;
    top: 1px;
    right: -4px;
}

.myButton>div {
    display: flex;
}

.myButton>div>div {
    text-align: left;
    line-height: 25px;
    margin-left: 5px;
}</style>
