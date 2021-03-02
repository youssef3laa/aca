<template>
  <div>
    <v-container>
      <AppBuilder :app="app" ref="appBuilder"/>
    </v-container>
  </div>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder"
// import http from "@/modules/core-module/services/http"

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
              icon: 'fas fa-layer-group',
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
                        key: 'addUnitModal',
                        modalId: 'addUnitModal',
                        modalTitle: 'Add Unit',
                        inputs: [
                          {
                            type: 'InputComponent',
                            label: 'Name',
                            name: 'nameAr',
                            col: '4',
                            rule: 'required',
                          },
                          // {
                          //   type: 'InputComponent',
                          //   label: 'English name',
                          //   name: 'nameEn',
                          //   col: '4',
                          //   rule: 'required',
                          // },
                          {
                            type: 'InputComponent',
                            label: 'Unit type code',
                            name: 'unitTypeCode',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Unit code',
                            name: 'unitCode',
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
                          // nameEn: '',
                          unitTypeCode: '',
                          unitCode: '',
                          parentCode: '',
                        },
                      },
                      {
                        key: 'editUnitModal',
                        modalId: 'editUnitModal',
                        modalTitle: 'Edit Unit',
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
                            label: 'Name',
                            name: 'nameAr',
                            col: '4',
                            rule: 'required',
                          },
                          // {
                          //   type: 'InputComponent',
                          //   label: 'English name',
                          //   name: 'nameEn',
                          //   col: '4',
                          //   rule: 'required',
                          // },
                          {
                            type: 'InputComponent',
                            label: 'Unit type code',
                            name: 'unitTypeCode',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Unit code',
                            name: 'unitCode',
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
                          // nameEn: '',
                          unitTypeCode: '',
                          unitCode: '',
                          parentCode: '',
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
                            filter: true,
                            add: true,
                            actions: ['edit'],
                            modalId: 'addEscalationModal',
                          },
                        ],
                        model: {
                          escalationTable: {
                            url: 'org/unit/read/list',
                            headers: [
                              {
                                text: 'Internal code',
                                value: 'name',
                              },
                              {
                                text: 'Name',
                                value: 'nameAr',
                              },
                              // {
                              //   text: 'Name en',
                              //   value: 'nameEn',
                              // },
                              {
                                text: 'Unit type code',
                                value: 'unitTypeCode',
                              },
                              {
                                text: 'Unit code',
                                value: 'unitCode',
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
      // this.$observable.subscribe("addUnitModal_addModal", (obj) => {
      //   let item = obj.obj;
      //   delete item._valid;
      //   item.name = item.unitCode;
      //   let parentCode = item.parentCode;
      //   delete item.parentCode;
      //
      //   http.post("/org/unit/create", item).then(() => {
      //     http.put("/org/unit/" + parentCode + "/subUnitWithCode/" + item.name).then(() => {
      //       alert("Added Successfully");
      //     }).catch((response) => {
      //       // alert(response.data.metaInfo.errorMessage);
      //       alert(response);
      //     });
      //   }).catch((response) => {
      //     // alert(response.data.metaInfo.errorMessage);
      //     alert(response);
      //   });
      // });
      // this.$observable.subscribe("editUnitModal_updateModal", (obj) => {
      //   let item = obj.obj;
      //   let id = item.id;
      //   let parentCode = item.parentCode;
      //
      //   item = {
      //     "name": item.name,
      //     "nameAr": item.nameAr,
      //     "nameEn": item.nameEn,
      //     "unitTypeCode": item.unitTypeCode,
      //     "unitCode": item.unitCode,
      //   }
      //
      //   http.put("/org/unit/update/" + id, item).then(() => {
      //     http.put("/org/unit/" + parentCode + "/subUnitWithCode/" + item.name).then(() => {
      //       alert("Updated Successfully");
      //     }).catch((response) => {
      //       // alert(response.data.metaInfo.errorMessage);
      //       alert(response);
      //     });
      //   }).catch((response) => {
      //     // alert(response.data.metaInfo.errorMessage);
      //     alert(response);
      //   });
      // });
      // this.$observable.subscribe("unitsTable_add", () => {
      //   this.$observable.fire("addUnitModal", {
      //     action: "add",
      //   })
      // });
      // this.$observable.subscribe("unitsTable_edit", (data) => {
      //   data.item.parentCode = (data.item.parent.length > 0) ? data.item.parent[0].name : null;
      //   this.$refs.appBuilder.setModelData("editUnitModal", data.item)
      //   this.$observable.fire("editUnitModal", {
      //     action: "edit",
      //     obj: data.item,
      //   })
      // })
      // this.$observable.subscribe("unitsTable_delete", (data) => {
      //   http.delete("/org/unit/delete/" + data.item.id).then((response) => {
      //     alert(response.data.metaInfo.infoMessage);
      //   });
      // })
    },
  },

  mounted: function () {
    this.handleEvents();
  },
}
</script>
