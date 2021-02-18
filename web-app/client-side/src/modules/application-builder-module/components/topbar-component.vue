<template>
      <div style="width: 100%; display: flex">
      <TasksLists></TasksLists>
      <AdvancedSearch></AdvancedSearch>
      <OrgChartBtn></OrgChartBtn>
    </div>
</template>

<script>
import TasksLists from "../../application-builder-module/components/tasks-list-component";
import AdvancedSearch from "../../application-builder-module/components/advanced-search-component";
import OrgChartBtn from "../../application-builder-module/components/org-chart-btn-component";
export default {
name:"Topbar",
 components: {
    TasksLists,
    AdvancedSearch,
    OrgChartBtn}
}
</script>

<style>

</style>