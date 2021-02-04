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
        },
        initiateBrava: function(){
            this.$observable.subscribe('open-file-brava', async ({fileId, verNum}) => {
                this.$observable.fire('file-component-skeleton', true)
                let userToken;
                try {
                  userToken = await http.post("http://45.240.63.94:8081/otdsws/rest/authentication/credentials", {
                    "userName": "admin",
                    "password": "Asset99a",
                    "ticketType": "OTDSTICKET"
                  });
                  if (verNum) {
                    this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
                        'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&vernum=' + verNum + '&OTDSTicket=' + userToken.data.ticket;
                  } else {
                    this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
                        'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
                  }
          
                } catch (e) {
                  console.log(e);
                }
              });
            this.initAttachmentVersionsModal()
        },
        initAttachmentVersionsModal:function(){
            this.$observable.subscribe("openVersionsModal", (file) => {
                this.$observable.fire("versionModal");
                this.$refs.appBuilder.setModelData("fileVersionsModal", {
                  versionGrid: {
                    nodeId: file.properties.id
                  }
                });
              })
        }
    }
}