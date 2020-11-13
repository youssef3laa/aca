<template>
  <v-container>
    <div v-for="(section, key) in page.sections" :key="key">
      <sectionBuilder v-on:modelChange="dataChange" :section="section" />
    </div>
  </v-container>
</template>

<script>
import sectionBuilder from './section-builder'
export default {
  name: 'page-builder',
  components: {
    sectionBuilder,
  },
  props: ['page'],
  methods: {
    dataChange: function(model) {
      console.log('Page Builder')
      console.log(model)
      this.$emit('modelChange', model)
    },
  },
}
</script>
