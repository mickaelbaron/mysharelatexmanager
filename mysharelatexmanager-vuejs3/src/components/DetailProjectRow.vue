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
      <label>Name: {{ data.name }}</label>
    </div>
    <div class="inline field">
      <label>Description: {{ data.description }}</label>
    </div>
    <div class="inline field">
      <label>Owner Id: {{ data.owner_ref }}</label>
    </div>
    <div class="inline field">
      <label>Owner: {{ data.owner }}</label>
    </div>
    <div class="inline field">
      <label>Collaberators: {{ data.collaberators }}</label>
    </div>
    <div class="inline field">
      <label>is Active: {{ data.active }}</label>
    </div>
  </div>
</template>
