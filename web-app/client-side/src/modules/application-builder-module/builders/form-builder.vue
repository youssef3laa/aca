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
          <v-expansion-panels v-if="forms.type == 'collapse'">
            <v-expansion-panel>
              <v-expansion-panel-header>
                <v-row no-gutters>
                  <v-col cols="4">
                    <span>{{ forms.name }}</span>
                    <span class="line"></span>
                  </v-col>
                  <v-col cols="8" class="text--secondary"> </v-col>
                </v-row>
                <template v-slot:actions>
                  <!--                  <v-icon color="error">-->
                  <!--                    mdi-arrow-collapse-down-->
                  <!--                  </v-icon>-->
                </template>
              </v-expansion-panel-header>
              <v-expansion-panel-content>
                <component
                  :is="field.type"
                  :field="field"
                  :val="formModel[field.name]"
                  v-on:update="updateText"
                ></component>
              </v-expansion-panel-content>
            </v-expansion-panel>
          </v-expansion-panels>
          <component
            :is="field.type"
            v-else-if="formModel"
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
import { ValidationObserver } from 'vee-validate'
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
import SkeletonLoader from '../components/skeleton-loader-component'
import chartsComponent from '../components/charts-component'
import ShowAttachmentComponent from '../components/show-attachment-component'
import DataTableComponent from '../components/dataTable-component'
import ModalComponent from '../components/modal-component'
import D3GraphComponent from '../components/d3-graph-component'
import SignatureComponent from '../components/signature-component'

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
    SkeletonLoader,
    AttachmentComponent,
    chartsComponent,
    ShowAttachmentComponent,
    DataTableComponent,
    ModalComponent,
    D3GraphComponent,
    SignatureComponent
  },
  data() {
    return {
      formModel: this.model,
      content: '',
    }
  },
  methods: {
    updateText: async function(data) {
      console.log(data)

      if (data.name && data.value) this.forms.model[data.name] = data.value

      if (this.forms.model)
        this.forms.model['_valid'] = !this.$refs['observer']['_data'].flags
          .invalid
      // if (this.forms.key)
      //   this.forms.model['_key'] = this.forms.key;

      console.log(this.$refs['observer'].errors[data.name])
      console.log(this.$refs['observer']['_data'].flags)
      // this.$refs['observer'].validateWithInfo().then((val)=> console.log(val))
      let res = await this.$refs.observer.validate()
      console.log(res)
      if (data.type == 'ButtonComponent' && data.publish) {
        if (data.modalId) {
          console.log(data.modalId)
          this.$observable.fire(data.publish, {
            type: 'ButtonComponent',
            action: data.action,
            publish: data.publish,
            modalId: data.modalId,
          })
        } else {
          this.$observable.fire(data.publish, {
            model: this.forms.model,
            valid: !this.$refs['observer'].flags.invalid,
          })
        }
      } else if (this.forms.publish) {
        this.$observable.fire(this.forms.publish, {
          model: this.forms.model,
          valid: !this.$refs['observer'].flags.invalid,
        })
      }
    },
    saveValue: function() {},
  },
  props: ['forms', 'model'],
  created() {
    var self = this
    if (this.forms.subscribe) {
      console.log('subscribe')
      this.$observable.subscribe(this.forms.subscribe, function(data) {
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
