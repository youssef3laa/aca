import http from "../modules/core-module/services/http";

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
        initiateProcess: function (data){
            http.post("/process/initiate", data)
                .then((response) => {
                    console.log(response);
                })
                .catch((error) => console.error(error));
        },
        getTaskData: function (taskId) {
            http.get('/workflow/task/data?taskId=' + taskId)
                .then((response) => {     
                    this.taskData = response.data.data.Body.GetTaskResponse.tuple.old.Task;
                    this.readData();
                })
                .catch((error) => {
                    console.error(error)
                })
        },
        claimTask: function (taskId) {
            http.post('/workflow/task/claim', taskId)
                .then((response) => {
                    console.log(response);
                })
                .catch((error) => {
                    console.error(error)
                })
        },
        readEntity: function(entityName, entityId) {
            return http.get('/entity/read?&entityName='+ entityName +'&entityId='+entityId)      
        },
        completeStep: function(data){
            http.post("/process/complete", data)
            .then((response) => {
                console.log(response);
            })
            .catch((error) => console.error(error));
        }
    }
}