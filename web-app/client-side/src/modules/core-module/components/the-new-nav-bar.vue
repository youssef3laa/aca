<template>
  <div>
    <v-app-bar
      class="app-bar"
      height="100"
      flat
      :style="[$vuetify.rtl ? { direction: 'rtl' } : { direction: 'ltr' }]"
    >
      <template v-if="$vuetify.breakpoint.smAndUp">
        <v-app-bar-nav-icon v-if="drawer" class="mx-1" @click="drawer = !drawer"
          ><v-icon>mdi-close</v-icon></v-app-bar-nav-icon
        >
        <v-app-bar-nav-icon
          v-else
          class="mx-1"
          @click="drawer = !drawer"
        ></v-app-bar-nav-icon>
      </template>
      <v-spacer></v-spacer>
      <div style="width: 250px">
        <router-link :to="{ name: 'Home' }">
          <v-img
            contain
            width="250px"
            src="https://www.aca.gov.eg/style%20library/img/mainLogo.png"
          >
          </v-img>
        </router-link>
      </div>
      <v-spacer></v-spacer>
    </v-app-bar>
    <v-navigation-drawer
      style="margin-top: 6.2rem"
      absolute
      right
      v-model="drawer"
      app
    >
      <!-- <v-list-item>
        <v-list-item-content>
          <v-list-item-title class="bar-item">
            <div class="bar-icon-wrapper">
              <i class="far fa-edit"></i>
            </div>
            <span>{{ page }}</span>
          </v-list-item-title>
        </v-list-item-content>
      </v-list-item> -->
      <v-list dense>
        <v-list-item
          v-for="item in items"
          :key="item.title"
          v-on:click="item.action"
          link
          class="bar-item"
        >
          <v-list-item-title v-if="!item.options" class="bar-item-title">
            <div class="bar-icon-wrapper">
              <i :class="item.icon"></i>
            </div>
            <span>{{ item.title }}</span>
          </v-list-item-title>

          <v-list-group class="bar-group" v-else :value="true">
            <template v-slot:activator>
              <v-list-item-title class="bar-group-item-title"
                ><div class="bar-icon-wrapper">
                  <i :class="item.icon"></i>
                </div>
                <span>{{ item.title }}</span></v-list-item-title
              >
            </template>
            <v-divider style="margin-right: 8px; margin-left: 8px"></v-divider>
            <v-list-item
              v-for="(option, i) in item.options"
              @click="option.action"
              :key="i"
              link
            >
              <!-- <v-list-item-icon>
                <v-icon style="font-size:24px" v-text="option.icon"></v-icon>
              </v-list-item-icon> -->
              <v-list-item-title class="group-item-title">
                <i :class="option.icon"></i>
                <span>{{ option.title }}</span></v-list-item-title
              >
            </v-list-item>
            <!-- <v-list-group :value="true" no-action sub-group>
              <template v-slot:activator>
                <v-list-item-content>
                  <v-list-item-title>Admin</v-list-item-title>
                </v-list-item-content>
                
              </template>
            </v-list-group> -->
          </v-list-group>

          <!-- <div v-if="item.options" v-for="item" ></div> -->
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
import config from "../../../config/config";
import router from "../../../router";

export default {
  name: "TheNavbar",
  components: {},
  data() {
    return {
      page: "الصفحة الرئيسية",
      user: this.$user,
      title: config.APP_NAME,
      drawer: false,
      items: [
        { title: "الصفحة الرئيسية", icon: "fas fa-home", action: this.home },
        {
          title: "المهام",
          icon: "fas fa-bolt",
          action:this.about,
          options: [
            {
              title: "إنشاء وارد جديد",
              icon: "far fa-edit",
              action: this.process,
            },
            { title: "تسجيل موضوع", icon: "far fa-edit",action: this.about },
            { title: "إنشاء تكليف", icon: "far fa-edit",action: this.about },
            { title: "شاشة التوجيهات", icon: "far fa-edit",action: this.about },

          ],
        },
        { title: "التقارير", icon: "fas fa-chart-pie", action: this.about },
        { title: "Logout", icon: "fas fa-sign-out-alt", action: this.logout },
      ],

    };
  },
  methods: {
    home() {
      // router.push('home')
      router.push({ name: "HomePage" }).catch(function () {
        router.go();
      });
    },
    process() {
      // router.push('demo')
      router.push({ name: "generalProcess-init" }).catch(function () {
        router.go();
      });
    },
    about() {},
    logout() {
      localStorage.removeItem("user");
      router.push({ name: "LoginView" }).catch(function () {
        router.go();
      });
    },
  },
};
</script>
<style scoped>
.app-bar {
  background: white !important;
  z-index: 7 !important;
  border: 1px solid #70707033 !important;
}
.bar-item {
  width: 80%;
  margin: 14px;
  padding: 0px !important;
}
.bar-item-title {
  display: flex;
  justify-content: flex-start;
  align-items: center;
  background-color: rgba(7, 104, 159, 0.05);
  height: 40px;
    /* font-weight: bold !important; */

  border-radius: 5px;
  padding: 16px;
}
.bar-group-item-title {
    /* font-weight: bold !important; */

  display: flex;
  justify-content: flex-start;
  align-items: center;

  height: 40px;
}

.bar-group {


  background-color: rgba(7, 104, 159, 0.05) !important;
  /* margin: 10px;
  height: 40px;
  padding: 10px; */
  width: 100%;
  border-radius: 5px;
}
.bar-icon-wrapper {
  display: flex;
  color: white;
  width: 28px;
  height: 28px;
  justify-content: center;
  align-items: center;
  border-radius: 20%;
  background: #0278ae;
  border-radius: 5px;
  margin-left: 10px;
}
.group-item-title {
  margin-right: 12px;
  color: #0278ae;
}
.group-item-title i {
  margin-left: 8px;
}
</style>
