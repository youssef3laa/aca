<template>
  <v-container>
    <AppBuilder ref="appBuilders" :app="app"/>
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
          this.$refs.appBuilders.setAppData(response.data.data.app);
        })
        .catch((error) => console.error(error));

    this.$observable.subscribe("form1Change", (model) => {
      console.log("form1Change", model);
    })

  },
  mounted() {

  },
  data() {
    return {
      app: {},
      model: {}
    };
  },
};
</script>
