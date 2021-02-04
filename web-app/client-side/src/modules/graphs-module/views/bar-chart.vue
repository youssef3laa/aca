<script>
import {Bar} from "vue-chartjs";

export default {
  extends: Bar,
  name:"BarChart",

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
