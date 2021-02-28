<template>
  <v-container>
    <validation-observer :ref="forms.key">
      <v-row>
        <!-- <div v-for="(field, key) in forms" :key="key"> -->
        <!--v-if="field.show && field.show != false"-->
        <v-col
          v-for="(field, i) in showField"
          :key="i"
          :cols="field.col"
          :md="field.col"
        >
          <component
            v-if="formModel"
            :is="field.type"
            :field="field"
            :val="formModel[field.name]"
            :model="formModel"
            v-on:update="updateText"
          ></component>

          <component
            v-else
            :is="field.type"
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
import DynamicApprovalComponent from '../components/approvalCard-dynamic-component'
import ReceiverFormComponent from '../components/receiver-form-component'
import DynamicReceiverFormComponent from '../components/receiver-form-dynamic-component'
import VersionGridComponent from '../components/version-grid-component'
import ProcessStatusControl from '../components/process-status-control'
import SaveProcessComponent from '../components/save-process-component'
import SecretaryViewSignaturesComponent from '../components/secretary-view-signatures-component'
import InboxComponent from '../components/inbox-component'
import ImageComponent from "../components/image-component"
import ActionsTopComponent from "../components/actions-top-component"
import BarcodeComponent from "../components/barcode-component"
import TextComponent from "../components/text-component"
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
    SignatureComponent,
    DynamicApprovalComponent,
    ReceiverFormComponent,
    DynamicReceiverFormComponent,
    VersionGridComponent,
    ProcessStatusControl,
    SaveProcessComponent,
    SecretaryViewSignaturesComponent,
    InboxComponent,
    ImageComponent,
    ActionsTopComponent,
    BarcodeComponent,
    TextComponent,
  },
  data() {
    return {
      formModel: this.model,
      content: '',
      test: null,
      panel: [0],
    }
  },
  methods: {
    updateText: async function(data) {
      // console.log(data)

        if (data.name && (data.value!==undefined)) {
            if (data.key) {
                this.forms.model[data.name][data.key] = data.value;
            } else this.forms.model[data.name] = data.value
        }

      if (this.forms.model)
        this.forms.model['_valid'] = !this.$refs[this.forms.key]['_data'].flags
          .invalid
      // if (this.forms.key)
      //   this.forms.model['_key'] = this.forms.key;

      // console.log(this.$refs[this.forms.key].errors[data.name])
      // console.log(this.$refs[this.forms.key]['_data'].flags)
      // this.$refs[this.forms.key].validateWithInfo().then((val)=> console.log(val))
      // let res =
      //         await this.$refs.observer.validate()
      // console.log(res)
      if (data.type == 'ButtonComponent' && data.publish) {
        if (data.modalId) {
          // console.log(data.modalId)
          this.$observable.fire(data.publish, {
            type: 'ButtonComponent',
            action: data.action,
            publish: data.publish,
            modalId: data.modalId,
          })
        } else {
          this.$observable.fire(data.publish, {
            model: this.forms.model,
            valid: !this.$refs[this.forms.key].flags.invalid,
          })
        }
      } else if (this.forms.publish) {
        this.$observable.fire(this.forms.publish, {
          model: this.forms.model,
          valid: !this.$refs[this.forms.key].flags.invalid,
        })
      }
    },
    saveValue: function() {},
  },
  props: ['forms', 'model'],
  watch: {
    model: function(newVal) {
      this.formModel = newVal
    },
  },
  created() {
    console.log(this.forms)
    // console.log(this.model)
    // var self = this
    // if (this.forms.subscribe) {
    //   // console.log('subscribe')
    //   this.$observable.subscribe(this.forms.subscribe, function(data) {
    //     if (data.type == 'modelUpdate') {
    //       var keys = Object.keys(data.model)
    //       // keys.forEach((key, index) => {
    //         // console.log(index)
    //       keys.forEach((key) => {
    //         self.formModel[key] = data.model[key]
    //       })
    //       self.formModel.serial_num = data.model.serial_num
    //     }
    //     // console.log(data)
    //   })
    // }
    // this.$observable.subscribe("validateModel", async (key)=>{
    //   if(this.$refs[key])
    //     this.forms.model['_valid'] =  await this.$refs[key].validate()
    // })
  },
  computed: {
    showField: function() {
      // let inputs = []
      // for(let i = 0; i<this.forms.length; i++){
      //   inputs.push(this.forms[i].inputs)
      // }
      return this.forms.inputs.filter(
        (i) => (i.show && i.show != false) || i.show == undefined
      )
    },
  },
}
</script>

<style></style>
