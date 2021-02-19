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
        name: "incoming-case-registration-read",
        mixins: [formPageMixin, historyMixin, incomingRegistrationMixin, opinionsMixin, outcomingMixin],
        components: {AppBuilder},
        data() {
            return {
                requestId: this.$route.params.requestId,
                app: {},
            }
        },
        async created() {
            this.loadForm("incoming-case-registration-read", this.formLoaded)
        },
        methods: {
            formLoaded: async function () {
                this.$refs.appBuilder.disableAllSections();
                this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.requestId)})
                this.$refs.appBuilder.setModelData("opinionsTable", {opinionsTable: this.createOpinionTableModel(this.requestId)})
                this.$refs.appBuilder.setModelData("memorandumForm", {memorandum: {requestId: this.requestId}})
                this.$refs.appBuilder.setModelData("signatureForm", {signature: {requestId: this.requestId}})

                let requestData = await this.readRequest(this.requestId);

                //TODO condition shown tabs according to requestData.status with stepId
                
                let incomingRegistration = await this.readIncomingRegistration(requestData.entityId)
                this.$refs.appBuilder.setModelData("mainData", incomingRegistration)

                let incomingCase = await this.readIncomingCase(incomingRegistration.jobEntityId)
                this.$refs.appBuilder.setModelData("caseData", incomingCase)
                let outcomingData = await this.readOutcoming(incomingRegistration.outcomingId)

                this.$refs.appBuilder.setModelData("outcomingData", outcomingData)
                this.$refs.appBuilder.setModelData("outcomingIssueForm", outcomingData)
                this.$refs.appBuilder.setFieldData("outcomingIssueForm", "recipientName", {show: !!(outcomingData.recipientName)})
                this.$refs.appBuilder.setFieldData("outcomingIssueForm", "job", {show: !!(outcomingData.job)})
                this.$refs.appBuilder.setFieldData("outcomingIssueForm", "receivingAdministration", {show: !!(outcomingData.receivingAdministration)})
            }
        }
    }
</script>