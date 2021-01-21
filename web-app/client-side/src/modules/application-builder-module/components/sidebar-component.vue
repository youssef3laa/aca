<template>
  <div class="sidebar-wrapper">
    <div class="sidebar-item">
      <span> <span> الصادر</span> 34</span>
      <i class="far fa-edit"></i>

    </div>
    <div class="sidebar-item">
      <span> <span> الوارد</span> 34</span>
      <i class="far fa-edit"></i>

    </div>
  </div>
</template>

<script>
export default {};
</script>

<style>
.sidebar-wrapper {
  display: flex;
  flex-direction: column;
  background: white;
height: 100%;

  align-items: center !important;
  justify-content: center;
}
.sidebar-item {
  display: flex;
  align-items: center;
  background-color: rgba(7, 104, 159, 0.05);
  margin: 10px;
  height: 40px;
  width: 80%;
  padding: 10px;
  justify-content: center;
  border-radius: 5px;
  color: #0278ae;
}

.sidebar-item i {
  margin-left: 10px;
}
</style>