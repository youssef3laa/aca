<template>
  <div>
    <ckeditor style="max-height: 400px !important;" :editor="editor" @input="updateValue" @ready="onReady" v-model="editorData"></ckeditor>
  </div>
</template>
<script>
import DecoupledEditor from '@ckeditor/ckeditor5-build-decoupled-document'
export default {
  data() {
    return {
      editor: DecoupledEditor,
      editorData: this.val,
      editorConfig: {
        // The configuration of the editor.
      },
    }
  },
  methods: {
    updateValue(content){
      this.editorData = content;
      this.$emit('update', {
        name: this.field.name,
        value: this.editorData,
        type: 'inputChange',
      })
    },
    onReady(editor) {
      // Insert the toolbar before the editable area.
      editor.ui
        .getEditableElement()
        .parentElement.insertBefore(
          editor.ui.view.toolbar.element,
          editor.ui.getEditableElement()
        )
    },
  },
  props: ['val', 'field'],
  watch: {
    val: function(newVal, oldVal) {
      console.log(oldVal)
      this.editorData = newVal
    },
  },
}
</script>
