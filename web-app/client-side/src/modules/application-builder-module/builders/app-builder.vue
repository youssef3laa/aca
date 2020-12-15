<template>
  <span>
    <div v-for="(page, key) in appData.pages" :key="key">
      <PageBuilder v-on:modelChange="dataChange" :page="page" />
    </div>
  </span>
</template>

<script>
import PageBuilder from './page-builder'

export default {
  name: 'AppBuilder',
  components: { PageBuilder },
  data() {
    return {
      appData: this.app,
      positions: {},
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

    findModelData: function(key) {
      for (let i = 0; i < this.appData.pages.length; i++) {
        const page = this.appData.pages[i]
        console.log(page)
        if (page.sections) {
          for (let j = 0; j < page.sections.length; j++) {
            const section = page.sections[j]
            if (section.forms) {
              for (let k = 0; k < section.forms.length; k++) {
                const form = section.forms[k]
                if (form.key === key) {
                  this.positions[key] = [i, j, k]
                }
              }
            }
          }
        }
      }
    },

    getModelData: function(key) {
      if (!this.positions[key]) this.findModelData(key)
      return this.appData.pages[this.positions[key][0]].sections[
        this.positions[key][1]
      ].forms[this.positions[key][2]].model
    },
    setModelData: function(key, obj) {
      if (!this.positions[key]) this.findModelData(key)
      var form = this.appData.pages[this.positions[key][0]].sections[
        this.positions[key][1]
      ].forms[this.positions[key][2]]
      for (let property in obj) {
        form.model[property] = obj[property]
      }
    },
  },

  props: ['app'],
}
</script>
