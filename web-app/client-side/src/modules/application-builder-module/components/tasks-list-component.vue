<template>
  <div class="row task-list-wrapper">
    <div class="task-list-heading">
      <div class="bolt-icon-wrapper">
        <i class="fas fa-bolt"></i>
      </div>
      <span>شريط المهام</span>
    </div>
    <div class="task-list">
      <div v-for="(task, key) in taskList" :key="key" class="task">
        <i class="far fa-edit"></i>
        <span>{{ task.name }}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["val", "field"],

  data() {
    return {
      taskList: [
        { name: "إنشاء وارد جديد" },
        { name: "تسجيل موضوع" },
        { name: "إنشاء تكليف" },
        { name: "شاشة التوجيهات" },
      ],
    };
  },
};
</script>

<style>
.task-list-heading {
  display: flex;
  margin: 20px;
}
.bolt-icon-wrapper {
  display: flex;
  width: 28px;
  height: 28px;
  justify-content: center;
  align-items: center;
  border-radius: 20%;
  background: #0278ae;
  border-radius: 5px;
  margin-left: 10px;
}
.bolt-icon-wrapper i {
  color: white;
}
.task {
  display: flex;
  align-items: center;
  background-color: rgba(7, 104, 159, 0.05);
  margin: 10px;
  height: 40px;
  padding: 10px;
  border-radius: 5px;
  color: #0278ae;
}
.task i {
  margin-left: 10px;
}
.task-list-wrapper {
  height: 80px;
  background: white;
  width: 70%;
  margin: 10px;
  border-radius: 10px;
  align-items: center !important;
  justify-content: space-around;
}
.task-list {
  display: flex;
  width: 67%;
}
</style>