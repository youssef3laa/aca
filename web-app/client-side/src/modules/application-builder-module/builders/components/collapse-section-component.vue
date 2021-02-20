<template>
  <span class="collapse-section">
    <v-expansion-panels
      style="position:relative;    width: 100%;
    display: block;"
      v-model="panel"
      multiple
    >
      <v-expansion-panel>
        <v-expansion-panel-header>
          <v-row no-gutters>
            <v-col cols="4">
              <span>{{ $t(section.name) }}</span>
              <span class="line"></span>
            </v-col>
            <v-col cols="8" class="text--secondary"> </v-col>
          </v-row>
          <template v-slot:actions>
            <!--                    <v-icon color="error">-->
            <!--                      mdi-arrow-collapse-down-->
            <!--                    </v-icon>-->
          </template>
        </v-expansion-panel-header>
        <v-expansion-panel-content>
          <span v-for="formData in section.forms" :key="formData.id">
            <FormBuilder :forms="formData" :model="formData.model" />
          </span>
        </v-expansion-panel-content>
      </v-expansion-panel>
    </v-expansion-panels>
  </span>
</template>
<script>
import FormBuilder from '../form-builder'
export default {
  components: {
    FormBuilder,
  },
  data() {
    return {
      panel: [0],
    }
  },
  mounted() {
    console.log(this.formData)
  },
  props: ['formData', 'section'],
}
</script>
<style>
.v-application--is-rtl .v-expansion-panel-header__icon {
  border: 1px solid #0278ae !important;
  border-radius: 3px !important;
}
.theme--light.v-expansion-panels
  .v-expansion-panel-header
  .v-expansion-panel-header__icon
  .v-icon {
  color: #0278ae !important;
}
.collapse-section {
  display: block;
  padding: 30px 30px 10px 30px;
}
.v-expansion-panels {
  border: thin solid rgba(0, 0, 0, 0.12);
}
.v-expansion-panel--active > .v-expansion-panel-header {
  color: #0278ae !important;
  font-size: 18px;
}
.v-expansion-panel-content__wrap {
  padding: 0 !important;
}
</style>
