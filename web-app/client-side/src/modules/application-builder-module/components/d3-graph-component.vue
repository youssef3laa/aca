<template>
  <v-app>
    <v-container>
      <v-row>
        <v-col id="graph" style="background: white"/>
      </v-row>
    </v-container>
  </v-app>
</template>


<script>
import TreeView from "d3-org-chart";

export default {
  name: "D3GraphComponent",
  data() {
    return {};
  },
  methods: {
    drawGraph(data) {
      new TreeView()
          .container('#graph')
          .data(data)
          .svgWidth(1200) // Change Size Dynamically
          .initialZoom(0.5).render();
    }
  },
  mounted() {
    this.$observable.subscribe('drawD3Graph', (data) => {
      this.drawGraph(data);
    });
  },
  props: ['val', 'field'],
};
</script>
