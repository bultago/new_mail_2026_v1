import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import MainLayout from '../layouts/MainLayout.vue'
import MailListView from '../views/mail/MailListView.vue'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'login',
            component: LoginView
        },
        {
            path: '/mail',
            component: MainLayout,
            children: [
                {
                    path: 'list/:folder?',
                    name: 'mail-list',
                    component: MailListView
                },
                {
                    path: 'write',
                    name: 'mail-write',
                    component: () => import('../views/mail/MailWriteView.vue')
                },
                {
                    path: 'read/:id',
                    name: 'mail-read',
                    component: () => import('../views/mail/MailReadView.vue')
                },
                {
                    path: '',
                    redirect: 'list/inbox'
                }
            ]
        },
        {
            path: '/',
            redirect: '/login'
        }
    ]
})

export default router
