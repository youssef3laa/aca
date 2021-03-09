
<template>
    <div style="width:100%">
        <v-container>
            <v-row>
                <v-col class="padding-left" cols="5">
                    <AppBuilder ref="tableAppBuilder" :app="app"/>
                </v-col>
                <v-col class="no-padding" cols="7">
                    <AlertComponent ref="alertComponent"></AlertComponent>

                    <AppBuilder v-show="itemIsSelected" ref="appBuilder" :app="app1"/>
                    <div class="empty-form" v-show="!itemIsSelected">
                        <div>
                            <v-img width="300px" src="../../../assets/documents.svg"></v-img>
                            <span>قم باختيار وارد لعرضة وعرض والتأشيرات</span>
                        </div>
                    </div>
                </v-col>
            </v-row>
        </v-container>
    </div>
</template>

<script>
import http from "../../core-module/services/http";
import formPageMixin from "../../../mixins/formPageMixin";
import orgChartMixin from '../../../mixins/orgChartMixin';
import router from "../../../router";
import signatureMixin from "../../graphs-module/mixin/signatureMixin";

export default {
        components: {
            AppBuilder: () => import("../builders/app-builder"),
        },
        mixins: [formPageMixin, orgChartMixin,signatureMixin],

        mounted() {
            this.loadForm(
                "secretary-incoming-signatures-table",
                this.formLoaded,
                "tableAppBuilder"
            );
            this.loadForm("secretary-incoming-signatures-form", null, "appBuilder");
            this.submit()
            this.getTasks("signatures");

            this.$observable.subscribe("signaturesTable_view", (item) => {
                this.viewTask(item)
            });

            this.$observable.subscribe("signaturesTable_selected", async (selected) => {
                console.log("signaturesTable_selected",selected);

                this.selected = selected;

                if (selected.length != 0) {
                    let inputSchema = selected[selected.length - 1].TaskData.ApplicationData
                        .ACA_ProcessRouting_InputSchemaFragment;
                    this.itemIsSelected = true;
                    let requestData = await this.readRequest(
                        inputSchema.requestId
                    );

                    console.log(requestData);
                    this.$refs.appBuilder.setModelData("mainData", {
                        followUpNumber:
                        inputSchema.requestNumber,
                        followUpDate: requestData.requestDate,
                        nextFollowUpDate: requestData.requestDate,
                    })

                    // let readSignatureResponse = await this.readSignature(inputSchema.extraData.signatureEntityId);
                    let readSignatureResponse = await this.readSignature("376836");

                    console.log("readSignatureResponse", readSignatureResponse)
                    let unitCode = this.$user.details.groups[0].unit.unitCode;
                    let base64Start = "data:image/png;base64,";

                    if (unitCode == "VCC") {
                        //Vice
                        this.$refs.appBuilder.setModelData("viceChairmanOfCommisionSignatureForm", {
                            "viceChairmanOfCommisionSignature": {
                                "url": base64Start+readSignatureResponse.data.data.signatureViceImg
                            }

                        });


                    } else {
                        //Chairman


                        this.$refs.appBuilder.setModelData("viceChairmanOfCommisionSignatureForm", {
                            "chairmanOfCommisionSignatureForm": {
                                "url": base64Start+readSignatureResponse.data.data.signatureHeadImg
                            }

                        });

                        this.$refs.appBuilder.setModelData("viceChairmanOfCommisionSignatureForm", {
                            "viceChairmanOfCommisionSignature": {
                                "url": base64Start+readSignatureResponse.data.data.signatureViceImg
                            },
                            viceChairmanOfCommisionText:readSignatureResponse.data.data.signatureViceTxt

                        });
                        this.$refs.appBuilder.setFieldData("viceChairmanOfCommisionSignatureForm", "viceChairmanOfCommisionText", {readonly: true});
                    }


                } else {
                    this.itemIsSelected = false;
                }
            });
        },
        methods: {
            viewTask(item) {
                try {
                    let taskId = item.item.TaskId,
                        page = item.item.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.component;
                    router.push({
                        name: page,
                        params: {taskId: taskId},
                    });
                } catch (e) {
                    console.error(e);
                }
            },
            formLoaded() {
                http.get("workflow/human/tasks").then((response) => {
                    console.log(response);
                    let data = response.data.data;

                    let fromSignatures = [];

                    for (let key in data) {
                        if (data[key].TaskData.ApplicationData?.ACA_ProcessRouting_InputSchemaFragment?.caseType == "signatures") {
                            fromSignatures.push(data[key]);
                        }
                    }

                    this.$observable.fire("secretarySignatures", {
                        type: "modelUpdate",
                        model: {data: fromSignatures},
                    });
                });
            },
            submit() {
                this.$observable.subscribe("complete-step", async () => {
                    if (this.selected.length != 0) {
                        let data = this.$refs.appBuilder.getModelData("mainData")
                        let task = this.selected[0];
                        let signtureText;

                        let unitCode = this.$user.details.groups[0].unit.unitCode;
                        let group;
                        let caseType = "sentFromCertification";

                        let viceOrHead;
                        let inputSchema = task.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment;

                        if (unitCode == "VCC") {
                            //Vice
                            group = await this.getHeadRoleByUnitCode("TVS");
                            let model = this.$refs.appBuilder.getModelData("viceChairmanOfCommisionSignatureForm");
                            console.log(model);
                            signtureText = model.viceChairmanOfCommisionText;
                            viceOrHead = 2;
                        } else {
                            //Chairman
                            group = await this.getHeadRoleByUnitCode("TCS");
                            let model = this.$refs.appBuilder.getModelData("chairmanOfCommisionSignatureForm");
                            signtureText = model.chairmanOfCommisionText;
                            console.log(model);
                            viceOrHead = 2;
                        }

                        await this.updateSignature({
                            id: inputSchema.extraData.signatureEntityId,
                            viceOrHead: viceOrHead,
                            signatureTxt: signtureText
                        }, false)

                        console.log(signtureText);
                        console.log(data);
                        let assignedCN = "cn=" + group.groupCode + ",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";

                        //TODO handle estyfa2 and mowaf2a
                        let obj = {
                            caseType,
                            taskId: task.TaskId,
                            extraData:inputSchema.extraData,
                            requestId: inputSchema.requestId,
                            stepId: inputSchema.stepId,
                            process: inputSchema.process,
                            parentHistoryId: inputSchema.parentHistoryId,
                            assignedCN: assignedCN,
                            decision: "send",
                            comment: data.notes,
                            receiverType: "single"
                        };
                        console.log("route", this.$route.name);
                        this.completeStep(obj);
                        console.log(obj);
                        console.log(this.selected);
                    }
                });
            },
        },
        data() {
            return {
                taskList: [{title: "إنشاء وارد جديد"}, {title: "تسجيل موضوع"}],
                app: {},
                app1: {},
                selected: [],
                inputSchemaArray: [],
                itemIsSelected: false,
            };
        },
    };
</script>

<style scoped>
    .empty-form {
        display: flex;
        justify-content: center;
        align-items: center;
        min-height: 500px;
    }

    .no-padding {
        padding: 0px !important;
    }

    .padding-left {
        padding: 0px !important;
        padding-left: 1px !important;
        border-left: 1px solid #d1d1d1 !important;
    }
</style>
