<template>
  <v-container>
    <div v-for="(page, key) in appData.pages" :key="key">
      <PageBuilder v-on:modelChange="dataChange" :page="page" /></div
  ></v-container>
</template>

<script>
import PageBuilder from './page-builder'
export default {
  name: 'AppBuilder',
  components: { PageBuilder },
  data() {
    return {
      appData: this.app,
    }
  },
  methods: {
    dataChange: function(model) {
      console.log('App Builder')
      console.log(model)
    },

    setAppData: function(data) {
      this.appData = data
    },
    setModelData: function(key, obj) {
      // app - pages - sections : forms
      // app - pages - sections : tabs

      console.log("setModelData is called");

      // pages loop
      for (let i = 0; i < this.app.pages.length; i++) {
        const page = this.app.pages[i]
        console.log(page)
        if (page.sections) {
          for (let j = 0; j < page.sections.length; j++) {
            const section = page.sections[j]
            console.log(section)
            if (section.forms) {
              for (let k = 0; k < section.forms.length; k++) {
                const form = section.forms[k]
                console.log(form)
                if (form.key == key) {
                  for (let property in obj) {
                    console.log(property)
                    form.model[property] = obj[property]
                  }
                }
              }
            }
          }
        }
      }
      // for app.pages {
      //   let page = pages[i]
      // }

      console.log(key)
      console.log(obj)
    },
  },

  props: ['app'],
}
</script>
