<template>
  <v-dialog v-model="dialog" :width="modalWidth" eager persistent>
    <v-card>
      <v-row>
        <v-col :cols="10">
          <v-card-title class="headline"
            >{{ $t(form.model.modalTitle) }}
          </v-card-title>
        </v-col>
        <v-col :cols="2">
          <span
            style="text-align: left; padding: 25px 30px; float: left;"
            @click="dialog = false"
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
          {{ $t('cancel') }}
        </v-btn>
        <span v-for="(action, index) in form.model.action" :key="index">
          <v-btn
            class="modalAddButton"
            @click="submitModal(action)"
            style="margin: 10px;"
          >
            <span style="color:#ecf3f7;">{{ $t(action) }}</span>
          </v-btn>
        </span>
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
      form: this.formData,
      dialog: this.dialogState,
      modalWidth: this.width,
      modalAction: 'add',
    }
  },
  computed: {
    isActive: function() {
      return !this.formData.model._valid
    },
  },
  methods: {
    submitModal: function(actionName) {
      if (this.formData.model._valid) {
        this.$observable.fire(this.formData.modalId + '_' + actionName, {
          obj: this.formData.model,
        })
        // if (this.modalAction == 'edit') {
        //   this.$observable.fire(this.formData.modalId + '_updateModal', {
        //     obj: this.formData.model,
        //   })
        // } else if (this.modalAction == 'add') {
        //   this.$observable.fire(this.formData.modalId + '_addModal', {
        //     obj: this.formData.model,
        //   })
        // }


        this.dialog = false
      }
    },
  },
  mounted() {
    console.log(this.formData)
    console.log(this.formData.modalId)
    this.$observable.context = this
    this.$observable.subscribe(this.formData.modalId, (modalObj) => {
      if (modalObj && modalObj.action) this.modalAction = modalObj.action
      this.dialog = !this.dialog
    })
    // console.log(this.formData.forms[0])
    // console.log(this.val)
  },
  watch: {
    formData: function(newVal) {
      // this.val = newVal
      this.form = newVal
    },
  },
  props: {
    formData: Object,
    width: {
      default: 1200,
    },
    dialogState: {
      default: false,
    },
  },
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
