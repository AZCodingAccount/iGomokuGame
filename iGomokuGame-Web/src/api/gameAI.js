import request from '@/utils/request'

// AI对战所用到的所有请求

// 根据用户id查询用户信息
export const getUserInfo = (id) => {
  return request.get(`/user/${id}`)
}

// 添加AI棋局
export const addAIChess = () => {
  return request.post('/game/ai/games')
}

// 下棋的信息传输
export const sendChessDetail = (data) => {
  return request.post('/game/ai/pieces', data)
}
