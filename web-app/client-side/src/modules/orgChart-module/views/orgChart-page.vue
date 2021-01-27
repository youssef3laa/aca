<template>
  <div>
    <v-container>
      <AppBuilder :app="app" ref="appBuilder"/>
    </v-container>
  </div>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder"
import http from "@/modules/core-module/services/http"

export default {
  name: "OrgChart",
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
                key: "units",
                id: "1",
                isActive: true,
                name: "Units",
                icon: "fas fa-layer-group",
              },
              {
                key: "roles",
                id: "2",
                name: "Roles",
                icon: "fas fa-briefcase",
              },
              {
                key: "users",
                id: "3",
                name: "Users",
                icon: "fas fa-user",
              },
              {
                key: "chart",
                id: "4",
                name: "Chart",
                icon: "fas fa-project-diagram",
              },
            ],
            sections: [
              {
                key: "title",
                type: "TitleComponet",
                name: "Organizational Chart",
              },
              {
                key: "unitModal",
                type: "ModalSection",
                name: "unitModal",
                isCard: true,
                forms: [
                  {
                    key: "addUnitModal",
                    modalId: "addUnitModal",
                    modalTitle: "Add Unit",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Arabic name",
                        name: "name_ar",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "English name",
                        name: "name_en",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Unit type code",
                        name: "unitTypeCode",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Unit code",
                        name: "unitCode",
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
                      name_ar: "",
                      name_en: "",
                      unitTypeCode: "",
                      unitCode: "",
                      parentCode: ""
                    },
                  },
                  {
                    key: "editUnitModal",
                    modalId: "editUnitModal",
                    modalTitle: "Edit Unit",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Internal code",
                        name: "name",
                        col: "4",
                        readonly: true
                      },
                      {
                        type: "InputComponent",
                        label: "Arabic name",
                        name: "name_ar",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "English name",
                        name: "name_en",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Unit type code",
                        name: "unitTypeCode",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Unit code",
                        name: "unitCode",
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
                      name_ar: "",
                      name_en: "",
                      unitTypeCode: "",
                      unitCode: "",
                      parentCode: "",
                    },
                  },
                ],
              },
              {
                key: "roleModal",
                type: "ModalSection",
                name: "roleModal",
                isCard: true,
                forms: [
                  {
                    key: "addRoleModal",
                    modalId: "addRoleModal",
                    modalTitle: "Add Role",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Arabic name",
                        name: "name_ar",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "English name",
                        name: "name_en",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Role code",
                        name: "groupCode",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Unit code",
                        name: "unitCode",
                        col: "4",
                        rule: "required",
                      },
                      // {
                      //   type: "CheckboxComponent",
                      //   label: "Head role",
                      //   name: "isHeadRole",
                      //   col: "4",
                      //   rule: "required",
                      // },
                      // {
                      //   type: "CheckboxComponent",
                      //   label: "Vice role",
                      //   name: "isViceRole",
                      //   col: "4",
                      //   rule: "required",
                      // },
                    ],
                    model: {
                      "id": "",
                      "name": "",
                      "description": "",
                      "name_en": "",
                      "name_ar": "",
                      "groupCode": "",
                      "isHeadRole": "",
                      "isViceRole": "",
                      "unitCode": ""
                    },
                  },
                  {
                    key: "editRoleModal",
                    modalId: "editRoleModal",
                    modalTitle: "Edit Role",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Arabic name",
                        name: "name_ar",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "English name",
                        name: "name_en",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Role code",
                        name: "groupCode",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Unit code",
                        name: "unitCode",
                        col: "4",
                        rule: "required",
                      },
                      // {
                      //   type: "CheckboxComponent",
                      //   label: "Head role",
                      //   name: "isHeadRole",
                      //   col: "4",
                      //   rule: "required",
                      // },
                      // {
                      //   type: "CheckboxComponent",
                      //   label: "Vice role",
                      //   name: "isViceRole",
                      //   col: "4",
                      //   rule: "required",
                      // },
                    ],
                    model: {
                      "id": "",
                      "name": "",
                      "description": "",
                      "name_en": "",
                      "name_ar": "",
                      "groupCode": "",
                      "isHeadRole": "",
                      "isViceRole": "",
                      "unitCode": "",
                    },
                  },
                ],
              },
              {
                key: "userModal",
                type: "ModalSection",
                name: "userModal",
                isCard: true,
                forms: [
                  {
                    key: "addUserModal",
                    modalId: "addUserModal",
                    modalTitle: "Add User",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Username",
                        name: "username",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Password",
                        name: "password",
                        col: "4",
                        password: true,
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Email",
                        name: "email",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Group code",
                        name: "groupCode",
                        col: "4",
                        rule: "required",
                      },
                    ],
                    model: {
                      "id": "",
                      "username": "",
                      "password": "",
                      "email": "",
                      "groupCode": "",
                    },
                  },
                  {
                    key: "editUserModal",
                    modalId: "editUserModal",
                    modalTitle: "Edit User",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Username",
                        name: "username",
                        col: "4",
                        rule: "required",
                      },
                      // {
                      //   type: "InputComponent",
                      //   label: "Password",
                      //   name: "password",
                      //   col: "4",
                      //   rule: "required",
                      // },
                      {
                        type: "InputComponent",
                        label: "Email",
                        name: "email",
                        col: "4",
                        rule: "required",
                      },
                      {
                        type: "InputComponent",
                        label: "Group code",
                        name: "groupCode",
                        col: "4",
                        rule: "required",
                      },
                    ],
                    model: {
                      "id": "",
                      "username": "",
                      // "password": "",
                      "email": "",
                      "groupCode": "",
                    },
                  },
                ],
              },
              {
                tabId: "1",
                isTab: true,
                type: "DefaultSection",
                isCard: true,
                display: "block",
                isActive: true,
                forms: [
                  {
                    inputs: [
                      {
                        type: "DataTableComponent",
                        name: "unitsTable",
                        subscribe: "units",
                        col: 12,
                        search: true,
                        filter: true,
                        add: true,
                        actions: ["edit", "delete"],
                        modalId: "addUnitModal",
                      },
                    ],
                    model: {
                      unitsTable: {
                        url: "org/unit/read/list",
                        headers: [
                          {
                            text: "Internal code",
                            value: "name",
                          },
                          {
                            text: "Name ar",
                            value: "name_ar",
                          },
                          {
                            text: "Name en",
                            value: "name_en",
                          },
                          {
                            text: "Unit type code",
                            value: "unitTypeCode",
                          },
                          {
                            text: "Unit code",
                            value: "unitCode",
                          },
                          {
                            text: "",
                            value: "action",
                          },
                        ],
                        data: [],
                        search: "",
                      },
                    },
                  },
                ],
              },
              {
                tabId: "2",
                isTab: true,
                type: "DefaultSection",
                isCard: true,
                display: "none",
                forms: [
                  {
                    inputs: [
                      {
                        type: "DataTableComponent",
                        name: "rolesTable",
                        subscribe: "roles",
                        col: 12,
                        search: true,
                        filter: true,
                        add: true,
                        actions: ["edit", "delete"],
                        modalId: "addRoleModal",
                      },
                    ],
                    model: {
                      rolesTable: {
                        url: "org/group/read/list",
                        headers: [
                          {
                            text: "Internal code",
                            value: "name",
                          },
                          {
                            text: "Name ar",
                            value: "name_ar",
                          },
                          {
                            text: "Name en",
                            value: "name_en",
                          },
                          {
                            text: "Role code",
                            value: "groupCode",
                          },
                          {
                            text: "",
                            value: "action",
                          },
                        ],
                        data: [],
                        search: "",
                      },
                    },
                  },
                ],
              },
              {
                tabId: "3",
                isTab: true,
                type: "DefaultSection",
                isCard: true,
                display: "none",
                forms: [
                  {
                    inputs: [
                      {
                        type: "DataTableComponent",
                        name: "usersTable",
                        subscribe: "users",
                        col: 12,
                        search: true,
                        filter: true,
                        add: true,
                        actions: ["edit", "delete"],
                        modalId: "addUserModal",
                      },
                    ],
                    model: {
                      usersTable: {
                        url: "org/user/read/list",
                        headers: [
                          {
                            text: "Username",
                            value: "name",
                          },
                          {
                            text: "Display name",
                            value: "displayName",
                          },
                          {
                            text: "Email",
                            value: "details.email",
                          },
                          {
                            text: "Phone",
                            value: "details.phone",
                          },
                          {
                            text: "",
                            value: "action",
                          },
                        ],
                        data: [],
                        search: "",
                      },
                    },
                  },
                ],
              },
              {
                tabId: "4",
                isTab: true,
                type: "DefaultSection",
                isCard: true,
                display: "none",
                // background: "transparent",
                forms: [
                  {
                    inputs: [
                      {
                        type: "D3GraphComponent",
                        name: "chart",
                        subscribe: "chart",
                        col: 12,
                      },
                    ],
                    model: {
                      chart: {},
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
    }
  },
  methods: {
    handleUnitEvents: function () {
      this.$observable.subscribe("addUnitModal_addModal", (obj) => {
        let item = obj.obj;
        delete item._valid;
        item.name = item.unitCode;
        let parentCode = item.parentCode;
        delete item.parentCode;

        http.post("/org/unit/create", item).then(() => {
          http.put("/org/unit/" + parentCode + "/subUnitWithCode/" + item.name).then(() => {
            alert("Added Successfully");
          }).catch((response) => {
            // alert(response.data.metaInfo.errorMessage);
            alert(response);
          });
        }).catch((response) => {
          // alert(response.data.metaInfo.errorMessage);
          alert(response);
        });
      });
      this.$observable.subscribe("editUnitModal_updateModal", (obj) => {
        let item = obj.obj;
        let id = item.id;
        let parentCode = item.parentCode;

        item = {
          "name": item.name,
          "name_ar": item.name_ar,
          "name_en": item.name_en,
          "unitTypeCode": item.unitTypeCode,
          "unitCode": item.unitCode,
        }

        http.put("/org/unit/update/" + id, item).then(() => {
          http.put("/org/unit/" + parentCode + "/subUnitWithCode/" + item.name).then(() => {
            alert("Updated Successfully");
          }).catch((response) => {
            // alert(response.data.metaInfo.errorMessage);
            alert(response);
          });
        }).catch((response) => {
          // alert(response.data.metaInfo.errorMessage);
          alert(response);
        });
      });
      this.$observable.subscribe("unitsTable_add", () => {
        this.$observable.fire("openModal", {
          modalId: "addUnitModal",
          action: "add",
        })
      });
      this.$observable.subscribe("unitsTable_edit", (data) => {
        data.item.parentCode = (data.item.parent.length > 0) ? data.item.parent[0].name : null;
        this.$refs.appBuilder.setModelData("editUnitModal", data.item)
        this.$observable.fire("openModal", {
          modalId: "editUnitModal",
          action: "edit",
          obj: data.item,
        })
      })
      this.$observable.subscribe("unitsTable_delete", (data) => {
        http.delete("/org/unit/delete/" + data.item.id).then((response) => {
          alert(response.data.metaInfo.infoMessage);
        });
      })
    },
    handleRoleEvents: function () {
      this.$observable.subscribe("addRoleModal_addModal", (obj) => {
        let item = obj.obj;
        let unitCode = item.unitCode;
        item = {
          "name": item.groupCode,
          "name_en": item.name_en,
          "name_ar": item.name_ar,
          "groupCode": item.groupCode,
          // "isHeadRole": item.isHeadRole,
          // "isViceRole": item.isViceRole,
        }
        http.post("/org/group/create", item).then(() => {
          http.put("/org/group/" + item.groupCode + "/relation/unit/add/" + unitCode, {}).then(() => {
            alert("Added Successfully");
          }).catch((response) => {
            console.log(response);
            // alert(response.data.metaInfo.errorMessage);
            alert(response);
          });
        }).catch((response) => {
          // alert(response.data.metaInfo.errorMessage);
          alert(response);
        });
      });
      this.$observable.subscribe("editRoleModal_updateModal", (obj) => {
        let item = obj.obj;
        let id = item.id;
        let unitCode = item.unitCode;
        item = {
          "name": item.groupCode,
          "name_en": item.name_en,
          "name_ar": item.name_ar,
          "groupCode": item.groupCode,
          // "isHeadRole": item.isHeadRole,
          // "isViceRole": item.isViceRole,
        }

        http.put("/org/group/update/" + id, item).then(() => {
          http.put("/org/group/" + item.groupCode + "/relation/unit/add/" + unitCode, {}).then(() => {
            alert("Updated Successfully");
          }).catch((response) => {
            // alert(response.data.metaInfo.errorMessage);
            alert(response);
          });
        }).catch((response) => {
          // alert(response.data.metaInfo.errorMessage);
          alert(response);
        });
      });
      this.$observable.subscribe("rolesTable_add", () => {
        this.$observable.fire("openModal", {
          modalId: "addRoleModal",
          action: "add",
        })
      });
      this.$observable.subscribe("rolesTable_edit", (data) => {
        data.item.unitCode = (data.item.unit !== undefined) ? data.item.unit.name : null;
        this.$refs.appBuilder.setModelData("editRoleModal", data.item)
        this.$observable.fire("openModal", {
          modalId: "editRoleModal",
          action: "edit",
          obj: data.item,
        })
      });
      this.$observable.subscribe("rolesTable_delete", (data) => {
        http.delete("/org/group/delete/" + data.item.id).then((response) => {
          alert(response.data.metaInfo.infoMessage);
        });
      })
    },
    handleUserEvents: function () {
      this.$observable.subscribe("addUserModal_addModal", (obj) => {
        let item = obj.obj;
        let groupCode = item.groupCode;
        item = {
          "username": item.username,
          "password": item.password,
          "email": item.email,
        };
        http.post("/org/user/create", item).then(() => {
          http.put("/org/user/" + item.username + "/assign/group/" + groupCode, {}).then(() => {
            alert("Added Successfully");
          }).catch((response) => {
            console.log(response);
            // alert(response.data.metaInfo.errorMessage);
            alert(response);
          });
        }).catch((response) => {
          // alert(response.data.metaInfo.errorMessage);
          alert(response);
        });
      });
      // TODO: Need to add Consolidation to work
      this.$observable.subscribe("editUserModal_updateModal", (obj) => {
        let item = obj.obj;
        let id = item.id;
        let groupCode = item.groupCode;
        item = {
          "username": item.username,
          "email": item.email,
        };
        http.put("/org/user/update/" + id, item).then(() => {
          http.put("/org/user/" + item.username + "/assign/group/" + groupCode, {}).then(() => {
            alert("Updated Successfully");
          }).catch((response) => {
            console.log(response);
            // alert(response.data.metaInfo.errorMessage);
            alert(response);
          });
        }).catch((response) => {
          // alert(response.data.metaInfo.errorMessage);
          alert(response);
        });
      });
      this.$observable.subscribe("usersTable_add", () => {
        this.$observable.fire("openModal", {
          modalId: "addUserModal",
          action: "add",
        })
      });
      this.$observable.subscribe("usersTable_edit", (data) => {
        data.item = {
          "id": data.item.id,
          "username": data.item.name,
          "email": data.item.details.email,
          "groupCode": (data.item.groups.length > 0)? data.item.groups[0].name: null,
        };
        this.$refs.appBuilder.setModelData("editUserModal", data.item);
        this.$observable.fire("openModal", {
          modalId: "editUserModal",
          action: "edit",
          obj: data.item,
        })
      });
      // TODO: Need to add Consolidation to work
      this.$observable.subscribe("usersTable_delete", (data) => {
        http.delete("/org/user/delete/" + data.item.id).then((response) => {
          alert(response.data.metaInfo.infoMessage);
        });
      })
    },

    generateD3OrgChartJson: function () {
      http.get("/org/unit/read/list").then((response) => {
        console.log(response.data.data)
        let data = response.data.data.map((item) => {
          const width = Math.round(Math.random() * 50 + 300)
          const height = Math.round(Math.random() * 20 + 130)
          const cornerShape = ["ORIGINAL", "ROUNDED", "CIRCLE"][
              Math.round(Math.random() * 2)
              ]
          const nodeImageWidth = 100
          const nodeImageHeight = 100
          const centerTopDistance = 0
          const centerLeftDistance = 0
          const expanded = false

          // const titleMarginLeft = nodeImageWidth / 2 + 20 + centerLeftDistance;
          // const contentMarginLeft = width / 2 + 25;
          return {
            nodeId: item.id,
            parentNodeId: item.parent.length !== 0 ? item.parent[0].id : null,
            width: width,
            height: height,
            borderWidth: 1,
            borderRadius: 15,
            borderColor: {
              red: 15,
              green: 140,
              blue: 121,
              alpha: 0.5,
            },
            backgroundColor: {
              red: 3,
              green: 94,
              blue: 136,
              alpha: 1,
            },
            nodeImage: {
              url: "",
              width: nodeImageWidth,
              height: nodeImageHeight,
              centerTopDistance: centerTopDistance,
              centerLeftDistance: centerLeftDistance,
              cornerShape: cornerShape,
              shadow: false,
              borderWidth: 0,
              borderColor: {
                red: 19,
                green: 123,
                blue: 128,
                alpha: 1,
              },
            },
            template: `<div>
                  <div style="margin-top:10px;
                              font-size:20px;
                              font-weight:bold;
                              text-align: center;
                         ">${item.name_ar} </div>
                 <div style="margin-top:3px;
                              font-size:16px;
                              text-align: center;
                         ">${item.name_en} </div>

                 <div style="margin-top:3px;
                              font-size:14px;
                              text-align: center;
                         ">${item.name}</div>
              </div>`,
            connectorLineColor: {
              red: 2,
              green: 120,
              blue: 174,
              alpha: 1,
            },
            connectorLineWidth: 5,
            dashArray: "",
            expanded: expanded,
          }
        })
        this.$observable.fire("drawD3Graph", data)
      })
    },
  },

  mounted: function () {
    this.handleUnitEvents()
    this.handleRoleEvents();
    this.handleUserEvents();
    this.generateD3OrgChartJson()
  },
}
</script>
