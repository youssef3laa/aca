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
        this.$observable.subscribe("jobTypeChange", this.jobTypeChange)
    },
    methods: {
        formLoaded: function () {
            this.$refs.appBuilder.setModelData("mainData",
                {
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
        jobTypeChange: async function (jobType) {
            console.log(jobType);
            if (jobType === undefined) {
                this.$refs.appBuilder.clearSectionForms("caseDataSection");
                return;
            }
            let view = await this.loadView("incoming-registration\\" + jobType.object.stringKey)
            console.log(view)
            this.$refs.appBuilder.clearSectionForms("caseDataSection")
            this.$refs.appBuilder.appendForm("caseDataSection", view)
        },
        submit: function () {

            // this.$observable.subscribe("attachmentValidationAnswer",function (validityState) {
            //     console.log(validityState);
            // });
            // this.$observable.fire("attachmentValidationAsk");

            let attacmhnetComponentModel = this.$refs.appBuilder.getModelData("AttachmentComponent");
            if (!attacmhnetComponentModel.attachment.validity) {
                this.$refs.alertComponent._alertSuccess({type: "error", "message": this.$t("pleaseAddAtLeastOneAttachment")});
            }

            // let mainData = this.$refs.appBuilder.getModelData("mainData")
            // mainData.confidentiality = mainData.confidentiality.value.value
            // mainData.incomingType = mainData.incomingType.value.value
            // mainData.jobType = mainData.jobType.value.value
            // mainData.priorityLevel = mainData.priorityLevel.value.value
            // mainData.taskType = mainData.taskType.value.value
            //
            // let caseData = this.$refs.appBuilder.getModelData("caseData")
            // caseData.caseType = caseData.caseType.value.value
            //
            // let approvalCard = this.$refs.appBuilder.getModelData("approvalForm")
            //
            // let data = {
            //     incomingRegistration: mainData,
            //     incomingCase: caseData,
            //     outputSchema: {
            //         requestId: this.request.id,
            //         process: "incomingRegistrationCase",
            //         stepId: "init",
            //         entityName: "ACA_Entity_Incoming_Registration",
            //         code: approvalCard.approval.code,
            //         decision: "initiate",
            //         assignedCN: approvalCard.approval.assignedCN,
            //         comment: approvalCard.approval.comment
            //     }
            // }
            //
            // this.initiateCaseProcess(data)

            // console.log("Data", data)
            // if(!approval._valid){
            //     this.$refs.alertComponent._alertSuccess({type: "error",message:"pleaseFillRequiredFields"})
            // }
        },
    }
};
</script>
