<template>
  <span>
    <div v-for="(page, key) in appData.pages" :key="key">
      <PageBuilder :page="page" v-on:modelChange="dataChange"/>
    </div>
  </span>
</template>

<script>
import PageBuilder from './page-builder'

export default {
  name: 'AppBuilder',
  components: {PageBuilder},
  data() {
    return {
      appData: this.app,
      positions: {},
    }
  },
  methods: {
    dataChange: function (model) {
      console.log('App Builder')
      console.log(model)
    },

    setAppData: function (data) {
      this.appData = data
    },

    findKey: function (key) {
      for (let i = 0; i < this.appData.pages.length; i++) {
        const page = this.appData.pages[i]
        if (page.key === key) {
          this.positions[key] = [i];
          return;
        }
        if (page.sections) {
          for (let j = 0; j < page.sections.length; j++) {
            const section = page.sections[j]
            if (section.key === key) {
              this.positions[key] = [i, j];
              return;
            }
            if (section.forms) {
              for (let k = 0; k < section.forms.length; k++) {
                const form = section.forms[k]
                // eslint-disable-next-line no-prototype-builtins
                if (!form.hasOwnProperty("resizable")) {
                  if (form.key === key) {
                    this.positions[key] = [i, j, k]
                    return;
                  }
                } else {
                  let nestedForms = form.resizable.forms;
                  for (let y = 0; y < nestedForms.length; ++y) {
                    const nestedFormElement = nestedForms[y];
                    if (nestedFormElement.key === key) {
                      this.positions[key] = [i, j, k, y]
                      return;
                    }
                  }
                }

              }
            }
          }
        }
      }
    },

    getModelData: function (key) {
      if (!this.positions[key]) this.findKey(key)
      let form = this.appData.pages[this.positions[key][0]].sections[
          this.positions[key][1]
          ].forms[this.positions[key][2]];
      // eslint-disable-next-line no-prototype-builtins
      if (form.hasOwnProperty('resizable'))
        return form.resizable.forms[this.positions[key][3]].model
      return form.model
    },
    setModelData: function (key, obj) {
      if (!this.positions[key]) this.findKey(key)
      let form = this.appData.pages[this.positions[key][0]].sections[
          this.positions[key][1]
          ].forms[this.positions[key][2]]
      // eslint-disable-next-line no-prototype-builtins
      if (form.hasOwnProperty('resizable'))
        form = form.resizable.forms[this.positions[key][3]]
      for (let property in obj) {
        form.model[property] = obj[property]
      }
    },
    appendForm: function (key, obj) {
      if (!this.positions[key]) this.findKey(key);
      this.appData.pages[this.positions[key][0]].sections[
          this.positions[key][1]].forms = [...this.appData.pages[this.positions[key][0]].sections[
          this.positions[key][1]].forms, obj];
    },
    appendSection: function (key, obj) {
      if (!this.positions[key]) this.findKey(key);
      this.appData.pages[this.positions[key][0]].sections.push(obj);
    }
  },

  props: ['app'],
}
</script>
