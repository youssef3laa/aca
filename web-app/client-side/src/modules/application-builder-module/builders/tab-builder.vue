<template>
  <!-- <v-container> -->
  <span>
    <v-tabs v-model="tab" align-with-title>
      <v-tabs-slider color="outline"></v-tabs-slider>
      <v-tab v-for="tab in section.tabs" :key="tab.id">
        <v-icon right color="outline">
          mdi-account
        </v-icon>
        {{ tab.name }}
        </v-tab >
    </v-tabs>
    <v-tabs-items v-model="tab" v-if="section.tabs">
      <v-tab-item v-for="formData in section.forms" :key="formData.id">
        <v-card flat>
          <!-- <v-card-text v-text="formData.inputs.name"></v-card-text> -->
          <FormBuilder :forms="formData" :model="formData.model" />
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </span>

  <!-- </v-container> -->
</template>

<style>
.v-tabs-bar.v-tabs-bar--is-mobile .v-tab {
    margin: 0px !important;
}

 .v-application--is-rtl .v-tabs--align-with-title > .v-tabs-bar:not(.v-tabs-bar--show-arrows):not(.v-slide-entity--is-overflowing) > .v-slide-group__wrapper > .v-tabs-bar__content > .v-tabs-slider-wrapper + .v-tab {
   margin: 0px !important;
 }
</style>

<script>
import FormBuilder from './form-builder'

export default {
  tab: null,
  name: 'TabBuilder',
  components: {
    FormBuilder,
  },
  data() {
    return {
      tab: null,
    }
  },
  props: ['section'],
}
</script>
