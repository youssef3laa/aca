<template>
  <div style="width:100%">
    <v-container>
      <AppBuilder ref="appBuilder" :app="app" />
    </v-container>
  </div>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import http from "../../core-module/services/http";
import router from "../../../router";

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mixins: [formPageMixin],
  mounted() {
    this.loadForm("secertary-inbox", this.formLoaded);
    this.$observable.subscribe("sentFromAdministratorTable_view", (item) => {
      this.viewTask(item)
    });
  },
  data() {
    return {
      app: {},
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
    };
  },

  methods: {
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
    formLoaded() {
      http.get("workflow/human/tasks").then((response) => {
        console.log(response);
        let data = response.data.data;

        let fromAdmins = [];
        let fromSignatures = [];

        for (let key in data) {
          switch (
            data[key].TaskData.ApplicationData?.ACA_ProcessRouting_InputSchemaFragment?.caseType
          ) {
            case "signatures":
              fromSignatures.push(data[key]);
              break;
            default:
              // "sentFromAdministratorsSecretary"
              fromAdmins.push(data[key]);
          }
        }

        this.$refs.appBuilder.setTabValue(
          "sentFromAdministratorTab",
          fromAdmins.length + ""
        );
        this.$observable.fire("sentFromAdministrator", {
          type: "modelUpdate",
          model: { data: fromAdmins },
        });

        this.$refs.appBuilder.setTabValue(
          "signaturesTab",
          fromSignatures.length + ""
        );
      });
    },
  },
};
</script>
