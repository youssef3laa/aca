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
        var x = this.getHistoryByProcessNameAndEntityId(this.processName, this.entityId );
        console.log(x);
    },
    data () {
        return{
            processName:"",
            entityId:"",
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
                            value: 'userName',
                          },
                          {
                            text: 'التاريخ',
                            value: 'approvalDate',
                          },
                          
                        ],
                        data: [{
                            userName:"test",
                            approvalDate:"test"
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