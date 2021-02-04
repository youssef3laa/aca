<template>
  <v-container>
    <v-card class="mx-auto">
      <button @click="changeVal">click me</button>
      <component
        v-if="loaded"
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
import chartsMixin from "../../../mixins/chartsMixin";

export default {
  name: "Charts",
  props: ["val", "field"],

  mixins: [chartsMixin],
    components: {
    PieChart,
    BarChart,
    BubbleChart,
  },
  async mounted() {
    this.loaded = false;
    console.log(this.val);
    try {
      this.response = await this.getDynamicReport(this.val.requestData);
      this.loaded = true;
    } catch (e) {
      console.error(e);
    }
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
    };
  },

  computed: {
    chartdata() {
      return {
        labels: this.response.labels,
        datasets: [
          {
            label: this.field.name,
            data: this.response.data,
            backgroundColor: this.val.backgroundColor,
          },
        ],
      };

    },
  },
  watch: {
    val: {
      immediate: true,
      async handler() {
        try {
          this.response = await this.getDynamicReport(this.requestData);
          this.loaded = true;
          console.log(this.response);
          console.log(this.chartdata);
        } catch (e) {
          console.error(e);
        }
      },
    },
  },

  methods: {
    async changeVal() {
      try {
        this.response = await this.getDynamicReport(this.requestData);
        this.loaded = true;
      } catch (e) {
        console.error(e);
      }
    },
  },
};
</script>