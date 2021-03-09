<template>
  <AppBuilder ref="appBuilder" :app="app" />
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";
import formPageMixin from "../../../mixins/formPageMixin";

export default {
  mixins: [formPageMixin],
  components: { AppBuilder },
  props: ["val", "field"],
  mounted() {
    this.loadForm("audit-page", false, "appBuilder");
  },
  data() {
    return {
      app: {},
    };
  },
  methods: {},
};
</script>

<style></style>
