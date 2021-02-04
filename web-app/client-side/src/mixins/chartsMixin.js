import http from "../modules/core-module/services/http";

export default {
    methods: {

        async getDynamicReport(data) {
            try {
                var response = await http.post("/dynamic/report/get/", JSON.stringify(data));
                console.log(response.data.data);
                return response.data.data;

            }
            catch (error) {
                console.log(error);
            }

        },

        getRichtextCountBody() {
            let requestData = {
                table: "memoValues",
                aggregations: [
                    {
                        function: "count",
                        column: "jsonKey",
                    },
                ],
                columns: ["jsonKey"],
                groupBy: ["jsonKey"],
            };
            return requestData;
        },

        getProcessNameCountBody() {
            let requestData = {
                "table": "ApprovalHistory",
                "aggregations": [
                    {
                        "function": "count",
                        "column": "processName"
                    }
                ],
                "columns": [
                    "processName"
                ],
                "groupBy": [
                    "processName"
                ],
                "where": [
                    {
                        "or": [
                            {
                                "type": "equal",
                                "column": "processName",
                                "value": "generalProcess"
                            },
                            {
                                "type": "equal",
                                "column": "processName",
                                "value": "process-1"
                            }
                        ]
                    }
                ]
            };
            return requestData;
        },

    }
}