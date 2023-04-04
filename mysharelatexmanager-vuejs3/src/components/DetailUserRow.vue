<script setup>
defineProps({
  data: {
    type: Object,
    required: true
  }
})
</script>

<template>
  <div>
    <div class="inline field">
      <label>Id: {{ data.id }}</label>
    </div>
    <div class="inline field">
      <label>First name: {{ data.first_name }}</label>
    </div>
    <div class="inline field">
      <label>Last name: {{ data.last_name }}</label>
    </div>
    <div class="inline field">
      <label>Institution: {{ data.institution }}</label>
    </div>
    <div class="inline field">
      <label>Email: {{ data.email }}</label>
    </div>
    <div class="inline field">
      <label>Login Count: {{ data.loginCount }}</label>
    </div>
    <div class="inline field">
      <label>Last Logged In: {{ data.lastLoggedIn }}</label>
    </div>
    <div class="inline field">
      <label>is Admin: {{ data.isAdmin }}</label>
    </div>
  </div>
</template>
