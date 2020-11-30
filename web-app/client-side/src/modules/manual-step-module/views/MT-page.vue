<template>
  <!-- <v-app id="inspire"> -->
  <v-container>
    <AppBuilder ref="appBuilders" :app="app" />
    <!-- <ApprovalCard :taskID="taskId" /> -->
  </v-container>

  <!-- </v-app> -->
</template>

<script>
import http from '../../core-module/services/http'
// import {ref} from 'vue'
import AppBuilder from '../../application-builder-module/components/app-builder'
// import ApprovalCard from '../../approval-card-module/Approval-component'

export default {
  name: 'MT',
  props: ['item'],
  components: {
    AppBuilder,

    // ApprovalCard,
  },
  computed: {
    task() {
      const taskId = this.$route.params.taskId
      return this.tasks[taskId]
    },
  },
  methods: {
    readEntity: function(entityId) {
      // let self = this;
      http
        .get(
          '/entity/read?projectName=MyCompanyACA&entityName=EmployeeData&entityId=' +
            entityId
        )
        .then((response) => {
          console.log(response)
          let firstName =
            response.data.data.Body.ReadEmployeeDataResponse.EmployeeData
              .FirstName
          let lastName =
            response.data.data.Body.ReadEmployeeDataResponse.LastName
          let email =
            response.data.data.Body.ReadEmployeeDataResponse.EmployeeData.Email

          this.$refs.appBuilders.setModelData('form1',
            {
              Fname: firstName,
              Lname: lastName,
              Email: email,
            }
          )

          let model = this.app.pages[0].sections[0].forms[0]
          console.log(model)
          // model.Fname = firstName
          // model.Lname = lastName
          // model.Email = email
        })
        .catch((error) => {
          console.error(error)
        })
    },
    getTaskData: function() {
      const url = '/workflow/task/data?taskId=' + this.taskId
      console.log(url)

      http
        .get(url)
        .then((response) => {
          console.log(response)
          this.readEntity(
            response.data.data.Body.GetTaskResponse.tuple.old.Task.TaskData
              .ApplicationData.inputSchemaFragment.entityId
          )
        })
        .catch((error) => {
          console.error(error)
        })
    },
  },
  mounted() {
    http
      .post('/workflow/task/claim', this.$route.params.taskId)
      .then((response) => {
        console.log(response)
        this.getTaskData()
      })
      .catch((error) => {
        console.error(error)
      })
    const item = this.$route.params.item
    console.log(item)
    this.$observable.subscribe('compelete', (model) => {
      console.log(model)
      console.log(this.app.pages[0].sections[0].forms[0].model)
      this.app.pages[0].sections[0].forms[0].form.pop()
      this.$refs.appBuilders.setAppData(this.app)
      // this.$refs.appBuilder.setPageDatas(this.app);

      // .push({
      //   type: 'InputComponent',
      //   label: 'name',
      //   name: 'Fname',
      //   col: 4,
      //   readonly: false,
      //   rule: 'required|minmax:2,25',
      // })
      console.log('complete Key')
    })
    this.$observable.subscribe('submit', (model) => {
      console.log(model)
      console.log('submit is called')
      // var output = {
      //   TaskId: this.taskId,
      //   NameSpace: 'http://schemas.cordys.com/',
      //   TaskData: {
      //     outputSchema: {
      //       Decision: model.decision,
      //       Comment: model.comment,
      //     },
      //   },
      // }
      // http
      //   .post('/workflow/complete', output)
      //   .then(function(response) {
      //     console.log(response)
      //   })
      //   .catch(function(error) {
      //     console.error(error)
      //   })
    })
  },
  created() {
    this.taskId = this.$route.params.taskId
  },
  data() {
    return {
      taskId: '',
      tasks: [],
      app: {
        pages: [
          {
            key:"page1",
            sections: [
              {
                key:"section1",
                tabs: [
                  {
                    key:"tab1",
                    id: 1,
                    name: 'بيانات السياسة',
                  },
                ],
                forms: [
                  {
                    key: 'form1',
                    publish: 'complete',
                    form: [
                      {
                        key:"input1",
                        type: 'InputComponent',
                        label: 'First name',
                        name: 'Fname',
                        col: 4,
                        readonly: false,
                        rule: 'required|minmax:2,25',
                      },
                      {
                        key:"input1",
                        type: 'InputComponent',
                        label: 'Last name',
                        name: 'Lname',
                        col: 4,
                        readonly: true,
                      },
                      {
                        key:"input1",
                        type: 'InputComponent',
                        label: 'Email Adress',
                        name: 'Email',
                        col: 4,
                        readonly: true,
                      },
                      // {
                      //   type: 'ButtonComponent',
                      //   action: 'submit',
                      //   label: 'submit',
                      //   name: 'submitBtn',
                      //   col: 4,
                      //   rule: 'required|minmax:2,25',
                      // },
                    ],
                    model: {
                      Fname: '',
                      Lname: '',
                      Email: '',
                    },
                  },
                ],
              },

              {
                forms: [
                  {
                    key:"form2",
                    publish: 'compelete',
                    event: 'submit',
                    form: [
                      {
                        key:"form2Input",
                        type: 'RadioGroupComponent',
                        decisions: [
                          {
                            label: 'approve',
                            id: 1,
                            value: 'approve',
                          },
                          {
                            label: 'reject',
                            id: 2,
                            value: 'reject',
                          },
                          {
                            label: 'requestModification',
                            id: 3,
                            value: 'requestModification',
                          },
                        ],
                        name: 'decision',
                        label: 'decision',
                        col: 6,
                      },
                      {
                        type: 'TextareaComponent',
                        label: 'comment',
                        name: 'comment',
                        col: 12,
                      },
                      {
                        type: 'ButtonComponent',
                        label: 'submit',
                        action: 'submit',
                        name: 'submit',
                        col: 4,
                      },
                    ],
                    model: {
                      decision: '',
                      comment: '',
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
}
</script>
