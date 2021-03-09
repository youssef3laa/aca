<template>
    <div style="width:100%">
      <AppBuilder ref="appBuilder" :app="app" />
    </div>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import router from "../../../router";
import http from "../../core-module/services/http";

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mixins: [formPageMixin],
  data() {
    return {
      viewRequestSelected:[],
      incomingRequestSelected: [],
      inputSchemaArray: [],
      app: {},
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
      sentFromManagementFilter: [{
          property: "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.subject",
          value: ""
        }, {
          property: "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.requestNumber",
          value: ""
        }]
    };
  },
  mounted() {
    this.loadForm("office-sector-head-inbox", this.formLoaded);
    this.viewRequestTopActionsSubscriptions();
    this.incomingRequestTopActionsSubscriptions();
    this.$observable.subscribe("viewRequestTable_view", (item) => {this.viewTask(item);});
    this.$observable.subscribe("incomingRequestTable_view", (item) => {this.viewTask(item);});
    this.$observable.subscribe("viewRequestTable_selected", (selected) => {
      this.viewRequestSelected = selected;
      let show;
      if (this.viewRequestSelected.length != 0) {
        show = true;
      } else {
        show = false;
      }
      this.$refs.appBuilder.setFieldData("viewRequest","actionTopComponent",
    {
          show: show
        }
      );
    });
    this.$observable.subscribe("incomingRequestTable_selected", (selected) => {
      this.incomingRequestSelected = selected;
      let show;
      if (this.incomingRequestSelected.length != 0) {
        show = true;
      } else {
        show = false;
      }
      this.$refs.appBuilder.setFieldData("incomingRequest","actionTopComponent",
    {
          show: show,
        }
      );
    });

    // this.$observable.subscribe("subjectHeadOfOfficeGroup", (text) => {
    //   this.sentFromManagementFilter[0].value = text
    //   this.$observable.fire("technicalTasks", {
    //     type: "modelUpdate",
    //     model: {
    //       filter: this.sentFromManagementFilter
    //     },
    //   });
    // });
    // this.$observable.subscribe("incomingNumberHeadOfOfficeGroup", (text) => {
    //   this.sentFromManagementFilter[1].value = text
    //   this.$observable.fire("technicalTasks", {
    //     type: "modelUpdate",
    //     model: {
    //       filter: this.sentFromManagementFilter
    //     },
    //   });
    // });
  },
  methods: {
    formLoaded(){
      http.get('workflow/human/tasks').then((response) => {
        console.log(response)
        let data = response.data.data

        let fromAdmins = [];
        let fromCertifications = [];

        for (let key in data) {
          switch (data[key].TaskData.ApplicationData?.ACA_ProcessRouting_InputSchemaFragment?.caseType) {
            case "sentFromCertification":
              fromCertifications.push(data[key])
              break
            default: // "sentFromAdministrators"
              fromAdmins.push(data[key])
          }
        }

        this.$refs.appBuilder.setTabValue("viewRequestTab", fromAdmins.length + "")
        this.$observable.fire("technicalTasks",{ type: "modelUpdate", model: {data: fromAdmins}});

        this.$refs.appBuilder.setTabValue("incomingTab", fromCertifications.length + "")
        this.$observable.fire("technicalTasksCertifications",{ type: "modelUpdate", model: {data: fromCertifications}});
      });
    },
    viewTask(item) {
      try {
        let taskId = item.item.TaskId,
        page = item.item.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.component;
        router.push({
          name: page,
          params: { taskId: taskId },
        });
      } catch (e) {
        console.error(e);
      }
    },
    viewRequestTopActionsSubscriptions() {
      this.$observable.subscribe("viewRequest_sendBackToTechnicalOffice", () => {
        console.log("viewRequest_sendBackToTechnicalOffice");
      });
      this.$observable.subscribe("viewRequest_sendToChairman", () => {
        console.log("viewRequest_sendToChairman");
      });
      this.$observable.subscribe("viewRequest_sendToVice", () => {
        console.log("viewRequest_sendToVice");
      });
    },
    incomingRequestTopActionsSubscriptions() {
      this.$observable.subscribe("incomingRequest_sendBackToChairman", () => {
        console.log("incomingRequest_sendBackToChairman");
      });
      this.$observable.subscribe("incomingRequest_sendToVice", () => {
        console.log("incomingRequest_sendToVice");
      });
      this.$observable.subscribe("incomingRequest_sendToTechnicalOffice", () => {
        console.log("incomingRequest_sendToTechnicalOffice");
      });

      this.$observable.subscribe("incomingRequest_sendBackToVice", () => {
        console.log("incomingRequest_sendBackToVice");
      });
      this.$observable.subscribe("incomingRequest_sendToChairman", () => {
        console.log("incomingRequest_sendToChairman");
      });
    },
  },
};
</script>
