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
        async setHistory(data) {
            console.log(data);
            try {
                var response = await http.post("/history/create", data);
                console.log(response.data);
            } catch (err) {
                console.error(err);
            }
        },
        createHistoryTableModel(processName, entityId) {
            return {
                url: "history/"+ processName + "/" + entityId,
                headers: [
                    {
                        text: "القرار",
                        align: "start",
                        value: "decision",
                    },
                    {
                        text: "الاسم",
                        align: "start",
                        value: "userCN",
                    },
                    {
                        text: "التاريخ",
                        value: "approvalDate",
                    },
                ],
                data: []
            }
        },
        createSentHistoryTableModel() {
            return {
                url:"history/user",
                headers: [
                    {
                        text: "عنوان الموضوع",
                        value: "processName",
                      },
                      {
                        text: "المرسل",
                        value: "userCN",
                      },
                
                    {
                      text: "التاريخ",

                      value: "approvalDate",
                    },
              
                    {
                      text: "الجهة",
                      value: "",
                    },
                    {
                        text: "رقم الوارد",
                        value: "processId",
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
