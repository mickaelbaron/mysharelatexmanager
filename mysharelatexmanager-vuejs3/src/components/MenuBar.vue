<script setup>
import { ref } from 'vue'
import { useRoute } from 'vue-router'
const emit = defineEmits({
  AboutPopup: null,
  LogOut: null,
  FilterContent: (value) => typeof value === 'string',
})

const route = useRoute()
const filterContent = ref('')

const navigation = [
  { name: 'UsersEditor', label: 'Users' },
  { name: 'ProjectsEditor', label: 'Projects' },
]

function search() {
  emit('FilterContent', filterContent.value)
}

function showAbout() {
  emit('AboutPopup')
}

function logout() {
  emit('LogOut')
}
</script>

<template>
  <div>
    <nav class="navbar navbar-expand-md navbar-dark fixed-top bg-dark">
      <div class="container-fluid">
        <span class="navbar-brand" href="#">MyShareLaTexManager</span>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarCollapse"
          aria-controls="navbarCollapse"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div id="navbarCollapse" class="collapse navbar-collapse">
          <ul class="navbar-nav me-auto mb-2 mb-md-0">
            <li v-for="item in navigation" :key="item.name" class="nav-item">
              <RouterLink :to="{ name: item.name }" class="nav-link" active-class="active">
                {{ item.label }}
              </RouterLink>
            </li>
            <li class="nav-item">
              <button class="nav-link btn btn-link" type="button" @click="showAbout">About</button>
            </li>
            <li class="nav-item">
              <button class="nav-link btn btn-link" type="button" @click="logout">Logout</button>
            </li>
          </ul>
          <form class="d-flex" role="search" @submit.prevent="search">
            <input
              v-model="filterContent"
              class="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
            />
            <button class="btn btn-outline-success" type="submit">Search</button>
          </form>
        </div>
      </div>
    </nav>
  </div>
</template>
