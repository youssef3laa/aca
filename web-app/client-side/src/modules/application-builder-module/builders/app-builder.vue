<template>
  <span>
    <TitleComponent v-if="appData && appData.pages && appData.pages.title" :section="appData.pages.title" />
    <TabBuilder
      v-if="appData && appData.pages && appData.pages.tabs"
      :tabkey="appData.pages.key"
      :tabs="handleTabs"
    />
    <span v-if="appData && appData.pages ">
      <div v-for="(page, key) in appData.pages.page" :key="key">
        <PageBuilder
          :page="page"
          v-on:modelChange="dataChange"
          :tabkey="appData.pages.key"
        />
      </div>
    </span>
  </span>
</template>

<script>
import PageBuilder from './page-builder'
import TitleComponent from './components/title-component'
import TabBuilder from './tab-builder'
export default {
  name: 'AppBuilder',
  components: { PageBuilder, TitleComponent, TabBuilder },
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

    findKey: function(key) {
      if(this.appData.pages.tabs){
        for (let i = 0; i < this.appData.pages.tabs.length; i++){
          if(this.appData.pages.tabs[i].key == key){
            this.positions[key] = [i]
            return
          }
        }
      }
      for (let i = 0; i < this.appData.pages.page.length; i++) {
        const page = this.appData.pages.page[i]
        if (page.key === key) {
          this.positions[key] = [i]
          return
        }
        if (page.sections) {
          for (let j = 0; j < page.sections.sec.length; j++) {
            const section = page.sections.sec[j]
            if (section.key === key) {
              this.positions[key] = [i, j]
              return
            }
            if (section.forms) {
              for (let k = 0; k < section.forms.length; k++) {
                const form = section.forms[k]
                // eslint-disable-next-line no-prototype-builtins
                if (!form.hasOwnProperty('resizable')) {
                  if (form.key === key) {
                    this.positions[key] = [i, j, k]
                    return
                  }
                } else {
                  let nestedForms = form.resizable.forms
                  for (let y = 0; y < nestedForms.length; ++y) {
                    const nestedFormElement = nestedForms[y]
                    if (nestedFormElement.key === key) {
                      this.positions[key] = [i, j, k, y]
                      return
                    }
                  }
                }
              }
            }
          }
        }
      }
    },

    getModelData: function(key) {
      if (!this.positions[key]) this.findKey(key)
      let form = this.appData.pages.page[this.positions[key][0]].sections.sec[
        this.positions[key][1]
      ].forms[this.positions[key][2]]
      // eslint-disable-next-line no-prototype-builtins
      if (form.hasOwnProperty('resizable'))
        return form.resizable.forms[this.positions[key][3]].model
      return form.model
    },
    setModelData: function(key, obj) {
      if (!this.positions[key]) this.findKey(key)
      // appData.pages.page
      console.log(this.appData.pages.page)
      let form = this.appData.pages.page[this.positions[key][0]].sections.sec[
        this.positions[key][1]
      ].forms[this.positions[key][2]]
      // eslint-disable-next-line no-prototype-builtins
      if (form.hasOwnProperty('resizable'))
        form = form.resizable.forms[this.positions[key][3]]
      for (let property in obj) {
        form.model[property] = obj[property]
      }
    },
    setFieldData: function(key, name, obj) {
      if (!this.positions[key]) this.findKey(key)
      let form = this.appData.pages.page[this.positions[key][0]].sections.sec[
        this.positions[key][1]
      ].forms[this.positions[key][2]]
      // eslint-disable-next-line no-prototype-builtins
      if (form.hasOwnProperty('resizable'))
        form = form.resizable.forms[this.positions[key][3]]

      let input = undefined
      for (let index in form.inputs) {
        if (form.inputs[index].name == name) {
          input = form.inputs[index]
          break
        }
      }
      if (!input) return
      for (let property in obj) {
        input[property] = obj[property]
      }
    },

    setShowTab: function(key, show){
      if (!this.positions[key]) this.findKey(key)
      this.appData.pages.tabs[this.positions[key][0]].show = show
    },

    appendForm: function(key, obj) {
      if (!this.positions[key]) this.findKey(key)
      this.appData.pages.page[this.positions[key][0]].sections.sec[
        this.positions[key][1]
      ].forms = [
        ...this.appData.pages.page[this.positions[key][0]].sections.sec[
          this.positions[key][1]
        ].forms,
        obj,
      ]
    },
    appendSection: function(key, obj) {
      if (!this.positions[key]) this.findKey(key)
      this.appData.pages.page[this.positions[key][0]].sections.sec.push(obj)
    },
    replacePage: function(key, obj) {
      if (!this.positions[key]) this.findKey(key)
      this.appData.pages.page[this.positions[key][0]] = obj
    },

    disableSection: function(key) {
      if (!this.positions[key]) this.findKey(key)
      var section = this.appData.pages.page[this.positions[key][0]].sections
        .sec[this.positions[key][1]]
      if (section.forms) {
        for (let k = 0; k < section.forms.length; k++) {
          const form = section.forms[k]
          // eslint-disable-next-line no-prototype-builtins
          if (!form.hasOwnProperty('resizable')) {
            for (let j = 0; j < form.inputs.length; j++) {
              form.inputs[j].readonly = true
            }
          } else {
            let nestedForms = form.resizable.forms
            for (let y = 0; y < nestedForms.length; ++y) {
              const nestedFormElement = nestedForms[y]
              for (let j = 0; j < nestedFormElement.inputs.length; j++) {
                nestedFormElement.inputs[j].readonly = true
              }
            }
          }
        }
      }
    },
    clearSectionForms: function(key) {
      if (!this.positions[key]) this.findKey(key);
      this.appData.pages.page[this.positions[key][0]].sections.sec[this.positions[key][1]].forms = [];
    },

    disableAllSections: function () {
      for (let i = 0; i < this.appData.pages.page.length; i++) {
        const page = this.appData.pages.page[i]
        if (page.sections) {
          for (let j = 0; j < page.sections.length; j++) {
            const section = page.sections.sec[j]
            this.disableSection(section.key)
          }
        }
      }
    },

    getFormKeyByPageKey: function(pageKey) {
      var result = []
      for (let i = 0; i < this.appData.pages.page.length; i++) {
        var page = this.appData.pages.page[i]
        if (page.key != pageKey) continue
        if (page.sections) {
          for (let j = 0; j < page.sections.sec.length; j++) {
            var section = page.sections.sec[j]

            if (section.forms) {
              for (let k = 0; k < section.forms.length; k++) {
                var form = section.forms[k]
                // eslint-disable-next-line no-prototype-builtins
                if (!form.hasOwnProperty('resizable')) {
                  result.push(form.key)
                } else {
                  let nestedForms = form.resizable.forms
                  for (let y = 0; y < nestedForms.length; ++y) {
                    const nestedFormElement = nestedForms[y]
                    result.push(nestedFormElement.key)
                  }
                }
              }
            }
          }
        }
      }
      return result
    },
  },
  computed: {
    handleTabs: function() {
      

      var x = this.appData.pages.tabs.filter((element)=>
        (typeof element.show =="undefined" || element.show  )
      )
      console.log(x);
      return x;
    }
  },
  props: ['app'],
}
</script>
