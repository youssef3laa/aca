<template>
  <span>
    <v-card-subtitle> الإصدار الأخير </v-card-subtitle>

    <v-card-text v-if="latestVersion != null">
      <v-row>
        <v-col :cols="6">
          <div class="card pa-1">
            <v-row>
              <v-col :cols="2" class="card-icon">
                <v-icon> mdi-file-pdf-outline</v-icon>
              </v-col>
              <v-col :cols="8" class="card-name" style="cursor: pointer">
                {{ latestVersion.name }} <br />
                {{ latestVersion.modify_date }}
              </v-col>
              <v-col
                :cols="2"
                style="cursor: pointer; align-self: center"
                @click="openVersionInBrava(latestVersion)"
              >
                <v-icon color="#07689F"> mdi-eye-outline</v-icon>
              </v-col>
            </v-row>
          </div>
        </v-col>
      </v-row>
    </v-card-text>

    <v-card-subtitle> الإصدارات السابقة </v-card-subtitle>
    <v-card-text>
      <v-row>
        <v-col v-for="(file, index) in fileVersions" :key="index" :cols="6">
          <div class="card pa-1">
            <v-row>
              <v-col :cols="2" class="card-icon">
                <v-icon> mdi-file-pdf-outline</v-icon>
              </v-col>
              <v-col :cols="8" class="card-name" style="cursor: pointer">
                {{ file.name }} <br />
                {{ file.modify_date }}
              </v-col>
              <v-col
                :cols="2"
                style="cursor: pointer; align-self: center"
                @click="openVersionInBrava(file)"
              >
                <v-icon color="#07689F"> mdi-eye-outline</v-icon>
              </v-col>
            </v-row>
          </div>
        </v-col>
      </v-row>
    </v-card-text>
  </span>
</template>

<script>
import Http from "../../core-module/services/http";

export default {
  name: "VersionGridComponent",
  data() {
    return {
      fileVersions: [],
      latestVersion: null,
      nodeId: this.val.nodeId,
    };
  },
  props: ["val", "field"],
  watch: {
    val: {
      deep: true,
      handler: function (newValue, oldValue) {
        console.log(newValue, oldValue);
        this.nodeId = newValue.nodeId;
        this.getFileVersions();
      },
    },

    // val: function (newVal, oldVal) {
    //   console.log(oldVal, newVal);
    //   // if (newVal.nodeId && (newVal.nodeId !== oldVal.nodeId)) {
    //   this.nodeId = newVal.nodeId;
    //   this.getFileVersions();
    //   // }F
    // },
  },
  methods: {
    getFileVersions: async function () {
      this.fileVersions = [];
      this.latestVersion = null;
      let fileVersionsResponse = await Http.get(
        "/document/version/list/" + this.nodeId + "?order=desc"
      );
      console.log(fileVersionsResponse.data.data.results);
      fileVersionsResponse = fileVersionsResponse.data.data.results;
      this.latestVersion = fileVersionsResponse[0].data.versions;
      for (let i = 1; i < fileVersionsResponse.length; ++i) {
        let element = fileVersionsResponse[i].data.versions;
        // element = element.modify_date.replace("T", " ");
        this.fileVersions.push(element);
      }
    },
    openVersionInBrava(file) {
      this.$emit("openVersionInBrava", {
        fileId: this.nodeId,
        verNum: file.version_number,
      });
      // this.$observable.fire("open-file-brava", {fileId: this.nodeId, verNum: file.version_number});
      // this.$observable.fire("versionModal");
    },
  },
};
</script>

<style scoped>
</style>