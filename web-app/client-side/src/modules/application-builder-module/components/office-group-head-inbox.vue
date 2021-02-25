<template>
  <div style="width:100%">
    <v-container>
      <AppBuilder ref="appBuilder" :app="app" />
    </v-container>
  </div>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mixins: [formPageMixin],
  mounted() {
    // this.loadForm("secertary-inbox");
    this.$observable.subscribe("secertaryOutgoing_selected", (selected) => {
      console.log(selected);
      if (selected.length != 0) {
        this.$refs.appBuilder.setFieldData("outgoing", "actionTopComponent", {
          show: true,
        });
      } else {
        this.$refs.appBuilder.setFieldData("outgoing", "actionTopComponent", {
          show: false,
        });
      }
    });
    this.$observable.subscribe("subjectHeadOfOfficeGroup", (text) => {
      console.log(text);
      this.$observable.fire("outgoing",{type:'modelUpdate',model:{data:[
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
                              },]}})
    });
    this.$observable.subscribe("incomingNumberHeadOfOfficeGroup", (text) => {
      console.log(text);
    });
  },
  data() {
    return {
      app: {
        pages: {
          key: "officeGroupHeadInboxPage",
          tabs: [
            {
              key: "outgoingTab",
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
                        key: "outgoing",
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
                            name: "secertaryOutgoing",
                            actions: ["view"],
                            subscribe: "outgoing",
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
                              publish: "backToCertification",
                            },
                            {
                              title: "send",
                              icon: "far fa-paper-plane",
                              publish: "backToCertification",
                            },
                            {
                              title: "sendToAnotherManagement",
                              icon: "far fa-save",
                              publish: "backToCertification",
                            },
                          ],

                          secertaryOutgoing: {
                            url: null,
                            key: "requestNumber",
                            headers: [
                              {
                                text: "requestNumber",
                                value: "requestNumber",
                              },
                              { text: "incomingDate", value: "incomingDate" },
                              {
                                text: "subject",
                                value: "subject",
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
                              {
                                requestNumber: "soba3 kofta",
                                incomingDate: "159",
                                subject: "4.0",
                                management: "24",
                                importance: false,
                                chairmanOfCommisionSignature:
                                  "https://i.picsum.photos/id/11/500/300.jpg?hmac=X_37MM-ameg7HWL6TKJT2h_5_rGle7IGN_CUdEDxsAQ",
                                viceChairmanOfCommisionSignature:
                                  "https://i.picsum.photos/id/11/500/300.jpg?hmac=X_37MM-ameg7HWL6TKJT2h_5_rGle7IGN_CUdEDxsAQ",
                              },
                            ],
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
};
</script>