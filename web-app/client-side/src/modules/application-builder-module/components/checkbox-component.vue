<template>
  <v-container class="px-0" fluid>
    <v-checkbox
      v-for="(dec, key) in ApprovalCardDecision"
      :key="key"
      :value="dec.value"
      v-model="decision"
      :label="labelTranslate(decision.label)"
      @click="test"
    ></v-checkbox>
  </v-container>
</template>

<script>
export default {
  name: 'checkboxComponent',
  props: ['val', 'field'],
  watch: {
    val: function(newVal, oldVal) {
      console.log(oldVal)
      this.decision = newVal
    },
  },

  methods: {
    labelTranslate(label){
      return this.$t(label)
    },
    test: function() {
      // if(this.field.publish){

      // }

      console.log(this.decision)
      this.$emit('update', {
        name: this.field.name,
        value: this.decision,
        type: 'inputChange',
      })
      console.log(this.decision)
    },
  },
  data() {
    return {
      ApprovalCardDecision: this.field.decisions,
      decision: "",
    }
  },
}
</script>
