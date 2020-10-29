<template>
  <div id="app">
    <v-app id="inspire" >
      <v-container>
        <router-view/>
      </v-container>
      
    </v-app>
  </div>
</template>

<style lang="scss">
    .theme--light.v-label {
        color: #555555
    }
    .theme--light.v-application {
          background-color: #f2f2f2;
    }
    h1 {
      font-size: 26px;
    }
</style>
<script>

export default {
  components: {
    
  },
}
</script>