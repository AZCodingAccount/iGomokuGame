import request from '@/utils/request'

export const addFriend = (data) => {
    return request.post('/user/friend', data)
}

export const delFriend = (friendId) => {
    return request.delete('/user/friend/' + friendId)
}

export const getFriendList = () => {
    return request.get('/user/friend/list')
}

export const getUnreadMessageObj = () => {
    return request.get('/user/friend/unread/message/list')
}

export const modifyUnread = (friendId) => {
    return request.put('/user/friend/message/' + friendId)
}

export const getChatInfo = (friendId) => {
    return request.get('/user/friend/list/' + friendId)
}

