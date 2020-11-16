<template>
  <v-container>
    <v-tabs v-model="tab" align-with-title>
      <v-tabs-slider color="yellow"></v-tabs-slider>
      <v-tab v-for="tab in section.tabs" :key="tab.id">{{ tab.name }}</v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab" v-if="section.tabs">
      <v-tab-item v-for="formData in section.forms" :key="formData.id">
        <v-card flat>
          <v-card-text v-text="formData.form.name"></v-card-text>
          <formBuilder :forms="formData.form" :model="formData.model"/>
        </v-card>
      </v-tab-item>
    </v-tabs-items>
 
  </v-container>
</template>

<script>
import formBuilder from './FormBuilder'
export default {
  tab: null,
  name: 'tabBuilder',
  components: {
    formBuilder,
  },
  data() {
    return {
      tab: null,
    }
  },
  props: ['section'],
}
</script>
