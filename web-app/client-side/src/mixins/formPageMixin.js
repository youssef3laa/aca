import http from "../modules/core-module/services/http";
import router from "../router";

export default {
    methods: {
        loadForm: function (formName,callBack) {
            http.get("/user/form/"+formName)
                .then((response) => {
                    this.$refs.appBuilder.setAppData(response.data.data.app);
                    if(callBack){
                        callBack();
                    }
                })
                .catch((error) => console.error(error));
        },
        claimTask: function (taskId) {
            http.post('/workflow/task/claim', taskId)
                .then((response) => {
                    console.log("ClaimTask Response:", response);
                })
                .catch((error) => {
                    console.error(error)
                })
        },
        getTaskData: async function (taskId) {
            try{
                let response = await http.get('/workflow/task/data?taskId=' + taskId);
                console.log("TaskData Response: ", response);
                return response.data.data.Body.GetTaskResponse.tuple.old.Task;
            } catch (error) {
                console.error(error);
            }
        },
        createRequest: async function(){
            if(localStorage.getItem("requestId")){
                return localStorage.getItem("requestId")
            }else{
                try{
                    let response = await http.get('/request/create/temp');
                    console.log("Request Response: ", response);
                    localStorage.setItem("requestId",response.data.data.id)
                    return response.data.data.id;
                } catch (error) {
                    console.error(error);
                }
            }
        },
        readEntity: async function(entityName, entityId) {
            try{
                let response = await http.get('/entity/read?entityName='+ entityName +'&entityId='+entityId);
                console.log("EntityData Response: ", response);
                return response.data.data;
            } catch (error) {
                console.error(error);
            }
        },
        getLookupByCategoryAndKey: async function(category,key){
            try {
                var response = await http.get("lookup/get/category/" + category + "/key/" + key);
                console.log("getLookupResponse",response.data);
                return response.data.data;
            }
            catch (error) {
                console.log(error);
            }
        },
        getLookupByCategory: async function (category) {
            try {
                let response = await http.get("lookup/get/category/" + category);
                console.log("getLookupByCategoryResponse", response.data);
                return response.data.data;
            } catch (error) {
                console.log(error);
            }
        },
        checkParallelTasksFinished: async function(requestId){
            try {
                let response = await http.get("parallel/finished/" + requestId);
                console.log("Parallel Tasks Finished", response.data);
                return response.data.data;
            } catch (error) {
                console.log(error);
            }
        },
        initiateProcess: function (data){
            http.post("/process/initiate", data)
                .then((response) => {
                    console.log(response);
                    localStorage.removeItem("requestId")
                    // alert("Initiate Complete!");
                    router.push({name: 'HomePage'})
                })
                .catch((error) => console.error(error));
        },
        completeStep: function(data){
            http.post("/process/complete", data)
                .then((response) => {
                    console.log(response);
                    // alert("Step Complete!");
                    router.push({name: 'HomePage'})
                })
                .catch((error) => console.error(error));
        }
    }
}