<template>
  <div class="row btn-wrapper top-bar">
 
      <div :style="{'background-color': val.backgroundColor}" class="btn-icon-wrapper">
        <i :class="field.icon"></i>
      </div>
      <span>{{$t(field.label)}}</span>
    </div>

</template>

<script>
export default {
  props: ["val", "field"],
  

};
</script>

<style>

.btn-icon-wrapper {
  display: flex;
  width: 28px;
  height: 28px;
  justify-content: center;
  align-items: center;
  border-radius: 20%;
  border-radius: 5px;
}
.btn-icon-wrapper i {
  color: white;
}

.btn-wrapper {
  display: flex;
  margin: 10px;
  padding: 10px;
  align-items: center !important;
  justify-content: space-around;
  cursor: pointer;
}
</style>