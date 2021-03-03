<template>
  <div :class="label !== undefined ? 'text-wrapper' : ''">
    <span class="label">{{ $t(field.label) }}</span>
    <br />
    <span>{{ val }}</span>
  </div>
</template>

<script>
export default {
  props: ["field", "val"],
  data() {
    return {
      label: this.field.label,
    };
  },
};
</script>

<style scoped>
.text-wrapper {
  border: 1px solid #e0e0e0;
  border-radius: 10px;
  padding: 16px;
}
.label {
  color: #0278ae;
  font-size: 18px;
  display: block;
  margin-bottom:6px;
}
</style>
