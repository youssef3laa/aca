<template>
  <v-container>
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
      <v-row v-else>
        <v-col>
          <h1>
            <button
              style="background:white; border-radius:4px; text:center;"
              @click="goBack"
            >
              <v-icon right>
                mdi-arrow-right
              </v-icon>
            </button>
            {{ section.name }}
          </h1>
        </v-col>
        <v-spacer></v-spacer>
        <v-col class="text-left">
          <span v-for="(action, index) in section.actions" :key="index">
            <span v-if="action == 'save'" class="actions-contianer">
              <v-icon>
                mdi-minus-circle
              </v-icon>
              حفظ
              </span
            >
            <span v-if="action == 'complete'" class="actions-contianer">
              <v-icon>
                mdi-checkbox-marked-circle
              </v-icon>
              إرسال
            </span>
            <span v-if="action == 'cancel'" class="actions-contianer">
              <v-icon>
                mdi-cancel
              </v-icon>
              إلغاء
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
.actions-contianer{
  border-radius: 10px;
  background: white;
  margin: 5px;
  padding: 5px;
}
</style>
