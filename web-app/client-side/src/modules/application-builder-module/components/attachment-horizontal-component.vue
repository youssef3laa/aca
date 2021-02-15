<template>
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
                  @click="openHorizontalFile( {fileId: file.properties.id })"
                >
                  {{ file.properties.name }} <br />
                  {{ file.properties.fileTypeValue }}
                </v-col>
                <v-col :cols="2" style="cursor: pointer" @click="openVersionsPopup(file)">
                    <v-icon color="#22B07D"> mdi-folder-multiple</v-icon>
                  </v-col>
                 <v-col :cols="2" style="cursor: pointer" @click="retrieveMemo(file.properties.id)">
                    <v-icon color="#22B07D"> mdi-file-document-edit</v-icon>
                  </v-col>
                <!-- <v-col
                  :cols="2"
                  style="cursor: pointer"
                  @click="deleteFile(file)"
                >
                  <v-icon color="#ea9cb3"> mdi-delete-circle-outline</v-icon>
                </v-col> -->
              </div>
            </div>
          </draggable>
        </v-col>
      </v-row>
      <FileVersionsModalComponent
              :dialogState="versionsDialogState"
              :modalTitle="selectedFile.modalTitle"
              :nodeIdVal="selectedFile.nodeId"
              @openVersionInBrava="openHorizontalFile"
              @versionsModalClosed="versionsModalClosed"
      >
      </FileVersionsModalComponent>
    </v-container>
</template>

<script>
import draggable from "vuedraggable";
// import Http from "../../core-module/services/http";
import formPageMixin from "../../../mixins/formPageMixin";
import attachmentMixin from "../../../mixins/attachmentMixin";
import FileVersionsModalComponent from "./file-versions-modal-component";

export default {
  components: { draggable,FileVersionsModalComponent },
  mixins: [formPageMixin,attachmentMixin],

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
      versionsDialogState: false,
      selectedFile: {}
    };
  },
  created() {
    // this.bwsId = 680482;
    // this.requestEntityId = 1;
    this.categoryId = 686057;

    console.log(this.bwsId);
  },
  methods: {
    retrieveMemo(nodeId){
      var data={nodeId:nodeId};
      this.$observable.fire("retrieveMemo",data);
    },
    versionsModalClosed() {
      this.versionsDialogState = false;
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
    openHorizontalFile: function (file) {
      // console.log(file.properties.id);
      this.$emit("attachmentHorizontalChange", {file, contextObj: this})
    },

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

    await this.listFiles();

    this.$observable.subscribe("refreshHorizontalAttachmentFiles", this.listFiles);

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
