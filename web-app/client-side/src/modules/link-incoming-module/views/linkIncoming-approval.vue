<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app" />
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import historyMixin from "../../history-module/mixin/historyMixin";
import linkingMixin from "../mixin/linkingMixin";
// import router from "../../../router";

export default {
  name: "linkIncoming-approval",
  mixins: [formPageMixin, historyMixin, linkingMixin],
  components: {
    AppBuilder,
    // router,
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
    this.$observable.subscribe("linkingTable_view", (item) => {
      try {
        

              console.log(item)
        // router.push({
        //   name: page,
        //   params: { taskId: taskId },
        // });
      } catch (e) {
        console.error(e);
      }
    });
    this.taskId = this.$route.params.taskId;
    this.claimTask(this.taskId);

    this.taskData = await this.getTaskData(this.taskId);
    this.inputSchema = this.taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment;
    this.loadForm(this.inputSchema.config, this.fillForm);

    this.$observable.subscribe("complete-step", this.submit);
  },
  methods: {
    fillForm: async function() {
      this.$refs.appBuilder.disableSection("section1");
      let entityName = this.inputSchema.entityName;
      let entityId = this.inputSchema.entityId;
      console.log(this.inputSchema);
      // get main entityData
      // get requestId

      // sourceLinkIncoming (entityId)
      // targetLinkIncoming (entityId)
      // requestId
      // sourceRequestId
      // targetRequestId

      let linkIncomingEntityData = await this.readEntity(entityName, entityId);
      console.log(linkIncomingEntityData);

      let entityData = await this.readEntity(
        "ACA_Entity_generalProcess",
        linkIncomingEntityData.sourceIncomingId
      );
      let parentEntityData = await this.getRequestData(
        linkIncomingEntityData.targetRequestId
      );
      console.log(parentEntityData);

      let workTypeObj = await this.getLookupByCategoryAndKey(
        "workType",
        entityData.workType
      );
      let incomingMeansObj = await this.getLookupByCategoryAndKey(
        "incomingMeans",
        entityData.incomingMeans
      );
      console.log(entityData);

      console.log(workTypeObj);
      console.log(incomingMeansObj);

      this.$refs.appBuilder.setModelData("form1", {
        stepId: this.inputSchema.stepId,
        subjectSummary: entityData.summary,
        incomingUnit: entityData.incomingUnit,
        workType: workTypeObj.arValue,
        incomingMeans: incomingMeansObj.arValue,
        writingDate: entityData.writingDate.split("Z")[0],
      });

      this.$refs.appBuilder.setModelData("form2", {
        receiver: entityData,
      });

      this.$refs.appBuilder.setModelData("memoPage", {
        memoComp: {
          requestId: linkIncomingEntityData.sourceRequestId,
        },
      });
      //
      this.$refs.appBuilder.setModelData("historyTable", {
        taskTable: this.createHistoryTableModel(
          this.inputSchema.process,
          this.inputSchema.entityId
        ),
      });

      //
      let data = [];
      data.push(parentEntityData);
      let model = { data };
      this.$observable.fire("linkParent", {
        type: "modelUpdate",
        model: model,
      });
      this.$refs.appBuilder.setModelData("approvalForm", {
        approval: this.inputSchema.router,
      });
    },
    submit: function() {
      let approvalModel = this.$refs.appBuilder.getModelData("approvalForm");
      console.log("approval model", approvalModel);
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

        code: approvalModel.approval.code,
        assignedCN: approvalModel.approval.assignedCN,
        decision: approvalModel.approval.decision,
        // decision: "end",
        comment: approvalModel.approval.comment,
        assignees: approvalModel.approval.assignees,
        receiverType: approvalModel.approval.receiverType,
      };
      this.completeStep(data);
    },
  },
};
</script>
