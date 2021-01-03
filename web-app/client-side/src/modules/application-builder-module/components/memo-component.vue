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
  </div>
</template>

<script>
import AutocompleteComponent from "./autocomplete-component.vue";
// import AppBuilder from '../builders/app-builder'

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
    AutocompleteComponent,
  },

  data() {
    return {
      value: null,
      richText: [2, 1, 6],
      val: { list: ["form1", "form2"] },
      app: {
        pages: [
          {
            key: "page1",
            sections: [
              {
                type: "collapse",
                name: "جهة الإختصاص",
                forms: [
                  {
                    key: "form1",
                    inputs: [
                      {
                        type: "richtextComponent",
                        col: 12,
                      },
                    ],
                  },
                ],
              },
              {
                type: "collapse",
                name: "جهة الإختصاص",
                forms: [
                  {
                    key: "form1",
                    inputs: [
                      {
                        type: "richtextComponent",
                        col: 12,
                      },
                    ],
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
    changeVal: function (event) {
      this.$refs.appBuilder.setSectionData("page1", {});
      console.log(event);
      this.value = event;
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
      var val = { list: ["form1", "form2"] };
      return val;
    },
  },
};
</script>

<style>
</style>