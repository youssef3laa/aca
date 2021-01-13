<script>
import {Pie} from "vue-chartjs";

export default {
  extends: Pie,
  mounted() {
    // Overwriting base render method with actual data.
    this.renderChart(
        {
          labels: ["وارد", "صادر"],
          datasets: [
            {
              borderWidth: 1,
              // borderColor: ["#A52A2A", "#D3D3D3"],
              backgroundColor: ["#A52A2A", "#D3D3D3"],
              data: [1000, 500],
            },
          ],
        },
        {responsive: true, maintainAspectRatio: false}
    );
  },
};
</script>
