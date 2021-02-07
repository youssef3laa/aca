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
    this.$observable.subscribe("complete-step", this.submit);
  },
  methods: {
    submit: function () {
      let model = this.$refs.appBuilder.getModelData("form1");
      let model2 = this.$refs.appBuilder.getModelData("form2");
      if (!model._valid) {
        //@TODO show warining
        return;
      }

      console.log("Model", model)
      console.log("Receiver", model2)
      let obj = {
        generalProcessEntity: {
          writingDate: model.writingDate,
          summary: model.subjectSummary,
          workType: model.workType.value.value,
          incomingMeans: model.incomingMeans.value.value,
          incomingUnit: model.incomingUnit.value.text,
          agency: (model2.receiver.agency)? model2.receiver.agency.name_ar: null,
          sector: (model2.receiver.sector)? model2.receiver.sector.name_ar: null,
          office: (model2.receiver.office)? model2.receiver.office.name_ar: null,
        },
        processModel: {
          process: "generalProcess",
          stepId: "init",
          entityName: "ACA_Entity_generalProcess",
          code: model2.receiver.role.unit.unitTypeCode,
          assignedCN: model2.receiver.assignedCN
        }
      };
      this.initiateProcess(obj);
    },
  }
};
</script>