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
import signatureMixin from "../../graphs-module/mixin/signatureMixin";

export default {
    name: "incomingCaseRegistration-signature-enter",
    mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, opinionsMixin, signatureMixin],
    components: {AppBuilder},
    data() {
        return {
            taskId: this.$route.params.taskId,
            inputSchema: {},
            app: {}
        }
    },
    async created() {
        let taskData = await this.getTaskData(this.taskId)
        this.inputSchema = taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment
        await this.claimTask(this.taskId, this.inputSchema.requestId)
        this.loadForm(this.inputSchema.config, this.formLoaded)
        this.$observable.subscribe("complete-step", async () => {
            let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");
            let assignedCN, decision = approvalCard.approval.decision, caseType;
            if (this.inputSchema.stepId === "viceSecretaryAgain") {
                assignedCN = "cn=HTVS,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                let signatureFormModelData = this.$refs.appBuilder.getModelData("signatureForm");
                console.log(signatureFormModelData);
                await this.updateSignature({
                    id: this.inputSchema.extraData.signatureEntityId,
                    viceOrHead: 2,
                    signatureTxt: signatureFormModelData.signature.signatureTxt
                }, false)
                caseType = "sentFromCertification";
            } else if (this.inputSchema.stepId === "presidentSecretaryAgain") {
                assignedCN = "cn=HTCS,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                let signatureFormModelData = this.$refs.appBuilder.getModelData("signatureForm");
                console.log(signatureFormModelData);
                await this.updateSignature({
                    id: this.inputSchema.extraData.signatureEntityId,
                    viceOrHead: 1,
                    signatureTxt: signatureFormModelData.signature.signatureTxt
                }, false);
                caseType="sentFromCertification";
            }
            console.log(assignedCN, decision);
            this.completeStep({
                taskId: this.taskId,
                caseType: caseType,
                requestId: this.inputSchema.requestId,
                stepId: this.inputSchema.stepId,
                process: this.inputSchema.process,
                parentHistoryId: this.inputSchema.parentHistoryId,
                code: approvalCard.approval.selected.code,
                assignedCN: assignedCN,
                decision: decision,
                comment: approvalCard.approval.inputs.comment,
                opinion: approvalCard.approval.inputs.opinion,
                receiverType: approvalCard.approval.selected.receiverType
            })
        })
    },
    methods: {
        formLoaded: async function () {
            if (this.inputSchema.stepId === "presidentSecretary") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "complete"
                    ]
                })
            } else if (this.inputSchema.stepId === "headOfGRPPresident") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "sendBackToAdministration",
                        "saveTemporarily",
                        "sendForConfirmation"
                    ]
                })
            } else if (this.inputSchema.stepId === "vicePresident") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "doneViewing"
                    ]
                })
            } else if (this.inputSchema.stepId === "viceSecretaryAgain") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "complete"
                    ]
                })

                console.log({
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 2,
                        requestId: this.inputSchema.requestId,
                        readonly: true,
                        enterTxt: true
                    }
                });
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 2,
                        requestId: this.inputSchema.requestId,
                        readonly: true,
                        signatureTxt: "",
                        enterTxt: true
                    }
                })
            } else if (this.inputSchema.stepId === "presidentSecretaryAgain") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "complete"
                    ]
                })
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 1,
                        requestId: this.inputSchema.requestId,
                        readonly: true,
                        signatureTxt: "",
                        enterTxt: true
                    }
                })
            }

            this.$refs.appBuilder.disableSection("mainData");
            this.$refs.appBuilder.disableSection("caseData");
            this.$refs.appBuilder.setModelData("approvalForm", {approval: this.inputSchema.router})
            this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.inputSchema.requestId)})
            this.$refs.appBuilder.setModelData("opinionsTable", {opinionsTable: this.createOpinionTableModel(this.inputSchema.requestId)})
            this.$refs.appBuilder.setModelData("memorandumForm", {memorandum: {requestId: this.inputSchema.requestId}})
            // this.$refs.appBuilder.setModelData("signatureForm", {signature: {requestId: this.inputSchema.requestId}})

            let incomingRegistration = await this.readIncomingRegistration(this.inputSchema.entityId)
            this.$refs.appBuilder.setModelData("mainData", incomingRegistration)
            let incomingCase = await this.readIncomingCase(incomingRegistration.jobEntityId)
            this.$refs.appBuilder.setModelData("caseData", incomingCase)
        },

    }
}
</script>

<style scoped>

</style>
