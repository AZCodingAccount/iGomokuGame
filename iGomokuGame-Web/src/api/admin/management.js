import request from '@/utils/request'

export const getFeedbackList = () => {
  return request.get('/management/feedbacks')
}

export const fixFeedback = (feedback) => {
  return request.put('/management/feedbacks', feedback)
}

// 获取用户数据，条件获取
export const getUserList = (user) => {
  return request.get('/management/users', {
    params: user
  })
}

// 禁用用户账号
export const disableUser = (id) => {
  return request.delete(`/user/${id}`)
}
