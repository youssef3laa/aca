<template>
  <div style="width: 100%; height: 100%">
    <v-container
      
        v-if="placeHolder && !loading"
        :loading="loading"
        class="attachment-iframe"
        title="Click to grab a file from your PC!"
    >
      <v-row align="center" justify="center">
        <v-col :cols="12" align="center" justify="center">
          <v-icon
              color="#9e9e9e"
              style="margin: 10px 10px 0 0; padding: 5px 10px; font-size: 150px"
          >mdi-file-document
          </v-icon>
        </v-col>
        <v-col :cols="12" align="center" justify="center">
          <span style="margin: 10px 10px 0 0; padding: 5px 10px; color: #c7c8c8"
          >{{ $t("chooseFileToOpenOrShow") }}
          </span>
        </v-col>
      </v-row>
    </v-container>
    <iframe
        v-else
        :src="value.src"
        style="
        border-width: 0;
        padding: 0;
        margin: 0;
        overflow: hidden;
        width: 100%;
        height: 100%;
      "
        @load="iframeLoaded"
    />
  </div>
</template>

<script>
export default {
  name: "iframe-component",
  components: {},
  props: ["val", "field"],
  data() {
    return {
      placeHolder: true,
      loading: false,
      value: this.val,
    };
  },
  watch: {
    val: {
      deep: true,
      handler: function (newVal, oldVal) {
        console.log(oldVal, newVal);
        this.value = newVal;
        this.loading = newVal.src && newVal.src.length > 0;
      },
    },
  },
  methods: {
    iframeLoaded: function () {
      console.log("iframe is loaded");
    },
  },
};
</script>

<style>
.attachment-iframe {
  
  background-color: #f1f2f3;
  border: 2px dashed #f1f2f3 !important;
  border-radius: 6px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}
</style>
