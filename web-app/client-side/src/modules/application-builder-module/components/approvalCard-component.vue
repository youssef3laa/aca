<template>
  <div>
    <v-card flat>
      <v-card-title> {{ field.title }} </v-card-title>
      <v-radio-group v-model="decision" row>
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
          color="outline"
          v-model="comment"
           @change="onValueChange"
          outlined
        >
          <template #label>
            <span v-t="field.commentLabel"></span>
          </template>
        </v-textarea>
      </v-container>
    </v-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      decision: this.val.decision,
      comment: this.val.comment,
    }
  },
  methods: {
    onValueChange: function() {
      this.$emit('update', {
        name: this.field.name,
        value: {
          decision: this.decision,
          comment: this.comment,
        },
      })
    //   if (this.field.publish) {
    //     this.$observable.fire(this.field.publish, this.decision)
    //     console.log(this.decision)
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
