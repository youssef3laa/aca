<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"/>
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import Http from "@/modules/core-module/services/http";

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

    this.$observable.subscribe('open-file-brava', async (fileId) => {
      this.$observable.fire('file-component-skeleton', true)

      console.log(fileId);
      let userToken;
      try {
        userToken = await Http.post("http://appworks-dev:8080/otdsws/rest/authentication/credentials", {
          "userName": "admin",
          "password": "Asset99a",
          "ticketType": "OTDSTICKET"
        });
        this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
            'http://appworks-dev/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
        console.log(userToken);
        // this.$observable.fire('file-component-skeleton', false)

      } catch (e) {
        console.log(e);
      }


    });

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
          process: "process-1",
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
