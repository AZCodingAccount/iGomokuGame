<script setup>
import { defineProps } from 'vue'
const props = defineProps({
    userInfo: Object
})

</script>

<template>
    <el-card>
        <div class="tab">
            <div>
                <div style="font-weight: 1000;">用户名</div><div style="max-height: 60px; overflow-y: auto; word-wrap : break-word;">{{ userInfo.username }}</div>
            </div>
            <div>
                <div style="font-weight: 1000;">昵称</div><div style="max-height: 60px; overflow-y: auto; word-wrap : break-word;">{{ userInfo.nickname }}</div>
            </div>
            <div>
                <div style="font-weight: 1000;">性别</div><div>{{ userInfo.gender }}</div>
            </div>
            <div>
                <div style="font-weight: 1000;">年龄</div><div>{{ userInfo.age }}</div>
            </div>
            <div>
                <div style="font-weight: 1000;">自我介绍</div><div style="max-height: 60px; overflow-y: auto; word-wrap : break-word;">{{ userInfo.decription }}</div>
            </div>
            <div>
                <div style="font-weight: 1000;">联系方式</div><div style="max-height: 60px; overflow-y: auto; word-wrap : break-word;">{{ userInfo.socialAccount }}</div>
            </div>
        </div>
    </el-card>
</template>

<style scoped>
.tab {
    line-height: 28px;
    text-align: left;
}
</style>
