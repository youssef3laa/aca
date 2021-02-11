<template>
    <v-container>
        <AppBuilder ref="appBuilder" :app="app"/>
    </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import historyMixin from "../../history-module/mixin/historyMixin";
import http from "../../core-module/services/http";
import orgChartMixin from "../../../mixins/orgChartMixin";
import userMixin from "../../../mixins/userMixin";

export default {
  name: "generalProcess-member",
  mixins: [formPageMixin, historyMixin, orgChartMixin, userMixin],
  components: {
    AppBuilder,
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
  async created() {
    this.taskId = this.$route.params.taskId;
    this.claimTask(this.taskId);

    this.taskData = await this.getTaskData(this.taskId);
    this.inputSchema = this.taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment;
    this.loadForm(this.inputSchema.config, this.fillForm);

    this.$observable.subscribe("complete-step", this.submit);
    this.$observable.subscribe("searchIncoming", async (data) => {
      await this.getRequestEntities(data);
    });

  },
  methods: {
    async getRequestEntities(data) {
      let requestEntities = await http.get("/request/read/forLinkIncoming", {
        params: {
          process: "generalProcess",
          requestDate: new Date().toISOString().split("T")[0],
          subject: data.model.requestSubject,
          requestNumber: data.model.requestNumber,
        },
      });
      console.log(requestEntities);
      let parentDetails = await this.getParentDetails();
      console.log(parentDetails)
      console.log(this.$user);
      let userDetails = await this.getUserDetails();
      console.log(userDetails);

      this.$observable.fire("link", {
        type: "modelUpdate",
        model: requestEntities.data
      });

      this.$observable.subscribe("linkingTable_view", async (item) => {
        try {
          item = item.item;
          console.log("linkingTable_view", item);
          let obj = {
            "processModel": {
              "process": "linkIncoming",
              "stepId": "init",
              "entityName": "ACA_Entity_linkIncoming",
              "decision": "initiate",
              "assignedCN": parentDetails.cn,
              "extraData": {
                "sourceIncomingId": this.inputSchema.entityId,
                "targetIncomingId": item.entityId,
                "targetRequestId": item.id,
                "sourceRequestId": this.inputSchema.requestId,
                "subject": "طلب ربط وارد: " + item.subject,
                "initiatorId": userDetails.cn
              }
            }
          }
          console.log(obj);
          let response = await http.post("/process/initiateLinkedIncoming", obj);
          
          console.log(response, obj);


        } catch (e) {
          console.log(e);
        }


      })
    },
    fillForm: async function () {
      this.$refs.appBuilder.disableSection("section1")
      let entityName = this.inputSchema.entityName;
      let entityId = this.inputSchema.entityId;
      console.log(this.inputSchema);

      let entityData = await this.readEntity(entityName, entityId);
      let workTypeObj = await this.getLookupByCategoryAndKey("workType", entityData.workType);
      let incomingMeansObj = await this.getLookupByCategoryAndKey("incomingMeans", entityData.incomingMeans);

      this.$refs.appBuilder.setModelData("form1", {
        stepId: this.inputSchema.stepId,
        subjectSummary: entityData.summary,
        incomingUnit: entityData.incomingUnit,
        workType: workTypeObj.arValue,
        incomingMeans: incomingMeansObj.arValue,
        writingDate: entityData.writingDate.split("Z")[0],
      });

      this.$refs.appBuilder.setModelData("form2", {
        receiver: entityData
      });

      this.$refs.appBuilder.setModelData("memoPage", {
        memoComp: {
          requestId: this.inputSchema.requestId
        }
      })

      this.$refs.appBuilder.setModelData("historyTable", {
        taskTable: this.createHistoryTableModel(this.inputSchema.process, this.inputSchema.entityId)
      });

      this.$refs.appBuilder.setModelData("signaturePage", {
        signature: {
          requestId: this.inputSchema.requestId
        }
      });

      this.$refs.appBuilder.setModelData("approvalForm", {
        approval: {
          "fields": ["comment"],
          "receiverTypes": ["single"],
          "direction": "up"
        }
      });
    },
    submit: function () {
      let approvalModel = this.$refs.appBuilder.getModelData("ApprovalForm");
      // if (!model._valid){
      //   //@TODO show warning
      //   return;
      // }

      var data = {
        taskId: this.taskId,
        stepId: this.inputSchema.stepId,
        process: this.inputSchema.process,
        parentHistoryId: this.inputSchema.parentHistoryId,

        code: approvalModel.routing.code,
        assignedCN: approvalModel.routing.assignedCN,
        decision: approvalModel.routing.decision,
        comment: approvalModel.routing.comment,
        assignees: approvalModel.routing.assignees,
        receiverType: approvalModel.routing.receiverType
      };
      this.completeStep(data);
    }
  }
};
</script>
