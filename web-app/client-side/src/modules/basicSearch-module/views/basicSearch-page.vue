<template>
  <div>
    <v-container>
      <AppBuilder :app="app" ref="appBuilder" />
    </v-container>
  </div>
</template>
<script>
import AppBuilder from '../../application-builder-module/builders/app-builder'
import http from '../../core-module/services/http'
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
            name: 'basicSearch',
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
                            label: 'subject',
                            name: 'subject',
                            col: '4',
                          },
                          {
                            type: 'AutoCompleteComponent',
                            // label: 'status',
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
                          status: {
                            url: 'lookup/get/category/processStatus',
                          },
                          requestNumber: '',
                          requestDate: '',
                          process: '',
                          initiator: '',
                        },
                      },
                      {
                        key: 'basicSearchTable',
                        inputs: [
                          {
                            type: 'DataTableComponent',
                            name: 'basicSearchTable',
                            subscribe: 'basicSearch',
                            col: 12,
                            search: false,
                            filter: false,
                            add: false,
                            actions: ['view'],
                          },
                        ],
                        model: {
                          basicSearchTable: {
                            url: '',
                            headers: [
                              {
                                text: 'subject',
                                value: 'subject',
                              },
                              {
                                text: 'status',
                                value: 'status',
                              },
                              {
                                text: 'requestNumber',
                                value: 'requestNumber',
                              },
                              {
                                text: 'requestDate',
                                value: 'date',
                              },
                              {
                                text: 'processName',
                                value: 'process',
                              },
                              {
                                text: 'unitName',
                                value: 'unitName',
                              },
                              {
                                text: 'displayName',
                                value: 'displayName',
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
  mounted() {
    this.$observable.subscribe('search', (obj) => {
      let statusQuery = {
        type: 'like',
        column: 'status',
        value: '%%',
      }
      console.log(obj)
      if (obj.model.status.value) {
        if (obj.model.status.value.object.stringKey == 'end') {
          statusQuery = {
            type: 'equal',
            column: 'status',
            value: 'end',
          }
        } else {
          statusQuery = {
            type: 'notEqual',
            column: 'status',
            value: 'end',
          }
        }
      }
      let qBody = {
        table: 'RequestEntity',
        where: [
          {
            and: [
              {
                type: 'like',
                column: 'subject',
                value: '%' + obj.model.subject + '%',
              },
              {
                type: 'greaterThanOrEqualTo',
                column: 'requestDate',
                // value: obj.model.requestDate + ':00:00:00',
                value: '1900-01-01T00:00:00.000',
              },
              {
                type: 'smallerThanOrEqualTo',
                column: 'requestDate',
                // value: obj.model.requestDate + ':23:59:59',
                value: '2200-01-01T23:59:59.000',
              },
              statusQuery,
              {
                type: 'like',
                column: 'requestNumber',
                value: '%' + obj.model.requestNumber + '%',
              },
              {
                type: 'like',
                column: 'process',
                value: '%' + obj.model.process + '%',
              },
            ],
          },
        ],
      }
      console.log(qBody)
      http.post('/dynamic/report/run', qBody).then((response) => {
        console.log(response)
        this.$refs.appBuilder.setModelData('basicSearchTable', {
          basicSearchTable: { data: response.data.data },
        })
      })
    })
  },
}
</script>
