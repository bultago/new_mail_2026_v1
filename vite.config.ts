import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import tailwindcss from 'tailwindcss'
import autoprefixer from 'autoprefixer'

// https://vite.dev/config/
// Force restart - touched by agent
export default defineConfig({
  css: {
    postcss: {
      plugins: [tailwindcss(), autoprefixer()],
    },
  },
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/common': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/i18n': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/design': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/mail': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        bypass: (req, res, options) => {
          if (req.headers.accept && req.headers.accept.includes('text/html')) {
            return req.url
          }
        }
      },
    }
  }
})
