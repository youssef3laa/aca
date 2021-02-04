<!--suppress EqualityComparisonWithCoercionJS -->
<template>
  <div>
    <v-container
        class="input-file-prim"
        title="Click to grap a file from your PC!"
    >
      <input multiple style="display: none" type="file"/>

      <v-row align="center" justify="center">
        <v-icon color="outline" style="margin: 10px 10px 0 0; padding: 5px 10px"
        >mdi-cloud-upload
        </v-icon>
      </v-row>
      <v-row align="center" justify="center">
        <span style="margin: 10px 10px 0 0; padding: 5px 10px"
        >قم بتحميل الملفات أو <a color="outline">اضغط هنا</a></span
        >
      </v-row>
    </v-container>
    <br/>
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
            {{ $t('pleaseChooseFileTypeToCompleteFileUpload') }}
          </v-alert>


          <v-row v-for="(file, index) in files" :key="index">
            <v-col cols="5">

              <v-text-field
                  v-model="file.file.name"
                  color="outline"
                  hide-details
                  outlined readonly v-bind:label="$t('nameOfTheFile')"
              >
                <v-icon slot="append" color="red"
                        @click="file.removeFile({file,index})">mdi-close-circle-outline</v-icon>
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
                  v-bind:label="$t('typeOfTheFile')">

              </v-autocomplete>


            </v-col>

            <v-col cols="2">
              <v-btn style="height: 100%" @click="uploadFile({file,index})">
                <v-icon>mdi-plus</v-icon>
                test3
              </v-btn>
            </v-col>
          </v-row>

        </v-container>
        <span>الملفات</span>
    </span>
    <v-container>
      <v-row>
        <v-col :cols="12">

          <draggable :animation="150" :list="filesUploaded" :swapThreshold="0.5" class="row" tag="div"

                     @end="onEnd($event)">

            <div
                v-for="(file, index) in filesUploaded"
                :key="index"
                @dragstart="startDrag($event, file)"
                @dragover.prevent
                @dragenter.prevent
                class="col-6">
              <div

                  class="card"

              >
                <div class="row pa-3">
                  <v-col :cols="2" class="card-icon">
                    <v-icon> mdi-file-pdf-outline</v-icon>
                  </v-col>
                  <v-col :cols="6"
                         class="card-name"
                         style="cursor: pointer"
                         @click="openFileInBrave(file.properties.id)"
                  >

                    {{ file.properties.name }} <br/>
                    {{ file.properties.fileTypeValue }}
                  </v-col>
                  <v-col :cols="2" style="cursor: pointer" @click="openVersionsPopup(file)">
                    <v-icon color="#22B07D"> mdi-folder-multiple</v-icon>
                  </v-col>
                  <v-col :cols="2" style="cursor: pointer" @click="deleteFile(file)">
                    <v-icon color="#ea9cb3"> mdi-delete-circle-outline</v-icon>
                  </v-col>

                </div>
              </div>
            </div>

          </draggable>
        </v-col>

      </v-row>
    </v-container>
  </div>
</template>

<style lang="scss">

/*colors*/
$color-prim-font: #1A1A2E;
$color-prim-border: #94bed6;
$color-prim-bg: #f2f7fa;
$color-secondary: #9E9E9E;
/*fonts */
$font-12: 12px;


.input-file-prim {
  border: 2px dashed $color-prim-border !important;
  border-radius: 6px;
  background-color: $color-prim-bg;

}

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


</style>
<script>
import draggable from 'vuedraggable'
import Http from '../../core-module/services/http'
import formPageMixin from "../../../mixins/formPageMixin";

export default {
  components: {draggable},
  mixins: [formPageMixin],
  name: 'file-input-component',
  props: ["val", "field"],
  data() {
    return {
      bwsId: '',
      categoryId: '',
      files: [],
      requestEntityId: '',
      filesUploaded: [],
      attachmentSortList: [],
      fileTypes: []

    }
  },
  created() {
    this.bwsId = 680482;
    this.requestEntityId = 1;
    this.categoryId = 686057;
  },
  methods: {
    setFileTypeOnFileUploaded: function (file) {
      let categoryValue;
      if (file.categories == undefined || file.categories.length == 0) {
        //shouldn't go here...
        file.properties.fileTypeValue = "لم يتم إدخاله بعد";
        return;
      }
      categoryValue = file.categories[0][this.categoryId + "_2"];

      let lookupObj = this.fileTypes.find((element) => element.value == categoryValue);
      if (lookupObj == undefined) file.properties.fileTypeValue = "قيمة غير معرفة";
      else file.properties.fileTypeValue = lookupObj.text;
    },
    onEnd: function () {
      let tempArr = [];
      for (let i = 0; i < this.filesUploaded.length; ++i) {
        let element = this.filesUploaded[i];
        let attachmentSortElement = this.attachmentSortList.find(val => val.fileId == element.properties.id);
        attachmentSortElement.position = i;
        tempArr.push(attachmentSortElement);
      }
      this.updateMultipleAttachmentSortRecords(tempArr);
    },
    startDrag: function (evt, file) {
      evt.dataTransfer.dropEffect = 'move'
      evt.dataTransfer.effectAllowed = 'move'
      evt.dataTransfer.setData('itemID', file.properties.id)
    },
    openFileInBrave: function (fileId) {
      // let fileId = this.filesUploaded.find(element => element.name === file.properties.name).properties.id;
      this.$observable.fire('open-file-brava', {fileId});
    },
    deleteFile: async function (file) {
      file = file.properties;
      if (file == undefined || file.id == undefined) return;
      try {
        await Http.delete('/document/delete/' + file.id);
        this.filesUploaded = this.filesUploaded.filter(element => element.properties.id != file.id);
        this.files = this.files.filter((fileVal) => fileVal.name != file.name);
        let attachmentSortId;
        this.attachmentSortList = this.attachmentSortList.filter(value => {
          if (value.fileId != file.id) {
            return true;
          } else {
            attachmentSortId = value.id;
            return false
          }
        })
        if (!attachmentSortId) await Http.delete('/document/sort/' + attachmentSortId);
        let tempArr = [];
        for (let i = 0; i < this.filesUploaded.length; ++i) {
          let element = this.filesUploaded[i];
          let attachmentSortElement = this.attachmentSortList.find(val => val.fileId == element.properties.id);
          attachmentSortElement.position = i;
          tempArr.push(attachmentSortElement);
        }
        this.updateMultipleAttachmentSortRecords(tempArr);
      } catch (e) {
        console.error(e)
      }

    },
    listFiles: async function () {
      let nodesResponse
          , attachmentSortResponse;
      try {
        nodesResponse = await Http.get('/document/list/' + this.bwsId + '?fields=properties&fields=categories&where_type=-3');
        attachmentSortResponse = await Http.get('/document/sort', {
          params: {
            requestEntityId: this.requestEntityId,
            bwsId: this.bwsId
          }
        })
      } catch (e) {
        console.log(e);
      }
      if (!nodesResponse) return;
      nodesResponse = nodesResponse.data.data;
      this.attachmentSortList = attachmentSortResponse.data.data;


      nodesResponse.forEach((val) => {
        let attachmentSortElementObj;
        if ((attachmentSortElementObj = this.attachmentSortList.find(attachmentElement => attachmentElement.fileId == val.properties.id)) != null) {
          val.properties.position = attachmentSortElementObj.position;
          attachmentSortElementObj.exists = true;
        }
        //TODO move to function
        this.setFileTypeOnFileUploaded(val);
      });
      nodesResponse.sort((a, b) => (a.properties.position > b.properties.position) ? 1 : -1)
      let itemsToPost = [];
      nodesResponse.forEach((val, i) => {
        // noinspection EqualityComparisonWithCoercionJS
        if (val.properties.position == undefined) {
          val.properties.position = i
          let itemToPost = {
            "fileId": val.properties.id,
            "bwsId": this.bwsId,
            "requestEntityId": this.requestEntityId,
            "position": i
          };
          itemsToPost.push(itemToPost);

        }
      })
      if (itemsToPost.length > 0) {
        try {
          let documentSortResponse = await Http.post('/document/sort/bulk', itemsToPost)
          documentSortResponse.data.data.forEach(element => {
            element.exists = true;
            this.attachmentSortList.push(element)
          });

        } catch (e) {
          console.log(e);
        }
      }
      let attachmentSortListsToBeDeleted = this.attachmentSortList.filter(element => !element.exists);
      let ids = attachmentSortListsToBeDeleted.map(element => element.id).join(',');
      if (attachmentSortListsToBeDeleted.length > 0) {
        try {
          await Http.delete('/document/sort/bulk/' + ids)
        } catch (e) {
          console.log(e)
        }

      }
      this.filesUploaded = [];
      nodesResponse.forEach((val) => this.filesUploaded.push(val));
    },
    updateAttachmentSortRecord: function (obj) {
      let itemToBeUpdated = this.attachmentSortList.find(val => val.fileId == obj.fileId);
      itemToBeUpdated.position = obj.position;
      Http.post('/document/sort/update', itemToBeUpdated)
          .then(response => console.log(response))
          .catch(reason => console.log(reason));
    },
    createAttachmentSortRecord: async function (arr) {
      if (!(arr instanceof Array) && arr != undefined) arr = [].concat(arr);
      if (arr instanceof Array && arr.length > 0) {
        console.log(arr);
        try {
          let documentSortResponse = await Http.post('/document/sort/bulk', arr)
          documentSortResponse.data.data.forEach(element => {
            element.exists = true;
            this.attachmentSortList.push(element)
          });

        } catch (e) {
          console.log(e);
        }
      }
    },
    updateMultipleAttachmentSortRecords: function (arr) {
      Http.post('/document/sort/update/bulk', arr)
          .then(() => console.log("finished updating position in backend"))
          .catch(reason => console.error(reason))
    },
    uploadFile: async function (obj) {
      console.log(obj);

      let tempObj = {};
      let formData = new FormData();
      formData.append('file', obj.file.file);
      formData.append('parent_id', this.bwsId)
      formData.append('name', obj.file.file.name)
      formData.append('category_id', this.categoryId)
      tempObj[this.categoryId + "_2"] = obj.file.fileTypeSelected;
      formData.append('categoriesMap', JSON.stringify(tempObj))
      Http.addHeader('Content-Type', 'multipart/form-data')
      try {

        let uploadDataResponse = await Http.post(`document/uploadAndSetCategory`, formData)
        if (uploadDataResponse.status == 200) {
          this.files.splice(obj.index, 1);
          // await this.listFiles();
          uploadDataResponse = uploadDataResponse.data.data;
          let toBeCreated = {
            "fileId": uploadDataResponse.properties.id,
            "bwsId": this.bwsId,
            "requestEntityId": this.requestEntityId,
            "position": this.filesUploaded.length
          }
          await this.createAttachmentSortRecord(toBeCreated);
          this.filesUploaded.push(uploadDataResponse);
          this.setFileTypeOnFileUploaded(uploadDataResponse);
        }
        // console.log(uploadDataResponse);
      } catch (e) {
        console.log(e);
      }
    },
    fillFilesArray: function (filesSource) {
      if (!filesSource) return;
      filesSource.forEach(element => {
        this.files.push({
          file: element,
          removeFile: (obj) => {
            console.log(obj)
            this.files.splice(obj.index, 1);
          },
          fileTypeSelected: null
        })
      })
      // this.files.forEach(element => {
      //   // fileName
      //   // removeFile
      //   // fileTypeSelected
      //   this.filesBeforeUpload.push({
      //     fileName: element.name,
      //     removeFile: (obj) => {
      //       console.log(obj)
      //       this.filesBeforeUpload.splice(obj.index, 1);
      //       this.files.splice(obj.index, 1);
      //     },
      //     fileTypeSelected: null
      //   })
      //   console.log(element);
      // })
    },
    openVersionsPopup: function (file) {

      this.$observable.fire("openVersionsModal", file)

      console.log(file);
    }
  },
  async mounted() {
    const dropzone = this.$el.firstElementChild
    const fileUpload = dropzone.firstElementChild
    if (dropzone) {
      dropzone.addEventListener('dragenter', (e) => {
        e.preventDefault()
        this.dragover = true
      })
      dropzone.addEventListener('dragleave', (e) => {
        e.preventDefault()
        this.dragover = false
      })
      dropzone.addEventListener('dragover', (e) => {
        e.preventDefault()
        this.dragover = true
      })
      dropzone.addEventListener('drop', (e) => {
        e.preventDefault()
        const dragevent = e
        if (dragevent.dataTransfer) {
          this.fillFilesArray(dragevent.dataTransfer.files);
          // for (let i = 0; i < dragevent.dataTransfer.files.length; i++) {
          //   this.files.push(this.prepareFileObject(dragevent.dataTransfer.files[i]))
          // }
          // this.uploadFiles()
          // this.updateFilesBeforeUpload();

        }
      })
      dropzone.addEventListener('click', () => {
        fileUpload.click()
      })

      if (fileUpload) {
        fileUpload.addEventListener('change', (e) => {
          const target = e.target
          if (target.files && target.files.length > 0) {
            this.fillFilesArray(target.files);

            // for (let i = 0; i < target.files.length; i++) {
            //   this.files.push(this.prepareFileObject(target.files[i]))
            // }
          }
          // this.uploadFiles()
          //this.updateFilesBeforeUpload();

        })
      }
    }

    let lookups = await this.getLookupByCategory("attachmentFileType");
    console.log(lookups, this.$i18n.locale);
    let langKey = this.$i18n.locale == 'ar' ? 'arValue' : 'enValue';
    this.fileTypes = Array.from(lookups).map((element) => {
      return {
        text: element[langKey],
        value: element['key']
      }
    })
    await this.listFiles();

  }
}
</script>

<style>

.card {
  border: 2px solid #d6d6d6;
  border-radius: 6px;
  margin: 10px;
}

/* On mouse-over, add a deeper shadow */
.card:hover {
  border: 2px solid #0278ae;
}
</style>

<!-- <v-data-table
      :headers="tableHeaders"
      :items="files"
      item-key="id"
      :show-select="false"
      :disable-pagination="true"
      :hide-default-footer="true"
      class="page__table"
    >
      <template v-slot:body="props">
        <draggable :list="props.items" tag="tbody">
          <tr v-for="(file, index) in props.items" :key="index"> 
            <td>
              <v-icon small class="page__grab-icon">
                mdi-arrow-all
              </v-icon>
            </td>
            <td>{{file[0].name}}</td>
            <td>
              <v-icon small >
                mdi-pencil
              </v-icon>
            </td>
          </tr>
        </draggable>
      </template>
    </v-data-table> -->
