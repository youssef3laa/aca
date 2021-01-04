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
    <v-btn @click="trigger">Submit</v-btn>
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
      app: {},
      // app: {
      //   pages: [
      //     {
      //       key: "memoPage",
      //       sections: [
      //         {
      //           type: "collapse",
      //           name: "نوع النص",
      //           forms: [
      //             {
      //               key: "form1",
      //               inputs: [
      //                 {
      //                   type: "richtextComponent",
      //                   name: "richtext1",
      //                   col: 12,
      //                 },
      //               ],
      //               model: {
      //                 richtext1: "",
      //               },
      //             },
      //           ],
      //         },
      //       ],
      //     },
      //   ],
      // },
    };
  },
  props: ["field"],

  methods: {
    changeVal: function (event) {
      var selected = event.value.value.text;
      this.loadForm(selected, this.fillForm);
      // setTimeout(() => {
      //   let model = this.$refs.appBuilder.getModelData("form1");
      //   console.log(model);
      // }, 100);
      // let model = this.$refs.appBuilder.getModelData("form1");

      // var sectionsData = this.getMemoSections(event.value.value.text);
    },

    async fillForm() {
      var model = await this.getMemoData();
      this.$refs.appBuilder.setModelData("form1", model);
      // this.$refs.appBuilder.setModelData("form2", model);
    },
    trigger() {
      let model = this.$refs.appBuilder.getModelData("form1");
      console.log(model);
      let data = {
        requestId: "",
        JSONId: "form1",
        memo: [{ Key: "", value: "richtext1" }],
      };

      this.setMemoData(data);
    },
  },
  computed: {
    getFormData() {
      var response = [
        { component: "RichtextComponent", numberOfItems: 2, val: "" },
        // { component: "AutocompleteComponent", numberOfItems: 1, val: "" },
      ];
      console.log(response);
      console.log(this.response);
      return response;
    },
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