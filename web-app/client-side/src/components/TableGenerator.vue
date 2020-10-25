<template>
    <div id="app">
  <v-app id="inspire">
    <v-data-table
      :headers="data.headers"
      :items="data.desserts"
      class="elevation-1"
    >
    <template v-slot:item.calories="{ item }">
        <v-chip
          :color="getColor(item.calories)"
          dark
        >
          {{ item.calories }}
        </v-chip>
      </template>
      
    </v-data-table>
  </v-app>
</div>
</template>
<script>
    export default {
        name: 'TableGenerator',
        props: ['data'],
  methods: {
    getColor (calories) {
      if (calories > 400) return 'red'
      else if (calories > 200) return 'orange'
      else return 'green'
    },
  }

    }
</script>