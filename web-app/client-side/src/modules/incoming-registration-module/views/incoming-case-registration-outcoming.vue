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
                issueType: "",
                outcoming: {}
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
                this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.inputSchema.process, this.inputSchema.entityId)})
                this.$refs.appBuilder.setModelData("opinionsTable", {opinionsTable: this.createOpinionTableModel(this.inputSchema.requestId)})
                this.$refs.appBuilder.setModelData("memorandumForm", {memorandum: {requestId: this.inputSchema.requestId}})
                this.$refs.appBuilder.setModelData("signatureForm", {signature: {requestId: this.inputSchema.requestId}})

                let incomingRegistration = await this.readIncomingRegistration(this.inputSchema.entityId)
                this.$refs.appBuilder.setModelData("mainData", incomingRegistration)
                let incomingCase = await this.readIncomingCase(incomingRegistration.jobEntityId)
                this.$refs.appBuilder.setModelData("caseData", incomingCase)

                this.outcoming = await this.getOutcoming()
                this.$refs.appBuilder.setModelData("outcomingData",{
                    outcomingNumber: this.outcoming.outcomingNumber,
                    outcomingDate: new Date().toISOString().split("T")[0]
                })
            },
            submit: function(){
                let outcomingData = this.$refs.appBuilder.getModelData("outcomingData");
                outcomingData.outcomingType = outcomingData.outcomingType.value.value
                let outcomingIssueData = this.$refs.appBuilder.getModelData("outcomingIssueForm");
                outcomingIssueData.outcomingIssueType = outcomingIssueData.outcomingIssueType.value.value
                outcomingIssueData.receivingAdministration = (outcomingIssueData.receivingAdministration.value)?outcomingIssueData.receivingAdministration.value.text: null

                let outcoming = {id: this.outcoming.id,incomingIds: this.inputSchema.requestNumber}
                for(let key in outcomingData){
                    outcoming[key] = outcomingData[key]
                }
                for(let key in outcomingIssueData){
                    outcoming[key] = outcomingIssueData[key]
                }

                let outputSchema = {
                    taskId: this.taskId,
                    stepId: this.inputSchema.stepId,
                    process: this.inputSchema.process,
                    parentHistoryId: this.inputSchema.parentHistoryId,

                    assignedCN: "cn=HCOC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com",
                    decision: "send"
                }

                this.completeOutcomingStep({
                    outcoming: outcoming,
                    outputSchema: outputSchema,
                    registrationId: this.inputSchema.entityId
                })
            }
        }
    }
</script>