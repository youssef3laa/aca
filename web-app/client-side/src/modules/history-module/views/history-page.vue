<template>

    <div>
        <v-container>
            <app-builder :app="app" />
        </v-container>
    </div>
  
</template>

<script>
import appBuilder from '../../application-builder-module/builders/app-builder.vue'
import formPageMixin from '../../../mixins/formPageMixin'
export default {
    name: 'historyView',
    components: { appBuilder },
    mixins: [formPageMixin],
    created(){
        this.getHistoryByPid(this.processID);
    },
    data () {
        return{
            processID:111,
             app: {
        pages: [
          {
            sections: [
            
              {
                tabs: [
                  {
                    id: 1,
                    name: 'الاراء السابقة',
                  },
                ],
                forms: [
                  {
                    inputs: [
                      {
                        type: 'TableComponent',
                        name: 'taskTable',
                        subscribe: 'tasks',
                        col: 12,
                      },
                    ],
                    model: {
                      taskTable: {
                        headers: [
                          {
                            text: 'الاسم',
                            align: 'start',
                            filterable: false,
                            value: 'name',
                          },
                          {
                            text: 'التاريخ',
                            value: 'date',
                          },
                          
                        ],
                        data: [{
                            name:"test",
                            date:"test"
                        }],
                        search: '',
                      },
                    },
                  },
                ],
              },
            ],
          },
        ],
      },
    

        }},

    methods:{}
    
}
</script>

<style>

</style>