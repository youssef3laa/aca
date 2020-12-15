<template>
  <v-container>
    <AppBuilder ref="appBuilders" :app="app"/>
  </v-container>
</template>

<script>
import http from "../../core-module/services/http";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "InitPage",
  components: {
    AppBuilder,
  },
  methods: {},

  created() {
    http
        .get("/user/form/page1")
        .then((response) => {
          this.$refs.appBuilders.setAppData(response.data.data.app);
        })
        .catch((error) => console.error(error));
  },
  mounted() {
    this.$observable.subscribe("initiate", () => {
      console.log(this.formModel);
      if (this.formModel.valid) {
        http
            .post("employee/initiate/", this.formModel.model)
            .then((response) => {
              console.log(response);
              console.log("process initiated");
            })
            .catch((error) => {
              console.error(error);
            });
      }
    });
    this.$observable.subscribe("formChange", (model) => {
      this.formModel = model;
    });
  },
  data() {
    return {
      app: {},
      formModel: {},
    };
  },
};
</script>
