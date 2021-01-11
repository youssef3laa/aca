import InitStep from '../views/process-init'
import ProcessStepEarly from '../views/process-step-early'
import ProcessStepRE from '../views/process-step-RE'
import ProcessStepMember from '../views/process-step-member'

const routes = [
    {
        path: '/init-process',
        name: 'initiation-step',
        component: InitStep
    },
    {
        path: '/process-step-early/:taskId/',
        name: 'process-step-early',
        component: ProcessStepEarly,
    }, {
        path: '/process-step-RE/:taskId/',
        name: 'process-step-RE',
        component: ProcessStepRE
    }, {
        path: '/process-step-MM/:taskId/',
        name: 'process-step-member',
        component: ProcessStepMember
    },
]

export default routes
