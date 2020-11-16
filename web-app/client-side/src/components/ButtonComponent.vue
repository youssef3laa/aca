<template>
  <div>
    <v-btn v-on:click="submit" :field="field">Submit</v-btn>
  </div>
</template>

<script>
export default {
  name: 'buttonComponent',
  props: ['labels', 'val', 'field'],
  methods: {
    submit: function() {
      this.$emit(this.field.eventName,null)
    },
  },
}
</script>
