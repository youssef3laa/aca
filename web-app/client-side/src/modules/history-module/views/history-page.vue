<template>
  <div>
    <v-container>
      <app-builder ref="appBuilder" :app="app" />
    </v-container>
  </div>
</template>

<script>
import appBuilder from "../../application-builder-module/builders/app-builder";
import historyMixin from "../mixin/historyMixin";
import formPageMixin from "../../../mixins/formPageMixin";
export default {
  name: "historyView",
  components: { appBuilder },
  mixins: [historyMixin, formPageMixin],
  async created() {
    this.response = await this.getHistoryByProcessNameAndEntityId(
      this.processName,
      this.entityId
    );
    console.log("responsyyy", this.response);
    this.loadForm("approval-history", this.fillForm);
    // this.fillForm();
  },

  methods: {
    fillForm() {
      this.$refs.appBuilder.setModelData("historyTable", {
        taskTable: {
          headers: [
            {
              text: "الاسم",
              align: "start",
              value: "userCN",
            },
            {
              text: "التاريخ",
              value: "approvalDate",
            },
          ],
          data: this.response,
          search: "",
        },
      });
    },
  },

  data() {
    return {
      response: [],
      processName: "test",
      entityId: "test",
      app: {},
      //        app: {
      //   pages: [
      //     {
      //       sections: [

      //         {
      //           tabs: [
      //             {
      //               id: 1,
      //               name: 'الاراء السابقة',
      //             },
      //           ],
      //           forms: [
      //             {
      //               key:"historyTable",
      //               inputs: [
      //                 {
      //                   type: 'TableComponent',
      //                   name: 'taskTable',
      //                   subscribe: 'tasks',
      //                   col: 12,
      //                 },
      //               ],
      //               model: {
      //                 taskTable: {
      //                   headers: [
      //                     {
      //                       text: 'الاسم',
      //                       align: 'start',
      //                       filterable: false,
      //                       value: 'userCN',
      //                     },
      //                     {
      //                       text: 'التاريخ',
      //                       value: 'approvalDate',
      //                     },

      //                   ],
      //                   data: [],
      //                   search: '',
      //                 },
      //               },
      //             },
      //           ],
      //         },
      //       ],
      //     },
      //   ],
      // },
    };
  },
};
</script>

<style>
</style>
