<template>
  <v-card>
    <TabBuilder :form="formData" :section="section" />
  </v-card>
</template>

<script>
import TabBuilder from '../tab-builder'
export default {
  name: 'TabsSection',
  components: {
    TabBuilder,
  },
  props: ['formData', 'section'],
  mounted() {
    console.log(this.section)
  },
}
</script>
