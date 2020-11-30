<template>
  <v-container>
    <div v-for="(section, key) in page.sections" :key="key">
      <SectionBuilder v-on:modelChange="dataChange" :section="section" />
    </div>
  </v-container>
</template>

<script>
import SectionBuilder from './section-builder'
export default {
  name: 'PageBuilder',
  components: {
    SectionBuilder,
  },
  props: ['page'],
  methods: {
    dataChange: function(model) {
      console.log('Page Builder')
      console.log(model)
      this.$emit('modelChange', model)
    }
   
  },
  
}
</script>
