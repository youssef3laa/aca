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
import incomingRegistrationMixin from "../../incoming-registration-module/mixins/incomig-registration-mixin";

export default {
  name: "linkIncoming-approval",
  mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, linkingMixin],
  components: {
    AppBuilder,
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
      this.$refs.appBuilder.disableSection("coresspondenceDataSection")
      this.$refs.appBuilder.setModelData("AttachmentComponent", {attachment: {readonly: true}})
      this.entityInformation.entityId = this.$route.params.entityId;

      this.entityInformation.entityName = "ACA_Entity_linkIncoming";
      let linkIncomingEntityData = await this.readEntity(this.entityInformation.entityName, this.entityInformation.entityId);

      let incomingRegistration = await this.readIncomingRegistration(linkIncomingEntityData.targetIncomingId)
      this.$refs.appBuilder.setModelData("mainData", incomingRegistration)

      this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(linkIncomingEntityData.targetRequestId)})
    }
  }
};
</script>
