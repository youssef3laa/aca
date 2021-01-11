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
        async setHistory(data) {
            console.log(data);
            try {
                var response = await http.post("/history/create", data);
                console.log(response.data);
            }
            catch (err) {
                console.error(err);
            }
        },
        async setMemoData(data) {
            try {
                var response = await http.post("/memorandum/create", data);
                console.log(response.data);
                
            }
            catch (error) {
                console.log(error);
            }
        },
        async getMemoData(memo) {

            try {
                var response = await http.get("memorandum/get/" + memo);
                console.log(response.data);
                return response.data.data;
            }
            catch (error) {
                console.log(error);
            }
            // try {
            //     var response = [
            //         { formKey: "richtext1", model: { richtext1: "Snoopy" } }];
            //     var response2 = [
            //         { formKey: "richtext1", model: { richtext1: "Snoopy" } },
            //         { formKey: "richtext2", model: { richtext2: "bla" } },
            //     ];

            //     return (memo=="memo1") ?  response :  response2;
            // }
            // catch (error) {
            //     console.log(error);
            // }
        }
    }
}