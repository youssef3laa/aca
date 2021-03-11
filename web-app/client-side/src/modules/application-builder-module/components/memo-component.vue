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
        <div class="right-pane-top-bar" v-if="state == 2 || state == 3">
          <div class="name-number-wrapper">
            <div class="versions-number">
              <span style="color:#55c29b">7</span>
            </div>
            <div class="title-date-wrapper">
              <span>{{ title }}</span>
              <div>
                {{ date }}
              </div>
            </div>
          </div>
          <div v-if="!showRichText && state == 2">
            <v-btn
              text
              style="height: 100%; background: #f2f8fb; color: #247aab;float: left;"
              v-on:click="loadLatestMemoRichtext"
            >
              <v-icon>mdi-plus</v-icon>
              {{ $t("editMemo") }}
            </v-btn>
          </div>
          <div class="save-cancel-wrapper" v-if="showRichText && state == 2">
            <v-btn text class="cancel-btn save-cancel-btn">
              <i class="far fa-times-circle"></i>
              <span>{{ $t("cancel") }}</span>
            </v-btn>
            <v-btn @click="triggerSubmit" text class="save-btn save-cancel-btn">
              <i class="far fa-save"></i>
              <span>{{ $t("save") }}</span>
            </v-btn>
          </div>
        </div>
        <v-row style="height:700px">
          <v-col
            :cols="12"
            style="padding-top:0px"
            v-if="state != 3 && showRichText"
          >
            <AutocompleteComponent
              :field="{ name: field.label }"
              :val="{ value: selected, list: [], url: url }"
              v-if="state==1"
              @update="changeVal"
            >
            </AutocompleteComponent>
            <InputComponent  :field="inputComponentField" :val="heading">
            </InputComponent>
            <div style="max-height: 600px; overflow-y: auto">
              <AppBuilder
                style="overflow-y: hidden"
                ref="appBuilder"
                :app="app"
              />
            </div>
          </v-col>

          <v-col :cols="12" v-else-if="state != 1" v-show="!showRichText">
            <IframeComponent :val="iframeOjbect" />
          </v-col>
        </v-row>
      </pane>
    </splitpanes>
  </div>
</template>

<script>
import AutocompleteComponent from "./autocomplete-component.vue";
import memoComponentMixin from "../../../mixins/memoComponentMixin";
import formPageMixin from "../../../mixins/formPageMixin";
import ShowAttachmentComponent from "./show-attachment-component";
import IframeComponent from "../components/iframe-component";
import { Pane, Splitpanes } from "splitpanes";
import "splitpanes/dist/splitpanes.css";
import InputComponent from "./input-component";
import attachmentMixin from "../../../mixins/attachmentMixin";
export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
    AutocompleteComponent,
    ShowAttachmentComponent,
    Splitpanes,
    Pane,
    IframeComponent,
    InputComponent,
  },
  mixins: [memoComponentMixin, formPageMixin, attachmentMixin],
  async mounted() {
    this.$observable.subscribe("retrieveMemo", async (data) => {
      try {
        this.nodeId = data.id;
        this.title = data.name;
        this.date = data.modify_date.split("T")[0];
        var memoType = await this.getMemoJsonId(this.nodeId);
        console.log(memoType);
        this.$refs.appBuilder.setAppData({
          pages: [{ sections: [{ forms: [] }] }],
        });
        this.loadForm(memoType);
        await this.fillForm(this.nodeId);
        console.log(data);
      } catch (error) {
        console.log(error);
      }

      // this.loadForm(this.selected, this.fillForm("722454"));
    });

    this.$observable.subscribe("memoCreate", async (callback) => {
      if (callback) callback(await this.triggerSubmit());
    });
    if (this.state != 1) {
      this.loadLatestMemoBrava();
      this.showRichText = false;
    }
  },
  data() {
    return {
      iframeOjbect: { src: "" },
      d: this.val,
      url: "lookup/get/category/memoType",
      selected: "",
      nodeId: "",
      inputComponentField: {
        label: "heading",
        readonly: false,
      },
      state: 2,
      heading: "",
      richText: {},
      memoData: [],
      bwsId: 680482,
      app: {},
      title: "",
      date: "",
      showRichText: true,
      placeHolder: true,
      loading: false,
    };
  },
  props: ["val", "field"],

  methods: {
    async loadLatestMemoBrava() {
      this.showRichText = false;
      let data = await this.getLatestMemo(this.d.requestId);
      this.title = data.jsonId;
      this.nodeId = data.nodeId;
      await this.openFileInBrave({ fileId: this.nodeId });
    },

    async loadLatestMemoRichtext() {
      this.showRichText = true;
      var memoType = await this.getMemoJsonId(this.nodeId);
      let data = await this.getLatestMemo(this.d.requestId);
      this.nodeId = data.nodeId;
      this.loadForm(memoType);
      await this.fillForm(this.nodeId);
    },
    changeVal(event) {
      this.nodeId = null;
      if (event.value.value) {
        this.$refs.appBuilder.setAppData({
          pages: [{ sections: [{ forms: [] }] }],
        });
        console.log(event);
        this.selected = event.value.value.object.stringKey;
        this.loadForm(this.selected);
        console.log(this.selected);
      } else {
        this.$refs.appBuilder.setAppData({
          pages: [{ sections: [{ forms: [] }] }],
        });
      }
    },

    async fillForm(nodeId) {
      var memoData = await this.getMemoData(nodeId);
      console.log("MemoData", memoData);
      this.selected = memoData.jsonId;
      if (memoData == undefined) return;
      memoData.memoValues.forEach((element) => {
        var model = { [element.jsonKey]: element.value };
        console.log(model);
        console.log(element);
        this.$refs.appBuilder.setModelData(element.jsonKey, model);
        console.log("appbuilder", this.$refs.appBuilder);
      });
    },

    async triggerSubmit() {
      var formKeys = this.$refs?.appBuilder?.getFormKeyByPageKey("memoPage");
      if (
        formKeys == undefined ||
        (formKeys instanceof Array && formKeys.length === 0)
      ) {
        return false;
      }
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

      await this.setMemoData(data);
      this.$observable.fire("refreshHorizontalAttachmentFiles");
      await this.loadLatestMemoBrava();
      this.showRichText = false;
      return true;
    },
  },
  watch: {
    val: function(newVal) {
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
.save-cancel-btn {
  height: 100%;
  background: #f2f8fb;
  float: left;
  width: 45%;
}
.save-cancel-wrapper {
  display: flex;
  justify-content: space-around;
}
.save-btn {
  background-color: rgba(2, 120, 174, 0.1) !important;
  color: rgba(2, 120, 174, 1) !important;
}
.cancel-btn {
  background: rgba(199, 0, 57, 0.1) !important;
  color: rgba(199, 0, 57, 1) !important;
}
.right-pane-top-bar {
  display: flex;
  justify-content: space-between;
  height: 44px;
}
.versions-number {
  display: flex;
  justify-content: center;
  align-items: center;
  background: #effaf6;
  border: 1px solid #effaf6;
  border-radius: 6px;
  width: 40px;
  height: 100%;
  margin-left: 8px;
}
.name-number-wrapper {
  display: flex;
}
.title-date-wrapper{
  display: flex;
  align-items: center;
}
h3 {
  display: none !important;
}
p {
  text-align: right !important;
}
ol {
  direction: rtl !important;
  text-align: left !important;
}
ol ol {
  list-style-type: lower-alpha !important;
  margin-right: 3em !important;
}
ol ol ol {
  list-style-type: upper-roman !important;
}
</style>
