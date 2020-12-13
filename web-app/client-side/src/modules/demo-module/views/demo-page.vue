<template>
  <div>
    <v-app>
      <v-container>
        <v-row>
          <v-col>
            <v-file-input
                v-model="files"
                label="Input File"
                multiple
                outlined show-size
            ></v-file-input>
          </v-col>
          <v-col>
            <v-btn @click="upload">Upload</v-btn>
          </v-col>
        </v-row>
      </v-container>
    </v-app>
  </div>
</template>


<script>
import Http from '@/modules/core-module/services/http'

export default {
  name: 'DemoPage',
  data() {
    return {
      files: null
    }
  },
  components: {},
  methods: {
    upload() {
      console.log(this.files);
      const formData = new FormData();
      this.files.forEach(file => formData.append('file', file))
      // formData.append('file', this.files);
      formData.append('parentId', "2000");
      Http.addHeader("Content-Type", "multipart/form-data");
      Http.post('/document/upload', formData)
          .then(response => console.log(response))
          .catch(reason => console.log(reason));
    }
  }
}
</script>
