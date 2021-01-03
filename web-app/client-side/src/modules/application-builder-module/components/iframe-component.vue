<template>

  <iframe :src="value.src"
          style="border-width: 0px; padding: 0px; margin: 0px; overflow: hidden; width:100%; height:500px;"></iframe>
</template>

<script>
export default {
  name: "iframe-component",
  props: ["val", "field"],
  data() {
    return {
      value: this.val
    }
  },
  watch: {
    val: function (newVal, oldVal) {
      console.log(oldVal, newVal)
      this.value = newVal
    }
  }, mounted() {

  }
}
</script>

<style scoped>

</style>
