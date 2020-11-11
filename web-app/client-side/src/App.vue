<template>
  <div id="app">
    <!-- <dynamicView /> -->
    <router-view />
  </div>
</template>

<style lang="scss">
@import url(http://fonts.googleapis.com/earlyaccess/amiri.css);

.v-application {
  background-color: #f2f2f2 !important;
  font-family: 'Amiri', serif !important;
  .title {
    // To pin point specific classes of some components
    font-family: 'Amiri', serif !important;
  }
  .red--text {
    color: #900 !important;
  }
}
.theme--light.v-label {
  color: rgba(0, 0, 0, 0.6);
}
.theme--light.v-application {
  background-color: #f2f2f2;
}
h1 {
  font-size: 26px;
}
</style>
<script>
import router from './router'
import SystemUser from './config/user'
import Vue from 'vue'
// import dynamicView from './components/DynamicView'

export default {
  components: {  },
  methods: {
    syncUser: function() {
      var systemUser = localStorage.getItem('user')
      Vue.prototype.$user = new SystemUser()

      if (systemUser) Vue.prototype.$user.create(JSON.parse(systemUser))
      else router.push('login')
    },
  },
  created: function() {
    this.syncUser()
  },
}
</script>
