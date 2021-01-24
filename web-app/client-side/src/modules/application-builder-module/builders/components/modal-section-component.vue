<template>
  <v-dialog v-model="dialog" persistent width="600">
    <template v-slot:activator="{ on, attrs }">
      <v-btn color="primary" dark v-bind="attrs" v-on="on">
        Open Dialog
      </v-btn>
    </template>

    <v-card>
      <v-card-title class="headline">
        Use Google's location service?
      </v-card-title>
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
  },
  props: ['formData'],
}
</script>
