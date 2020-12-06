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
      this.$emit('update', {type: 'ButtonComponent', action: this.field.action, publish: this.field.publish})
      // this.$observable.subscribe(this.field.eventName)
    },
  },
}
</script>
