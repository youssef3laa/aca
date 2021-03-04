<template>
  <span>
    <v-row style="padding-right:12px;padding-left:12px;height: 80px;">
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
            {{$t(section.name)}}
          </span>
        </h1>
      </v-col>

      <ActionsComponent :actions="section.actions"></ActionsComponent>
      <div style="display:flex">
      <TitleBtnComponent v-if="section.goalButton"></TitleBtnComponent>
      </div>
    </v-row>
  </span>
</template>

<script>
import ActionsComponent from '../../components/actions-component';
import TitleBtnComponent from '../../components/title-goals-btn-component';

export default {
  components: {ActionsComponent,TitleBtnComponent},
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
