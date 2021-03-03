<template>
  <div class="pagination-wrapper">
    <div class="remaining-subjects">
      <span>{{ $t("remainingSubjects") }}</span
      >:<span style="color:#07689F;">11</span>
    </div>
    <div>
      <v-btn @click="rightClicked()" class="scroller">
        <i class="fas fa-chevron-right"></i>
      </v-btn>
      <v-btn @click="leftClicked()" class="scroller">
        <i class="fas fa-chevron-left"></i>
      </v-btn>
    </div>
    <div class="view-importance-wrapper">
      <v-checkbox v-model="d.importance" value="important" color="#07689F">
        <template v-slot:label>
          <span style="color: #1A1A2E;">
            {{ $t("importantAndImmediate") }}
          </span></template
        ></v-checkbox
      >
      <v-btn text class="viewed-btn">
        <i class="far fa-edit"></i>
        <span>{{ $t("doneViewing") }}</span>
      </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  props: ["val", "field"],
  data(){
    return{
      d : this.val
    }
  },
  methods: {
    rightClicked() {
      this.$$observable.fire("paginationRightClicked");
    },
    leftClicked() {
      this.$$observable.fire("paginationLeftClicked");
    },
  },
  watch:{
    val(newVal){
      this.d = newVal;
    }
  }
};
</script>

<style scoped>
.pagination-wrapper {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background-color: white;
  padding: 18px;
  border-top: 1px solid #e0e0e0;
}
.viewed-btn {
  display: flex;
  align-items: center;
  background-color: rgba(7, 104, 159, 0.05);
  margin: 10px;
  height: 40px;
  padding: 10px;
  min-width: 160px !important;
  justify-content: center;
  border-radius: 5px;
  color: #0278ae;
}
.viewed-btn i {
  margin-left: 10px;
}
.view-importance-wrapper {
  display: flex;
  align-items: center;
}
.remaining-subjects {
  font-weight: bold;
  font-size: 20px;
}
.remaining-subjects span {
  margin: 10px;
}
</style>
