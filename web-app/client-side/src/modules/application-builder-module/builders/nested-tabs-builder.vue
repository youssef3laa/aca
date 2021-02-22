<template>
  <v-card flat style="padding: 10px 0 5px 0;">
    <span
      style="display: block; background-color:#f8f8f8; border:1px solid #e1e1e1;
    border-radius:6px;border-radius-top-righ:6px; border-radius-top-left:6px;
    margin: 15px; padding: 5px;"
    >
      <button
        style="padding:15px;"
        v-for="tab in tabs"
        :key="tab.id"
        @click="selectedTab(tab)"
        :class="['tab-btn', { active: selected === tab }]"
      >
        {{ $t(tab.name) }}
      </button>
    </span>
  </v-card>
</template>

<script>
export default {
  data() {
    return {
      selected: null,
      isActive: null,
      formId: null,
    }
  },
  methods: {
    selectedTab: function(tab) {
      this.selected = tab

      this.$observable.fire(this.tabkey, tab.id)

      if (tab.publish) {
        if (!(tab.publish instanceof Array)) tab.publish = [tab.publish]
        for (let key in tab.publish) {
          this.$observable.fire(tab.publish[key])
        }
      }
      console.log('Nested Tab Selected', tab)
    },
  },
  mounted() {
    console.log(this.tabs)
    for (let i = 0; this.tabs && i < this.tabs.length; i++) {
      if (this.tabs[i].isActive) {
        this.selected = this.tabs[i]
        this.selectedTab(this.tabs[i])
      }
    }
  },
  props: ['tabs', 'tabkey'],
}
</script>

<style scoped>
.tab-btn {
 outline: none;
}
.active {
  /* border-bottom: 3px solid #0278ae !important; */
  border: 1px solid white;
  border-radius: 6px;
  background-color: white;
  color: #0278ae;
}
</style>
