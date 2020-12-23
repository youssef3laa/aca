<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app" />
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "process-step",
  mixins: [formPageMixin],
  components: {
    AppBuilder,
  },

  methods: {
    readData: function () {
      console.log("TaskData", this.taskData);
      this.inputSchema = this.taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment;
    //   let page = this.inputSchema.page;

      this.loadForm("process-init", this.fillForm);
    },
    fillForm: function () {
      let entityName = this.inputSchema.entityName;
      let entityId = this.inputSchema.entityId;
      this.readEntity(entityName, entityId)
        .then((response) => {
          let assignedCN = this.inputSchema.assignedCN;

          response = JSON.parse(response.data.data);

          this.$refs.appBuilder.setModelData("form1", {
            notes: response.notes,
            receiver: {
              list: [
                {
                  text: response.receiver,
                  value: assignedCN,
                },
              ],
              value: assignedCN,
            },
            requestDate: response.requestDate.split("Z")[0],
          });

          console.log("response", response);
        })
        .catch((error) => {
          console.error(error);
        });
    },
  },
  async created() {
    this.taskId = this.$route.params.taskId;
    this.claimTask(this.taskId);
    this.getTaskData(this.taskId);

    this.$observable.subscribe("complete-step", () => {
      console.log("complete-step-clicked");
      console.log(this.$refs.appBuilder);
      var model = this.$refs.appBuilder.getModelData("form1");
      if (model._valid) {
        var data = {
            taskId: this.taskId,
            stepId: this.inputSchema.stepId,
            process: this.inputSchema.process,
            code: "HSEC",
            assignedCN: "cn=AbdElHakim@aw.aca,cn=organizational users,o=aca,cn=cordys,cn=defaultInst,o=appworks-aca.local",
            decision: "approve",
        };
        this.completeStep(data);
      }
    });
  },

  data() {
    return {
      taskId: "",
      taskData: {},
      inputSchema: {},
      app: {},
      model: {},
    };
  },
};
</script>
