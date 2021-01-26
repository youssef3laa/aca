<template>
  <div>
    <v-container>
      <AppBuilder :app="app" ref="appBuilder" />
    </v-container>
  </div>
</template>

<script>
import AppBuilder from '../../application-builder-module/builders/app-builder'
// import http from "../../core-module/services/http";

export default {
  name: 'OrgChart',
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
                key: 'unitModal',
                type: 'ModalSection',
                name: 'unitModal',
                isCard: true,
                forms: [
                  {
                    key: 'editUnitModal',
                    modalId: 'unitModal',
                    inputs: [
                      {
                        type: 'InputComponent',
                        label: 'Id',
                        name: 'id',
                        col: '4',
                      },
                    ],
                    model: {
                      id : ''
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
                        name: 'unitsTable',
                        subscribe: 'units',
                        col: 12,
                        search: true,
                        filter: true,
                        add: true,
                        actions: ['edit', 'delete', 'view'],
                      },
                    ],
                    model: {
                      unitsTable: {
                        url: 'org/unit/read/list',
                        headers: [
                          {
                            text: 'Internal code',
                            value: 'name',
                          },
                          {
                            text: 'Name ar',
                            value: 'name_ar',
                          },
                          {
                            text: 'Name en',
                            value: 'name_en',
                          },
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
              {
                tabId: '2',
                isTab: true,
                type: 'DefaultSection',
                isCard: true,
                display: 'none',
                forms: [
                  {
                    inputs: [
                      {
                        type: 'DataTableComponent',
                        name: 'rolesTable',
                        subscribe: 'roles',
                        col: 12,
                        search: true,
                        filter: true,
                        add: true,
                        actions: ['edit', 'delete', 'view'],
                      },
                    ],
                    model: {
                      rolesTable: {
                        url: 'org/group/read/list',
                        headers: [
                          {
                            text: 'Internal code',
                            value: 'name',
                          },
                          {
                            text: 'Name ar',
                            value: 'name_ar',
                          },
                          {
                            text: 'Name en',
                            value: 'name_en',
                          },
                          {
                            text: 'Role code',
                            value: 'groupCode',
                          },
                        ],
                        data: [],
                        search: '',
                      },
                    },
                  },
                ],
              },
              {
                tabId: '3',
                isTab: true,
                type: 'DefaultSection',
                isCard: true,
                display: 'none',
                forms: [
                  {
                    inputs: [
                      {
                        type: 'DataTableComponent',
                        name: 'usersTable',
                        subscribe: 'users',
                        col: 12,
                        search: true,
                        filter: true,
                        add: true,
                        actions: ['edit', 'delete', 'view'],
                      },
                    ],
                    model: {
                      usersTable: {
                        url: 'org/user/read/list',
                        headers: [
                          {
                            text: 'Username',
                            value: 'name',
                          },
                          {
                            text: 'Display name',
                            value: 'displayName',
                          },
                          {
                            text: 'Email',
                            value: 'details.email',
                          },
                          {
                            text: 'Phone',
                            value: 'details.phone',
                          },
                        ],
                        data: [],
                        search: '',
                      },
                    },
                  },
                ],
              },
              {
                tabId: '4',
                isTab: true,
                type: 'DefaultSection',
                isCard: true,
                display: 'none',
                // background: "transparent",
                forms: [
                  {
                    inputs: [
                      {
                        type: 'D3GraphComponent',
                        name: 'chart',
                        subscribe: 'chart',
                        col: 12,
                      },
                    ],
                    model: {
                      chart: {
                        url:
                          'https://gist.githubusercontent.com/bumbeishvili/dc0d47bc95ef359fdc75b63cd65edaf2/raw/c33a3a1ef4ba927e3e92b81600c8c6ada345c64b/orgChart.json',
                      },
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
  methods: {},

  mounted: function() {
    this.$observable.subscribe('unitsTable_view', (data) => {
      console.log(data)
      this.$refs.appBuilder.setModelData("editUnitModal", {
        id: data.item.id,
      });
      this.$observable.fire('openModal', {
        modalId: 'unitModal',
        obj: data,
      })
    })
  },
}
</script>
