import GeneralProcessInit from '../views/generalProcess-init'
import GeneralProcessEarly from '../views/generalProcess-early'
import GeneralProcessMember from '../views/generalProcess-member'

const routes = [
    {
        path: '/generalProcess-init',
        name: 'generalProcess-init',
        component: GeneralProcessInit
    },
    {
        path: '/generalProcess-early/:taskId/',
        name: 'generalProcess-early',
        component: GeneralProcessEarly,
    },{
        path: '/generalProcess-member/:taskId/',
        name: 'generalProcess-member',
        component: GeneralProcessMember
    }
]

export default routes
