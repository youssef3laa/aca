import http from "../../core-module/services/http";
// import router from "../../../router";

export default {
    methods: {
        readIncomingRegistration: async function(id){
            try {
                let response = await http.get("/incoming/registration/read/"+id)
                return response.data.data
            }
            catch (error) {
                console.log(error)
            }
        },
        readIncomingCase: async function(id){
            try {
                let response = await http.get("/incoming/case/read/"+id)
                return response.data.data
            }
            catch (error) {
                console.log(error)
            }
        },
        initiateCaseProcess:function (data) {
            return http.post("/incoming/registration/initiate", data);
        }
    }
}
