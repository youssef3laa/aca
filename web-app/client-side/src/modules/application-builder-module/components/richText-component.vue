<template>
  <vue-editor v-validate="'required'" v-model="content" @text-change="test"></vue-editor>
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
  methods: {
    test: function() {
      console.log('text changed')
    },
  },
  props: ['val', 'field'],
  watch: {
    val: function(newVal, oldVal) {
      console.log(oldVal)
      this.val = newVal
    },
  },
  $_veeValidate: {
    // fetch the current value from the innerValue defined in the component data.
    value() {
      return this.content
    },
  },
}
</script>
