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

function resetHeaders() {
    REQUEST.defaults.headers = {
        'Content-Type': 'application/json',
        'X-TenantID': 'TenantOne'
    }
}

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
                function (response) {
                    resetHeaders();
                    if (response.data) return JSON.parse(response.data)
                    return JSON.parse(response)
                },
            ],
        })
    },
    delete(url) {
        if (Vue.prototype.$user && Vue.prototype.$user.getSAMLart()) {
            REQUEST.defaults.headers['X-Auth-Token'] = Vue.prototype.$user.getSAMLart()
        }

        return REQUEST.delete(url, {
            transformResponse: [
                function (response) {
                    resetHeaders();
                    if (response.data) return JSON.parse(response.data)
                    return JSON.parse(response)
                },
            ],
        })
    },

    get(url, config) {
        if (Vue.prototype.$user && Vue.prototype.$user.getSAMLart()) {
            REQUEST.defaults.headers['X-Auth-Token'] = Vue.prototype.$user.getSAMLart()
        }
        return REQUEST.get(url, config)

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
                function (data) {
                    return JSON.parse(data)
                },
            ],
        })
    },
}
