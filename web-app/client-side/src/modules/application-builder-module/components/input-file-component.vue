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
    <span>الملفات</span>
    <v-container>
      <v-row>
        <v-col :cols="12">

          <draggable :animation="150" :list="filesUploaded" :swapThreshold="0.5" class="row" tag="div"
                     @change="onChange"
                     @end="onEnd($event)">
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
                <v-col :cols="8"
                       class="card-name"
                       style="cursor: pointer"
                       @click="openFileInBrave(file)"
                >

                  {{ file.name }} <br/>
                  {{ file.size }}
                </v-col>
                <v-col :cols="2" style="cursor: pointer" @click="deleteFile(file)">
                  <v-icon color="#ea9cb3"> mdi-delete-circle-outline</v-icon>
                </v-col>
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

export default {
  components: {draggable},

  name: 'file-input-component',
  props: ["val", "field"],
  data() {
    return {
      bwsId: '',
      files: [],
      requestEntityId: '',
      filesUploaded: [],
      attachmentSortList: []

    }
  },
  created() {
    this.bwsId = 680482;
    this.requestEntityId = 1;
  },
  methods: {
    onChange: function (evt) {
      console.log("onChange", evt);
    },
    onEnd: function () {
      let tempArr = [];
      for (let i = 0; i < this.filesUploaded.length; ++i) {
        let element = this.filesUploaded[i];
        let attachmentSortElement = this.attachmentSortList.find(val => val.fileId == element.id);
        attachmentSortElement.position = i;
        tempArr.push(attachmentSortElement);
      }
      this.updateMultipleAttachmentSortRecords(tempArr);
    },
    openFileInBrave: function (file) {
      let fileId = this.filesUploaded.find(element => element.name === file.name).id;
      this.$observable.fire('open-file-brava', fileId);
    },
    deleteFile: async function (file) {
      if (file == undefined || file.id == undefined) return;
      try {
        await Http.delete('/document/delete/' + file.id);
        this.filesUploaded = this.filesUploaded.filter(element => element.id != file.id);
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
          let attachmentSortElement = this.attachmentSortList.find(val => val.fileId == element.id);
          attachmentSortElement.position = i;
          tempArr.push(attachmentSortElement);
        }
        this.updateMultipleAttachmentSortRecords(tempArr);
      } catch (e) {
        console.error(e)
      }

    },
    startDrag: function (evt, file) {
      evt.dataTransfer.dropEffect = 'move'
      evt.dataTransfer.effectAllowed = 'move'
      evt.dataTransfer.setData('itemID', file.id)
    },
    uploadFiles: function () {
      const formData = new FormData()
      this.files.forEach((file) => {
        if (!this.filesUploaded.find(element => element.name === file.name))
          formData.append('file', file)
      })
      // formData.append('file', this.files);
      formData.append('parentId', this.bwsId)
      Http.addHeader('Content-Type', 'multipart/form-data')
      Http.post('/document/upload', formData)
          .then((response) => {
            let data = response.data.data;
            let tempFilesArr = [];
            for (let i = 0; i < data.length; ++i) {
              const element = data[i];
              for (let key in element) {
                // eslint-disable-next-line no-prototype-builtins
                if (element.hasOwnProperty(key)) {
                  const fileObj = element[key].results.data.properties;

                  tempFilesArr.push({
                    "fileId": fileObj.id,
                    "bwsId": this.bwsId,
                    "requestEntityId": this.requestEntityId,
                    "position": this.filesUploaded.length
                  });

                  this.filesUploaded.push(fileObj);
                  this.files = this.files.filter(fileElement => fileElement.name !== fileObj.name)
                }
              }

            }
            this.createAttachmentSortRecord(tempFilesArr);

          })
          .catch((reason) => {
            console.log(reason)
            this.files = [];
          })
    },
    listFiles: async function () {
      let nodesResponse
          , attachmentSortResponse;
      try {
        nodesResponse = await Http.get('/document/list/' + this.bwsId + '?fields=properties');
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
      let nodeResults = nodesResponse.data.data.results
      this.attachmentSortList = attachmentSortResponse.data.data;


      nodeResults.forEach((val) => {
        let attachmentSortElementObj;
        if ((attachmentSortElementObj = this.attachmentSortList.find(attachmentElement => attachmentElement.fileId == val.data.properties.id)) != null) {
          val.data.properties.position = attachmentSortElementObj.position;
          attachmentSortElementObj.exists = true;
        }
      });
      nodeResults.sort((a, b) => (a.data.properties.position > b.data.properties.position) ? 1 : -1)
      let itemsToPost = [];
      nodeResults.forEach((val, i) => {
        // noinspection EqualityComparisonWithCoercionJS
        if (val.data.properties.position == undefined) {
          val.data.properties.position = i
          let itemToPost = {
            "fileId": val.data.properties.id,
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
      nodeResults.forEach((val) => this.filesUploaded.push(val.data.properties));
    },
    updateAttachmentSortRecord: function (obj) {
      let itemToBeUpdated = this.attachmentSortList.find(val => val.fileId == obj.fileId);
      itemToBeUpdated.position = obj.position;
      Http.post('/document/sort/update', itemToBeUpdated)
          .then(response => console.log(response))
          .catch(reason => console.log(reason));
    },
    createAttachmentSortRecord: async function (arr) {
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
    }
  },
  mounted() {
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
          for (let i = 0; i < dragevent.dataTransfer.files.length; i++) {
            this.files.push(dragevent.dataTransfer.files[i])
          }
          this.uploadFiles()

        }
      })
      dropzone.addEventListener('click', () => {
        fileUpload.click()
      })

      if (fileUpload) {
        fileUpload.addEventListener('change', (e) => {
          const target = e.target
          if (target.files && target.files.length > 0) {
            for (let i = 0; i < target.files.length; i++) {
              this.files.push(target.files[i])
            }
          }
          this.uploadFiles()
        })
      }
    }
    this.listFiles();

  }
  ,
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
