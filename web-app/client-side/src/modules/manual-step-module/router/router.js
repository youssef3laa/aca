import ManualPage from '../views/MT-page'

const routes = [
  {
    path: '/manual-step/:taskId',
    name: 'MT',
    component: ManualPage,
    props: true,
  },
]

export default routes
