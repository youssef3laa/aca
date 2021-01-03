<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"/>
  </v-container>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";
import Http from "http";
// import Http from "../../core-module/services/http"

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
            sections: [
              {
                numOfResizable: 2,
                key: 'section1',
                tabs: [

                  {
                    key: 'tab2',
                    id: 2,
                    name: 'المرفقات',
                    icon: 'fas fa-paperclip',
                  },
                ],
                forms: [
                  {
                    key: "form1",
                    resizable: {
                      forms: [
                        {
                          background: 'white',
                          inputs: [
                            {
                              type: 'InputFileComponent',
                              name: 'inputFile',
                              col: 12,
                            },
                          ],
                          model: {
                            inputFile: '',
                          },
                        },
                        {
                          key: 'iframeObj',
                          background: 'white',
                          inputs: [
                            {
                              type: 'IframeComponent',
                              name: 'iframeObj',
                              col: 12,
                            },
                          ],
                          model: {
                            iframeObj: {
                              src: "",
                            },
                          },
                        },
                      ],
                    },
                  },
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
    this.$observable.subscribe('open-file-brava', async (fileId) => {
      console.log(fileId);
      let userToken;
      try {
        userToken = await Http.post("http://appworks-dev:8080/otdsws/rest/authentication/credentials", {
          "userName": "admin",
          "password": "Asset99a",
          "ticketType": "OTDSTICKET"
        });
        console.log(userToken);
      } catch (e) {
        console.log(e);
      }
      this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
          'http://appworks-dev/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
      // this.$refs.appBuilder.appendForm("section2", {
      //   key: "form3",
      //   inputs: [
      //     {
      //       type: 'InputComponent',
      //       name: 'receiverEntityName',
      //       col: 4,
      //     },
      //   ],
      //   model: {
      //     receiverEntityName: '',
      //   },
      // })
      // this.$refs.appBuilder.appendSection("page1", {
      //   key: 'section3',
      //   tabs: [
      //     {
      //       key: 'tab3',
      //       id: 2,
      //       name: 'new tab',
      //       icon: 'fas fa-paperclip',
      //     }
      //
      //   ],
      //   forms: [
      //     {
      //       key: "form3",
      //       inputs: [
      //         {
      //           type: 'InputComponent',
      //           name: 'receiverEntityName',
      //           col: 4,
      //         },
      //       ],
      //       model: {
      //         inputFile: '',
      //       },
      //     }
      //   ],
      // },)

    });
  }, mounted() {
    //load list of files
    // this.$observable.fire('load-files-list', 577193);
  },
  updated() {
    console.log("updated is called")
  }
}
;
</script>
