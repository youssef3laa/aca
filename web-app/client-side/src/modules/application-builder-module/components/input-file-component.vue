<template>
  <v-sheet
    tabindex="0"
    title="Click to grap a file from your PC!"
    color="indigo lighten-4"
    width="100%"
    height="200"
    class="pa-2"
  >
    <input type="file" style="display:none" />
    <span>Drag and Drop File!</span>
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

    <draggable tag="ul" :list="files">
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
    </draggable>
  </v-sheet>
</template>

<script>
import draggable from 'vuedraggable'
import Http from '../../core-module/services/http'
export default {
  components: { draggable },

  name: 'file-input-component',
  data() {
    return {
      filesSelected: '',
      files: [],
    }
  },
  methods: {
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
    uploadFiles: function() {
      console.log(this.files)
      const formData = new FormData()
      this.files.forEach((file) => formData.append('file', file))
      // formData.append('file', this.files);
      formData.append('parentId', '2000')
      Http.addHeader('Content-Type', 'multipart/form-data')
      Http.post('/document/upload', formData)
        .then((response) => console.log(response))
        .catch((reason) => console.log(reason))
    },
  },
  mounted() {
    const dropzone = this.$el
    const fileupload = this.$el.firstElementChild
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
