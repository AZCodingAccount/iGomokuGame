import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useShowingFrienderStore = defineStore('showingFriender', () => {
    // data
    const showingFriendInfo = ref({
        id: -1,
        nickname: '',
        age: 999,
        description: '',
        socialAccount: '',
        imageUrl: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        userLevel: '',
        userScore: 0,
        game_total_counts: 0,
        game_person_counts: 0,
        game_ai_counts: 0,
        game_success_counts: 0,
        game_fail_counts: 0,
        game_dead_heat_counts: 0
    })
    const setShowingFriendInfo = (data) => {
        showingFriendInfo.value = data
    }

    const unreadMessageObj = ref(
        {
            totalCount: 0,
            userMessageResultList: []
        })
    const setUnreadMessageObj = (data) => {
        unreadMessageObj.value = data
    }

    return {
        showingFriendInfo,
        setShowingFriendInfo,
        unreadMessageObj,
        setUnreadMessageObj
    }
},{
    persist: true
})