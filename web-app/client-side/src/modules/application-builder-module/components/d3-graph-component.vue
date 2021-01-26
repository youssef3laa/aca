<template>
  <v-app>
    <v-container>
      <v-row>
        <v-col id="tree" style="background: white"/>
      </v-row>
    </v-container>
  </v-app>
</template>


<script>
import * as d3 from "d3";
import TreeView from "d3-org-chart";

export default {
  name: "D3GraphComponent",
  data() {
  },
  methods: {
    drawGraph() {
      d3.json(this.val.url)
          .then(data=>{
            new TreeView()
                .container('#tree')
                .data(data)
                .initialZoom(0.6)
                .onNodeClick(d=> console.log(d+' node clicked'))
                .render()
          })
    }
  },
  mounted() {
    this.drawGraph();
  },
  props: ['val', 'field'],
};
</script>
