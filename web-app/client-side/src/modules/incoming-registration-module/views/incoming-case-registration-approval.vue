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
    import opinionsMixin from "../../../mixins/opinionsMixin";

    export default {
        name: "incoming-case-registration-approval",
        mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, opinionsMixin],
        components: {AppBuilder},
        data() {
            return {
                taskId: this.$route.params.taskId,
                request: {},
                inputSchema: {},
                app: {}
            }
        },
        async created(){
            this.claimTask(this.taskId)
            let taskData = await this.getTaskData(this.taskId)
            this.inputSchema = taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment
            this.loadForm(this.inputSchema.config, this.formLoaded)
            this.request = await this.readRequest(this.inputSchema.requestId)
            this.$observable.subscribe("complete-step", this.submit)
        },
        methods: {
            formLoaded: async function(){
                this.$refs.appBuilder.disableSection("mainData");
                this.$refs.appBuilder.disableSection("caseData");
                this.$refs.appBuilder.setModelData("approvalForm", {approval: this.inputSchema.router})
                this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.inputSchema.requestId)})
                this.$refs.appBuilder.setModelData("opinionsTable", {opinionsTable: this.createOpinionTableModel(this.inputSchema.requestId)})
                this.$refs.appBuilder.setModelData("memorandumForm", {memorandum: {requestId: this.inputSchema.requestId}})

                let incomingRegistration = await this.readIncomingRegistration(this.inputSchema.entityId)
                this.$refs.appBuilder.setModelData("mainData", incomingRegistration)
                let incomingCase = await this.readIncomingCase(incomingRegistration.jobEntityId)
                this.$refs.appBuilder.setModelData("caseData", incomingCase)
            },
            submit: function(){
                let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");

                let assignedCN = approvalCard.approval.assignedCN
                if(this.inputSchema.stepId == "headOfAgencyApproval" && approvalCard.approval.decision == "approve"){
                    assignedCN = this.request.initiator
                }

                this.completeStep({
                    taskId: this.taskId,
                    stepId: this.inputSchema.stepId,
                    process: this.inputSchema.process,
                    parentHistoryId: this.inputSchema.parentHistoryId,

                    code: approvalCard.approval.code,
                    assignedCN: assignedCN,
                    decision: approvalCard.approval.decision,
                    comment: approvalCard.approval.comment,
                    opinion: approvalCard.approval.opinion,
                    receiverType: approvalCard.approval.receiverType
                })
            }
        }
    }
</script>