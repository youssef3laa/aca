
<template>
  <div style="width:100%">
    <v-container>
      <v-row>
        <v-col class="padding-left" cols="5">
          <AppBuilder ref="tableAppBuilder" :app="app" />
        </v-col>
        <v-col class="no-padding" cols="7">
          <AppBuilder v-show="itemIsSelected" ref="appBuilder" :app="app1" />
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

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mixins: [formPageMixin,orgChartMixin],

  mounted() {
    this.loadForm(
      "secretary-incoming-signatures-table",
      this.formLoaded,
      "tableAppBuilder"
    );
    this.loadForm("secretary-incoming-signatures-form", null, "appBuilder");
    this.submit()
    this.getTasks("signatures");

    this.$observable.subscribe("signaturesTable_selected", async (selected) => {
      console.log(selected);
      this.selected=selected;
      if (selected.length != 0) {
        this.itemIsSelected = true;
        let requestData = await this.readRequest(
          selected[selected.length - 1].TaskData.ApplicationData
            .ACA_ProcessRouting_InputSchemaFragment.requestId
        );

        console.log(requestData);
        this.$refs.appBuilder.setModelData("mainData", {
          followUpNumber:
            selected[selected.length - 1].TaskData.ApplicationData
              .ACA_ProcessRouting_InputSchemaFragment.requestNumber,
          followUpDate: requestData.requestDate,
          nextFollowUpDate: requestData.requestDate,
        });
      } else {
        this.itemIsSelected = false;
      }
    });
  },
  methods: {
    formLoaded() {
      http.get("workflow/human/tasks").then((response) => {
        console.log(response);
        let data = response.data.data;

        let fromSignatures = [];

        for (let key in data) {
          // if (
          //   data[key].TaskData.ApplicationData
          //     .ACA_ProcessRouting_InputSchemaFragment.caseType == "signatures"
          // ) {
          fromSignatures.push(data[key]);

          // }
        }

        this.$observable.fire("secretarySignatures", {
          type: "modelUpdate",
          model: { data: fromSignatures },
        });
      });
    },
    submit() {
      this.$observable.subscribe("complete-step", async () => {
        if (this.selected.length != 0) {
          let data =this.$refs.appBuilder.getModelData("mainData")
          console.log(data);
          let group = await this.getHeadRoleByUnitCode("TCS");
          let assignedCN =
            "cn=" +
            group.groupCode +
            ",cn=organizational roles,o=aca,cn=cordys,cn=defaultInst,o=example.com";
          let task = this.selected[0];
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
            extraData: data
          };

          this.completeStep(obj);
          console.log(obj);
          console.log(this.selected);
        }
      });
    },
  },
  data() {
    return {
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
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
