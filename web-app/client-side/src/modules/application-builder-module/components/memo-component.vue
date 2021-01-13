<template>
  <div>
    <AutocompleteComponent
      :field="field"
      :val="getAutoCompeleteVal"
      @update="changeVal"
    >
    </AutocompleteComponent>

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
      richText: {},
      Memodata: [],
      app: {}
    }
  },
  props: ["field","val"],

  methods: {
    changeVal(event) {
      this.$refs.appBuilder.setAppData({});
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
    },

    triggerSubmit() {
      var formKeys = this.$refs.appBuilder.getFormKeyByPageKey("memoPage");

      formKeys.forEach((element)=>{
          var data = this.$refs.appBuilder.getModelData(element);
          this.richText[element] = "<![CDATA[" + data[element] + "]]>";
          console.log(data);
          console.log(element);
      })
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
  }
};
</script>

<style>
</style>