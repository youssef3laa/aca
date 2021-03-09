<template>
  <splitpanes style="height: auto; direction: ltr" class="default-theme">

    <pane style="direction: rtl">
        <AlertComponent ref="alertComponent"></AlertComponent>
        <AppBuilder v-show="sidebarItem == 'viewSubjects'" ref="subjects" :app="subjects"/>
      <AppBuilder v-show="sidebarItem == 'viewSentMemos'" ref="outbox" :app="outbox"/>
    </pane>
    <pane style="height: auto; direction: rtl" max-size="14" min-size="10" size="14">
      <Sidebar :val="sidebarItems" @btnClicked="updateView"></Sidebar>
    </pane>
  </splitpanes>
</template>

<script>
import router from "../../../router";
import http from "../../core-module/services/http";
import "splitpanes/dist/splitpanes.css";
import {Pane, Splitpanes} from "splitpanes";
import AppBuilder from "../builders/app-builder";
import Sidebar from "../../application-builder-module/components/sidebar-component";
import formPageMixin from "../../../mixins/formPageMixin";
// import historyMixin from "../../history-module/mixin/historyMixin";
import incomingRegistrationMixin from "../../incoming-registration-module/mixins/incomig-registration-mixin";
import opinionsMixin from "../../../mixins/opinionsMixin";
import signatureMixin from "../../graphs-module/mixin/signatureMixin";
// import Topbar from "../../application-builder-module/components/topbar-component"

export default {
  mixins: [formPageMixin, incomingRegistrationMixin, opinionsMixin, signatureMixin],
  components: { Sidebar, Splitpanes, Pane, AppBuilder},
  props: ["val", "field"],
  mounted() {
    console.log("app", JSON.stringify(this.app));
    this.loadForm("COC-inbox-subjects", this.fillSubjects, "subjects");
    this.loadForm("COC-outbox", this.fillOutbox, "outbox")
      this.$observable.subscribe("viewed-done", () => {
          console.log("viewed-done");
          let inputSchema = this.selectedSubject.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment;
          console.log("inputSchema", inputSchema);

          let objectToCompleteStepBy = {
              taskId: this.selectedSubject.TaskId,
              requestId: inputSchema.requestId,
              stepId: inputSchema.stepId,
              process: inputSchema.process,
              parentHistoryId: inputSchema.parentHistoryId,
              caseType : "signatures",
              decision: "send",
              extraData: inputSchema.extraData,
              receiverType: "single"
          };
          // noinspection EqualityComparisonWithCoercionJS
          if (this.$user.details.groups[0].unit.unitCode == "VCC") {
              objectToCompleteStepBy.assignedCN = "cn=SVCC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";

          } else if (this.$user.details.groups[0].unit.unitCode == "COC") {
              objectToCompleteStepBy.assignedCN = "cn=SCOC,cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
              // this.$observable.fire("save-signature", {
              //     callback: ({status,signatureEntityId}) => {
              //         if (!status) {
              //             this.$refs.alertComponent._alertSuccess({
              //                 type: "error",
              //                 message: 'pleaseEnterSignature',
              //             })
              //             return;
              //         }
              //         if (objectToCompleteStepBy.extraData == undefined || objectToCompleteStepBy.extraData == null)
              //             objectToCompleteStepBy.extraData = {signatureEntityId};
              //         else {
              //             objectToCompleteStepBy.extraData.signatureEntityId = signatureEntityId;
              //         }
              //         // this.completeStep(objectToCompleteStepBy)
              //     },
              //     // signatureEntityId: inputSchema?.extraData?.signatureEntityId
              //     signatureEntityId: "376836"
              // })
          }
          this.$observable.fire("save-signature", {
              callback: ({status, signatureEntityId}) => {
                  if (!status) {
                      this.$refs.alertComponent._alertSuccess({
                          type: "error",
                          message: 'pleaseEnterSignature',
                      })
                      return;
                  }
                  if (objectToCompleteStepBy.extraData == undefined || objectToCompleteStepBy.extraData == null)
                      objectToCompleteStepBy.extraData = {signatureEntityId};
                  else {
                      objectToCompleteStepBy.extraData.signatureEntityId = signatureEntityId;
                  }
                  this.completeStep(objectToCompleteStepBy)
              },
              signatureEntityId: inputSchema?.extraData?.signatureEntityId
          })

      });


    this.$observable.subscribe("allSubjectsTable_view", (item) => {
      this.viewTask(item);
    });
    // this.$observable.subscribe("outboxTable_view", (item) => {
    //   this.viewSentTask(item);
    // });
        this.$observable.subscribe(
      "outboxTable_selected",
      (selected) => {
        this.selectedCertfication = selected;
        console.log(this.selected);
        if (this.selectedCertfication.length != 0) {
          this.$refs.appBuilder.setFieldData(
            "outboxForm",
            "actionTopComponent",
            {
              show: true,
            }
          );
        } else {
          this.$refs.appBuilder.setFieldData(
            "outboxForm",
            "actionTopComponent",
            {
              show: false,
            }
          );
        }
      }
    );
  },
  data() {
    return {
      selectedSubject: {},
      response: [],
      subjects: {},
      outbox: {},
      sidebarItem: "viewSubjects",
      sidebarItems: [
        {
          name: "subjects",
          notifications: 0,
          icon: "fas fa-download",
          action: "viewSubjects",
        },
        {
          name: "outgoingMemos",
          notifications: 0,
          icon: "far fa-paper-plane",
          action: "viewSentMemos",
        },
      ],
    };
  },
  methods: {
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
    viewSentTask(item) {
      try {
        let requestId = item.item.requestId;
        let page = item.item.readonlyComponent;
        router.push({
          name: page,
          params: { requestId: requestId },
        });
      } catch (e) {
        console.error(e);
      }
    },
    fillSubjects: function() {
      http.get("workflow/human/tasks").then((response) => {
        let data = response.data.data;
        this.selectedSubject=data[1];
        this.sidebarItems[0].notifications = data.length;
        this.$refs.subjects.setTabValue("tab2", data.length + "")
        this.$refs.subjects.setModelData("allSubjectsForm", {allSubjectsTable: {data: data}});
        this.fillSelectedSubject();
      });
    },
    fillOutbox: function() {
      http.get('history/user/count').then((response) => {
        if(!response.data.data) return;
        this.sidebarItems[1].notifications = response.data.data
      });
    },
    updateView($event) {
      if ($event == "viewSubjects") {
        this.sidebarItem = "viewSubjects";
      } else if ($event == "viewSentMemos") {
        this.sidebarItem = "viewSentMemos";
      }
    },

      fillSelectedSubject: async function () {
          let inputSchema = this.selectedSubject.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragment
          await this.claimTask(this.selectedSubject.TaskId, inputSchema.requestId)
          let incomingRegistration = await this.readIncomingRegistration(inputSchema.entityId)
          // this.$refs.appBuilder.setModelData("mainData", incomingRegistration)
          // let incomingCase = await this.readIncomingCase(incomingRegistration.jobEntityId)
          // this.$refs.appBuilder.setModelData("caseData", incomingCase)

          console.log();
          console.log("incomingRegistration",incomingRegistration)
          this.$refs.subjects.setModelData("labels", {
              "incomingNumber": incomingRegistration.incomingNumber,
              "incomingDate": incomingRegistration.incomingDate,
              "subjectSource": incomingRegistration.responsibleEntityEdaraTxt,
              "sendDate": this.selectedSubject["DeliveryDate"]
          }) ;
          this.$refs.subjects.setModelData("subjectForm", {
              "notes": incomingRegistration.subject
          })

          this.$refs.subjects.setModelData("subjectSummaryfrom", {
              "notes": incomingRegistration.topicSummary
          })
          let viceOrHead;
          if (this.$user.details.groups[0].unit.unitCode == "VCC") viceOrHead = 2; else if (this.$user.details.groups[0].unit.unitCode == "COC") viceOrHead = 1
          this.$refs.subjects.setModelData("signature", {
              signature: {
                  incomingEntityId: inputSchema.entityId,
                  viceOrHead: viceOrHead,
                  requestId: inputSchema.requestId
              }
          })

          this.$refs.subjects.setModelData("technicalOfficeForm", {
              opinionTableModel: {
                  url: "opinion/" + inputSchema.requestId,
                  headers: [
                      {
                          text: "name",
                          align: "start",
                          value: "displayName",
                      },
                      {
                          text: "theEntity",
                          value: "unitName",
                      },
                      {
                          text: "date",
                          value: "opinionDate",
                      }
                  ],
                  subHeaders: [{
                      text: "opinion",
                      value: "opinion"
                  }],
                  key: "id",
                  data: []
              },
              "notes": ""
          })
          this.$observable.fire("clear-signature");
      }
  },
};
</script>

<style></style>
