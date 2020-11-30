<template>
  <v-textarea
    row
    background-color="grey lighten-2"
    color="cyan"
    label="Label"
    v-model="d"
    @input="addInput"
  ></v-textarea>
</template>
<script>
export default {
  name: 'textareaComponent',
  data() {
    return {
      eventName: this.field.eventName,
      d: this.val,
      readonly: this.field.readonly,
    }
  },
  methods: {
    addInput: function() {
      this.$emit('update', {
        name: this.field.name,
        value: this.d,
        type: 'inputChange',
      })

      // this.$observable.fire('input', this.d);
    },
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
