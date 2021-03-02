<template>
  <splitpanes style="height: auto" class="default-theme" dir="ltr">
    <pane>
      <AppBuilder dir="rtl" ref="appBuilder" :app="app" />
    </pane>
    <pane style="height: auto" max-size="20" size="14">
      <Sidebar :val="val.sideBar"  @btnClicked="updateView"></Sidebar>
    </pane>
  </splitpanes>
</template>

<script>
import http from "../../core-module/services/http";
import Sidebar from "../../application-builder-module/components/sidebar-component";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import historyMixin from "../../history-module/mixin/historyMixin";
import router from "../../../router";
import formPageMixin from "../../../mixins/formPageMixin"
export default {
  mixins: [historyMixin,formPageMixin],
  components: {
    Sidebar,
    Splitpanes,
    Pane,
      AppBuilder: () => import("../builders/app-builder"),

  },
  props:["val","field"],
  data() {
    return {
      response: [],
      sidebarItem: "viewReceived",
    app:{},
    items:[{name:"الوارد",notifications:21, icon:"fas fa-download",action:"viewReceived"},{name:"المرسل",notifications:34, icon:"far fa-paper-plane",action:"viewSent"},
        ],
    };
  },
  methods: {
    viewTask(item) {
      console.log("SideBarItem", this.sidebarItem);
      console.log("item obj in table", item);
      if (this.sidebarItem == "viewReceived") {
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
      else{

        try{
           let requestId = item.item.requestId;
           let page = item.item.readonlyComponent;
           router.push({
             name: page,
             params: { requestId: requestId}
           })}
           catch(e){
             console.error(e)
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
        console.log(data)

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
              text: "subject",
              align: "start",
              filterable: false,
              value:
                "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.subject",
            },
            {
              text: "creator",
              value: "Sender.displayName",
            },
            {
              text: "deliveryDate",
              value: "DeliveryDate",
            },
            {
              text: "taskRequestNumber",
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
    this.loadForm("inbox",this.getTasks)

  
    this.$observable.subscribe("taskTable_view", (item) => {
      this.viewTask(item);
    });
    console.log(JSON.stringify(this.app))
  },
  // created() {
  //   this.$observable.subscribe("taskTable_view", (item) => {
  //     this.viewTask(item);
  //   });
  // },
};
</script>

<style>
.row{
margin-top: 0px;
}
</style>
