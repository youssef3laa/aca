<template>
  <div class="container">
    <splitpanes class="default-theme" dir="ltr">
    
      <pane>
        <ShowAttachmentComponent
          :key="d.requestId"
          :bwsId="bwsId"
          :requestEntityId="d.requestId"
        >
        </ShowAttachmentComponent>
      </pane>
      <pane class="bg-white" dir="rtl">
        <v-row class="mb-3" style="padding : 5px;">
          <v-col
            :cols="1"
            style="background : #effaf6; border:1px solid effaf6; border-radius: 6px;text-align: center;height: 100%;
    margin-top: 15px;"
            ><span style="color:#55c29b">7</span></v-col
          >
          <v-col :cols="7">
            <v-row no-gutters style="margin-top: 10px;">
              <v-col :cols="6">
                <span>{{ title }}</span>
              </v-col>
              <v-col :cols="6">
                {{ date }}
              </v-col>
            </v-row>
          </v-col>
          <v-col :cols="4">
            <v-btn
              style="height: 100%; background: #f2f8fb; color: #247aab;float: left;"
              v-on:click="showRichText = !showRichText"
            >
              <v-icon>mdi-plus</v-icon>
              تعديل مذكرة العرض
            </v-btn></v-col
          >
        </v-row>
      <v-row>
        <v-col :cols="12" v-if="showRichText">
          <AutocompleteComponent
            :field="{ name: field.label }"
            :val="{ value: selected, list: [], url: url }"
            @update="changeVal"
          >
          </AutocompleteComponent>
          <div style="max-height: 600px; overflow-y: auto">
            <AppBuilder
              style="overflow-y: hidden"
              ref="appBuilder"
              :app="app"
            />
          </div>
        </v-col>
     
      
        <v-col :cols="12" v-else>
          <IframeComponent :val="iframeOjbect" />
        </v-col>
      </v-row>
      </pane>
    
    </splitpanes>
  </div>
</template>

<script>
import AutocompleteComponent from './autocomplete-component.vue'
import memoComponentMixin from '../../../mixins/memoComponentMixin'
import formPageMixin from '../../../mixins/formPageMixin'
import ShowAttachmentComponent from './show-attachment-component'
import IframeComponent from '../components/iframe-component'
import {Pane, Splitpanes} from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'

export default {
  components: {
    AppBuilder: () => import('../builders/app-builder'),
    AutocompleteComponent,
    ShowAttachmentComponent,
    Splitpanes,
    Pane,
    IframeComponent,
  },
  mixins: [memoComponentMixin, formPageMixin],
  async mounted() {
    this.$observable.subscribe('retrieveMemo', async (data) => {
      try {
        var memoType = await this.getMemoJsonId(data.nodeId)
        console.log(memoType)
        this.$refs.appBuilder.setAppData({
          pages: [{ sections: [{ forms: [] }] }],
        })
        this.loadForm(memoType)
        await this.fillForm(data.nodeId)
        console.log(data)
        this.nodeId = data.nodeId
      } catch (error) {
        console.log(error)
      }

      // this.loadForm(this.selected, this.fillForm("722454"));
    });

      this.$observable.subscribe("memoCreate", async (callback) => {
          if (callback) callback(await this.triggerSubmit());
      })
  },
  data() {
    return {
      iframeOjbect: { src: '' },
      d: this.val,
      url: 'lookup/get/category/memoType',
      selected: '',
      nodeId: '',
      richText: {},
      memoData: [],
      bwsId: 680482,
      app: {},
      title: this.val.title,
      date: this.val.date,
      showRichText: false,
      placeHolder: true,
      loading: false,
    }
  },
  props: ['val', 'field'],

  methods: {
    async loadBrava(obj) {
      await this.openFileInBrava(obj.file, obj.contextObj)
    },
    changeVal(event) {
      this.nodeId = null
      if (event.value.value) {
        this.$refs.appBuilder.setAppData({
          pages: [{ sections: [{ forms: [] }] }],
        })
        console.log(event)
        this.selected = event.value.value.object.stringKey
        this.loadForm(this.selected)
        console.log(this.selected)
      } else {
        this.$refs.appBuilder.setAppData({
          pages: [{ sections: [{ forms: [] }] }],
        })
      }
    },

    async fillForm(nodeId) {
      var memoData = await this.getMemoData(nodeId)
      console.log('MemoData', memoData)
      this.selected = memoData.jsonId
      if (memoData == undefined) return
      memoData.memoValues.forEach((element) => {
        var model = { [element.jsonKey]: element.value }
        console.log(model)
        console.log(element)
        this.$refs.appBuilder.setModelData(element.jsonKey, model)
        console.log('appbuilder', this.$refs.appBuilder)
      })
    },

    async triggerSubmit() {
      var formKeys = this.$refs.appBuilder.getFormKeyByPageKey('memoPage')
      if(formKeys instanceof  Array && formKeys.length===0){
        return  false;
      }
      this.richText = {}
      formKeys.forEach((element) => {
        var data = this.$refs.appBuilder.getModelData(element)
        this.richText[element] = data[element]
        console.log(data)
        console.log(element)
      })
      let data
      data = {
        requestId: this.d.requestId,
        jsonId: this.selected,
        nodeId: this.nodeId,
        values: this.richText,
      }

      await this.setMemoData(data);
      this.$observable.fire("refreshHorizontalAttachmentFiles");
      return true;
    },
  },
  watch: {
    val: function(newVal) {
      for (var key in newVal) {
        this.d[key] = newVal[key]
      }
      // this.d = newVal
    },
  },
}
</script>

<style>
.bg-white {
  background-color: white !important;
}
</style>