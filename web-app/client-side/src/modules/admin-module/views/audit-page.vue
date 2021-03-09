<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"></AppBuilder>
  </v-container>
</template>

<script>
import formPageMixin from "../../../mixins/formPageMixin";
import AppBuilder from "../../application-builder-module/builders/app-builder";

export default {
  name: "AuditPage",
  components: {
    AppBuilder,
  },
  mixins: [formPageMixin],
  data() {
    return {
      app: {
        pages: {
          key: "auditTablePage",
          page: [
            {
              key: "page1",
              sections: {
                sec: [
                  {
                    key: "section 1",
                    type: "DefaultSection",
                    display: "block",
                    isCard: true,
                    forms: [
                      {
                        key: "auditForm",
                        inputs: [
                          {
                            type: "DataTableComponent",
                            name: "auditTable",
                            add: false,
                            filter: false,
                            search: true,
                            select: false,
                            actions: ["view"],
                            subscribe: "secretarySignatures",
                            col: 12,
                          },
                        ],
                        model: {
                          outboxTable: {
                            url: "history/user",
                            key: "id",
                            headers: [
                              {
                                text: "userCN",
                                align: "start",
                                value: "userCN",
                              },
                              {
                                text: "date",
                                value: "timestamp",
                              },
                              {
                                text: "action",
                                value: "",
                              },
                              {
                                text: "reponseCode",
                                value: "",
                              },
                            ],
                            data: [],
                            search: "",
                          },
                        },
                      },
                    ],
                  },
                ],
              },
            },
          ],
        },
      },
    };
  },
  mounted() {
 //   this.loadForm("audit-page");
   this.loadForm("COC-outbox")
  },
};
</script>

<style scoped></style>
