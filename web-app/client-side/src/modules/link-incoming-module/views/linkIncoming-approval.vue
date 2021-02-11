<template>
    <v-container>
        <span>samo 3aleko</span>
        <AppBuilder ref="appBuilder" :app="app"/>
    </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import historyMixin from "../../history-module/mixin/historyMixin";

export default {
  name: "linkIncoming-approval",
  mixins: [formPageMixin, historyMixin],
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
  },
  methods: {
    fillForm: async function () {
      this.$refs.appBuilder.disableSection("section1")
      let entityName = this.inputSchema.entityName;
      let entityId = this.inputSchema.entityId;

      // get main entityData
      // get requestId

      // sourceLinkIncoming (entityId)
      // targetLinkIncoming (entityId)
      // requestId
      // sourceRequestId
      // targetRequestId


      let linkIncomingEntityData = await this.readEntity(entityName, entityId);
      console.log(linkIncomingEntityData);


      // let workTypeObj = await this.getLookupByCategoryAndKey("workType", entityData.workType);
      // let incomingMeansObj = await this.getLookupByCategoryAndKey("incomingMeans", entityData.incomingMeans);
      //
      // this.$refs.appBuilder.setModelData("form1", {
      //     stepId: this.inputSchema.stepId,
      //     subjectSummary: entityData.summary,
      //     incomingUnit: entityData.incomingUnit,
      //     workType: workTypeObj.arValue,
      //     incomingMeans: incomingMeansObj.arValue,
      //     writingDate: entityData.writingDate.split("Z")[0],
      // });
      //
      // this.$refs.appBuilder.setModelData("form2", {
      //     receiver: entityData
      // });
      //
      // this.$refs.appBuilder.setModelData("memoPage", {
      //     memoComp: {
      //         requestId: this.inputSchema.requestId
      //     }
      // })
      //
      // this.$refs.appBuilder.setModelData("historyTable", {
      //     taskTable: this.createHistoryTableModel(this.inputSchema.process, this.inputSchema.entityId)
      // });

      //
      // this.$refs.appBuilder.setModelData("approvalForm", {
      //     approval: {
      //         decisions: ["approve", "redirect", "reject"],
      //         receiverTypes: ["single"]
      //     }
      // });
    },
    submit: function () {
      let approvalModel = this.$refs.appBuilder.getModelData("ApprovalForm");
      // if (!model._valid){
      //   //@TODO show warning
      //   return;
      // }

      var data = {
        taskId: this.taskId,
        entityId: this.inputSchema.entityId,
        stepId: this.inputSchema.stepId,
        process: this.inputSchema.process,
        parentHistoryId: this.inputSchema.parentHistoryId,

        code: approvalModel.routing.code,
        assignedCN: approvalModel.routing.assignedCN,
        // decision: approvalModel.routing.decision,
        decision: "end",
        comment: approvalModel.routing.comment,
        assignees: approvalModel.routing.assignees,
        receiverType: approvalModel.routing.receiverType
      };
      this.completeStep(data);
    }
  }
};
</script>
