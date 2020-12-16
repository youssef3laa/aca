<template>
  <div>
    <validation-provider
      :name="field.name"
      :rules="field.rule"
      v-slot="{ errors }"
      :vid="field.name"
    >
      <richtextValidateComponent
        :label="field.name"
        v-model="content"
        :error="errors"
        :val="val"
        :field="field"
      ></richtextValidateComponent>
      <span class="red--text">{{ errors[0] }}</span>

      <!-- Errors: {{ errors }} -->
      <!-- <span class="input-entity-addon">
        "Error"
      </span>
      <span v-show="errors.any()" class="text-danger m-t-xs">
        {{ errors.first(field.name) }}
      </span> -->
    </validation-provider>
  </div>
</template>

<script>
import richtextValidateComponent from './rich-text-validate-component'
import { ValidationProvider } from 'vee-validate'

export default {
  name: 'richtextComponent',
  components: { richtextValidateComponent, ValidationProvider },
  data() {
    return {
      content: this.val,
    }
  },
  props: ['val', 'field'],
  watch: {
    val: function(newVal, oldVal) {
      console.log(oldVal)
      this.val = newVal
    },
  },
  $_veeValidate: {
    // value getter
    value() {
      return this.value
    },
    // component name getter
    name() {
      return 'richtextComponent'
    },
  },
}
</script>
