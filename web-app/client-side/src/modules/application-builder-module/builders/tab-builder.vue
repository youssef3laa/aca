<template>
  <!-- <v-container> -->
  <span>
    <v-tabs v-model="tab" align-with-title height="90">
      <v-tabs-slider color="info"></v-tabs-slider>
      <v-tab v-for="tab in section.tabs" :key="tab.id">
        <v-icon right color="info" >
          {{tab.icon}}
        </v-icon>
        <span class="tab-name">{{ tab.name }}</span>
      </v-tab>
    </v-tabs>
    <v-tabs-items v-model="tab" v-if="section.tabs">
      <v-tab-item   v-for="formData in section.forms" :key="formData.id">
        <v-card flat>
          <span v-if="formData.resizable">
            <splitpanes class="default-theme" dir="rtl">
              <pane v-bind:style="{'background': form.background}" v-for="(form,index) in formData.resizable.forms" :key="index">
                <span dir="rtl">
                  <FormBuilder :forms="form" :model="form.model" />
                </span>
              </pane>
            </splitpanes>
            <!-- <Multipane class="vertical-panes" layout="vertical">
              <div
                class="pane"
                :style="{
                  width: formData.width,
                }"
              >
                <FormBuilder :forms="formData" :model="formData.model" />
              </div>
              <MultipaneResizer></MultipaneResizer>
            </Multipane> -->
          </span>
          <span v-else>
            <FormBuilder :forms="formData" :model="formData.model" />
          </span>
          <!-- <v-card-text v-text="formData.inputs.name"></v-card-text> -->
        </v-card>
      </v-tab-item>
    </v-tabs-items>
  </span>

  <!-- </v-container> -->
</template>

<script>
import FormBuilder from './form-builder'
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'
// import { Multipane, MultipaneResizer } from 'vue-multipane'
export default {
  tab: null,
  name: 'TabBuilder',
  components: {
    FormBuilder,
    Splitpanes,
    Pane,
    // Multipane,
    // MultipaneResizer,
  },
  data() {
    return {
      tab: null,
    }
  },
  mounted() {
    console.log(this.section.tabs)
  },
  props: ['section'],
}
</script>

<style>
.v-tab--active {
  background: rgba(2, 120, 174, 0.1);
}

.tab-name {
  margin: 0 10px 0 10px;
  font-weight: bold;
  font-size: 16px;
}

.v-tab--active {
  background:'#0278ae';
}

.splitpanes.default-theme .splitpanes__splitter:after, .splitpanes.default-theme .splitpanes__splitter:before{
  background-color: #d1d1d1;
}
/* .splitpanes.default-theme .splitpanes__pane {
  background: transparent!important;
} */
.vertical-panes {
  width: 100%;
  height: 400px;
  border: 1px solid #ccc;
}
.vertical-panes > .pane {
  text-align: left;
  padding: 15px;
  overflow: hidden;
  background: #eee;
}
.vertical-panes > .pane ~ .pane {
  border-left: 1px solid #ccc;
}
.v-tabs-bar.v-tabs-bar--is-mobile .v-tab {
  margin: 0px !important;
}

.v-application--is-rtl
  .v-tabs--align-with-title
  > .v-tabs-bar:not(.v-tabs-bar--show-arrows):not(.v-slide-group--is-overflowing)
  > .v-slide-group__wrapper
  > .v-tabs-bar__content
  > .v-tabs-slider-wrapper
  + .v-tab {
  margin: 0px !important;
}
.v-tabs {
  border-top-left-radius: 6px !important;
  border-top-right-radius: 6px !important;
  border-bottom: 2px solid #e1e1e1;
}
</style>
