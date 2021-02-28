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
              key: "tab3",
              id: "3",
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
                    key: "tab5",
                    id: "5",
                    isActive: true,
                    name: "mainData",
                  },
                  {
                    key: "tab6",
                    id: "6",
                    name: "supervisorLevels/technicalOffice",
                  },
                  {
                    key: "tab2",
                    id: "2",
                    name: "attachments",
                  },
                  {
                    key: "tab4",
                    id: "4",
                    name: "COCDecision",
                    icon: "",
                  },
                ],
                sec: [
                  {
                    key: "subjectTab",
                    tabId: "5",
                    isTab: true,
                    type: "collapseSection",
                    display: "block",
                    isCard: true,
                    name: "subject",
                    show: true,
                    forms: [
                      {
                        key: "form1",
                        publish: "form1Change",
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
                    ],
                  },
                  {
                    key: "subjectSummaryTab",
                    tabId: "5",
                    isTab: true,
                    type: "collapseSection",
                    display: "block",
                    isCard: true,
                    name: "subjectSummary",
                    show: true,
                    forms: [
                      {
                        key: "subjectSummaryfrom",
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
                    ],
                  },
                  {
                    key: "presidentDirectionsTab",
                    tabId: "5",
                    isTab: true,
                    type: "collapseSection",
                    display: "block",
                    isCard: true,
                    name: "presidentDirections",
                    show: true,
                    forms: [
                      {
                        key: "presidentDirectionsForm",
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
                    ],
                  },
                  {
                    key: "situationEstimateTab",
                    tabId: "5",
                    isTab: true,
                    type: "collapseSection",
                    display: "block",
                    isCard: true,
                    name: "situationEstimate",
                    show: true,
                    forms: [
                      {
                        key: "situationEstimateForm",
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
                    ],
                  },

                  {
                    key: "proposalsTab",
                    tabId: "5",
                    isTab: true,
                    type: "collapseSection",
                    display: "block",
                    isCard: true,
                    name: "proposals",
                    show: true,
                    forms: [
                      {
                        key: "proposalsForm",
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
                    ],
                  },
                  {
                    key: "opinionTab",
                    tabId: "5",
                    isTab: true,
                    type: "collapseSection",
                    display: "block",
                    isCard: true,
                    name: "opinion",
                    show: true,
                    forms: [
                      {
                        key: "opinionForm",
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
                    ],
                  },
                  {
                    key: "viceOpinionTab",
                    tabId: "5",
                    isTab: true,
                    type: "collapseSection",
                    display: "block",
                    isCard: true,
                    name: "viceOpinion",
                    show: true,
                    forms: [
                      {
                        key: "viceOpinionForm",
                        publish: "form1Change",
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
                    ],
                  },
                  {
                    key: "section2",
                    type: "Resizable",
                    tabId: "2",
                    isTab: true,
                    isCard: true,
                    display: "none",
                    forms: [
                      {
                        resizable: {
                          forms: [
                            {
                              key: "iframeObj",
                              background: "white",
                              inputs: [
                                {
                                  type: "IframeComponent",
                                  name: "iframeObj",
                                  col: 12,
                                },
                              ],
                              model: {
                                iframeObj: {
                                  src: "",
                                },
                              },
                            },
                            {
                              background: "white",
                              inputs: [
                                {
                                  type: "InputFileComponent",
                                  name: "inputFile",
                                  col: 12,
                                },
                              ],
                              model: {
                                inputFile: "",
                              },
                            },
                          ],
                        },
                      },
                    ],
                  },
                  {
                    key: "section3",
                    tabId: "3",
                    isTab: true,
                    isCard: true,
                    display: "none",
                    type: "CollapseSection",
                    name: "النص",
                    forms: [
                      {
                        key: "richtext",
                        inputs: [
                          {
                            type: "richtextComponent",
                            name: "richtext",
                            col: 12,
                          },
                        ],
                        model: {
                          richtext: "<p></p>",
                        },
                      },
                    ],
                  },
                  {
                    key: "section4",
                    tabId: "4",
                    isTab: true,
                    isCard: true,
                    isCanvas: true,
                    // visibility: 'hidden',
                    type: "DefaultSection",
                    forms: [
                      {
                        key: "signature",
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

    this.$observable.subscribe("taskTable_view", (item) => {
      this.viewTask(item);
    });
    this.$observable.subscribe("outboxTable_view", (item) => {
      this.viewSentTask(item);
    });
  },
};
</script>

<style></style>
