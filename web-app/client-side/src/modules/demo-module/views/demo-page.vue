<template>
  <div>
    <v-app>
      <v-container>
        <v-row>
          <v-col>
            <v-file-input
                v-model="file"
                counter="1"
                label="Input File"
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

export default {
  name: 'DemoPage',
  data() {
    return {
      file: null
    }
  },
  components: {},
  methods: {
    upload() {
      console.log(this.file);
    }
  }
}
</script>
