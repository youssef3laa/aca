<template>
  <span>
    <FormBuilder :forms="formData" :model="formData.model" />
  </span>
</template>

<script>
import FormBuilder from '../form-builder'
export default {
  components: {
    FormBuilder,
  },
  props: ['formData'],
  mounted() {
      console.log(this.formData)
  },
}
</script>
