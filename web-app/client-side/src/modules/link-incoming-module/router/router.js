import linkIncoming from "../views/linkIncoming-approval"
import linkIncomingParent from "../views/parent-incoming-view"

const routes = [
    {
        path: '/linkIncoming-approval/:taskId/',
        name: 'linkIncoming-approval',
        component: linkIncoming
    },
    {
        path: '/linkIncoming-parent/:entityId/',
        name: 'linkIncoming-parent',
        component: linkIncomingParent
    }
]

export default routes
