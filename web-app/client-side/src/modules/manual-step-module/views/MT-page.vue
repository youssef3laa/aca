<template>
  <!-- <v-app id="inspire"> -->
  <v-container>
    <AppBuilder :app="app" />
    <ApprovalCard :taskID="taskId" />
  </v-container>

  <!-- </v-app> -->
</template>

<script>
import http from '../../core-module/services/http'
import AppBuilder from '../../application-builder-module/components/app-builder'
import ApprovalCard from '../../approval-card-module/Approval-component'

export default {
  name: 'MT',
  props: ['item'],
  components: {
    AppBuilder,
    ApprovalCard,
  },
  computed: {
    task() {
      const taskId = this.$route.params.taskId
      return this.tasks[taskId]
    },
  },
  mounted() {
    http
      .post('/workflow/task/claim', this.$route.params.taskId)
      .then(function(response) {
        console.log(response)
      })
      .catch(function(error) {
        console.error(error)
      })
    const item = this.$route.params.item
    console.log(item)
    // this.$observable.subscribe('BPItem', function(response) {
    //   console.log(response)
    // })
    // console.log(this)
    // console.log(this.item)
  },
  created() {
    this.taskId = this.$route.params.taskId
    // this.$observable.subscribe('BPItem', function(response) {
    //   console.log(response)
    // })
  },
  data() {
    return {
      taskId: '',
      tasks: [],
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
                        type: 'InputComponent',
                        label: 'First name',
                        name: 'Fname',
                        col: 4,
                        rule: 'required|minmax:2,25',
                        //  readonly : true
                      },
                      {
                        type: 'InputComponent',
                        label: 'Last name',
                        name: 'Lname',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                      {
                        type: 'InputComponent',
                        label: 'Email Adress',
                        name: 'Email',
                        col: 4,
                        rule: 'required|minmax:2,25',
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
            ],
          },
        ],
      },
    }
  },
}
</script>
