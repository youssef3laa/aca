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
import http from "../../core-module/services/http";

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
        async created() {
            let taskData = await this.getTaskData(this.taskId)
            this.inputSchema = taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment
            console.log(this.inputSchema);
            await this.claimTask(this.taskId, this.inputSchema.requestId)
            this.loadForm(this.inputSchema.config, this.formLoaded)
            this.$observable.subscribe("complete-step", this.submit)
        },
        methods: {
            formLoaded: async function () {
                this.$refs.appBuilder.disableSection("mainData");
                this.$refs.appBuilder.disableSection("caseData");
                this.$refs.appBuilder.disableSection("outcomingData");
                this.$refs.appBuilder.disableSection("outcomingIssueForm");
                this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.inputSchema.requestId)})
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
                this.$refs.appBuilder.setFieldData("outcomingIssueForm", "recipientName", {show: !!(outcomingData.recipientName)})
                this.$refs.appBuilder.setFieldData("outcomingIssueForm", "job", {show: !!(outcomingData.job)})
                this.$refs.appBuilder.setFieldData("outcomingIssueForm", "receivingAdministration", {show: !!(outcomingData.receivingAdministration)})
            },
            submit: async function () {
                let approvalForm = this.$refs.appBuilder.getModelData("saveProcessForm");

                let dataObj = {
                    taskId: this.taskId,
                    requestId: this.inputSchema.requestId,
                    stepId: this.inputSchema.stepId,
                    process: this.inputSchema.process,
                    parentHistoryId: this.inputSchema.parentHistoryId,
                    assignedCN: "",
                    decision: approvalForm.approval.decision
                }
                console.log("SaveProcess", approvalForm)
                try {
                    if (approvalForm.approval.decision === "tempSave") {
                        dataObj.extraData = {
                            pauseDate: new Date().toISOString(),
                            resumeDate: approvalForm.approval.displayDate
                        }
                        let processPauseResponse = await http.post("/process/pause", dataObj);
                        this.$refs.alertComponent._alertSuccess({message: "processHasBeenSavedTemporarily"})
                        console.log(processPauseResponse)
                    } else if (approvalForm.approval.decision === "save") {
                        this.completeStep(dataObj);
                    }
                } catch (e) {
                    console.error(e);
                }
            }
        }
    }
</script>
