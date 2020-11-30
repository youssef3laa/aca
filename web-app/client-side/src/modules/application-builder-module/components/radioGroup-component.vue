<template>
  <v-radio-group v-model="decision" row>
    <v-radio
      v-for="(decision, key) in ApprovalCardDecision"
      :key="key"
      :value="decision.value"
      :label="decision.label"
      @click="test"
    >
    </v-radio>
  </v-radio-group>
</template>

<script>
export default {
  name: 'radioGroupComponent',
  props: ['val', 'field'],
  watch: {
    val: function(newVal, oldVal) {
      console.log(oldVal)
      this.decision = newVal
    },
  },
  methods: {
    test: function() {
      // if(this.field.publish){

      // }

      console.log(this.decision)
      this.$emit('update', {
        name: this.field.name,
        value: this.decision,
        type: 'inputChange',
      })
    },
  },
  data() {
    return {
      name: 'ahmedEzzat',
      ApprovalCardDecision: this.field.decisions,
      decision: '',
    }
  },
}
</script>
