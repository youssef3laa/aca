<template>
  <div style="width:100%">
    <Topbar :val="{ tasks: taskList }"></Topbar>
    <v-container>
      <AppBuilder ref="appBuilder" :app="app" />
    </v-container>
  </div>
</template>

<script>
import Topbar from "../../application-builder-module/components/topbar-component";
import formPageMixin from "../../../mixins/formPageMixin";
export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
    Topbar,
  },
  mixins:[formPageMixin],
  mounted(){
    this.loadForm("secertary-inbox");
  },
  data() {
    return {
    app:{},
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
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
