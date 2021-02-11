<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"/>
    <AlertComponent ref="alertComponent"></AlertComponent>
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "incoming-registration-init",
  mixins: [formPageMixin],
  components: {
    AppBuilder
  },
  data() {
    return {
      app: {},
      model: {},
      request: undefined
    };
  },
  async created() {
    this.request = await this.getRequest()
    this.loadForm("incoming-registration-init", this.formLoaded);
    this.$observable.subscribe("complete-step", this.submit);
  },
  methods: {
    formLoaded: function(){
      this.$refs.appBuilder.setModelData("mainData",
    {
          incomingNumber: this.request.requestNumber,
          maxDate: new Date().toISOString().split("T")[0],
        }
      )
    },
    submit: function () {
      
    },
  }
};
</script>