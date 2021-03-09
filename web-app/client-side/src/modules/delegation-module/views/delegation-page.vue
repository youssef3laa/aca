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
  name: "Delegation",
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
            name: 'Delegation',
          },
          tabs: [
            {
              key: 'delegation',
              id: '1',
              isActive: true,
              name: 'Delegation',
              icon: 'fas fa-exchange-alt',
            }
          ],
          page: [
            {
              key: 'page1',
              sections: {
                key: 'page1',
                sec: [
                  {
                    key: 'delegationModal',
                    type: 'ModalSection',
                    name: 'delegationModal',
                    isCard: true,
                    forms: [
                      {
                        key: 'delegationModalForm',
                        modalId: 'delegationModal',
                        inputs: [
                          {
                            type: 'DatePickerComponent',
                            label: 'From',
                            name: 'from',
                            col: '4',
                            rule: 'required'
                          },
                          {
                            type: 'DatePickerComponent',
                            label: 'To',
                            name: 'to',
                            col: '4',
                            rule: 'required'
                          },
                          {
                            type: 'AutoCompleteComponent',
                            label: 'Role',
                            name: 'role',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'AutoCompleteComponent',
                            label: 'UserId',
                            name: 'userId',
                            col: '4',
                            rule: 'required',
                            responseValue: 'userId',
                            responseText: 'details.displayName'
                          },
                          {
                            type: 'AutoCompleteComponent',
                            label: 'Delegated to',
                            name: 'delegatedTo',
                            col: '4',
                            rule: 'required',
                          },
                        ],
                        model: {
                          action: [],
                          modalTitle: '',
                          id: '',
                          from: '',
                          to: '',
                          role: {},
                          userId: {},
                          delegatedTo: {}
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
                            name: 'delegationTable',
                            subscribe: 'delegation',
                            col: 12,
                            search: true,
                            filter: true,
                            add: true,
                            actions: ['edit'],
                            modalId: 'delegationTable',
                          },
                        ],
                        model: {
                          delegationTable: {
                            url: 'delegation/read/list',
                            headers: [
                              {
                                text: 'From',
                                value: 'from',
                              },
                              {
                                text: 'To',
                                value: 'to',
                              },
                              {
                                text: 'Role',
                                value: 'role',
                              },
                              {
                                text: 'UserId',
                                value: 'userId',
                              },
                              {
                                text: 'Delegated to',
                                value: 'delegatedTo',
                              },
                              {
                                text: 'Active',
                                value: 'isActive',
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

      self.$observable.subscribe("delegationTable_add", function () {
        self.$refs.appBuilder.setModelData("delegationModalForm", {
          ...{
            role: {
              url: 'org/group/read/list',
            },
            userId: {
              url: 'org/user/read/list',
            },
            delegatedTo: {
              url: 'org/group/read/list',
            },
          },
          ...{
            modalTitle: "Add Delegation",
            action: ["add",]
          }
        });
        self.$observable.fire("delegationModal");
      });

      self.$observable.subscribe("delegationTable_edit", function (obj) {
        let item = obj.item;
        self.$refs.appBuilder.setModelData("delegationModalForm", {
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
            modalTitle: "Edit Delegation",
            action: ["edit",]
          }
        });
        self.$observable.fire("delegationModal");
      });

      self.$observable.subscribe("delegationModal_edit", function (object) {
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
          http.put("/delegation/update/" + obj.id, item).then(() => {
            self.$refs.alertComponent._alertSuccess({
                  type: "success",
                  message: "Added Successfully"
                }, () => {
                  self.$observable.fire("delegationTable_refresh");
                }
            )
          }).catch((err) => {
            console.error(err);
          });
        } else {
          http.post("/delegation/create", item).then(() => {
            self.$refs.alertComponent._alertSuccess({
                  type: "success",
                  message: "Edited Successfully"
                }, () => {
                  self.$observable.fire("delegationTable_refresh");
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
