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
                        key: 'escalationModal',
                        modalId: 'escalationModal',
                        modalTitle: 'Edit Escalation',
                        inputs: [
                          {
                            type: 'AutoCompleteComponent',
                            label: 'Job type',
                            name: 'jobType',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Duration',
                            name: 'duration',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Extension',
                            name: 'extension',
                            col: '4',
                            rule: 'required',
                          },
                          {
                            type: 'AutoCompleteComponent',
                            label: 'Unit type',
                            name: 'unitType',
                            col: '4',
                            rule: 'required',
                          },
                        ],
                        model: {
                          action : ['escalationTable_edit'],
                          jobType: '',
                          duration: '',
                          extension: '',
                          unitType: ''
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
                                text: 'Unit type Responsible',
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
      this.$observable.subscribe("escalationTable_edit", (obj) => {
        console.log(obj);
        // this.$refs.appBuilder.setModelData(key,obj)
        this.$observable.fire("escalationModal", {
          action: "edit",
        })
      });
    },
  },

  mounted: function () {
    this.handleEvents();
  },
}
</script>
