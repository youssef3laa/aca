import http from "../../core-module/services/http";
import router from "../../../router";

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
        initiateCaseProcess: function(data){
            http.post("/incoming/registration/case/initiate", data)
                .then((response) => {
                    console.log(response);
                    localStorage.removeItem("requestId")
                    // alert("Initiate Complete!");
                    this.$refs.alertComponent._alertSuccess({message:"initiateSuccess"})
                    router.push({name: 'HomePage'})
                })
                .catch((error) => console.error(error));
        }
    }
}