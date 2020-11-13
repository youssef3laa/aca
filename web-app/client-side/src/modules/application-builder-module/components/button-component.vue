<template>
  <div>
    <v-btn v-on:click="submit" :field="field">{{field.label}}</v-btn>
  </div>
</template>

<script>
export default {
  name: 'buttonComponent',
  props: ['val', 'field'],
  methods: {
    submit: function() {
      this.$emit(this.field.eventName,'null')
      this.$observable.fire(this.field.eventName, this.field);
      // this.$observable.subscribe(this.field.eventName)
    },
  },
}
</script>
