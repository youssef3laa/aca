<template>
  <div>
    <vue-editor
      @blur="$emit('blur')"
      v-model="content"
      @text-change="updateValue"

    ></vue-editor>
  </div>
</template>


<script>
import { VueEditor } from "vue2-editor";
export default {
  name: "richtextComponent",
  components: { VueEditor },
  data() {
    return {
      content: this.val,
    };
  },
  methods: {
    test: function () {
      console.log("text changed");
    },
    updateValue() {
      this.$emit("input", this.content);
    },
  },
  props: ["validate", "val", "field"],
  mounted() {
    if (this.value) {
      this.$refs.input.value = this.value;
    }
  },

  // ['val', 'field'],
  watch: {
    val: function (newVal, oldVal) {
      console.log(oldVal);
      this.content = newVal;
    },
  },
  created() {
    this.$validator = this.$parent.$validator;
  },

  // $_veeValidate: {
  //   name() {
  //     return this.label
  //   },
  //   value() {
  //     return this.value
  //   },
  // },
};
</script>
