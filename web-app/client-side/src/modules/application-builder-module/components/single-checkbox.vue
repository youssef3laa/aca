<template>
      <v-checkbox
      v-model="isSelected"
      style="color:#C70039"
      :label="label"
        color="#C70039"
    ></v-checkbox>
</template>

<script>
export default {
props:['field',"val"],
data(){
    return{
        isSelected: false
}
},
computed:{
    label(){
        return this.$t(this.field.label)
    }
},
watch:{
    isSelected(newValue){
      this.$emit('update', {
        name: this.field.name,
        value: newValue,
        type: 'inputChange',
      })

    }
}
}
</script>

<style>

</style>