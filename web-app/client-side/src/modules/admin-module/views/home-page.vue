<template>
  <div>
    <v-container>
      <AppBuilder :app="app" />
    </v-container>
  </div>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";
import http from "../../core-module/services/http";
import { extend } from "vee-validate";

extend("password", {
  params: ["target"],
  validate(value, { target }) {
    return value === target;
  },
  message: "Password confirmation does not match",
});

export default {
  name: "HomePage",
  components: {
    AppBuilder,
  },
  data() {
    return {
      // componentName: null,
      app: {
        pages: [
          {
            sections: [
              // {
              //   tabs: [
              //     {
              //       id: 1,
              //       name: 'بيانات السياسة',
              //     },
              //   ],
              //   forms: [
              //     {
              //       publish: 'submit',
              //       event: 'submit',
              //       inputs: [
              //         {
              //           type: 'InputComponent',
              //           label: 'First name',
              //           name: 'Fname',
              //           col: 4,
              //           rule: 'required|minmax:2,25',
              //           //  readonly : true
              //         },
              //         {
              //           type: 'InputComponent',
              //           label: 'Last name',
              //           name: 'Lname',
              //           col: 4,
              //           rule: 'required|password:@Fname',
              //           // rule: 'required|minmax:2,25',
              //         },
              //         {
              //           type: 'InputComponent',
              //           label: 'Email Adress',
              //           name: 'Email',
              //           col: 4,
              //           rule: 'required|minmax:2,25',
              //         },
              //         {
              //           type: 'ButtonComponent',
              //           action: 'submit',
              //           label: 'submit',
              //           name: 'submitBtn',
              //           col: 4,
              //           rule: 'required|minmax:2,25',
              //         },
              //       ],
              //       model: {
              //         Fname: '',
              //         Lname: '',
              //         Email: '',
              //       },
              //     },
              //   ],
              // },
              {
                type: "card",
                forms: [
                  {
                    key: "form3",
                    inputs: [
                      {
                        type: "chartsComponent",
                        chartType: "BarChart",
                        col: 4,
                      },
                      {
                        type: "chartsComponent",
                        chartType: "PieChart",
                        col: 4,
                      },
                      {
                        type: "chartsComponent",
                        chartType: "BubbleChart",
                        col: 4,
                      },
                    ],
                    model: {},
                  },
                ],
              },
              {
                tabs: [
                  {
                    id: 1,
                    name: "المهام",
                  },
                ],
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
    };
  },
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

  mounted: function () {
    this.getTasks();
    this.$observable.subscribe("submit", (model) => {
      console.log(model);
      this.getTasks();
      if (model.valid) {
        http
          .post("employee/initiate/", model.model)
          .then((response) => {
            console.log(response);
            this.getTasks();
          })
          .catch((error) => {
            console.error(error);
          });
      }
    });
  },
};

// <component
//         v-if="formBuilder != null"
//         v-bind:is="formBuilder"
//       ></component>

// computed: {
//   formBuilder: function () {
//     if (this.componentName) {
//       // return () => import(`../../application-builder-module/components/${this.componentName}`)
//       return () => import(`../../../components/${this.componentName}`);
//     }
//     return null;
//   },
// },
</script>
