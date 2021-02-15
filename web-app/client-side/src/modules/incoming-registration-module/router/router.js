import incomingRegistrationInit from '../views/incoming-registration-init'
import incomingCaseRegistrationComment from '../views/incoming-case-registration-comment'
import incomingCaseRegistrationMemoCreate from '../views/incoming-case-registration-memo-create'
import incomingCaseRegistrationMemoApproval from '../views/incoming-case-registration-approval'
import incomingCaseRegistrationMemoSignature from '../views/incoming-case-registration-signature'
import incomingCaseRegistrationMemoOutcoming from '../views/incoming-case-registration-outcoming'
import incomingCaseRegistrationMemoPerformance from '../views/incoming-case-registration-performance'

const routes = [
    {
        path: '/incoming-registration-init',
        name: 'incomingRegistration-init',
        component: incomingRegistrationInit
    },
    {
        path: '/incoming-case-registration-comment/:taskId/',
        name: 'incomingCaseRegistration-comment',
        component: incomingCaseRegistrationComment
    },
    {
        path: '/incoming-case-registration-memo-create/:taskId/',
        name: 'incomingCaseRegistration-memoCreate',
        component: incomingCaseRegistrationMemoCreate
    },
    {
        path: '/incoming-case-registration-approval/:taskId/',
        name: 'incomingCaseRegistration-approval',
        component: incomingCaseRegistrationMemoApproval
    },
    {
        path: '/incoming-case-registration-signature/:taskId/',
        name: 'incomingCaseRegistration-signature',
        component: incomingCaseRegistrationMemoSignature
    },
    {
        path: '/incoming-case-registration-outcoming/:taskId/',
        name: 'incomingCaseRegistration-outcoming',
        component: incomingCaseRegistrationMemoOutcoming
    },
    {
        path: '/incoming-case-registration-performance/:taskId/',
        name: 'incomingCaseRegistration-performance',
        component: incomingCaseRegistrationMemoPerformance
    }
]

export default routes
