import Vue from 'vue'
import App from './App.vue'
import vuetify from './modules/core-module/lib/vuetify'
import observable from './modules/core-module/lib/vue-sub-lib'
import i18n from './modules/core-module/lib/i18n'
import router from './router'
import { ValidationProvider } from 'vee-validate'
import axios from '../node_modules/axios'
import VueSignaturePad from 'vue-signature-pad'
import ConfirmDialogue from '../src/modules/application-builder-module/components/ConfirmDialogue'
import AlertComponent from '../src/modules/application-builder-module/components/alertComponent'
Vue.use(VueSignaturePad);

Vue.component('ValidationProvider', ValidationProvider)
Vue.component('axios', () => {})
Vue.component('ConfirmDialogue', ConfirmDialogue)
Vue.component('AlertComponent', AlertComponent)

Vue.config.productionTip = false
// Vue.prototype.$alert = AlertComponent
new Vue({
    vuetify,
    i18n,
    router,
    axios,
    observable,
    ConfirmDialogue,
    AlertComponent,
    VueSignaturePad,
    render: (h) => h(App),
}).$mount('#app')