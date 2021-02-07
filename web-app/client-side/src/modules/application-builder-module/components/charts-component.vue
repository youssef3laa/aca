<template>
  <v-container>
    <v-card class="mx-auto">
     
      <component
        :chartdata="chartdata"
        :is="field.chartType"
        style="padding: 15px"
      ></component>
    </v-card>
  </v-container>
</template>

<script>
import PieChart from "../../graphs-module/views/pie-chart.vue";
import BarChart from "../../graphs-module/views/bar-chart.vue";
import BubbleChart from "../../graphs-module/views/bubble-chart.vue";
import DoughnutChart from "../../graphs-module/views/doughnut-chart.vue";

import HorizontalBarChart from "../../graphs-module/views/horizontal-bar-chart";
import chartsMixin from "../../../mixins/chartsMixin";

export default {
  name: "Charts",
  props: ["val", "field"],

  mixins: [chartsMixin],
    components: {
    PieChart,
    HorizontalBarChart,
    BarChart,
    DoughnutChart,
    BubbleChart,
  },

  data() {
    return {
      drawCharts: "",
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
      loaded: false,
      
      response: {},
      // d:{
      //   labels: this.val.labels,
      //   datasets: [
      //     {
      //       label: this.field.name,
      //       data: this.val.data,
      //       backgroundColor: this.val.backgroundColor,
      //     },
      //   ],
      // }
    };
  },

  computed: {
    chartdata() {
      return {
        labels: this.val.labels,
        datasets: [
          {
            label: this.field.name,
            data: this.val.data,
            backgroundColor: this.val.backgroundColor,
          },
        ],
      };

    },
  },
//  watch:{
//    val(newVal){
//       this.d={
//         labels: newVal.labels,
//         datasets: [
//           {
//             label: this.field.name,
//             data: newVal.data,
//             backgroundColor: newVal.backgroundColor,
//           },
//         ],
//       }
//    }
//  }

};
</script>