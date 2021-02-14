<template>
  <splitpanes style="height: auto" class="default-theme" dir="ltr">
    <pane>
      <AppBuilder dir="rtl" ref="appBuilder" :app="app" />
    </pane>
    <pane style="height: auto" max-size="20" size="14">
      <Sidebar @btnClicked="updateView"></Sidebar>
    </pane>
  </splitpanes>
</template>

<script>
import http from "../../core-module/services/http";
import Sidebar from "../../application-builder-module/components/sidebar-component";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import historyMixin from "../../history-module/mixin/historyMixin";
import router from "../../../router";
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
      sidebarItem: "viewReceived",
      app: {
        pages: [
          {
            tabs: [
              {
                key: "tab1",
                id: "1",
                name: "استيفاء",
                icon: "fas fa-copy",
                isActive: true,
              },
              {
                id: "2",
                name: "للتأشير",
                icon: "fas fa-pen-square",
              },
              {
                id: "3",
                name: "لإبداء الرأي",
                icon: "fas fa-comments",
              },
              {
                id: "4",
                name: "تكليفات",
                icon: "fas fa-copy",
              },
              {
                id: "5",
                name: "مراسلات داخلية",
                icon: "fas fa-inbox",
              },
            ],
            sections: [
              {
                key: "section 1",
                tabId: 1,
                isTab: true,
                type: "DefaultSection",
                display: "block",
                isCard: true,
                forms: [
                  {
                    key: "userHistoryTable",
                    inputs: [
                      {
                        type: "DataTableComponent",
                        name: "taskTable",
                        actions: ["view"],
                        subscribe: "tasks",
                        col: 12,
                      },
                    ],
                    model: {
                      taskTable: {
                        headers: [
                          {
                            text: "Task",
                            align: "start",
                            filterable: false,
                            value: "Activity",
                          },
                          {
                            text: "Sender Name",
                            value: "Sender.displayName",
                          },
                          {
                            text: "Process Name",
                            value:
                              "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.process",
                          },
                          {
                            text: "Date",
                            value: "DeliveryDate",
                          },
                          {
                            text: "",
                            value: "action",
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
  methods: {
    viewTask(item) {
      console.log("SideBarItem", this.sidebarItem);
      if (this.sidebarItem == "viewReceived") {
        console.log("item obj in table", item);
        try {
          let taskId = item.item.TaskId,
            page =
              item.item.TaskData.ApplicationData
                .ACA_ProcessRouting_InputSchemaFragment.component;
          router.push({
            name: page,
            params: { taskId: taskId },
          });
        } catch (e) {
          console.error(e);
        }
      }
    },
    getTasks: function() {
      http.get('workflow/human/tasks').then((response) => {
        console.log(response)
        let data = JSON.parse(response.data.data)
        console.log(data)
        for(let key in data.data){
          data.data[key].DeliveryDate = new Date(data.data[key].DeliveryDate).toLocaleString()
        }
        this.$observable.fire("tasks", {
          type: "modelUpdate",
          model: data,
        });
      });
    },
    createTasks() {
      this.$refs.appBuilder.setModelData("userHistoryTable", {
        taskTable: {
          url: null,
          headers: [
            {
              text: "الموضوع",
              align: "start",
              filterable: false,
              value:
                "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.subject",
            },
            {
              text: "المنشئ",
              value: "Sender.displayName",
            },
            {
              text: "تاريخ الارسال",
              value: "DeliveryDate",
            },
            {
              text: "رقم الطلب",
              value:
                "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.requestNumber",
            },
            {
              text: "",
              value: "action",
            },
          ],
          data: [],
          search: "",
        },
      });
    },
    updateView($event) {
      if ($event == "viewSent") {
        this.sidebarItem = "viewSent";
        this.$refs.appBuilder.setModelData("userHistoryTable", {
          taskTable: this.createSentHistoryTableModel(),
        });
      } else if ($event == "viewReceived") {
        this.sidebarItem = "viewReceived";
        this.createTasks();
        this.getTasks()
      }
    },
  },
  mounted: function () {
    this.createTasks();
    this.getTasks();
  },
  created() {
    this.$observable.subscribe("taskTable_view", (item) => {
      this.viewTask(item);
    });
  },
};
</script>

<style></style>
