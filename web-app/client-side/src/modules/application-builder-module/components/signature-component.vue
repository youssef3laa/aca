<template>
  <v-container>
      <signatureDraw :field="field" :readonly="d.readonly"
                     :incomingEntityId="d.incomingEntityId"
                     :viceOrHead="d.viceOrHead"
                     :requestId="d.requestId"
                     :pastSignaturesOnly="d.pastSignaturesOnly"
                     :enterTxt="d.enterTxt"
                     @update="signatureTextUpdated"
      ></signatureDraw>
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
    methods:{
        signatureTextUpdated: function (obj) {
            this.$emit('update', {
                name: this.field.name,
                value: obj.value,
                key:"signatureTxt",
                type: 'inputChange',
            })
        }
    },
  watch: {
    val: function(newVal) {
      this.d = newVal
    }
  },
};
</script>
