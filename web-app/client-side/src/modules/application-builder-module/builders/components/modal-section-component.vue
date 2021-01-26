<template>
  <v-dialog v-model="dialog" persistent width="1200">
    <v-card>
      <v-card-title class="headline">Title</v-card-title>
      <span>
        <FormBuilder :forms="formData" :model="formData.model" />
      </span>
      <v-card-actions>
        <v-spacer></v-spacer>
        <v-btn color="green darken-1" text @click="dialog = false">
          Disagree
        </v-btn>
        <v-btn color="green darken-1" text @click="dialog = false">
          Agree
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
  mounted() {
    console.log(this.formData)
    console.log(this.formData.modalId)
    let modelId = this.formData.modalId
    this.$observable.context = this
    this.$observable.subscribe('openModal', (modalObj) => {
      console.log(this.dialog)
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
