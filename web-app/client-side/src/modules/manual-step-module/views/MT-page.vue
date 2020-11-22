<template>
  <h1>
    Hello Ezzat
  </h1>
</template>

<script>
export default {
  name: 'MT',
  props: ['item'],
  computed: {
    task() {
      const taskId = this.$route.params.taskId
      return this.tasks[taskId]
    },
  },
  mounted() {
    const item = this.$route.params.item
    console.log(item)
    // this.$observable.subscribe('BPItem', function(response) {
    //   console.log(response)
    // })
    // console.log(this)
    // console.log(this.item)
  },
  created() {
    // this.$observable.subscribe('BPItem', function(response) {
    //   console.log(response)
    // })
  },
  data() {
    return {
      tasks: [],
    }
  },
}
</script>
