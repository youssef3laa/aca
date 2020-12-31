<template>
  <div>
    <v-container
        style="border: 2px dashed #94bed6 !important; border-radius: 6px; background-color:#f2f7fa"
        title="Click to grap a file from your PC!"
    >
      <input style="display:none" type="file"/>

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
      <draggable tag="div" :list="files">
        <div
            v-for="(file, index) in files"
            :key="index"
            class="card"
            draggable
            @dragstart="startDrag($event, file)"
            @drop="onDrop($event)"
            @dragover.prevent
            @dragenter.prevent
        >
          <v-row>
            <v-col :cols="2">
              <v-icon>
                mdi-file-pdf-outline
              </v-icon>
            </v-col>
            <v-col :cols="8" style="cursor: pointer" @click="openFileInBrave(file)">
              {{ file.name }} <br/>
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
  data() {
    return {
      filesSelected: '',
      files: [],
      filesUploaded: new Map()
    }
  },
  methods: {
    openFileInBrave: function (file) {
      let fileId = this.filesUploaded.get(file.name).results.data.properties.id;
      this.$observable.fire('open-file-brava', fileId);
    },
    deleteFile: async function (file) {
      console.log();
      let fileId = this.filesUploaded.get(file.name).results.data.properties.id;
      try {
        let deleteResponse = await Http.delete('/document/delete/' + fileId);
        console.log(deleteResponse);
        this.filesUploaded.delete(file.name);
        this.files = this.files.filter((fileVal) => {
          console.log(file.name, fileVal);
          return fileVal.name !== file.name;
        });
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
        if (!this.filesUploaded.get(file.name))
          formData.append('file', file)
      })
      // formData.append('file', this.files);
      formData.append('parentId', '577193')
      Http.addHeader('Content-Type', 'multipart/form-data')
      Http.post('/document/upload', formData)
          .then((response) => {
            console.log(response)
            let data = response.data.data;
            for (let i = 0; i < data.length; ++i) {
              const element = data[i];
              for (let key in element) {
                // eslint-disable-next-line no-prototype-builtins
                if (element.hasOwnProperty(key))
                  this.filesUploaded.set(key, element[key]);
              }

            }
            console.log(this.filesUploaded);
          })
          .catch((reason) => {
            console.log(reason)
          })
    },
  },
  mounted() {
    const dropzone = this.$el.firstElementChild
    const fileupload = dropzone.firstElementChild
    // console.log(fileupload)
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
          // dragevent.dataTransfer.fi
          for (var i = 0; i < dragevent.dataTransfer.files.length; i++) {
            console.log(dragevent.dataTransfer.files[i])
            this.files.push(dragevent.dataTransfer.files[i])
          }
          this.uploadFiles()

          // console.log(this.files);
          //  console.log(dragevent)
          //  console.log(dragevent.dataTransfer)
        }
      })
      dropzone.addEventListener('click', (e) => {
        console.log(e)
        fileupload.click()
      })
      // dropzone.addEventListener("keypress", e => {
      //     e.preventDefault()
      //     const keyEvent = e
      //     if (keyEvent.key === "Enter") {
      //         if (fileupload)
      //             fileupload.click()
      //     }
      // })
      if (fileupload) {
        fileupload.addEventListener('change', (e) => {
          const target = e.target
          if (target.files && target.files.length > 0) {
            for (var i = 0; i < target.files.length; i++) {
              console.log(target.files[i])
              this.files.push(target.files[i])
            }
            // this.files.push(target.files)
            // console.log(this.files)
            // this.filesSelected(target.files)
          }
          this.uploadFiles()
        })
      }
    }
  },
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
