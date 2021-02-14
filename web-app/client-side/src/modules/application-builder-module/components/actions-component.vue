<template>
  <v-col style="padding-top: 20px; padding-bottom:20px" class="text-left">
    <span v-for="(action, index) in actions" :key="index">
      <v-btn v-if="action == 'cancel'"
             :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
             @click="cancelStep"
             color="white" elevation="0" height="52px">
                      <v-icon color="error">far fa-times-circle</v-icon>
                      <span style="margin: 3px"></span>
                      <span style="font-weight: bold" v-t="cancelText"></span>
                  </v-btn>
        <v-btn v-if="action == 'save'"
               :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
               @click="saveStep"
               color="white" elevation="0" height="52px">
                    <v-icon color="info">far fa-save</v-icon>
                    <span style="margin: 3px"></span>
                    <span style="font-weight: bold" v-t="saveText"></span>
                </v-btn>
        <v-btn v-if="action == 'complete'"
               :style="{marginLeft: (index == actions.length-1)? '0px':'5px', marginRight: (index == 0)? '0px':'5px'}"
               @click="completeStep"
               color="white" elevation="0" height="52px">
                    <v-icon color="info">far fa-paper-plane</v-icon>
                    <span style="margin: 3px"></span>
                    <span style="font-weight: bold" v-t="sendText"></span>
                </v-btn>
    </span>
  </v-col>
</template>

<script>
import router from "../../../router";

export default {
  props: ['actions'],
  data() {
    return {
      cancelText: 'cancel',
      saveText: 'save',
      sendText: 'send',
    }
  },
  methods: {
    completeStep: function() {
      this.$observable.fire('complete-step')
    },
    saveStep: function() {
      this.$observable.fire('save-step')
    },
    cancelStep: function() {
        router.push({name: 'HomePage'})
    },
  },
}
</script>

<style scoped>
.actions-contianer {
  margin: 15px 15px 15px 0;
  padding: 5px;
  background: #ffffff;
  border: 1px solid #f8f8f8;
  border-radius: 4px;
}
</style>
