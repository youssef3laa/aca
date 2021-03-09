<template>
  <div>
    <ckeditor :editor="editor" @ready="onReady" ></ckeditor>
  </div>
</template>
<script>
import DecoupledEditor from '@ckeditor/ckeditor5-build-decoupled-document'
export default {
  data() {
    return {
      editor: DecoupledEditor,
      //   editorData: '<p>Content of the editor.</p>',
      editorConfig: {
        // The configuration of the editor.
      },
    }
  },
  methods: {
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
}
</script>
