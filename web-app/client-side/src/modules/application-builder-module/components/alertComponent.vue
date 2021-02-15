<template>
  <!-- <snackBarComponent ref="snackBar">
    <div> -->
    <v-alert
      elevation="7"
      top
      min-width="30%"
      :type=type
      :value="isVisible"
      transition="scale-transition"
      >{{ message }}</v-alert
    >

  <!-- </div>
  </snackBarComponent> -->
</template>

<script>
// import snackBarComponent from '../components/snackBarsComponent'
export default {
  name: 'AlertComponent',
  components: {
    // snackBarComponent,
  },
  data() {
    return {
      isVisible: false,
      message: null,
      type: "success"
    }
  },
  methods: {
    _alertSuccess(opts = {}) {
      if(opts.type) this.type = opts.type
      else this.type = "success"
      this.message = this.$t(opts.message)
      this.isVisible = true
      setTimeout(() => {
        this.isVisible = false
      }, 2000)
      // this.isVisible = false
    },
  },
}
</script>
<style scoped>
.v-alert {
  position: fixed;
  top: 85px;
  z-index: 999;
  left: 35%;
  /*transform: translate(-50%, -50%);*/
  /*width: 30%;*/
}
</style>
