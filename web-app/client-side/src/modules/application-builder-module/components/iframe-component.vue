<template>

  <iframe id="brava-viewer-custom-ui-97755008995162080" allowfullscreen="true"
          data-options="{&quot;nodeType&quot;:&quot;iframe&quot;,&quot;path&quot;:&quot;http://appworks-dev/img/brava/HTML/viewer&quot;,&quot;requestHeaders&quot;:{&quot;OTCSTicket&quot;:&quot;zn+0Dj9fbGMIScbmlV0d8vOnF21jHaNnxSODMiS381Vg5BxGwAO/AHoT5XEpnqORSRGqwgBI0XpXrNHcuP1g1Vv0jgbR5UC+&quot;},&quot;container&quot;:{},&quot;clientType&quot;:&quot;2D&quot;,&quot;height&quot;:&quot;100%&quot;,&quot;width&quot;:&quot;100%&quot;,&quot;signedConfigUrl&quot;:&quot;/otcs/cs.exe/api/v1/brava/view/438580?_&amp;format=xmlraw&amp;clienttype=2D&amp;resturl=%2Fotcs%2Fcs.exe%2Fapi%2Fv1&quot;,&quot;xmlPostType&quot;:&quot;text/xml&quot;,&quot;trustedTargetOrigin&quot;:&quot;http://appworks-dev&quot;,&quot;trustedClientOrigins&quot;:[&quot;http://appworks-dev&quot;],&quot;suppressErrorScreen&quot;:true,&quot;useSimpleScroll&quot;:true,&quot;locale&quot;:&quot;en&quot;}"
          height="100%"
          src="http://appworks-dev/img/brava/HTML/viewer/lib/client.html?logLevel=error&amp;locale=en&amp;clientPath=http%3A%2F%2Fappworks-dev%2Fimg%2Fbrava%2FHTML%2Fviewer%2Flib%2F&amp;trustedTargetOrigin=http%3A%2F%2Fappworks-dev#brava-viewer-custom-ui-97755008995162080"
          style="border-width: 0px; padding: 0px; margin: 0px; overflow: hidden;" width="100%"></iframe>
</template>

<script>
export default {
  name: "iframe-component",
  props: ["val", "field"],
  data() {
    return {
      value: this.val
    }
  },
  watch: {
    val: function (newVal, oldVal) {
      console.log(oldVal, newVal)
      this.value = newVal
    }
  }
}
</script>

<style scoped>

</style>
