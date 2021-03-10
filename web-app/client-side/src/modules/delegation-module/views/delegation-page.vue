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
import { DateTime  } from "luxon";

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
                            label: 'Group',
                            name: 'group',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'AutoCompleteComponent',
                            label: 'User',
                            name: 'user',
                            col: '4',
                            rule: 'required',
                            responseValue: 'userId',
                            responseText: 'displayName'
                          },
                          {
                            type: 'AutoCompleteComponent',
                            label: 'Delegated to',
                            name: 'delegatedGroup',
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
                          group: {},
                          user: {},
                          delegatedGroup: {},
                          isActive: ''
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
                            actions: ['edit', {
                              icon: 'fas fa-retweet',
                              name: 'changeStatus'
                            }],
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
                                text: 'Group',
                                value: 'group.nameAr',
                              },
                              {
                                text: 'User',
                                value: 'user.displayName',
                              },
                              {
                                text: 'Delegated to',
                                value: 'delegatedGroup.nameAr',
                              },
                              {
                                text: 'Active',
                                value: 'isActive',
                              },
                              {
                                text: '',
                                value: 'action',
                                align: 'end'
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

      // console.log("------------------------------");
      // console.log(DateTime.local().plus({ days: 1 }).toFormat("yyyy-MM-dd"));
      // console.log("------------------------------");

      self.$observable.subscribe("delegationTable_add", function () {
        self.$refs.appBuilder.setModelData("delegationModalForm", {
          from: DateTime.local().plus({ days: 1 }).toFormat("yyyy-MM-dd"),
          to: DateTime.local().plus({ days: 2 }).toFormat("yyyy-MM-dd"),
          ...{
            group: {
              url: 'org/group/read/list',
              list: [],
              value: ""
            },
            user: {
              url: 'org/user/read/list',
              list: [],
              value: ""
            },
            delegatedGroup: {
              url: 'org/group/read/list',
              list: [],
              value: ""
            },
          },
          ...{
            modalTitle: "Add Delegation",
            action: ["add",]
          }
        });
        self.$observable.fire("delegationModal");
      });
      self.$observable.subscribe("delegationModal_add", function (object) {
        let obj = object.obj;
        console.log(obj);
        let item = {
          fromDate: obj.from,
          toDate: obj.to,
          role: obj.group.value.object.name,
          userId: obj.user.value.object.userId,
          delegatedTo: obj.delegatedGroup.value.object.name,
          isActive: true
        };
        console.log(item);
        http.post("/delegation/create", item).then(() => {
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
      });

      self.$observable.subscribe("delegationTable_edit", function (obj) {
        let item = obj.item;
        console.log(item);
        self.$refs.appBuilder.setModelData("delegationModalForm", {
          ...item,
          ...{
            group: {
              url: 'org/group/read/list',
              list: [
                {
                  text: (item.group !== null) ? item.group.nameAr : "",
                  value: (item.group !== null) ? item.group.name : "",
                }
              ],
              value: item.group.name
            },
            user: {
              url: 'org/user/read/list',
              list: [
                {
                  text: (item.user !== null) ? item.user.displayName : "",
                  value: (item.user !== null) ? item.user.userId : "",
                }
              ],
              value: (item.user !== null) ? item.user.userId : ""
            },
            delegatedGroup: {
              url: 'org/group/read/list',
              list: [
                {
                  text: (item.delegatedGroup !== null) ? item.delegatedGroup.nameAr : "",
                  value: (item.delegatedGroup !== null) ? item.delegatedGroup.name : "",
                }
              ],
              value: (item.delegatedGroup !== null) ? item.delegatedGroup.name : ""
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
        console.log(obj);
        let item = {
          from: obj.from,
          to: obj.to,
          role: (obj.group.value.object !== undefined)? obj.group.value.object.name: obj.group.value,
          userId: (obj.user.value.object !== undefined)? obj.user.value.object.userId: obj.user.value,
          delegatedTo: (obj.delegatedGroup.value.object !== undefined)? obj.delegatedGroup.value.object.name: obj.delegatedGroup.value,
          isActive: obj.isActive
        };
        console.log(item);
        http.put("/delegation/update/" + obj.id, item).then(() => {
          self.$refs.alertComponent._alertSuccess({
                type: "success",
                message: "Updated Successfully"
              }, () => {
                self.$observable.fire("delegationTable_refresh");
              }
          )
        }).catch((err) => {
          console.error(err);
        });
      });

      self.$observable.subscribe("delegationTable_changeStatus", function (object) {
        let obj = object.item;
        let item = {
          from: obj.from,
          to: obj.to,
          role: obj.group.name,
          userId: obj.user.userId,
          delegatedTo: obj.delegatedGroup.name,
          isActive: (!obj.isActive)
        };

        http.put("/delegation/update/" + obj.id, item).then(() => {
          self.$refs.alertComponent._alertSuccess({
                type: "success",
                message: "Updated the Status Successfully"
              }, () => {
                self.$observable.fire("delegationTable_refresh");
              }
          )
        }).catch((err) => {
          console.error(err);
        });
      });
    },
  },

  mounted: function () {
    this.handleEvents();
  },
}
</script>
