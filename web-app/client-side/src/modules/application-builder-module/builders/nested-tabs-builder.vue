<template>
  <v-card flat style="padding: 10px 0 5px 0;">
    <span
      style="display: block; background-color:#f8f8f8; border:1px solid #e1e1e1;
    border-radius:6px;border-radius-top-righ:6px; border-radius-top-left:6px;
    margin: 15px; padding: 5px;"
    >
      <v-row>
        <v-col :cols="10" id="tabs-list">
          <button
            v-ripple
            style="padding:15px;margin-right: 15px;"
            v-for="tab in tabs"
            :key="tab.id"
            @click="selectedTab(tab)"
            :class="['tab-btn', { active: selected === tab }]"
            :cols="3"
          >
            {{ $t(tab.name) }}
          </button>
        </v-col>
        <v-col :cols="2">
          <div class="scroller-wrapper" style="float:left;">
            <v-btn class="scroller" @click="scroll('right', 'tabs-list')">
              <i class="fas fa-chevron-right"></i>
            </v-btn>
            <v-btn class="scroller" @click="scroll('left', 'tabs-list')">
              <i class="fas fa-chevron-left"></i>
            </v-btn>
          </div>
        </v-col>
      </v-row>
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
    scroll(direction, content) {
      let scrollPixels = 180
      if (direction == 'left') scrollPixels = -scrollPixels

      console.log(scrollPixels)
      const element = document.getElementById(content)
      // element.animate({scrollLeft: '=-300'},1000);
      var scroll = scrollPixels / 10

      var scrolled = 0
      const interval = setInterval(() => {
        element.scrollLeft += scroll
        scrolled += scroll
        console.log(scrolled)
        console.log(scrollPixels)
        if (Math.round(scrolled) == Math.round(scrollPixels)) {
          clearInterval(interval)
        }
      }, 20)
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
  border: 1px solid white !important;
  border-radius: 6px !important;
  background-color: white !important;
  color: #0278ae !important;
}
button.tab-btn.active {
  color: #0278ae !important;
}
#tabs-list {
  overflow-x: hidden;
  overflow-y: hidden;
  display: inline-flex;
}
.scroller {
  min-width: 34px !important;
  width: 34px !important;
  height: 34px !important;
  text-indent: 0em !important;
  margin-left: 5px;
  background-color: transparent !important;
  border: 1px solid #07689f;
  color: #07689f !important;
  box-shadow: none !important;
}
</style>
