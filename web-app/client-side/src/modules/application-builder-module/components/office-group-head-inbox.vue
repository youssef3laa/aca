<template>
  <div style="width:100%">
    <v-container>
      <AppBuilder ref="appBuilder" :app="app" />
    </v-container>
  </div>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import router from "../../../router";

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mixins: [formPageMixin],
  mounted() {
    this.getTasks("technicalTasks");
    // this.loadForm("secertary-inbox");
    this.topActionsSubscriptions();
    this.$observable.subscribe("technicalTasksTable_view", (item) => {
      this.viewTask(item);
    });
    this.$observable.subscribe("technicalTasksTable_selected", (selected) => {
      console.log(selected);
      if (selected.length != 0) {
        this.$refs.appBuilder.setFieldData(
          "sentFromManagement",
          "actionTopComponent",
          {
            show: true,
          }
        );
      } else {
        this.$refs.appBuilder.setFieldData(
          "sentFromManagement",
          "actionTopComponent",
          {
            show: false,
          }
        );
      }
    });
    this.$observable.subscribe("subjectHeadOfOfficeGroup", (text) => {
      console.log(text);
      this.$observable.fire("sentFromManagement", {
        type: "modelUpdate",
        model: {
          data: [
            {
              requestNumber: "Frozen Yogurt",
              incomingDate: "159",
              subject: "6.0",
              management: "24",
              importance: true,
              chairmanOfCommisionSignature:
                "https://i.picsum.photos/id/11/500/300.jpg?hmac=X_37MM-ameg7HWL6TKJT2h_5_rGle7IGN_CUdEDxsAQ",
              viceChairmanOfCommisionSignature:
                "https://i.picsum.photos/id/11/500/300.jpg?hmac=X_37MM-ameg7HWL6TKJT2h_5_rGle7IGN_CUdEDxsAQ",
            },
          ],
        },
      });
    });
    this.$observable.subscribe("incomingNumberHeadOfOfficeGroup", (text) => {
      console.log(text);
    });
  },
  data() {
    return {
      inputSchemaArray:[],
      app: {
        pages: {
          key: "officeGroupHeadInboxPage",
          tabs: [
            {
              key: "sentFromManagementTab",
              id: "1",
              isActive: "true",
              name: "sentFromManagement",
              icon: "fas fa-copy",
            },
            {
              key: "signaturesTab",
              id: "2",
              name: "sentFromCertification",
              icon: "fas fa-pen-alt",
            },
            {
              key: "temporarilySavedForModificationTab",
              id: "3",
              name: "temporarilySavedForModification",
              icon: "fas fa-save",
            },
          ],
          page: [
            {
              key: "page1",
              sections: {
                tabs: [],
                sec: [
                  {
                    key: "section 1",
                    tabId: 1,
                    isTab: true,
                    type: "DefaultSection",
                    display: "block",
                    isCard: true,
                    forms: [
                      {
                        key: "sentFromManagement",
                        inputs: [
                          {
                            type: "ActionsTopComponent",
                            name: "actionTopComponent",
                            show: false,
                            col: 6,
                          },
                          {
                            type: "InputComponent",
                            label: "subject",
                            name: "subject",
                            readonly: false,
                            publish: "subjectHeadOfOfficeGroup",
                            col: 3,
                          },
                          {
                            type: "InputComponent",
                            label: "incomingNumber",
                            name: "incomingNumber",
                            readonly: false,
                            publish: "incomingNumberHeadOfOfficeGroup",
                            col: 3,
                          },

                          {
                            type: "DataTableComponent",
                            name: "technicalTasksTable",
                            actions: ["view"],
                            subscribe: "technicalTasks",
                            select: true,
                            col: 12,
                            images: [
                              "chairmanOfCommisionSignature",
                              "viceChairmanOfCommisionSignature",
                            ],
                            selectable: "importance",
                          },
                        ],
                        model: {
                          actionTopComponent: [
                            {
                              title: "backToCertification",
                              icon: "fas fa-undo",
                              publish: "backToCertification",
                            },
                            {
                              title: "temporarySave",
                              icon: "far fa-save",
                              publish: "temporarySave",
                            },
                            {
                              title: "send",
                              icon: "far fa-paper-plane",
                              publish: "send",
                            },
                            {
                              title: "sendToAnotherManagement",
                              icon: "far fa-save",
                              publish: "sendToAnotherManagement",
                            },
                          ],

                          technicalTasksTable: {
                            url: null,
                            key: "TaskId",
                            headers: [
                              {
                                text: "requestNumber",
                                value:
                                  "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.requestNumber",
                              },
                              { text: "incomingDate", value: "DeliveryDate" },
                              {
                                text: "subject",
                                value:
                                  "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.subject",
                              },
                              {
                                text: "section/management",
                                value: "management",
                              },
                              {
                                text: "chairmanOfCommisionSignature",
                                value: "chairmanOfCommisionSignature",
                              },
                              {
                                text: "viceChairmanOfCommisionSignature",
                                value: "viceChairmanOfCommisionSignature",
                              },
                              {
                                text: "importantAndImmediate",
                                translate: false,
                                value: "importance",
                              },
                              {
                                text: "",
                                value: "action",
                                sortable: false,
                              },
                            ],
                            data: [],
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
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
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
    topActionsSubscriptions() {
      this.$observable.subscribe("sendToAnotherManagement", () => {
        console.log("sendToAnotherManagement");
      });
      this.$observable.subscribe("backToCertification", () => {
        console.log("backToCertification");
      });
      this.$observable.subscribe("temporarySave", () => {
        console.log("temporarySave");
      });
      this.$observable.subscribe("send", () => {
        console.log("send");
      });
    },
  },
};
</script>
