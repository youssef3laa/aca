<template>
  <splitpanes class="default-theme" dir="ltr">
    <pane>
      <AppBuilder dir="rtl" ref="appBuilder" :app="app" />
    </pane>
    <pane max-size="20" size="14">
      <Sidebar @btnClicked="updateView"></Sidebar>
    </pane>
  </splitpanes>
</template>

<script>
import Sidebar from "../../application-builder-module/components/sidebar-component";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import historyMixin from "../../history-module/mixin/historyMixin";

export default {
  mixins: [historyMixin],
  components: {
    Sidebar,
    Splitpanes,
    Pane,
    AppBuilder,
  },
  data() {
    return {
      response: [],

      app: {
        pages: [
          {
            tabs: [
              {
                key : 'tab1',
                id: '1',
                name: "استيفاء",
                icon: "fas fa-copy",
                isActive: true,
              },
              {
                id: '2',
                name: "للتأشير",
                icon: "fas fa-pen-square",
              },
              {
                id: '3',
                name: "لإبداء الرأي",
                icon: "fas fa-comments",
              },
              {
                id: '4',
                name: "تكليفات",
                icon: "fas fa-copy",
              },
              {
                id: '5',
                name: "مراسلات داخلية",
                icon: "fas fa-inbox",
              },
            ],
            sections: [
              {
                tabId: "1",
                isTab: true,
                type: "DefaultSection",
                isCard: "true",
                forms: [
                  {
                    key: "userHistoryTable",
                    inputs: [
                      {
                        type: "DataTableComponent",
                        name: "taskTable",
                        subscribe: "tasks",
                        col: 12,
                      },
                    ],
                    model: {
                      taskTable: {
                        url: "",
                        headers: [],
                        data: [],
                      },
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
    };
  },
  methods: {
    updateView($event) {
      if ($event == "viewSent") {
        this.$refs.appBuilder.setModelData("userHistoryTable", {
          taskTable: this.createSentHistoryTableModel(),
        });
      }
    },
  },
};
</script>

<style>
</style>