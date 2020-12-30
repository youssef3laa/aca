import DemoPage from '../views/demo-page'
import AttachmentDemo from '../views/attachement-demo-page'

const routes = [
    {
        path: '/demo',
        name: 'DemoPage',
        component: DemoPage
    },
    {
        path: '/attachment-demo',
        name: 'Attachment Demo',
        component: AttachmentDemo
    }
]

export default routes
