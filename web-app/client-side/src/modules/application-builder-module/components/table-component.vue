<template>
  <v-container>
    <v-card>
      <v-data-table :headers="d.headers" :items="d.data" :search="d.search">
        <template v-slot:item.actions="{ item }">
          <v-icon small class="mr-2" @click="edit(item)">
            mdi-pencil
          </v-icon>
        </template>
      </v-data-table>
    </v-card>
  </v-container>
</template>

<script>
import router from '../../../router'

export default {
  name: 'TableComponent',
  data() {
    return {
      d: this.val,
    }
  },
  methods: {
    edit: function (item) {
      console.log("item obj in table", item)
      // console.log(item.TaskData.ApplicationData.inputSchemaFragment.entityId)
      // TaskData.ApplicationData.inputSchemaFragment
      let pageKey = item.TaskId
      console.log(pageKey);


      // console.log(item.TaskData.ApplicationData.inputSchemaFragment)
      // this.$route.params(item);
      // if (pageKey) router.push({ path: pageKey, props:{item:item}  })

      let taskUrl = item.TaskData.ApplicationData.inputSchemaFragment.taskUrl
      router.push({
        name: taskUrl,
        params: {taskId: pageKey, taskUrl: taskUrl}
      })

      // this.$observable.fire('BPItem', item)
    },
  },
  props: ['val', 'field'],
  watch: {
    val: function (newVal, oldVal) {
      console.log(oldVal)
      this.d = newVal
    },
  },
  created() {
    if (this.field.subscribe) {
      console.log('subscribe')
      this.$observable.subscribe(this.field.subscribe, (data) => {
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
