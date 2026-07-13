<script setup>
const props = defineProps({
  data: {
    type: Object,
    required: true,
  },
})

const fields = [
  ['Id', 'id'],
  ['First name', 'first_name'],
  ['Last name', 'last_name'],
  ['Institution', 'institution'],
  ['Email', 'email'],
  ['Login Count', 'login_count'],
  ['Last Logged In', 'last_logged_in'],
  ['Admin', 'admin'],
]
</script>

<template>
  <div>
    <div v-for="[label, key] in fields" :key="key" class="inline field">
      <label> {{ label }}: {{ props.data[key] }} </label>
    </div>
  </div>
</template>
