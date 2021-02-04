<template>
  <div id="app">
    <v-card style="margin: 20px 0; border-radius: 6px">
      <div class="container">
        <v-alert outlined type="info" prominent icon="mdi-draw">
          <p style="font-size: 16px; color: black">
            <span style="font-size: 20px; color: #609ec1"> التأشيرة </span
            ><br />
            تستخدم هذه المساحة لكتابة تأشيرة السيد مدير الهيئة
          </p>
        </v-alert>
        <!-- <h3>التأشيرة</h3> -->
        <div class="col-12 mt-2">
          <VueSignaturePad
            id="signature"
            width="100%"
            height="500px"
            ref="signaturePad"
            :options="options"
          />
        </div>
        <v-row align="center" justify="space-around" style="padding: 10px">
          <v-btn @click="clear">مسح</v-btn>
          <v-btn @click="undo">الغاء</v-btn>
          <v-btn @click="save">حفظ</v-btn>
        </v-row>
      </div>
    </v-card>
    <v-card style="margin: 50px 0; border-radius: 6px">
      <div class="container">
        <!-- <h3>التأشيرات السابقة</h3> -->
        <v-alert outlined type="info" prominent icon="mdi-draw">
          <p style="font-size: 16px; color: black">
            <span style="font-size: 20px; color: #609ec1">
              التأشيرات السابقة
            </span>
          </p>
        </v-alert>
        <v-img
          :src="selectedUrl"
          width="100%"
          height="500px"
          id="signature"
        ></v-img>
        <v-sheet class="mx-auto" elevation="8" max-width="100%">
          <v-slide-group v-model="selected" class="pa-4" mandatory show-arrows>
            <v-slide-item
              v-for="signature in signatures"
              v-bind:value="signature.src"
              :key="signature.id"
              v-slot="{ active, toggle }"
            >
              <v-card
                :style="active ? 'border: 2px solid #aaaaaa;' : ''"
                class="ma-4"
                height="200"
                width="155px"
                @click="toggle"
              >
                <v-img
                  :src="signature.src"
                  style="border: 2px solid #e9e9e9; height: 150px"
                ></v-img>
                <v-card-text style="border-top: 2px solid #e9e9e9">{{
                  signature.date.split('T')[0]
                }}</v-card-text>
              </v-card>
            </v-slide-item>
          </v-slide-group>
        </v-sheet>
      </div>
    </v-card>
  </div>
</template>

<script>
import signatureMixin from '../mixin/signatureMixin'

export default {
  data() {
    return {
      options: {
        penColor: 'black',
        onBegin: () => {
          
          this.$refs.signaturePad.resizeCanvas();
        },
        // backgroundColor: 'rgb(255, 255, 255)'
      },
      // requestId: 665146,
      signaturesContainer: 715948,
      signatures: [],
      selected: null,
      folderId: null,
    }
  },
  mixins: [signatureMixin],
  props: ['requestId'],
  methods: {
    undo() {
      this.$refs.signaturePad.undoSignature()
    },
    async save() {
      const { isEmpty, data } = this.$refs.signaturePad.saveSignature()

      alert('Open DevTools see the save data.')
      console.log(isEmpty)
      // console.log(data);

      await this.uploadToCS(data, this.folderId)
      await this.reload()
    },
    change() {
      this.options = {
        penColor: '#00f',
      }
    },
    resume() {
      this.options = {
        penColor: '#c0f',
      }
    },
    clear() {
      this.$refs.signaturePad.clearSignature()
    },
    reload: async function() {
      if (!this.folderId) return
      const subNodes = await this.getSubNodes(this.folderId)

      // Download thumbnails signatures
      this.signatures = []
      this.signatures = await this.thumbnail(subNodes.results)
    },
    initialize: async function() {
      if (!this.requestId) return
      let folder = await this.createFolder(
        this.signaturesContainer,
        this.requestId
      )
      this.folderId = folder.id

      // List all signatures attached to request id
      await this.reload()
    },
  },
  async created() {
    // Return folder id
    await this.initialize()
  },
  watch: {
    requestId: function() {
      this.initialize()
    },
  },
  computed: {
    selectedUrl: function() {
      if (!this.selected) return
      return this.selected.replace('&verNum=1&verType=otthumb&pageNum=1', '')
    },
  },
  mounted() {
    this.$observable.subscribe('resizeCanvas', () => {
      console.log('canvas')
      this.clear();
    })
  },
}
</script>

<style>
#signature {
  /* border: double 3px transparent;
border-radius: 5px;
background-image: linear-gradient(white, white),
    radial-gradient(circle at top left, #4bc5e8, #9f6274);
background-origin: border-box;
  background-clip: content-box, border-box; */
  background: var(--unnamed-color-ffffff) 0% 0% no-repeat padding-box;
  background: #ffffff 0% 0% no-repeat padding-box;
  box-shadow: 0px 3px 6px #00000014;
  border: 2px solid #aaaaaa;
  border-radius: 5px;
  opacity: 1;
}
.v-alert--outlined {
  border: none !important ;
}
.v-slide-item--active {
  border: 2px solid #2d7fae !important;
}
</style>
