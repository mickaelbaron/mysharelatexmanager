<script setup>
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
defineEmits(['AboutPopup', 'LogOut', 'FilterContent', 'Test'])

const router = useRouter()
const route = useRoute()
const filterContent = ref('')

function changeRoute(value) {
  filterContent.value = ''
  router.push({ name: value })
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
            <li class="nav-item">
              <a
                class="nav-link"
                :class="{ active: route.name === 'UsersEditor' }"
                href="#"
                @click="changeRoute('UsersEditor')"
                >Users</a
              >
            </li>
            <li class="nav-item">
              <a
                class="nav-link"
                :class="{ active: route.name === 'ProjectsEditor' }"
                href="#"
                @click="changeRoute('ProjectsEditor')"
                >Projects</a
              >
            </li>
            <li class="nav-item">
              <a href="#" class="nav-link" @click="$emit('AboutPopup')"
                >About</a
              >
            </li>
            <li class="nav-item">
              <a href="#" class="nav-link" @click="$emit('LogOut')">Logout</a>
            </li>
          </ul>
          <form
            class="d-flex"
            role="search"
            @submit.prevent="$emit('FilterContent', filterContent)"
          >
            <input
              v-model="filterContent"
              class="form-control me-2"
              type="search"
              placeholder="Search"
              aria-label="Search"
            />
            <button class="btn btn-outline-success" type="submit">
              Search
            </button>
          </form>
        </div>
      </div>
    </nav>
  </div>
</template>
