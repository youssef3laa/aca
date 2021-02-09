<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app" />
  </v-container>
</template>

<script>

// import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "correspondenceView",
  components: {
    AppBuilder,
  },
  data() {
    return {
      app: {
        pages: [
          {
            tabs: [
              {
                key: "tab1",
                id: 1,
                name: "بيانات المكاتبة",
                isActive: true,
              },
            ],
            sections: [
              {
                key: "section 1",
                tabId: 1,
                isTab: true,
                type: "DefaultSection",
                display: "block",
                isCard: true,
                forms: [
                  {
                    key: "form1",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "رقم الوارد",
                        name: "number",
                        col: 5,
                        readonly: false,
                      },
                      {
                        type: "InputComponent",
                        label: "اسم الموضوع",
                        name: "name",
                        col: 5,
                        readonly: false,
                      },
                      {
                        type: "ButtonComponent",
                        label: "بحث الوارد",
                        action: "search",
                        name: "search",
                        col: 2,
                      },
                      {
                        type: "DataTableComponent",
                        name: "taskTable",
                        actions: ["view"],
                        subscribe: "tasks",
                        searchable:false,
                        col: 12,
                      },
                    ],
                    model: {
                      number: "",
                      name: "",
                      search: "",
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
                            text: "",
                            value: "action",
                          },
                        ],
                        subHeaders: [
                          {
                            text: "Date",
                            value: "TaskId",
                          },
                        ],
                        data: [],
                        key: "DeliveryDate",
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
    };
  },
};
</script>

<style>
</style>