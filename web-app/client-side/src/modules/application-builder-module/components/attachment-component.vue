<template>
  <v-container>
    <v-container>
      <v-row>
        <v-col :cols="12">
          <draggable
            :animation="150"
            :list="filesUploaded"
            :swapThreshold="0.5"
            class="row vertical-scroll"
            tag="div"
            @change="onChange"
            @end="onEnd($event)"
          >
            <div
              v-for="(file, index) in filesUploaded"
              :key="index"
              class="card col-5"
              @dragstart="startDrag($event, file)"
              @dragover.prevent
              @dragenter.prevent
            >
              <div class="row pa-1">
                <v-col :cols="2" class="card-icon">
                  <v-icon> mdi-file-pdf-outline</v-icon>
                </v-col>
                <v-col
                  :cols="8"
                  class="card-name"
                  style="cursor: pointer"
                  @click="openFileInBrave(file)"
                >
                  {{ file.name }} <br />
                  {{ file.size_formatted }}
                </v-col>
                <v-col
                  :cols="2"
                  style="cursor: pointer"
                  @click="deleteFile(file)"
                >
                  <v-icon color="#ea9cb3"> mdi-delete-circle-outline</v-icon>
                </v-col>
              </div>
            </div>
          </draggable>
        </v-col>
      </v-row>
    </v-container>
  </v-container>
</template>

<script>
import draggable from "vuedraggable";
import Http from "../../core-module/services/http";

export default {
  components: { draggable },

  name: "file-input-component",
  props: ["val", "field","bwsId","requestEntityId"],
  data() {
    return {
      // bwsId: "",
      files: [],
      // requestEntityId: "",
      filesUploaded: [],
      attachmentSortList: [],
    };
  },
  created() {
    // this.bwsId = 680482;
    // this.requestEntityId = 1;
    console.log(this.bwsId);
  },
  methods: {
    onChange: function (evt) {
      console.log("onChange", evt);
    },
    onEnd: function () {
      let tempArr = [];
      for (let i = 0; i < this.filesUploaded.length; ++i) {
        let element = this.filesUploaded[i];
        let attachmentSortElement = this.attachmentSortList.find(
          (val) => val.fileId == element.id
        );
        attachmentSortElement.position = i;
        tempArr.push(attachmentSortElement);
      }
      this.updateMultipleAttachmentSortRecords(tempArr);
    },
    openFileInBrave: function (file) {
      let fileId = this.filesUploaded.find(
        (element) => element.name === file.name
      ).id;
      this.$observable.fire("open-memo-file-brava", fileId);
    },
    deleteFile: async function (file) {
      if (file == undefined || file.id == undefined) return;
      try {
        await Http.delete("/document/delete/" + file.id);
        this.filesUploaded = this.filesUploaded.filter(
          (element) => element.id != file.id
        );
        this.files = this.files.filter((fileVal) => fileVal.name != file.name);
        let attachmentSortId;
        this.attachmentSortList = this.attachmentSortList.filter((value) => {
          if (value.fileId != file.id) {
            return true;
          } else {
            attachmentSortId = value.id;
            return false;
          }
        });
        if (!attachmentSortId)
          await Http.delete("/document/sort/" + attachmentSortId);
        let tempArr = [];
        for (let i = 0; i < this.filesUploaded.length; ++i) {
          let element = this.filesUploaded[i];
          let attachmentSortElement = this.attachmentSortList.find(
            (val) => val.fileId == element.id
          );
          attachmentSortElement.position = i;
          tempArr.push(attachmentSortElement);
        }
        this.updateMultipleAttachmentSortRecords(tempArr);
      } catch (e) {
        console.error(e);
      }
    },
    startDrag: function (evt, file) {
      evt.dataTransfer.dropEffect = "move";
      evt.dataTransfer.effectAllowed = "move";
      evt.dataTransfer.setData("itemID", file.id);
    },

    listFiles: async function () {
      let nodesResponse, attachmentSortResponse;
      try {
        nodesResponse = await Http.get(
          "/document/list/" + this.bwsId + "?fields=properties"
        );
        attachmentSortResponse = await Http.get("/document/sort", {
          params: {
            requestEntityId: this.requestEntityId,
            bwsId: this.bwsId,
          },
        });
      } catch (e) {
        console.log(e);
      }
      if (!nodesResponse) return;
      let nodeResults = nodesResponse.data.data.results;
      this.attachmentSortList = attachmentSortResponse.data.data;

      nodeResults.forEach((val) => {
        let attachmentSortElementObj;
        if (
          (attachmentSortElementObj = this.attachmentSortList.find(
            (attachmentElement) =>
              attachmentElement.fileId == val.data.properties.id
          )) != null
        ) {
          val.data.properties.position = attachmentSortElementObj.position;
          attachmentSortElementObj.exists = true;
        }
      });
      nodeResults.sort((a, b) =>
        a.data.properties.position > b.data.properties.position ? 1 : -1
      );
      let itemsToPost = [];
      nodeResults.forEach((val, i) => {
        // noinspection EqualityComparisonWithCoercionJS
        if (val.data.properties.position == undefined) {
          val.data.properties.position = i;
          let itemToPost = {
            fileId: val.data.properties.id,
            bwsId: this.bwsId,
            requestEntityId: this.requestEntityId,
            position: i,
          };
          itemsToPost.push(itemToPost);
        }
      });
      if (itemsToPost.length > 0) {
        try {
          let documentSortResponse = await Http.post(
            "/document/sort/bulk",
            itemsToPost
          );
          documentSortResponse.data.data.forEach((element) => {
            element.exists = true;
            this.attachmentSortList.push(element);
          });
        } catch (e) {
          console.log(e);
        }
      }
      let attachmentSortListsToBeDeleted = this.attachmentSortList.filter(
        (element) => !element.exists
      );
      let ids = attachmentSortListsToBeDeleted
        .map((element) => element.id)
        .join(",");
      if (attachmentSortListsToBeDeleted.length > 0) {
        try {
          await Http.delete("/document/sort/bulk/" + ids);
        } catch (e) {
          console.log(e);
        }
      }
      nodeResults.forEach((val) =>
        this.filesUploaded.push(val.data.properties)
      );
    },

    updateMultipleAttachmentSortRecords: function (arr) {
      Http.post("/document/sort/update/bulk", arr)
        .then(() => console.log("finished updating position in backend"))
        .catch((reason) => console.error(reason));
    },
  },
  mounted() {
    this.listFiles();
  },
};
</script>

<style lang="scss">
/*colors*/
$color-prim-font: #1a1a2e;
$color-prim-border: #94bed6;
$color-prim-bg: #f2f7fa;
$color-secondary: #9e9e9e;
/*fonts */
$font-12: 12px;

.card-name {
  font-size: $font-12;
  color: $color-prim-font;
}

.card-icon {
  height: 40px;
  padding: 5px;
  border-radius: 6px;
  vertical-align: middle;
  text-align: center;
}

.card-icon i {
  font-size: 35px !important;
  padding-top: 8px;
}
.vertical-scroll{
  flex-wrap: unset !important;
  overflow-y: hidden;
  overflow-x: scroll ;
}
</style>
