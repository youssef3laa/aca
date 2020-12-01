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
  props: ["item"],
  components: {
    AppBuilder,
  },
  methods: {},
  mounted() {
  },
  created() {
    //get page config

    http
        .get("/user/form/page1")
        .then((response) => {
          console.log(response);
          this.$refs.appBuilders.setAppData(response.data.data.app);
          console.log(this.app)
        })
        .catch((error) => console.error(error));
  },
  data() {
    return {
      app: {},
    };
  },
};
</script>
