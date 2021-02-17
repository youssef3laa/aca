<template>
  <div class="row advanced-search-wrapper top-bar">
 
      <div class="search-icon-wrapper">
        <i class="fas fa-search"></i>
      </div>
      <span>{{$t('advancedSearch')}}</span>
    </div>

</template>

<script>
export default {
  props: ["val", "field"],


};
</script>

<style>

.search-icon-wrapper {
  display: flex;
  width: 28px;
  height: 28px;
  justify-content: center;
  align-items: center;
  border-radius: 20%;
  background: #22B07D;
  border-radius: 5px;
}
.search-icon-wrapper i {
  color: white;
}

.advanced-search-wrapper {
  display: flex;
  margin: 10px;
  padding: 10px;
  align-items: center !important;
  justify-content: space-around;
}
</style>