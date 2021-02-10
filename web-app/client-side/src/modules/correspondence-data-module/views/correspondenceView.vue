<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app" />
  </v-container>
</template>

<script>
// import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "correspondenceView",
  components: {
    AppBuilder,
  },
  data() {
    return {
      app: {
        pages: [
          {
            tabs: [
              {
                key: "tab1",
                id: 1,
                name: "بيانات المكاتبة",
                isActive: true,
              },
            ],
            sections: [
              {
                key: "section 1",
                tabId: 1,
                isTab: true,
                type: "DefaultSection",
                display: "block",
                isCard: true,
                forms: [
                  {
                    key: "form1",
                    inputs: [
                      {
                        type: "InputComponent",
                        label: "رقم الوارد",
                        name: "number",
                        col: 5,
                        readonly: false,
                      },
                      {
                        type: "InputComponent",
                        label: "اسم الموضوع",
                        name: "name",
                        col: 5,
                        readonly: false,
                      },
                      {
                        type: "ButtonComponent",
                        label: "بحث الوارد",
                        action: "search",
                        publish:"searchIncoming",
                        name: "search",
                        col: 2,
                      },
                      {
                        type: "DataTableComponent",
                        name: "linkingTable",
                        actions: ["view"],
                        subscribe: "link",
                        searchable: false,
                        col: 12,
                      },
                      {
                        type: "DataTableComponent",
                        name: "linkedTable",
                        actions: ["view"],
                        // subscribe: "link",
                        searchable: false,
                        col: 12,
                      },
                    ],
                    model: {
                      number: "",
                      name: "",
                      search: "",
                      linkingTable: {
                        headers: [
                          {
                            text: "topicName",
                            align: "start",
                            value: "",
                          },
                          {
                            text: "senderName",
                            value: "",
                          },
                          {
                            text: "entityName",
                            value: "",
                          },
                          {
                            text: "processNumber",
                            value: "",
                          },

                          {
                            text: "date",
                            value: "",
                          },
                          {
                            text: "",
                            value: "action",
                          },
                        ],

                        data: [],
                        url: "",
                        search: "",
                      },
                      linkedTable: {
                        headers: [
                          {
                            text: "topicName",
                            align: "start",
                            value: "",
                          },
                          {
                            text: "senderName",
                            value: "",
                          },
                          {
                            text: "entityName",
                            value: "",
                          },
                          {
                            text: "processNumber",
                            value: "",
                          },

                          {
                            text: "date",
                            value: "",
                          },
                          {
                            text: "",
                            value: "action",
                          },
                        ],

                        data: [],
                        url: "",
                        search: "",
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
  
  mounted(){
    this.$observable.subscribe("searchIncoming",()=>{
      var data = this.$refs.appBuilder.getModelData("form1"); 
      console.log(data);
    })
  }
};
</script>

<style>
</style>