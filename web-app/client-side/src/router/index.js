import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import LoginRoutes from '../modules/login-module/router/router'
import AdminRoutes from '../modules/admin-module/router/router'

Vue.use(VueRouter)

// const routess = [
//   {
//     path: '/',
//     name: 'Home',
//     component: Home
//   },
//   {
//     path: '/about',
//     name: 'About',
//     // route level code-splitting
//     // this generates a separate chunk (about.[hash].js) for this route
//     // which is lazy-loaded when the route is visited.
//     component: () => import(/* webpackChunkName: "about" */ '../views/About.vue')
//   }
// ]

var allRoutes = []
allRoutes = allRoutes.concat(LoginRoutes, AdminRoutes, {
	path: '/',
	name: 'Home',
	component: Home
})

const routes = allRoutes

const router = new VueRouter({
	mode: 'history',
	base: process.env.BASE_URL,
	routes
})

export default router
