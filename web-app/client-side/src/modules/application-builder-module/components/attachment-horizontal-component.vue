<template>
  <v-container>
    <v-container>
      <v-row>
        <v-col :cols="12">
          <v-icon @click="scroll(-300)" class="left-arrow">
            mdi-arrow-left</v-icon
          >
          <v-icon @click="scroll(300)" class="right-arrow">
            mdi-arrow-right</v-icon
          >
          <draggable
            ref="content"
            id="content"
            :animation="150"
            :list="filesUploaded"
            :swapThreshold="0.5"
            class="row horizontal-scroll"
            tag="div"
            @change="onChange"
            @end="onEnd($event)"
          >
            <div
              v-for="(file, index) in filesUploaded"
              :key="index"
              class="card col-6"
              @dragstart="startDrag($event, file)"
              @dragover.prevent
              @dragenter.prevent
            >
              <div class="row pa-1">
                <v-col :cols="2" class="card-icon">
                  <v-icon> mdi-file-pdf-outline</v-icon>
                </v-col>
                <v-col
                  :cols="6"
                  class="card-name"
                  style="cursor: pointer"
                  @click="openFileInBrave(file)"
                >
                  {{ file.properties.name }} <br />
                  {{ file.properties.fileTypeValue }}
                </v-col>
                <v-col :cols="2" style="cursor: pointer" @click="openVersionsPopup(file)">
                    <v-icon color="#22B07D"> mdi-folder-multiple</v-icon>
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
import formPageMixin from "../../../mixins/formPageMixin";

export default {
  components: { draggable },
  mixins: [formPageMixin],

  name: "file-input-component",
  props: ["val", "field", "bwsId", "requestEntityId"],
  data() {
    return {
      // bwsId: "",
      files: [],
      // requestEntityId: "",
      categoryId: "",
      filesUploaded: [],
      attachmentSortList: [],
      fileTypes: [],
    };
  },
  created() {
    // this.bwsId = 680482;
    // this.requestEntityId = 1;
    this.categoryId = 686057;

    console.log(this.bwsId);
  },
  methods: {
    setFileTypeOnFileUploaded: function (file) {
      let categoryValue;
      if (file.categories == undefined || file.categories.length == 0) {
        //shouldn't go here never...
        file.properties.fileTypeValue = "لم يتم إدخاله بعد";
        return;
      }
      categoryValue = file.categories[0][this.categoryId + "_2"];

      let lookupObj = this.fileTypes.find(
        (element) => element.value == categoryValue
      );
      if (lookupObj == undefined)
        file.properties.fileTypeValue = "قيمة غير معرفة";
      else file.properties.fileTypeValue = lookupObj.text;
    },
    scroll(scrollPixels) {
      const content = this.$refs.content.clientWidth;
      // content.scrollLeft -=300;
      console.log(content);
      const element = document.getElementById("content");

      // element.animate({scrollLeft: '=-300'},1000);
      var scroll = scrollPixels / 10;
      var scrolled = 0;
      const interval = setInterval(() => {
        element.scrollLeft += scroll;
        scrolled += scroll;
        if (scrolled == scrollPixels) {
          clearInterval(interval);
        }
      }, 20);
    },
    scrollRight() {
      const content = this.$refs.content;
      // content.scrollLeft -=300;
      console.log(content);
      const element = document.getElementById("content");
      element.scrollLeft += 300;
    },
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
          "/document/list/" +
            this.bwsId +
            "?fields=properties&fields=categories"
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
      nodesResponse = nodesResponse.data.data;
      this.attachmentSortList = attachmentSortResponse.data.data;

      nodesResponse.forEach((val) => {
        let attachmentSortElementObj;
        if (
          (attachmentSortElementObj = this.attachmentSortList.find(
            (attachmentElement) => attachmentElement.fileId == val.properties.id
          )) != null
        ) {
          val.properties.position = attachmentSortElementObj.position;
          attachmentSortElementObj.exists = true;
        }
        //TODO move to function
        this.setFileTypeOnFileUploaded(val);
      });
      nodesResponse.sort((a, b) =>
        a.properties.position > b.properties.position ? 1 : -1
      );
      let itemsToPost = [];
      nodesResponse.forEach((val, i) => {
        // noinspection EqualityComparisonWithCoercionJS
        if (val.properties.position == undefined) {
          val.properties.position = i;
          let itemToPost = {
            fileId: val.properties.id,
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
      this.filesUploaded = [];
      nodesResponse.forEach((val) => this.filesUploaded.push(val));
    },

    updateMultipleAttachmentSortRecords: function (arr) {
      Http.post("/document/sort/update/bulk", arr)
        .then(() => console.log("finished updating position in backend"))
        .catch((reason) => console.error(reason));
    },
        openVersionsPopup: function (file) {

      this.$observable.fire("openVersionsModal", file)

      console.log(file);
    }
  },
  async mounted() {
    let lookups = await this.getLookupByCategory("attachmentFileType");
    console.log(lookups, this.$i18n.locale);
    let langKey = this.$i18n.locale == "ar" ? "arValue" : "enValue";
    this.fileTypes = Array.from(lookups).map((element) => {
      return {
        text: element[langKey],
        value: element["key"],
      };
    });

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
.horizontal-scroll {
  flex-wrap: nowrap !important;
  overflow-y: hidden;
  overflow-x: hidden;
}
// .left-arrow {
//   position: absolute;
//   vertical-align: middle;
//   left: 0;
// }
.right-arrow {
  position: absolute !important;
  right: 0;
  top: 50%;
  margin-left: auto;
  margin-right: 1%;
  text-align: center;
}
.left-arrow {
  position: absolute !important;
  left: 0;
  top: 50%;
  margin-left: 1%;
  margin-right: auto;
  text-align: center;
}
</style>
