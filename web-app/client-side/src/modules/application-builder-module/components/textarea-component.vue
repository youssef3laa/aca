<template>
  <!-- background-color="grey lighten-2" -->
  <validation-provider
    :name="field.name"
    :rules="field.rule"
    v-slot="{ errors }"
    :vid="field.name"
  >
    <v-textarea
      row
      color="outline"
      v-model="d"
      @input="addInput"
      :disabled="readonly"
      outlined
    >
      <template #label>
        <span v-t="field.label"></span>
      </template>
    </v-textarea>
    <span class="red--text">{{ errors[0] }}</span>
  </validation-provider>
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
    val: function(newVal) {
      this.d = newVal
      this.readonly = this.field.readonly
    },
  },
}
</script>
