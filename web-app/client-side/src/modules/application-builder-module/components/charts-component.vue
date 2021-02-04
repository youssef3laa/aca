<template>
  <v-container>
    <v-card class="mx-auto">
      <component v-if="loaded" :chartdata="chartdata" :is="field.chartType" style="padding: 15px"></component>
    </v-card>
  </v-container>
</template>

<script>
import PieChart from "../../graphs-module/views/pie-chart.vue";
import BarChart from "../../graphs-module/views/bar-chart.vue";
import BubbleChart from "../../graphs-module/views/bubble-chart.vue";
import chartsMixin from "../../../mixins/chartsMixin";

export default {
  name: "charts",
  mixins: [chartsMixin],
  async mounted() {
   this.loaded = false;
     try {
      this.response = await this.getDynamicReport(this.requestData);
      this.loaded = true
    } catch (e) {
      console.error(e)
    }

  },
  data() {
    return {
      drawCharts: "",
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
      loaded:false,

      response:{},
      
    };
  },

  computed:{
    chartdata(){
      return {
        labels: this.response.labels,
        datasets: [
          {
            label: "richtext",
            data: this.response.data,
            backgroundColor: "rgba(224, 248, 255)",

          },
          ]
          }
      // return  {
      //   labels: [
      //     "January",
      //     "February",
      //     "March",
      //     "April",
      //     "May",
      //     "June",
      //     "July",
      //     "August",
      //     "September",
      //     "October",
      //     "November",
      //     "December"
      //   ],
      //   datasets: [
      //     {
      //       label: "تقييم",
      //       backgroundColor: "green",
      //       data: [40, 20, 18, 39, 17, 40, 39, 80, 40, 20, 12, 11],
      //     },
      //   ],
      // }
    }
  },
  components: {
    PieChart,
    BarChart,
    BubbleChart,
  },
  props: ["val", "field"],
};
</script>