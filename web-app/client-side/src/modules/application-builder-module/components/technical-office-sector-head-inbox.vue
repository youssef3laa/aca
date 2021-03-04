<template>
  <div style="width:100%">
    <v-container>
      <AppBuilder ref="appBuilder" :app="app" />
    </v-container>
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
      selected: [],
      selectedCertfication:[],
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
    this.certificationTopActionsSubscriptions();
    this.managementTopActionsSubscriptions();
    this.$observable.subscribe("technicalTasksTable_view", (item) => {
      this.viewTask(item);
    });
    this.$observable.subscribe("technicalTasksTableCertification_selected", (selected) => {
      this.selectedCertfication = selected;
      console.log(this.selected);
      if (this.selectedCertfication.length != 0) {
        this.$refs.appBuilder.setFieldData(
          "sentFromCertification",
          "actionTopComponent",
          {
            show: true,
          }
        );
      } else {
        this.$refs.appBuilder.setFieldData(
          "sentFromCertification",
          "actionTopComponent",
          {
            show: false,
          }
        );
      }
    });
    this.$observable.subscribe("technicalTasksTable_selected", (selected) => {
      this.selected = selected;
      console.log(this.selected);
      if (this.selected.length != 0) {
        this.$refs.appBuilder.setFieldData(
                "sentFromManagement",
                "actionTopComponent",
                {
                  show: true,
                }
        );
      } else {
        this.$refs.appBuilder.setFieldData(
                "sentFromManagement",
                "actionTopComponent",
                {
                  show: false,
                }
        );
      }
    });

    this.$observable.subscribe("subjectHeadOfOfficeGroup", (text) => {
      this.sentFromManagementFilter[0].value = text

      this.$observable.fire("technicalTasks", {
        type: "modelUpdate",
        model: {
          filter: this.sentFromManagementFilter
        },
      });
    });
    this.$observable.subscribe("incomingNumberHeadOfOfficeGroup", (text) => {
      this.sentFromManagementFilter[1].value = text

      this.$observable.fire("technicalTasks", {
        type: "modelUpdate",
        model: {
          filter: this.sentFromManagementFilter
        },
      });
    });
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
            default: // "sentFromAdministratorsTechnicalOffice"
              fromAdmins.push(data[key])
          }
        }

        this.$refs.appBuilder.setTabValue("sentFromManagementTab", fromAdmins.length + "")
        this.$observable.fire("technicalTasks",{ type: "modelUpdate", model: {data: fromAdmins}});

        this.$refs.appBuilder.setTabValue("signaturesTab", fromCertifications.length + "")
        this.$observable.fire("technicalTasksCertifications",{ type: "modelUpdate", model: {data: fromCertifications}});
      });
    },
    submit() {
      this.selected.forEach((item) => {
        this.inputSchemaArray.push(
          item.taskData.TaskData.ApplicationData
            .ACA_ProcessRouting_InputSchemaFragments
        );
      });
    },
    viewTask(item) {
      try {
        let taskId = item.item.TaskId,
          page =
            item.item.TaskData.ApplicationData
              .ACA_ProcessRouting_InputSchemaFragment.component;
        router.push({
          name: page,
          params: { taskId: taskId },
        });
      } catch (e) {
        console.error(e);
      }
    },
    certificationTopActionsSubscriptions() {
      this.$observable.subscribe("sendToAnotherManagement", () => {
        console.log("sendToAnotherManagement");
      });
      this.$observable.subscribe("backToCertification", () => {
        console.log("backToCertification");
      });
      this.$observable.subscribe("ManagementtemporarySave", () => {
        console.log("ManagementtemporarySave");
      });
      this.$observable.subscribe("send", () => {
        console.log("send");
      });
    },
    managementTopActionsSubscriptions() {
      this.$observable.subscribe("backToManagement", () => {
        console.log("backToManagement");
      });
      this.$observable.subscribe("temporarySave", () => {
        console.log("temporarySave");
      });

      this.$observable.subscribe("sendToCertification", () => {
        console.log("sendToCertification");
      });
    },

  },
};
</script>
