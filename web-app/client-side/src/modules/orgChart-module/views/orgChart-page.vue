<template>
  <div>
    <v-container>
      <AppBuilder :app="app" ref="appBuilder"/>
    </v-container>
  </div>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder"
import http from "@/modules/core-module/services/http";

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
                    key: "editUnitModal",
                    modalId: "unitModal",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Arabic name",
                        name: "name_ar",
                        col: "4",
                      },
                      {
                        type: "InputComponent",
                        label: "English name",
                        name: "name_en",
                        col: "4",
                      },
                      {
                        type: "InputComponent",
                        label: "Unit type code",
                        name: "unitTypeCode",
                        col: "4",
                      },
                      {
                        type: "InputComponent",
                        label: "Unit code",
                        name: "unitCode",
                        col: "4",
                      },
                    ],
                    model: {
                      id: "",
                      name: "",
                      name_ar: "",
                      name_en: "",
                      unitTypeCode: "",
                      unitCode: ""
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
                    key: "editRoleModal",
                    modalId: "roleModal",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "Arabic name",
                        name: "name_ar",
                        col: "4",
                      },
                      {
                        type: "InputComponent",
                        label: "English name",
                        name: "name_en",
                        col: "4",
                      },
                      {
                        type: "InputComponent",
                        label: "Role code",
                        name: "groupCode",
                        col: "4",
                      },
                      {
                        type: "InputComponent",
                        label: "Head role",
                        name: "isHeadRole",
                        col: "4",
                      },
                      {
                        type: "InputComponent",
                        label: "Vice role",
                        name: "isViceRole",
                        col: "4",
                      },
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
                      "cn": "",
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
                    key: "editUserModal",
                    modalId: "userModal",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "User Id",
                        name: "userId",
                        col: "4",
                      },
                      {
                        type: "InputComponent",
                        label: "Email",
                        name: "details.email",
                        col: "4",
                      },
                    ],
                    model: {
                      "id": "",
                      "name": "",
                      "description": "",
                      "userId": "",
                      "displayName": "",
                      "cn": "",
                      "details": {
                        "id": "",
                        "userId": "",
                        "title": "",
                        "firstName": "",
                        "middleName": "",
                        "lastName": "",
                        "email": "",
                        "phone": "",
                        "mobile": "",
                        "notes": ""
                      },
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
                        actions: ["edit", "delete",],
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
                        actions: ["edit", "delete",],
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
                    key: "chart",
                    inputs: [
                      {
                        type: "D3GraphComponent",
                        name: "chart",
                        subscribe: "chart",
                        col: 12,
                      },
                    ],
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
      this.$observable.subscribe("unitsTable_edit", (data) => {
        this.$refs.appBuilder.setModelData("editUnitModal", data.item);
        this.$observable.fire("openModal", {
          modalId: "unitModal",
          obj: data.item,
        });
      });
      this.$observable.subscribe("unitsTable_delete", (data) => {
        http.delete("/org/unit/delete/" + data.item.id).then((response) => {
          alert(response.data.metaInfo.infoMessage);
        });
      });
    },
    // handleRoleEvents: function () {
    //   this.$observable.subscribe("rolesTable_edit", (data) => {
    //     this.$refs.appBuilder.setModelData("editUnitModal", data.item);
    //     this.$observable.fire("openModal", {
    //       modalId: "unitModal",
    //       obj: data.item,
    //     });
    //   });
    //   this.$observable.subscribe("rolesTable_delete", (data) => {
    //     http.delete("/org/unit/delete/" + data.item.id).then((response) => {
    //       alert(response.data.metaInfo.infoMessage);
    //     });
    //   });
    // },

    generateD3OrgChartJson: function () {
      http.get("/org/unit/read/list").then(response => {
        console.log(response.data.data);
        let data = response.data.data.map(item => {
          const width = Math.round(Math.random() * 50 + 300);
          const height = Math.round(Math.random() * 20 + 130);
          const cornerShape = ["ORIGINAL", "ROUNDED", "CIRCLE"][Math.round(Math.random() * 2)];
          const nodeImageWidth = 100;
          const nodeImageHeight = 100;
          const centerTopDistance = 0;
          const centerLeftDistance = 0;
          const expanded = false;

          // const titleMarginLeft = nodeImageWidth / 2 + 20 + centerLeftDistance;
          // const contentMarginLeft = width / 2 + 25;
          return {
            nodeId: item.id,
            parentNodeId: (item.parent.length !== 0) ? item.parent[0].id : null,
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
            expanded: expanded
          }
        });
        this.$observable.fire("drawD3Graph", data);
      });
    },
  },

  mounted: function () {
    this.handleUnitEvents();
    // this.handleRoleEvents();
    this.generateD3OrgChartJson();
  },
}
</script>
