<template>
    <v-container>
        <AppBuilder ref="appBuilder" :app="app"/>
    </v-container>
</template>

<script>
    import formPageMixin from "../../../mixins/formPageMixin";
    import AppBuilder from "../../application-builder-module/builders/app-builder";
    import historyMixin from "@/modules/history-module/mixin/historyMixin";

    export default {
        name: "generalProcess-approvals",
        mixins: [formPageMixin, historyMixin],
        components: {
            AppBuilder,
        },

        methods: {
            readData: function () {
                console.log("TaskData", this.taskData);
                this.inputSchema = this.taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment;
                //   let page = this.inputSchema.page;

                this.loadForm("generalProcess-member", this.fillForm);
            },
            fillForm: async function () {
                this.$refs.appBuilder.disableSection("section1")
                let entityName = this.inputSchema.entityName;
                let entityId = this.inputSchema.entityId;
                this.readEntity(entityName, entityId)
                    .then(async (response) => {
                        response = JSON.parse(response.data.data);

                        var workTypeObj = await this.getLookupByCategoryAndKey("workType",response.workType);
                        var incomingMeansObj = await this.getLookupByCategoryAndKey("incomingMeans",response.incomingMeans);

                        this.$refs.appBuilder.setModelData("form1", {
                            stepId: this.inputSchema.stepId,
                            subjectSummary: response.summary,
                            workType: workTypeObj.arValue,
                            incomingMeans: incomingMeansObj.arValue,
                            receiver: {
                                url: this.inputSchema.roleFilter,
                                list: [],
                                value: ""
                            },
                            writingDate: response.writingDate.split("Z")[0],
                        });

                        this.$refs.appBuilder.setModelData("historyTable", {
                            taskTable: this.createHistoryTableModel(this.inputSchema.process, this.inputSchema.entityId)
                        });

                        console.log("response", response);
                    })
                    .catch((error) => {
                        console.error(error);
                    });
            },
        },
        async created() {
            this.taskId = this.$route.params.taskId;
            this.claimTask(this.taskId);
            this.getTaskData(this.taskId);
            this.initiateBrava();
            this.$observable.subscribe("complete-step", () => {
                // if(!this.$refs.appBuilder) return;
                console.log("complete-step-clicked");
                console.log(this.$refs.appBuilder);
                // var model =
                let model = this.$refs.appBuilder.getModelData("form1");
                let approvalModel = this.$refs.appBuilder.getModelData("ApprovalForm");
                console.log("MODEL:", model);
                console.log("APPROVAL MODEL", approvalModel);
                // if (model._valid) {
                var data = {
                    taskId: this.taskId,
                    entityId: this.inputSchema.entityId,
                    stepId: this.inputSchema.stepId,
                    process: this.inputSchema.process,
                    parentHistoryId: this.inputSchema.parentHistoryId,

                    code: model.receiver.value.code,
                    assignedCN: model.receiver.value.value,
                    decision: approvalModel.approval.decision,
                    comment: approvalModel.approval.comment,
                };
                this.completeStep(data);
                // }
            });

        },

        data() {
            return {
                taskId: "",
                taskData: {},
                inputSchema: {},
                app: {},
                model: {},
            };
        },
    };
</script>
