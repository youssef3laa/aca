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
import AppBuilder from '../../application-builder-module/builders/app-builder'
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

          this.$refs.appBuilders.setModelData('form1', {
            Fname: firstName,
            Lname: lastName,
            Email: email,
          })

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
            key: 'page1',
            tabs: [
              {
                key: 'tab1',
                id: '1',
                isActive: true,
                name: 'البيانات الأساسية',
                icon: 'far fa-file-alt',
              },
              {
                key: 'tab2',
                id: '2',
                name: 'المرفقات',
                icon: 'fas fa-paperclip',
              },
              {
                key: 'tab3',
                id: '3',
                name: 'collapse',
                icon: 'fas fa-paperclip',
              },
            ],
            sections: [
              {
                key: 'title',
                type: 'TitleComponet',
                name: 'بيانات المكاتبة',
                actions: ['cancel', 'save', 'complete'],
                // sections: [
                //   {
                //     type: 'title',
                //   },
                // ],
              },
              {
                key: 'section1',
                tabId: '1',
                isTab: true,
                type: 'DefaultSection',
                isCard: true,
                display: 'block',
                forms: [
                  {
                    key: 'form1',
                    publish: 'form1Change',
                    inputs: [
                      {
                        type: 'AutoCompleteComponent',
                        name: 'receiver',
                        rule: 'required',
                        // url: '',
                        // list: '',
                        // isAutoComplete: true,
                        col: '6',
                      },
                      {
                        type: 'DatePickerComponent',
                        label: 'requestDate',
                        name: 'requestDate',
                        rule: 'required',
                        col: '6',
                      },
                      {
                        type: 'TextareaComponent',
                        label: 'notes',
                        name: 'notes',
                        col: '12',
                      },
                    ],
                    model: {
                      receiver: {
                        url: 'org/group/findByCodes/HGCS,HRCA,HCAO',
                        list: [],
                        value: '',
                      },
                      requestDate: '',
                      notes: '',
                    },
                  },
                ],
              },
              {
                key: 'section2',
                type: 'Resizable',
                tabId: '2',
                isTab: true,
                isCard: true,
                display: 'none',
                forms: [
                  {
                    resizable: {
                      forms: [
                        {
                          key: 'iframeObj',
                          background: 'white',
                          inputs: [
                            {
                              type: 'IframeComponent',
                              name: 'iframeObj',
                              col: 12,
                            },
                          ],
                          model: {
                            iframeObj: {
                              src: '',
                            },
                          },
                        },
                        {
                          background: 'white',
                          inputs: [
                            {
                              type: 'InputFileComponent',
                              name: 'inputFile',
                              col: 12,
                            },
                          ],
                          model: {
                            inputFile: '',
                          },
                        },
                      ],
                    },
                  },
                ],
              },
              {
                key: 'section3',
                tabId: '3',
                isTab: true,
                isCard: true,
                display: 'none',
                type: 'CollapseSection',
                name: 'النص',
                forms: [
                  {
                    key: 'richtext',
                    inputs: [
                      {
                        type: 'richtextComponent',
                        name: 'richtext',
                        col: 12,
                      },
                    ],
                    model: {
                      richtext: '<p></p>',
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
