<template>
      <div class="top-bar-wrapper">
      <TasksLists :val="val.tasks"></TasksLists>
      <TopBarBtn :val="{backgroundColor:'#22B07D'}" :field="{label:'advancedSearch',icon:'fas fa-search'}"></TopBarBtn>
      <TopBarBtn :val="{backgroundColor:'#F37121'}" :field="{label:'orgChart',icon:'fas fa-sitemap'}"></TopBarBtn>

    </div>
</template>

<script>
import TasksLists from "../../application-builder-module/components/tasks-list-component";
import TopBarBtn from "../../application-builder-module/components/topbar-btn-component";
export default {
name:"Topbar",
props:["val","field"],
 components: {
    TasksLists,
    TopBarBtn,
   },

}
</script>

<style>
.top-bar-wrapper{
width: 100%;
 display: flex;
}
</style>