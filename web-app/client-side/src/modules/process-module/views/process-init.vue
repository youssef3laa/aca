<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app" />
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "initiation-step",
  components: {
    AppBuilder,
  },
  mixins: [formPageMixin],
  async created() {
    await this.loadForm("process-init");

    this.$observable.subscribe("complete-step", () => {
      this.completeStep();
    });
  },
  methods: {
    completeStep: function () {
      console.log("complete-step-clicked");
      var model = this.$refs.appBuilder.getModelData("form1");
      console.log(model);
      if (model._valid) {
        var obj = {
          entityName: "ACA_Entity_request",
          entityModel: {
            RequestDate: model.requestDate,
            Receiver: model.receiver.name,
            Notes: model.notes,
          },
          processModel: {
            assignedCN: model.receiver.cn,
            assignedType: model.receiver.type,
          },
        };
        this.initiateProcess(obj);
      }
    },
  },
  mounted() {},
  data() {
    return {
      app: {},
      model: {},
    };
  },
};
</script>
