<template>
  <div style="display:flex;">
    <v-btn  @click="publish('addGoal')" class="title-btn" text>
      <i style="color:#07689F;" class="fas fa-plus"></i>
      <span>{{ $t("addAnnualGoal") }}</span>
    </v-btn>

    <div class="title-btn" >
      <i class="far fa-calendar-alt" style="display:flex; align-items:center; margin-right:10px"></i>
      <span style="display:flex; align-items:center">{{selectedItem}}</span>
        <div style="float:left; margin: 7px;" class="text-center">
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-icon v-bind="attrs" v-on="on" style="margin-right: 7px;">mdi-menu-down</v-icon>
            </template>
            <v-list style="padding: 1px;">
              <v-list-item v-for="item in items" :key="item" style="cursor:pointer; border-bottom: 1px solid #AAAAAA57; opacity: 1;">
                <v-list-item-title @click="selectedItem=item" style="font: normal 16px/20px Neo Sans Arabic; margin: 8px;">{{item}}</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
        </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["val", "field"],
  data() {
    return {
      items: ["2020", "2021"],
      selectedItem:"2020",
    };
  },
  methods:{
    publish(name){
      this.$observable.fire(name);
    }
  }
};
</script>

<style scoped>
.title-btn {
  border-radius: 8px !important;
  border: 1px solid #e0e0e0 !important;
  background-color: white !important;
  height: 44px !important;
  display: flex;
  margin-right: 14px;
}

.title-btn i {
  margin-left: 10px;
}

</style>
