<template>
  <div>
    <v-btn v-on:click="submit" :field="field">{{ field.label }}</v-btn>
  </div>
</template>

<script>
export default {
  name: 'ButtonComponent',
  props: ['val', 'field'],
  methods: {
    submit: function() {
      console.log(this.field)
      //   if (this.field.modalId) {
      //      this.$observable.fire(this.field.publish, {
      //        type: 'ButtonComponent',
      //        action: this.field.action,
      //publish: this.field.publish,
      //        modalId: this.field.modalId,
      //      })
      // this.$emit('openPopUp', {
      //   type: 'ButtonComponent',
      //   action: this.field.action,
      //   publish: this.field.publish,
      //   modalId: this.modalId,
      // })
      //    } else {
      this.$emit('update', {
        type: 'ButtonComponent',
        action: this.field.action,
        publish: this.field.publish,
        modalId: this.field.modalId ? this.field.modalId : '',
      })
      //     }
      // this.$observable.subscribe(this.field.eventName)
    },
  },
}
</script>
