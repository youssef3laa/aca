import Vue from 'vue'
import Vuetify from 'vuetify/lib'
import arabic from 'vuetify/es5/locale/ar'
import arabicpack from '../../core-module/locales/ar.json'

Vue.use(Vuetify)

const vuetify = new Vuetify({
  rtl: true,
  lang: {
    locales: { arabic, arabicpack },
    current: 'zhHans',
  },
  theme: {
    themes: {
      light: {
        background: '#072742',
        primary: '#072742',
        secondary: '#1a4573',
        // accent: '#fffcf2',
        anchor: '#8c9eff',
        error: '#b71c1c',
        outline : '#0278ae',
        info: '#0278ae'
      },
      dark: {
        background: '#072742',
      },
    },
  },
})

export default vuetify
