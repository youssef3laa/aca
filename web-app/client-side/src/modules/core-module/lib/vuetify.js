import Vue from 'vue';
import Vuetify from 'vuetify/lib';
import zhHans from 'vuetify/es5/locale/ar'
import sv from '../../../locales/ar.json'

Vue.use(Vuetify);


const vuetify = new Vuetify({
  lang: {
    locales: { zhHans, sv },
    current: 'zhHans',
  },
  theme: {
    themes: {
      light: {
        background: '#072742',
        primary: '#072742',
        secondary: '#1a4573',
        accent: '#fffcf2',
        anchor: '#8c9eff',
        error: '#b71c1c',
      },
      dark: {
        background: '#072742',
      }
    },
  },
})

export default vuetify;
