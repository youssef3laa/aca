import ManualPage from '../views/MT-page'
import InitPage from '../views/init-page'

const routes = [
  {
    path: '/manual-step/:taskId',
    name: 'MT',
    component: ManualPage,
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
