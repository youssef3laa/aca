<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"/>
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "generalProcess-init",
  mixins: [formPageMixin],
  components: {
    AppBuilder,
  },
  data() {
    return {
      app: {},
      model: {},
    };
  },
  async created() {
    await this.loadForm("generalProcess-init");
    this.initiateBrava();
    this.$observable.subscribe("complete-step", this.submit);
  },
  methods: {
    submit: function () {
      let model = this.$refs.appBuilder.getModelData("form1");
      if (!model._valid) {
        //@TODO show warining
        return;
      }

      let obj = {
        generalProcessEntity: {
          writingDate: model.writingDate,
          summary: model.subjectSummary,
          workType: model.workType.value.value,
          incomingMeans: model.incomingMeans.value.value
        },
        processModel: {
          process: "generalProcess",
          stepId: "init",
          entityName: "ACA_Entity_generalProcess",
          code: model.receiver.value.code,
          assignedCN: model.receiver.value.value
        }
      };
      this.initiateProcess(obj);
    },
  }
};
</script>