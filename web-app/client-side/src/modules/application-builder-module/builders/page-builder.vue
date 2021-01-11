<template>
  <span>
    <span v-if="page.tabs">
      <button
        v-for="tab in page.tabs"
        :key="tab.id"
        @click="selectedTab (tab)"
        :class="['tab-btn', { active: selected === tab }]"
      >
        {{ tab.name }}
      </button>
    </span>
    <div v-for="(section, key) in page.sections" :key="key">
      <SectionBuilder
        v-bind:style="[
          section.type == 'tab' && section.display == 'none'
            ? { display: 'none' }
            : { display: 'block' },
        ]"
        v-on:modelChange="dataChange"
        :section="section"
      />
    </div>
  </span>
</template>

<script>
import SectionBuilder from './section-builder'
export default {
  name: 'PageBuilder',
  components: {
    SectionBuilder,
  },
  data() {
    return {
      tab: null,
      selected: null,
      isActive : null
    }
  },
  props: ['page'],
  methods: {
    dataChange: function(model) {
      console.log('Page Builder')
      console.log(model)
      this.$emit('modelChange', model)
    },
    selectedTab: function(tab) {
      this.selected = tab;
      for (let i = 0; i < this.page.sections.length; i++) {
        const section = this.page.sections[i]
        if (section.type === 'tab') {
          this.page.sections[i].display = 'none';
        }
        if (section.tabId == this.selected.id) {
          this.page.sections[i].display = 'block';
        }
      }
    },
  },
  mounted() {
      for (let i = 0; i < this.page.tabs && this.page.tabs.length; i++) {
        if(this.page.tabs[i].isActive){
          this.selected = this.page.tabs[i]
          console.log("this.isActive")
        }
      }
},

  
}
</script>

<style>
.tab-btn {
  padding: 6px 10px;
  background: #ffffff;
  cursor: pointer;
  margin-bottom: 1rem;
  border: 2px solid #cccccc;
  outline: none;
}

.active {
  border-bottom: 3px solid green;
  background: #fcfc;
}

.tab {
  border: 1px solid #ccc;
  padding: 10px;
}
</style>
