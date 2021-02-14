<template>
    <v-container>
        <AppBuilder ref="appBuilder" :app="app"/>
        <AlertComponent ref="alertComponent"></AlertComponent>
    </v-container>
</template>

<script>
    import formPageMixin from "../../../mixins/formPageMixin";
    import AppBuilder from "../../application-builder-module/builders/app-builder";
    import incomingRegistrationMixin from "../mixins/incomig-registration-mixin";

    export default {
        name: "incoming-registration-init",
        mixins: [formPageMixin, incomingRegistrationMixin],
        components: {
            AppBuilder
        },
        data() {
            return {
                app: {},
                model: {},
                request: undefined
            };
        },
        async created() {
            this.request = await this.getRequest()
            this.loadForm("incoming-registration-init", this.formLoaded)
            this.$observable.subscribe("complete-step", this.submit)
        },
        methods: {
            formLoaded: function () {
                this.$refs.appBuilder.setModelData("mainData",
                    {
                        incomingNumber: this.request.requestNumber,
                        maxDate: new Date().toISOString().split("T")[0]
                    }
                )
                this.$refs.appBuilder.setModelData("approvalForm",
                    {
                        approval: {
                            direction: "all",
                            fields: ["comment"],
                            receiverTypes: ["single"]
                        }
                    }
                )
            },
            submit: function () {
                let mainData = this.$refs.appBuilder.getModelData("mainData")
                mainData.confidentiality = mainData.confidentiality.value.value
                mainData.incomingType = mainData.incomingType.value.value
                mainData.jobType = mainData.jobType.value.value
                mainData.priorityLevel = mainData.priorityLevel.value.value
                mainData.taskType = mainData.taskType.value.value

                let caseData = this.$refs.appBuilder.getModelData("caseData")
                caseData.caseType = caseData.caseType.value.value

                let approvalCard = this.$refs.appBuilder.getModelData("approvalForm")

                let data = {
                    incomingRegistration: mainData,
                    incomingCase: caseData,
                    outputSchema: {
                        requestId: this.request.id,
                        process: "incomingRegistrationCase",
                        stepId: "init",
                        entityName: "ACA_Entity_Incoming_Registration",
                        code: approvalCard.approval.code,
                        decision: "initiate",
                        assignedCN: approvalCard.approval.assignedCN,
                        comment: approvalCard.approval.comment
                    }
                }

                this.initiateCaseProcess(data)

                console.log("Data", data)
                // if(!approval._valid){
                //     this.$refs.alertComponent._alertSuccess({type: "error",message:"pleaseFillRequiredFields"})
                // }
            },
        }
    };
</script>