<template>
  <v-radio-group v-model="selected" row>
    <span v-if="title" class="radio-group-component-title-class col-2" :style="{color: color}" v-t="title"></span>
    <v-radio v-for="(type, index) in d.options"
             :key="index"
             :value="type.name"
             :color="(selected == type.name)? color: 'black'"
             class="radio-group-component-radio-class col-3">
      <template #label>
        <span v-t="type.label" :style="{color: (selected == type.name)? color:'black'}"></span>
      </template>
    </v-radio>
  </v-radio-group>
</template>
<script>
export default {
  name: 'radioGroupComponent',
  props: ['val', 'field'],
  data() {
    return {
      d: this.val,
      title: this.field.title,
      color: (this.field.color)? this.field.color:'#07689F',
      selected: this.val.value
    }
  },
  methods: {
    onValueChange: function() {
      this.$emit('update', {
        name: this.field.name,
        value: this.selected
      })
    },
  },
  watch: {
    val: function(newVal) {
      for(var key in  newVal){
        this.d[key] = newVal[key]
      }
      this.selected = this.d.value
    },
    selected: function() {
      this.onValueChange()
    }
  },
}
</script>

<style>
  .radio-group-component-title-class {
    font-size: 20px;
    padding: 0px 30px 0px 30px;
    text-align: justify;
  }

  .radio-group-component-radio-class {
    padding: 0px 0px 0px 0px;
    font-weight: bold;
  }
</style>