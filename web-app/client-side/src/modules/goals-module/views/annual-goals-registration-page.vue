<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"></AppBuilder>
  </v-container>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";
export default {
  components: {
    AppBuilder,
  },
  mounted() {
    this.handleGoals();
  },
  methods: {
    handleGoals() {
      this.$observable.subscribe("addGoal", () => {
        this.$observable.fire("addGoalModal", {
          action: "add",
        });
      });
    },
  },
  data() {
    return {
      app: {
        pages: {
          key: "officeGroupHeadInboxPage",
          title: {
            key: "title",
            type: "TitleComponet",
            name: "annualGoals",
            goalButton:true,
          },

          page: [
            {
              key: "page1",
              sections: {
                tabs: [],
                sec: [
                  {
                    key: "section 1",
                    type: "DefaultSection",
                    display: "block",
                    search: true,
                    isCard: true,
                    forms: [
                      {
                        key: "annualGoalsForm",
                        inputs: [
                          {
                            type: "DataTableComponent",
                            name: "annualGoalsTable",
                            // actions: ["view", "edit", "delete"],
                            subscribe: "annualGoalsTable",
                            search:true,
                            modalId: "addGoalModal",
                            col: 12,
                          },
                        ],
                        model: {
                          annualGoalsTable: {
                            url: null,
                            key: "requestNumber",
                            headers: [
                              {
                                text: "goalText",
                                value: "goalText",
                              },

                              // {
                              //   text: "",
                              //   value: "action",
                              //   sortable: false,
                              // },
                            ],
                            data: [
                              {
                                goalText:
                                  "هذا النص هو مثال لنص يمكن أن يستبدل في نفس المساحة، لقد تم توليد هذا النص من مولد النص العربى، حيث يمكنك أن تولد مثل هذا النص أو العديد من النصوص الأخرى إضافة إلى زيادة عدد الحروف التي يولدها التطبيق. إذا كنت تحتاج إلى عدد أكبر من الفقرات يتيح لك مولد النص العربى زيادة عدد الفقرات كما تريد، النص لن يبدو مقسما ولا يحوي أخطاء لغوية، مولد النص العربى مفيد لمصممي المواقع على وجه الخصوص حيث يحتاج العميل فى كثير من الأحيان أن يطلع على صورة حقيقية لتصميم الموقع",
                              },
                              {
                                goalText:
                                  "هذا النص هو مثال لنص يمكن أن يستبدل في نفس المساحة، لقد تم توليد هذا النص من مولد النص العربى، حيث يمكنك أن تولد مثل هذا النص أو العديد من النصوص الأخرى إضافة إلى زيادة عدد الحروف التي يولدها التطبيق. إذا كنت تحتاج إلى عدد أكبر من الفقرات يتيح لك مولد النص العربى زيادة عدد الفقرات كما تريد، النص لن يبدو مقسما ولا يحوي أخطاء لغوية، مولد النص العربى مفيد لمصممي المواقع على وجه الخصوص حيث يحتاج العميل فى كثير من الأحيان أن يطلع على صورة حقيقية لتصميم الموقع",
                              },
                            ],
                          },
                        },
                      },
                    ],
                  },
                  {
                    key: "goalsModal",
                    type: "ModalSection",
                    name: "goalsModal",
                    isCard: true,
                    forms: [
                      {
                        key: "addGoalModal",
                        modalId: "addGoalModal",
                        modalTitle: "Add Goal",
                        inputs: [
                          {
                            type: "DatePickerComponent",
                            label: "Arabic name",
                            name: "nameAr",
                            col: "6",
                            rule: "required",
                          },
                          {
                            type: "TextareaComponent",
                            label: "goalText",
                            name: "goalText",
                            col: "12",
                            rule: "required",
                          },
                          {
                            type: "checkboxComponent",
                            name: "parentCode",
                            decisions:[{
                              value:"tradionalWork",
                              label:"tradionalWork",
                            }],
                            col: "4",
                            rule: "required",
                          },
                        ],
                        model: {
                          nameAr: "",
                          nameEn: "",
                          goalTypeCode: "",
                          goalCode: "",
                          parentCode: "",
                          action:["add"]
                        },
                      },
                      {
                        key: "editGoalModal",
                        modalId: "editGoalModal",
                        modalTitle: "Edit Goal",
                        inputs: [
                          {
                            type: "InputComponent",
                            label: "Internal code",
                            name: "name",
                            col: "4",
                            readonly: true,
                          },
                          {
                            type: "InputComponent",
                            label: "Arabic name",
                            name: "nameAr",
                            col: "4",
                            rule: "required",
                          },
                          {
                            type: "InputComponent",
                            label: "English name",
                            name: "nameEn",
                            col: "4",
                            rule: "required",
                          },
                          {
                            type: "InputComponent",
                            label: "Goal type code",
                            name: "goalTypeCode",
                            col: "4",
                            rule: "required",
                          },
                          {
                            type: "InputComponent",
                            label: "Goal code",
                            name: "goalCode",
                            col: "4",
                            rule: "required",
                          },
                          {
                            type: "InputComponent",
                            label: "Parent code",
                            name: "parentCode",
                            col: "4",
                            rule: "required",
                          },
                        ],
                        model: {
                          id: "",
                          name: "",
                          nameAr: "",
                          nameEn: "",
                          goalTypeCode: "",
                          goalCode: "",
                          parentCode: "",
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
