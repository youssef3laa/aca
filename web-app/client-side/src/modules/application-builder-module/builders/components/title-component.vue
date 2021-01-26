<template>
  <span>
    <v-row style="height: 80px;">
      <v-col>
        <h1>
          <button style="background:transparent; width: 45px; height: 45px"
                  @click="goBack">
            <v-icon style="background-color: white;width: inherit; height: inherit;border-radius: 5px;"
                    right>
              mdi-arrow-right
            </v-icon>
          </button>
          <span style="margin-left:15px;margin-right:15px">
            {{section.name}}
          </span>
        </h1>
      </v-col>
      <v-spacer></v-spacer>
      <ActionsComponent :actions="section.actions"></ActionsComponent>
    </v-row>
  </span>
</template>

<script>
import ActionsComponent from '../../components/actions-component';
export default {
  components: {ActionsComponent},
  props: ['section'],
  methods: {
    goBack: function() {
      this.$router.back()
    }
  },
  mounted() {
    console.log(this.section)
  },
}
</script>
