<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"/>
  </v-container>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";
// import Http from "http";
import Http from "../../core-module/services/http"

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
      this.$observable.fire('file-component-skeleton', true)

      console.log(fileId);
      let userToken;
      try {
        userToken = await Http.post("http://45.240.63.94:8081/otdsws/rest/authentication/credentials", {
          "userName": "admin",
          "password": "Asset99a",
          "ticketType": "OTDSTICKET"
        });
        this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
            'http://45.240.63.94/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&viewType=1&OTDSTicket=' + userToken.data.ticket;
        console.log(userToken);
        this.$observable.fire('file-component-skeleton', false)

      } catch (e) {
        console.log(e);
      }


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
