<template>
  <v-container>
    <AppBuilder ref="appBuilders" :app="app"/>
  </v-container>
</template>

<script>
import AppBuilder from "@/modules/application-builder-module/components/app-builder";
import http from "@/modules/core-module/services/http";

export default {
  name: "MT2-page",
  props: ['item'],
  components: {
    AppBuilder,

    // ApprovalCard,
  },

  methods: {
    getPageConfig: function () {
      http
          .get("/user/form/" + this.taskUrl)
          .then((response) => {
            this.$refs.appBuilders.setAppData(response.data.data.app);
            this.claimTask();
          })
          .catch((error) => console.error(error));
    },
    readEntity: function (entityId) {
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
                response.data.data.Body.LastName
            let email =
                response.data.data.Body.ReadEmployeeDataResponse.EmployeeData.Email

            this.$refs.appBuilders.setModelData('form1', {
              Fname: firstName,
              Lname: lastName,
              Email: email,
            })
          })
          .catch((error) => {
            console.error(error)
          })
    },
    getTaskData: function () {
      const url = '/workflow/task/data?taskId=' + this.taskId
      http
          .get(url)
          .then((response) => {
            console.log("taskData", response);
            this.readEntity(
                response.data.data.Body.GetTaskResponse.tuple.old.Task.TaskData
                    .ApplicationData.inputSchemaFragment.entityId
            )
          })
          .catch((error) => {
            console.error(error)
          })
    },
    claimTask: function () {
      http
          .post('/workflow/task/claim', this.taskId)
          .then((response) => {
            console.log(response)
            this.getTaskData()
          })
          .catch((error) => {
            console.error(error)
          })
    },
    completeStep: function (approvalCardModel) {
      var output = {
        TaskId: this.taskId,
        NameSpace: 'http://schemas.cordys.com/',
        TaskData: {
          outputSchema: {
            Decision: approvalCardModel.decision,
            Comment: approvalCardModel.comment,
          },
        },
      }
      http
          .post('/workflow/complete', output)
          .then(function (response) {
            console.log("task is completed", response)
          })
          .catch(function (error) {
            console.error(error)
          })
    }
  },
  mounted() {


    this.$observable.subscribe('completeStep', (model) => {
      console.log("completeStep", model)
      // console.log("completeStep", this.$refs.appBuilders)
      if (this.$refs.appBuilders.getModelData("form1")._valid) {
        console.log(("valid"))
        this.completeStep(model);
      }
    })

  },
  created() {
    this.taskId = this.$route.params.taskId
    this.taskUrl = this.$route.params.taskUrl
    this.getPageConfig();
  },
  data() {
    return {
      taskId: '',
      taskUrl: '',
      model: '',
      app: {},
    }
  },
}
</script>


