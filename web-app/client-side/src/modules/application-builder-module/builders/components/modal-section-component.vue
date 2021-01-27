<template>
  <v-dialog v-model="dialog" persistent width="1200">
    <v-card>
      <v-row>
        <v-col :cols="10">
          <v-card-title class="headline">{{
            formData.modalTitle
          }}</v-card-title>
        </v-col>
        <v-col :cols="2">
          <span
            @click="dialog = false"
            style="text-align: left; padding: 25px 30px; float: left;"
            ><v-icon> fas fa-times</v-icon></span
          >
        </v-col>
      </v-row>
      <span>
        <FormBuilder :forms="formData" :model="formData.model" />
      </span>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn
          style="color:#07689f;height: 36px !important;background-color: transparent;"
          @click="dialog = false"
        >
          إلغاء
        </v-btn>
        <v-btn class="modalAddButton" @click="submitModal">
          <span style="color:#ecf3f7">إضافة</span>
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script>
// import FormBuilder from '../builders/form-builder'
import FormBuilder from '../form-builder'
export default {
  components: {
    FormBuilder,
  },
  data() {
    return {
      dialog: false,
    }
  },
  methods: {
    submitModal: function() {
      console.log(this.formData.model)
      console.log(this.formData)
      if (this.modalAction == 'edit') {
        this.$observable.fire(this.formData.modalId + '_updateModal', {
          obj: this.formData.model,
        })
      } else if (this.modalAction == 'add') {
        this.$observable.fire(this.formData.modalId + '_addModal', {
          obj: this.formData.model,
        })
      }
      this.dialog = false
    },
  },
  mounted() {
    console.log(this.formData)
    console.log(this.formData.modalId)
    let modelId = this.formData.modalId
    this.$observable.context = this
    this.$observable.subscribe('openModal', (modalObj) => {
      console.log(this.dialog)
      console.log(modalObj.action)
      this.modalAction = modalObj.action
      // console.log(modalObj.valid)
      // this.dialog = modalObj.valid
      if (modelId == modalObj.modalId) {
        this.dialog = true
        console.log(this.dialog)
      }
    })
    // console.log(this.formData.forms[0])
    // console.log(this.val)
  },
  props: ['formData'],
}
</script>
<style>
.modalAddButton {
  background: #07689f !important;
  height: 36px !important;
  width: 80px;
}
.v-dialog {
  overflow-y: inherit !important;
}
</style>
