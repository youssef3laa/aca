<template>
  <v-container>
      <signatureDraw></signatureDraw>
  </v-container>
</template>

<script>
import signatureDraw from "../../graphs-module/views/signature-draw.vue";

export default {
  name: 'signatureComponent',
  data() {
    return {
    };
  },
  components: {
    signatureDraw,
  },
};
</script>