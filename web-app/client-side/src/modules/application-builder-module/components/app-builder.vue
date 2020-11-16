<template>
  <v-container>
    <div v-for="(page, key) in app.pages" :key="key">
      <PageBuilder v-on:modelChange="dataChange" :page="page" /></div
  ></v-container>
</template>

<script>
import PageBuilder from './page-builder'
export default {
  name: 'AppBuilder',
  components: { PageBuilder },
  methods: {
    dataChange: function(model) {
      console.log('App Builder')
      console.log(model)
    },
  },
  
  props: ['app'],
}
</script>
