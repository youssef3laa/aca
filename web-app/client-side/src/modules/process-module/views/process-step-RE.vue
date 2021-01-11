<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"/>
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import historyMixin from "@/modules/history-module/mixin/historyMixin";
import Http from "@/modules/core-module/services/http";

export default {
  name: "process-step-RE",
  mixins: [formPageMixin, historyMixin],
  components: {
    AppBuilder,
  },

  methods: {
    readData: function () {
      console.log("TaskData", this.taskData);
      this.inputSchema = this.taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment;
      //   let page = this.inputSchema.page;

      this.loadForm("process-stepEarly", this.fillForm);
    },
    fillForm: async function () {
      this.$refs.appBuilder.disableSection("section1")
      let entityName = this.inputSchema.entityName;
      let entityId = this.inputSchema.entityId;
      this.readEntity(entityName, entityId)
          .then(async (response) => {
            response = JSON.parse(response.data.data);

            this.$refs.appBuilder.setModelData("form1", {
              stepId: this.inputSchema.stepId,
              notes: response.notes,
              receiver: {
                url: this.inputSchema.roleFilter,
                list: [],
                value: ""
              },
              requestDate: response.requestDate.split("Z")[0],
            });
            this.approvalsHistoryResponse = await this.getHistoryByProcessNameAndEntityId(
                this.inputSchema.process,
                this.inputSchema.entityId
            );

            this.$refs.appBuilder.setModelData("historyTable", {
              taskTable: {
                headers: [
                  {
                    text: "القرار",
                    align: "start",
                    value: "decision",
                  },
                  {
                    text: "الاسم",
                    align: "start",
                    value: "userCN",
                  },
                  {
                    text: "التاريخ",
                    value: "approvalDate",
                  },
                ],
                data: this.approvalsHistoryResponse,
                search: "",
              },
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
      // var model =
      let model = this.$refs.appBuilder.getModelData("form1");
      let approvalModel = this.$refs.appBuilder.getModelData("ApprovalForm");
      console.log("MODEL:", model);
      console.log("APPROVAL MODEL", approvalModel);
      // if (model._valid) {
      var data = {
        taskId: this.taskId,
        entityId: this.inputSchema.entityId,
        stepId: this.inputSchema.stepId,
        process: this.inputSchema.process,
        parentHistoryId: this.inputSchema.parentHistoryId,

        code: model.receiver.value.code,
        assignedCN: model.receiver.value.value,
        decision: approvalModel.approval.decision,
        comment: approvalModel.approval.comment,
      };
      this.completeStep(data);
      // }
    });
    this.$observable.subscribe('open-file-brava', async (fileId) => {
      this.$observable.fire('file-component-skeleton', true)

      console.log(fileId);
      let userToken;
      try {
        userToken = await Http.post("http://appworks-dev:8080/otdsws/rest/authentication/credentials", {
          "userName": "admin",
          "password": "Asset99a",
          "ticketType": "OTDSTICKET"
        });
        this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
            'http://appworks-dev/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
        console.log(userToken);
        this.$observable.fire('file-component-skeleton', false)

      } catch (e) {
        console.log(e);
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
