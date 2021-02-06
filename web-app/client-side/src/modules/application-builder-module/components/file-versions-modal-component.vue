<template>
  <v-dialog
    v-model="dialog"
    eager
    width="978px"
    @click:outside="modalClickOutside"
  >
    <v-card>
      <v-row>
        <v-col :cols="10">
          <v-card-title class="headline">{{ modalTitle }} </v-card-title>
        </v-col>
        <v-col :cols="2">
          <span
            style="
              cursor: pointer;
              text-align: left;
              padding: 25px 30px;
              float: left;
            "
            @click="modalClickOutside"
            ><v-icon> fas fa-times</v-icon>
          </span>
        </v-col>
      </v-row>
      <span>
        <VersionGridComponent
          :val="{ nodeId: nodeId }"
          @openVersionInBrava="openVersionInBrava"
        >
        </VersionGridComponent>
      </span>
    </v-card>
  </v-dialog>
</template>

<script>
import VersionGridComponent from "@/modules/application-builder-module/components/VersionGridComponent";

export default {
  name: "file-versions-modal-component",
  components: { VersionGridComponent },
  data() {
    return {
      dialog: this.dialogState,
      nodeId: this.nodeIdVal,
    };
  },
  watch: {
    nodeIdVal: function (newVal) {
      this.nodeId = newVal;
    },
    dialogState: function (newVal) {
      console.log("dialogState", newVal);
      this.dialog = newVal;
    },
    modalTitle: function (newVal, oldVal) {
      console.log("modalTitle", newVal, oldVal);
    },
  },
  props: ["dialogState", "nodeIdVal", "modalTitle"],
  mounted: function () {},
  methods: {
    modalClickOutside() {
      this.$emit("versionsModalClosed");
    },
    openVersionInBrava(obj) {
      this.$emit("versionsModalClosed");
      this.$emit("openVersionInBrava", obj);
    },
  },
};
</script>

<style scoped>
</style>