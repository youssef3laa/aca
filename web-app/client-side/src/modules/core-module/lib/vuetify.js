import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import zhHans from 'vuetify/es5/locale/ar'
import sv from '../../../locales/en.json'

Vue.use(Vuetify);


const vuetify = new Vuetify({
  lang: {
    locales: { zhHans, sv },
    current: 'zhHans',
  },
  theme: {
    themes: {
      light: {
        primary: '#3f51b5',
        secondary: '#b0bec5',
        accent: '#8c9eff',
        error: '#b71c1c',
      },
    },
  },
})

export default vuetify;
