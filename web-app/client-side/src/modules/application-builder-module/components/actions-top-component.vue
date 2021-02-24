<template>
   <v-row style="margin-bottom:8px">
        <div v-for="(item,index) in val" :key="index">
        <div @click="triggerAction(item.publish)" class="decision-btn">
          <i :class="item.icon"></i>
          <span class="decision-btn-title">{{$t(item.title)}}</span>
        </div>
        </div>
   </v-row>
</template>

<script>
export default {
props:["val","field"],
methods:{
    triggerAction(publish){
        this.$observable.fire(publish);
    }
}
}
</script>

<style scoped>
.decision-btn {
  display: flex;
  align-items: center;
  background-color: rgba(7, 104, 159, 0.05);
  margin: 10px;
  height: 40px;
  padding: 10px;
  justify-content: center;
  border-radius: 5px;
  color: #0278ae;
  cursor:pointer;
}
.decision-btn i {
  margin-left: 10px;
}
.decision-btn-title {
  white-space: nowrap;
  overflow: hidden;
}
</style>