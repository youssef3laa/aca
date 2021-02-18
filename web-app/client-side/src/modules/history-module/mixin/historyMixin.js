import http from "../../core-module/services/http";

export default {

    methods: {
        async getHistoryByProcessNameAndEntityId(processName, entityId) {
            try {
                var response = await http.get("/history/" + processName + "/" + entityId);
                return response.data.data;
            } catch (error) {
                console.error(error);
            }
        },
        async getHistoryByRequestId(requestId) {
            try {
                var response = await http.get("/history/" + requestId);
                return response.data.data;
            } catch (error) {
                console.error(error);
            }
        },
        async setHistory(data) {
            console.log(data);
            try {
                var response = await http.post("/history/create", data);
                console.log(response.data);
            } catch (err) {
                console.error(err);
            }
        },
        createHistoryTableModel(requestId) {
            return {
                url: "history/"+ requestId,
                headers: [
                    {
                        text: "decision",
                        align: "start",
                        value: "decision",
                    },
                    {
                        text: "name",
                        value: "displayName",
                    },
                    {
                        text: "theEntity",
                        value: "unitName",
                    },

                    {
                        text: "date",
                        value: "approvalDate",
                    },
                ],
                subHeaders:[{
                    text:"comments",
                    value:"comment"
                },
            ],
                key:"id",
                data: []
            }
        },
        createSentHistoryTableModel() {
            return {
                url:"history/user",
                headers: [
                    {
                        text: "processName",
                        value: "processName",
                      },
                      {
                        text: "displayName",
                        value: "displayName",
                      },
                
                    {
                      text: "approvalDate",

                      value: "approvalDate",
                    },
              
                    {
                      text: "unitName",
                      value: "unitName",
                    },
                    {
                        text: "requestNumber",
                        value: "requestNumber",
                      },
                      {
                        text: "",
                        value: "action",
                        sortable: false,
                      }
               
                  ],
                  data: [],
            }
        }
    }
}
