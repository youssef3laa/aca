<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"/>
  </v-container>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";
// import Http from "http";
import http from "../../core-module/services/http";

export default {
  name: "attachment",
  components: {
    AppBuilder,
  },
  data() {
    return {
      app: {
        pages: [
          {
            key: "page1",
            tabs: [
              {
                "key": "tab1",
                "id": "1",
                "isActive": true,
                "name": "المرفقات",
                "icon": "far fa-file-alt"
              },
            ],
            sections: [
              {
                "key": "title",
                "type": "TitleComponet",
                "name": "بيانات المكاتبة",
                "actions": [
                  "cancel",
                  "complete"
                ]
              },
              {
                "key": "section1",
                "tabId": "1",
                "isTab": true,
                "isCard": true,
                "type": "DefaultSection",
                "display": "block",
                "forms": [
                  {
                    "key": "AttachmentComponent",
                    "inputs": [
                      {
                        "type": "AttachmentComponent",
                        "name": "attachmentComponent",
                        "col": 12
                      }
                    ],
                    "model": {}

                    // "resizable": {
                    //   "forms": [
                    //     {
                    //       "key": "iframeObj",
                    //       "background": "white",
                    //       "inputs": [
                    //         {
                    //           "type": "IframeComponent",
                    //           "name": "iframeObj",
                    //           "col": 12
                    //         }
                    //       ],
                    //       "model": {
                    //         "iframeObj": {
                    //           "src": ""
                    //         }
                    //       }
                    //     },
                    //     {
                    //       "background": "white",
                    //       "inputs": [
                    //         {
                    //           "type": "InputFileComponent",
                    //           "name": "inputFile",
                    //           "col": 12
                    //         }
                    //       ],
                    //       "model": {
                    //         "inputFile": ""
                    //       }
                    //     }
                    //   ]
                    // },
                  }
                ],
              },
            ],
          },
        ],
      },
    };
  }
  ,
  created() {
    this.$observable.subscribe('open-file-brava', async ({fileId, verNum}) => {
      this.$observable.fire('file-component-skeleton', true)
      let userToken;
      try {
        userToken = await http.post("http://45.240.63.94:8081/otdsws/rest/authentication/credentials", {
          "userName": "admin",
          "password": "Asset99a",
          "ticketType": "OTDSTICKET"
        });
        if (verNum) {
          this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
              'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&vernum=' + verNum + '&OTDSTicket=' + userToken.data.ticket;
        } else {
          this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
              'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
        }

      } catch (e) {
        console.log(e);
      }
    });
  }, mounted() {

    this.$observable.subscribe("openVersionsModal", (file) => {
      this.$observable.fire("versionModal");
      this.$refs.appBuilder.setModelData("fileVersionsModal", {
        versionGrid: {
          nodeId: file.properties.id
        }
      });
    })

  }
}

</script>
