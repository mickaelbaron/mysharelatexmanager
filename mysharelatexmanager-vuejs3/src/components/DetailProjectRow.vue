<script setup>
const props = defineProps({
  project: {
    type: Object,
    required: true,
  },
})

const fields = [
  ['Id', 'id'],
  ['Name', 'name'],
  ['Description', 'description'],
  ['Owner Id', 'owner_ref'],
  ['Owner', 'owner'],
]
</script>

<template>
  <div>
    <div v-for="[label, key] in fields" :key="key" class="inline field">
      <label>{{ label }}: {{ props.project[key] }}</label>
    </div>

    <div class="inline field">
      <label>
        Collaborators:
        {{ props.project.collaborators.join(', ') }}
      </label>
    </div>

    <div class="inline field">
      <label>
        Active:
        {{ props.project.active ? 'Yes' : 'No' }}
      </label>
    </div>
  </div>
</template>
