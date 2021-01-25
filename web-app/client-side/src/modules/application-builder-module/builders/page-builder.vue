<template>
  <span>
    <div v-for="(section, key) in page.sections" :key="key">
      <span v-if="page.tabs && key == 0">
        <TabBuilder :page="page" />
      </span>
      <SectionBuilder
        v-bind:style="[
          section.isTab && section.display == 'none'
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
import TabBuilder from './tab-builder'
export default {
  name: 'PageBuilder',
  components: {
    SectionBuilder,
    TabBuilder,
  },
  data() {
    return {
      // tab: null,
      // selected: null,
      // isActive: null,
    }
  },
  props: ['page'],
  methods: {
    dataChange: function(model) {
      console.log('Page Builder')
      console.log(model)
      this.$emit('modelChange', model)
    },
    // selectedTab: function(tab) {
    //   this.selected = tab
    //   for (let i = 0; i < this.page.sections.length; i++) {
    //     const section = this.page.sections[i]
    //     if (section.type === 'tab') {
    //       this.page.sections[i].display = 'none'
    //     }
    //     if (section.tabId == this.selected.id) {
    //       this.page.sections[i].display = 'block'
    //     }
    //   }
    // },
    // selectTabById: function(tabid){

    // }
  },

  // mounted() {
  //   for (let i = 0; this.page.tabs && i < this.page.tabs.length; i++) {
  //     if (this.page.tabs[i].isActive) {
  //       this.selected = this.page.tabs[i]
  //       console.log('this.isActive')
  //     }
  //   }
  // },
}
</script>

<style>
/* .tab-btn {
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
} */
</style>
