import GeneralProcessInit from '../views/generalProcess-init'
import GeneralProcessEarly from '../views/generalProcess-early'
import GeneralProcessResponsible from '../views/generalProcess-responsible'
import GeneralProcessMember from '../views/generalProcess-member'
import GeneralProcessApprovals from '../views/generalProcess-approvals'
import GeneralProcessParallel from '../views/generalProcess-parallel'
import GeneralProcessStepAD from '../views/generalProcess-stepAD'

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
    },
    {
        path: '/generalProcess-responsible/:taskId/',
        name: 'generalProcess-responsible',
        component: GeneralProcessResponsible,
    },{
        path: '/generalProcess-member/:taskId/',
        name: 'generalProcess-member',
        component: GeneralProcessMember
    },{
        path: '/generalProcess-approvals/:taskId/',
        name: 'generalProcess-approvals',
        component: GeneralProcessApprovals
    },{
        path: '/generalProcess-parallel/:taskId/',
        name: 'generalProcess-parallel',
        component: GeneralProcessParallel
    },{
        path: '/generalProcess-stepAD/:taskId/',
        name: 'generalProcess-stepAD',
        component: GeneralProcessStepAD
    }
]

export default routes
