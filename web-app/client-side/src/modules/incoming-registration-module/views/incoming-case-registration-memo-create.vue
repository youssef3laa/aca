<template>
    <v-container>
        <AppBuilder ref="appBuilder" :app="app"/>
        <AlertComponent ref="alertComponent"></AlertComponent>
    </v-container>
</template>

<script>
    import AppBuilder from "../../application-builder-module/builders/app-builder";
    import formPageMixin from "../../../mixins/formPageMixin";
    import historyMixin from "../../history-module/mixin/historyMixin";
    import incomingRegistrationMixin from "../mixins/incomig-registration-mixin";
    import http from "../../core-module/services/http";
    import orgChartMixin from "../../../mixins/orgChartMixin";
    import userMixin from "../../../mixins/userMixin";

    export default {
        name: "incoming-case-registration-memo-create",
        mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, orgChartMixin, userMixin],
        components: {AppBuilder},
        data() {
            return {
                taskId: this.$route.params.taskId,
                inputSchema: {},
                app: {}
            }
        },
        async created(){
            this.claimTask(this.taskId)
            let taskData = await this.getTaskData(this.taskId)
            this.inputSchema = taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment
            this.loadForm(this.inputSchema.config, this.formLoaded)
            this.$observable.subscribe("complete-step", this.submit)
            this.$observable.subscribe("searchIncoming", (data) => { this.getRequestEntities(data) })
        },
        methods: {
            getRequestEntities: async function(data) {
                let requestEntities = await http.get("/request/read/forLinkIncoming", {
                    params: {
                        process: this.inputSchema.process,
                        requestDate: new Date().toISOString().split("T")[0],
                        subject: data.model.requestSubject,
                        requestNumber: data.model.requestNumber,
                    },
                });
                console.log(requestEntities);
                let parentDetails = await this.getParentDetails();
                console.log(parentDetails)
                console.log(this.$user);
                let userDetails = await this.getUserDetails();
                console.log(userDetails);

                this.$observable.fire("link", {
                    type: "modelUpdate",
                    model: requestEntities.data
                });

                this.$observable.subscribe("linkingTable_createLink", async (item) => {
                    try {
                        item = item.item;
                        console.log("linkingTable_view", item);
                        let obj = {
                            processModel: {
                                "process": "linkIncoming",
                                "stepId": "init",
                                "entityName": "ACA_Entity_linkIncoming",
                                "decision": "initiate",
                                "assignedCN": parentDetails.cn,
                                "extraData": {
                                    "sourceIncomingId": this.inputSchema.entityId,
                                    "targetIncomingId": item.entityId,
                                    "targetRequestId": item.id,
                                    "sourceRequestId": this.inputSchema.requestId,
                                    "subject": "طلب ربط وارد: " + item.subject,
                                    "initiatorId": userDetails.cn
                                }
                            }
                        }
                        console.log(obj);
                        let response = await http.post("/process/initiateLinkedIncoming", obj);

                        console.log(response, obj);
                    } catch (e) {
                        console.log(e);
                    }
                })
            },
            formLoaded: async function(){
                this.$refs.appBuilder.disableSection("mainData");
                this.$refs.appBuilder.disableSection("caseData");
                this.$refs.appBuilder.setModelData("approvalForm", {approval: this.inputSchema.router})
                this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.inputSchema.requestId)})
                this.$refs.appBuilder.setModelData("memorandumForm", {memorandum: {requestId: this.inputSchema.requestId}})

                let incomingRegistration = await this.readIncomingRegistration(this.inputSchema.entityId)
                this.$refs.appBuilder.setModelData("mainData", incomingRegistration)
                let incomingCase = await this.readIncomingCase(incomingRegistration.jobEntityId)
                this.$refs.appBuilder.setModelData("caseData", incomingCase)
            },
            submit: function(){
                let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");

                this.completeStep({
                    taskId: this.taskId,
                    stepId: this.inputSchema.stepId,
                    process: this.inputSchema.process,
                    parentHistoryId: this.inputSchema.parentHistoryId,

                    code: approvalCard.approval.code,
                    assignedCN: approvalCard.approval.assignedCN,
                    decision: "memorandumCreate",
                    comment: approvalCard.approval.comment,
                    receiverType: approvalCard.approval.receiverType
                })
            }
        }
    }
</script>