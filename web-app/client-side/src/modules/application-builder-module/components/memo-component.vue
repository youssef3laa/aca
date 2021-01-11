<template>
  <div>
    <AutocompleteComponent
      :field="field"
      :val="getAutoCompeleteVal"
      @update="changeVal"
    >
    </AutocompleteComponent>

    <!-- <div v-if="value">
      <div v-for="(data, key) in getFormData" :key="key">
        <div v-for="(numberOfItems, key) in data.numberOfItems" :key="key">
          <component :is="data.component" :field="field" :val="data.val">
          </component>
        </div>
      </div>
    </div> -->
    <v-container>
      <AppBuilder ref="appBuilder" :app="app" />
    </v-container>
    <v-btn @click="triggerSubmit">Submit</v-btn>
  </div>
</template>

<script>
import AutocompleteComponent from "./autocomplete-component.vue";
import memoComponentMixin from "../../../mixins/memoComponentMixin";
import formPageMixin from "../../../mixins/formPageMixin";

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
    AutocompleteComponent,
  },
  mixins: [memoComponentMixin, formPageMixin],

  data() {
    return {
      selected: "",
      // richText: [],
      richText: {},
      Memodata: [],
      app: {},
      app2: {
        pages: [
          {
            key: "page1",
            sections: [
              {
                numOfResizable: 2,
                key: "section1",
                tabs: [
                  {
                    key: "tab2",
                    id: 2,
                    name: "المرفقات",
                    icon: "fas fa-paperclip",
                  },
                ],
                forms: [
                  {
                    key: "form1",
                    resizable: {
                      forms: [
                        {
                          background: "white",
                          inputs: [
                            {
                              type: "InputFileComponent",
                              name: "inputFile",
                              col: 12,
                            },
                          ],
                          model: {
                            inputFile: "",
                          },
                        },
                        {
                          key: "iframeObj",
                          background: "white",
                          inputs: [
                            {
                              type: "IframeComponent",
                              name: "iframeObj",
                              col: 12,
                            },
                          ],
                          model: {
                            iframeObj: {
                              src: "",
                            },
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

      app3: {
        pages: [
          {
            key: "memoPage",
            sections: [
              {
                numOfResizable: 2,
                type: "collapse",
                name: "نوع النص",

                tabs: [
                  {
                    key: "tab1",
                    id: 1,
                    name: "البيانات الأساسية",
                    icon: "far fa-file-alt",
                  },
                  {
                    key: "tab2",
                    id: 2,
                    name: "المرفقات",
                    icon: "fas fa-paperclip",
                  },
                ],
                forms: [
                  {
                    key: "form1",
                    resizable: {
                      forms: [
                        {
                          key: "form1",
                          inputs: [
                            {
                              type: "richtextComponent",
                              name: "richtext1",
                              col: 12,
                            },
                          ],
                          model: {
                            richtext1: "",
                          },
                        },
                        {
                          key: "form1",
                          inputs: [
                            {
                              type: "richtextComponent",
                              name: "richtext1",
                              col: 12,
                            },
                          ],
                          model: {
                            richtext1: "",
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
    };
  },
  props: ["field"],

  methods: {
    changeVal(event) {
      // this.$observable.subscribe("richtextvalueUpdate", (richTextObj) => {
      //   console.log(richTextObj);
      //   this.richText[richTextObj.fieldName] =
      //     "<![CDATA[" + richTextObj.value + "]]>";
      //   console.log(this.richText);
      // });
      this.selected = event.value.value.text;
      this.loadForm(this.selected, this.fillForm);
      console.log(this.selected);
    },

    async fillForm() {
      this.Memodata = await this.getMemoData(this.selected);
      console.log(this.Memodata);
      if (this.Memodata == undefined) return;
      this.Memodata[this.Memodata.length - 1].memoValues.forEach((element) => {
        var model = { [element.jsonKey]: element.value };
        console.log(model);
        console.log(element);
        this.$refs.appBuilder.setModelData(element.jsonKey, model);
      });
      // this.$refs.appBuilder.setModelData("richtext1", { richtext1: "Snoopy" });
    },

    triggerSubmit() {
      // let model = this.$refs.appBuilder.getModelData("richtext1");
      // console.log(model);
      // const richTextName = Object.keys(model)[0];
      // console.log(richTextName);
      //  this.richText[richTextObj.fieldName] =
      //     "<![CDATA[" + richTextObj.value + "]]>";
      
      // this.Memodata[this.Memodata.length - 1].memoValues.forEach((element) => {
      //   var data = this.$refs.appBuilder.getModelData(element.jsonKey);
      //   this.richText[element.jsonKey] = "<![CDATA[" + data + "]]>";
      //   console.log(data);
      //   console.log(element.jsonKey);
      // });

     var test = this.$refs.appBuilder.getModelData('richtext1');
      console.log(test);
      let data;
      data = {
        requestId: "1",
        jsonId: this.selected,
        values: this.richText,
      };
      //[{ key: element.formName , value: element, },{ key: "form2", value: this.richText }],
      console.log(JSON.stringify(data));
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
</style>