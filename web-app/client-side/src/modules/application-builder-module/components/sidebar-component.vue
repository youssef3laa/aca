<template>
  <div class="sidebar-wrapper">
    <div v-for="(d,key) in data" :key="key" @click="btnClicked(d.action)" class="sidebar-item">
      <span> <span> {{d.name}}</span> {{d.notifications}}</span>
      <div class="icon-wrapper">
     <i :class=d.icon></i>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  props:["val","field"],
  data(){
    return{
        data:[{name:"الوارد",notifications:21, icon:"fas fa-download",action:"viewReceived"},{name:"المرسل",notifications:34, icon:"far fa-paper-plane",action:"viewSent"},
        ],
        selected : null
    };
  },
  methods:{
    btnClicked(action){
      this.selected = action;
      this.$emit('btnClicked',action);
    }
  }
  
};
</script>

<style>
.sidebar-wrapper {
  display: flex;
  flex-direction: column;
  background: white;
  padding-top: 10%;
  height: 100%;
  align-items: flex-end !important;
}
.sidebar-item {
  display: flex;
  align-items: center;
  background-color: rgba(7, 104, 159, 0.05);
  margin: 10px;
  height: 40px;
  width: 85%;
  cursor:pointer;
  padding: 10px;
  justify-content: center;
  border-radius: 5px;
  margin-right:4px ;
  color: #0278ae;
}
.sidebar-item--selected {
  display: flex;
  align-items: center;
  background-color: rgba(62, 66, 68, 0.05);
  margin: 10px;
  height: 40px;
  width: 85%;
  padding: 10px;
  justify-content: center;
  border-radius: 5px;
  margin-right:4px ;
  color: #0278ae;
}


.icon-wrapper {
  display: flex;
  color:white;
  width: 22px;
  height: 22px;
  font-size: 12px;
  justify-content: center;
  align-items: center;
  background: #0278ae;
  border-radius: 5px;
  margin-left: 10px;
}
</style>