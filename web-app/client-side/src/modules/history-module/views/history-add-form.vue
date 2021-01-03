<template>
  <div>
      <v-form ref="form" class="mx-2"> 
     
            <v-col cols="11">
                 <v-text-field v-model="history.decision"  label="decision">
                </v-text-field>
            </v-col>
            <v-col cols="11">
                 <v-text-field v-model="history.comment"  label="comment">
                </v-text-field>
            </v-col>
            <v-col cols="11">
                 <v-text-field v-model="history.userCN"  label="userCN">
                </v-text-field>
            </v-col>
            <v-col cols="11">
                 <v-text-field v-model="history.entityId"  label="entityId">
                </v-text-field>
            </v-col>
            <v-col cols="11">
                 <v-text-field v-model="history.processName"  label="processName">
                </v-text-field>
            </v-col>
            <v-col cols="11">
                 <v-text-field v-model="history.stepId"  label="stepId">
                </v-text-field>
            </v-col>            
            <v-btn  v-on:click="mapping()" color="info">Submit</v-btn>
                
            
      </v-form>
        
  </div>

</template>

<script>
import historyMixin from '../mixin/historyMixin'

export default {
    mixins:[historyMixin],
    data(){
        return {
            history:{},
        }
    },
    
methods:{
    mapping()
    {
        historyMixin.methods.setHistory(JSON.stringify(this.history))
    }
}
}

</script>

<style>

</style>