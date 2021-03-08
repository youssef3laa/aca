<template>
  <splitpanes style="height: auto; direction: ltr" class="default-theme">
    <pane style="direction: rtl">
      <AppBuilder v-show="sidebarItem == 'viewSubjects'" ref="subjects" :app="subjects"/>
      <AppBuilder v-show="sidebarItem == 'viewSentMemos'" ref="outbox" :app="outbox"/>
    </pane>
    <pane style="height: auto; direction: rtl" max-size="14" min-size="10" size="14">
      <Sidebar :val="sidebarItems" @btnClicked="updateView"></Sidebar>
    </pane>
  </splitpanes>
</template>

<script>
import router from "../../../router";
import http from "../../core-module/services/http";
import "splitpanes/dist/splitpanes.css";
import { Splitpanes, Pane } from "splitpanes";
import AppBuilder from "../builders/app-builder";
import Sidebar from "../../application-builder-module/components/sidebar-component";
import formPageMixin from "../../../mixins/formPageMixin";
// import Topbar from "../../application-builder-module/components/topbar-component"

export default {
  mixins: [formPageMixin],
  components: { Sidebar, Splitpanes, Pane, AppBuilder },
  props: ["val", "field"],
  mounted() {
    console.log("app", JSON.stringify(this.app));
    this.loadForm("COC-inbox-subjects", this.fillSubjects, "subjects");
    this.loadForm("COC-outbox", this.fillOutbox, "outbox")
    this.$observable.subscribe("allSubjectsTable_view", (item) => {
      this.viewTask(item);
    });
    // this.$observable.subscribe("outboxTable_view", (item) => {
    //   this.viewSentTask(item);
    // });
        this.$observable.subscribe(
      "outboxTable_selected",
      (selected) => {
        this.selectedCertfication = selected;
        console.log(this.selected);
        if (this.selectedCertfication.length != 0) {
          this.$refs.appBuilder.setFieldData(
            "outboxForm",
            "actionTopComponent",
            {
              show: true,
            }
          );
        } else {
          this.$refs.appBuilder.setFieldData(
            "outboxForm",
            "actionTopComponent",
            {
              show: false,
            }
          );
        }
      }
    );
  },
  data() {
    return {
      response: [],
      subjects: {},
      outbox: {},
      sidebarItem: "viewSubjects",
      sidebarItems: [
        {
          name: "subjects",
          notifications: 0,
          icon: "fas fa-download",
          action: "viewSubjects",
        },
        {
          name: "outgoingMemos",
          notifications: 0,
          icon: "far fa-paper-plane",
          action: "viewSentMemos",
        },
      ],
    };
  },
  methods: {
    viewTask(item) {
      try {
        let taskId = item.item.TaskId,
        page = item.item.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.component;
        router.push({
          name: page,
          params: { taskId: taskId },
        });
      } catch (e) {
        console.error(e);
      }
    },
    viewSentTask(item) {
      try {
        let requestId = item.item.requestId;
        let page = item.item.readonlyComponent;
        router.push({
          name: page,
          params: { requestId: requestId },
        });
      } catch (e) {
        console.error(e);
      }
    },
    fillSubjects: function() {
      http.get("workflow/human/tasks").then((response) => {
        let data = response.data.data;
        this.sidebarItems[0].notifications = data.length;
        this.$refs.subjects.setTabValue("tab2", data.length + "")
        this.$refs.subjects.setModelData("allSubjectsForm", {allSubjectsTable: {data: data}});
      });
    },
    fillOutbox: function() {
      http.get('history/user/count').then((response) => {
        if(!response.data.data) return;
        this.sidebarItems[1].notifications = response.data.data
      });
    },
    updateView($event) {
      if ($event == "viewSubjects") {
        this.sidebarItem = "viewSubjects";
      } else if ($event == "viewSentMemos") {
        this.sidebarItem = "viewSentMemos";
      }
    },
  },
};
</script>

<style></style>
