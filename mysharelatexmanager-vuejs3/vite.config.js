import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import packageJson from './package.json'

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  process.env = { ...loadEnv(mode, process.cwd()) }

  return {
    plugins: [vue()],
    base: process.env.VITE_APP_SUBPATH,
    build: {
      sourcemap: 'true'
    },
    define: {
      'import.meta.env.PACKAGE_VERSION': JSON.stringify(packageJson.version)
    }
  }
})
