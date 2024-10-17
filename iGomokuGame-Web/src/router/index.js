import { createRouter, createWebHistory } from 'vue-router'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/introduction',
      meta: { requireAuth: false } // 进入该界面不需要权限
    },
    {
      path: '/introduction',
      component: () => import('@/views/IntroductionWindow.vue'),
      meta: { requireAuth: false } // 进入该界面不需要权限
    },
    {
      path: '/boardAI',
      component: () => import('@/views/BoardAIWindow.vue')
    },
    {
      path: '/boardFriend',
      component: () => import('@/views/BoardFriendWindow.vue')
    },
    {
      path: '/main',
      component: () => import('@/views/MainWindow.vue'),
      children: [
        {
          path: '',
          redirect: '/main/game'
        },
        {
          path: 'game',
          component: () => import('@/views/GameWindow.vue')
        },
        {
          path: 'friend',
          component: () => import('@/views/FriendWindow.vue')
        },
        {
          path: 'feedback',
          component: () => import('@/views/FeedbackWindow.vue')
        }
      ]
    },
    // 下面是管理员模块路由配置
    {
      path: '/admin',
      redirect: '/admin/login',
      component: () => import('@/views/admin/AdminLogin.vue'),
      meta: { requireAuth: false } // 进入该界面不需要权限
    },
    {
      path: '/admin/login',
      component: () => import('@/views/admin/AdminLogin.vue'),
      meta: { requireAuth: false } // 进入该界面不需要权限
    },
    {
      path: '/dashboard/bigscreen',
      component: () => import('@/views/admin/BigScreen.vue')
    },
    {
      path: '/dashboard',
      redirect: '/dashboard/index',
      component: () => import('@/views/admin/LayOut.vue'),
      children: [
        {
          path: 'index',
          component: () => import('@/views/admin/AdminIndex.vue')
        },
        {
          path: 'management/feedback',
          component: () => import('@/views/admin/management/FeedbackManagement.vue')
        },
        {
          path: 'management/user',
          component: () => import('@/views/admin/management/UserManagement.vue')
        },
        {
          path: 'analysis/game',
          component: () => import('@/views/admin/analysis/GameAnalysis.vue')
        },
        {
          path: 'analysis/website',
          component: () => import('@/views/admin/analysis/WebsiteAnalysis.vue')
        },
        {
          path: 'website/monitor',
          component: () => import('@/views/admin/WebsiteMonitor.vue')
        }
      ]
    },
    {
      path: '/:pathMatch(.*)',
      component: () => import('@/views/NotFound.vue')
    }
  ]
})

/*
路由守卫：实现不同角色，是否有权限这两个功能:
      前端实现管理员和用户的身份划分。当然也可以在后端实现.后端只是判断了是否过期和是否被篡改。2023-12-26:后端也加校验了
  再细粒度的权限划分就到springSecurity了,本次没有进行判断。但是敏感的比如用户数据还是加了一些校验的。
*/
const isJwtValid = (jwt) => {
  // 验证jwt
  const token = jwt
  if (!token) {
    return false
  }
  try {
    const payload = JSON.parse(atob(token.split('.')[1])) // 解码 JWT 中的 Payload
    const now = Date.now().valueOf() / 1000 // 检查令牌是否过期
    if (typeof payload.exp !== 'undefined' && payload.exp < now) {
      return false
    }
    return true
  } catch (error) {
    return false
  }
}
import { useUserIderStore } from '@/stores/userIder'
import { useAdminStore } from '@/stores/adminInfo'

router.beforeEach((to, from, next) => {
  // 管理员和普通用户使用不同的存储，结构相似
  const userStore = useUserIderStore()
  const adminStore = useAdminStore()
  const userJwt = userStore.userInfo.jwt
  const adminJwt = adminStore.getAdminInfo().jwt

  // 检查是否需要认证
  const isAuthRequired = to.meta.requireAuth !== false

  // 检查用户角色，如果是访问后台的jwt不符合规则就重定向到管理员的登录页，访问前台的jwt不符合规则就重定向到前台的登录页
  const isAdminRoute = to.path.startsWith('/admin') || to.path.startsWith('/dashboard')
  const jwt = isAdminRoute ? adminJwt : userJwt

  if (isAuthRequired) {
    if (to.path.startsWith('/login') && isJwtValid(jwt)) {
      next('/main/game')
    }
    if (!jwt || !isJwtValid(jwt)) {
      // 如果这是管理员路由但没有有效的管理员JWT，重定向到管理员登录页
      if (isAdminRoute) {
        next('/admin/login')
      } else {
        // 否则，重定向到普通用户登录页
        next('/introduction')
      }
    } else {
      // JWT 有效，放行
      next()
    }
  } else {
    // 不需要认证，放行
    next()
  }
})

export default router
