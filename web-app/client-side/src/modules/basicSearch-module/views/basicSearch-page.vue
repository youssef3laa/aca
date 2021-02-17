<template>
  <div>
    <v-container>
      <AppBuilder :app="app" ref="appBuilder" />
    </v-container>
  </div>
</template>
<script>
import AppBuilder from '../../application-builder-module/builders/app-builder'
// import http from '@/modules/core-module/services/http'
export default {
  name: 'OrgChart',
  components: {
    AppBuilder,
  },
  data() {
    return {
      app: {
        pages: {
          title: {
            key: 'title',
            type: 'TitleComponet',
            name: 'Basic Search',
          },
          tabs: [],
          page: [
            {
              key: 'searchPage',
              sections: {
                key: 'searchSection',
                sec: [
                  {
                    key: 'searchSection',
                    type: 'DefaultSection',
                    name: 'searchForm',
                    isCard: true,
                    forms: [
                      {
                        key: 'searchForm',
                        subscribe: 'search',
                        inputs: [
                          {
                            type: 'InputComponent',
                            label: 'Subject',
                            name: 'subject',
                            col: '4',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Status',
                            name: 'status',
                            col: '4',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Request Number',
                            name: 'requestNumber',
                            col: '4',
                          },
                          {
                            type: 'DatePickerComponent',
                            label: 'Request Date',
                            name: 'requestDate',
                            col: '4',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Process',
                            name: 'process',
                            col: '4',
                          },
                          {
                            type: 'InputComponent',
                            label: 'Initiator',
                            name: 'initiator',
                            col: '4',
                          },
                          {
                            type: 'ButtonComponent',
                            label: 'Search',
                            name: 'searchBtn',
                            col: '4',
                            action: 'search',
                            publish: 'search',
                          },
                        ],
                        model: {
                          subject: '',
                          status: '',
                          requestDate: '',
                          process: '',
                          initiator: '',
                        },
                      },
                      {
                        inputs: [
                          {
                            type: 'DataTableComponent',
                            name: 'basicSearchTable',
                            subscribe: 'basicSearch',
                            col: 12,
                            search: true,
                            filter: true,
                            add: true,
                            actions: ['view'],
                          },
                        ],
                        model: {
                          basicSearchTable: {
                            url: '',
                            headers: [
                              {
                                text: 'subject',
                                value: '',
                              },
                              {
                                text: 'status',
                                value: '',
                              },
                              {
                                text: 'request Number',
                                value: '',
                              },
                              {
                                text: 'reques tDate',
                                value: '',
                              },
                              {
                                text: 'process',
                                value: '',
                              },
                              {
                                text: 'initiator',
                                value: '',
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
}
</script>
