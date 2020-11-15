<template>
  <div>
    <TheNavbar />
    <v-app id="inspire">
      <v-container>
        <!-- <TableComponent /> -->
        <component v-if="formBuilder != null" v-bind:is="formBuilder"></component>
      </v-container>
    </v-app>
  </div>
</template>

<script>
import TheNavbar from '../../core-module/components/the-nav-bar'
// import TableComponent from '../../core-module/components/tabel-component'
import http from '../../core-module/services/http'
// import https from '../../application-builder-module/components/app-builder'

export default {
  name: 'HomePage',
  components: {
    TheNavbar,
    // TableComponent,
  },
  data() {
    return {
      currentTabComponent: null,
      componentName: null,
    }
  },
  methods: {
    getHumanTask: function() {
      http.get('workflow/human/tasks').then((response) => {
        console.log(response)
      })
    },
    getTable: function() {
      http
        .get('http://localhost:9000/api/vue/components/get/')
        .then((response) => {
          console.log(response)
          this.componentName = response.data.data
          this.componentName = 'app-builder.vue'
        })
    },
  },
  computed: {
    formBuilder: function() {
      if (this.componentName) {
        return () => import(`../../application-builder-module/components/${this.componentName}`)
      }
      return null
    },
  },
  mounted: function() {
    this.getHumanTask()
    this.getTable()
  },
}
</script>
