<template>
  <div style="display:flex;">
    <v-btn @click="publish('addGoal')" class="title-btn" text>
      <i style="color:#07689F;" class="fas fa-plus"></i>
      <span>{{ $t("addAnnualGoal") }}</span>
    </v-btn>

    <div class="title-btn">
      <i
        class="far fa-calendar-alt"
        style="display:flex; align-items:center; margin-right:10px"
      ></i>
      <span style="display:flex; align-items:center">{{ selectedItem }}</span>
      <div style="float:left; margin: 7px;" class="text-center">
        <v-menu offset-y>
          <template v-slot:activator="{ on, attrs }">
            <v-icon v-bind="attrs" v-on="on" style="margin-right: 7px;"
              >mdi-menu-down</v-icon
            >
          </template>
          <v-list style="padding: 1px;">
            <v-list-item
              v-for="item in items"
              :key="item"
              style="cursor:pointer; border-bottom: 1px solid #AAAAAA57; opacity: 1;"
               @click="selectItem('titleYearSelected',item)"
            >
              <v-list-item-title
                style="font: normal 16px/20px Neo Sans Arabic; margin: 8px;"
                >{{ item }}</v-list-item-title
              >
            </v-list-item>
          </v-list>
        </v-menu>
      </div>
    </div>
  </div>
</template>

<script>
import goalsMixin from "../../goals-module/mixin/goalsMixin";
export default {
  props: ["val", "field"],
  mixins: [goalsMixin],
  async mounted() {
    this.items = await this.getYears();
    this.selectedItem = this.items[0];
  },
  data() {
    return {
      items: [],
      selectedItem: "",
    };
  },
  methods: {
    selectItem(name,item){
      this.selectedItem = item;
      this.publish(name,item)
    },
    publish(name,data) {
      this.$observable.fire(name,data);
    },
  },
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
