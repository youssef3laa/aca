<template>
  <div class="row org-chart-wrapper top-bar">
 
      <div class="org-icon-wrapper">
        <i class="fas fa-sitemap"></i>
      </div>
      <span>الهيكلي<br>المخطط</span>
    </div>

</template>

<script>
export default {
  props: ["val", "field"],


};
</script>

<style>

.org-icon-wrapper {
  display: flex;
  width: 28px;
  height: 28px;
  justify-content: center;
  align-items: center;
  border-radius: 20%;
  background: #F37121;
  border-radius: 5px;
}
.org-icon-wrapper i {
  color: white;
}

.org-chart-wrapper {
  display: flex;

  margin: 10px;
  padding: 10px;

  align-items: center !important;
  justify-content: space-around;
}
</style>