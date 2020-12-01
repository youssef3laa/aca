<template>
  <vue-editor v-model="content"></vue-editor>
</template>

<script>

import { VueEditor } from 'vue2-editor'
export default {
  name: 'richtextComponent',
  components: { VueEditor },
  data() {
    return {
      content: this.val,
    }
  },
  props: ['val', 'field'],
  watch: {
    val: function(newVal, oldVal) {
      console.log(oldVal)
      this.d = newVal
    },
  },
}
</script>
