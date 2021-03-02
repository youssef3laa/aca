<template>
  <span>
    <!-- <span v-for="(form, index) in formData" :key="index"> -->
      <FormBuilder ref="formBuild" :forms="formData" :model="formData.model" />
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
  validateModel: function(key){
    if(this.formData.key == key)
      return this.$refs.formBuild.validateModel(key);
  }
}
</script>
