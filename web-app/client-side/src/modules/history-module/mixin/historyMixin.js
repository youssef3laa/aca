import http from "../../core-module/services/http";

export default {

    methods:{
        async getHistoryByProcessNameAndEntityId(processName, entityId){
            try{
                var response  =  await http.get("/history/"+processName+"/"+entityId);
                return response.data.json();
            }
            catch(error){
                console.error(error);
            }
        },
        async setHistory(data){
            try{
                var response = await http.post("/history",data);
                console.log(response.data);
            }
            catch (err){
                console.error(err);
            }
        }
}
}