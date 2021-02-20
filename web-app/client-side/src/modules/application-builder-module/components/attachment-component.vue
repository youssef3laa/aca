<template>
    <span>
        <splitpanes class="default-theme"
                    dir="ltr">
            <pane dir="rtl"
                  style="background: white">
                <IframeComponent :val="iframeOjbect"> </IframeComponent>
            </pane>
            <pane dir="rtl"
                  style="background: white">
                <v-container v-if="!readOnly"
                             id="inputFileContainer"
                             class="input-file-prim"
                             title="Click to grap a file from your PC!">
                    <input id="fileInput"
                           multiple
                           style="display: none"
                           type="file"/>

                    <v-row align="center"
                           justify="center">
                        <v-icon color="outline"
                                style="margin: 10px 10px 0 0; padding: 5px 10px">mdi-cloud-upload
                        </v-icon>
                    </v-row>
                    <v-row align="center"
                           justify="center">
                        <span style="margin: 10px 10px 0 0; padding: 5px 10px">قم بتحميل الملفات أو <a
                            color="outline">اضغط هنا</a>
                        </span>
                    </v-row>
                </v-container>
                <br/>
                <span v-if="!readOnly">
                    <span>الملفات المعلقة</span>
                    <v-container>
                        <v-alert border="left"
                                 color="outline"
                                 colored-border
                                 dense
                                 icon="mdi-information-outline"
                                 text>
                            {{ $t("pleaseChooseFileTypeToCompleteFileUpload") }}
                        </v-alert>

                        <v-row v-for="(file, index) in files"
                               :key="index">
                            <v-col cols="5">
                                <v-text-field v-model="file.file.name"
                                              color="outline"
                                              hide-details
                                              outlined
                                              readonly
                                              v-bind:label="$t('nameOfTheFile')">
                                    <v-icon slot="append"
                                            color="red"
                                            @click="file.removeFile({ file, index })">mdi-close-circle-outline</v-icon>
                                </v-text-field>
                            </v-col>

                            <v-col cols="5">
                                <v-autocomplete v-model="file.fileTypeSelected"
                                                :items="fileTypes"
                                                clearable
                                                color="outline"
                                                hide-details
                                                hide-no-data
                                                open-on-clear
                                                outlined
                                                small-chips
                                                v-bind:label="$t('typeOfTheFile')">
                                </v-autocomplete>
                            </v-col>

                            <v-col cols="2">
                                <v-btn style="height: 100%"
                                       @click="uploadFile({ file, index })">
                                    <v-icon>mdi-plus</v-icon>
                                </v-btn>
                            </v-col>
                        </v-row>
                    </v-container>
                </span>
                <span>
                    <span>الملفات</span>
                    <v-container>
                        <draggable :animation="150"
                                   :swapThreshold="0.5"
                                   class="row"
                                   tag="div"
                                   @end="onEnd($event)">
                            <div v-for="(file, index) in filesUploaded"
                                 :key="index"
                                 class="col-12"
                                 @dragstart="startDrag($event, file)"
                                 @dragover.prevent
                                 @dragenter.prevent>
                                <div class="card"
                                     max-height="100%"
                                     min-height="100%"
                                     v-bind:class="{ 'selected-card': file.isActive }">
                                    <div class="row pa-1"
                                         style="height: 100px">
                                         <v-col :cols="1"
                                               style="align-self: center">
                                            <v-icon style="float:left; color: #D1D1D1" size="20">fas fa-ellipsis-v</v-icon>
                                            <v-icon style="float:left; color: #D1D1D1" size="20">fas fa-ellipsis-v</v-icon>
                                        </v-col>
                                        <v-col v-if="file.properties.name.indexOf('.pdf')!=-1" :cols="1"
                                               style="align-self: center">
                                            <v-icon style="color=#D1D1D1" size="60">fas fa-file-pdf</v-icon>
                                        </v-col>
                                        <v-col v-else-if="excelExtension.some(substring=>file.properties.name.includes(substring))" :cols="1"
                                               style="align-self: center">
                                            <v-icon style="color=#D1D1D1" size="60">fas fa-file-excel</v-icon>
                                        </v-col>
                                        <v-col  v-else-if="wordExtension.some(substring=>file.properties.name.includes(substring))" :cols="1"
                                               style="align-self: center">
                                            <v-icon  style="color=#D1D1D1" size="60">fas fa-file-word</v-icon>
                                        </v-col>
                                        <v-col v-else-if="imageExtension.some(substring=>file.properties.name.includes(substring))" :cols="1"
                                               style="align-self: center">
                                            <v-icon style="color=#D1D1D1" size="60">fas fa-file-image</v-icon>
                                        </v-col>
                                        <v-col v-else :cols="1"
                                               style="align-self: center">
                                            <v-icon style="color=#D1D1D1" size="60">fas fa-file</v-icon>
                                        </v-col>
                                        <v-col :cols="8"
                                               class="card-name"
                                               style="cursor: pointer; align-self: center"
                                               @click="openFileInBrave({ fileId: file.properties.id });">
                                            {{ file.properties.name }} <br/>
                                            {{ file.properties.fileTypeValue }}
                                        </v-col>
                                        <v-col :cols="readOnly? 2 : 1"
                                               style="cursor: pointer; align-self: center"
                                               @click="openVersionsPopup(file)">
                                            <v-icon color="#22B07D"
                                                    size="25">
                                                mdi-folder-multiple</v-icon>
                                        </v-col>
                                        <v-col v-if="!readOnly"
                                               :cols="1"
                                               style="cursor: pointer; align-self: center"
                                               @click="deleteFile(file)">
                                            <v-icon color="#ea9cb3"
                                                    size="25">
                                                fas fa-trash-alt</v-icon>
                                        </v-col>
                                    </div>
                                </div>
                            </div>
                        </draggable>
                    </v-container>
                </span>

            </pane>
        </splitpanes>

        <FileVersionsModalComponent :dialogState="versionsDialogState"
                                    :modalTitle="selectedFile.modalTitle"
                                    :nodeIdVal="selectedFile.nodeId"
                                    @openVersionInBrava="openVersionInBrava"
                                    @versionsModalClosed="versionsModalClosed">
        </FileVersionsModalComponent>
    </span>
</template>

<script>
import draggable from "vuedraggable";
import IframeComponent from "../components/iframe-component";
import attachmentMixin from "@/mixins/attachmentMixin";
import {Pane, Splitpanes} from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import formPageMixin from "@/mixins/formPageMixin";
import FileVersionsModalComponent from "@/modules/application-builder-module/components/file-versions-modal-component";

export default {
  components: {
    FileVersionsModalComponent,
    draggable,
    IframeComponent,
    Splitpanes,
    Pane,
  },

  name: "attachment-component",
  props: ["val", "field"],
  mixins: [attachmentMixin, formPageMixin],
  data() {
    return {
      imageExtension: [".tif", ".tiff", ".bmp", ".jpg", ".jpeg", ".gif", ".png", ".eps"],
      excelExtension: [".xlsx", ".xlsm", ".xlsb", ".xltx", ".xls", ".xlt", "xlm"],
      wordExtension: [".doc", ".docx", ".docm", ".dotx", ".dotm", ".docb"],
      valid: this.filesUploaded > 0,
      readOnly: (this.field.readonly)? this.field.readonly:this.val.readonly,
      iframeOjbect: {src: ""},
      bwsId: "",
      categoryId: "",
      files: [],
      requestEntityId: "",
      filesUploaded: [],
      attachmentSortList: [],
      fileTypes: [],
      versionsDialogState: false,
      selectedFile: {modalTitle: "", nodeId: ""},
    };
  },
  created() {
    this.bwsId = 680482;
    this.requestEntityId = 1;
    this.categoryId = 686057;
  },

  async mounted() {
    const dropzone = this.$el.querySelector("#inputFileContainer");
    console.log(this.$el);
    const fileUpload = this.$el.querySelector("#fileInput");
    console.log(fileUpload);

    if (dropzone) {
      dropzone.addEventListener("dragenter", (e) => {
        e.preventDefault();
        this.dragover = true;
      });
      dropzone.addEventListener("dragleave", (e) => {
        e.preventDefault();
        this.dragover = false;
      });
      dropzone.addEventListener("dragover", (e) => {
        e.preventDefault();
        this.dragover = true;
      });
      dropzone.addEventListener("drop", (e) => {
        e.preventDefault();
        const dragevent = e;
        if (dragevent.dataTransfer) {
          this.fillFilesArray(dragevent.dataTransfer.files);
        }
      });
      dropzone.addEventListener("click", () => {
        fileUpload.click();
      });

      if (fileUpload) {
        fileUpload.addEventListener("change", (e) => {
          const target = e.target;
          if (target.files && target.files.length > 0) {
            this.fillFilesArray(target.files);
          }
        });
      }
    }

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

        this.$observable.subscribe("refreshAttachmentFiles", this.listFiles);
        this.$observable.subscribe("attachmentValidationAsk", function () {
            this.$observable.fire("attachmentValidationAnswer", this.valid);
        });
    },
    methods: {
        versionsModalClosed() {
            this.versionsDialogState = false;
            this.selectedFile.nodeId = "";
            this.selectedFile.modalTitle = "";
        },
        openVersionInBrava(obj) {
            this.openFileInBrave(obj);
        },

    },
    watch: {
        'val.readonly': {
            handler: function (newVal) {
                this.readOnly = newVal;
            }
        },
        filesUploaded: {
            handler: function (newVal) {
              console.log(newVal)
                this.valid = newVal.length > 0;
            }
        }

  }
};
</script>

<style lang="scss" scoped>
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

.card {
  border: 2px solid #d6d6d6;
  border-radius: 6px;
  margin: 10px;
}

/* On mouse-over, add a deeper shadow */
.card:hover {
  border: 2px solid #0278ae;
}

.selected-card {
  border: 2px solid #0278ae;
}

// .card-icon {
//   height: 40px;
//   padding: 5px;
//   border-radius: 6px;
//   vertical-align: middle;
//   text-align: center;
// }

// .card-icon i {
//   font-size: 40px !important;
// }

.vertical-scroll {
  flex-wrap: unset !important;
  overflow-y: hidden;
  overflow-x: scroll;
}
</style>
