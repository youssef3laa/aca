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
    <!-- <SecrtaryInbox></SecrtaryInbox> -->
    <Topbar :val="{ tasks: taskList }"></Topbar>
  <!-- <OfficeGroudHeadInbox></OfficeGroudHeadInbox> -->
     <Inbox></Inbox>

  </v-container>
</template>

<script>
import chartsMixin from "../../../mixins/chartsMixin";
import Charts from "../../application-builder-module/components/charts-component";
import Inbox from "../../application-builder-module/components/inbox-component";
// import SecrtaryInbox from "../../application-builder-module/components/secretary-inbox-component"
// import OfficeGroudHeadInbox from "../../application-builder-module/components/office-group-head-inbox"
import http from "../../core-module/services/http";
import userMixin from '../../../mixins/userMixin'
// import AppBuilder from "../../application-builder-module/builders/app-builder";
import Topbar from "../../application-builder-module/components/topbar-component";
export default {
  name: "HomePage",
  components: {
  // SecrtaryInbox,
    Inbox,
    // OfficeGroudHeadInbox,
    Charts, Topbar
  },
  mixins: [chartsMixin, userMixin],
  async mounted() {
    try {
      this.barChartData = await this.getDynamicReport(
        this.richtextChart.requestData
      );
      this.pieChartData = await this.getDynamicReport(
        this.processHistory.requestData
      );
      this.loaded = true;
      this.userDetails = await this.getUserDetails();
      console.log(this.userDetails)

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
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
      response: [],
      chartsLoaded: 0,
      barChartData: {},
      pieChartData: {},
      loaded: false,
      userDetails:{},
      // barChartData:{},

      richtextChart: {
        backgroundColor: ["#22B07D", "#22B07D", "#22B07D"],
        requestData: {
          table: "memoValues",
          aggregations: [
            {
              function: "count",
              column: {
                table: "memoValues",
                name: "jsonKey"
              },
            },
          ],
          columns: [{
            table: "memoValues",
            name: "jsonKey"
          }],
          groupBy: [{
            table: "memoValues",
            name: "jsonKey"
          }],
        },
      },
      processHistory: {
        backgroundColor: ["#22B07D", "#D91828"],
        requestData: {
          table: "ApprovalHistory",
          aggregations: [
            {
              function: "count",
              column: {
                table: "ApprovalHistory",
                name: "processName"
              },
            },
          ],
          columns: [{
            table: "ApprovalHistory",
            name: "processName"
          }],
          groupBy: [{
            table: "ApprovalHistory",
            name: "processName"
          }],
          where: [
            {
              or: [
                {
                  type: "equal",
                  column: {
                    table: "ApprovalHistory",
                    name: "processName"
                  },
                  value: "generalProcess",
                },
                {
                  type: "equal",
                  column: {
                    table: "ApprovalHistory",
                    name: "processName"
                  },
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