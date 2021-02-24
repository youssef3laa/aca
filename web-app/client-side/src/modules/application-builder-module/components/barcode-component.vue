<template>
  <v-row justify="space-around">
    <v-col cols="auto">
      <v-dialog transition="dialog-bottom-transition" max-width="600">
        <template v-slot:activator="{ on, attrs }">
          <v-btn color="primary" v-bind="attrs" v-on="on">Click here</v-btn>
        </template>
        <template v-slot:default="dialog">
          <v-card>
            <v-toolbar style="box-shadow: none">
              <span class="barcode-title">البار كود</span>
              <span class="barcode-close-icon">
                <v-icon @click="dialog.value = false">mdi-close</v-icon>
              </span>
            </v-toolbar>
            <v-card-text>
              <div class="barcode-position"><barcode></barcode></div>
            </v-card-text>
            <v-card-actions class="justify-end">
              <v-btn
                text
                @click="cancelBarcode((dialog.value = false))"
                class="barcode-cancel"
                >إلغاء</v-btn
              >
              <v-btn text @click="printBarcode" class="barcode-print"
                >طباعة</v-btn
              >
              <v-btn text @click="sendBarcode" class="barcode-send"
                >إرسال</v-btn
              >
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
    </v-col>
  </v-row>
</template>

<script>
import barcode from "../../graphs-module/views/barcode";

export default {
  name: "BarcodeComponent",
  data() {
    return {};
  },
  components: {
    barcode,
  },
  methods: {
    sendBarcode() {
      this.$observable.fire("barcode-send", { test: "sendValue" });
    },

    printBarcode() {
      const barcode = document.querySelector(".barcode-position").innerHTML;
      const newWindow = window.open("", "Print-Window");
      // newWindow.document.open();
      newWindow.document.write(
        `<html><head></head><body onload="window.print()">${barcode}</body></html>`
      );
      newWindow.document.close();

      this.$observable.fire("barcode-print", { test: "printValue" });
    },

    cancelBarcode() {
      this.$observable.fire("barcode-cancel", { test: "cancelValue" });
    },
  },
};
</script>

<style scoped>
::v-deep .v-toolbar__content {
  display: block !important;
}

.v-toolbar .barcode-title {
  float: right;
  font: normal 26px/32px Neo Sans Arabic;
  padding: 10px 0;
}

.v-toolbar .barcode-close-icon {
  float: left;
  padding: 10px 0;
}

.barcode-position {
  text-align: center;
}

.justify-end .barcode-cancel {
  margin: 10px;
  color: #c70039;
  background-color: #07689f0d;
}

.justify-end .barcode-print {
  margin: 10px;
  color: #07689f;
  background-color: #07689f0d;
}

.justify-end .barcode-send {
  margin: 10px;
  color: #07689f;
  background-color: #07689f0d;
}
</style>