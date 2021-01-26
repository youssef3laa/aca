<script>
import { Pie } from "vue-chartjs";

export default {
  extends: Pie,
  data() {
    return {
      chartdata: {
        labels: ["متقدم", "متأخر"],
        datasets: [
          {
            label:"test",
            backgroundColor: ["#22B07D", "#D91828"],
            data: [1000, 500],
          },
        ],
      },
    };
  },
  mounted() {
    // Overwriting base render method with actual data.
    this.renderChart(this.chartdata, {
      responsive: true,
      maintainAspectRatio: false,
    });
  },
};
</script>
