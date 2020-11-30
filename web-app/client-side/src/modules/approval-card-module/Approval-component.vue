<template>
  <div>
    <v-container>
      <v-card flat>
        <v-card-title> ملاحظات أحمد عزت</v-card-title>
        <v-radio-group v-model="ApprovalCardDecision" row>
          <v-radio label="Option 1" value="approve"></v-radio>
          <v-radio label="Option 2" value="reject"></v-radio>
        </v-radio-group>
        <v-container>
          <v-textarea
            row
            background-color="grey lighten-2"
            color="cyan"
            label="Label"
            v-model="comment"
            @change="someHandler"
          ></v-textarea>
        </v-container>
        <ButtonComponent :field="submit" @update="completeTask" />
      </v-card>
    </v-container>
  </div>
</template>

<script>
import ButtonComponent from '../application-builder-module/components/button-component'
import http from '../core-module/services/http'

export default {
  props: ['taskID'],
  components: {
    ButtonComponent,
  },
  name: 'ApprovalCard',
  data() {
    return {
      ApprovalCardDecision: '',
      comment: '',
      submit: {
        type: 'ButtonComponent',
        action: 'submit',
        label: 'submit',
        name: 'submitBtn',
      },
    }
  },
  mounted() {
    console.log(this.taskID)
    console.log(this.comment)
    console.log(this.ApprovalCardDecision)
  },
  methods: {
    someHandler: function() {
      console.log(this.comment)
      console.log(this.ApprovalCardDecision)
    },
    completeTask: function(val) {
      console.log(val)
      if (val.action == 'submit') {
        var output = {
          TaskId: this.taskID,
          NameSpace: 'http://schemas.cordys.com/',
          TaskData: {
            outputSchema: {
              Decision: this.ApprovalCardDecision,
              Comment: this.comment,
            },
          },
        }
        http
          .post('/workflow/complete', output)
          .then(function(response) {
            console.log(response)
          })
          .catch(function(error) {
            console.error(error)
          })
      }
    },
  },
}
</script>
