<template>
  <!-- <v-text-field @input="$emit('input', $event.target.value)"> -->
  <validation-provider
      :name="field.name"
      :rules="field.rule"
      v-slot="{ errors }"
      :vid="field.name"
  >
    <v-text-field v-model="d" @input="onValueChange" @change="onChange" :disabled="readonly">
      <template #label>
        <span v-t="field.label"></span>
      </template>
    </v-text-field>
    <span class="red--text">{{ errors[0] }}</span>
  </validation-provider>
</template>

<script>
import {ValidationProvider} from "vee-validate";

export default {
  name: "InputComponent",
  components: {
    ValidationProvider,
  },
  data() {
    return {
      eventName: this.field.eventName,
      d: this.val,
      readonly: this.field.readonly,
    };
  },
  methods: {
    onValueChange: function(){
       if (this.field.publish) {
        this.$observable.fire(this.field.publish, this.d);
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
  props: ["val", "field"],
  watch: {
    val: function(newVal) {
      this.d = newVal
    },
    // d: function (newVal, oldVal) {
    //   console.log(newVal, oldVal);
    // }
  },
};
</script>
