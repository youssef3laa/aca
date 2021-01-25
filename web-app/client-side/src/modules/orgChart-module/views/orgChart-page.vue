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
                key: 'units',
                id: '1',
                isActive: true,
                name: 'Units',
                icon: 'fas fa-layer-group',
              },
              {
                key: 'roles',
                id: '2',
                name: 'Roles',
                icon: 'fas fa-briefcase',
              },
              {
                key: 'users',
                id: '3',
                name: 'Users',
                icon: 'fas fa-user',
              },
              {
                key: 'chart',
                id: '4',
                name: 'Chart',
                icon: 'fas fa-project-diagram',
              },
            ],
            sections: [
              {
                key: 'title',
                type: 'TitleComponet',
                name: 'Organizational Chart',
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
                        type: "TableComponent",
                        name: "unitsTable",
                        subscribe: "units",
                        col: 12,
                      },
                    ],
                    model: {
                      unitsTable: {
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
                          // {
                          //   text: "Actions",
                          //   value: "actions",
                          //   sortable: false,
                          // },
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
                        type: "TableComponent",
                        name: "rolesTable",
                        subscribe: "roles",
                        col: 12,
                      },
                    ],
                    model: {
                      rolesTable: {
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
                          // {
                          //   text: "Actions",
                          //   value: "actions",
                          //   sortable: false,
                          // },
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
                        type: "TableComponent",
                        name: "usersTable",
                        subscribe: "users",
                        col: 12,
                      },
                    ],
                    model: {
                      usersTable: {
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
                          // {
                          //   text: "Actions",
                          //   value: "actions",
                          //   sortable: false,
                          // },
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
              },
            ],
          },
        ],
      },
    };
  },
  methods: {
    getUnits: function () {
      http.post("org/unit/read/list").then((response) => {
        this.$observable.fire("units", {
          type: "modelUpdate",
          model: response.data,
        });
      });
    },
    getRoles: function () {
      http.post("org/group/read/list").then((response) => {
        this.$observable.fire("roles", {
          type: "modelUpdate",
          model: response.data,
        });
      });
    },
    getUsers: function () {
      http.post("org/user/read/list").then((response) => {
        this.$observable.fire("users", {
          type: "modelUpdate",
          model: response.data,
        });
      });
    },
  },

  mounted: function () {
    this.getUnits();
    this.getRoles();
    this.getUsers();
  },
};
</script>
