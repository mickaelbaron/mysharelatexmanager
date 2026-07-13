<script setup>
import { ref, reactive, onMounted, onBeforeUnmount, computed, watch } from 'vue'
import { Modal } from 'bootstrap'
import GlobalService from '../js/GlobalService.js'

const props = defineProps({
  visible: {
    type: Boolean,
    required: true,
  },
  projectId: {
    type: String,
    required: true,
  },
})
const emit = defineEmits(['update:visible', 'save-new-projectdata'])

const modalRef = ref()
let modal = null

const createEmptyProject = () => ({
  id: '',
  name: '',
  description: '',
  owner: '',
  owner_ref: '',
  collaborators: [],
  active: true,
})

const updatedProject = reactive(createEmptyProject())

const users = ref([])
const selectedCollaborator = ref('')

onMounted(() => {
  if (modalRef.value) {
    modal = new Modal(modalRef.value)
    modalRef.value.addEventListener('hidden.bs.modal', close)
  }
})

onBeforeUnmount(() => {
  modalRef.value?.removeEventListener('hidden.bs.modal', close)
  modal?.dispose()
})

watch(
  () => props.visible,
  (visible) => {
    if (!visible) {
      modal?.hide()
      return
    }

    GlobalService.prepareUpdateProject(
      props.projectId,
      onSuccessPrepareUpdateProject,
      onErrorPrepareUpdateProject,
    )
  },
  {
    immediate: true,
  },
)

watch(
  () => updatedProject.owner_ref,
  (currentValue) => {
    const owner = users.value.find((user) => user.id === currentValue)

    updatedProject.owner = owner?.email ?? ''
  },
)

function onSuccessUpdateProject(data) {
  if (data.status === 200) {
    emit('save-new-projectdata', updatedProject)
  } else if (data.status === 204) {
    emit('save-new-projectdata')
  }

  close()
}

function onErrorUpdateProject(error) {
  // TODO: display error on Toast
  console.log(error)
}

function onSuccessPrepareUpdateProject(data) {
  Object.assign(updatedProject, createEmptyProject(), data.update_project)

  users.value = data.users
    .map((user) => {
      return {
        id: user.id,
        email: user.email,
      }
    })
    .sort((a, b) => {
      if (a.email > b.email) {
        return 1
      }
      if (a.email < b.email) {
        return -1
      }
      return 0
    })
  modal.show()
}

function onErrorPrepareUpdateProject(error) {
  // TODO: display error on Toast
  console.log(error)
}

function applyUpdateProject() {
  GlobalService.updateProject(updatedProject, onSuccessUpdateProject, onErrorUpdateProject)
}

function close() {
  emit('update:visible', false)
}

function onRemoveCollaborators(index) {
  updatedProject.collaborators.splice(index, 1)
}

function addCollaborator() {
  const user = users.value.find((user) => user.id === selectedCollaborator.value)

  if (!user) return

  const exists = updatedProject.collaborators.some((collaborator) => collaborator.id === user.id)

  if (!exists) {
    updatedProject.collaborators.push({
      id: user.id,
      email: user.email,
    })
  }

  // Remet le select sur l'option vide
  selectedCollaborator.value = ''
}

function isCollaboratorSelected(userId) {
  return updatedProject.collaborators.some((collaborator) => collaborator.id === userId)
}
</script>

<template>
  <div ref="modalRef" class="modal fade" tabindex="-1">
    <div class="modal-dialog modal-lg">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title">Project Edition</h5>
          <button type="button" class="btn-close" aria-label="Close" @click="close()"></button>
        </div>
        <form @submit.prevent="applyUpdateProject">
          <div class="modal-body">
            <div class="mb-3">
              <label for="inputId" class="form-label">Id</label>
              <input
                id="inputId"
                :value="updatedProject.id"
                type="text"
                placeholder="Id"
                disabled
                class="form-control"
              />
            </div>
            <div class="mb-3">
              <label for="inputName" class="form-label">Name</label>
              <input
                id="inputName"
                v-model="updatedProject.name"
                type="text"
                class="form-control"
                placeholder="Name"
              />
            </div>
            <div class="mb-3">
              <label for="inputDescription" class="form-label">Description</label>
              <input
                id="inputDescription"
                v-model="updatedProject.description"
                type="text"
                class="form-control"
                placeholder="Description"
              />
            </div>
            <div class="mb-3">
              <label for="owner" class="form-label">Owner</label>
              <select
                id="owner"
                v-model="updatedProject.owner_ref"
                placeholder="Id"
                class="form-control"
              >
                <option v-for="option in users" :key="option.id" :value="option.id">
                  {{ option.email }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <div class="form-check">
                <input
                  id="checkIsActive"
                  v-model="updatedProject.active"
                  class="form-check-input"
                  type="checkbox"
                />
                <label class="form-check-label" for="checkIsActive">Active</label>
              </div>
            </div>
            <div class="mb-3">
              <label for="selectcollaborators" class="form-label">Collaborators</label>
              <select
                id="selectcollaborators"
                v-model="selectedCollaborator"
                placeholder="Id"
                class="form-control"
                @change="addCollaborator"
              >
                <option value="" disabled>Select a collaborator</option>
                <option
                  v-for="user in users"
                  :key="user.id"
                  :value="user.id"
                  :disabled="isCollaboratorSelected(user.id)"
                >
                  {{ user.email }}
                </option>
              </select>
            </div>
            <div class="mb-3">
              <ul>
                <li v-for="(item, index) in updatedProject.collaborators" :key="item.id">
                  <button
                    type="button"
                    class="btn btn-outline-danger ms-2 btn-sm"
                    @click="onRemoveCollaborators(index)"
                  >
                    <i class="bi bi-trash"></i>
                  </button>
                  {{ item.email }}
                </li>
              </ul>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" @click="close()">Close</button>
            <button type="submit" class="btn btn-primary">Save changes</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>
