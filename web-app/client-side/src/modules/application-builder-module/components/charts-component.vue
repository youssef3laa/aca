<template>
  <v-container>
    <v-card class="mx-auto">
      <component
        :is="field.chartType"
        style="padding:15px;"
      ></component>
    </v-card>
  </v-container>
</template>

<script>
import PieChart from "../../graphs-module/views/pie-chart.vue";
import BarChart from "../../graphs-module/views/bar-chart.vue";
import BubbleChart from "../../graphs-module/views/bubble-chart.vue";

export default {
  name: "charts",
  data() {
    return {
      drawCharts: "",
    };
  },
  components: {
    PieChart,
    BarChart,
    BubbleChart,
  },
  props: ["val", "field"],
};
</script>