<template>
  <span>
    <splitpanes class="default-theme" dir="ltr">
      <pane dir="rtl" style="background: white">
        <IframeComponent :val="iframeOjbect"> </IframeComponent>
      </pane>
      <pane dir="rtl" style="background: white">
        <div>
          <v-container
            id="inputFileContainer"
            class="input-file-prim"
            title="Click to grap a file from your PC!"
          >
            <input id="fileInput" multiple style="display: none" type="file" />

            <v-row align="center" justify="center">
              <v-icon
                color="outline"
                style="margin: 10px 10px 0 0; padding: 5px 10px"
                >mdi-cloud-upload
              </v-icon>
            </v-row>
            <v-row align="center" justify="center">
              <span style="margin: 10px 10px 0 0; padding: 5px 10px"
                >قم بتحميل الملفات أو <a color="outline">اضغط هنا</a>
              </span>
            </v-row>
          </v-container>
          <br />
          <span>
            <span>الملفات المعلقة</span>
            <v-container>
              <v-alert
                border="left"
                color="outline"
                colored-border
                dense
                icon="mdi-information-outline"
                text
              >
                {{ $t("pleaseChooseFileTypeToCompleteFileUpload") }}
              </v-alert>

              <v-row v-for="(file, index) in files" :key="index">
                <v-col cols="5">
                  <v-text-field
                    v-model="file.file.name"
                    color="outline"
                    hide-details
                    outlined
                    readonly
                    v-bind:label="$t('nameOfTheFile')"
                  >
                    <v-icon
                      slot="append"
                      color="red"
                      @click="file.removeFile({ file, index })"
                      >mdi-close-circle-outline</v-icon
                    >
                  </v-text-field>
                </v-col>

                <v-col cols="5">
                  <v-autocomplete
                    v-model="file.fileTypeSelected"
                    :items="fileTypes"
                    clearable
                    color="outline"
                    hide-details
                    hide-no-data
                    open-on-clear
                    outlined
                    small-chips
                    v-bind:label="$t('typeOfTheFile')"
                  >
                  </v-autocomplete>
                </v-col>

                <v-col cols="2">
                  <v-btn
                    style="height: 100%"
                    @click="uploadFile({ file, index })"
                  >
                    <v-icon>mdi-plus</v-icon>
                    test3
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
            <span>الملفات</span>
          </span>
          <v-container>
            <draggable
              :animation="150"
              :swapThreshold="0.5"
              class="row"
              tag="div"
              @end="onEnd($event)"
            >
              <div
                v-for="(file, index) in filesUploaded"
                :key="index"
                class="col-6"
                @dragstart="startDrag($event, file)"
                @dragover.prevent
                @dragenter.prevent
              >
                <div class="card">
                  <div class="row pa-1">
                    <v-col :cols="2" class="card-icon">
                      <v-icon> mdi-file-pdf-outline</v-icon>
                    </v-col>
                    <v-col
                      :cols="6"
                      class="card-name"
                      style="cursor: pointer; align-self: center"
                      @click="openFileInBrave({ fileId: file.properties.id })"
                    >
                      {{ file.properties.name }} <br />
                      {{ file.properties.fileTypeValue }}
                    </v-col>
                    <v-col
                      :cols="2"
                      style="cursor: pointer; align-self: center"
                      @click="openVersionsPopup(file)"
                    >
                      <v-icon color="#22B07D"> mdi-folder-multiple</v-icon>
                    </v-col>
                    <v-col
                      :cols="2"
                      style="cursor: pointer; align-self: center"
                      @click="deleteFile(file)"
                    >
                      <v-icon color="#ea9cb3">
                        mdi-delete-circle-outline</v-icon
                      >
                    </v-col>
                  </div>
                </div>
              </div>
            </draggable>
          </v-container>
        </div>
      </pane>
    </splitpanes>

    <FileVersionsModalComponent
      :dialogState="versionsDialogState"
      :modalTitle="selectedFile.modalTitle"
      :nodeIdVal="selectedFile.nodeId"
      @openVersionInBrava="openVersionInBrava"
      @versionsModalClosed="versionsModalClosed"
    >
    </FileVersionsModalComponent>
  </span>
</template>

<script>
import draggable from "vuedraggable";
import IframeComponent from "../components/iframe-component";
import attachmentMixin from "@/mixins/attachmentMixin";
import { Pane, Splitpanes } from "splitpanes";
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
      iframeOjbect: {src:""},
      bwsId: "",
      categoryId: "",
      files: [],
      requestEntityId: "",
      filesUploaded: [],
      attachmentSortList: [],
      fileTypes: [],
      versionsDialogState: false,
      selectedFile: {},
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
  },
  methods: {
    versionsModalClosed() {
      this.versionsDialogState = false;
    },
    openVersionInBrava(obj) {
      this.openFileInBrave(obj);
    },
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

.vertical-scroll {
  flex-wrap: unset !important;
  overflow-y: hidden;
  overflow-x: scroll;
}
</style>
