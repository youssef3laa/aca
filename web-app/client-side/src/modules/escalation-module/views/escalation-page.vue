<template>
  <div>
    <v-container>
      <AppBuilder :app="app" ref="appBuilder"/>
      <AlertComponent ref="alertComponent"></AlertComponent>
    </v-container>
  </div>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder"
import http from "@/modules/core-module/services/http"

export default {
  name: "Escalation",
  components: {
    AppBuilder,
  },
  data() {
    return {
      app: {
        pages: {
          key: 'page1',
          title: {
            key: 'title',
            type: 'TitleComponet',
            name: 'Escalation',
          },
          tabs: [
            {
              key: 'escalation',
              id: '1',
              isActive: true,
              name: 'Escalation',
              icon: 'fas fa-level-up-alt',
            }
          ],
          page: [
            {
              key: 'page1',
              sections: {
                key: 'page1',
                sec: [
                  {
                    key: 'escalationModal',
                    type: 'ModalSection',
                    name: 'escalationModal',
                    isCard: true,
                    forms: [
                      {
                        key: 'escalationModalForm',
                        modalId: 'escalationModal',
                        inputs: [
                          {
                            type: 'AutoCompleteComponent',
                            label: 'Job type',
                            name: 'jobType',
                            col: '4',
                            rule: 'required',
                            readonly: true,
                          },
                          {
                            type: 'InputComponent',
                            inputType: "number",
                            label: 'Duration',
                            name: 'duration',
                            col: '4',
                            rule: 'required|min_value:1',
                          },
                          {
                            type: 'InputComponent',
                            inputType: "number",
                            label: 'Extension',
                            name: 'extension',
                            col: '4',
                            rule: 'required|min_value:1',
                          },
                          {
                            type: 'AutoCompleteComponent',
                            label: 'Unit type to Approve',
                            name: 'unitType',
                            col: '4',
                            rule: 'required',
                          },
                        ],
                        model: {
                          action: [],
                          modalTitle: '',
                          id: '',
                          jobType: {},
                          duration: '',
                          extension: '',
                          unitType: {}
                        },
                      },
                    ],
                  },
                  {
                    tabId: '1',
                    isTab: true,
                    type: 'DefaultSection',
                    isCard: true,
                    display: 'block',
                    isActive: true,
                    forms: [
                      {
                        inputs: [
                          {
                            type: 'DataTableComponent',
                            name: 'escalationTable',
                            subscribe: 'escalation',
                            col: 12,
                            search: true,
                            filter: false,
                            add: false,
                            actions: ['edit'],
                            modalId: 'escalationTable',
                          },
                        ],
                        model: {
                          escalationTable: {
                            url: 'escalation/jobTypes/read/list',
                            headers: [
                              {
                                text: 'Job type',
                                value: 'jobType.arValue',
                              },
                              {
                                text: 'Duration',
                                value: 'duration',
                              },
                              {
                                text: 'Extension',
                                value: 'extension',
                              },
                              {
                                text: 'Unit type to Approve',
                                value: 'unitType.arValue',
                              },
                              {
                                text: '',
                                value: 'action',
                              },
                            ],
                            data: [],
                            search: '',
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
    }
  },
  methods: {
    handleEvents: function () {
      let self = this;
      self.$observable.subscribe("escalationTable_edit", function (obj) {
        let item = obj.item;
        self.$refs.appBuilder.setModelData("escalationModalForm", {
          ...item,
          ...{
            jobType: {
              url: 'lookup/get/category/jobType',
              list: [
                {
                  text: (item.jobType !== null) ? item.jobType.arValue : "",
                  value: (item.jobType !== null) ? item.jobType.key : "",
                }
              ],
              value: (item.jobType !== null) ? item.jobType.key : ""
            },
            unitType: {
              url: 'lookup/get/category/unitType',
              list: [
                {
                  text: (item.unitType !== null) ? item.unitType.arValue : "",
                  value: (item.unitType !== null) ? item.unitType.key : "",
                }
              ],
              value: (item.unitType !== null) ? item.unitType.key : ""
            },
          },
          ...{
            modalTitle: "Edit Escalation",
            action: ["edit",]
          }
        });
        self.$observable.fire("escalationModal");
      });

      self.$observable.subscribe("escalationModal_edit", function (object) {
        let obj = object.obj;
        let item = {
          duration: parseInt(obj.duration),
          extension: parseInt(obj.extension),
          jobType: parseInt(obj.jobType.value),
          unitType: parseInt(obj.unitType.value)
        };
        // item.jobType = (Number.isNaN(item.jobType)? obj.jobType.value.value: item.jobType;
        item.unitType = (Number.isNaN(item.unitType)) ? obj.unitType.value.value : item.unitType;
        if (obj.id !== null) {
          http.put("/escalation/update/" + obj.id, item).then(() => {
            self.$refs.alertComponent._alertSuccess({
                  type: "success",
                  message: "Added Successfully"
                }, () => {
                  self.$observable.fire("escalationTable_refresh");
                }
            )
          }).catch((err) => {
            console.error(err);
          });
        } else {
          http.post("/escalation/create", item).then(() => {
            self.$refs.alertComponent._alertSuccess({
                  type: "success",
                  message: "Edited Successfully"
                }, () => {
                  self.$observable.fire("escalationTable_refresh");
                }
            )
          }).catch((err) => {
            console.error(err);
          });
        }
      });
    },
  },

  mounted: function () {
    this.handleEvents();
  },
}
</script>
