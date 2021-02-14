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

    export default {
        name: "incoming-case-registration-comment",
        mixins: [formPageMixin, historyMixin, incomingRegistrationMixin],
        components: {AppBuilder},
        data() {
            return {
                taskId: this.$route.params.taskId,
                inputSchema: {},
                app: {}
            }
        },
        async created(){
            this.claimTask(this.taskId)
            let taskData = await this.getTaskData(this.taskId)
            this.inputSchema = taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment
            this.loadForm(this.inputSchema.config, this.formLoaded)
            this.$observable.subscribe("complete-step", this.submit)
        },
        methods: {
            formLoaded: async function(){
                this.$refs.appBuilder.disableSection("mainData");
                this.$refs.appBuilder.disableSection("caseData");
                this.$refs.appBuilder.setModelData("approvalForm", {approval: this.inputSchema.router})
                this.$refs.appBuilder.setModelData("historyTable", {historyTable: this.createHistoryTableModel(this.inputSchema.requestId)})

                let incomingRegistration = await this.readIncomingRegistration(this.inputSchema.entityId)
                this.$refs.appBuilder.setModelData("mainData", incomingRegistration)
                let incomingCase = await this.readIncomingCase(incomingRegistration.jobEntityId)
                this.$refs.appBuilder.setModelData("caseData", incomingCase)
            },
            submit: function(){
                let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");

                this.completeStep({
                    taskId: this.taskId,
                    stepId: this.inputSchema.stepId,
                    process: this.inputSchema.process,
                    parentHistoryId: this.inputSchema.parentHistoryId,

                    code: approvalCard.approval.code,
                    assignedCN: approvalCard.approval.assignedCN,
                    decision: approvalCard.approval.decision,
                    comment: approvalCard.approval.comment,
                    receiverType: approvalCard.approval.receiverType
                })
            }
        }
    }
</script>