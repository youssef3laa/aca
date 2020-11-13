<template>
  <div>
    <v-container>
      <validation-observer ref="observer">
        <v-row>
          <!-- <div v-for="(field, key) in forms" :key="key"> -->
          <v-col
            v-for="(field, key) in forms"
            :key="key"
            :cols="field.col"
            :md="field.col"
          >
            <component
              :field="field"
              :val="model[field.name]"
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
  model: {},
  components: {
    inputComponent,
    ValidationObserver,
    buttonComponent,
  },
  methods: {
    updateText: function(data) {
      // console.log(data)
      this.model[data.name] = data.value
      console.log(this.$refs['observer'])
      if(!this.$refs['observer'].flags.invalid) {
        this.$emit("modelChange", this.model)
      }
      // console.log(this.model);
    },
    saveValue: function() {},
  },
  props: ['forms', 'model'],
  // data() {
  //   return {
  //     model: {
  //       x: 'null',
  //       y: 'test',
  //     },
  //     forms: [
  //       {
  //         id: 'x',
  //         type: 'inputText',
  //         label: 'First Name',
  //         name: 'input',
  //         col: 4,
  //         rule: 'required|minmax:2,25',
  //         //  readonly : true
  //       },
  //       {
  //         id: 'y',
  //         type: 'inputText',
  //         label: 'First Name',
  //         name: 'input',
  //         method: 'updateText',
  //         col: 4,
  //         rule: 'required|minmax:2,25',
  //       },
  //       {
  //         type: 'inputText',
  //         label: 'First Name',
  //         name: 'input',
  //         method: 'updateText',
  //         col: 6,
  //         rule: 'required|minmax:2,25',
  //       },
  //       {
  //         type: 'inputText',
  //         label: 'First Name',
  //         name: 'input',
  //         method: 'updateText',
  //         col: 6,
  //         rule: 'required|minmax:2,25',
  //       },
  //     ],
  //   }
  // },
}
</script>

<style></style>
