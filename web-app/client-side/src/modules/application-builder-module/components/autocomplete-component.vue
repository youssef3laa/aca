<template>
  <v-autocomplete
    v-model="value"
    :items="items"
    outlined
    dense
    chips
    deletable-chips
    small-chips
    :label="field.name"
    multiple
    clearable
    v-on:change="test"
    color="outline"
  ></v-autocomplete>
</template>

<script>
export default {
  name: 'autoCompleteComponent',
  data() {
    return {
      //   items: ['foo', 'bar', 'fizz', 'buzz'],
      //   values: ['foo', 'bar'],
      items: this.field.items,
      //   value: this.field.value,
      value: this.val,
    }
  },
  methods: {
    test: function() {
      console.log(this.value)
    },
  },
  props: ['val', 'field'],
  watch: {
    val: function(newVal, oldVal) {
      console.log(oldVal)
      this.val = newVal
    },
  },
}
</script>
<style >
  .v-select__slot{
    padding: 7px 12px;
  }
</style>