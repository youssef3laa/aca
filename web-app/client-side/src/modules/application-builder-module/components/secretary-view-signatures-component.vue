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

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mixins: [formPageMixin],

  mounted() {
    this.loadForm("secretary-incoming-signatures-table", "tableAppBuilder");
    this.loadForm("secretary-incoming-signatures-form", "appBuilder");

    this.getTasks("signatures");

    this.$observable.subscribe("signaturesTable_selected", async (selected) => {
      console.log(selected);

      if (selected.length !=0) {
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
      }
      else{
        this.itemIsSelected=false;
      }
    });
  },
  methods: {
        submit(){
      this.selected.forEach((item)=>{
        this.inputSchemaArray.push(item.taskData.TaskData.ApplicationData.ACA_ProcessRouting_InputSchemaFragments)
      })
    },
    loadForm: function(formName, appBuilder, callBack) {
      http
        .get("/user/form/" + formName)
        .then((response) => {
          this.$refs[appBuilder].setAppData(response.data.data.app);
          if (callBack) {
            callBack();
          }
        })
        .catch((error) => console.error(error));
    },
  },
  data() {
    return {
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
      app: {},
      app1: {},
      selected:[],
      inputSchemaArray:[],
      itemIsSelected: false,
    };
  },
};
</script>

<style scoped>
.empty-form{
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
