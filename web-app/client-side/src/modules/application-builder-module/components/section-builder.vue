<template>
  <v-container>
    <tabBuilder
      :section="section"
      v-on:modelChange="dataChange"
      v-if="section.tabs"
    />
  </v-container>
</template>

<script>
import tabBuilder from './tab-builder'
// import formBuilder from './FormBuilder'
export default {
  name: 'section-builder',
  components: {
    tabBuilder,
    // formBuilder,
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
