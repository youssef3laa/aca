<template>
  <v-card
    v-if="sec.isCard"
    v-bind:style="{ background: sec.background }"
    style="margin-bottom: 10px"
    flat
  >
    <span>
      <component
        :is="sec.type"
        :formData="formData"
        :section="sec"
        v-for="formData in sec.forms"
        :key="formData.id"
      ></component>
    </span>
  </v-card>
  <span v-else>
    <component :is="sec.type" :section="sec"></component>
  </span>
</template>

<script>
import CollapseSection from '../builders/components/collapse-section-component'
import DefaultSection from '../builders/components/default-section-component.vue'
import Resizable from '../builders/components/resizable-section-component'
import TitleComponet from '../builders/components/title-component'
// import TabBuilder from './tab-builder'
// import FormBuilder from './form-builder'
// import { Splitpanes, Pane } from 'splitpanes'
// import 'splitpanes/dist/splitpanes.css'

export default {
  name: 'SectionBuilder',
  components: {
    CollapseSection,
    DefaultSection,
    Resizable,
    TitleComponet,
    // TabBuilder,
    // FormBuilder,
    // Splitpanes,
    // Pane,
  },
  methods: {
    dataChange: function(model) {
      console.log('Section Builder')
      console.log(model)
      this.$emit('modelChange', model)
    },
    // goBack: function() {
    //   this.$router.back()
    // },
    // completeStep: function() {
    //   this.$observable.fire('complete-step')
    // },
    // saveStep: function() {
    //   this.$observable.fire('save-step')
    // },
    // cancelStep: function() {
    //   this.$observable.fire('cancel-step')
    // },
  },
  data() {
    return {
      panel: [0],
      sec: this.section,
    }
  },
  watch: {
    section: function(newVal) {
      // this.val = newVal
      this.sec = newVal
    },
  },
  props: ['section'],
}
</script>
<style>
.actions-contianer {
  border-radius: 5px;
  background: white;
  margin: 0 20px 0 0;
  padding: 10px 15px;
}
.v-expansion-panel__header {
  border: none;
}
.line {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 0.5em;
  border-top: 1px solid black;
  z-index: -1;
}
</style>
