<template>
  <v-container>
    <div v-for="(page, key) in app.pages" :key="key">
      <pageBuilder v-on:modelChange="dataChange" :page="page" /></div
  ></v-container>
</template>

<script>
import pageBuilder from './page-builder'
export default {
  name: 'app-builder',
  components: { pageBuilder },
  methods: {
    dataChange: function(model) {
      console.log('App Builder')
      console.log(model)
    },
  },
  mounted() {
    this.$observable.subscribe('input', function(data){
      console.log(data)
    });
    this.$observable.subscribe('submit', function(data){
      console.log(data)
    });
  },
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
                    name: 'بيانات السياسة',
                  },
                ],
                forms: [
                  {
                    form: [
                      {
                        type: 'inputComponent',
                        label: 'رقم التسلسل',
                        name: 'serial_num',
                        eventName: 'input',
                        col: 4,
                        rule: 'required|minmax:2,25',
                        //  readonly : true
                      },
                      {
                        type: 'inputComponent',
                        label: 'اسم السياسة',
                        name: 'policyName',
                        eventName: 'input',
                        method: 'updateText',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                      {
                        type: 'inputComponent',
                        label: 'تاريخ التوجيه',
                        name: 'policyDate',
                        eventName: 'input',
                        method: 'updateText',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                      {
                        type: 'buttonComponent',
                        label: 'submit',
                        name: 'submitBtn',
                        eventName: 'submit',
                        method: 'updateText',
                        col: 4,
                        rule: 'required|minmax:2,25',
                      },
                    ],
                    model: {
                      serial_num: '',
                      policyName: '',
                      policyDate: '',
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
