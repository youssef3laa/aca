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
  components: {
    AppBuilder,
  },
  mixins: [formPageMixin],
  async created() {
    await this.loadForm("process-init");

    this.$observable.subscribe("complete-step", () => {
      this.completeStep();
    });

    this.initiateBrava();
  },
  methods: {
    completeStep: function () {
      let model = this.$refs.appBuilder.getModelData("form1");
      if (!model._valid) {
        //@TODO show warining
        return;
      }

      let obj = {
        requestEntity: {
          requestDate: model.requestDate,
          receiver: model.receiver.value.text,
          notes: model.notes,
        },
        processModel: {
          process: "generalProcess",
          stepId: "init",
          entityName: "ACA_Entity_request",
          code: model.receiver.value.code,
          assignedCN: model.receiver.value.value
        }
      };
      this.initiateProcess(obj);
    },
  },
  mounted() {
  },
  data() {
    return {
      app: {},
      model: {},
    };
  },
};
</script>
