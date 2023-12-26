import { createApp } from 'vue'
import { createPinia } from 'pinia'
import persist from 'pinia-plugin-persistedstate'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

import App from './App.vue'
import router from './router'
import datav from '@iamzzg/data-view/dist/vue3/datav.map.vue.esm'
import '@/assets/main.css'

const app = createApp(App)
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}
const pinia = createPinia()

app.use(createPinia().use(persist))
app.use(router)

app.use(ElementPlus, {
  locale: zhCn
})
app.use(datav)

app.mount('#app')

// import { defineStore } from 'pinia'

// defineStore('persisted', {
//   state: () => ({ saved: '' })
// })
// import { createPersistedState } from 'pinia-plugin-persistedstate'

// pinia.use(
//   createPersistedState({
//     auto: true
//   })
// )
