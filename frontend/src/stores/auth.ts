import { defineStore } from 'pinia';
import api from '@/api/axios';

interface User {
    userId: string;
    email: string;
    name: string;
    domain: string;
    accessToken: string;
}

interface AuthState {
    user: User | null;
    isAuthenticated: boolean;
    getError: string | null;
    loading: boolean;
}

export const useAuthStore = defineStore('auth', {
    state: (): AuthState => ({
        user: JSON.parse(localStorage.getItem('user') || 'null'),
        isAuthenticated: !!localStorage.getItem('accessToken'),
        getError: null,
        loading: false,
    }),

    actions: {
        async login(id: string, pass: string, domain: string) {
            this.loading = true;
            this.getError = null;
            try {
                const response = await api.post('/auth/login', {
                    userId: id,
                    password: pass, // API expects 'password'
                    domain,
                });

                if (response.data.success) {
                    const userData = response.data.data;
                    this.user = userData;
                    this.isAuthenticated = true;

                    // Perist to LocalStorage
                    localStorage.setItem('accessToken', userData.accessToken);
                    localStorage.setItem('user', JSON.stringify(userData));

                    return true;
                } else {
                    throw new Error(response.data.message || 'Login failed');
                }
            } catch (error: any) {
                console.error('Login Error:', error);
                this.getError = error.response?.data?.message || 'Login failed. Please check your credentials.';
                this.isAuthenticated = false;
                return false;
            } finally {
                this.loading = false;
            }
        },

        logout() {
            this.user = null;
            this.isAuthenticated = false;
            localStorage.removeItem('accessToken');
            localStorage.removeItem('user');
            // Ideally redirect via router context, but here we just clear state
        },
    },
});
