 import http from "../../core-module/services/http";

export default {

    methods:{
        async getRequestData(requestId){
            try{
                let response = await http.get('/request/read/'+ requestId)

                return response.data.data;
            }
            catch(error){
                console.error(error);
            }
        },


}
}