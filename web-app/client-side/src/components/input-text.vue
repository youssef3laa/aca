<template>
  <!-- <v-text-field @input="$emit('input', $event.target.value)"> -->
  <validation-provider
    :name="field.name"
    :rules="field.rule"
    v-slot="{ errors }"
  >
    <v-text-field v-model="d" @input="addInput">
      <template #label>
        <span v-t="labels"></span>
      </template>
    </v-text-field>
    <span class="red--text">{{ errors[0] }}</span>
  </validation-provider>
</template>

<script>
import { ValidationProvider } from 'vee-validate'

export default {
  name: 'inputText',
  components: {
    ValidationProvider,
  },
  data() {
    return {
      eventName: this.field.eventName,
      d: this.val,
    }
  },
  methods: {
    addInput: function() {
      this.$emit(this.eventName, {
        id: this.field.id,
        value: this.d,
        method: 'saveValue',
      })
    },
  },
  props: ['labels', 'val', 'field'],
}
</script>
