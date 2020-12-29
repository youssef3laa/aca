import historyView from '../views/history-page.vue'
import historyForm from '../views/history-add-form.vue'
const routes = [{
    path:'/history',
    name: 'historyView',
    component: historyView
    },
    {
    path:'/history/form',
    name: 'historyForm',
    component: historyForm,
    }
]

export default routes