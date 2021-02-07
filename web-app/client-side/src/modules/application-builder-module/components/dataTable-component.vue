<template>
  <div>
    <v-row>
      <v-col :cols="7">
        <button style="padding: 5px; margin: 20px;" @click="handlAddButton()">
          <v-icon color="info">fas fa-plus</v-icon>
          <span> إضافة </span>
        </button>
      </v-col>
      <v-col :cols="1">
        <button style="padding: 5px; margin: 20px;">
          <v-icon>fas fa-filter</v-icon>
        </button>
      </v-col>
      <v-col :cols="4">
        <v-text-field
            v-model="search"
            append-icon="mdi-magnify"
            label="بحث"
            single-line
            hide-details
        ></v-text-field>
      </v-col>
    </v-row>

    <v-data-table
      :headers="d.headers"
      :items="d.data"
      :options.sync="options"
      :server-items-length="(d.url)?totalItems:-1"
      :loading="loading"
      :footer-props="footerProps"
      :search="search"
      item-key="id"
      class="elevation-1"
    >
      <template v-slot:item.action="{ item }">
        <!-- <v-btn
          color="primary"
          dark
        >
          {{item.name_ar}}
        </v-btn> -->

        <v-menu top>
          <template v-slot:activator="{ on, attrs }">
            <v-btn elevation="0" v-bind="attrs" v-on="on">
              <v-icon style="font-size: medium"> fas fa-ellipsis-h </v-icon>
            </v-btn>
          </template>

          <v-list>
            <v-list-item v-for="(i, index) in field.actions" :key="index">
              <v-list-item-title v-on:click="handleAction(item, i)"
                ><span class="dropDown-menu" v-if="i == 'edit'"
                  ><v-icon> far fa-edit </v-icon> {{ i }}</span
                >
                <span class="dropDown-menu" v-else-if="i == 'delete'">
                  <v-icon> far fa-trash-alt </v-icon> {{ i }}</span
                >
                <span class="dropDown-menu" v-else-if="i == 'view'">
                  <v-icon> fas fa-expand-arrows-alt </v-icon> {{ i }}</span
                >
                <span class="dropDown-menu" v-else>
                  <v-icon> {{i.icon}} </v-icon> {{ i.name }}</span
                >
              </v-list-item-title>
            </v-list-item>
          </v-list>
        </v-menu>
      </template>
    </v-data-table>
  </div>
</template>
<script>
import http from '../../core-module/services/http'

export default {
  name: 'DataTableComponent',
  data() {
    return {
      search: '',
      totalItems: this.val.data.length,
      d: this.val,
      loading: true,
      footerProps: {
        'items-per-page-options': [5, 10, 25, -1],
        'show-first-last-page': true
      },
      options: {},
    }
  },
  watch: {
    options: {
      handler(event) {
        this.getDataFromApi(event)
      },
      deep: true,
    },
    val: function(newVal) {
      for (var key in newVal) {
        this.d[key] = newVal[key]
      }
      if (this.d.url) {
        this.getDataFromApi({ page: 1, itemsPerPage: 10 })
      }else{
        this.loading = false
      }
    },
  },
  methods: {
    handlAddButton() {
      console.log(this.field)
      this.$observable.fire(this.field.name + '_add')
    },
    handleAction(item, actionName) {
      if(actionName instanceof Object) actionName = actionName.name
      console.log(this.field.name + '_' + actionName)
      //   console.log(item)
      //   console.log(actionName)

      this.$observable.fire(this.field.name + '_' + actionName, { item })
    },
    getDataFromApi(event) {
      if (!this.d.url) {
        this.loading = false
        return
      }

      this.loading = true
      const page = event.page - 1
      let limit = event.itemsPerPage
      if (limit == -1) {
        limit = this.totalItems
      }
      this.d.params = this.d.params ? this.d.params : {}
      let params = this.d.params
      params.page = page
      params.size = limit
      const URL = this.d.url + '?' + new URLSearchParams(params).toString()
      http
        .get(URL)
        .then((response) => {

          if(response.data.data) {
            this.totalItems = response.data.metaInfo.totalCount
            for (var key in response.data.data) {
              for (var i = 0; i < this.d.headers.length; i++) {
                console.log("dataTable log ", response.data.data[key][this.d.headers[i].value]);
                response.data.data[key][this.d.headers[i].value] = this.$t(response.data.data[key][this.d.headers[i].value])
              }
            }
            this.d.data = response.data.data
          }else{
            this.totalItems = 0
            this.d.data = []
          }
          this.loading = false
        })
        .catch((error) => {
          console.error(error)
        })
    },
  },
  created() {
    if (this.field.subscribe) {
      console.log('subscribe')
      this.$observable.subscribe(this.field.subscribe, (data) => {
        if (data.type == 'modelUpdate') {
          var keys = Object.keys(data.model)
          keys.forEach((key, index) => {
            console.log(index)
            this.d[key] = data.model[key]
          })
        }
        console.log(data)
      })
    }
  },
  mounted() {
    console.log(this.d)
  },
  props: ['val', 'field'],
}
</script>
<style scoped>
.v-btn {
  color: black;
  background: #eaeaea;
  padding: 0 5px !important;
  margin: 0 5px !important;
  height: 20px !important;
  min-width: 0 !important;
}
/* .dropDown-menu {
  background: #96969f !important;
} */
</style>
