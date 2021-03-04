<template>
  <div style="width:100%">
    <v-container>
      <AppBuilder ref="appBuilder" :app="app" />
      <AlertComponent ref="alertComponent"></AlertComponent>
    </v-container>
  </div>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import orgChartMixin from "../../../mixins/orgChartMixin";
import router from "../../../router";
import http from "../../core-module/services/http";

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mixins: [formPageMixin, orgChartMixin],
  data() {
    return {
      selected: [],
      selectedCertfication: [],
      inputSchemaArray: [],
      app: {},
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
      sentFromManagementFilter: [
        {
          property:
            "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.subject",
          value: "",
        },
        {
          property:
            "TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment.requestNumber",
          value: "",
        },
      ],
    };
  },
  mounted() {
    this.loadForm("office-group-head-inbox", this.formLoaded);
    this.certificationTopActionsSubscriptions();
    this.managementTopActionsSubscriptions();
    this.$observable.subscribe("technicalTasksTable_view", (item) => {
      this.viewTask(item);
    });
    this.$observable.subscribe(
      "technicalTasksTableCertification_selected",
      (selected) => {
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
      }
    );
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
      this.sentFromManagementFilter[0].value = text;

      this.$observable.fire("technicalTasks", {
        type: "modelUpdate",
        model: {
          filter: this.sentFromManagementFilter,
        },
      });
    });
    this.$observable.subscribe("incomingNumberHeadOfOfficeGroup", (text) => {
      this.sentFromManagementFilter[1].value = text;

      this.$observable.fire("technicalTasks", {
        type: "modelUpdate",
        model: {
          filter: this.sentFromManagementFilter,
        },
      });
    });
  },
  methods: {
    formLoaded() {
      http.get("workflow/human/tasks").then((response) => {
        console.log(response);
        let data = response.data.data;

        let fromAdmins = [];
        let fromCertifications = [];

        for (let key in data) {
          switch (data[key].TaskData.ApplicationData?.ACA_ProcessRouting_InputSchemaFragment?.caseType) {
            case "certificationTechnicalOffice":
              fromCertifications.push(data[key]);
              break;
            default:
              // "sentFromAdministratorsTechnicalOffice"
              fromAdmins.push(data[key]);
          }
        }

        this.$refs.appBuilder.setTabValue(
          "sentFromManagementTab",
          fromAdmins.length + ""
        );
        this.$observable.fire("technicalTasks", {
          type: "modelUpdate",
          model: { data: fromAdmins },
        });

        this.$refs.appBuilder.setTabValue(
          "signaturesTab",
          fromCertifications.length + ""
        );
        this.$observable.fire("technicalTasksCertifications", {
          type: "modelUpdate",
          model: { data: fromCertifications },
        });
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
        let outschemaArray = [];
       
        this.selected.forEach(async (task) => {
          let inputSchema =
            task.TaskData.ApplicationData
              .ACA_ProcessRouting_InputSchemaFragment;
          let requestData = await this.readRequest(inputSchema.requestId);
          let entity = await this.readEntity(
            requestData.entityName,
            requestData.entityId
          );
          if (entity.responsibleEntityEdara) {
            let group = await this.getHeadRoleByUnitCode(
              entity.responsibleEntityEdara
            );
            let assignedCN = "cn=" + group.groupCode + ",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
            let obj = {
              taskId: task.TaskId,
              requestId: inputSchema.requestId,
              stepId: inputSchema.stepId,
              process: inputSchema.process,
              parentHistoryId: inputSchema.parentHistoryId,
              assignedCN: assignedCN,
              decision: "approve",
              receiverType: "single",
            };
            outschemaArray.push(obj);
          }
        });
        this.completeMultipleSteps(outschemaArray, this.formLoaded);
      });
    },
    managementTopActionsSubscriptions() {
      this.$observable.subscribe("backToManagement", () => {
        console.log("backToManagement");
      });
      this.$observable.subscribe("temporarySave", () => {
        console.log("temporarySave");
      });

      this.$observable.subscribe("sendToCertification", async () => {
        console.log("sendToCertification");
        let outschemaArray = [];
        let unitCode = this.$user.details.groups[0].unit.unitCode;
        let assignedCN = "";
        let group;
        if (unitCode == "TVA") {
          //Vice
          group = await this.getHeadRoleByUnitCode("TVS");
        } else {
          //Chairman
          group = await this.getHeadRoleByUnitCode("TCS");
        }
        assignedCN ="cn=" +group.groupCode +",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";

        this.selected.forEach((task) => {
          let inputSchema =
            task.TaskData.ApplicationData
              .ACA_ProcessRouting_InputSchemaFragment;
          let obj = {
            taskId: task.TaskId,
            requestId: inputSchema.requestId,
            stepId: inputSchema.stepId,
            process: inputSchema.process,
            parentHistoryId: inputSchema.parentHistoryId,
            assignedCN: assignedCN,
            decision: "approve",
            receiverType: "single",
          };
          outschemaArray.push(obj);
        });
        this.completeMultipleSteps(outschemaArray, this.formLoaded);
        console.log(outschemaArray);
        console.log(this.selected);
      });
    },
  },
};
</script>
