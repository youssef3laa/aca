<template>
  <v-dialog v-model="dialog" persistent width="600">
    <!-- <template v-slot:activator="{ on, attrs }">
      <v-btn color="primary" dark v-bind="attrs" v-on="on">
        Open Dialog
      </v-btn>
    </template> -->

    <v-card>
      <v-card-title class="headline">Title</v-card-title>
      <span>
        <FormBuilder :forms="field.forms[0]" :model="field.forms[0].model" />
      </span>

      <!-- <v-card-title class="headline">
                    Use Google's location service?
                </v-card-title>
                <v-card-text>Let Google help apps determine location. This means sending anonymous location data to
                    Google, even when no apps are running.</v-card-text>
                <v-card-actions>
                    <v-spacer></v-spacer>
                    <v-btn color="green darken-1" text @click="dialog = false">
                        Disagree
                    </v-btn>
                    <v-btn color="green darken-1" text @click="dialog = false">
                        Agree
                    </v-btn>
                </v-card-actions> -->
    </v-card>
  </v-dialog>
</template>

<script>
// import FormBuilder from '../builders/form-builder'
export default {
  components: {
    FormBuilder: () => import('../builders/form-builder'),

    // FormBuilder,
  },
  data() {
    return {
      dialog: false,
    }
  },
  mounted() {
    console.log(this.field.modalId)
    let modelId = this.field.modalId
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
    console.log(this.field.forms[0])
    console.log(this.val)
  },
  props: ['field', 'val'],
}
</script>
