<script>
import {Pie} from "vue-chartjs";

export default {
  extends: Pie,
  name:"PieChart",

   props: {
    chartdata: Object
  },

  watch:{
    chartdata(newVal){
       this.renderChart(newVal,  { responsive: true, maintainAspectRatio: false })
    }
  }
};
</script>
