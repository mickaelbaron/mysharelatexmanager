<script setup>
import { ref, provide, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'

import UsersEditor from './UsersEditor.vue'
import ProjectsEditor from './ProjectsEditor.vue'
import AboutPopup from './AboutPopup.vue'
import MenuBar from './MenuBar.vue'
import ErrorToast from './ErrorToast.vue'

import errorState from '../js/ErrorState.js'

provide('ErrorState', errorState)

const router = useRouter()
const route = useRoute()
const aboutVisible = ref(false)

const searchContent = ref('')

onMounted(() => {
  if (localStorage.getItem('TOKEN') === null) {
    router.push({
      name: 'SignIn'
    })
  }
})

function logout() {
  localStorage.removeItem('TOKEN')
  router.push({
    name: 'SignIn'
  })
}

function applySearchContent(data) {
  searchContent.value = data
}
</script>

<template>
  <MenuBar
    @log-out="logout"
    @about-popup="aboutVisible = true"
    @filter-content="applySearchContent"
  />

  <main class="container-fluid editor">
    <ProjectsEditor
      v-if="route.name === 'ProjectsEditor'"
      :search-content="searchContent"
    />
    <UsersEditor
      v-if="route.name === 'UsersEditor'"
      :search-content="searchContent"
    />
  </main>
  <ErrorToast />
  <AboutPopup v-model:visible="aboutVisible" />
</template>

<style>
.editor {
  min-height: 75rem;
  padding-top: 4.5rem;
}
</style>
