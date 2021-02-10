<template>
  <div>
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
        <v-container>
          <AutocompleteComponent
            :field="{ name: field.label }"
            :val="{ list: [], url: url }"
            @update="changeVal"
          >
          </AutocompleteComponent>
          <AppBuilder style="overflow: hidden" ref="appBuilder" :app="app" />
        </v-container>
      </pane>
    </splitpanes>
    <v-btn @click="triggerSubmit">Submit</v-btn>
  </div>
</template>

<script>
import AutocompleteComponent from "./autocomplete-component.vue";
import memoComponentMixin from "../../../mixins/memoComponentMixin";
import formPageMixin from "../../../mixins/formPageMixin";
import ShowAttachmentComponent from "./show-attachment-component";
import { Splitpanes, Pane } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
    AutocompleteComponent,
    ShowAttachmentComponent,
    Splitpanes,
    Pane,
  },
  mixins: [memoComponentMixin, formPageMixin],
  async mounted() {
    this.$observable.subscribe("retrieveMemo", async (data) => {
    try{
        var memoType = await this.getMemoJsonId(data.nodeId);
        this.loadForm(memoType);
        await this.fillForm(data.nodeId);
        this.nodeId = data.nodeId;
        
    }

    catch(error){
      console.log(error);
    }

      // this.loadForm(this.selected, this.fillForm("722454"));
    });
  },
  data() {
    return {
      d: this.val,
      url: "lookup/get/category/memoType",
      selected: "",
      nodeId:null,
      richText: {},
      Memodata: [],
      bwsId: 680482,
      app: {},
    };
  },
  props: ["val", "field"],

  methods: {
    changeVal(event) {
      this.nodeId = null;
      if (event.value.value) {
        this.$refs.appBuilder.setAppData({
          pages: [{ sections: [{ forms: [] }] }],
        });
        console.log(this.$refs.appBuilder);
        this.selected = event.value.value.value;
        this.loadForm(this.selected);
        console.log(this.selected);
      } else {
        this.$refs.appBuilder.setAppData({
          pages: [{ sections: [{ forms: [] }] }],
        });
      }
    },

    async fillForm(nodeId) {
      var Memodata = await this.getMemoData(nodeId);
      console.log("MemoData", Memodata);
      if (Memodata == undefined) return;
      Memodata.memoValues.forEach((element) => {
        var model = { [element.jsonKey]: element.value };
        console.log(model);
        console.log(element);
        this.$refs.appBuilder.setModelData(element.jsonKey, model);
      });
      
    },
    

    triggerSubmit() {
      var formKeys = this.$refs.appBuilder.getFormKeyByPageKey("memoPage");
      this.richText = {};
      formKeys.forEach((element) => {
        var data = this.$refs.appBuilder.getModelData(element);
        this.richText[element] = data[element];
        console.log(data);
        console.log(element);
      });
      let data;
      data = {
        requestId: this.d.requestId,
        jsonId: this.selected,
        nodeId: this.nodeId,
        values: this.richText,
      };

      this.setMemoData(data);
    },
  },
  watch: {
    val: function (newVal) {
      for (var key in newVal) {
        this.d[key] = newVal[key];
      }
      // this.d = newVal
    },
  },
};
</script>

<style>
.bg-white {
  background-color: white !important;
}
</style>