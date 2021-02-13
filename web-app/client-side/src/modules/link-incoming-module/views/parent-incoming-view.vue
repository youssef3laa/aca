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
      entityInformation: {},
      taskData: {},
      inputSchema: {},
      app: {},
      model: {},
    };
  },
  mounted() {
    this.loadForm("parent-incoming-view", this.fillForm);

  },
  methods: {
    fillForm: async function () {
      this.$refs.appBuilder.disableSection("section1");
      this.entityInformation.entityId = this.$route.params.entityId;
      this.entityInformation.entityName = "ACA_Entity_linkIncoming";
      let linkIncomingEntityData = await this.readEntity(this.entityInformation.entityName, this.entityInformation.entityId);
      console.log(linkIncomingEntityData);
      let entityData = await this.readEntity(
          "ACA_Entity_generalProcess",
          linkIncomingEntityData.targetIncomingId
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

      this.$refs.appBuilder.setModelData("historyTable", {
        taskTable: this.createHistoryTableModel(
            "generalProcess",
            linkIncomingEntityData.targetIncomingId
        ),
      });
    }
  }
};
</script>
