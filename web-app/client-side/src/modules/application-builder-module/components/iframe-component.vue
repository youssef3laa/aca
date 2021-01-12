<template>
  <div
    style="  
    width:100%;"
  >
    <!-- <SkeletonLoader
      v-if="loading"
      :field="{
        class: 'mx-auto',
        width: '100%',
        height: '500px',
        loaderType: 'image',
      }"
      :loading="loading"
    /> -->
    <v-container
      v-if="!loading"
      :loading="loading"
      class="attachment-iframe"
      title="Click to grap a file from your PC!"
    >
      <!-- <v-row align="center" justify="center">
        <v-col>
        <v-icon color="outline" style="margin: 10px 10px 0 0; padding: 5px 10px"
          >mdi-cloud-upload
        </v-icon>
        </v-col>
      </v-row> -->
      <v-row align="center" justify="center">
        <v-col align="center" justify="center" :cols="12">
          <v-icon
            color="#9e9e9e"
            style="margin: 10px 10px 0 0; padding: 5px 10px; font-size: 150px;"
            >mdi-file-document
          </v-icon></v-col
        >
        <v-col align="center" justify="center" :cols="12">
          <span style="margin: 10px 10px 0 0; padding: 5px 10px; color: #c7c8c8"
            >قم بإختيار ملف لإستعراضه أو عرض بياناته</span
          >
        </v-col>
      </v-row>
    </v-container>
    <iframe
      v-else
      :src="value.src"
      style="border-width: 0; padding: 0; margin: 0; overflow: hidden; width:100%; height:500px;"
      @load="iframeLoaded"
    />
  </div>
</template>

<script>
// import SkeletonLoader from '../components/skeleton-loader-component'

export default {
  name: 'iframe-component',
  components: {
    // SkeletonLoader,
  },
  props: ['val', 'field'],
  data() {
    return {
      loading: false,
      value: this.val,
    }
  },
  watch: {
    val: function(newVal, oldVal) {
      console.log(oldVal, newVal)
      this.value = newVal
    },
  },
  created() {
    this.$observable.subscribe('file-component-skeleton', (bool) => {
      // if (!bool) setTimeout(() => (this.loading = bool), 9000)
      // else this.loading = bool
      this.loading = bool
    })
  },
  methods: {
    iframeLoaded: function() {
      console.log('iframe is loaded')
    },
  },
}
</script>

<style>
.attachment-iframe {
  background-color: #f1f2f3;
  border: 2px dashed #f1f2f3 !important;
  border-radius: 6px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 500px;
}
</style>
