import InitStep from '../views/process-init'
import ProcessStep from '../views/process-step-RG'
import ProcessStepRQ from '../views/process-step-RQ'
import ProcessStepRE from '../views/process-step-RE'

const routes = [
    {
        path: '/init-process',
        name: 'initiation-step',
        component: InitStep
    },
    {
        path: '/process-step-RG/:taskId/',
        name: 'process-step-RG',
        component: ProcessStep,
    }, {
        path: '/process-step-RQ/:taskId/',
        name: 'process-step-RQ',
        component: ProcessStepRQ
    }, {
        path: '/process-step-RE/:taskId/',
        name: 'process-step-RE',
        component: ProcessStepRE
    },
]

export default routes
