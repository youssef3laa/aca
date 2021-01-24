<template>
  <div>
    <v-row>
      <v-col :cols="7">
        <button style="padding: 5px; margin: 20px;">
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
          label="Search"
          single-line
          hide-details
        ></v-text-field>
      </v-col>
    </v-row>

    <v-data-table
      :headers="d.headers"
      :items="d.data"
      :options.sync="options"
      :server-items-length="totalItems"
      :loading="loading"
      :footer-props="footerProps"
      class="elevation-1"
    ></v-data-table>
  </div>
</template>
<script>
import http from '../../core-module/services/http'
export default {
  name: 'DataTableComponent',
  data() {
    return {
      search: null,
      totalItems: 0,
      d: this.val,
      loading: true,
      footerProps: {
        'items-per-page-options': [5, 10, 25, -1],
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
      }
    },
  },
  methods: {
    getDataFromApi(event) {
      if (!this.d.url) return

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
          this.totalItems = response.data.data.count
          this.d.data = response.data.data.list
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
    console.log(this.d.data)
  },
  props: ['val', 'field'],
}
</script>
