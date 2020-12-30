<template>
  <v-container>
    <AppBuilder ref="appBuilder" :app="app"/>
  </v-container>
</template>

<script>
import AppBuilder from "../../application-builder-module/builders/app-builder";

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
                          background: 'grey',
                          inputs: [
                            {
                              type: 'IframeComponent',
                              name: 'iframeObj',
                              col: 12,
                            },
                          ],
                          model: {
                            iframeObj: {
                              src: "http://appworks-dev/otcs/cs.exe?func=brava.bravaviewer&nodeid=586038&viewType=1&nexturl=%2Fotcs%2Fcs%2Eexe%3Ffunc%3Dll%26objid%3D577193%26objAction%3Dbrowse%26sort%3Dname%26viewType%3D1",
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
  },
  created() {
    this.$observable.subscribe('open-file-brava', (fileId) => {
      console.log(fileId);
      // this.$refs.appBuilder.setModelData('iframeObj', {
      //   iframeObj: {
      //     src: 'http://cs/otcs/cs.exe?func=ll&objid=577193&objAction=browse&sort=name&viewType=1',
      //   },
      // })
      this.$refs.appBuilder.getModelData('iframeObj')['iframeObj']['src'] =
          'http://cs/otcs/cs.exe?func=brava.bravaviewer&nodeid=' + fileId + '&OpenInNewWin=_blank&NewWinParam=resizable&viewType=1&nexturl=%2Fotcs%2Fcs%2Eexe%3Ffunc%3Dllworkspace';

    });
  }
};
</script>
