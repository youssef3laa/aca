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
import orgChartMixin from "../../../mixins/orgChartMixin";

export default {
        name: "incoming-case-registration-approval",
        mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, opinionsMixin,orgChartMixin],
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
            let taskData = await this.getTaskData(this.taskId)
            this.inputSchema = taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment
            await this.claimTask(this.taskId, this.inputSchema.requestId)
            this.loadForm(this.inputSchema.config, this.formLoaded)
            this.request = await this.readRequest(this.inputSchema.requestId)
            this.$observable.subscribe("complete-step", this.submit)

            this.$observable.subscribe("save-temporarily",  ()=> {
                console.log("save temporarily");
            })
            this.$observable.subscribe("send-back-to-administration",  ()=> {
                console.log("send-back-to-administration");
            })
            this.$observable.subscribe("send-back-to-technical-office",  ()=> {
                console.log("send-back-to-technical-office");
            })
            this.$observable.subscribe("send-back-to-vice-president",  ()=> {
                console.log("send-back-to-vice-president");
            })
            this.$observable.subscribe("send-for-confirmation", () => {
                let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");
                let assignedCN, decision;
                if (this.inputSchema.stepId === "presidentSecretary") {
                    assignedCN = "cn=HTCA,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                } else if (this.inputSchema.stepId === "headOfGRPPresident") {
                    assignedCN = "cn=HTCS,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                } else if (this.inputSchema.stepId === "headOfGRPVice") {
                    assignedCN = "cn=HTVS,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                    decision = "submitForConfirmation";
                } else if (this.inputSchema.stepId === "headOfSECVice") {
                    assignedCN = "cn=HVCC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                    decision = "sendToVicePresident";
                } else if (this.inputSchema.stepId === "headOfSCTPViceAgain") {
                    assignedCN = "cn=SCOC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                    decision = "sendToPresident";
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

        },
        methods: {
            formLoaded: async function(){
                //actionsSection
                if (this.inputSchema.stepId === "viceSecretary") {
                    this.$refs.appBuilder.setSectionValue("actionsSection", {
                        "actions": ["complete"

                        ]
                    })
                } else if (this.inputSchema.stepId === "headOfGRPVice") {
                    this.$refs.appBuilder.setSectionValue("actionsSection", {
                        "actions": [
                            // {
                            //     text: "sendBackToAdministration",
                            //     callback: function () {
                            //
                            //     },
                            //     color:"red",
                            //     icon:"fa fa-undo-alt"
                            // },
                            "sendBackToAdministration",
                            "saveTemporarily",
                            "sendForConfirmation"
                        ]
                    })
                } else if (this.inputSchema.stepId === "headOfSECVice") {
                    this.$refs.appBuilder.setSectionValue("actionsSection", {
                        "actions": [
                            "sendBackToTheTechnicalOffice",
                            "sendToVicePresident"
                        ]
                    })
                } else if (this.inputSchema.stepId === "headOfSCTPViceAgain") {
                    this.$refs.appBuilder.setSectionValue("actionsSection", {
                        "actions": [
                            "sendBackToVicePresident",
                            "sendToChairmanOfCommission"
                        ]
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
                this.$refs.appBuilder.setModelData("responsibleEntityForm",incomingRegistration )

            },
            submit: async function () {
                let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");
                console.log(approvalCard);
                let assignedCN = approvalCard.approval.selected.assignedCN
                // if(this.inputSchema.stepId == "headOfAgencyApproval" && approvalCard.approval.decision == "approve"){
                //     assignedCN = this.request.initiator
                // }
                // if(approvalCard.approval?.selected?.type)
                let decision = approvalCard.approval.decision;
                if (this.inputSchema.stepId === "viceSecretary") {
                    assignedCN = "cn=HTVA,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                } else if (decision === "approve") {
                    if (this.inputSchema.stepId === "headOfOfficeApproval") {
                        decision = approvalCard.approval.selected.type;
                        let parentDetailsResponse;
                        switch (decision) {
                            case "headOfSector":
                                parentDetailsResponse = await this.getParentDetails();
                                assignedCN = parentDetailsResponse.cn;
                                break;
                            case "viceHeadOfSector":
                                assignedCN = "";
                                break;
                            case "assistantToHeadOfSector":
                                assignedCN = "";
                                break;
                            default:
                                console.error("invalid choice")
                        }

                    } else if (this.inputSchema.stepId === "headOfSectorApproval") {
                        decision = approvalCard.approval.selected.type;
                        let parentDetailsResponse;
                        switch (decision) {
                            case "headOfAgency":
                                parentDetailsResponse = await this.getParentDetails();
                                assignedCN = parentDetailsResponse.cn;
                                break;
                            case "viceHeadOfAgency":
                                assignedCN = "";
                                break;
                            case "assistantToHeadOfAgency":
                                assignedCN = "";
                                break;
                            default:
                                console.error("invalid choice")
                        }
                    } else if (this.inputSchema.stepId === "headOfAgencyApproval") {
                        decision = approvalCard.approval.selected.type;
                        switch (decision) {
                            case "goToVice":
                                assignedCN = "cn=SVCC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                                break;
                            case "goToPresident":
                                assignedCN = "cn=SCOC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
                                break;
                            case "goToAssistant":
                                assignedCN = "";
                                break;
                            default:
                                console.error("invalid choice")
                        }

                    }
                }

                console.log(decision,assignedCN);

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
                    receiverType: approvalCard.approval.selected.type
                })
            }
        }
    }
</script>
