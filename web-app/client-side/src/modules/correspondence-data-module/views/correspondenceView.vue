<template>
  <div>
    <div style="display: flex">
      <TasksLists></TasksLists>
      <AdvancedSearch></AdvancedSearch>
      <OrgChartBtn></OrgChartBtn>
    </div>
    <splitpanes class="default-theme" dir="ltr">
      <pane>
    
        <AppBuilder ref="appBuilder" :app="app" />
      </pane>
      <pane size="21">
        <Sidebar></Sidebar>
      </pane>
    </splitpanes>
    <!-- <v-container>
      <AppBuilder ref="appBuilder" :app="app1" />
    </v-container> -->
  </div>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";
import correspondenceMixin from "../mixin/correspondenceMixin";
import formPageMixin from "../../../mixins/formPageMixin";
import TasksLists from "../../application-builder-module/components/tasks-list-component";
import AdvancedSearch from "../../application-builder-module/components/advanced-search-component";
import OrgChartBtn from "../../application-builder-module/components/org-chart-btn-component";
import Sidebar from "../../application-builder-module/components/sidebar-component";
// import TableComponent from "../../application-builder-module/components/table-component"
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import http from "../../core-module/services/http";

export default {
  name: "correspondenceView",
  components: {
    TasksLists,
    AdvancedSearch,
    OrgChartBtn,
    Sidebar,
    Splitpanes,
    Pane,
    AppBuilder
  },
  mixins: [correspondenceMixin, formPageMixin],

  methods: {
    getTasks: function () {
      http.get("workflow/human/tasks").then((response) => {
        console.log(response);
        var data = JSON.parse(response.data.data);
        console.log(data);
        this.$observable.fire("tasks", {
          type: "modelUpdate",
          model: data,
        });
      });
    },
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
                name: "المهام",
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
                            text: "Target Type",
                            value: "Target.type",
                          },
                          {
                            text: "Actions",
                            value: "actions",
                            sortable: false,
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
      app1: {
        pages: [
          {
            key: "page1",
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
                name: "الآراء السابقة",
                icon: "fas fa-history",
              },
              {
                key: "tab3",
                id: "3",
                name: "مذكرة العرض",
                icon: "fas fa-print",
              },
              {
                key: "tab4",
                id: "4",
                name: "المرفقات",
                icon: "fas fa-paperclip",
              },
            ],
            sections: [
              {
                key: "section1",
                type: "TitleComponet",
                name: "بيانات المكاتبة",
                actions: ["cancel", "complete"],
              },
              {
                key: "section1",
                tabId: "1",
                isTab: true,
                type: "DefaultSection",
                isCard: true,
                display: "block",
                forms: [
                  {
                    key: "form1",
                    publish: "form1Change",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Step Name",
                        name: "stepId",
                        readonly: false,
                        col: "12",
                      },
                      {
                        type: "AutoCompleteComponent",
                        name: "receiver",
                        rule: "required",
                        readonly: false,
                        col: "6",
                      },
                      {
                        type: "InputComponent",
                        label: "requestDate",
                        name: "requestDate",
                        readonly: false,
                        col: "6",
                      },
                      {
                        type: "TextareaComponent",
                        label: "notes",
                        name: "notes",
                        readonly: false,
                        col: "12",
                      },
                    ],
                    model: {
                      stepId: "",
                      receiver: "",
                      requestDate: "",
                      notes: "",
                    },
                  },
                ],
              },
              {
                key: "section2",
                tabId: "2",
                isTab: true,
                type: "DefaultSection",
                isCard: true,
                display: "none",
                forms: [
                  {
                    key: "historyTable",
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
                            text: "الاسم",
                            align: "start",
                            filterable: false,
                            value: "userCN",
                          },
                          {
                            text: "التاريخ",
                            value: "approvalDate",
                          },
                        ],
                        data: [],
                        search: "",
                      },
                    },
                  },
                ],
              },
              {
                key: "section3",
                tabId: "3",
                isTab: true,
                type: "DefaultSection",
                isCard: true,
                display: "none",
                forms: [
                  {
                    key: "memoPage",
                    name: "القسم الأول",
                    inputs: [
                      {
                        type: "MemoComponent",
                        name: "memoComp",
                        label: "memorandumType",
                        col: 12,
                      },
                    ],
                    model: {
                      memoComp: {
                        list: [
                          {
                            value: "1",
                            text: "memo1",
                          },
                          {
                            value: "2",
                            text: "memo2",
                          },
                        ],
                        requestId: "",
                      },
                    },
                  },
                ],
              },
              {
                key: "section4",
                tabId: "4",
                isTab: true,
                type: "DefaultSection",
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
                key: "section5",
                type: "DefaultSection",
                isCard: true,
                name: "الموافقة على الطلب",
                forms: [
                  {
                    key: "ApprovalForm",
                    inputs: [
                      {
                        type: "ApprovalComponent",
                        title: "الرجاء الموافقة على الطلب",
                        commentLabel: "notes",
                        name: "approval",
                        actions: [
                          {
                            value: "approve",
                            label: "الموافقة على الطلب",
                          },
                          {
                            value: "requestModification",
                            label: "طلب تعديل",
                          },
                        ],
                      },
                    ],
                    model: {
                      approval: {
                        decision: "",
                        comment: "",
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