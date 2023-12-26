<script setup>
// 主页面，排行榜加上的那个
import { ref, watch, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserIderStore } from '@/stores/userIder'
import { login } from '@/api/account.js'
import router from '@/router'

const userIderStore = useUserIderStore()
const rankingList = ref([]) // 排行榜列表
const init = async () => { // 登录，获取排行榜信息
    var res = await login(userIderStore.loginForm)
    rankingList.value = res.data.rankingList
    for (let i = 0; i < rankingList.value.length; i++) {
        rankingList.value[i]['ranking'] = i + 1
    }
}
init()
const myRankingInfo = computed(() => { // 获取我的排行榜信息
    return rankingList.value.filter(item => item.id == userIderStore.userInfo.id)
})

// 人机对战困难选择
const selectDifficultyDialogVisible = ref(false)
const difficultyTitle = ref('先选择一下难度吧')
const difficulty = ref(-1)
const imgUrl = ref('/img/thinking.png')
const difficultyDialog = ref(null)
watch(difficulty, (newValue) => {
    if (newValue === 1) {
        difficultyTitle.value = '简单：先试试手'
        imgUrl.value = '/img/leaf.png'
    } else if (newValue === 2) {
        difficultyTitle.value = '普通：中规中矩'
        imgUrl.value = '/img/common.png'
    } else {
        difficultyTitle.value = '困难：你很勇吼'
        imgUrl.value = '/img/devil.png'
    }
})
const confirmDifficulty = () => { // 确定难度，开始人机对战
    if (difficulty.value === -1) {
        ElMessage.error('请选择难度')
    } else { // 开始人机对战
        selectDifficultyDialogVisible.value = false
        router.push({ path: '/boardAI', query: { difficulty: difficulty.value } })
    }
}

const myType = ({ row }) => {
    if (row.id == userIderStore.userInfo.id)
        return {
            'background-color': '#b2d1cf',
            'border-radius': '10px'
        }
    return {
        'border-radius': '10px'
    }
}

const lv = (level) => {
    if (level == '初入江湖')
        return 1
    if (level == '略知一二')
        return 2
    if (level == '小有所成')
        return 3
    if (level == '登峰造极')
        return 4
    if (level == '胜天半子')
        return 5
    if (level == '渡劫飞升')
        return 6
}
</script>

<template>
    <div>
        <!-- 按钮 -->
        <div style="display: flex; justify-content: space-between; padding: 0 60px;">
            <el-button class="button" round @click="selectDifficultyDialogVisible = true">
                <el-image src="/img/robot.png"></el-image>
                <div style="font-size: 20px;">人机对战</div>
            </el-button>

            <!-- 选择难度对话框 -->
            <el-dialog ref="difficultyDialog" v-model="selectDifficultyDialogVisible" width="30%" center>
                <template #title>
                    <span style="font-size: 30px;">人机对战</span>
                </template>

                <span style="text-align: center;">
                    <div style="font-size: 30px;">{{ difficultyTitle }}</div>
                    <div style="display: flex; justify-content: space-around; padding: 30px 50px;">
                        <el-button style="width: 150px;" size="large" @click="difficulty = 1">简单</el-button>
                        <el-button style="width: 150px;" size="large" type="warning" @click="difficulty = 2">普通</el-button>
                        <el-button style="width: 150px;" size="large" type="danger" @click="difficulty = 3">困难</el-button>
                    </div>
                    <div style="text-align: center; margin-right: 10px;">
                        <el-image style="width: 100px; height: 100px" :src="imgUrl" />
                    </div>

                </span>
                <template #footer>
                    <span>
                        <el-button size="large" @click="selectDifficultyDialogVisible = false">取消</el-button>
                        <el-button type="primary" size="large" @click="confirmDifficulty">
                            确定
                        </el-button>
                    </span>
                </template>
            </el-dialog>

            <!-- 联机对战 -->
            <el-button class="button" round @click="router.push('/boardFriend')">
                <el-image src="/img/team.png"></el-image>
                <div style="font-size: 20px;">好友对战</div>
            </el-button>
        </div>

        <!-- 排行榜 -->
        <div style="font-weight: 800;">
            <el-table style="width: 100%; position: relative;" :data="myRankingInfo" :row-style="myType">
                <el-table-column prop="ranking" width="45" />
                <el-table-column prop="imageUrl" width="65">
                    <template #default="scope">
                        <el-avatar :src="scope.row.imageUrl"></el-avatar>
                    </template>
                </el-table-column>
                <el-table-column prop="nickname" width="140" />
                <el-table-column prop="userLevel">
                    <template #default="scope">
                        <span>{{ scope.row.userLevel }}（ lv{{ lv(scope.row.userLevel) }} ）</span>
                    </template>
                </el-table-column>
                <el-table-column prop="gameSuccessCounts" label="赢" width="50" />
                <el-table-column prop="gameFailCounts" label="输" width="50" />
                <el-table-column prop="gameDeadHeatCounts" label="平" width="50" />
                <el-table-column prop="userScore" label="得分" width="60" />
            </el-table>
        </div>
        <div style="font-weight: 800;">
            <el-table height="450" style="width: 100%; position: relative;" :data="rankingList" :show-header="false"
                :row-style="myType">
                <el-table-column prop="ranking" width="45" />
                <el-table-column prop="imageUrl" width="65">
                    <template #default="scope">
                        <el-avatar :src="scope.row.imageUrl"></el-avatar>
                    </template>
                </el-table-column>
                <el-table-column prop="nickname" width="140" />
                <el-table-column prop="userLevel">
                    <template #default="scope">
                        <span>{{ scope.row.userLevel }}（ lv{{ lv(scope.row.userLevel) }} ）</span>
                    </template>
                </el-table-column>
                <el-table-column prop="gameSuccessCounts" label="赢" width="50" />
                <el-table-column prop="gameFailCounts" label="输" width="50" />
                <el-table-column prop="gameDeadHeatCounts" label="平" width="50" />
                <el-table-column prop="userScore" label="得分" width="60" />
            </el-table>
        </div>


    </div>
</template>

<style scoped>

.verification-container {
    display: flex;
}

.verification-input {
    width: 54px;
    height: 54px;
    margin-right: 30px;
    text-align: center;
    font-size: 20px;
    border: 1px solid #ebebeb;
    border-radius: 5px;
}

.verification-input>.last-child {
    margin-right: 0;
}

.verification-input:focus {
    outline: none;
    border-color: #007bff;
    box-shadow: 0 0 5px #007bff;
}

.button {
    width: 250px;
    height: 250px;

}

.button>span {
    display: flex;
    flex-direction: column;
}

.myItem {
    display: flex;
    justify-content: space-around;
    padding: 5px;
    border-radius: 5px;
}

.myItem>span {
    line-height: 40px;
    margin-top: 10px;
    margin-right: 20px;
}

.item {
    display: flex;
    justify-content: space-around;
    padding: 5px;
    border-radius: 5px;
}


.item:hover {
    background-color: rgb(223, 220, 220);
}

.item>span {
    line-height: 40px;
    margin-top: 10px;
    margin-right: 20px;
}
</style>
