<template>
  <v-container class="wrapper">
      <div   class="item">
          <span class="item-header">{{$t(field.label)}}</span>
          <span >{{$t(val)}}</span>
      </div>

  </v-container>
</template>

<script>
export default {
props:['val','field']
}
</script>

<style scoped>
.wrapper{
    display: flex;
    justify-content: space-between;
}
.item{
    display: flex;
    flex-direction: column;
}
.item-header{
    color: #9E9E9E;
}
</style>