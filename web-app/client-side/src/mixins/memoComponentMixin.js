import http from "../modules/core-module/services/http";

export default {
    methods: {

        async getMemoAutoCompeleteList() {
            try {
                var response = await http.get("/memo");
                return response.data.data;
            }
            catch (error) {
                console.log(error);
            }
        },
        async getMemoSections(value) {
            try {
                var response = await http.get("/memo/" + value);
                return response.data.data;
            }
            catch (error) {
                console.log(error);
            }
        },
        async setMemoData(data) {
            try {
                var response = await http.post("/memo", data);
                return response;
            }
            catch (error) {
                console.log(error);
            }
        },
        async getMemoData() {
            try {
                // var response = await http.get("/memo");
                // return response.data.data;
                var response = {richtext1:"hi snoopy"};
                return response;
            }
            catch (error) {
                console.log(error);
            }
        }
    }
}