import incomingRegistrationInit from '../views/incoming-registration-init'
import incomingCaseRegistrationComment from '../views/incoming-case-registration-comment'
import incomingCaseRegistrationMemoCreate from '../views/incoming-case-registration-memo-create'
import incomingCaseRegistrationMemoApproval from '../views/incoming-case-registration-approval'
import incomingCaseRegistrationMemoSignature from '../views/incoming-case-registration-signature'
import incomingCaseRegistrationMemoOutcoming from '../views/incoming-case-registration-outcoming'
import incomingCaseRegistrationMemoPerformance from '../views/incoming-case-registration-performance'
import incomingCaseRegistrationRead from '../views/incoming-case-registration-read'

const routes = [
    {
        path: '/incoming-registration-init',
        name: 'incomingRegistration-init',
        component: incomingRegistrationInit
    },
    {
        path: '/case/incoming-case-registration-comment/:taskId/',
        name: 'incomingCaseRegistration-comment',
        component: incomingCaseRegistrationComment
    },
    {
        path: '/case/incoming-case-registration-memo-create/:taskId/',
        name: 'incomingCaseRegistration-memoCreate',
        component: incomingCaseRegistrationMemoCreate
    },
    {
        path: '/case/incoming-case-registration-approval/:taskId/',
        name: 'incomingCaseRegistration-approval',
        component: incomingCaseRegistrationMemoApproval
    },
    {
        path: '/case/incoming-case-registration-signature/:taskId/',
        name: 'incomingCaseRegistration-signature',
        component: incomingCaseRegistrationMemoSignature
    },
    {
        path: '/case/incoming-case-registration-outcoming/:taskId/',
        name: 'incomingCaseRegistration-outcoming',
        component: incomingCaseRegistrationMemoOutcoming
    },
    {
        path: '/case/incoming-case-registration-performance/:taskId/',
        name: 'incomingCaseRegistration-performance',
        component: incomingCaseRegistrationMemoPerformance
    },
    {
        path: '/incoming-case-registration-read/:requestId/',
        name: 'incomingCaseRegistration-read',
        component: incomingCaseRegistrationRead
    }
]

export default routes
