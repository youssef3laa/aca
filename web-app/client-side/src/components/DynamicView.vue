<template>
  <div>
    <!-- <formBuilder /> -->
  <v-app>
    <v-container fluid>
      <v-textarea
            name="input-7-1"
            label="Task Data"
            v-model="taskData"
            filled
            disabled
          ></v-textarea>
      <v-radio-entity
        v-model="decision" row>
        <v-radio
          label="Yes"
          value="yes"
        ></v-radio>
        <v-radio
          label="No"
          value="no"
        ></v-radio>
      </v-radio-entity>
      <v-btn
          elevation="2" v-on:click="complete"
      >Complete</v-btn>
    </v-container>
  </v-app>
  </div>
</template>

<script>
// import formBuilder from './FormGenerator'
export default {
  // Vue.component("AsyncCmp", () => import("./AsyncCmp"));
  props: ['taskInfo'],

  data() {
    return {
      taskData: null,
      taskId: null,
      decision: null
    }
  },
  methods: {
    complete() {
      console.log(this.taskData);
      console.log(this.decision);
      console.log(this.taskId);
    }
  },
  mounted: function () {
    
  },
}
</script>
