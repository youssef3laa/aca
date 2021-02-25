<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"></AppBuilder>
  </v-container>
</template>

<script>
import AppBuilder from "../builders/app-builder";
export default {
  components: {
    AppBuilder,
  },
  mounted(){
    this.handleGoals();
  },
  methods:{
      handleGoals(){
                    this.$observable.subscribe("annualGoalsTable_add", () => {
                this.$observable.fire("addGoalModal", {
                    action: "add",
                });
            });
      }
  },
  data() {
    return {
      app: {
        pages: {
          key: "officeGroupHeadInboxPage",
          tabs: [
            {
              key: "AnnualGoalsRegistration",
              id: "1",
              isActive: "true",
              name: "AnnualGoalsRegistration",
              icon: "fas fa-copy",
            },
            {
              key: "",
              id: "2",
              name: "sentFromCertification",
              icon: "fas fa-pen-alt",
            },
            {
              key: "",
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
                        key: "annualGoalsForm",
                        inputs: [
                          {
                            type: "DataTableComponent",
                            name: "annualGoalsTable",
                            actions: ["view", "edit", "delete"],
                            subscribe: "annualGoalsTable",
                            modalId: "addGoalModal",
                            col: 12,
                            add: true,
                          },
                        ],
                        model: {
                          annualGoalsTable: {
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
                              },
                              {
                                requestNumber: "soba3 kofta",
                                incomingDate: "159",
                                subject: "4.0",
                                management: "24",
                              },
                            ],
                          },
                        },
                      },
                    ],
                  },
                {
                    key: 'goalsModal',
                    type: 'ModalSection',
                    name: 'goalsModal',
                    isCard: true,
                    forms: [
                      {
                        key: 'addGoalModal',
                        modalId: 'addGoalModal',
                        modalTitle: 'Add Goal',
                        inputs: [
                          {
                            type: 'InputComponent',
                            label: 'Arabic name',
                            name: 'nameAr',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'English name',
                            name: 'nameEn',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Goal type code',
                            name: 'goalTypeCode',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Goal code',
                            name: 'goalCode',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Parent code',
                            name: 'parentCode',
                            col: '4',
                            rule: 'required',
                          },
                        ],
                        model: {
                          nameAr: '',
                          nameEn: '',
                          goalTypeCode: '',
                          goalCode: '',
                          parentCode: '',
                        },
                      },
                      {
                        key: 'editGoalModal',
                        modalId: 'editGoalModal',
                        modalTitle: 'Edit Goal',
                        inputs: [
                          {
                            type: 'InputComponent',
                            label: 'Internal code',
                            name: 'name',
                            col: '4',
                            readonly: true,
                          },
                          {
                            type: 'InputComponent',
                            label: 'Arabic name',
                            name: 'nameAr',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'English name',
                            name: 'nameEn',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Goal type code',
                            name: 'goalTypeCode',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Goal code',
                            name: 'goalCode',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Parent code',
                            name: 'parentCode',
                            col: '4',
                            rule: 'required',
                          },
                        ],
                        model: {
                          id: '',
                          name: '',
                          nameAr: '',
                          nameEn: '',
                          goalTypeCode: '',
                          goalCode: '',
                          parentCode: '',
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
    };
  },
};
</script>

<style scoped></style>
