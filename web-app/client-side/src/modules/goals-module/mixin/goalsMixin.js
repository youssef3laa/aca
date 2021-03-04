import http from "../../../modules/core-module/services/http"

export default {
    methods: {
        async getAllGoals() {
            try {
                let response = await http.get("goals/get/all")
                return response.data.data
            }
            catch (error) {
                console.log(error)
            }
        },
        async postGoal(data) {
            try {
                let response = await http.post("goals/create",data)
                return response.data
            }
            catch (error) {
                console.log(error)
            }
        },
        async getYears() {
            try {
                let response = await http.get("goals/get/years")
                return response.data.data
            }
            catch (error) {
                console.log(error)
            }
        }, 
        async getGoalsByYear(year){
            try {
                let response = await http.get("goals/get/year",{params:{ year: year }})
                return response.data.data
            }
            catch (error) {
                console.log(error)
            }
        }
    }
}