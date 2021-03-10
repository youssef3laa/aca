<template>
  <div class="row top-bar top-bar-list">
    <div class="task-list-wrapper">
    <div class="task-list-heading">
        <div class="bolt-icon-wrapper">
          <i class="fas fa-bolt"></i>
        </div>
        <span>شريط المهام</span>
      </div>
      <div ref="taskList" id="task-list">
        <div  v-ripple v-for="(task, key) in val" :key="key" class="task" @click="handleTaskCallback(task)">
          <i class="far fa-edit"></i>
          <span class="task-name">{{ task.title }}</span>
        </div>
      </div>
    </div>

    <div class="scroller-wrapper">
      <v-btn @click="scroll('right','task-list')" class="scroller"> <i class="fas fa-chevron-right"></i> </v-btn>
      <v-btn @click="scroll('left','task-list')" class="scroller"> <i class="fas fa-chevron-left"></i> </v-btn>
    </div>
  </div>
</template>

<script>
export default {
  props: ["val", "field"],

  data() {
    return {
      taskList: [
        { title: "إنشاء وارد جديد" },
        { title: "تسجيل موضوع" },
        { title: "إنشاء تكليف" },
        { title: "شاشة التوجيهات" },
        { title: "شاشة التوجيهات" },
        { title: "شاشة التوجيهات" },
      ],
    };
  },
  methods: {
      handleTaskCallback: function (task) {

          // eslint-disable-next-line no-prototype-builtins
          if(task.hasOwnProperty("callback")) task.callback();

      },
      scroll(direction, content) {
          let scrollPixels = 180;
          if (direction == 'left') scrollPixels = -scrollPixels;

          console.log(scrollPixels);
          const element = document.getElementById(content);
          // element.animate({scrollLeft: '=-300'},1000);
          var scroll = scrollPixels / 10;

          var scrolled = 0;
          const interval = setInterval(() => {
              element.scrollLeft += scroll;
              scrolled += scroll;
              console.log(scrolled);
              console.log(scrollPixels);
              if (Math.round(scrolled) == Math.round(scrollPixels)) {
                  clearInterval(interval);
              }
          }, 20);
      },
  },
};
</script>

<style>
.task-list-heading {
  display: inline-flex;
  height: 100%;
  align-items: center;
  padding-left: 2% ;
  padding-right: 1%;
  border-left: 1px solid #00000012;
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
  min-width: 180px;
  justify-content: center;
  border-radius: 5px;
  color: #0278ae;
}
.task i {
  margin-left: 10px;
}

.task:hover {
    cursor: pointer;
}

.top-bar-list{
  align-items: center !important;
  justify-content: space-between;
  margin: 10px;
}
.task-list-wrapper {
  width: 70%;
  height: 100%;
}
#task-list {
  overflow-x: hidden;
  overflow-y: hidden;
  display: inline-flex;
  max-width: 65%;
}
.scroller {
  min-width: 34px !important;
  width: 34px !important;
  height: 34px !important;
  text-indent:0em !important;
  margin-left: 5px;
  background-color: transparent !important;
  border: 1px solid #07689f;
  color: #07689f !important;
  box-shadow: none !important;
}
.task-name {
  white-space: nowrap;
  overflow: hidden;
}
.scroller-wrapper{
  display: flex;
  height: 100%;
  align-items: center;
  padding-right: 2% ;
  padding-left:1%;
  border-right: 1px solid #00000012;
}
</style>
