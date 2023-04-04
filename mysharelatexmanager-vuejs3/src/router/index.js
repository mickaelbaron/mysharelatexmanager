import { createRouter, createWebHistory } from 'vue-router'

import SignIn from '../components/SignIn.vue'
import RootEditor from '../components/RootEditor.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/users'
    },
    {
      path: '/login',
      name: 'SignIn',
      component: SignIn
    },
    {
      path: '/users',
      name: 'UsersEditor',
      component: RootEditor
    },
    {
      path: '/projects',
      name: 'ProjectsEditor',
      component: RootEditor
    }
  ]
})

export default router
