<script setup>
import { ref, onMounted, onUnmounted, watch, computed, inject } from 'vue'
import { Modal } from 'bootstrap'
import GlobalService from '../js/GlobalService.js'

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },
  user: {
    type: Object,
    required: true,
  },
})
const emit = defineEmits(['update:visible'])
const errorState = inject('ErrorState')

const owned_projects = ref([])
const collaborators_project = ref([])

const modalRef = ref()
let modal = null
const confirmEmail = ref('')
const deleting = ref(false)

onMounted(() => {
  if (modalRef.value) {
    modal = new Modal(modalRef.value)
    modalRef.value.addEventListener('hidden.bs.modal', close)
  }
})

onUnmounted(() => {
  modalRef.value?.removeEventListener('hidden.bs.modal', close)

  modal?.dispose()
})

watch(
  () => props.visible,
  (visible) => {
    if (!modal) return

    if (visible) {
      resetState()
      modal.show()
    } else {
      modal.hide()
    }
  },
  { immediate: true },
)

const canDelete = computed(() => {
  return owned_projects.value.length === 0 && collaborators_project.value.length === 0
})

function resetState() {
  owned_projects.value = []
  collaborators_project.value = []
  confirmEmail.value = ''
}

function onSuccessDeleteUser(data) {
  deleting.value = false
  owned_projects.value = data.owned_projects
  collaborators_project.value = data.collaborators_project

  if (canDelete.value) {
    close()
  }
}

function onErrorDeleteUser(error) {
  deleting.value = false
  errorState.methods.setErrorCode(error)
}

function applyDeleteUser() {
  deleting.value = true
  GlobalService.deleteUser(props.user.id, onSuccessDeleteUser, onErrorDeleteUser)
}

function close() {
  emit('update:visible', false)
}
</script>

<template>
  <div
    id="aboutPopupModal"
    ref="modalRef"
    class="modal fade"
    tabindex="-1"
    aria-labelledby="exampleModalLabel"
    aria-hidden="true"
  >
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h1 id="exampleModalLabel" class="modal-title fs-5">Delete User Confirmation</h1>
          <button type="button" class="btn-close" aria-label="Close" @click="close()"></button>
        </div>
        <div class="modal-body">
          <div v-if="canDelete">
            <p class="my-4">
              You are about to <b>delete</b> this user {{ user.email }}, are you absolutely sure?
            </p>
            <p class="my-4">Enter the following to confirm: {{ user.email }}</p>
            <input id="inputConfirmEmail" v-model="confirmEmail" type="text" class="form-control" />
          </div>
          <div v-else>
            <p class="my-4">Cannot <b>delete</b> {{ user.email }} for these reasons:</p>
            <div class="alert alert-danger" role="alert">
              <div v-if="owned_projects.length !== 0">
                <p><strong>Owner of</strong></p>
                <ul>
                  <li v-for="current in owned_projects" :key="current.id">
                    {{ current.name }}
                  </li>
                </ul>
              </div>
              <div v-if="collaborators_project.length !== 0">
                <p><strong>Collaborator of</strong></p>
                <ul>
                  <li v-for="current in collaborators_project" :key="current.id">
                    {{ current.name }}
                  </li>
                </ul>
              </div>
            </div>
            <p>You need to fix these issues before deleting {{ user.email }}.</p>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-secondary" @click="close()">Close</button>
          <button
            v-if="canDelete"
            type="button"
            class="btn btn-primary"
            :disabled="deleting || confirmEmail !== user.email"
            @click="applyDeleteUser()"
          >
            {{ deleting ? 'Deleting...' : 'Delete' }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>
