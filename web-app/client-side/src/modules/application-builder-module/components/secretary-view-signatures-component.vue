<template>
  <div style="width:100%">
    <v-container>
      <v-row>
        <v-col class="padding-left" cols="5">
          <AppBuilder ref="appBuilder" :app="app" />
        </v-col>
        <v-col class="no-padding" cols="7">
          <AppBuilder ref="appBuilder1" :app="app1" />
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import http from "../../core-module/services/http"
export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
  },
  mounted() {
    console.log(JSON.stringify(this.app));
    console.log(JSON.stringify(this.app1));
    this.loadForm("secretary-incoming-signatures-table","appBuilder");
    this.loadForm("secretary-incoming-signatures-form","appBuilder1");

  },
  methods: {
    loadForm: function(formName,appBuilder, callBack) {
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
      app:{},
      app1:{}
    };
  },
};
</script>

<style scoped>
.no-padding {
  padding: 0px !important;
}
.padding-left {
  padding: 0px !important;
  padding-left: 1px !important;
  border-left: 1px solid #d1d1d1 !important;
}
</style>
