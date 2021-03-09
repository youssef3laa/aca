<template>
  <v-container> 
      <AppBuilder></AppBuilder>
  </v-container>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";
export default {
  components: {
    AppBuilder,
  },
};
</script>

<style scoped></style>
