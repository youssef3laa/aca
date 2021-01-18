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
      <pane> <IframeComponent> </IframeComponent></pane
    ></splitpanes>
  </div>
</template>

<script>
import IframeComponent from "./iframe-component.vue";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
// import InputFileComponent from "./input-file-component"
import AttachmentComponent from "./attachment-component";
export default {
  props: ["bwsId", "requestEntityId"],
  components: {
    IframeComponent,
    Splitpanes,
    Pane,
    AttachmentComponent,
    // InputFileComponent
  },
};
</script>

<style>
.bg-white {
  background-color: white !important;
}
</style>