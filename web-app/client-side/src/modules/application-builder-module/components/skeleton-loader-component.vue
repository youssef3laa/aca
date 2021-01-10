<template>

  <v-skeleton-loader :class="field.class"
                     :height="field.height"
                     :loading="loading"
                     :max-width="field.maxWidth"
                     :type="field.loaderType"
                     :width="field.width"
                     transition="fade-transition">
  </v-skeleton-loader>

</template>

<script>
export default {
  name: "skeleton-loader-component",
  props: ['field'],

  data() {
    return {
      loading: false,
    }
  },
  created() {

  }
}
</script>

<style scoped>

</style>
