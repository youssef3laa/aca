<template>
  <div>
    <v-app-bar
      class="app-bar"
      height="80"
      flat
      fixed
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
      <v-spacer></v-spacer>
      <div style="width: 250px">
        <router-link :to="{ name: 'Home' }">
          <v-img
            contain
            width="250px"
            alt="ACA logo"
            src="../../../assets/mainLogo.png"
          >
          </v-img>
        </router-link>
      </div>
      <v-spacer></v-spacer>
      <div>
        <v-badge :content="messages" :value="messages" color="red" overlap>
          <v-icon
            style="float:left; margin: 7px;"
            v-on:click="goToNotifications"
            >mdi-bell-outline</v-icon
          >
        </v-badge>
        <p style="float:left; margin: 7px;">EN</p>
      </div>
      <div
        style="float:left; margin: 7px; border-left: 2px solid #F1F2F3; height: 42px; opacity: 1;"
      ></div>
      <div>
        <v-avatar size="36px" style=" margin:7px;">
          <img
            src="https://image.flaticon.com/icons/png/512/17/17004.png"
            alt="John"
          />
        </v-avatar>
        <div style="float:left; margin: 7px;" class="text-center">
          <v-menu offset-y>
            <template v-slot:activator="{ on, attrs }">
              <v-icon v-bind="attrs" v-on="on" style="margin: 7px;"
                >mdi-menu-down</v-icon
              >
            </template>
            <v-list style="padding: 1px;">
              <v-list-item
                style="border-bottom: 1px solid #AAAAAA57; opacity: 1;"
              >
                <v-icon style="width: 12px; height: 12px;"
                  >mdi-account-box</v-icon
                >
                <v-list-item-title
                  style="font: normal 16px/20px Neo Sans Arabic; margin: 8px;"
                  >الملف الشخصي</v-list-item-title
                >
              </v-list-item>
              <v-list-item>
                <v-icon style="width: 12px; height: 12px;"
                  >mdi-exit-to-app</v-icon
                >
                <v-list-item-title
                  style="font: normal 16px/20px Neo Sans Arabic; margin: 8px;"
                  >خروج</v-list-item-title
                >
              </v-list-item>
            </v-list>
          </v-menu>
        </div>
        <p style="float:left; margin: 4px;">
          <span style="font: normal 18px/22px Neo Sans Arabic; opacity: 1;">{{
            user.details.displayName
          }}</span>
          <br />
          <span
            style="font: normal 12px/15px Neo Sans Arabic; color: #9E9E9E; opacity: 1;"
          >
            إدارة المعلومات</span
          >
        </p>
      </div>
    </v-app-bar>
    <v-navigation-drawer style="margin-top: 80px" right v-model="drawer" app>
      <v-list dense>
        <v-list-item
          v-for="item in items"
          :key="item.title"
          link
          class="bar-item"
        >
          <v-list-item-title
            v-if="!item.options"
            v-on:click="routeTo(item.route)"
            class="bar-item-title"
          >
            <div class="bar-icon-wrapper">
              <i :class="item.icon"></i>
            </div>
            <span>{{ $t(item.title) }}</span>
          </v-list-item-title>

          <v-list-group class="bar-group" v-else :value="true">
            <template v-slot:activator>
              <v-list-item-title class="bar-group-item-title"
                ><div class="bar-icon-wrapper">
                  <i :class="item.icon"></i>
                </div>
                <span>{{ $t(item.title) }}</span></v-list-item-title
              >
            </template>
            <v-divider style="margin-right: 8px; margin-left: 8px"></v-divider>
            <v-list-item
              v-for="(option, i) in item.options"
              @click="routeTo(option.route)"
              :key="i"
              link
            >
              <v-list-item-title class="group-item-title">
                <i :class="option.icon"></i>
                <span>{{ $t(option.title) }}</span></v-list-item-title
              >
            </v-list-item>
          </v-list-group>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
import config from '../../../config/config'
import router from '../../../router'

export default {
  name: 'TheNavbar',
  components: {},
  data() {
    return {
      // page: "الصفحة الرئيسية",
      messages: 0,
      user: this.$user,
      title: config.APP_NAME,
      drawer: false,
      items: [
        { title: 'homePage', icon: 'fas fa-home', route: 'HomePage' },
        {
          title: 'tasks',
          icon: 'fas fa-bolt',
          route: '',
          options: [
            {
              title: 'createNewImport',
              icon: 'far fa-edit',
              route: 'incomingRegistration-init',
            },
            { title: 'registerTopic', icon: 'far fa-edit', route: '' },
            { title: 'createAssignment', icon: 'far fa-edit', route: '' },
            {
              title: 'directionsScreen',
              icon: 'far fa-edit',
              route: '',
            },
          ],
        },
        { title: 'reports', icon: 'fas fa-chart-pie', route: '' },
        { title: 'logout', icon: 'fas fa-sign-out-alt', route: 'LoginView' },
      ],
    }
  },
  methods: {
    routeTo(name) {
      if (name == 'LoginView') {
        this.drawer = false
        localStorage.removeItem('user')
      }

      router.push({ name: name }).catch(function() {
        router.go()
      })
    },
    goToNotifications: function() {
      this.$router.push('/notification')
    },
  },
  mounted() {
    this.$observable.subscribe('notificationCounter', (counter) => {
      this.messages = counter
    })
  },
}
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
