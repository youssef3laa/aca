import http from "../modules/core-module/services/http"

export default {
    methods: {
        getUserDetails: async function() {
            try {
                let response = await http.get("user/details")
                console.log("UserDetails",response.data.data)
                return response.data.data
            }
            catch (error) {
                console.log(error)
            }
        }
    }
}