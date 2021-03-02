<template>
  <splitpanes style="height: auto; direction: ltr" class="default-theme">
    <pane style="direction: rtl">
      <AppBuilder
        v-show="sidebarItem == 'viewSubjects'"
        ref="subjects"
        :app="app"
      />
      <AppBuilder
        v-show="sidebarItem == 'viewSentMemos'"
        ref="outbox"
        :app="app2"
      />
    </pane>
    <pane
      style="height: auto; direction: rtl"
      max-size="16"
      min-size="10"
      size="16"
    >
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
  data() {
    return {
      response: [],
      app: {
        pages: {
          key: "tabtest",
          tabs: [
            {
              key: "tab1",
              id: "1",
              isActive: true,
              name: "البيانات الأساسية",
              icon: "far fa-file-alt",
            },

            {
              key: "tab2",
              id: "2",
              name: "collapse",
              icon: "fas fa-paperclip",
            },
          ],
          page: [
            {
              key: "page1",
              sections: {
                key: "tabtest",
                tabs: [
                  {
                    key: "subTab1",
                    id: "1",
                    isActive: true,
                    name: "mainData",
                  },
                  {
                    key: "subTab2",
                    id: "2",
                    name: "supervisorLevels/technicalOffice",
                  },
                  {
                    key: "subTab3",
                    id: "3",
                    name: "attachments",
                  },
                  {
                    key: "subTab4",
                    id: "4",
                    name: "COCDecision",
                    icon: "",
                  },
                ],
                sec: [
                  {
                    isNestedTab: true,
                    key: "subjectTab",
                    tabId: "1",
                    isTab: true,
                    type: "DefaultSection",
                    display: "block",
                    isCard: true,
                    show: true,
                    forms: [
                      {
                        type: "collapse",
                        name: "subject",

                        key: "subjectForm",
                        tabId: "1",
                        display: "block",
                        isTab: true,

                        inputs: [
                          {
                            type: "TextComponent",
                            label: "notes",
                            name: "notes",
                            col: "12",
                          },
                        ],
                        model: {
                          notes: "كلام كلام",
                        },
                      },
                      {
                        name: "subjectSummary",
                        type: "collapse",
                        key: "subjectSummaryfrom",
                        tabId: "1",

                        inputs: [
                          {
                            type: "TextComponent",
                            label: "notes",
                            name: "notes",
                            col: "12",
                          },
                        ],
                        model: {
                          notes: "كلام كلام",
                        },
                      },
                      {
                        name: "presidentDirections",
                        type: "collapse",
                        key: "presidentDirectionsForm",
                        tabId: "1",

                        inputs: [
                          {
                            type: "TextComponent",
                            label: "notes",
                            name: "notes",
                            col: "12",
                          },
                        ],
                        model: {
                          notes: "كلام كلام",
                        },
                      },
                      {
                        name: "situationEstimate",
                        type: "collapse",
                        key: "situationEstimateForm",
                        tabId: "1",

                        inputs: [
                          {
                            type: "TextComponent",
                            label: "notes",
                            name: "notes",
                            col: "12",
                          },
                        ],
                        model: {
                          notes: "كلام كلام",
                        },
                      },
                      {
                        name: "opinion",
                        type: "collapse",
                        key: "opinionForm",
                        tabId: "1",

                        inputs: [
                          {
                            type: "TextComponent",
                            label: "notes",
                            name: "notes",
                            col: "12",
                          },
                        ],
                        model: {
                          notes: "كلام كلام",
                        },
                      },
                      {
                        name: "viceOpinion",
                        type: "collapse",
                        key: "viceOpinionForm",
                        tabId: "1",

                        inputs: [
                          {
                            type: "TextComponent",
                            label: "notes",
                            name: "notes",
                            col: "12",
                          },
                        ],
                        model: {
                          notes: "كلام كلام",
                        },
                      },
                      // Signatures
                      {
                        tabId: "4",
                        key: "signature",
                        display: "block",
                        isTab: true,
                        inputs: [
                          {
                            type: "SignatureComponent",
                            name: "signature",
                            oldSignatures: false,
                            col: 12,
                          },
                        ],
                        model: {
                          signature: { requestId: "" },
                        },
                      },

                      {
                        tabId: "3",
                        key: "attachments",
                        display: "block",
                        isTab: true,
                        inputs: [
                          {
                            type: "AttachmentComponent",
                            name: "attachment",
                            col: 12,
                          },
                        ],
                        model: {
                          attachment: {
                            readonly: true,
                          },
                        },
                      },
                    ],
                  },
                ],
              },
            },
          ],
        },
      },
      app2: {},
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
        console.log(response);
        let data = JSON.parse(response.data.data);
        console.log(data);
      });
    },
    fillOutbox: function() {
      http.get("history/user/count").then((response) => {
        if (!response.data.data) return;
        this.sidebarItems[1].notifications = response.data.data;
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
  mounted: function() {
    // this.loadForm("subjects", this.fillSubjects, "subjects")
    // this.loadForm("outbox", this.fillOutbox, "outbox")
    // this.$observable.subscribe("taskTable_view", (item) => {
    //   this.viewTask(item);
    // });
    // this.$observable.subscribe("outboxTable_view", (item) => {
    //   this.viewSentTask(item);
    // });
  },
};
</script>

<style></style>
