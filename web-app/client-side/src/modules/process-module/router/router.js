import InitStep from '../views/process-init'
import ProcessStep from '../views/process-step'

const routes = [
    {
        path: '/init-process',
        name: 'initiation-step',
        component: InitStep
    },
    {
        path: '/process-step/:taskId/',
        name: 'process-step',
        component: ProcessStep
    },
]

export default routes
