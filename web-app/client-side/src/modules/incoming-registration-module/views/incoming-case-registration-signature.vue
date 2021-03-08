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
import http from "../../core-module/services/http";
import orgChartMixin from "../../../mixins/orgChartMixin";

export default {
    name: "incoming-case-registration-signature",
    mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, opinionsMixin, orgChartMixin],
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
        this.$observable.subscribe("complete-step", this.submit)
        this.$observable.subscribe("save-temporarily", () => {
            console.log("save temporarily");
        })
        this.$observable.subscribe("send-back-to-administration", () => {
            console.log("send-back-to-administration");

        })
        this.$observable.subscribe("send-back-to-president", () => {
            console.log("send-back-to-president");

        })
        this.$observable.subscribe("send-to-other-administration", () => {
            console.log("send-to-other-administration");

        })
        this.$observable.subscribe("send-to-technical-office", () => {
            let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");
            let assignedCN, decision = approvalCard.approval.decision;
            if (this.inputSchema.stepId === "headOfSECPresidentAgain") {
                assignedCN = "cn=HTCA,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                decision = "sendToGRPPresident";
            }
            this.completeStep({
                taskId: this.taskId,
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
        this.$observable.subscribe("send-for-confirmation", () => {
            let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");
            let shouldCompleteStep = true;
            // let  decision = approvalCard.approval.decision;
            let objectToCompleteStepBy = {
                taskId: this.taskId,
                requestId: this.inputSchema.requestId,
                stepId: this.inputSchema.stepId,
                process: this.inputSchema.process,
                parentHistoryId: this.inputSchema.parentHistoryId,
                code: approvalCard.approval.selected.code,
                // assignedCN: assignedCN,
                decision: approvalCard.approval.decision,
                comment: approvalCard.approval.inputs.comment,
                opinion: approvalCard.approval.inputs.opinion,
                extraData: this.inputSchema.extraData,
                receiverType: approvalCard.approval.selected.receiverType
            };
            if (this.inputSchema.stepId === "presidentSecretary") {
                objectToCompleteStepBy.assignedCN = "cn=HTCA,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
            } else if (this.inputSchema.stepId === "headOfGRPPresident") {
                objectToCompleteStepBy.assignedCN = "cn=HTCS,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                objectToCompleteStepBy.decision = "submitForConfirmation";
            } else if (this.inputSchema.stepId === "vicePresident") {
                shouldCompleteStep = false;
                objectToCompleteStepBy.assignedCN = "cn=SVCC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";

                this.validateAndSaveSignature(({status, signatureEntityId}) => {
                    if (!status) {
                        this.$refs.alertComponent._alertSuccess({
                            type: "error",
                            message: 'pleaseEnterSignature',
                        })
                        return;
                    }
                    if (objectToCompleteStepBy.extraData == undefined || objectToCompleteStepBy.extraData == null)
                        objectToCompleteStepBy.extraData = {signatureEntityId};
                    else {
                        objectToCompleteStepBy.extraData.signatureEntityId = signatureEntityId;
                    }
                    console.log(objectToCompleteStepBy);
                    this.completeStep(objectToCompleteStepBy)
                })
            } else if (this.inputSchema.stepId === "headOfSECPresident") {
                objectToCompleteStepBy.assignedCN = "cn=HCOC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                objectToCompleteStepBy.decision = "sendToPresident";
            } else if (this.inputSchema.stepId === "president") {
                shouldCompleteStep = false;
                objectToCompleteStepBy.assignedCN = "cn=SCOC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                this.validateAndSaveSignature(({status}) => {
                    if (!status) {
                        this.$refs.alertComponent._alertSuccess({
                            type: "error",
                            message: 'pleaseEnterSignature',
                        })
                        return;
                    }
                    this.completeStep(objectToCompleteStepBy)
                })
            }
            if (shouldCompleteStep) {
                this.completeStep(objectToCompleteStepBy)
            }

        })
    },
    methods: {
        validateAndSaveSignature: function (callback) {
            this.$observable.fire("save-signature", {
                callback: ({status, signatureEntityId}) => {
                    if (callback) callback({status, signatureEntityId});
                },
                signatureEntityId: this.inputSchema?.extraData?.signatureEntityId
            })
        },
        formLoaded: async function () {
            if (this.inputSchema.stepId === "presidentSecretary") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "complete"
                    ]
                })

                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 2,
                        readonly: true,
                        pastSignaturesOnly:true,
                        requestId: this.inputSchema.requestId
                    }
                })
            } else if (this.inputSchema.stepId === "headOfGRPPresident") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "sendBackToAdministration",
                        "saveTemporarily",
                        "sendForConfirmation"
                    ]
                })
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 1,
                        readonly: true,
                        pastSignaturesOnly: true,
                        requestId: this.inputSchema.requestId
                    }
                })
            } else if (this.inputSchema.stepId === "vicePresident") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "doneViewing"
                    ]
                })
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 2,
                        requestId: this.inputSchema.requestId
                    }
                })
            } else if (this.inputSchema.stepId === "headOfSECPresident") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "sendBackToAdministration",
                        "sendToChairmanOfCommission"
                    ]
                })
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 1,
                        readonly: true,
                        pastSignaturesOnly: true,
                        requestId: this.inputSchema.requestId
                    }
                })
            } else if (this.inputSchema.stepId === "president") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "doneViewing"
                    ]
                })
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 1,
                        requestId: this.inputSchema.requestId
                    }
                })
            } else if (this.inputSchema.stepId === "headOfSECPresidentAgain") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "sendBackToPresident",
                        "sendToVicePresident",
                        "sendToTheTechnicalOffice"
                    ]
                })
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 1,
                        readonly: true,
                        pastSignaturesOnly: true,
                        requestId: this.inputSchema.requestId
                    }
                })
            } else if (this.inputSchema.stepId === "headOfGRPPresidentAgain") {
                this.$refs.appBuilder.setSectionValue("actionsSection", {
                    "actions": [
                        "sendBackForConfirmation",
                        "saveTemporarily",
                        "complete",
                        "sendToOtherAdministration"
                    ]
                })
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        viceOrHead: 1,
                        readonly: true,
                        pastSignaturesOnly: true,
                        requestId: this.inputSchema.requestId
                    }
                })
            } else {
                this.$refs.appBuilder.setModelData("signatureForm", {
                    signature: {
                        incomingEntityId: this.inputSchema.entityId,
                        readonly: true,
                        pastSignaturesOnly: true,
                        requestId: this.inputSchema.requestId
                    }
                })
            }

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
            this.$refs.appBuilder.setModelData("responsibleEntityForm", incomingRegistration)
        },

        submit: async function () {
            let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");

            let assignedCN, decision = approvalCard.approval.decision;
            if (this.inputSchema.stepId === "presidentSecretary") {
                assignedCN = "cn=HTCA,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
            } else if (this.inputSchema.stepId === "headOfGRPPresidentAgain") {
                let responsibleEntityModel = this.$refs.appBuilder.getModelData("responsibleEntityForm");

                let getGroupByUnitCodesResponse = await this.getHeadRoleByUnitCode(responsibleEntityModel.responsibleEntityEdara);
                assignedCN = "cn=" + getGroupByUnitCodesResponse.groupCode + ",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                // assignedCN = "cn=HCAO,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";

                decision = "sendToResponsibleAdministration"
            } else if (this.inputSchema.stepId === "responsibleAdministrationBeforeOutcoming") {
                let primaryMemberDefaultResponse;
                switch (decision) {
                    case "approve":
                        //goto edaret el ma3lomat far3 el sader
                        assignedCN = "cn=HITS,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                        break;
                    case "requestModification":

                        //goto el 3odw el mas2ol
                        primaryMemberDefaultResponse = await http.get("/primaryMember/default", {
                            params: {
                                incomingRegistrationEntityId: this.inputSchema.entityId
                            }
                        })
                        console.log(primaryMemberDefaultResponse);
                        assignedCN = primaryMemberDefaultResponse.data.data.userCN;
                        break;
                    case "requestRedirection":
                        break;

                }


            }
            console.log(assignedCN, decision);
            this.completeStep({
                taskId: this.taskId,
                requestId: this.inputSchema.requestId,
                stepId: this.inputSchema.stepId,
                process: this.inputSchema.process,
                parentHistoryId: this.inputSchema.parentHistoryId,
                code: approvalCard.approval.selected.code,
                assignedCN: assignedCN,
                decision: decision,
                extraData: this.inputSchema.extraData,
                comment: approvalCard.approval.inputs.comment,
                opinion: approvalCard.approval.inputs.opinion,
                receiverType: approvalCard.approval.selected.receiverType
            })
        }

    }
}
</script>
