<template>
  <div style="width:100%">
    <v-container>
      <AppBuilder ref="appBuilder" :app="app" />
    </v-container>
  </div>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
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