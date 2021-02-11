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
        name: "incoming-registration-init",
        mixins: [formPageMixin],
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
            this.loadForm("incoming-registration-init", this.formLoaded);
            this.$observable.subscribe("complete-step", this.submit);
        },
        methods: {
            formLoaded: function () {
                this.$refs.appBuilder.setModelData("mainData",
                    {
                        incomingNumber: this.request.requestNumber,
                        maxDate: new Date().toISOString().split("T")[0],
                    }
                )
                this.$refs.appBuilder.setModelData("approvalForm",
                    {
                        approval: {
                            fields: ["comment"],
                            receiverTypes: ["single"],
                            direction: "all"
                        }
                    }
                )
            },
            submit: function () {
                let mainData = this.$refs.appBuilder.getModelData("mainData")
                let caseData = this.$refs.appBuilder.getModelData("caseData")
                let approval = this.$refs.appBuilder.getModelData("approvalForm")
                console.log("MainData", mainData)
                console.log("CaseData", caseData)
                console.log("ApprovalCard", approval)
            },
        }
    };
</script>