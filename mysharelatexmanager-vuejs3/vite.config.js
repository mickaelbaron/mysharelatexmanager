import { fileURLToPath, URL } from 'node:url'
import { defineConfig, loadEnv } from 'vite'
import packageJson from './package.json'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

export default defineConfig(({ mode }) => {
  process.env = { ...loadEnv(mode, process.cwd()) }

  return {
    plugins: [vue(), vueDevTools()],
    build: {
      sourcemap: 'true',
    },
    resolve: {
      alias: {
        '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
    },
    define: {
      'import.meta.env.PACKAGE_VERSION': JSON.stringify(packageJson.version),
    },
    base: process.env.VITE_APP_SUBPATH,
  }
})
