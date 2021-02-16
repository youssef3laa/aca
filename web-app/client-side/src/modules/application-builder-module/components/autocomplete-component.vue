<template>
  <validation-provider
    :name="field.name"
    :rules="field.rule"
    v-slot="{ errors }"
    :vid="field.name"
  >
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
      :disabled="readonly"
      v-on:change="autocompleteChange"
      :search-input.sync="search"
      :loading="loading"
      allow-overflow
    >
      <template #label>
        <span v-t="field.name"></span>
      </template>
    </v-autocomplete>
    <span class="red--text">{{ errors[0] }}</span>
  </validation-provider>

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
      readonly: null,
      show: true,
    };
  },
  methods: {
    fetch(v, url) {
      http
        .get(url)
        .then((response) => {
          const res = response.data.data.map((element) => {
            let obj = {};
            if (element["cn"]) {
              obj = {
                name: element["name_ar"],
                value: element["cn"],
                text: element["name_ar"],
                code: element["groupCode"],
                object: element,
              };
            } else if (element["unitCode"]) {
              obj = {
                value: element["unitCode"],
                text: element["name_ar"],
                object: element,
              };
            } else {
              obj = {
                value: element["key"],
                text: element["arValue"],
                object: element,
              };
            }
            return obj;
          });
          this.items.list = res.filter((e) => {
            // console.log(e)
            return (e.name || "").indexOf(v || "") > -1;
          });
          // console.log(this.items.list)

          if (this.val.default) {
            this.value = this.val.default;
            this.autocompleteChange();
          } else if (
            (v == null || v == "") &&
            this.field.autofill &&
            this.items.list.length == 1
          ) {
            this.value = this.items.list[0].value;
            this.autocompleteChange();
          }

          this.loading = false;
        })
        .catch((err) => console.log(err));
    },
    querySelections(v, url) {
      this.loading = true;
      this.fetch(v, url);
    },
    autocompleteChange: function() {
      const list = this.items.list;
      if (!list) return;
      const selectedObject = list.filter((el) => el.value === this.value)[0];
      // console.log("selectedObject", selectedObject)
      if (this.field.publish) {
        this.$observable.fire(this.field.publish, selectedObject);
      }
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
    field: function(newVal) {
      if (newVal.readonly == true || newVal.readonly == false) {
        this.readonly = newVal.readonly;
      } else if (newVal.readonly) {
        this.readonly = this.model[newVal.readonly];
      }
    },
    val: {
      immediate: true,
      handler(newVal) {
        // console.log(oldVal)
        // this.val = newVal
        if (newVal.url) {
          this.items.list = [];
          this.querySelections("", newVal.url);
        }
        this.items = newVal;
        if (newVal.value != null || newVal.value != undefined)
          this.value = newVal.value;
        // console.log('val', this.val)
        if (this.field.readonly == true || this.field.readonly == false) {
          this.readonly = this.field.readonly;
        } else if (this.field.readonly) {
          this.readonly = this.model[this.field.readonly];
        }
        if (this.field.show) {
          this.show = this.model[this.field.show];
        }
      },
    },
    // val: function(newVal, oldVal) {
    //   console.log(oldVal)
    //   // this.val = newVal
    //   if (newVal.url) {
    //     this.items.list = []
    //     this.querySelections('', newVal.url)
    //   }
    //   this.items = newVal
    //   this.value = newVal.value
    //   console.log('val', this.val)
    // },
    search: function(val) {
      if (this.val.url) {
        val && val !== this.value && this.querySelections(val, this.val.url);
      }
    },
  },
  mounted() {
    if (this.val.url) {
      this.querySelections(null, this.val.url);
    }
    // this.autocompleteChange();
  },
  props: ["val", "field", "model"],
};
</script>
<style>
.v-select__slot {
  padding: 7px 12px;
}
</style>
