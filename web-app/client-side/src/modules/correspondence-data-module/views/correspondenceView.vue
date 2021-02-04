<template>
  <v-container class="fill-height">
    <div style="width: 100%;  display: flex">
      <Charts :field="{ name: 'richTextChart', chartType: 'BarChart' }" :val="richtextChart"> </Charts>
      <Charts :field="{ name: 'process', chartType: 'PieChart' }" :val="processHistory"> </Charts>
      <Charts :field="{ name: 'process', chartType: 'PieChart' }" :val="processHistory"> </Charts>

    </div>
    <div style="width: 100%; display: flex">
      <TasksLists></TasksLists>
      <AdvancedSearch></AdvancedSearch>
      <OrgChartBtn></OrgChartBtn>
    </div>
    <Inbox></Inbox>
    <!-- <v-container>
      <AppBuilder ref="appBuilder" :app="app1" />
    </v-container> -->
  </v-container>
</template>

<script>
import correspondenceMixin from "../mixin/correspondenceMixin";
import formPageMixin from "../../../mixins/formPageMixin";
import TasksLists from "../../application-builder-module/components/tasks-list-component";
import AdvancedSearch from "../../application-builder-module/components/advanced-search-component";
import OrgChartBtn from "../../application-builder-module/components/org-chart-btn-component";
import Charts from "../../application-builder-module/components/charts-component";
import Inbox from "../../application-builder-module/components/inbox-component";

import http from "../../core-module/services/http";

export default {
  name: "correspondenceView",
  components: {
    TasksLists,
    AdvancedSearch,
    OrgChartBtn,
    Inbox,
    Charts,
  },
  mixins: [correspondenceMixin, formPageMixin],

  methods: {
    getTasks: function () {
      http.get("workflow/human/tasks").then((response) => {
        console.log(response);
        var data = JSON.parse(response.data.data);
        console.log(data);
        this.$observable.fire("tasks", {
          type: "modelUpdate",
          model: data,
        });
      });
    },
  },

  data() {
    return {
      response: [],
      richtextChart: {
        backgroundColor: ["#22B07D", "#22B07D", "#22B07D"],
        requestData: {
          table: "memoValues",
          aggregations: [
            {
              function: "count",
              column: "jsonKey",
            },
          ],
          columns: ["jsonKey"],
          groupBy: ["jsonKey"],
        },
      },
      processHistory: {
        backgroundColor: ["#22B07D", "#D91828"],
        requestData: {
          table: "ApprovalHistory",
          aggregations: [
            {
              function: "count",
              column: "processName",
            },
          ],
          columns: ["processName"],
          groupBy: ["processName"],
          where: [
            {
              or: [
                {
                  type: "equal",
                  column: "processName",
                  value: "generalProcess",
                },
                {
                  type: "equal",
                  column: "processName",
                  value: "process-1",
                },
              ],
            },
          ],
        },
      },
    };
  },
};
</script>

<style>
</style>