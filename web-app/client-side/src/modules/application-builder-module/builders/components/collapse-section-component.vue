<template>
  <span>
    <v-expansion-panels
      style="position:relative;    width: 100%;
    display: block;"
      v-model="panel"
      multiple>
      <v-expansion-panel>
        <v-expansion-panel-header>
          <v-row no-gutters>
            <v-col cols="4">
              <span>{{ section.name }}</span>
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
      panel: [0]
    }
  },
  mounted() {
      console.log(this.formData);
  },
  props: ['formData', 'section'],
}
</script>
