<template>
  <span
    style="display: block; background-color: #fff; border-bottom:1px solid #e1e1e1;"
  >
    <button
      style="margin-bottom: 0px;border-radius-top-righ:6px; border-radius-top-left:6px"
      v-for="tab in page.tabs"
      :key="tab.id"
      @click="selectedTab(tab)"
      :class="['tab-btn', { active: selected === tab }]"
    >
      <v-icon right color="info">
        {{ tab.icon }}
      </v-icon>
      {{ tab.name }}
    </button>
  </span>
</template>
<script>
export default {
  data() {
    return {
      selected: null,
      isActive: null,
    }
  },
  methods: {
    selectedTab: function(tab) {
      this.selected = tab
      for (let i = 0; i < this.page.sections.length; i++) {
        const section = this.page.sections[i]
        if (section.isTab) {
          if (section.visibility) {
            this.page.sections[i].visibility = 'hidden'
          } else {
            this.page.sections[i].display = 'none'
          }
        }
        if (section.tabId == this.selected.id) {
          if (section.visibility) {
            this.page.sections[i].visibility = 'visible'
          } else {
            this.page.sections[i].display = 'block'
            console.log(this.page.sections[i].display)
          }
        }
      }
    },
  },
  mounted() {
    console.log(this.page)
    for (let i = 0; this.page.tabs && i < this.page.tabs.length; i++) {
      if (this.page.tabs[i].isActive) {
        this.selected = this.page.tabs[i]
        console.log('this.isActive')
      }
    }
  },
  props: ['page'],
}
</script>

<style scoped>
.tab-btn {
  padding: 6px 10px;
  background: #ffffff;
  cursor: pointer;
  margin-bottom: 1rem;
  border: 2px solid #cccccc;
  outline: none;
}

.active {
  border-bottom: 3px solid #0278ae !important;
  background-color: rgba(2, 120, 174, 0.1);
}
.tab-btn {
  border: none;
  padding: 16px;
}
.tab {
  border: 1px solid #ccc;
  padding: 10px;
}
</style>
