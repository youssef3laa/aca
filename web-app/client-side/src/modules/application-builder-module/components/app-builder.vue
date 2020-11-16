<template>
  <v-container>
    <div v-for="(page, key) in app.pages" :key="key">
      <pageBuilder v-on:modelChange="dataChange" :page="page" /></div
  ></v-container>
</template>

<script>
import pageBuilder from './page-builder'
export default {
  name: 'app-builder',
  components: { pageBuilder },
  methods: {
    dataChange: function(model) {
      console.log('App Builder')
      console.log(model)
    },
  },
  
  props: ['app'],

}
</script>
