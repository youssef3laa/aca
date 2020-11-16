<template>
  <v-container>
    <v-card>
        <v-data-table
            :headers="d.headers"
            :items="d.data"
            :search="d.search">
            <template v-slot:item.actions="{ item }">
              <v-icon
                small
                class="mr-2"
                @click="edit(item)"
              >
                mdi-pencil
              </v-icon>
            </template>
        </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
export default {
  name: "TableComponent",
  data () {
    return {
      d: this.val
    }
  },
  methods: {
    edit: function(item){
      console.log(item)
    }
  },
  props: ['val', 'field'],
  watch: {
    val: function(newVal, oldVal){
      console.log(oldVal);
      this.d = newVal;
    }
  },
  created() {
    if (this.field.subscribe) {
      console.log('subscribe')
      this.$observable.subscribe(this.field.subscribe, (data)=> {
        if (data.type == 'modelUpdate') {
          var keys = Object.keys(data.model)
          keys.forEach((key, index) => {
            console.log(index)
            this.d[key] = data.model[key]
          })
        }
        console.log(data)
      })
    }
  },
}
</script>