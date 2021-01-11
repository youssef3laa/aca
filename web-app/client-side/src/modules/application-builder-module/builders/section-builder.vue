<template>
  <v-container style="padding-top: 0; padding-bottom:0">
    <TabBuilder v-if="sec.tabs" v-on:modelChange="dataChange" :section="sec" />
    <span v-else>

      <span v-if="sec.forms">
        <v-card  v-bind:style="{'background': sec.background}" style="margin-bottom: 10px" v-for="formData in sec.forms" :key="formData.id" flat>
          <!-- <v-card-text v-text="formData.form.name"></v-card-text> -->

      
          <span v-if="formData.resizable">
          <splitpanes class="default-theme" dir="ltr">
            <pane v-bind:style="{'background': form.background}" v-for="(form,index) in formData.resizable.forms" :key="index">
              <span dir="rtl">
                <FormBuilder :forms="form" :model="form.model" />
              </span>
            </pane>
          </splitpanes>
          </span>
          
          <span v-if="sec.type != 'collapse'">

            <FormBuilder :forms="formData" :model="formData.model" />
          </span>
          <span
            v-if="sec.type == 'collapse'"
            style="position:relative;    width: 100%;
    display: block;"
          >
            <!-- <p style="position:absolute; z-index:999;">{{ sec.name }}</p> -->
            <!-- <span class="line"></span> -->
            <v-expansion-panels>
              <v-expansion-panel>
                <v-expansion-panel-header disable-icon-rotate>
                  <v-row no-gutters>
                    <v-col cols="4">
                      <span>{{ sec.name }}</span>
                      <span class="line"></span>
                    </v-col>
                    <v-col cols="8" class="text--secondary"> </v-col>
                  </v-row>
                  <template v-slot:actions>
<!--                    <v-icon color="error">-->
<!--                      mdi-arrow-collapse-down-->
<!--                    </v-icon>-->
                  </template>
                </v-expansion-panel-header>
                <v-expansion-panel-content>
                  <span v-for="formData in sec.forms" :key="formData.id">
                    <FormBuilder :forms="formData" :model="formData.model" />
                  </span>
                </v-expansion-panel-content>
              </v-expansion-panel>
            </v-expansion-panels>
            <!-- <FormBuilder :forms="formData" :model="formData.model" /> -->
          </span>
        </v-card>
      </span>
      <v-row v-if="sec.type == 'title'" style="height: 80px;">
        <v-col>
          <h1>
            <button
              style="background:transparent; width: 45px; height: 45px"
              @click="goBack"
            >
              <v-icon
                style="background-color: white;width: inherit; height: inherit;border-radius: 5px;"
                right
              >
                mdi-arrow-right
              </v-icon>
            </button>
            <span style="margin-left:15px;margin-right:15px">{{
              section.name
            }}</span>
          </h1>
        </v-col>
        <v-spacer></v-spacer>
        <v-col style="padding-top: 20px; padding-bottom:0" class="text-left">
          <span v-for="(action, index) in section.actions" :key="index">
            <span
              v-if="action == 'cancel'"
              @click="cancelStep"
              class="actions-contianer"
            >
              <v-icon color="error">
                far fa-times-circle
              </v-icon>
              إلغاء
            </span>
            <span
              v-if="action == 'save'"
              @click="saveStep"
              class="actions-contianer"
            >
              <v-icon color="info">
                far fa-save
              </v-icon>
              حفظ
            </span>
            <span
              v-if="action == 'complete'"
              @click="completeStep"
              class="actions-contianer"
            >
              <v-icon color="info">
                far fa-paper-plane
              </v-icon>
              إرسال
            </span>
          </span>
        </v-col>
      </v-row>
    </span>
  </v-container>
</template>

<script>
import TabBuilder from './tab-builder'
import FormBuilder from './form-builder'
import { Splitpanes, Pane } from 'splitpanes'
import 'splitpanes/dist/splitpanes.css'

export default {
  name: 'SectionBuilder',
  components: {
    TabBuilder,
    FormBuilder,
    Splitpanes,
    Pane
  },
  methods: {
    dataChange: function(model) {
      console.log('Section Builder')
      console.log(model)
      this.$emit('modelChange', model)
    },
    goBack: function() {
      this.$router.back()
    },
    completeStep: function() {
      this.$observable.fire('complete-step')
    },
    saveStep: function() {
      this.$observable.fire('save-step')
    },
    cancelStep: function() {
      this.$observable.fire('cancel-step')
    },
  },
  data() {
    return {
      sec: this.section,
    }
  },
  watch: {
    section: function(newVal) {
      // this.val = newVal
      this.sec = newVal
    },
  },
  props: ['section'],
}
</script>
<style>
.actions-contianer {
  border-radius: 5px;
  background: white;
  margin: 0 20px 0 0;
  padding: 10px 15px;
}
.v-expansion-panel__header {
  border: none;
}
.line {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 0.5em;
  border-top: 1px solid black;
  z-index: -1;
}
</style>
