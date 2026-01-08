import { ref } from 'vue'

export type Theme = 'default' | 'modern' | 'dark'

const currentTheme = ref<Theme>('default')

export function useTheme() {
    // Internal setter that updates state and DOM but doesn't trigger loop
    const setThemeInternal = (theme: Theme) => {
        currentTheme.value = theme
        applyTheme(theme)
    }

    const setTheme = (theme: Theme) => {
        localStorage.setItem('theme', theme)
        setThemeInternal(theme)
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
            setThemeInternal(savedTheme)
        } else {
            setThemeInternal('default')
        }

        // Listen for changes in other windows
        window.addEventListener('storage', (e) => {
            if (e.key === 'theme' && e.newValue) {
                setThemeInternal(e.newValue as Theme)
            }
        })
    }

    return {
        currentTheme,
        setTheme,
        initTheme
    }
}
