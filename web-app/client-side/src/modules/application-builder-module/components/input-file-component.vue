<template>
  <div>
    <v-container
        style="border: 2px dashed #94bed6 !important; border-radius: 6px; background-color:#f2f7fa"
        title="Click to grap a file from your PC!"
    >
      <input multiple style="display:none" type="file"/>

      <v-row align="center" justify="center">
        <v-icon color="outline" style="margin: 10px 10px 0 0; padding:5px 10px"
        >mdi-cloud-upload
        </v-icon
        >
      </v-row>
      <v-row align="center" justify="center">
        <span style="margin: 10px 10px 0 0; padding:5px 10px"
        >قم بتحميل الملفات أو <a color="outline">اضغط هنا</a></span
        >
      </v-row>
    </v-container>
    <br/>
    <span>الملفات</span>
    <v-container>
      <!-- <draggable tag="ul" :list="files">
        <li
          v-for="(file, index) in files"
          :key="index"
          draggable
          class="drop-zone"
          @drop="onDrop($event)"
          @dragstart="startDrag($event, file)"
          @dragover.prevent
          @dragenter.prevent
        >
          {{ file.name }}
        </li>
      </draggable> -->
      <v-row>
        <v-col :cols="12">

        </v-col>
      </v-row>
      <draggable tag="div" :list="files" class="row">
        <div
          class="card col-5"
          v-for="(file, index) in files"
          :key="index"
          draggable
          @drop="onDrop($event)"
          @dragstart="startDrag($event, file)"
          @dragover.prevent
          @dragenter.prevent
        >
          <v-row class="row">
            <v-col :cols="2">
              <v-icon class="card-icon">
                mdi-file-pdf-outline
              </v-icon>
            </v-col>
            <v-col :cols="8"
                   class="card-name"
            >

              {{ file.name }} <br />
              {{ file.size }}
            </v-col>
            <v-col :cols="2" style="cursor: pointer" @click="deleteFile(file)">
              <v-icon color="#ea9cb3">
                mdi-delete-circle-outline
              </v-icon>
            </v-col>
          </v-row>
        </div>
      </draggable>
    </v-container>
  </div>
</template>

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
      filesUploaded: []
    }
  },
  created() {
    this.bwsId = 577193;
  },
  methods: {
    openFileInBrave: function (file) {
      let fileId = this.filesUploaded.find(element => element.name === file.name).id;
      this.$observable.fire('open-file-brava', fileId);
    },
    deleteFile: async function (file) {
      console.log();
      let fileId = this.filesUploaded.find(element => element.name === file.name).id;
      try {
        let deleteResponse = await Http.delete('/document/delete/' + fileId);
        console.log(deleteResponse);
        this.filesUploaded = this.filesUploaded.filter(element => element.name !== file.name);
        this.files = this.files.filter((fileVal) => fileVal.name !== file.name);
        console.log(this.files);
      } catch (e) {
        console.error(e)
      }

    },
    startDrag: (evt, file) => {
      console.log(file.name)
      evt.dataTransfer.dropEffect = 'move'
      evt.dataTransfer.effectAllowed = 'move'
      evt.dataTransfer.setData('itemID', file.name)
    },
    onDrop(evt) {
      console.log(evt.dataTransfer.getData('itemID'))
      const itemID = evt.dataTransfer.getData('itemID')
      const item = this.files.find((item) => item.name == itemID)
      console.log(item)
    },
    uploadFiles: function () {
      console.log(this.files)
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
            console.log(response)
            let data = response.data.data;
            for (let i = 0; i < data.length; ++i) {
              const element = data[i];
              for (let key in element) {
                // eslint-disable-next-line no-prototype-builtins
                if (element.hasOwnProperty(key)) {
                  const fileObj = element[key].results.data.properties;
                  this.filesUploaded.push(fileObj);
                  this.files = this.files.filter(fileElement => fileElement.name !== fileObj.name)
                }
              }

            }
            console.log(this.filesUploaded);
          })
          .catch((reason) => {
            console.log(reason)
          })
    },
    listFiles: async function () {
      let response;
      try {
        response = await Http.get('/document/list/' + this.bwsId + '?fields=properties');
      } catch (e) {
        console.log(e);
      }
      if (!response) return;
      // this.filesUploaded=response.data
      console.log(response);
    }
  },
  mounted() {
    console.log("val:", this.field)
    console.log("field:", this.field)
    const dropzone = this.$el.firstElementChild
    const fileupload = dropzone.firstElementChild
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
            console.log(dragevent.dataTransfer.files[i])
            this.files.push(dragevent.dataTransfer.files[i])
          }
          this.uploadFiles()

        }
      })
      dropzone.addEventListener('click', (e) => {
        console.log(e)
        fileupload.click()
      })

      if (fileupload) {
        fileupload.addEventListener('change', (e) => {
          const target = e.target
          if (target.files && target.files.length > 0) {
            for (let i = 0; i < target.files.length; i++) {
              console.log(target.files[i])
              this.files.push(target.files[i])
            }
          }
          this.uploadFiles()
        })
      }
    }

    this.listFiles();

  },
}
</script>
<style lang="scss">

/*colors*/
$color-prim-font:#1A1A2E;
$color-prim-border:#94bed6; 
$color-prim-bg: #f2f7fa;
$color-secondary:#9E9E9E;
/*fonts */
$font-12: 12px;



.input-file-prim{   
border: 2px dashed $color-prim-border !important;
border-radius: 6px;
background-color:$color-prim-bg;

}
.card-name{
    font-size: $font-12;
    color:$color-prim-font;
}
.card-icon{
    height: 40px;
    padding: 5px;
    background: lighten($color-secondary, $amount: 30);
    border-radius: 6px;
    vertical-align: middle;
    text-align: center;
}

    
</style>
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
