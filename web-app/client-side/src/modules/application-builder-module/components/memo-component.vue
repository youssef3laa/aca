<template>
  <div>
    <splitpanes class="default-theme"  dir="ltr">
      <pane>
        <ShowAttachmentComponent  :key="requestEntityId" :bwsId="bwsId" :requestEntityId="requestEntityId"> </ShowAttachmentComponent>
      </pane>
      <pane class="bg-white" dir="rtl">
        <v-container>
          <AutocompleteComponent
            :field="field"
            :val="getAutoCompeleteVal"
            @update="changeVal"
          >
          </AutocompleteComponent>
          <AppBuilder ref="appBuilder" :app="app" />
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

  data() {
    return {
      selected: "",
      richText: {},
      Memodata: [],
      bwsId : "",
      requestEntityId : "",
      app1: {
        pages: [
          {
            key: "memoPage",
            sections: [
              {
                forms: [
                  {
                    key: "form1",
                    resizable: {
                      forms: [
                        {
                          type: "collapse",
                          name: "المرفقات",
                          key: "attachments",
                          inputs: [
                            {
                              type: "ShowAttachmentComponent",
                              name: "showattachment",
                              col: 12,
                            },
                          ],
                          model: {},
                        },
                        {
                          type: "collapse",
                          name: "النص الثاني",
                          key: "richtext2",
                          inputs: [
                            {
                              type: "richtextComponent",
                              name: "richtext1",
                              col: 12,
                            },
                            {
                              type: "richtextComponent",
                              name: "richtext2",
                              col: 12,
                            },
                          ],
                          model: {
                            richtext1: "<p></p>",
                            richtext2: "<p></p>",
                          },
                        },
                      ],
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
      app: {},
    };
  },
  props: ["field", "val"],

  methods: {
    changeVal(event) {
      // this.$refs.appBuilder.setAppData({});
      this.selected = event.value.value.text;
      this.loadForm(this.selected, this.fillForm);
      console.log(this.selected);
      console.log(JSON.stringify(this.app1));
    },

    async fillForm() {
      this.bwsId = 680482;
      this.requestEntityId = 1;
      this.Memodata = await this.getMemoData(this.selected);
      console.log(this.Memodata);
      if (this.Memodata == undefined) return;
      this.Memodata[this.Memodata.length - 1].memoValues.forEach((element) => {
        var model = { [element.jsonKey]: element.value };
        console.log(model);
        console.log(element);
        this.$refs.appBuilder.setModelData(element.jsonKey, model);
      });
    },

    triggerSubmit() {
      var formKeys = this.$refs.appBuilder.getFormKeyByPageKey("memoPage");

      formKeys.forEach((element) => {
        var data = this.$refs.appBuilder.getModelData(element);
        this.richText[element] = "<![CDATA[" + data[element] + "]]>";
        console.log(data);
        console.log(element);
      });
      let data;
      data = {
        requestId: "1",
        jsonId: this.selected,
        values: this.richText,
      };
      this.setMemoData(data);
    },
  },
  computed: {
    getAutoCompeleteVal() {
      var val = {
        list: [
          { value: 1, text: "memo1" },
          { value: 2, text: "memo2" },
        ],
      };
      return val;
    },
  },
};
</script>

<style>
.bg-white{
  background-color: white !important;
}
</style>