<template>
  <!-- <snackBarComponent ref="snackBar">
    <div> -->
  <div>
    <v-alert
      style="postion:fixed"
      elevation="7"
      top
      type="success"
      v-if="isVisible"
      transition="scale-transition"
      >{{ message }}</v-alert
    >
  </div>
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
    }
  },
  methods: {
    _alertSuccess(opts = {}) {
      this.message = opts.message
      this.isVisible = true

      setTimeout(function (){
        this.isVisible = false
      },5000)
    },
  },
}
</script>
<style scoped>
.v-alert {
  position: fixed;
  top: 110px;
  z-index: 999;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 50%;
}
</style>
