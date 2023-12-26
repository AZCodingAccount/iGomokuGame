import request from '@/utils/request'

export const login = (data) => {
    return request.post('/user/login', data)
}

export const register = (data) => {
    return request.post('/user/register', data)
}

export const getUserInfo = (id) => {
    return request.get('/user/' + id)
}

export const uploadAvatar = (data) => {
    return request.post('/user/upload', data)
}

export const updateInfo = (data) => {
    return request.put('/user', data)
}

export const logout = (id) => {
    return request.delete('/user/' + id)
}

export const sendFeedbackMessage = (data) => {
    return request.post('/management/feedbacks', data)
}