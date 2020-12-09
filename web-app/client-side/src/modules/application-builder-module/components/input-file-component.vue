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
    <ul>
      <li v-for="(file, index) in files" :key="index">
        {{ file[0].name }}
      </li>
    </ul>
  </v-sheet>
</template>

<script>
export default {
  name: 'file-input-component',
  data() {
    return {
      filesSelected: '',
      files: [],
    }
  },
  mounted() {
    const dropzone = this.$el
    const fileupload = this.$el.firstElementChild
    console.log(fileupload)
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
          alert('working')
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
          if (target.files) {
            this.files.push(target.files)
            console.log(target.files)
            // this.filesSelected(target.files)
          }
        })
      }
    }
  },
}
</script>
