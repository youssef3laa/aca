<template>
  <v-row>
    <ActionsComponent :actions="section.actions"  ></ActionsComponent>
  </v-row>
</template>

<script>
    import ActionsComponent from '../../components/actions-component';
    export default {
      components: {
        ActionsComponent,
      },
      props: ['section'],
      data() {
        return {}
      },
      methods: {}
    }
</script>

<style></style>