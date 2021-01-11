<template>
  <div>
    <v-app-bar
      style="background : white"
      height="100"
      flat
      :style="[$vuetify.rtl ? { direction: 'rtl' } : { direction: 'ltr' }]"
    >
      <template v-if="$vuetify.breakpoint.smAndUp">
        <v-app-bar-nav-icon
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

      <template v-if="$vuetify.breakpoint.smAndUp">
        <!-- <v-toolbar-items>
          <v-btn text>Link 1</v-btn>
        </v-toolbar-items>
        <v-btn icon>
          <v-icon>mdi-export-variant</v-icon>
        </v-btn>
        <v-btn icon>
          <v-icon>mdi-delete-circle</v-icon>
        </v-btn>
        <v-btn icon>
          <v-icon>mdi-plus-circle</v-icon>
        </v-btn> -->
      </template>
    </v-app-bar>
    <v-navigation-drawer
      v-model="drawer"
      absolute
      :right="$vuetify.rtl"
      temporary
    >
      <v-list-item>
        <!-- <v-list-item-avatar>
					<v-img src="https://randomuser.me/api/portraits/men/78.jpg"></v-img>
				</v-list-item-avatar> -->
        <v-list-item-content>
          <v-list-item-title>{{ user.username }}</v-list-item-title>
        </v-list-item-content>
      </v-list-item>
      <v-divider></v-divider>
      <v-list dense>
        <v-list-item
          v-for="item in items"
          :key="item.title"
          v-on:click="item.action"
          link
        >
          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
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
      user: this.$user,
      title: config.APP_NAME,
      drawer: false,
      items: [
        { title: 'Home', icon: 'dashboard', action: this.home },
        { title: 'Demo', icon: 'dashboard', action: this.demo },
        { title: 'About', icon: 'question_answer', action: this.about },
        { title: 'Logout', icon: 'question_answer', action: this.logout },
      ],
    }
  },
  methods: {
    home() {
      router.push('home')
    },
    demo() {
      router.push('demo')
    },
    about() {},
    logout() {
      localStorage.removeItem('user')
      router.push({name: 'LoginView' })
    },
  },
}
</script>
