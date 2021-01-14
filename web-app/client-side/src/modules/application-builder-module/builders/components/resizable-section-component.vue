<template>
  <span>
    <splitpanes class="default-theme" dir="ltr">
      <pane
        v-bind:style="{ background: form.background }"
        v-for="(form, index) in formData.resizable.forms"
        :key="index"
      >
        <span dir="rtl">
          <FormBuilder :forms="form" :model="form.model" />
        </span>
      </pane>
    </splitpanes>
  </span>
</template>

<script>
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'
import FormBuilder from '../form-builder'
export default {
  components: {
    Splitpanes,
    Pane,
    FormBuilder,
  },
  props: ['formData'],
}
</script>
