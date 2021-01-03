<template>
  <div>
    <v-container>
      <app-builder ref="appBuilder" :app="app" />
    </v-container>
  </div>
</template>

<script>
import appBuilder from "../../application-builder-module/builders/app-builder";
import correspondenceMixin from "../mixin/correspondenceMixin";
import formPageMixin from "../../../mixins/formPageMixin";
export default {
  name: "correspondenceView",
  components: { appBuilder },
  mixins: [correspondenceMixin, formPageMixin],

  methods: {
    // fillForm() {

    //   this.$refs.appBuilder.setModelData("historyTable", {
    //     taskTable: {
    //       headers: [
    //         {
    //           text: "الاسم",
    //           align: "start",
    //           value: "userCN",
    //         },
    //         {
    //           text: "التاريخ",
    //           value: "approvalDate",
    //         },
    //       ],
    //       data: this.response,
    //       search: "",
    //     },
    //   });
    // },
  },

  data() {
    return {
      response: [],
     
   
             app: {
        pages: [
          {
            sections: [

              {
                tabs: [
                  {
                    id: 1,
                    name: 'مذكرة العرض',
                  },
                ],
                forms: [
                  {
                    key:"correspondence",
                    inputs: [
                      {
                        type: 'MemoComponent',
                        name: 'memp',
                        subscribe: 'tasks',
                        col: 12,
                      },
                      
                    ],
                    model: {
                      memo: {

                        // headers: [
                        //   {
                        //     text: 'الاسم',
                        //     align: 'start',
                        //     filterable: false,
                        //     value: 'userCN',
                        //   },
                        //   {
                        //     text: 'التاريخ',
                        //     value: 'approvalDate',
                        //   },

                        // ],
                        // data: [],
                        // search: '',
                      },
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
    };
  },
};
</script>

<style>
</style>