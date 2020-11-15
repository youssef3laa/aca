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
              :val="formModel[field.name]"
              @[field.eventName]="updateText"
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
import inputComponent from './input-component'
import buttonComponent from './button-component'

export default {
  name: 'form-builder',
  components: {
    inputComponent,
    ValidationObserver,
    buttonComponent,
  },
  data() {
    return {
      formModel: this.model,
    }
  },
  methods: {
    updateText: function(data) {
      // console.log(data)
      this.forms.model[data.name] = data.value
      console.log(this.$refs['observer'])
      if (!this.$refs['observer'].flags.invalid) {
        if (this.forms.publish) {
          this.$observable.fire(this.forms.publish, this.forms.model)
        }
        this.$emit('modelChange', this.forms.model)
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
            console.log(index);
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
