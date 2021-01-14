<template>
  <span>
    <v-row style="height: 80px;">
      <v-col>
        <h1>
          <button
            style="background:transparent; width: 45px; height: 45px"
            @click="goBack"
          >
            <v-icon
              style="background-color: white;width: inherit; height: inherit;border-radius: 5px;"
              right
            >
              mdi-arrow-right
            </v-icon>
          </button>
          <span style="margin-left:15px;margin-right:15px">{{
            section.name
          }}</span>
        </h1>
      </v-col>
      <v-spacer></v-spacer>
      <v-col style="padding-top: 20px; padding-bottom:0" class="text-left">
        <span v-for="(action, index) in section.actions" :key="index">
          <span
            v-if="action == 'cancel'"
            @click="cancelStep"
            class="actions-contianer"
          >
            <v-icon color="error">
              far fa-times-circle
            </v-icon>
            إلغاء
          </span>
          <span
            v-if="action == 'save'"
            @click="saveStep"
            class="actions-contianer"
          >
            <v-icon color="info">
              far fa-save
            </v-icon>
            حفظ
          </span>
          <span
            v-if="action == 'complete'"
            @click="completeStep"
            class="actions-contianer"
          >
            <v-icon color="info">
              far fa-paper-plane
            </v-icon>
            إرسال
          </span>
        </span>
      </v-col>
    </v-row>
  </span>
</template>

<script>
export default {
  props: ['section'],
  methods: {
    goBack: function() {
      this.$router.back()
    },
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
  mounted() {
    console.log(this.section)
  },
}
</script>
