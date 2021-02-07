<script>
import { HorizontalBar } from "vue-chartjs";

export default {
  extends: HorizontalBar,
  name: "HorizontalBarChart",

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
