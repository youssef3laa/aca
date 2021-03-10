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
@import url('./assets/Sass/app.scss');

@font-face {
  font-family: 'Neo Sans Arabic Regular';
  font-style: normal;
  font-weight: normal;
  src: local('Neo Sans Arabic Regular'),
    url('./assets/fonts/Neo Sans Arabic Regular.woff') format('woff');
}

input:-webkit-autofill,
input:-webkit-autofill:hover, 
input:-webkit-autofill:focus,
textarea:-webkit-autofill,
textarea:-webkit-autofill:hover,
textarea:-webkit-autofill:focus,
select:-webkit-autofill,
select:-webkit-autofill:hover,
select:-webkit-autofill:focus {
 
  -webkit-box-shadow: 0 0 0px 1000px white inset;
  transition: background-color 5000s ease-in-out 0s;
}

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
  font-family: 'Neo Sans Arabic Regular', serif !important;
  .title {
    // To pin point specific classes of some components
    font-family: 'Neo Sans Arabic Regular', serif !important;
  }
  .red--text {
    color: #900 !important;
  }

  .color-red{
    color: #900 !important;
  }
  .v-expansion-panel::before {
    box-shadow: none;
  }
}
.attachment-iframe{
  min-height: 600px !important;
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
  height: 60px;
  background: white;
  border-radius: 10px;
}

.v-data-table
  > .v-data-table__wrapper
  tbody
  tr.v-data-table__expanded__content {
  box-shadow: unset !important;
}
.ql-editor{
   max-height: 200px !important;
   min-height: auto !important;
  overflow-y: auto !important;
}
// .thead.v-data-table-header{
//     background-color: $color-gray !important;
// }
/* scroll*/
::-webkit-scrollbar {
  width: 10px;
}

::-webkit-scrollbar-track {
  background: #f1f1f1; 
}

::-webkit-scrollbar-thumb {
  background:  #0278ae; 
}

::-webkit-scrollbar-thumb:hover {
  background:  #1a1a2e; 
}
.theme--light.v-icon {
    color: rgb(213, 213, 213);
}
.col-*{
  padding: 8px !important;
}

</style>
<script>
import TheNavbar from './modules/core-module/components/the-nav-bar'
import SystemUser from './config/user'
import Vue from 'vue'
import homePageMixin from './modules/admin-module/mixins/homePageMixin'
import CKEditor from '@ckeditor/ckeditor5-vue2'
Vue.use(CKEditor)

// import dynamicView from './components/DynamicView'

export default {
  components: { TheNavbar },
  mixins: [homePageMixin],
  methods: {
    syncUser: async function() {
      var systemUser = localStorage.getItem('user')
      Vue.prototype.$user = new SystemUser()

      if (systemUser) Vue.prototype.$user.create(JSON.parse(systemUser))

      await this.appendHomeRoutes()
    },
  },
  created: async function() {
    await this.syncUser()
    console.log('App This', this)
  },
}
</script>
