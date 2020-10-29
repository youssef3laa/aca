import Vue from 'vue'
import App from './App.vue'
import vuetify from './modules/core-module/lib/vuetify';
import i18n from './modules/core-module/lib/i18n'
import router from './router'
import { ValidationProvider } from 'vee-validate';
Vue.component('ValidationProvider', ValidationProvider);

Vue.config.productionTip = false

new Vue({
  vuetify,
  i18n,
  router,
  render: h => h(App)
}).$mount('#app')
