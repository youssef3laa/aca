<template>
  <v-container style="padding-top: 0; padding-bottom:0">
    <TabBuilder
      :section="section"
      v-on:modelChange="dataChange"
      v-if="section.tabs"
    />
    <span v-else>
      <span v-if="section.forms">
        <v-card flat v-for="formData in section.forms" :key="formData.id">
          <!-- <v-card-text v-text="formData.form.name"></v-card-text> -->
          <FormBuilder :forms="formData" :model="formData.model" />
        </v-card>
      </span>
      <v-row v-else style="height: 80px;">
        <v-col>
          <h1>
            <button
              style="background:transparent; width: 45px; height: 45px"
              @click="goBack"
            >
              <v-icon style="background-color: white;width: inherit; height: inherit;border-radius: 5px;" right>
                mdi-arrow-right
              </v-icon>
            </button>
            <span style="margin-left:15px;margin-right:15px">{{ section.name }}</span>
          </h1>
        </v-col>
        <v-spacer></v-spacer>
        <v-col style="padding-top: 20px; padding-bottom:0" class="text-left">
          <span v-for="(action, index) in section.actions" :key="index">
            <span v-if="action == 'cancel'" class="actions-contianer">
              <v-icon color="error">
                far fa-times-circle
              </v-icon>
              إلغاء
            </span>
            <span v-if="action == 'save'" class="actions-contianer">
              <v-icon color="info">
                far fa-save
              </v-icon>
              حفظ
            </span>
            <span v-if="action == 'complete'" class="actions-contianer">
              <v-icon color="info">
                far fa-paper-plane
              </v-icon>
              إرسال
            </span>
          </span>
        </v-col>
      </v-row>
    </span>
  </v-container>
</template>

<script>
import TabBuilder from './tab-builder'
import FormBuilder from './form-builder'
export default {
  name: 'SectionBuilder',
  components: {
    TabBuilder,
    FormBuilder,
  },
  methods: {
    dataChange: function(model) {
      console.log('Section Builder')
      console.log(model)
      this.$emit('modelChange', model)
    },
    goBack: function() {
      this.$router.back()
    },
  },
  data() {
    return {}
  },
  props: ['section'],
}
</script>
<style>
.actions-contianer {
  border-radius: 5px;
  background: white;
  margin: 0 20px 0 0;
  padding: 10px 15px;
}
</style>
