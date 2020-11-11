import Vue from 'vue'
import App from './App.vue'
import vuetify from './modules/core-module/lib/vuetify'
import i18n from './modules/core-module/lib/i18n'
import router from './router'
import { ValidationProvider } from 'vee-validate'
import axios from '../node_modules/axios'

Vue.component('ValidationProvider', ValidationProvider)
Vue.component('axios', () => {})

Vue.config.productionTip = false

new Vue({
  vuetify,
  i18n,
  router,
  axios,
  render: (h) => h(App),
}).$mount('#app')
