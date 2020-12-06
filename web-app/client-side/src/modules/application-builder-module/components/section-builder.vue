<template>
  <v-container>
    <TabBuilder
      :section="section"
      v-on:modelChange="dataChange"
      v-if="section.tabs"
    />
    <span v-else v-for="formData in section.forms" :key="formData.id">
      <v-card flat>
        <!-- <v-card-text v-text="formData.form.name"></v-card-text> -->
        <FormBuilder :forms="formData" :model="formData.model" />
      </v-card>
    </span>
   
  </v-container>
</template>

<script>
import TabBuilder from './tab-builder'
import FormBuilder from './form-builder'

export default {
  name: 'SectionBuilder',
  components: {
    TabBuilder,
    FormBuilder,
  },
  methods: {
    dataChange: function(model) {
      console.log('Section Builder')
      console.log(model)
      this.$emit('modelChange', model)
    },
  },
  data() {
    return {}
  },
  props: ['section'],
}
</script>
