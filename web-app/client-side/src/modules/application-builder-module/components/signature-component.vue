<template>
  <v-container>
      <signatureDraw :field="field" :readonly="field.readonly" :incomingEntityId="d.incomingEntityId" :viceOrHead="d.viceOrHead"></signatureDraw>
  </v-container>
</template>

<script>
import signatureDraw from "../../graphs-module/views/signature-draw.vue";

export default {
  name: 'SignatureComponent',
  props: ['val', 'field'],
  data() {
    return {
      d: this.val
    };
  },
  components: {
    signatureDraw,
  },
  watch: {
    val: function(newVal) {
      this.d = newVal
    }
  },
};
</script>
