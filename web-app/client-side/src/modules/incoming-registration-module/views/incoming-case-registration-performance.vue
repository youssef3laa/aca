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
    import outcomingMixin from "../mixins/outcoming-mixin";
    import opinionsMixin from "../../../mixins/opinionsMixin";

    export default {
        name: "incoming-case-registration-outcoming",
        mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, opinionsMixin, outcomingMixin],
        components: {AppBuilder},
        data() {
            return {
                taskId: this.$route.params.taskId,
                inputSchema: {},
                app: {},
                issueType: ""
            }
        },
        async created(){
            this.claimTask(this.taskId)
            let taskData = await this.getTaskData(this.taskId)
            this.inputSchema = taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment
            this.loadForm(this.inputSchema.config, this.formLoaded)
            this.$observable.subscribe("complete-step", this.submit)
            // this.$observable.subscribe("outcomingIssueChange", (data) => {
            //     if(data.model.outcomingIssueType.value){
            //         let send = false
            //         let delivery = false
            //         if(data.model.outcomingIssueType.value == "1" && this.issueType != "delivery"){
            //             this.issueType = "delivery"
            //             delivery = true
            //         }else if(data.model.outcomingIssueType.value == "2" && this.issueType != "send"){
            //             this.issueType = "send"
            //             send = true
            //         }
            //         this.$refs.appBuilder.setModelData("outcomingIssueForm", {send: send, delivery: delivery})
            //     }
            // })
        },
        methods: {
            formLoaded: async function(){
                this.$refs.appBuilder.disableSection("mainData");
                this.$refs.appBuilder.disableSection("caseData");
                this.$refs.appBuilder.disableSection("outcomingData");
                this.$refs.appBuilder.disableSection("outcomingIssueForm");
                this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.inputSchema.process, this.inputSchema.entityId)})
                this.$refs.appBuilder.setModelData("opinionsTable", {opinionsTable: this.createOpinionTableModel(this.inputSchema.requestId)})
                this.$refs.appBuilder.setModelData("memorandumForm", {memorandum: {requestId: this.inputSchema.requestId}})
                this.$refs.appBuilder.setModelData("signatureForm", {signature: {requestId: this.inputSchema.requestId}})

                let incomingRegistration = await this.readIncomingRegistration(this.inputSchema.entityId)
                this.$refs.appBuilder.setModelData("mainData", incomingRegistration)
                let incomingCase = await this.readIncomingCase(incomingRegistration.jobEntityId)
                this.$refs.appBuilder.setModelData("caseData", incomingCase)
                let outcomingData = await this.readOutcoming(incomingRegistration.outcomingId)
                this.$refs.appBuilder.setModelData("outcomingData", outcomingData)
                this.$refs.appBuilder.setModelData("outcomingIssueForm", outcomingData)

            },
            submit: function(){
                let approvalForm = this.$refs.appBuilder.getModelData("saveProcessForm");

                console.log("SaveProcess", approvalForm)

                this.completeStep({
                    taskId: this.taskId,
                    stepId: this.inputSchema.stepId,
                    process: this.inputSchema.process,
                    parentHistoryId: this.inputSchema.parentHistoryId,

                    assignedCN: "",
                    decision: approvalForm.approval.decision
                })
            }
        }
    }
</script>