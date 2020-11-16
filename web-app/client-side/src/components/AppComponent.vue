<template>
  <v-container>
    <div v-for="(page, key) in app.pages" :key="key">
      <pageBuilder :page="page" /></div
  ></v-container>
</template>

<script>
import pageBuilder from './PageComponent'
export default {
  name: 'appBuilder',
  components: { pageBuilder },
  data() {
    return {
      app: {
        pages: [
          {
            sections: [
              {
                tabs: [
                  {
                    id: 1,
                    name: 'tab 1',
                  },
                  {
                    id: 2,
                    name: 'tab 2',
                  },
                ],
                forms: [
                  {
                    form: [
                      {
                        id: 'x',
                        type: 'inputText',
                        label: 'First Name',
                        name: 'input',
                        col: 6,
                        rule: 'required|minmax:2,25',
                        //  readonly : true
                      },
                      {
                        id: 'y',
                        type: 'inputText',
                        label: 'First Name',
                        name: 'input',
                        eventName: 'input',
                        method: 'updateText',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                        {
                        id: 'y',
                        type: 'buttonComponent',
                        label: 'First Name',
                        name: 'input',
                        method: 'updateText',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                    ],
                    model: {
                      x: 'null',
                      y: 'test',
                    },
                  },
                  {
                    form: [
                      {
                        id: 'x',
                        type: 'inputText',
                        label: 'First Name',
                        name: 'input',
                        col: 6,
                        rule: 'required|minmax:2,25',
                        //  readonly : true
                      },
                      {
                        id: 'y',
                        type: 'inputText',
                        label: 'First Name',
                        name: 'input',
                        method: 'updateText',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                    ],
                    model: {
                      x: 'null',
                      y: 'test',
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
    }
  },
}
</script>
