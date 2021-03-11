import http from "../modules/core-module/services/http";

export default {
    methods: {

        async setMemoData(data) {
            try {
                var response = await http.post("/memorandum/create", data);
                console.log(response.data);

            }
            catch (error) {
                console.log(error);
            }
        },

        async getMemoData(nodeId) {

            try {
                var response = await http.get("memorandum/get/" + nodeId);
                console.log("getMemoResponse", response.data);
                return response.data.data;
            }
            catch (error) {
                console.log(error);
            }
           
        },

        async getMemoJsonId(nodeId) {

            try {
                var response = await http.get("memorandum/get/" + nodeId);
                console.log("getMemoResponse", response.data);
                return response.data.data.jsonId;
            }
            catch (error) {
                console.log(error);
            }
           
        },
        async getLatestMemo(requestId) {

            try {
                var response = await http.get("memorandum/latest/" + requestId);
                console.log("getLatestMemo", response.data);
                return response.data.data;
            }
            catch (error) {
                console.log(error);
            }
           
        },
    }
}