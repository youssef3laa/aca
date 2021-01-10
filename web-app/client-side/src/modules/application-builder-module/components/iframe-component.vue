<template>
<span>

  <SkeletonLoader v-if="loading"
                  :field="{
                          class:'mx-auto',
                          width:'100%',
                          height:'500px',
                          loaderType:'image'
                          }"
                  :loading="loading"/>

  <iframe
      v-else
      :src="value.src"
      style="border-width: 0; padding: 0; margin: 0; overflow: hidden; width:100%; height:500px;"
      @load="iframeLoaded"/>

</span>

</template>

<script>
import SkeletonLoader from '../components/skeleton-loader-component'

export default {
  name: "iframe-component",
  components: {
    SkeletonLoader
  },
  props: ["val", "field"],
  data() {
    return {
      loading: false,
      value: this.val
    }
  },
  watch: {
    val: function (newVal, oldVal) {
      console.log(oldVal, newVal)
      this.value = newVal
    }
  }, created() {
    this.$observable.subscribe('file-component-skeleton', (bool) => {
      if (!bool) setTimeout(() => this.loading = bool, 9000);
      else this.loading = bool
    })
  }, methods: {
    iframeLoaded: function () {

      console.log("iframe is loaded");
    }
  }
}
</script>

<style scoped>

</style>
