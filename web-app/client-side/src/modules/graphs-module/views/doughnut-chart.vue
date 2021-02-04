<script>
import { Doughnut } from "vue-chartjs";

export default {
  extends: Doughnut,
  name: "DoughnutChart",

  props: {
    chartdata: Object,
  },
  mounted() {
    var {chartdata} = this;
    this.renderChart(chartdata, { responsive: true, maintainAspectRatio: false });
  },
  watch: {
    chartdata(newVal) {
      this.renderChart(newVal, {
        responsive: true,
        maintainAspectRatio: false,
      });
    },
  },
};
</script>
