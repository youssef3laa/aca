<template>
    <v-container>
        <AppBuilder ref="appBuilder" :app="app"/>
    </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import historyMixin from "../../history-module/mixin/historyMixin";
import linkingMixin from "../mixin/linkingMixin";
import router from "../../../router";
import incomingRegistrationMixin from "../../incoming-registration-module/mixins/incomig-registration-mixin";

export default {
  name: "linkIncoming-approval",
  mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, linkingMixin],
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
    this.$observable.subscribe("linkingTable_view", (item) => {
      try {
        console.log(item)
        router.push({
          name: "linkIncoming-parent",
          params: {entityId: this.inputSchema.entityId}
        });
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
    fillForm: async function () {
      this.$refs.appBuilder.disableSection("coresspondenceDataSection");
      let entityName = this.inputSchema.entityName;
      let entityId = this.inputSchema.entityId;

      let linkIncomingEntityData = await this.readEntity(entityName, entityId);

      let incomingRegistration = await this.readIncomingRegistration(linkIncomingEntityData.sourceIncomingId)
      this.$refs.appBuilder.setModelData("mainData", incomingRegistration)

      this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.inputSchema.requestId),})
      this.$refs.appBuilder.setModelData("approvalForm", {approval: this.inputSchema.router})

      let parentEntityData = await this.getRequestData(linkIncomingEntityData.targetRequestId)
      let data = [];
      data.push(parentEntityData);
      let model = {data};
      this.$observable.fire("linkParent", {
        type: "modelUpdate",
        model: model,
      });
    },
    submit: function () {
      let approvalModel = this.$refs.appBuilder.getModelData("approvalForm");
      console.log("approval model", approvalModel);
      // if (!approvalModel._valid) {
      //   //@TODO show warning
      //   console.log("not valid");
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
        receiverType: approvalModel.approval.receiverType
      };
      this.completeStep(data);
    }
  }
};
</script>
