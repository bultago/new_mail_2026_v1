import { ref, onMounted } from 'vue'

export type Theme = 'default' | 'modern' | 'dark'

const currentTheme = ref<Theme>('default')

export function useTheme() {
    const setTheme = (theme: Theme) => {
        currentTheme.value = theme
        localStorage.setItem('theme', theme)
        applyTheme(theme)
    }

    const applyTheme = (theme: Theme) => {
        const root = document.documentElement

        // Reset classes
        root.classList.remove('dark', 'theme-modern')

        if (theme === 'dark') {
            root.classList.add('dark')
        } else if (theme === 'modern') {
            root.classList.add('theme-modern')
        }
    }

    const initTheme = () => {
        const savedTheme = localStorage.getItem('theme') as Theme | null
        if (savedTheme) {
            setTheme(savedTheme)
        } else {
            setTheme('default')
        }
    }

    return {
        currentTheme,
        setTheme,
        initTheme
    }
}
