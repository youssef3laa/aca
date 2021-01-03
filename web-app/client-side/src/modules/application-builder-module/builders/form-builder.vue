<template>
  <v-container>
    <validation-observer ref="observer">
      <v-row>
        <!-- <div v-for="(field, key) in forms" :key="key"> -->
        <v-col
            v-for="(field, key) in forms.inputs"
            :key="key"
            :cols="field.col"
            :md="field.col"
        >
          <component
              :is="field.type"
              v-if="formModel"
              :field="field"
              :val="formModel[field.name]"
              v-on:update="updateText"
          ></component>

          <component
              :is="field.type"
              v-else
              :field="field"
              v-on:update="updateText"
          ></component>
        </v-col>

        <!-- </div> -->
      </v-row>
    </validation-observer>
    <!-- <ButtonComponent v-on="click"/> -->
  </v-container>
</template>

<script>
import {ValidationObserver} from 'vee-validate'
import InputComponent from '../components/input-component'
import ButtonComponent from '../components/button-component'
import TableComponent from '../components/table-component'
import RadioGroupComponent from '../components/radio-group-component'
import TextareaComponent from '../components/textarea-component'
import CheckboxComponent from '../components/checkbox-component'
import RichtextComponent from '../components/rich-text-component'
import AutoCompleteComponent from '../components/autocomplete-component'
import InputFileComponent from '../components/input-file-component'
import SelectComponent from '../components/select-component'
import DatePickerComponent from '../components/datePicker-component'
import ApprovalComponent from '../components/approvalCard-component'
import MemoComponent from '../components/memo-component'
import AttachmentComponent from '../components/attachment-component'
import IframeComponent from '../components/iframe-component'

export default {
  name: 'FormBuilder',
  components: {
    InputComponent,
    ValidationObserver,
    ButtonComponent,
    TableComponent,
    RadioGroupComponent,
    TextareaComponent,
    CheckboxComponent,
    RichtextComponent,
    AutoCompleteComponent,
    InputFileComponent,
    SelectComponent,
    DatePickerComponent,
    ApprovalComponent,
    MemoComponent,
    IframeComponent,
    AttachmentComponent
  },
  data() {
    return {
      formModel: this.model,
      content: '',
    }
  },
  methods: {
    updateText: async function (data) {
      console.log(data)

      if (data.name && data.value) this.forms.model[data.name] = data.value

      if (this.forms.model)
        this.forms.model['_valid'] = !this.$refs['observer']['_data'].flags
            .invalid

      console.log(this.$refs['observer'].errors[data.name])
      console.log(this.$refs['observer']['_data'].flags)
      // this.$refs['observer'].validateWithInfo().then((val)=> console.log(val))
      let res = await this.$refs.observer.validate()
      console.log(res)

      if (data.type == 'ButtonComponent' && data.publish) {
        this.$observable.fire(data.publish, {
          model: this.forms.model,
          valid: !this.$refs['observer'].flags.invalid,
        })
      } else if (this.forms.publish) {
        this.$observable.fire(this.forms.publish, {
          model: this.forms.model,
          valid: !this.$refs['observer'].flags.invalid,
        })
      }
    },
    saveValue: function () {
    },
  },
  props: ['forms', 'model'],
  created() {
    var self = this
    if (this.forms.subscribe) {
      console.log('subscribe')
      this.$observable.subscribe(this.forms.subscribe, function (data) {
        if (data.type == 'modelUpdate') {
          var keys = Object.keys(data.model)
          keys.forEach((key, index) => {
            console.log(index)
            self.formModel[key] = data.model[key]
          })
          self.formModel.serial_num = data.model.serial_num
        }
        console.log(data)
      })
    }
  },
}
</script>

<style></style>
