<template>
  <div>
    <v-card flat>
      <v-card-title> {{ field.title }} </v-card-title>
      <v-radio-group v-model="ApprovalCardDecision" row>
        <v-radio
          v-for="(action, index) in field.actions"
          :key="index"
          :value="action.value"
          :label="action.label"
          @click="onValueChange"
        ></v-radio>
        <!-- <v-radio label="Option 2" value="reject"></v-radio> -->
      </v-radio-group>
      <v-container>
        <v-textarea
          row
          background-color="grey lighten-2"
          color="primary"
          :label="field.commentLabel"
          v-model="comment"
           @change="onValueChange"
        ></v-textarea>
      </v-container>
    </v-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      ApprovalCardDecision: this.val.ApprovalCardDecision,
      comment: this.val.comment,
    }
  },
  methods: {
    onValueChange: function() {
      this.$emit('update', {
        name: this.field.name,
        value: {
          ApprovalCardDecision: this.ApprovalCardDecision,
          comment: this.comment,
        },
      })
    //   if (this.field.publish) {
    //     this.$observable.fire(this.field.publish, this.ApprovalCardDecision)
    //     console.log(this.ApprovalCardDecision)
    //   }
    },
  },
  props: ['field', 'val'],
  watch: {
    val: function(newVal, oldVal) {
      this.value = newVal
      console.log(oldVal)
    },
  },
}
</script>
