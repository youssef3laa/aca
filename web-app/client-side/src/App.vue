<template>
  <div id="app">
    <TheNavbar />
    <v-app id="inspire">
      <!-- <dynamicView /> -->
      <router-view />
    </v-app>
  </div>
</template>

<style lang="scss">
@import url(http://fonts.googleapis.com/earlyaccess/amiri.css);
@import url(https://fonts.googleapis.com/css2?family=Cairo:wght@200;300;400&display=swap);
@import url(https://use.fontawesome.com/releases/v5.0.13/css/all.css);
.container {
  max-width: 98%;
}

h1,span,label {
  font-family: 'Cairo', sans-serif;
}
h1 {
  font-weight: bold;
}

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
  .v-expansion-panel::before{
    box-shadow: none;
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
import TheNavbar from './modules/core-module/components/the-nav-bar'
import SystemUser from './config/user'
import Vue from 'vue'
// import dynamicView from './components/DynamicView'

export default {
  components: { TheNavbar },
  methods: {
    syncUser: function() {
      var systemUser = localStorage.getItem('user')
      Vue.prototype.$user = new SystemUser()

      if (systemUser) Vue.prototype.$user.create(JSON.parse(systemUser))
    },
  },
  created: function() {
    this.syncUser()
  },
}
</script>