<script>
import { Bar } from "vue-chartjs";

export default {
  extends: Bar,
  data() {
    return {
      chartdata: {
        labels: [
          "تكليفات رئيس الهيئة",
          "تحريات",
          "احتياجات رئاسة الجمهورية",
          "شكاوى",
          "احتياجات النيابة",
        ],
        datasets: [
          {
            label: "تقييم",
            backgroundColor: "purple",
            data: [30, 40, 100, 120, 2000],
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
