import Vue from 'vue'
import VueRouter from 'vue-router'
import Home from '../views/Home.vue'
import LoginRoutes from '../modules/login-module/router/router'
import AdminRoutes from '../modules/admin-module/router/router'
import DemoRoutes from '../modules/demo-module/router/router'
import ManualStep from '../modules/manual-step-module/router/router'
import generalProcessRoutes from '../modules/generalProcess-module/router/router'
import incomingRegistrationRoutes from '../modules/incoming-registration-module/router/router'
import lookupRoutes from '../modules/lookup-module/router/router'
import HistoryRoutes from '../modules/history-module/router/router'
import correspondenceRoutes from '../modules/correspondence-data-module/router/router'
import OrgChartRoutes from '../modules/orgChart-module/router/router'
import observable from "../modules/core-module/lib/vue-sub-lib";
import linkingRoutes from '../modules/link-incoming-module/router/router'
import basicSearchRoutes from '../modules/basicSearch-module/router/router'

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

let allRoutes = [];
allRoutes = allRoutes.concat(
    LoginRoutes,
    AdminRoutes,
    DemoRoutes,
    ManualStep,
    generalProcessRoutes,
    incomingRegistrationRoutes,
    lookupRoutes,
    HistoryRoutes,
    correspondenceRoutes,
    OrgChartRoutes,
    linkingRoutes,
    basicSearchRoutes, {
        path: '/',
        name: 'Home',
        component: Home,
    }
)

const routes = allRoutes

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes,
})

router.afterEach(() => {
    observable.observers = {}
})

export default router