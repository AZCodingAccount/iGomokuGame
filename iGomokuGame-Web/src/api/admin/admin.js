import request from '@/utils/request'

export const loginService = (data) => {
  return request.post('/admin/login', data)
}
