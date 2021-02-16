<template>
  <div id="app">
    <v-app id="inspire">
      <!-- <dynamicView /> -->
      <TheNavbar />
      <v-content class="content">
        <router-view />
      </v-content>
    </v-app>
  </div>
</template>

<style lang="scss">
@import url(http://fonts.googleapis.com/earlyaccess/amiri.css);
@import url(https://fonts.googleapis.com/css2?family=Cairo:wght@200;300;400&display=swap);
@import url(https://use.fontawesome.com/releases/v5.15.2/css/all.css);

.content {
  margin-top: 80px;
}
.splitpanes--dragging {
  pointer-events: none;
}

.container {
  max-width: 98%;
}

h1,
span,
label {
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
    position: relative;
    bottom: 25px;
  }
  .v-expansion-panel::before {
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
.top-bar {
  height: 80px;
  background: white;
  border-radius: 10px;
}

.v-data-table > .v-data-table__wrapper tbody tr.v-data-table__expanded__content {
  box-shadow: unset !important;
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
    console.log("App This", this)
  },
}
</script>
