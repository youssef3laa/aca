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

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mixins: [formPageMixin],
  mounted() {
    this.loadForm("office-group-head-inbox");
    this.getTasks("technicalTasks");
    this.topActionsSubscriptions();
    this.$observable.subscribe("technicalTasksTable_view", (item) => {
      this.viewTask(item);
    });
    this.$observable.subscribe("technicalTasksTable_selected", (selected) => {
      this.selected = selected;
      console.log(this.selected);
      if (selected.length != 0) {
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
      console.log(text);
      this.$observable.fire("sentFromManagement", {
        type: "modelUpdate",
        model: {
          data: [
            {
              requestNumber: "Frozen Yogurt",
              incomingDate: "159",
              subject: "6.0",
              management: "24",
              importance: true,
              chairmanOfCommisionSignature:
                "https://i.picsum.photos/id/11/500/300.jpg?hmac=X_37MM-ameg7HWL6TKJT2h_5_rGle7IGN_CUdEDxsAQ",
              viceChairmanOfCommisionSignature:
                "https://i.picsum.photos/id/11/500/300.jpg?hmac=X_37MM-ameg7HWL6TKJT2h_5_rGle7IGN_CUdEDxsAQ",
            },
          ],
        },
      });
    });
    this.$observable.subscribe("incomingNumberHeadOfOfficeGroup", (text) => {
      console.log(text);
    });
  },
  data() {
    return {
      selected: [],
      inputSchemaArray: [],
      app: {},
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
    };
  },
  methods: {
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
    topActionsSubscriptions() {
      this.$observable.subscribe("sendToAnotherManagement", () => {
        console.log("sendToAnotherManagement");
      });
      this.$observable.subscribe("backToCertification", () => {
        console.log("backToCertification");
      });
      this.$observable.subscribe("temporarySave", () => {
        console.log("temporarySave");
      });
      this.$observable.subscribe("send", () => {
        console.log("send");
      });
    },
  },
};
</script>
