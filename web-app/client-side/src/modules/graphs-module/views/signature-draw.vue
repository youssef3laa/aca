<template>
  <div id="app">
    <AlertComponent ref="alertComponent"></AlertComponent>
    <v-card
      v-if="readonly != true"
      style="margin: 0px 0px 40px 0px; border-radius: 6px"
    >
      <div class="container">
        <v-alert outlined type="info" prominent icon="mdi-draw">
          <p style="font-size: 16px; color: black">
            <span style="font-size: 20px; color: #609ec1">
              {{ $t('signature') }} </span
            ><br />
            <span v-t="'this-field-for-notes'"></span> {{ displayName }}
          </p>
        </v-alert>
        <!-- <h3>التأشيرة</h3> -->
        <div class="col-12 mt-2" style="padding:0 !important">
          <VueSignaturePad
            id="signature"
            width="100%"
            height="500px"
            ref="signaturePad"
            :options="options"
          />
        </div>
        <v-row style="padding: 10px">
          <v-btn
            text
            color="#07689F"
            style="background-color: #f2f7fa; margin: 10px"
            @click="clear"
            >{{ $t('clear') }}</v-btn
          >
          <v-btn
            text
            color="#07689F"
            style="background-color: #f2f7fa; margin: 10px"
            @click="undo"
            >{{ $t('cancel') }}</v-btn
          >
          <v-btn
            text
            color="#07689F"
            style="background-color: #f2f7fa; margin: 10px"
            @click="save"
            >{{ $t('save') }}</v-btn
          >
        </v-row>
      </div>
    </v-card>
    <v-card v-if="oldSignatures" style="border-radius: 6px; box-shadow:none;">
      <v-overlay :absolute="true" :value="loading">
        <v-progress-circular indeterminate size="64"></v-progress-circular>
      </v-overlay>
      <div class="container">
        <!-- <h3>التأشيرات السابقة</h3> -->

        <!-- <v-img
          :src="selectedUrl"
          width="100%"
          height="500px"
          id="signature"
        ></v-img> -->
        <v-expansion-panels
          style="position:relative;    width: 100%;
    display: block;"
          v-model="panel"
          multiple
        >
          <v-expansion-panel>
            <v-expansion-panel-header>
              <v-row no-gutters>
                <v-col cols="4">
                  <v-alert outlined type="info" prominent icon="mdi-draw">
                    <p style="font-size: 16px; color: black">
                      <span style="font-size: 20px; color: #609ec1">
                        {{ $t('oldSignatures') }}
                      </span>
                    </p>
                  </v-alert>
                  <span class="line"></span>
                </v-col>
                <v-col cols="8" class="text--secondary"> </v-col>
              </v-row>
              <template v-slot:actions>
                <!--                    <v-icon color="error">-->
                <!--                      mdi-arrow-collapse-down-->
                <!--                    </v-icon>-->
              </template>
            </v-expansion-panel-header>
            <v-expansion-panel-content>
              <span>
                <v-sheet
                  class="mx-auto"
                  elevation="8"
                  max-width="100%"
                  style="box-shadow: none !important;"
                >
                  <v-slide-group
                    v-model="selected"
                    class="pa-4"
                    mandatory
                    show-arrows
                  >
                    <!-- v-bind:value="signature.src" -->
                    <v-slide-item
                      v-for="signature in signatures"
                      :key="signature.id"
                      v-slot="{ active, toggle }"
                    >
                      <v-card
                        :style="active ? 'border: 2px solid #aaaaaa;' : ''"
                        class="ma-4"
                        height="45"
                        width="155px"
                        @click="toggle"
                      >
                        <!-- <v-img
                          :src="signature.src"
                          style="border: 2px solid #e9e9e9; height: 150px"
                        ></v-img> -->
                        <v-card-text style="border-top: 2px solid #e9e9e9">{{
                          signature.date
                        }}</v-card-text>
                      </v-card>
                    </v-slide-item>
                  </v-slide-group>
                </v-sheet>
              </span>
            </v-expansion-panel-content>
          </v-expansion-panel>
        </v-expansion-panels>
        <v-card style="margin: 0px 2px;">
          <v-card flat style="padding: 10px 0 5px 0;">
            <span
              style="display: block; background-color:#f8f8f8; border:1px solid #e1e1e1;
    border-radius:6px;border-radius-top-righ:6px; border-radius-top-left:6px;
    margin: 15px; padding: 5px;"
            >
              <button
                style="padding:15px;"
                v-for="tab in tabs"
                :key="tab.id"
                @click="selectedTab(tab)"
                :class="['tab-btn', { active: selected === tab }]"
              >
                {{ $t(tab.name) }}
              </button>
            </span>
          </v-card>
          <div id="1">
            <h1>Test 1</h1>
          </div>
          <div id="2">
            <h1>Test 1</h1>
          </div>
          <div id="3">
            <h1>Test 1</h1>
          </div>
        </v-card>
      </div>
    </v-card>
  </div>
</template>

<script>
import signatureMixin from '../mixin/signatureMixin'
import userMixin from '../../../mixins/userMixin'

export default {
  data() {
    return {
      signatures: [
        { date: '01-03-2021', id: 1 },
        { date: '02-03-2021', id: 2 },
        { date: '03-03-2021', id: 3 },
      ],
      panel: [0],
      tabs: [
        {
          key: 'units',
          id: '1',
          isActive: true,
          name: 'Test 1',
        },
        {
          key: 'units',
          id: '1',
          isActive: false,
          name: 'Test 2',
        },
        {
          key: 'units',
          id: '1',
          isActive: false,
          name: 'Test 3',
        },
      ],
      options: {
        penColor: 'black',
        onBegin: () => {
          this.$refs.signaturePad.resizeCanvas()
        },
        // backgroundColor: 'rgb(255, 255, 255)'
      },
      // requestId: 665146,
      signaturesContainer: 715948,
      // signatures: [],
      selected: null,
      folderId: null,
      loading: false,
      displayName: null,
    }
  },
  mixins: [signatureMixin, userMixin],
  props: ['requestId', 'readonly', 'field'],
  methods: {
    selectedTab: function(tab) {
      this.selected = tab

      this.$observable.fire(this.tabkey, tab.id)

      if (tab.publish) {
        if (!(tab.publish instanceof Array)) tab.publish = [tab.publish]
        for (let key in tab.publish) {
          this.$observable.fire(tab.publish[key])
        }
      }
      console.log('Nested Tab Selected', tab)
    },
    undo() {
      this.$refs.signaturePad.undoSignature()
    },
    async save() {
      const { isEmpty, data } = this.$refs.signaturePad.saveSignature()
      console.log(isEmpty)
      // console.log(data);

      await this.uploadToCS(data, this.folderId)
      this.$refs.alertComponent._alertSuccess({
        message: 'saveSignatureSuccess',
      })
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
      this.loading = true
      if (!this.folderId) return
      const subNodes = await this.getSubNodes(this.folderId)

      // Download thumbnails signatures
      this.signatures = []
      this.signatures = await this.thumbnail(subNodes)
      this.loading = false
    },
    initialize: async function() {
      if (!this.requestId) return
      let folder = await this.createFolder(
        this.signaturesContainer,
        this.requestId
      )
      this.folderId = folder.properties.id

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
    oldSignatures: function() {
      if (Object.prototype.hasOwnProperty.call(this.field, 'oldSignatures')) {
        return this.field.oldSignatures
      }
      return true
    },
  },

  async mounted() {
    let userDetails = await this.getUserDetails()
    this.displayName = userDetails.displayName
    this.$observable.subscribe('resizeCanvas', () => {
      console.log('canvas')
      this.clear()
    })

    for (let i = 0; this.tabs && i < this.tabs.length; i++) {
      if (this.tabs[i].isActive) {
        this.selected = this.tabs[i]
        this.selectedTab(this.tabs[i])
      }
    }
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
