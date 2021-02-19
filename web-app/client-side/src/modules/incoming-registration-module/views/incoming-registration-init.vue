<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app" />
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
    AppBuilder,
  },
  data() {
    return {
      app: {},
      model: {},
      request: undefined,
      inquiryData: [],
      inquiryId: 0,
    };
  },
  async mounted() {
    this.request = await this.getRequest();
    this.loadForm("incoming-registration-init", this.formLoaded);
    this.$observable.subscribe("complete-step", this.submit);
    this.$observable.subscribe("jobTypeChange", this.jobTypeChange);
    this.handleInquiry();
  },
  methods: {
    inquiryTable: function(data) {
      return {
        url: "",
        headers: [
          {
            text: "inquiryType",
            value: "inquiryType",
          },
          {
            text: "fullName",
            value: "fullName",
          },

          {
            text: "birthDate",
            value: "birthDate",
          },

          {
            text: "jobEntity",
            value: "jobEntity",
          },
          {
            text: "assignedJob",
            value: "assignedJob",
          },
          {
            text: "candidateJob",
            value: "candidateJob",
          },

          {
            text: "",
            value: "action",
            sortable: false,
          },
        ],

        data: data,
      };
    },
    handleInquiry: function() {
      this.$observable.subscribe("inquiryTable_add", () => {
        this.$observable.fire("addInquiryModal", {
          action: "add",
        });
      });
      this.$observable.subscribe("addInquiryModal_addModal", (obj) => {
        obj.obj.inquiryId = this.inquiryId;
        this.inquiryId++;
        this.inquiryData.push(Object.assign({}, obj.obj));

        this.$refs.appBuilder.setModelData("inquiryTable", {
          inquiryTable: this.inquiryTable(this.inquiryData),
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
      this.$observable.subscribe("inquiryTable_delete", (data) => {
  
        this.inquiryData = this.inquiryData.filter((inquiry) => {
          return inquiry.inquiryId != data.item.inquiryId;
        });
      
        this.$refs.appBuilder.setModelData("inquiryTable", {
          inquiryTable: this.inquiryTable(this.inquiryData),
        });
      });
    },
    formLoaded: function() {
      this.$refs.appBuilder.setModelData("mainData", {
        // incomingNumber: this.request.requestNumber,
        maxDate: new Date().toISOString().split("T")[0],
      });
      this.$refs.appBuilder.setModelData("approvalForm", {
        approval: {
          direction: "all",
          fields: ["comment"],
          receiverTypes: ["single"],
        },
      });
    },
    jobTypeChange: async function(jobType) {
      console.log(jobType);
      let view = await this.loadView(
        "incoming-registration\\" + jobType.object.stringKey
      );
      console.log(view);
      this.$refs.appBuilder.clearSectionForms("caseDataSection");
      this.$refs.appBuilder.appendForm("caseDataSection", view);
    },
    submit: function() {
      let mainData = this.$refs.appBuilder.getModelData("mainData");
      mainData.confidentiality = mainData.confidentiality.value.value;
      mainData.incomingType = mainData.incomingType.value.value;
      mainData.jobType = mainData.jobType.value.value;
      mainData.priorityLevel = mainData.priorityLevel.value.value;
      mainData.taskType = mainData.taskType.value.value;

      let caseData = this.$refs.appBuilder.getModelData("caseData");
      caseData.caseType = caseData.caseType.value.value;

      let approvalCard = this.$refs.appBuilder.getModelData("approvalForm");

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
          comment: approvalCard.approval.comment,
        },
      };

      this.initiateCaseProcess(data);

      // console.log("Data", data)
      // if(!approval._valid){
      //     this.$refs.alertComponent._alertSuccess({type: "error",message:"pleaseFillRequiredFields"})
      // }
    },
  },
};
</script>
