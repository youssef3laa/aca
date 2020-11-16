<template>
  <v-container>
    <tabBuilder :section="section" v-if="section.tabs" />
    
  </v-container>
</template>

<script>
import tabBuilder from './DaynamicTabBuilder'
// import formBuilder from './FormBuilder'
export default {
  name: 'sectionBuilder',
  components: {
    tabBuilder,
    // formBuilder,
  },
  data() {
    return {}
  },
  props: ['section'],
}
</script>
