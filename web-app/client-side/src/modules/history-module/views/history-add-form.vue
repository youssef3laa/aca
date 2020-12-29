<template>
  <div>
      <v-form ref="form" class="mx-2"> 
     
            <v-col cols="11">
                 <v-text-field v-model="history.userName"  label="userName">
                </v-text-field>
            </v-col>
            <v-col cols="11">
                 <v-text-field v-model="history.roleName"  label="roleName">
                </v-text-field>
            </v-col>
            <v-col cols="11">
                 <v-text-field v-model="history.decision"  label="decision">
                </v-text-field>
            </v-col>
            <v-col cols="11">
                 <v-text-field v-model="history.comment"  label="comment">
                </v-text-field>
            </v-col>
            
      </v-form>
        
  </div>

</template>

<script>
import formPageMixin from '../../../mixins/formPageMixin'

export default {
    mixins:[formPageMixin],
    data(){
        return {
            history:{},
        }
    }
    

}
</script>

<style>

</style>