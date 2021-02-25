<template>
  <div class="sidebar-wrapper">
    <v-btn text
      v-for="(d, key) in val"
      :key="key"
      @click="btnClicked(d.action)"
        width="90%"
       color="#07689F" style="margin-bottom: 10px;align-self:center; background-color: #f2f7fa"
    >
      <div class="icon-wrapper">
        <i :class="d.icon"></i>
      </div>
      <span style="font-weight: bold; color: black">{{ $t(d.name) }}</span> <span style="margin-right: 10px;margin-left: 10px">{{ d.notifications }}</span>
    </v-btn>
  </div>
</template>

<script>
export default {
  props: ["val", "field"],
  data() {
    return {
      selected: null,
    };
  },
  methods: {
    btnClicked(action) {
      this.selected = action;
      this.$emit("btnClicked", action);
    },
  },
};
</script>

<style>
.sidebar-wrapper {
  display: flex;
  flex-direction: column;
  background: white;
  padding-top: 10%;
  height: 100%;
}

.icon-wrapper {
  display: flex;
  color: white;
  width: 22px;
  height: 22px;
  font-size: 12px;
  justify-content: center;
  align-items: center;
  background: #0278ae;
  border-radius: 5px;
  margin-left: 10px;
}
</style>
