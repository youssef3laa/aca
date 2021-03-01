import http from "../../core-module/services/http";
// import router from "../../../router";

export default {
    methods: {
        readIncomingRegistration: async function (id) {
            try {
                let response = await http.get("/incoming/registration/read/" + id)
                return response.data.data
            } catch (error) {
                console.log(error)
            }
        },
        readIncomingCase: async function (id) {
            try {
                let response = await http.get("/incoming/case/read/" + id)
                return response.data.data
            } catch (error) {
                console.log(error)
            }
        },
        initiateCaseProcess: function (data) {
            return http.post("/incoming/registration/initiate", data);
        },
        completeCaseProcessStep: function (data) {
            this.completeStep({
                taskId: this.taskId,
                requestId: this.inputSchema.requestId,
                stepId: this.inputSchema.stepId,
                process: this.inputSchema.process,
                parentHistoryId: this.inputSchema.parentHistoryId,
                code: data.approvalCard.approval.selected.code,
                assignedCN: data.assignedCN ? data.assignedCN : data.approvalCard.approval.selected.assignedCN,
                decision: data.decision ? data.decision : data.approvalCard.approval.decision,
                comment: data.approvalCard.approval.inputs.comment,
                opinion: data.approvalCard.approval.inputs.opinion,
                receiverType: data.approvalCard.approval.selected.type
            })
        }
    }
}
