<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app" />
  </v-container>
</template>

<script>
import http from "../../core-module/services/http";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "initiation-step",
  components: {
    AppBuilder,
  },
  methods: {},

  created() {
    http
      .get("/user/form/process-init")
      .then((response) => {
        this.$refs.appBuilder.setAppData(response.data.data.app);
      })
      .catch((error) => console.error(error));

    this.$observable.subscribe("complete-step", () => {
      console.log("complete-step-clicked");
      
      var model =  this.$refs.appBuilder.getModelData("form1");
      console.log(model);
      if( model._valid){
        http
          .post("/process/initiate", {
            entityName: "ACA_Entity_request",
            entityModel: {
              RequestDate: model.requestDate,
              Receiver: model.receiver.name,
              Notes: model.notes,
            },
            processModel: {
              assignedCN: model.receiver.cn,
              assignedType: model.receiver.type
            },
          })
          .then((response) => {
            console.log(response);
          })
          .catch((error) => console.error(error));
      }

    });
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
