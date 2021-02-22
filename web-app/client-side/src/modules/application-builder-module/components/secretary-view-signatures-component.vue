<template>
  <div style="width:100%">

    <v-container>
      <v-row>
        <v-col class="padding-left" cols="5">
          <AppBuilder ref="appBuilder" :app="app" />
        </v-col>
        <v-col class="no-padding" cols="7">
          <AppBuilder ref="appBuilder2" :app="app1" />
          
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  data() {
    return {
      app: {
        pages: {
          key: "incomingTablePage",
          tabs: [],

          page: [
            {
              key: "page1",
              sections: {
                sec: [
                  {
                    key: "section 1",
                    type: "DefaultSection",
                    display: "block",
                    isCard: false,
                    forms: [
                      {
                        key: "signaturesTable",
                        inputs: [
                          {
                            type: "DataTableComponent",
                            name: "signaturesTable",
                            actions: ["view"],
                            subscribe: "signatures",
                            col: 12,
                          },
                        ],
                        model: {
                          signaturesTable: {
                            url: null,
                            headers: [
                              {
                                text: "incomingNumber",
                                align: "start",
                                value: "",
                              },
                              {
                                text: "incomingDate",
                                value: "",
                              },
                              { text: "", value: "action" },
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
            },
          ],
        },
      },

      app1: {
        pages: {
          key: "incomingPage",
          tabs: [
            {
              key: "mainDataTab",
              id: "1",
              isActive: "true",
              name: "mainData",
            },
            {
              key: "chairmanOfCommisionSignature",
              id: "2",
              name: "chairmanOfCommisionSignature",
            },
            {
              key: "viceChairmanOfCommisionSignature",
              id: "3",
              name: "viceChairmanOfCommisionSignature",
            },
          ],
          page: [
            {
              key: "page1",
              sections: {
                tabs: [],
                sec: [
                  {
                    key: "incomingDataSection",
                    name: "incomingDataSection",
                    tabId: "1",
                    isTab: true,
                    type: "DefaultSection",
                    isCard: true,
                    display: "block",
                    forms: [
                      {
                        key: "mainData",
                        inputs: [
                          {
                            type: "InputComponent",
                            label: "followUpNumber",
                            name: "followUpNumber",
                            readonly: true,
                            col: "4",
                          },
                          {
                            type: "DatePickerComponent",
                            label: "followUpDate",
                            name: "followUpDate",
                            rule: "required",
                            max: "maxDate",
                            col: "4",
                          },
                          {
                            type: "DatePickerComponent",
                            label: "nextFollowUpDate",
                            name: "nextFollowUpDate",
                            rule: "required",
                            max: "maxDate",
                            col: "4",
                          },
                          {
                            key: "signaturesRadioGroup",
                            type: "RadioGroupComponent",
                            name: "decision",
                            label: "decision",
                            col: 12,
                          },

                          {
                            type: "TextareaComponent",
                            label: "topicSummary",
                            name: "topicSummary",
                            col: "12",
                          },

                     
                          {
                            type: "DatePickerComponent",
                            label: "chairmanOfCommisionApprovalDate",
                            name: "chairmanOfCommisionApprovalDate",
                            rule: "required",
                            max: "maxDate",
                            col: "6",
                          },
                          {
                            type: "DatePickerComponent",
                            label: "viceChairmanOfCommisionApprovalDate",
                            name: "viceChairmanOfCommisionApprovalDate",
                            rule: "required",
                            max: "maxDate",
                            col: "6",
                          },
                        ],
                        model: {
                          maxDate: null,
                          decision: {
                            options: [
                              {
                                name: "approve",
                                label: "approve",
                              },
                               {
                                name: "requestModification",
                                label: "requestModification",
                              },
                            ],
                           
                          },
                        },
                      },
                    ],
                  },
                  {
                    key: "chairmanOfCommisionSignatureSection",
                    tabId: "2",
                    isTab: true,
                    type: "DefaultSection",
                    isCard: true,
                    display: "none",
                    forms: [
                      {
                        key: "chairmanOfCommisionSignatureForm",
                        inputs: [
                          {
                            type: "ImageComponent",
                            name: "chairmanOfCommisionSignature",
                            col: 12,
                          },
                          {
                            type: "TextareaComponent",
                            label: "topicSummary",
                            name: "chairmanOfCommisionText",
                            col: "12",
                          },
                        ],
                        model: {
                          chairmanOfCommisionSignature: {
                            url:"https://i.picsum.photos/id/11/500/300.jpg?hmac=X_37MM-ameg7HWL6TKJT2h_5_rGle7IGN_CUdEDxsAQ"
                          },
                          chairmanOfCommisionText: "",
                        },
                      },
                    ],
                  },
                  {
                    key: "vicechairmanOfCommisionSignatureSection",
                    tabId: "3",
                    isTab: true,
                    type: "DefaultSection",
                    isCard: true,
                    display: "none",
                    forms: [
                      {
                        key: "viceChairmanOfCommisionSignatureForm",
                        inputs: [
                          {
                            type: "SignatureComponent",
                            name: "viceChairmanOfCommisionSignature",
                            col: 12,
                          },
                          {
                            type: "TextareaComponent",
                            label: "topicSummary",
                            name: "viceChairmanOfCommisionText",
                            col: "12",
                          },
                        ],
                        model: {
                          viceChairmanOfCommisionSignature: {},
                          viceChairmanOfCommisionText: "",
                        },
                      },
                    ],
                  },
                  {
                    key: "processRouting",
                    type: "DefaultSection",
                    isCard: true,
                    forms: [
                      {
                        key: "approvalForm",
                        inputs: [
                          {
                            type: "ApprovalComponent",
                            name: "approval",
                          },
                        ],
                        model: {
                          approval: {},
                        },
                      },
                    ],
                  },
                  {
                    key: "actionsSection",
                    type: "ActionsSection",
                    actions: ["complete"],
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

<style scoped>
.no-padding {
  padding: 0px !important;
}
.padding-left {
  padding: 0px !important;
  padding-left: 1px !important;
  border-left: 1px solid #d1d1d1 !important;
}
</style>
