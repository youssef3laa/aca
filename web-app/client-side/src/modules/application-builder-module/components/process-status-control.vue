<template>

  <v-row
      align="center"
      justify="space-around"
      style="background: lightgray"
  >
    <v-btn color="error" depressed>
      Pause
    </v-btn>
    <v-btn color="success" depressed>
      Resume
    </v-btn>
  </v-row>
</template>

<script>
export default {
  name: "process-status-control",
  data() {
    return {
      processInstanceId: ""
    }
  },
  methods: {
    resumeProcess: function () {

    },
    pauseProcess: function () {

    }
  }
}
</script>

<style scoped>

</style>
