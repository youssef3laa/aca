<template>
  <v-container class="fill-height">
    <v-row>
      <v-col :cols="4">
        <Charts
          v-if="loaded"
          :field="{ name: 'richTextChart', chartType: 'BarChart' }"
          :val="{
            data: barChartData.data,
            labels: barChartData.labels,
            backgroundColor: richtextChart.backgroundColor,
          }"
        >
        </Charts>
      </v-col>
      <v-col :cols="4">
        <Charts
          v-if="loaded"
          :field="{ name: 'process', chartType: 'PieChart' }"
          :val="{
            data: pieChartData.data,
            labels: pieChartData.labels,
            backgroundColor: processHistory.backgroundColor,
          }"
        >
        </Charts>
      </v-col>
      <v-col :cols="4">
        <Charts
          v-if="loaded"
          :field="{ name: 'process', chartType: 'DoughnutChart' }"
          :val="{
            data: pieChartData.data,
            labels: pieChartData.labels,
            backgroundColor: processHistory.backgroundColor,
          }"
        >
        </Charts>
      </v-col>
    </v-row>
    <Topbar></Topbar>
    <Inbox></Inbox>

  </v-container>
</template>

<script>
import chartsMixin from "../../../mixins/chartsMixin";
import Topbar from "../../application-builder-module/components/topbar-component";
import Charts from "../../application-builder-module/components/charts-component";
import Inbox from "../../application-builder-module/components/inbox-component";

import http from "../../core-module/services/http";

export default {
  name: "HomePage",
  components: {
Topbar,
    Inbox,
    Charts,
  },
  mixins: [chartsMixin],
  async mounted() {
    try {
      this.barChartData = await this.getDynamicReport(
        this.richtextChart.requestData
      );
      this.pieChartData = await this.getDynamicReport(
        this.processHistory.requestData
      );
      this.loaded = true;
      // setTimeout(async ()=>{
      // this.barChartData = await this.getDynamicReport(this.processHistory.requestData);

      // },2000)
    } catch (e) {
      console.error(e);
    }
  },
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
      chartsLoaded: 0,
      barChartData: {},
      pieChartData: {},
      loaded: false,
      // barChartData:{},

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