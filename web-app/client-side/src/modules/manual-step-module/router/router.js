import ManualPage from '../views/MT-page'
import ManualPageTwo from '../views/MT2-page'
import InitPage from '../views/init-page'

const routes = [
    {
        path: '/manual-step/:taskId',
        name: 'manual-step',
        component: ManualPage,
        props: true,
    },
    {
        path: '/manual-step-two/:taskId/:taskUrl',
        name: 'manual-step-two',
        component: ManualPageTwo,
        props: true,
    },
    {
        path: '/init-page',
        name: 'initPage',
        component: InitPage,
        props: true,
    },
]

export default routes
