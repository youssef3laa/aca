<template>
  <div>
    <v-container>
      <validation-observer ref="observer">
        <v-row>
          <!-- <div v-for="(field, key) in forms" :key="key"> -->
          <v-col
            v-for="(field, key) in forms.form"
            :key="key"
            :cols="field.col"
            :md="field.col"
          >
            <component
              :field="field"
              v-on:update="updateText"
              :val="formModel[field.name]"
              :is="field.type"
            ></component>
          </v-col>

          <!-- </div> -->
        </v-row>
      </validation-observer>
      <!-- <ButtonComponent v-on="click"/> -->
    </v-container>
  </div>
</template>

<script>
import { ValidationObserver } from 'vee-validate'
import InputComponent from './input-component'
import ButtonComponent from './button-component'
import TableComponent from './table-component'
import RadioGroupComponent from './radioGroup-component'
import TextareaComponent from './textarea-component'
import CheckboxComponent from './checkbox-component'
import RichtextComponent from './richText-component'
import AutoCompleteComponent from './autocomplete-component'

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
  },
  data() {
    return {
      formModel: this.model,
      content: '',
    }
  },
  methods: {
    updateText: function(data) {
      // console.log(data)
      this.forms.model['_valid'] = !this.$refs['observer'].flags.invalid

      if (data.name && data.value) this.forms.model[data.name] = data.value

      if (
        this.forms.publish &&
        data.action &&
        this.forms.event == 'submit' &&
        data.action == 'submit'
      ) {
        this.$observable.fire(this.forms.publish, {
          model: this.forms.model,
          valid: !this.$refs['observer'].flags.invalid,
        })
      } else if (!this.$refs['observer'].flags.invalid) {
        if (this.forms.publish && !this.forms.event) {
          this.$observable.fire(this.forms.publish, this.forms.model)
        }
        // this.$emit('modelChange', this.forms.model)
      }
      // console.log(this.model);
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
