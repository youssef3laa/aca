<template>
  <span
    style="display: block; background-color: #fff; border-bottom:1px solid #e1e1e1;"
  >
    <button
      style="margin-bottom: 0px;border-radius-top-righ:6px; border-radius-top-left:6px"
      v-for="tab in tabs"
      :key="tab.id"
      @click="selectedTab(tab)"
      :class="['tab-btn', { active: selected === tab }]"
    >
      <v-icon right color="info">
        {{ tab.icon }}
      </v-icon>
      {{ $t(tab.name) }}
      <span v-if="tab.value" style="margin: 10px;color: rgba(2, 120, 174, 1)">{{ tab.value }}</span>
    </button>
  </span>
</template>
<script>
// import FormBuilder from '../builders/form-builder'
export default {
  components: {
    // FormBuilder,
  },
  data() {
    return {
      selected: null,
      isActive: null,
      formId: null,
    }
  },
  watch:{
    tabs: function(newVal){
      console.log("TabBuilder", newVal)
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
      console.log('Tab Selected', tab)

      // if (this.page) {
      //   for (let i = 0; i < this.page.sections.sec.length; i++) {
      //     const section = this.page.sections.sec[i]
      //     if (section.isTab) {
      //       if (section.visibility) {
      //         this.page.sections.sec[i].visibility = 'hidden'
      //       } else {
      //         this.page.sections.sec[i].display = 'none'
      //       }
      //     }
      //     if (section.tabId == this.selected.id) {
      //       this.page.sections.sec[i].display = 'block'
      //       if (section.isCanvas) {
      //         this.$observable.fire('resizeCanvas')
      //         // this.page.sections[i].visibility = 'visible'
      //       }
      //       // else {
      //       //   this.page.sections[i].display = 'block'
      //       //   console.log(this.page.sections[i].display)
      //       // }
      //     }
      //   }
      // }
    },
  },
  mounted() {
    console.log(this.tabs)
    for (let i = 0; this.tabs && i < this.tabs.length; i++) {
      if (this.tabs[i].isActive) {
        this.selected = this.tabs[i]
        this.selectedTab(this.tabs[i])
        // this.$observable.fire(this.tabkey, this.tabs[i].id)
      }
    }

    // if (this.page) {
    //   // console.log(this.page)
    //   for (let i = 0; this.page.tabs && i < this.page.tabs.length; i++) {
    //     if (this.page.tabs[i].isActive) {
    //       this.selected = this.page.tabs[i]
    //       console.log('this.isActive')
    //       console.log(this.page)
    //     }
    //   }
    // } else if (this.section) {
    //   console.log(this.section)
    //   for (let k = 0; this.section.tabs && k < this.section.tabs.length; k++) {
    //     if (this.section.tabs[k].isActive) {
    //       this.selected = this.section.tabs[k]
    //       console.log(this.section)
    //       console.log('this.isActive')
    //     }
    //   }
    // }
  },
  props: ['tabs', 'tabkey'],
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
