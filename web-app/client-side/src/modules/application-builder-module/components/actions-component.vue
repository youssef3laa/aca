<template>
  <v-col style="padding-top: 20px; padding-bottom:20px" class="text-left">
    <span v-for="(action, index) in actions" :key="index">
      <span
        v-if="action == 'cancel'"
        @click="cancelStep"
        class="actions-contianer"
      >
        <v-icon color="error">
          far fa-times-circle
        </v-icon>
        {{ cancelText }}
      </span>
      <span v-if="action == 'save'" @click="saveStep" class="actions-contianer">
        <v-icon color="info">
          far fa-save
        </v-icon>
        {{ saveText }}
      </span>
      <span
        v-if="action == 'complete'"
        @click="completeStep"
        class="actions-contianer"
      >
        <v-icon color="info">
          far fa-paper-plane
        </v-icon>
        {{ sendText }}
      </span>
    </span>
  </v-col>
</template>

<script>
export default {
  props: ['actions'],
  data() {
    return {
      cancelText: 'إلغاء',
      saveText: 'حفظ',
      sendText: 'إرسال',
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
      this.$observable.fire('cancel-step')
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
