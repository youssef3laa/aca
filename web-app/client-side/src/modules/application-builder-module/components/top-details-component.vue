<template>
  <div class="details-wrapper">
    <div>
      <span>title</span>
      <br/>
      <span>value</span>
    </div>
  </div>
</template>

<script>
export default {
  props: ["val", "field"],
};
</script>

<style scoped>
.details-wrapper {
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  padding: 16px;
  display: flex;
}
</style>
