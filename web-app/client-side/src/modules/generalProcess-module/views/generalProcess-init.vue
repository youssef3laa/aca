<template>
    <v-container>
        <AppBuilder ref="appBuilder" :app="app"/>
        <AlertComponent ref="alertComponent"></AlertComponent>
    </v-container>
</template>

<script>
    import formPageMixin from "../../../mixins/formPageMixin";
    import AppBuilder from "../../application-builder-module/builders/app-builder";

    export default {
        name: "generalProcess-init",
        mixins: [formPageMixin],
        components: {
            AppBuilder
        },
        data() {
            return {
                app: {},
                model: {},
                requestId: null
            };
        },
        async created() {
            await this.loadForm("generalProcess-init", this.formLoaded);
            // this.requestId = await this.createRequest();
            this.$observable.subscribe("complete-step", this.submit);
            // this.$refs.alertComponent._alertSuccess({type: "warning",message: "comment"})
        },
        methods: {
            formLoaded: function () {
                this.$refs.appBuilder.setModelData("form1", {
                    maxDate: new Date().toISOString().split('T')[0]
                })
                this.$refs.appBuilder.setModelData("approvalForm", {
                    approval: {
                        fields: [
                            "comment", "opinion"
                        ],
                        decisions: [
                            {
                                name: "approve",
                                label: "request-approve",
                                types: [
                                    {
                                        name: "vice",
                                        label: "request-vice-approval",
                                        assignedCN: "hamada"
                                    },
                                    {
                                        name: "president",
                                        label: "request-president-approval",
                                        assignedCN: "hamada2"
                                    },
                                    {
                                        name: "president2",
                                        label: "request-president-approval",
                                        assignedCN: "hamada3"
                                    }
                                ],
                                inputs: [
                                    {
                                        name: "theAgency",
                                        url: "org/unit/COC/down/all/unitTypeCode/AGN",
                                        type: "unit"
                                    },
                                    {
                                        name: "theSector",
                                        url: "org/unit/$1/down/all/unitTypeCode/SCT",
                                        type: "unit"
                                    }
                                ]
                            },
                            {
                                name: "reject",
                                label: "request-rejection"
                            }
                        ]
                    }
                })
            },
            submit: function () {
                // let model = this.$refs.appBuilder.getModelData("form1");
                // console.log("Model", model)
                // if (!model._valid) {
                //   //@TODO show warining
                //   return;
                // }

                let model2 = this.$refs.appBuilder.getModelData("approvalForm");
                console.log("Receiver", model2)


                // let obj = {
                //   generalProcessEntity: {
                //     writingDate: model.writingDate,
                //     summary: model.subjectSummary,
                //     workType: model.workType.value.value,
                //     incomingMeans: model.incomingMeans.value.value,
                //     incomingUnit: model.incomingUnit.value.text,
                //     agency: (model2.receiver.agency)? model2.receiver.agency.name_ar: null,
                //     sector: (model2.receiver.sector)? model2.receiver.sector.name_ar: null,
                //     office: (model2.receiver.office)? model2.receiver.office.name_ar: null,
                //   },
                //   processModel: {
                //     requestId: this.requestId.id,
                //     process: "generalProcess",
                //     stepId: "init",
                //     entityName: "ACA_Entity_generalProcess",
                //     code: model2.receiver.role.unit.unitTypeCode,
                //     assignedCN: model2.receiver.assignedCN
                //   }
                // };
                // this.initiateProcess(obj);
            },
        }
    };
</script>