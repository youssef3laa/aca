<template>
  <v-container fluid>
    <v-chip label otlined>{{ this.taskInfo.taskId }}</v-chip>
    <v-textarea
      name="input-7-1"
      label="Task Data"
      v-model="this.taskData"
      filled
      disabled
    ></v-textarea>
    <v-radio-group v-model="decision" row>
      <v-radio label="Yes" value="yes"></v-radio>
      <v-radio label="No" value="no"></v-radio>
    </v-radio-group>
    <v-btn elevation="2" @click="submit">Complete</v-btn>
  </v-container>
</template>

<script>
import http from '../modules/core-module/services/http'

export default {
  name: "TaskView",
  props: {
    taskInfo: {
      required: true,
    },
  },
  data() {
    return {
      taskData: null,
      decision: null,
    };
  },
  methods: {
    submit: function() {
      if (this.decision) {
        var output = {
          TaskId: this.taskInfo.taskId,
          NameSpace: "http://schemas.cordys.com/",
          TaskData: {
            outputSchemaFragement: {
              decision: this.decision,
            },
          }
        };
        http.post('workflow/complete',output).then((response) =>{
            console.log(response);
        }).catch((error)=>{
            console.error(error);
        })
        console.log(output);
      }
    },
  },
  mounted: function () {
    this.taskData = JSON.stringify(this.taskInfo.taskData);
  },
};
</script>
