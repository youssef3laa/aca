<template>
  <div>
    <splitpanes horizontal class="default-theme" style="height: 600px">
      <pane class="bg-white" size="31">
        <!-- <InputFileComponent></InputFileComponent> -->
        <v-expansion-panels dir="rtl">
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-row no-gutters>
                <v-col cols="4">
                  <span>المرفقات</span>
                  <span class="line"></span>
                </v-col>
                <v-col cols="8" class="text--secondary"> </v-col>
              </v-row>
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <AttachmentComponent
                :bwsId="bwsId"
                :requestEntityId="requestEntityId"
              ></AttachmentComponent>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
      </pane>
      <pane> <IframeComponent :val="iFrameObj"> </IframeComponent></pane
    ></splitpanes>
  </div>
</template>

<script>
import IframeComponent from "./iframe-component.vue";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
// import InputFileComponent from "./input-file-component"
import AttachmentComponent from "./attachment-horizontal-component";
import http from "../../core-module/services/http";
export default {
  props: ["bwsId", "requestEntityId"],
  components: {
    IframeComponent,
    Splitpanes,
    Pane,
    AttachmentComponent,
    // InputFileComponent
  },
  data() {
    return {
      iFrameObj: {
        src: ""
      }
    }
  },
  mounted() {
    this.$observable.subscribe('open-memo-file-brava', async (fileId) => {
      this.$observable.fire('file-component-skeleton', true)
      console.log("openfilebrava");
      console.log(fileId);
      let userToken;
      try {
        userToken = await http.post("http://45.240.63.94:8081/otdsws/rest/authentication/credentials", {
          "userName": "admin",
          "password": "Asset99a",
          "ticketType": "OTDSTICKET"
        });
        this.iFrameObj.src =
                'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
        console.log(userToken);
        // this.$observable.fire('file-component-skeleton', false)
      } catch (e) {
        console.log(e);
      }
    });
  }
};
</script>

<style>
.bg-white {
  background-color: white !important;
}
</style>