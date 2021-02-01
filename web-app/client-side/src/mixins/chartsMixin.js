import http from "../modules/core-module/services/http";

export default {
    methods: {

        async getDynamicReport(data) {
            try {
                var response = await http.post("/dynamic/report/get/", JSON.stringify(data));
                 console.log(response.data.data);
                return response.data.data;

            }
            catch (error) {
                console.log(error);
            }

        }
    }
}