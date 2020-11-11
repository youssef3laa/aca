<template>
  <div>
    <TheNavbar />
    <v-app id="inspire">
      <v-container>
        <!-- <TableComponent /> -->
        <component v-bind:is="formBuilder"></component>
      </v-container>
    </v-app>
  </div>
</template>

<script>
import TheNavbar from '../../core-module/components/the-nav-bar'
import TableComponent from '../../core-module/components/tabel-component'
import http from '../../core-module/services/http'

export default {
  name: 'HomePage',
  components: {
    TheNavbar,
    TableComponent,
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
          this.componentName = response.data.data
          this.componentName = 'EmployeeData'
        })
    },
  },
  computed: {
    formBuilder: function() {
      if (this.componentName) {
        return () => import(`../../../components/${this.componentName}`)
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
