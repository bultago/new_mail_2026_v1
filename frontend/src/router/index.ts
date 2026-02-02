import { createRouter, createWebHistory } from 'vue-router'
import LoginView from '../views/LoginView.vue'
import MainLayout from '../layouts/MainLayout.vue'
import MailListView from '../views/mail/MailListView.vue'
import { useAuthStore } from '@/stores/auth'

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
                    component: () => import('@/views/mail/MailWriteForm.vue')
                },
                {
                    path: 'result',
                    name: 'mail-result',
                    component: () => import('@/views/mail/MailSendResultView.vue')
                },
                {
                    path: 'read/:id',
                    name: 'mail-read',
                    component: () => import('../views/mail/MailReadView.vue')
                },
                {
                    path: 'receipt',
                    name: 'mail-receipt',
                    component: () => import('../views/mail/MailReceiptView.vue')
                },
                {
                    path: 'receipt/:id',
                    name: 'mail-receipt-detail',
                    component: () => import('../views/mail/MailReceiptDetailView.vue')
                },
                {
                    path: '',
                    redirect: 'list/inbox'
                }
            ]
        },
        {
            path: '/addr',
            component: MainLayout,
            children: [
                {
                    path: 'list',
                    name: 'addr-list',
                    component: () => import('../views/addr/AddressBookView.vue')
                }
            ]
        },
        {
            path: '/schedule',
            component: MainLayout,
            children: [
                {
                    path: '',
                    name: 'schedule-main',
                    component: () => import('../views/calendar/CalendarView.vue'),
                    children: [
                        {
                            path: '',
                            name: 'schedule-month',
                            component: () => import('../components/calendar/CalendarMonth.vue')
                        },
                        {
                            path: 'week',
                            name: 'schedule-week',
                            component: () => import('../components/calendar/CalendarWeek.vue')
                        },
                        {
                            path: 'day',
                            name: 'schedule-day',
                            component: () => import('../components/calendar/CalendarDay.vue')
                        },
                        {
                            path: 'search',
                            name: 'schedule-search',
                            component: () => import('../components/calendar/CalendarSearch.vue')
                        }
                    ]
                }
            ]
        },
        // Board
        {
            path: '/board',
            component: () => import('../layouts/BlankLayout.vue'),
            children: [
                {
                    path: '',
                    name: 'board-list',
                    component: () => import('../views/board/BoardView.vue')
                }
            ]
        },
        // Settings
        {
            path: '/settings',
            component: MainLayout,
            children: [
                {
                    path: '',
                    name: 'settings',
                    component: () => import('../views/SettingsView.vue')
                }
            ]
        },
        // Notification
        {
            path: '/notification',
            component: MainLayout,
            children: [
                {
                    path: '',
                    name: 'notification',
                    component: () => import('@/views/notification/NotificationView.vue')
                }
            ]
        },
        {
            path: '/',
            redirect: '/login'
        },
        {
            path: '/popup',
            component: () => import('../layouts/BlankLayout.vue'),
            children: [
                {
                    path: 'read/:id',
                    name: 'popup-read',
                    component: () => import('../views/mail/MailPopupView.vue')
                },
                {
                    path: 'write',
                    name: 'popup-write',
                    component: () => import('../views/mail/MailPopupWriteView.vue')
                },
                {
                    path: 'address',
                    name: 'popup-address',
                    component: () => import('../views/mail/MailPopupAddressView.vue')
                }
            ]
        }
    ]
})

// Navigation Guard
router.beforeEach((to, from, next) => {
    const authStore = useAuthStore()

    // Public pages that don't need auth
    const publicPages = ['/login']
    const authRequired = !publicPages.includes(to.path)

    if (authRequired && !authStore.isAuthenticated) {
        next('/login')
    } else {
        next()
    }
})

export default router
