<template>
  <div>
    <v-btn v-on:click="submit" :field="field">{{field.label}}</v-btn>
  </div>
</template>

<script>
export default {
  name: 'ButtonComponent',
  props: ['val', 'field'],
  methods: {
    submit: function() {
      this.$emit('update',{type: 'buttonClick', action: this.field.action})
      // this.$observable.subscribe(this.field.eventName)
    },
  },
}
</script>
