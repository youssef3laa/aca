<template>
  <splitpanes class="default-theme" dir="ltr">
    <pane>
      <AppBuilder dir="rtl" ref="appBuilder" :app="app" />
    </pane>
    <pane size="14">
      <Sidebar></Sidebar>
    </pane>
  </splitpanes>
</template>

<script>
import Sidebar from "../../application-builder-module/components/sidebar-component";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
export default {
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
                id: 1,
                name: "استيفاء",
                icon: "fas fa-copy",
                isActive: true,
              },
              {
                id: 2,
                name: "للتأشير",
                icon: "fas fa-pen-square",
              },
              {
                id: 3,
                name: "لإبداء الرأي",
                icon: "fas fa-comments",
              },
              {
                id: 4,
                name: "تكليفات",
                icon: "fas fa-copy",
              },
              {
                id: 5,
                name: "مراسلات داخلية",
                icon: "fas fa-inbox",
              },
            ],
            sections: [
              {
                tabId: "1",
                isTab: "true",
                type: "DefaultSection",
                isCard: "true",
                forms: [
                  {
                    inputs: [
                      {
                        type: "TableComponent",
                        name: "taskTable",
                        subscribe: "tasks",
                        col: 12,
                      },
                    ],
                    model: {
                      taskTable: {
                        headers: [
                          {
                            text: "",
                            value: "actions",
                            sortable: false,
                          },
                          {
                            text: "التاريخ",
                            align: "start",
                            filterable: false,
                            value: "",
                          },
                          {
                            text: "رقم الوارد",
                            value: "",
                          },
                          {
                            text: "الجهة",
                            value: "",
                          },
                          {
                            text: "المرسل",
                            value: "",
                          },
                          {
                            text: "عنوان الموضوع",
                            value: "",
                          },
                        ],
                        data: [],
                        search: "",
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
};
</script>

<style>
</style>