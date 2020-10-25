<template>
  <div id="app">
    <div id="nav">
      <router-link to="/">Home</router-link> |
      <router-link to="/about">About</router-link>
    </div>
    <v-app id="inspire">
      <v-container>
        <router-view/>
      </v-container>
      
    </v-app>
  </div>
</template>

<style lang="scss">
</style>
