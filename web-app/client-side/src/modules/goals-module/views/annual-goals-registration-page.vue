<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"></AppBuilder>
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";
import goalsMixin from "../mixin/goalsMixin";
export default {
  components: {
    AppBuilder,
  },
  mixins: [formPageMixin, goalsMixin],
  async mounted() {
    this.handleGoalModal();
    await this.setAllGoals();
    await this.setGoalsByYear();
  },
  methods: {
    async setGoalsByYear() {
      this.$observable.subscribe("titleYearSelected", async (year) => {
        console.log(year);
        let goals = await this.getGoalsByYear(year);
        this.$observable.fire("annualGoalsTable", {
          type: "modelUpdate",
          model: { data: goals },
        });
      });
    },
    async setAllGoals() {
      let goals = await this.getAllGoals();
      this.$observable.fire("annualGoalsTable", {
        type: "modelUpdate",
        model: { data: goals },
      });
    },
    handleGoalModal() {
      this.$observable.subscribe("addGoal", () => {
        this.$observable.fire("addGoalModal", {
          action: "add",
        });
      });
      this.$observable.subscribe("addGoalModal_add", () => {
        let data = this.$refs.appBuilder.getModelData("addGoalModal");
        console.log(data);
        this.postGoal(data);
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
            goalButton: true,
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
                            search: true,
                            modalId: "addGoalModal",
                            col: 12,
                          },
                        ],
                        model: {
                          annualGoalsTable: {
                            url: null,
                            styleType:"goalsTable",
                            key: "requestNumber",
                            headers: [
                              {
                                text: "goalText",
                                value: "goal",
                              },

                              // {
                              //   text: "",
                              //   value: "action",
                              //   sortable: false,
                              // },
                            ],
                            data: [],
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
                            label: "year",
                            name: "year",
                            format: "year",
                            col: "6",
                            rule: "required",
                          },
                          {
                            type: "TextareaComponent",
                            label: "goalText",
                            name: "goal",
                            col: "12",
                            rule: "required",
                          },
                          {
                            type: "SingleCheckboxComponent",
                            name: "isTraditional",
                            label: "tradionalWork",
                            col: "4",
                          },
                        ],
                        model: {
                          year: "",
                          goal: "",
                          isTraditional: false,
                          action: ["add"],
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
