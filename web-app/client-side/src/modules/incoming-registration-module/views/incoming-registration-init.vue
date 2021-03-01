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
import http from "../../core-module/services/http";
import router from "../../../router";

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
            request: undefined,
            inquiryData: [],
            inquiryId: 0,
            incomingRegistration: null
        };
    },
    async mounted() {
        this.request = await this.getRequest();
        this.incomingRegistration = await http.post("/incoming/registration/requestEntity/" + this.request.id + "/create");
        this.incomingRegistration = this.incomingRegistration.data.data;
        this.loadForm("incoming-registration-init", this.formLoaded);
        this.$observable.subscribe("complete-step", this.submit);
        this.$observable.subscribe("jobTypeChange", this.jobTypeChange);
        this.handleInquiry();
    },
    methods: {
        inquiryTable: function (data) {
            return {
                url: "",
                headers: [
                    {
                        "text": "fullName",
                        "value": "fullName"
                    },
                    {
                        "text": "inquiryType",
                        "value": "inquiryType"
                    },
                    {
                        "text": "birthDate",
                        "value": "birthDate"
                    },
                    {
                        "text": "birthLocation",
                        "value": "birthLocation"
                    },
                    {
                        "text": "jobAuthority",
                        "value": "jobAuthority"
                    },
                    {
                        "text": "positionAssignedTo",
                        "value": "positionAssignedTo"
                    },
                    {
                        "text": "candidatePosition",
                        "value": "candidatePosition"
                    },
                    {
                        "text": "",
                        "value": "action",
                        "sortable": false
                    }
                ],

                data: data,
            };
        },
        handleInquiry: function () {
            this.$observable.subscribe("inquiryTable_add", () => {
                this.$observable.fire("addInquiryModal", {
                    action: "add",
                });
            });
            this.$observable.subscribe("addInquiryModal_addModal", async (obj) => {
                console.log(obj);
                let objToBeCreated = {
                    birthDate: obj.obj.birthDate,
                    birthPlace: obj.obj.birthPlace,
                    candidatePosition: obj.obj.candidatePosition?.value?.value,
                    fullName: obj.obj.fullName,
                    inquiryType: obj.obj.inquiryType?.value?.value,
                    jobAuthority: obj.obj.jobAuthority?.value?.value,
                    positionAssignedTo: obj.obj.positionAssignedTo?.value?.value
                }
                await http.post("/incoming/inquiry/incomingRegistration/" + this.incomingRegistration.id, objToBeCreated)
                console.log(objToBeCreated);
                this.$observable.fire("inquiryTable_refresh");
                this.$refs.alertComponent._alertSuccess({
                    type: "success",
                    "message": this.$t("itemHasBeenAddedSuccessfully")
                });
            });
            this.$observable.subscribe("editInquiryModal_updateModal", (obj) => {

                this.inquiryData = this.inquiryData.map((inquiry) => {
                    if (inquiry.inquiryId == obj.obj.inquiryId) {
                        return Object.assign({}, obj.obj);
                    }
                });

                this.$refs.appBuilder.setModelData("inquiryTable", {
                    inquiryTable: this.inquiryTable(this.inquiryData),
                });
            });

            this.$observable.subscribe("inquiryTable_edit", (data) => {

                this.$refs.appBuilder.setModelData("editInquiryModal", data.item);
                this.$observable.fire("editInquiryModal", {
                    action: "edit",
                    obj: data.item,
                });
            });
            this.$observable.subscribe("inquiryTable_delete", async (data) => {
                console.log(data);
                try {
                    await http.delete("/incoming/inquiry/one/" + data.item.id);
                    this.$observable.fire("inquiryTable_refresh");
                    this.$refs.alertComponent._alertSuccess({
                        type: "success",
                        "message": this.$t("itemHasBeenDeletedSuccessfully")
                    });
                } catch (e) {
                    console.log(e);
                }

            });
        },
        formLoaded: function () {
            this.$refs.appBuilder.setModelData("mainData",
                {
                    maxDate: new Date().toISOString().split("T")[0]
                }
            )

            this.$refs.appBuilder.setModelData("approvalForm",
                {
                    approval: {
                        "inputs": [
                            {
                                "name": "theAgency",
                                "url": "org/unit/COC/down/all/unitTypeCode/AGN",
                                "type": "unit"
                            },
                            {
                                "name": "theSector",
                                "url": "org/unit/$1/down/all/unitTypeCode/SCT",
                                "type": "unit"
                            },
                            {
                                "name": "theAdminOffice",
                                "url": "org/unit/$2/down/all/unitTypeCode/OFC,ADM",
                                "type": "unit"
                            }
                        ]
                    }
                }
            )
        },
        jobTypeChange: async function (jobType) {
            if (jobType === undefined) {
                this.$refs.appBuilder.clearSectionForms("jobTypeSection");
                return;
            }
            console.log(jobType);
            let view = await this.loadView("incoming-registration\\" + jobType.object.stringKey)
            console.log(view)
            this.$refs.appBuilder.clearSectionForms("jobTypeSection")
            this.$refs.appBuilder.appendForm("jobTypeSection", view)
            if (jobType.value == 2) {
                this.$refs.appBuilder.setModelData("addInquiryModal",
                    {
                        maxBirthDate: new Date().toISOString().split("T")[0]
                    }
                )
                let inquiryTableModel = this.$refs.appBuilder.getModelData("inquiryTable");
                inquiryTableModel.inquiryTable.url = "/incoming/inquiry/all/incomingRegistration/" + this.incomingRegistration.id;

            }

        },
        submit: async function () {
            let vueThis = this;
            let attacmhnetComponentModel = this.$refs.appBuilder.getModelData("AttachmentComponent");
            if (!attacmhnetComponentModel.attachment.validity) {
                this.$refs.alertComponent._alertSuccess({
                    type: "error",
                    "message": this.$t("pleaseAddAtLeastOneAttachment")
                });
                return;
            }
            let mainData = this.$refs.appBuilder.getModelData("mainData");
            if (!(mainData._valid)) {
                this.$refs.alertComponent._alertSuccess({
                    type: "error",
                    "message": this.$t("pleaseFillRequiredFields")
                });
                return;
            }

            let returnedTargetMainData = Object.assign({}, mainData);
            console.log(mainData, returnedTargetMainData);
            returnedTargetMainData.confidentiality = returnedTargetMainData.confidentiality?.value.value
            returnedTargetMainData.incomingType = returnedTargetMainData.incomingType?.value.value
            returnedTargetMainData.jobType = returnedTargetMainData.jobType.value?.value
            returnedTargetMainData.priorityLevel = returnedTargetMainData.priorityLevel?.value.value
            returnedTargetMainData.taskType = returnedTargetMainData.taskType.value?.value
            returnedTargetMainData.incomingFrom = returnedTargetMainData.incomingFrom?.value.value
            returnedTargetMainData.requestEntityId = this.request.id;
            returnedTargetMainData.id = this.incomingRegistration.id;


            if (returnedTargetMainData.jobType == 1) {
                let approvalCard = this.$refs.appBuilder.getModelData("approvalForm")
                if (!approvalCard._valid) {
                    this.$refs.alertComponent._alertSuccess({type: "error", message: "pleaseFillRequiredFields"})
                    return;
                }
                console.log(approvalCard)
                returnedTargetMainData.responsibleEntityGehaz = approvalCard.approval.values[0]?.unitCode;
                if (approvalCard.approval.values[1])
                    returnedTargetMainData.responsibleEntityketa3 = approvalCard.approval.values[1].unitCode;
                if (approvalCard.approval.values[2])
                    returnedTargetMainData.responsibleEntityEdara = approvalCard.approval.values[2].unitCode;


                let caseData = this.$refs.appBuilder.getModelData("caseData")
                let caseDataReturnedTarget = Object.assign({}, caseData);
                caseDataReturnedTarget.caseType = caseDataReturnedTarget.caseType.value.value
                console.log(caseDataReturnedTarget)

                let data = {
                    incomingRegistration: returnedTargetMainData,
                    incomingCase: caseDataReturnedTarget,
                    outputSchema: {
                        requestId: this.request.id,
                        process: "incomingRegistrationCase",
                        stepId: "init",
                        entityName: "ACA_Entity_Incoming_Registration",
                        code: approvalCard.approval.code,
                        decision: "initiate",
                        entityId: this.incomingRegistration.id,
                        assignedCN: approvalCard.approval.assignedCN,
                        comment: approvalCard.approval.comment,
                        requestPriority: returnedTargetMainData.priorityLevel
                    }
                }
                console.log("Data", data)

                    try {
                        let initiationResponse = await this.initiateCaseProcess(data);
                        console.log(initiationResponse);
                        localStorage.removeItem("requestId")
                        vueThis.$refs.alertComponent._alertSuccess({
                            type: "success",
                            message: "initiateSuccess"
                        }, () => router.push({name: 'HomePage'}).then(r => console.log(r)));
                    } catch (e) {
                        console.error(e);
                    }


            }

        },
    }
};
</script>
