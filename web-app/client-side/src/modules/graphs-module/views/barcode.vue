<template>
  <div>
    <VueBarcode v-bind:value="barcodeValue" height="50" width="3" tag="svg">
      Show this if the rendering fails.
    </VueBarcode>
  </div>
</template>

<script>
import VueBarcode from "vue-barcode";
export default {
  data() {
    return {
      barcodeValue: "123456789",
    };
  },
  components: {
    VueBarcode,
  },

  created() {
    this.$observable.subscribe("barcode-send", (object) => {
      console.log(object);
    });

    this.$observable.subscribe("barcode-print", (object) => {
      console.log(object);
    });

    this.$observable.subscribe("barcode-cancel", (object) => {
      console.log(object);
    });
  },
};
</script>

<style>
</style>
