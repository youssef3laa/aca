<template>
  <!-- <v-text-field @input="$emit('input', $event.target.value)"> -->
  <validation-provider
    :name="field.name"
    :rules="field.rule"
    v-slot="{ errors }"
    :vid="field.name"
  >
    <v-text-field
      v-model="d"
      @input="onValueChange"
      @change="onChange"
      :type="field.inputType ? field.inputType : 'text'"
      :disabled="readonly"
      outlined
      color="outline"
    >
      <template #label>
        <span v-t="field.label"></span>
      </template>
    </v-text-field>
    <span class="red--text">{{ errors[0] }}</span>
  </validation-provider>
</template>

<script>
import { ValidationProvider } from 'vee-validate'

export default {
  name: 'InputComponent',
  components: {
    ValidationProvider,
  },
  data() {
    return {
      eventName: this.field.eventName,
      d: this.val,
      readonly: null,
      // password: this.field.password,
      show: true,
    }
  },
  methods: {
    onValueChange: function() {
      if (this.field.publish) {
        this.$observable.fire(this.field.publish, this.d)
      }
    },
    onChange: function() {
      this.$emit('update', {
        name: this.field.name,
        value: this.d,
        type: 'inputChange',
      })

      // this.$observable.fire('input', this.d);
    },
  },
  props: ['val', 'field', 'model'],
  watch: {
    model: function(newVal) {
      console.log("inputModel", newVal)
    },
    val: {
      immediate: true,
      handler(newVal) {
        this.d = newVal
        if(this.field.readonly == true || this.field.readonly == false){
          this.readonly = this.field.readonly
        }else if (typeof this.field.readonly == "undefined") {
          this.readonly = this.model[this.field.readonly]
        }
        if (typeof this.field.show != "undefined"  && typeof this.model[this.field.show] != "undefined") {
          this.show = this.model[this.field.show]
        }
      },
    },
    // val: function(newVal) {
    //   this.d = newVal
    //   if (this.field.readonly && this.val.readonly) {
    //     this.readonly = this.val.readonly
    //   }
    // },
    // d: function (newVal, oldVal) {
    //   console.log(newVal, oldVal);
    // }
  },
}
</script>
