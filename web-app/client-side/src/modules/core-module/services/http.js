import axios from 'axios'
import config from '../../../config/config'
import Vue from 'vue'

const REQUEST = axios.create({
  baseURL: config.API_URL,
  headers: {
    'Content-Type': 'application/json',
    'X-TenantID': 'TenantOne',
  },
})

export default {
  addHeader(key, value) {
    REQUEST.defaults.headers[key] = value
  },
  post(url, data) {
    if (Vue.prototype.$user && Vue.prototype.$user.getSAMLart()) {
      REQUEST.defaults.headers['X-Auth-Token'] = Vue.prototype.$user.getSAMLart()
    }

    return REQUEST.post(url, data, {
      transformResponse: [
        function(response) {
          if (response.data) return JSON.parse(response.data)
          return JSON.parse(response)
        },
      ],
    })
  },

  get(url) {
    console.log(Vue.prototype.$user)
    if (Vue.prototype.$user && Vue.prototype.$user.getSAMLart()) {
      REQUEST.defaults.headers['X-Auth-Token'] = Vue.prototype.$user.getSAMLart()
    }
    return REQUEST.get(url)
  },

  handleError(error) {
    var res = () => (error.data ? error.data : error.response)

    if (!res()) return 'CANNOT_CONNECT_TO_SERVER'

    return res().status == 400
      ? 'NOT_FOUND'
      : res().data
      ? res().data.error.message
      : res().status
  },

  getAll() {
    /**@TODO check if logged in  */
    return REQUEST.get('customers', {
      transformResponse: [
        function(data) {
          return JSON.parse(data)
        },
      ],
    })
  },
}
