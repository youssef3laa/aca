<template>
  <v-autocomplete
    v-model="value"
    :items="items.list"
    chips
    clearable
    color="outline"
    deletable-chips
    dense
    outlined
    small-chips
    v-on:change="autocompleteChange"
    :search-input.sync="search"
    :loading="loading"
  >
    <template #label>
      <span v-t="field.name"></span>
    </template>
  </v-autocomplete>
  <!--  multiple-->
</template>

<script>
import http from "../../core-module/services/http";
export default {
  name: "autoCompleteComponent",
  data() {
    return {
      //   items: ['foo', 'bar', 'fizz', 'buzz'],
      //   values: ['foo', 'bar'],
      items: this.val,
      //   value: this.field.value,
      value: this.val.value,
      search: null,
      loading: false,
    };
  },
  methods: {
    fetch(v, url) {
      http
        .get(url)
        .then((response) => {
          const res = response.data.data.map((element) => {
            let obj = {};
            if(element["cn"]){
              obj = {
                name: element["name_ar"],
                value: element["cn"],
                text: element["name_ar"],
                code: element["groupCode"],
                object: element
              }
            }else{
              obj = {
                value: element["key"],
                text: element["arValue"],
                object: element
              }
            }
            return obj;
          });
          this.items.list = res.filter((e) => {
            console.log(e);
            return (e.name || "").indexOf(v || "") > -1;
          });
          this.loading = false;
        })
        .catch((err) => console.log(err));
    },
    querySelections(v, url) {
      this.loading = true;
      this.fetch(v, url)
    },
    autocompleteChange: function () {
      const list = this.items.list;
      if (!list) return;
      const selectedObject = list.filter((el) => el.value === this.value)[0];
      // console.log("selectedObject", selectedObject)
      this.$emit("update", {
        name: this.field.name,
        value: {
          list: list,
          value: selectedObject,
        },
        type: "autocompleteChange",
      });
    },
  },
  watch: {
    val: function (newVal, oldVal) {
      console.log(oldVal);
      // this.val = newVal
      if(newVal.url)  this.fetch(null, this.val.url)
      this.items = newVal;
      this.value = newVal.value;
      console.log("val", this.val);
    },
    search: function (val) {
      if (this.val.url) {
        val && val !== this.value && this.querySelections(val, this.val.url);
      }
    },
  },
  mounted() {
    if (this.val.url) {
      this.fetch(null, this.val.url)
    }
    // this.autocompleteChange();
  },
  props: ["val", "field"],
};
</script>
<style>
.v-select__slot {
  padding: 7px 12px;
}
</style>