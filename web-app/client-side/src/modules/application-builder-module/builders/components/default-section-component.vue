<template>
  <span>
    <!-- <span v-for="(form, index) in formData" :key="index"> -->
      <FormBuilder :forms="formData" :model="formData.model" />
    <!-- </span> -->
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
