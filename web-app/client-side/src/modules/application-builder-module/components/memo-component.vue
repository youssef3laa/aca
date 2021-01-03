<template>
  <div>
  
    <AutocompleteComponent :field="field" :val="val" @update="changeVal">  </AutocompleteComponent>

  <div v-if="value">
    <div v-for="(data, key) in getData" :key="key">
      <div v-for="(numberOfItems, key) in data.numberOfItems" :key="key">
        <component :is="data.component" :field="field"> </component>
      </div>
    </div>
    </div>
  </div>
</template>

<script>
import AutocompleteComponent from "./autocomplete-component.vue";
import RichtextComponent from "./rich-text-component";

export default {
  components: { RichtextComponent, AutocompleteComponent },

  data() {
    return {
      value : null,
      richText: [2, 1, 6],
      response: [{ component: "RichtextComponent", numberOfItems: 2}],
      val: {list:["form1","form2"]}
    };
  },
  props: ["field"],

  methods: {
    changeVal :  function(event) {
      console.log(event);
      this.value = event;
    },

  
  },
  computed:{
    getData() {
      var response =[{ component: "RichtextComponent", numberOfItems: 2}];
      console.log(response);
      console.log(this.response);
      return response;
    },
  }
};
</script>

<style>
</style>