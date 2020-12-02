<template>
  <v-container>
    <AppBuilder ref="appBuilders" :app="app"/>
  </v-container>
</template>

<script>
import http from "../../core-module/services/http";
import AppBuilder from "../../application-builder-module/components/app-builder";

export default {
  name: "InitPage",
  components: {
    AppBuilder,
  },
  methods: {},

  created() {
    //get page config

    http
        .get("/user/form/page1")
        .then((response) => {
          console.log(response);
          this.$refs.appBuilders.setAppData(response.data.data.app);
          console.log(this.$refs.appBuilders);
        })
        .catch((error) => console.error(error));
  },
  mounted() {
    this.$observable.subscribe("initiate", (model) => {
      console.log(model);
    });
    this.$observable.subscribe("formChange", (model) => {
      console.log(model);

    });
  },
  data() {
    return {
      app: {},
    };
  },
};
</script>
