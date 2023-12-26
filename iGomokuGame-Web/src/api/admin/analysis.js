import request from '@/utils/request'

// 添加网站点击量
export const addWebsiteClick = () => {
  return request.post('/add/website/click')
}

// 获取棋局信息
export const getChessboardCountsInfo = () => {
  return request.get('/analysis/game/counts')
}

// 获取网站信息
export const getWebsiteInfo = () => {
  return request.get('/analysis/website/info')
}

// 获取地域数据
export const getRegionData = () => {
  return request.get('/analysis/visitor/count')
}

// 获取网站访客详细信息
export const getWebsiteVisitorDetails = () => {
  return request.get('/analysis/visitor/details')
}

// 获取服务器一些性能信息
export const getServerInfo = () => {
  return request.get('/analysis/server/monitor')
}
