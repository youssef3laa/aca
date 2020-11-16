<template>
  <div>
    <TheNavbar />
    <v-app id="inspire">
      <v-container>
        <TableComponent />
        <component
          v-if="formBuilder != null"
          v-bind:is="formBuilder"
        ></component>
        <appBuilder :app="app" />
      </v-container>
    </v-app>
  </div>
</template>

<script>
import TheNavbar from '../../core-module/components/the-nav-bar'
import TableComponent from '../../core-module/components/tabel-component'
import http from '../../core-module/services/http'
import appBuilder from '../../application-builder-module/components/app-builder'
// import https from '../../application-builder-module/components/app-builder'

export default {
  name: 'HomePage',
  components: {
    TheNavbar,
    TableComponent,
    appBuilder,
  },
  data() {
    return {
      currentTabComponent: null,
      componentName: null,
      app: {
        pages: [
          {
            sections: [
              {
                tabs: [
                  {
                    id: 1,
                    name: 'بيانات السياسة',
                  },
                ],
                forms: [
                  {
                    publish: 'submit',
                    event: 'submit',
                    form: [
                      {
                        type: 'inputComponent',
                        label: 'First name',
                        name: 'Fname',
                        col: 4,
                        rule: 'required|minmax:2,25',
                        //  readonly : true
                      },
                      {
                        type: 'inputComponent',
                        label: 'Last name',
                        name: 'Lname',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                      {
                        type: 'inputComponent',
                        label: 'Email Adress',
                        name: 'Email',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                      {
                        type: 'buttonComponent',
                        action: 'submit',
                        label: 'submit',
                        name: 'submitBtn',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                    ],
                    model: {
                      Fname: '',
                      Lname: '',
                      Email: '',
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
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
          // this.componentName = 'app-builder.vue'
          this.componentName = 'EmployeeData.vue'
        })
    },
  },
  computed: {
    formBuilder: function() {
      if (this.componentName) {
        // return () => import(`../../application-builder-module/components/${this.componentName}`)
        return () => import(`../../../components/${this.componentName}`)
      }
      return null
    },
  },
  mounted: function() {
    this.$observable.subscribe('submit', function(model) {
      if (model.valid) {
        http
          .post('employee/initiate/', model)
          .then((response) => {
            console.log(response)
          })
          .catch((error) => {
            console.error(error)
          })
      }
      console.log(model)
    })
    this.getHumanTask()
    this.getTable()
  },
}
</script>
