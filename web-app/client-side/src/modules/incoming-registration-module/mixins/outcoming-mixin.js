import http from "../../core-module/services/http";
import router from "../../../router";

export default {
    methods: {
        getOutcoming: async function(){
            if(localStorage.getItem("outcomingId")){
                let response = await http.get('/outcoming/read/'+ localStorage.getItem("outcomingId"))
                console.log("Outcoming Response: ", response)
                return response.data.data
            }else{
                try{
                    let response = await http.get('/outcoming/create/temp')
                    console.log("Outcoming Response: ", response)
                    localStorage.setItem("outcomingId",response.data.data.id)
                    return response.data.data
                } catch (error) {
                    console.error(error);
                }
            }
        },
        completeOutcomingStep: function(data){
            http.post("/outcoming/complete", data)
                .then((response) => {
                    console.log(response);
                    localStorage.removeItem("outcomingId")
                    // // alert("Step Complete!");
                    router.push({name: 'HomePage'})
                })
                .catch((error) => console.error(error));
        },
        readOutcoming: async function (outcomingId) {
            try{
                let response = await http.get('/outcoming/read/'+outcomingId)
                console.log("Outcoming Response: ", response)
                return response.data.data
            } catch (error) {
                console.error(error);
            }
        }
    }
}