import http from "../modules/core-module/services/http";

export default {
    methods: {
        loadForm: function (formName, callBack) {
            http.get("/user/form/" + formName)
                .then((response) => {
                    this.$refs.appBuilder.setAppData(response.data.data.app);
                    if (callBack) {
                        callBack();
                    }
                })
                .catch((error) => console.error(error));
        },
        initiateProcess: function (data) {
            http.post("/process/initiate", data)
                .then((response) => {
                    console.log(response);
                    alert("Initiate Complete!");
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
        readEntity: function (entityName, entityId) {
            return http.get('/entity/read?entityName=' + entityName + '&entityId=' + entityId)
        },
        completeStep: function (data) {
            http.post("/process/complete", data)
                .then((response) => {
                    console.log(response);
                    alert("Step Complete!");
                })
                .catch((error) => console.error(error));
        },
        getLookupByCategoryAndKey: async function (category, key) {
            try {
                var response = await http.get("lookup/get/category/" + category + "/key/" + key);
                console.log("getLookupResponse", response.data);
                return response.data.data;
            } catch (error) {
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
        initiateBrava: function () {
            this.$observable.subscribe('open-file-brava', async (fileId) => {
                this.$observable.fire('file-component-skeleton', true)
                console.log("openfilebrava");
                console.log(fileId);
                let userToken;
                try {
                    userToken = await http.post("http://45.240.63.94:8081/otdsws/rest/authentication/credentials", {
                        "userName": "admin",
                        "password": "Asset99a",
                        "ticketType": "OTDSTICKET"
                    });
                    this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
                        'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
                    console.log(userToken);
                    // this.$observable.fire('file-component-skeleton', false)
                } catch (e) {
                    console.log(e);
                }
            });
        }
    }
}
