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
  >
    <template #label>
      <span v-t="field.name"></span>
    </template>

  </v-autocomplete>
  <!--  multiple-->
</template>

<script>
export default {
  name: 'autoCompleteComponent',
  data() {
    return {
      //   items: ['foo', 'bar', 'fizz', 'buzz'],
      //   values: ['foo', 'bar'],
      items: this.val,
      //   value: this.field.value,
      value: this.val.value,

    }
  },
  methods: {
    autocompleteChange: function () {
      // console.log(this.value)
      // console.log(this.val)
      // console.log(this.items.list)
      const list = this.items.list
      if (!list) return;
      const selectedObject = list.filter(el => el.value === this.value)[0]
      // console.log("selectedObject", selectedObject)
      this.$emit('update', {
        name: this.field.name,
        value: {
          list: list,
          value: selectedObject
        },
        type: 'autocompleteChange'
      })
    },
  },
  props: ['val', 'field'],
  watch: {
    val: function (newVal, oldVal) {

      console.log(oldVal)
      // this.val = newVal
      this.items = newVal;
      this.value = newVal.value
      console.log("val", this.val)
    },
  },
  mounted() {
    this.autocompleteChange();
  }
}
</script>
<style>
.v-select__slot {
  padding: 7px 12px;
}
</style>
