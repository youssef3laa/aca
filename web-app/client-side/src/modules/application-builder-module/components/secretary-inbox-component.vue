<template>
  <div style="width:100%">
    <Topbar :val="{ tasks: taskList }"></Topbar>
    <v-container>
      
          <AppBuilder ref="appBuilder" :app="app3" />
    </v-container>
  </div>
</template>

<script>
import Topbar from "../../application-builder-module/components/topbar-component";

export default {
  components: {
    AppBuilder: () => import("../builders/app-builder"),
    Topbar,
  },
  data() {
    return {

      app3:{
        pages: {
          key: "inboxPage",
          tabs: [
            {
              key: "outgoingTab",
              id: "1",
              isActive: "true",
              name: "incomingFromManagement",
              icon: "fas fa-feather-alt",
            },
            {
              key: "signaturesTab",
              id: "2",
              name: "signatures",
              icon: "fas fa-feather-alt",
            },
          ],
          page: [
            {
              key: "page1",
              sections: {
                tabs: [],
                sec: [
                  {
                    key: "section 2",
                    tabId: 2,
                    type: "DefaultSection",
                    display: "block",
                    isCard: true,
                    forms: [
                      {
                        key: "secretaryViewSignatures",
                        inputs: [
                          {
                            type: "SecretaryViewSignaturesComponent",
                            name: "secretaryViewSignatures",
                            col: 12,
                          },
                        ],

                      },
                    ],
                  },
                  {
                    key: "section 1",
                    tabId: 1,
                    isTab: true,
                    type: "DefaultSection",
                    display: "block",
                    isCard: true,
                    forms: [
                      {
                        key: "outcoming",
                        inputs: [
                          {
                            type: "InboxComponent",
                            name: "inbox",
                            col: 12,

                          },
                        ],
                    model:{
                      inbox:[{name:"الوارد",notifications:21, icon:"fas fa-download",action:"viewReceived"}]
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
      taskList: [{ title: "إنشاء وارد جديد" }, { title: "تسجيل موضوع" }],
    };
  },
};
</script>

<style scoped>
.no-padding {
  padding: 0px !important;
}
.padding-left {
  padding: 0px !important;
  padding-left: 1px !important;
  border-left: 1px solid #d1d1d1 !important;
}
</style>
